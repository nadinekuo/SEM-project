package reservation.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
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
import org.springframework.http.ResponseEntity;
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
    private final transient long groupId = 1L;
    private final transient long sportFacilityId = 1L;
    private final transient String equipmentNameValid = "hockeyStick";

    private final transient String equipmentNameInvalid = "blopp";

    private final transient String validDate = "2099-01-06T17:00:00";

    transient String equipmentBookingUrl =
        "/reservation/{userId}/{equipmentName}/{date}/makeEquipmentBooking";

    transient String sportRoomBookingUrl =
        "/reservation/{userId}/{groupId}/{sportRoomId}/{date}/makeSportRoomBooking";

    transient String lessonBookingUrl =
        "/reservation/{userId}/{groupId}/{sportRoomId}/{date}/makeSportRoomBooking";

    transient DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    transient LocalDateTime bookableDate =
        LocalDateTime.parse("2099-01-06 17:00:00", dateTimeFormatter);

    private final transient Reservation reservation =
        new Reservation(ReservationType.EQUIPMENT, "hockey", userId, sportFacilityId, bookableDate);

    /**
     * The Reservation service.
     */
    @Mock
    transient ReservationService reservationService;
    /**
     * The Rest template.
     */
    @Mock
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
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);

        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new ReservationController(reservationService)).build();
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

        Mockito.when(restTemplate.getForEntity(
            ReservationController.sportFacilityUrl + "/equipment/" + equipmentNameValid
                + "/getAvailableEquipment", String.class))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(1L))));

        MvcResult result =
            mockMvc.perform(post(equipmentBookingUrl, userId, equipmentNameValid, date))
                .andExpect(status().is4xxClientError()).andReturn();

        assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Reservation could not be made.");
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

        Mockito.when(restTemplate.getForEntity(
            ReservationController.sportFacilityUrl + "/equipment/" + equipmentNameValid
                + "/getAvailableEquipment", String.class))
            .thenReturn(ResponseEntity.of(Optional.of("1")));

        given(reservationService.checkReservation(any(), any())).willReturn(true);

        MvcResult result =
            mockMvc.perform(post(equipmentBookingUrl, userId, equipmentNameValid, date))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Reservation successful!");
        verify(reservationService).makeSportFacilityReservation(reservation);
    }

    /**
     * Test sport room reservation invalid dates.
     *
     * @param date the date
     * @throws Exception the exception
     */
    @ParameterizedTest
    @MethodSource("invalidDateGenerator")
    public void testSportRoomReservationInvalidDates(String date) throws Exception {

        MvcResult result =
            mockMvc.perform(post(sportRoomBookingUrl, userId, groupId, sportFacilityId, date))
                .andExpect(status().is4xxClientError()).andReturn();

        assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Reservation could not be made.");
        verify(reservationService, never()).makeSportFacilityReservation(reservation);

    }

    /**
     * Test sport room reservation valid dates.
     *
     * @param date the date
     * @throws Exception the exception
     */
    @ParameterizedTest
    @MethodSource("validDateGenerator")
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testSportRoomReservationValidDates(String date) throws Exception {

        given(reservationService.checkReservation(any(), any())).willReturn(true);

        MvcResult result =
            mockMvc.perform(post(sportRoomBookingUrl, userId, groupId, sportFacilityId, date))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Reservation successful!");
        verify(reservationService).makeSportFacilityReservation(reservation);

    }

    /**
     * Gets reservation by id.
     *
     * @throws Exception the exception
     */
    @Test
    public void getReservation() throws Exception {
        mockMvc.perform(get("/reservation/{reservationId}", reservationId))
            .andExpect(status().isOk());
        verify(reservationService).getReservation(reservationId);
    }

    /**
     * Gets invalid reservation by id.
     *
     * @throws Exception the exception
     */
    @Test
    public void getInvalidReservation() throws Exception {
        when(reservationService.getReservation(any())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/reservation/{reservationId}", reservationId))
            .andExpect(status().isBadRequest());
        verify(reservationService).getReservation(reservationId);
    }

    /**
     * Deletes invalid reservation by id.
     *
     * @throws Exception the exception
     */
    @Test
    public void deleteInvalidReservation() throws Exception {
        when(reservationService.deleteReservation(anyLong()))
            .thenThrow(NoSuchElementException.class);

        mockMvc.perform(delete("/reservation/{reservationId}", reservationId))
            .andExpect(status().isBadRequest());
        verify(reservationService).deleteReservation(reservationId);
    }

    /**
     * Deletes reservation by id.
     *
     * @throws Exception the exception
     */
    @Test
    public void deleteReservation() throws Exception {
        mockMvc.perform(delete("/reservation/{reservationId}", reservationId))
            .andExpect(status().isOk());
        verify(reservationService).deleteReservation(reservationId);
    }

    /**
     * Test lesson reservation lesson id does not exist.
     *
     * @throws Exception the exception
     */
    @Disabled
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

    /**
     * Test last person that used equipment.
     *
     * @throws Exception the exception
     */
    @Test
    public void testLastPersonThatUsedEquipment() throws Exception {
        Mockito.when(reservationService.getLastPersonThatUsedEquipment(2L)).thenReturn(1L);

        MvcResult result =
            mockMvc.perform(get("/reservation/{equipmentId" + "}/lastPersonThatUsedEquipment", 2L))
                .andExpect(status().isOk()).andReturn();

        try {
            assertThat(result.getResponse().getContentAsString()).isEqualTo("<Long>1</Long>");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // Tests communication with other microservices
    // TODO: to be tested from the new helper class created (not as mockMVC here)

    //    /**
    //     * Gets user is premium.
    //     *
    //     * @throws Exception the exception
    //     */
    //    @Test
    //    public void getUserIsPremium() throws Exception {
    //
    //        Mockito.when(restTemplate
    //            .getForObject(ReservationController.userUrl + "/user/" + userId +
    //            "/isPremium",
    //                Boolean.class)).thenReturn(true);
    //
    //    }

}
