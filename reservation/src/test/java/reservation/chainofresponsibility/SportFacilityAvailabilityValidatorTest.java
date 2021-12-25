package reservation.chainofresponsibility;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.SportFacilityAvailabilityValidator;
import reservation.services.ReservationService;

@RunWith(MockitoJUnitRunner.class)
public class SportFacilityAvailabilityValidatorTest {

    private final transient Reservation reservationInvalidTime;

    private final transient Reservation equipmentReservation;
    private final transient Reservation equipmentReservationInvalid;

    private final transient Reservation sportRoomReservation;
    private final transient Reservation groupReservation;

    private final transient ReservationController reservationController;
    private final transient ReservationService reservationService;
    // Class under test:
    private final transient SportFacilityAvailabilityValidator sportFacilityAvailabilityValidator;
    private transient Long groupId;

    /**
     * Constructor for this test suite.
     */
    public SportFacilityAvailabilityValidatorTest() {
        reservationService = mock(ReservationService.class);
        reservationController = mock(ReservationController.class);
        this.sportFacilityAvailabilityValidator =
            new SportFacilityAvailabilityValidator(reservationService, reservationController);

        reservationInvalidTime = new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 15, 59));
        reservationInvalidTime.setId(53L);

        equipmentReservation = new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 15, 59));
        equipmentReservation.setId(54L);
        equipmentReservationInvalid = new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, -1L,
            LocalDateTime.of(2022, 10, 05, 15, 59));
        equipmentReservationInvalid.setId(55L);

        sportRoomReservation = new Reservation(ReservationType.SPORTS_ROOM, "hockey", 2L, 25L,
            LocalDateTime.of(2022, 10, 05, 17, 45));
        sportRoomReservation.setId(84L);

        groupReservation = new Reservation(ReservationType.SPORTS_ROOM, "hockey", 3L, 13L,
            LocalDateTime.of(2022, 02, 3, 20, 30), 84L);
        groupReservation.setId(99L);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(sportFacilityAvailabilityValidator);
    }

    @Test
    public void testInvalidTime() throws InvalidReservationException {

        assertThrows(InvalidReservationException.class, () -> {
            sportFacilityAvailabilityValidator.handle(reservationInvalidTime);
        });
    }

    @Test
    public void testInvalidEquipmentId() throws InvalidReservationException {

        assertThrows(InvalidReservationException.class, () -> {
            sportFacilityAvailabilityValidator.handle(equipmentReservationInvalid);
        });
    }

    @Test
    public void testUnavailableSportRoom() throws InvalidReservationException {

        when(reservationService.sportsFacilityIsAvailable(anyLong(), any())).thenReturn(false);

        assertThrows(InvalidReservationException.class, () -> {
            sportFacilityAvailabilityValidator.handle(sportRoomReservation);
        });
        verify(reservationService).sportsFacilityIsAvailable(
            sportRoomReservation.getSportFacilityReservedId(),
            sportRoomReservation.getStartingTime());
    }

    @Test
    public void testNonExistentSportRoom() throws InvalidReservationException {

        // Room is "available" (no reservation for that room yet), but does not exist!
        when(reservationService.sportsFacilityIsAvailable(anyLong(), any())).thenReturn(true);
        when(reservationController.getSportsRoomExists(anyLong())).thenReturn(false);

        assertThrows(InvalidReservationException.class, () -> {
            sportFacilityAvailabilityValidator.handle(sportRoomReservation);
        });
        verify(reservationService).sportsFacilityIsAvailable(
            sportRoomReservation.getSportFacilityReservedId(),
            sportRoomReservation.getStartingTime());
        verify(reservationController).getSportsRoomExists(
            sportRoomReservation.getSportFacilityReservedId());
    }

    @Test
    public void validGroupReservation() throws InvalidReservationException {

        // Assuming reservation with id 5L is another reservation by another group member,
        // for the same room
        when(reservationService.findByGroupIdAndTime(anyLong(), any())).thenReturn(5L);
        when(reservationController.getSportsRoomExists(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> {
            sportFacilityAvailabilityValidator.handle(groupReservation);
        });

        verify(reservationService).findByGroupIdAndTime(groupReservation.getGroupId(),
            groupReservation.getStartingTime());
        verify(reservationController).getSportsRoomExists(
            groupReservation.getSportFacilityReservedId());
    }

}
