package nl.tudelft.sem.reservation.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
import nl.tudelft.sem.reservation.entities.Reservation;
import nl.tudelft.sem.reservation.entities.ReservationType;
import nl.tudelft.sem.reservation.entities.chainofresponsibility.InvalidReservationException;
import nl.tudelft.sem.reservation.entities.chainofresponsibility.ReservationChecker;
import nl.tudelft.sem.reservation.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReservationControllerTest {

    private final transient long reservationId = 1L;
    private final transient long invalidId = 13L;
    private final transient long userId = 1L;
    private final transient long lessonId = 1L;
    private final transient long groupId = 1L;
    private final transient long sportFacilityId = 1L;
    private final transient String equipmentNameValid = "hockeyStick";
    private final transient Boolean madeByPremiumUser = true;

    private final transient String equipmentNameInvalid = "blopp";

    private final transient String validDate = "2099-01-06T17:00:00";

    private final transient String sportFacilityUrl = "http://eureka-sport-facilities";
    @Mock
    private final transient SportFacilityCommunicator sportFacilityCommunicator;
    @Mock
    private final transient UserFacilityCommunicator userFacilityCommunicator;
    transient String equipmentBookingUrl =
        "/reservation/{userId}/{equipmentName}/{date}/{madeByPremiumUser}/makeEquipmentBooking";
    transient String sportRoomBookingUrl =
        "/reservation/{userId}/{groupId}/{sportRoomId}/{date}/{madeByPremiumUser}"
            + "/makeSportRoomBooking";
    transient String lessonBookingUrl =
            "/reservation/{userId}/{lessonId}/makeLessonBooking";
    transient DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    transient LocalDateTime bookableDate =
        LocalDateTime.parse("2099-01-06 17:00:00", dateTimeFormatter);
    private final transient Reservation reservation =
        new Reservation(ReservationType.EQUIPMENT, "hockey", userId, sportFacilityId, bookableDate,
            madeByPremiumUser);
    @Mock
    transient ReservationService reservationService;
    @Mock
    transient ReservationChecker reservationChecker;
    @Mock
    transient RestTemplate restTemplate;
    @Autowired
    private transient MockMvc mockMvc;

    private transient ReservationController controller;

    public ReservationControllerTest() {
        this.sportFacilityCommunicator = mock(SportFacilityCommunicator.class);
        this.userFacilityCommunicator = mock(UserFacilityCommunicator.class);
    }

    /**
     * Sets up the tests.
     */
    @BeforeEach
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void setup() {
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);
        this.mockMvc = MockMvcBuilders.standaloneSetup(
            new ReservationController(reservationService, reservationChecker)).build();
        controller = new ReservationController(reservationService, reservationChecker);
    }

    @Test
    public void getSportFacilityCommunicatorTest() {
        assertNotNull(controller.getSportFacilityCommunicator());
    }

    @Test
    public void getUserFacilityCommunicatorTest() {
        assertNotNull(controller.getUserFacilityCommunicator());
    }

    @Test
    public void makeSportRoomReservationParseExceptionTest() throws Exception {
        String date = "invalidDateString";
        mockMvc.perform(
                post(sportRoomBookingUrl, userId, groupId, sportFacilityId, date,
                    madeByPremiumUser))
            .andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    public void makeSportRoomReservationHttpClientExceptionTest() throws Exception {
        String methodSpecificUrl = "/getSportRoomServices/" + sportFacilityId + "/getName";

        when(restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl,
            String.class)).thenThrow(HttpClientErrorException.class);

        mockMvc.perform(post(sportRoomBookingUrl, userId, groupId, sportFacilityId, bookableDate,
            madeByPremiumUser)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void makeSportRoomReservationExceptionTest() throws Exception {
        Mockito.doThrow(InvalidReservationException.class).when(reservationChecker)
            .checkReservation(any(), any());
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(
            ResponseEntity.ok("" + sportFacilityId));

        mockMvc.perform(post(sportRoomBookingUrl, userId, groupId, sportFacilityId, bookableDate,
            madeByPremiumUser)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void makeSportRoomReservationTest() throws Exception {
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(
            ResponseEntity.ok("" + sportFacilityId));
        doNothing().when(reservationChecker).checkReservation(any(), any());

        MvcResult result = mockMvc.perform(post(sportRoomBookingUrl, userId, groupId, sportFacilityId, bookableDate,
            madeByPremiumUser)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Reservation successful!");
    }

    @Test
    public void makeEquipmentHttpExceptionTest() throws Exception {
        doNothing().when(reservationChecker).checkReservation(any(), any());
        when(restTemplate.getForEntity(anyString(), any())).thenThrow(
            HttpClientErrorException.class);

        mockMvc.perform(
                post(equipmentBookingUrl, userId, sportFacilityId, bookableDate, madeByPremiumUser))
            .andExpect(status().isOk()).andReturn();

        reservation.setSportFacilityReservedId(-1L);

        verify(reservationChecker, times(1)).checkReservation(eq(reservation), any());

    }

    @Test
    public void makeEquipmentParseExceptionTest() throws Exception {
        String invalidDate = "invalidDateString";

        mockMvc.perform(
                post(equipmentBookingUrl, userId, sportFacilityId, invalidDate, madeByPremiumUser))
            .andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    public void makeEquipmentReservationExceptionTest() throws Exception {
        doThrow(InvalidReservationException.class).when(reservationChecker)
            .checkReservation(any(), any());
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(
            ResponseEntity.ok("" + sportFacilityId));

        mockMvc.perform(
                post(equipmentBookingUrl, userId, sportFacilityId, bookableDate, madeByPremiumUser))
            .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void makeEquipmentReservationTest() throws Exception {
        doNothing().when(reservationChecker).checkReservation(any(), any());
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(
            ResponseEntity.ok("" + sportFacilityId));

        MvcResult result = mockMvc.perform(
                        post(equipmentBookingUrl, userId, sportFacilityId, bookableDate, madeByPremiumUser))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Reservation successful!");
    }

    @Test
    public void setSportRoomMinimumCapacityTest() {
        Mockito.when(restTemplate.getForEntity(
            sportFacilityUrl + "/setSportRoomServices/" + equipmentNameValid + "/get",
            String.class)).thenReturn(ResponseEntity.of(Optional.of(String.valueOf(1L))));
    }

    @Test
    public void getReservationTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        verify(reservationService).getReservation(reservationId);

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Successful!");
    }

    @Test
    public void getInvalidReservationTest() throws Exception {
        when(reservationService.getReservation(any())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/reservation/{reservationId}", reservationId))
            .andExpect(status().isBadRequest());
        verify(reservationService).getReservation(reservationId);
    }

    @Test
    public void deleteInvalidReservationTest() throws Exception {
        when(reservationService.deleteReservation(anyLong())).thenThrow(
            NoSuchElementException.class);

        mockMvc.perform(delete("/reservation/{reservationId}", reservationId))
            .andExpect(status().isBadRequest());
        verify(reservationService).deleteReservation(reservationId);
    }

    @Test
    public void deleteReservationTest() throws Exception {
        MvcResult result = mockMvc.perform(delete("/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
        verify(reservationService).deleteReservation(reservationId);

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Successful!");
    }

    @Test
    public void lastPersonThatUsedEquipmentTest() throws Exception {
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

    @Test
    public void lastPersonThatUsedEquipmentThrowsExceptionTest() throws Exception {
        when(reservationService.getLastPersonThatUsedEquipment(invalidId)).thenThrow(
            new NoSuchElementException());

        mockMvc.perform(
                get("/reservation/{equipmentId" + "}/lastPersonThatUsedEquipment", invalidId))
            .andExpect(status().isBadRequest()).andReturn();

    }

//
//    @Test
//    public void makeLessonReservationTest() throws Exception {
//        when(sportFacilityCommunicator.getLessonName(anyLong())).thenReturn("Spinning");
//        when(sportFacilityCommunicator.getLessonBeginning(anyLong()))
//                .thenReturn(LocalDateTime.of(2022, 01, 01, 12, 00));
//        when(userFacilityCommunicator.getUserIsPremium(anyLong())).thenReturn(true);
//
//        MvcResult result = mockMvc
//                .perform(post(lessonBookingUrl, userId, lessonId))
//                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
//
//        assertThat(result.getResponse().getContentAsString()).isEqualTo("Reservation successful!");
//    }

//    @Test
//    public void makeLessonReservationFalseNameTest() throws Exception {
//        when(sportFacilityCommunicator.getLessonName(anyLong()))
//                .thenThrow(HttpClientErrorException.class);
//        mockMvc
//                .perform(post("/reservation/{userId}/{lessonId}/makeLessonBooking",
//                        userId, lessonId))
//                .andExpect(status().isBadRequest()).andReturn();
//    }

//    @Disabled
//    public void makeLessonReservationPremiumUserTest() throws Exception {
//        when(restTemplate.getForEntity(anyString(), any()))
//            .thenReturn(ResponseEntity.ok("1999-01" + "-06T00:00:00"));
//        when(userFacilityCommunicator.getUserIsPremium(anyLong()))
//            .thenThrow(HttpClientErrorException.class);
//        mockMvc
//            .perform(post("/reservation/{userId}/{lessonId}/makeLessonBooking",
//            userId, lessonId))
//            .andExpect(status().isBadRequest()).andReturn();
//    }
//


}
