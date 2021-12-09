package reservationPackage.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import reservationPackage.entities.Reservation;
import reservationPackage.entities.ReservationType;
import reservationPackage.services.ReservationService;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final long userId = 1L;
    private final long sportFacilityId = 1L;
    private final String equipmentNameValid = "hockeyStick";
    private final String equipmentNameInvalid = "blopp";

    String equipmentBookingURL = "/reservation/{userId}/{equipmentName"
        + "}/{date}/{isCombined}/makeEquipmentBooking";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime bookableDate = LocalDateTime.parse("2099-01-06 17:00:00", dateTimeFormatter);

    private final Reservation reservation = new Reservation(ReservationType.EQUIPMENT, userId,
        sportFacilityId, bookableDate);

    @Mock
    ReservationService reservationService;

    RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);

        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new ReservationController(reservationService)).build();
    }

    @Test
    public void getReservationIdTest() throws Exception{
        long reservationId = 1L;
        mockMvc.perform(get("/reservation/{reservationId}", reservationId)).andExpect(status().isOk());
        verify(reservationService).getReservation(1L);
    }

    @Test
    public void makeEquipmentReservationDateInThePastTest() throws Exception {
        String pastDate = "1990-01-06T17:00:00";
        MvcResult result = mockMvc.perform(post(equipmentBookingURL,
            userId, equipmentNameValid, pastDate, true))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Date and time has to be "
            + "after now");

        verify(reservationService, never()).makeSportFacilityReservation(reservation);
    }

    @ParameterizedTest
    @MethodSource("invalidDateGenerator")
    public void testEquipmentReservationInvalidDates(String date) throws Exception {

        MvcResult result = mockMvc.perform(post(equipmentBookingURL,
                userId, equipmentNameValid, date, true))
            .andExpect(status().is4xxClientError())
            .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Time has to be between "
            + "16:00 and 23:00");
        verify(reservationService, never()).makeSportFacilityReservation(reservation);

    }

    private static Stream<Arguments> invalidDateGenerator() {
        return Stream.of(
            //just before you can't reserve
            Arguments.of ("2099-01-06T15:59:59"),
            //just after you can't reserve
            Arguments.of ("2099-01-06T23:00:00"));

    }

    @ParameterizedTest
    @MethodSource("validDateGenerator")
    public void testEquipmentReservationValidDates(String date) throws Exception {
        Mockito.when(restTemplate.getForObject(ReservationController.sportFacilityUrl +
                "/equipment/" + equipmentNameValid + "/getAvailableEquipment",
            Long.class)).thenReturn(1L);

        Mockito.when(restTemplate.getForObject(ReservationController.userUrl +
            "/user/" + userId + "/isPremium", Boolean.class)).thenReturn(true);

        MvcResult result = mockMvc.perform(post(equipmentBookingURL,
                userId, equipmentNameValid, date, true))
            .andExpect(status().isOk())
            .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Equipment "
            + "reservation was successful!");
        verify(reservationService).makeSportFacilityReservation(reservation);

    }

    private static Stream<Arguments> validDateGenerator() {
        return Stream.of(
            //valid date
            Arguments.of ("2099-01-06T21:00:00"),
            //date a minute before you can't reserve
            Arguments.of ("2099-01-06T22:59:59"),
            //just when you can start reserving
            Arguments.of ("2099-01-06T16:00:00"));
    }

}
