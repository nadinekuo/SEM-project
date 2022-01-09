package reservation.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
import org.springframework.web.client.RestTemplate;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.ReservationChecker;
import reservation.services.ReservationService;

/**
 * The type Sport facility communicator test.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@MockitoSettings(strictness = Strictness.LENIENT)
public class SportFacilityCommunicatorTest {

    private final transient long sportRoomId = 1L;
    private final transient long equipmentId = 1L;
    private final transient String equipmentName = "hockeyStick";
    private final transient String sportName = "Box";
    private final transient long invalidId = 13L;
    private final transient long userId = 1L;
    private final transient long groupId = 1L;
    private final transient long sportFacilityId = 1L;
    private final transient String equipmentNameValid = "hockeyStick";
    private final transient Boolean madeByPremiumUser = true;

    private final transient String sportFacilityUrl = "http://eureka-sport-facilities";

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
        new Reservation(ReservationType.EQUIPMENT, "hockey", userId, sportFacilityId, bookableDate,
            madeByPremiumUser);

    /**
     * The Sport facility communicator.
     */
    SportFacilityCommunicator sportFacilityCommunicator;
    /**
     * The Reservation service.
     */
    @Mock
    transient ReservationService reservationService;

    /**
     * The Reservation checker.
     */
    @Mock
    transient ReservationChecker reservationChecker;

    /**
     * The Rest template.
     */
    @Mock
    transient RestTemplate restTemplate;

    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets .
     */
    @BeforeEach
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void setup() {
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);
        sportFacilityCommunicator = new SportFacilityCommunicator(restTemplate);
    }

    /**
     * Gets sport facility url test.
     */
    @Test
    public void getSportFacilityUrlTest() {
        assertEquals(sportFacilityUrl, sportFacilityCommunicator.getSportFacilityUrl());
    }

    /**
     * Gets sports room exists test.
     */
    @Test
    public void getSportsRoomExistsTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(true))));
        assertTrue(sportFacilityCommunicator.getSportsRoomExists(sportRoomId));
    }

    /**
     * Gets is sport hall test.
     */
    @Test
    public void getIsSportHallTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(true))));
        assertTrue(sportFacilityCommunicator.getIsSportHall(sportRoomId));
    }

    /**
     * Gets sport room maximum capacity test.
     */
    @Test
    public void getSportRoomMaximumCapacityTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, sportFacilityCommunicator.getSportRoomMaximumCapacity(sportRoomId));
    }

    /**
     * Gets sport room minimum capacity test.
     */
    @Test
    public void getSportRoomMinimumCapacityTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, sportFacilityCommunicator.getSportRoomMinimumCapacity(sportRoomId));
    }

    /**
     * Gets first available equipment id test.
     */
    @Test
    public void getFirstAvailableEquipmentIdTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(equipmentId))));
        assertEquals(Optional.of(equipmentId),
            Optional.of(sportFacilityCommunicator.getFirstAvailableEquipmentId(equipmentName)));
    }

    /**
     * Gets sport field sport test.
     */
    @Test
    public void getSportFieldSportTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(sportName)));
        assertEquals(sportName, sportFacilityCommunicator.getSportFieldSport(sportFacilityId));
    }

    /**
     * Gets sport max team size test.
     */
    @Test
    public void getSportMaxTeamSizeTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, sportFacilityCommunicator.getSportMaxTeamSize(sportName));
    }

    /**
     * Gets sport min team size test.
     */
    @Test
    public void getSportMinTeamSizeTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, sportFacilityCommunicator.getSportMinTeamSize(sportName));
    }
}

