package reservation.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.services.ReservationService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ReservationControllerTest {

    private final transient long reservationId = 1L;
    private final transient long userId = 1L;
    private final transient long sportFacilityId = 1L;
    private final transient String equipmentNameValid = "hockeyStick";
    private final transient String equipmentNameInvalid = "blopp";
    private final transient String validDate = "2099-01-06T17:00:00";
    transient String equipmentBookingUrl =
        "/reservation/{userId}/{equipmentName}/{date}/{isCombined}/makeEquipmentBooking";
    transient DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    transient LocalDateTime bookableDate =
        LocalDateTime.parse("2099-01-06 17:00:00", dateTimeFormatter);
    private final transient Reservation reservation =
        new Reservation(ReservationType.EQUIPMENT, userId, sportFacilityId, bookableDate, true);
    @Mock
    transient ReservationService reservationService;
    transient RestTemplate restTemplate;
    @Autowired
    private transient MockMvc mockMvc;

    private static Stream<Arguments> invalidDateGenerator() {
        return Stream.of(
            //just before you can't reserve
            Arguments.of("2099-01-06T15:59:59"),
            //just after you can't reserve
            Arguments.of("2099-01-06T23:00:00"));
    }

    private static Stream<Arguments> validDateGenerator() {
        return Stream.of(
            //valid date
            Arguments.of("2099-01-06T21:00:00"),
            //date a minute before you can't reserve
            Arguments.of("2099-01-06T22:59:59"),
            //just when you can start reserving
            Arguments.of("2099-01-06T16:00:00"));
    }

    /**
     * Sets up the tests.
     */
    @BeforeEach
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);

        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new ReservationController(reservationService)).build();
    }

    @Test
    public void getReservationId() throws Exception {
        mockMvc.perform(get("/reservation/{reservationId}", reservationId))
            .andExpect(status().isOk());
        verify(reservationService).getReservation(1L);
    }

    /**
     * Test equipment reservation with invalid dates.
     *
     * @param date the date
     * @throws Exception the mockito exception
     */
    @ParameterizedTest
    @MethodSource("invalidDateGenerator")
    public void testEquipmentReservationInvalidDates(String date) throws Exception {

        MvcResult result =
            mockMvc.perform(post(equipmentBookingUrl, userId, equipmentNameValid, date, true))
                .andExpect(status().is4xxClientError()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(
            "Time has to be between " + "16:00 and 23:00");
        verify(reservationService, never()).makeSportFacilityReservation(reservation);

    }

    /**
     * Test equipment reservation with valid dates.
     *
     * @param date the date
     * @throws Exception that mockito throws
     */
    @ParameterizedTest
    @MethodSource("validDateGenerator")
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testEquipmentReservationValidDates(String date) throws Exception {
        Mockito.when(restTemplate.getForObject(
            ReservationController.sportFacilityUrl + "/equipment/" + equipmentNameValid
                + "/getAvailableEquipment", Long.class)).thenReturn(1L);

        Mockito.when(restTemplate.getForObject(
                ReservationController.userUrl + "/user/" + userId + "/isPremium", Boolean.class))
            .thenReturn(true);

        MvcResult result =
            mockMvc.perform(post(equipmentBookingUrl, userId, equipmentNameValid, date, true))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Equipment reservation was successful!");
        verify(reservationService).makeSportFacilityReservation(reservation);
    }

}
