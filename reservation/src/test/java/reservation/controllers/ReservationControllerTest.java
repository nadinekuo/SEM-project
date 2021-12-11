package reservation.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

/**
 * The type Reservation controller test.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ReservationControllerTest {

    private final transient long reservationId = 1L;
    private final transient long userId = 1L;
    private final transient long sportFacilityId = 1L;
    private final transient String equipmentNameValid = "hockeyStick";

    /**
     * The Equipment booking url.
     */
    transient String equipmentBookingUrl =
        "/reservation/{userId}/{equipmentName}/{date}/{isCombined}/makeEquipmentBooking";

    /**
     * The Lesson booking url.
     */
    transient String lessonBookingUrl = "/reservation/{userId}/{lessonId}/makeLessonBooking";

    /**
     * The Date time formatter.
     */
    transient DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * The Bookable date.
     */
    transient LocalDateTime bookableDate =
        LocalDateTime.parse("2099-01-06 17:00:00", dateTimeFormatter);
    private final transient Reservation reservation =
        new Reservation(ReservationType.EQUIPMENT, userId, sportFacilityId, bookableDate, true);
    /**
     * The Reservation service.
     */
    @Mock
    transient ReservationService reservationService;
    /**
     * The Rest template.
     */
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
     * Sets .
     */
    @BeforeEach
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(ReservationService.restTemplate()).thenReturn(restTemplate);

        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new ReservationController(reservationService)).build();
    }

    /**
     * Gets reservation id.
     *
     * @throws Exception the exception
     */
    @Test
    public void getReservationId() throws Exception {
        mockMvc.perform(get("/reservation/{reservationId}", reservationId))
            .andExpect(status().isOk());
        verify(reservationService).getReservation(1L);
    }

    /**
     * Test equipment reservation invalid dates.
     *
     * @param date the date
     * @throws Exception the exception
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
     * Test equipment reservation valid dates.
     *
     * @param date the date
     * @throws Exception the exception
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

        assertThat(result.getResponse().getContentAsString()).isEqualTo(
            "Equipment reservation was successful!");
        verify(reservationService).makeSportFacilityReservation(reservation);
    }

    /**
     * Test equipment reservation past date.
     *
     * @throws Exception the exception
     */
    @Test
    public void testEquipmentReservationPastDate() throws Exception {
        String oldDate = "1999-01-06T21:00:00";

        MvcResult result =
            mockMvc.perform(post(equipmentBookingUrl, userId, equipmentNameValid, oldDate, true))
                .andExpect(status().is4xxClientError()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(
            "Date and time has to be after now");
        verify(reservationService, never()).makeSportFacilityReservation(reservation);

    }

    /**
     * Test lesson reservation.
     *
     * @throws Exception the exception
     */
    @Test
    public void testLessonReservation() throws Exception {

        Mockito.when(restTemplate.getForObject(
            ReservationController.sportFacilityUrl + "/lesson/" + 1 + "/getStartingTime",
            String.class)).thenReturn("1999-01-06T21:00:00");

        MvcResult result =
            mockMvc.perform(post(lessonBookingUrl, userId, 1L)).andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(
            "Lesson booking was successful!");
        verify(reservationService).makeSportFacilityReservation(reservation);

    }

    /**
     * Test lesson reservation lesson id does not exist.
     *
     * @throws Exception the exception
     */
    @Test
    public void testLessonReservationLessonIdDoesNotExist() throws Exception {

        Mockito.when(restTemplate.getForObject(
            ReservationController.sportFacilityUrl + "/lesson/" + -1 + "/getStartingTime",
            String.class)).thenReturn(null);

        MvcResult result =
            mockMvc.perform(post(lessonBookingUrl, userId, -1)).andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Lesson doesn't exist");
        verify(reservationService, times(0)).makeSportFacilityReservation(reservation);

    }

}
