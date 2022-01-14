package nl.tudelft.sem.reservation.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import nl.tudelft.sem.reservation.entities.Reservation;
import nl.tudelft.sem.reservation.entities.ReservationType;
import nl.tudelft.sem.reservation.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

    transient DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    transient LocalDateTime bookableDate =
        LocalDateTime.parse("2099-01-06 17:00:00", dateTimeFormatter);

    private final transient Reservation reservation =
        new Reservation(ReservationType.EQUIPMENT, "hockey", userId, sportFacilityId, bookableDate,
            madeByPremiumUser);

    SportFacilityCommunicator sportFacilityCommunicator;

    @Mock
    transient ReservationService reservationService;

    @Mock
    transient RestTemplate restTemplate;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void setup() {
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);
        sportFacilityCommunicator = new SportFacilityCommunicator(restTemplate);
    }

    @Test
    public void getSportFacilityUrlTest() {
        assertEquals(sportFacilityUrl, sportFacilityCommunicator.getSportFacilityUrl());
    }

    @Test
    public void getSportsRoomExistsTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(true))));
        assertTrue(sportFacilityCommunicator.getSportsRoomExists(sportRoomId));
    }

    @Test
    public void getIsSportHallTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(true))));
        assertTrue(sportFacilityCommunicator.getIsSportHall(sportRoomId));
    }

    @Test
    public void getSportRoomMaximumCapacityTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, sportFacilityCommunicator.getSportRoomMaximumCapacity(sportRoomId));
    }

    @Test
    public void getSportRoomMinimumCapacityTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, sportFacilityCommunicator.getSportRoomMinimumCapacity(sportRoomId));
    }

    @Test
    public void getFirstAvailableEquipmentIdTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(equipmentId))));
        assertEquals(Optional.of(equipmentId),
            Optional.of(sportFacilityCommunicator.getFirstAvailableEquipmentId(equipmentName)));
    }

    @Test
    public void getSportFieldSportTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(sportName)));
        assertEquals(sportName, sportFacilityCommunicator.getSportFieldSport(sportFacilityId));
    }

    @Test
    public void getSportMaxTeamSizeTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, sportFacilityCommunicator.getSportMaxTeamSize(sportName));
    }

    @Test
    public void getSportMinTeamSizeTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, sportFacilityCommunicator.getSportMinTeamSize(sportName));
    }

    @Test
    public void getLessonNameTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of("Spinning")));
        assertEquals("Spinning", sportFacilityCommunicator.getLessonName(1L));
    }

    @Test
    public void getLessonBeginningTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any())).thenReturn(
            ResponseEntity.of(Optional.of(String.valueOf(LocalDateTime.of(2022, 01, 01, 15, 00)))));
        assertEquals(LocalDateTime.of(2022, 01, 01, 15, 00),
            sportFacilityCommunicator.getLessonBeginning(1L));
    }
}

