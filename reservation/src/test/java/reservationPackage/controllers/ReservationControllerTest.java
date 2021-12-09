package reservationPackage.controllers;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
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
import reservation.controllers.ReservationController;
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
    transient String equipmentBookingURL =
        "/reservation/{userId}/{equipmentName" + "}/{date}/{isCombined}/makeEquipmentBooking";
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

    @BeforeEach
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);

        Mockito.when(restTemplate.getForObject(
            ReservationController.sportFacilityUrl + "/equipment/" + equipmentNameValid
                + "/getAvailableEquipment", Long.class)).thenReturn(1L);

        Mockito.when(restTemplate.getForObject(
                ReservationController.userUrl + "/user/" + userId + "/isPremium", Boolean.class))
            .thenReturn(true);

        //
        //        Mockito.when(restTemplate.getForObject(ReservationController.sportFacilityUrl +
        //                "/equipment/" + equipmentNameInvalid + "/getAvailableEquipment",
        //            Long.class)).thenReturn()

        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new ReservationController(reservationService)).build();
    }

    @Disabled
    public void getReservationId() throws Exception {
        mockMvc.perform(get("/reservation/{reservationId}", reservationId))
            .andExpect(status().isOk());
        verify(reservationService).getReservation(1L);
    }

    @Disabled
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void makeEquipmentReservationOKTest() throws Exception {

        MvcResult result =
            mockMvc.perform(post(equipmentBookingURL, userId, equipmentNameValid, validDate, true))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(
            "Equipment " + "reservation was successful!");
        verify(reservationService).makeSportFacilityReservation(reservation);
    }

    @Disabled
    public void makeEquipmentReservationDateInThePastTest() throws Exception {
        String pastDate = "1990-01-06T17:00:00";
        mockMvc.perform(post(equipmentBookingURL, userId, equipmentNameValid, pastDate, true))
            .andExpect(status().isBadRequest())
            //.andExpect((ResultMatcher) content().string(containsString("Date and time has to be "
            //+ "after now")))
            .andDo(MockMvcResultHandlers.print());
        verify(reservationService, never()).makeSportFacilityReservation(reservation);

    }

    @Disabled
    public void makeEquipmentReservationOutsideOfTimeslotEdgeCase1() throws Exception {
        String invalidTime = "2099-01-06T15:59:59";

        mockMvc.perform(post(equipmentBookingURL, userId, equipmentNameValid, invalidTime, true))
            .andExpect(status().is4xxClientError()).andDo(MockMvcResultHandlers.print());
        verify(reservationService, never()).makeSportFacilityReservation(reservation);
    }

    @Disabled
    public void makeEquipmentReservationOutsideOfTimeslotEdgeCase2() throws Exception {
        String invalidTime = "2099-01-06T23:00:00";

        mockMvc.perform(post(equipmentBookingURL, userId, equipmentNameValid, invalidTime, true))
            .andExpect(status().isBadRequest())
            //.andExpect((ResultMatcher) content().string("hi"))
            .andDo(MockMvcResultHandlers.print());
        verify(reservationService, never()).makeSportFacilityReservation(reservation);
    }

    @Disabled
    public void makeEquipmentReservationInsideOfTimeslotEdgeCase1() throws Exception {
        String time = "2099-01-06T16:00:00";

        mockMvc.perform(post(equipmentBookingURL, userId, equipmentNameValid, time, true))
            .andExpect(status().isOk()).andExpect((ResultMatcher) content().string(
                containsString("Equipment reservation " + "was successful!")));
        verify(reservationService).makeSportFacilityReservation(reservation);
    }

    @Disabled
    public void makeEquipmentReservationInsideOfTimeslotEdgeCase2() throws Exception {
        String time = "2099-01-06T22:59:59";

        mockMvc.perform(post(equipmentBookingURL, userId, equipmentNameValid, time, true))
            .andExpect(status().isOk()).andExpect((ResultMatcher) content().string(
                containsString("Equipment reservation " + "was successful!")));
        verify(reservationService).makeSportFacilityReservation(reservation);
    }

}
