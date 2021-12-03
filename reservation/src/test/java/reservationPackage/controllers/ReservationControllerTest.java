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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reservationPackage.entities.Reservation;
import reservationPackage.entities.ReservationType;
import reservationPackage.services.ReservationService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private final long reservationId = 1L;
    private final long userId = 1L;
    private final long sportFacilityId = 1L;
    private final String equipmentName = "hockeyStick";
    String validDate = "2099-01-06T17:00:00";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime bookableDate = LocalDateTime.parse("2099-01-06 17:00:00", dateTimeFormatter);

    private final Reservation reservation = new Reservation(ReservationType.EQUIPMENT, userId,
        sportFacilityId, bookableDate);

    @Mock
    ReservationService reservationService;

    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new ReservationController(reservationService)).build();
    }


    // TODO: communication SportsFacilities dependency injection?

//    @Test
//    public void getReservationId() throws Exception{
//        mockMvc.perform(get("/reservation/{reservationId}", reservationId)).andExpect(status().isOk());
//        verify(reservationService).getReservation(1L);
//    }
//
//
//    @Test
//    public void makeEquipmentReservationOKTest() throws Exception {
//        mockMvc.perform(post("/reservation/{userId}/{equipmentName}/{date}/makeEquipmentBooking",
//                userId,
//                equipmentName, validDate))
//            .andExpect(status().isOk())
//            .andExpect((ResultMatcher) content().string(containsString("Equipment reservation "
//            + "was successful!")));
//        verify(reservationService).makeSportFacilityReservation(reservation);
//    }
//
//    @Test
//    public void makeEquipmentReservationDateInThePast() throws Exception {
//        String pastDate = "1990-01-06T17:00:00";
//        mockMvc.perform(post("/reservation/{userId}/{equipmentName}/{date}/makeEquipmentBooking",
//                userId,
//                equipmentName, pastDate))
//            .andExpect(status().isBadRequest())
//            .andExpect((ResultMatcher) content().string(containsString("Date and time has to be after now")))
//            .andDo(MockMvcResultHandlers.print());;
//        verify(reservationService, never()).makeSportFacilityReservation(reservation);
//
//    }
//
//    @Test
//    public void makeEquipmentReservationOutsideOfTimeslotEdgeCase1() throws Exception {
//        String invalidTime = "2099-01-06T15:59:59";
//
//        mockMvc.perform(post("/reservation/{userId}/{equipmentName}/{date}/makeEquipmentBooking",
//                userId,
//                equipmentName, invalidTime))
//            .andExpect(status().isBadRequest())
//            .andDo(MockMvcResultHandlers.print());;
//        verify(reservationService, never()).makeSportFacilityReservation(reservation);
//    }
//
//    @Test
//    public void makeEquipmentReservationOutsideOfTimeslotEdgeCase2() throws Exception {
//        String invalidTime = "2099-01-06T23:00:00";
//
//        mockMvc.perform(post("/reservation/{userId}/{equipmentName}/{date}/makeEquipmentBooking",
//                userId,
//                equipmentName, invalidTime))
//            .andExpect(status().isBadRequest())
//            .andExpect((ResultMatcher) content().string("hi"))
//            .andDo(MockMvcResultHandlers.print());
//        verify(reservationService, never()).makeSportFacilityReservation(reservation);
//    }
//
//    @Test
//    public void makeEquipmentReservationInsideOfTimeslotEdgeCase1() throws Exception {
//        String time = "2099-01-06T16:00:00";
//
//        mockMvc.perform(post("/reservation/{userId}/{equipmentName}/{date}/makeEquipmentBooking",
//                userId,
//                equipmentName, time))
//            .andExpect(status().isOk())
//            .andExpect((ResultMatcher) content().string(containsString("Equipment reservation "
//                + "was successful!")));
//        verify(reservationService).makeSportFacilityReservation(reservation);
//    }
//
//    @Test
//    public void makeEquipmentReservationInsideOfTimeslotEdgeCase2() throws Exception {
//        String time = "2099-01-06T22:59:59";
//
//        mockMvc.perform(post("/reservation/{userId}/{equipmentName}/{date}/makeEquipmentBooking",
//                userId, equipmentName, time))
//            .andExpect(status().isOk())
//            .andExpect((ResultMatcher) content().string(containsString("Equipment reservation "
//                + "was successful!")));
//        verify(reservationService).makeSportFacilityReservation(reservation);
//    }

    // TODO: test ReservationRepository and service (wait for Arslan to push)
//
//    @Test
//    public void basicCustomerReservationBelowCapacity() throws Exception {
//
//        mockMvc.perform(post("/reservation/{userId}/{equipmentName}/{date}/makeEquipmentBooking",
//            2L, equipmentName, validDate))
//            .andExpect(status().isOk())
//            .andExpect((ResultMatcher) content().string(containsString("Equipment reservation "
//                + "was successful!")));
//        verify(reservationService).makeSportFacilityReservation(reservation);
//        verify(reservationService).getUserReservationCountOnDay(validDate, 2L);
//    }
//




}
