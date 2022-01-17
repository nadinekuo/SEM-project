package nl.tudelft.sem.reservation.chainofresponsibility;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import nl.tudelft.sem.reservation.controllers.ReservationController;
import nl.tudelft.sem.reservation.controllers.SportFacilityCommunicator;
import nl.tudelft.sem.reservation.controllers.UserFacilityCommunicator;
import nl.tudelft.sem.reservation.entities.Reservation;
import nl.tudelft.sem.reservation.entities.ReservationType;
import nl.tudelft.sem.reservation.entities.chainofresponsibility.InvalidReservationException;
import nl.tudelft.sem.reservation.entities.chainofresponsibility.TeamRoomCapacityValidator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TeamRoomCapacityValidatorTest {

    private final transient Reservation groupReservation;
    private final transient Reservation reservation1;

    private final transient ReservationController reservationController;
    private final transient SportFacilityCommunicator sportFacilityCommunicator;
    private final transient UserFacilityCommunicator userFacilityCommunicator;
    // Class under test:
    private final transient TeamRoomCapacityValidator teamRoomCapacityValidator;
    private final transient String sportName;
    private final transient boolean madeByPremiumUser = true;

    /**
     * Constructor for this test suite.
     */
    public TeamRoomCapacityValidatorTest() {

        reservationController = mock(ReservationController.class);
        sportFacilityCommunicator = mock(SportFacilityCommunicator.class);
        userFacilityCommunicator = mock(UserFacilityCommunicator.class);

        when(reservationController.getSportFacilityCommunicator()).thenReturn(
            sportFacilityCommunicator);
        when(reservationController.getUserFacilityCommunicator()).thenReturn(
            userFacilityCommunicator);

        this.teamRoomCapacityValidator = new TeamRoomCapacityValidator(reservationController);

        groupReservation = new Reservation(ReservationType.SPORTS_ROOM, "Hall 1", 3L, 13L,
            LocalDateTime.of(2022, 02, 3, 20, 30), 84L, madeByPremiumUser);
        groupReservation.setId(99L);
        reservation1 = new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 16, 00), madeByPremiumUser);
        reservation1.setId(53L);
        sportName = "soccer";
    }

    /**
     * Test constructor.
     */
    @Test
    public void constructorTest() {
        assertNotNull(teamRoomCapacityValidator);
    }

    @Test
    public void sportHallBelowMinCapacityTest() throws InvalidReservationException {

        // Sport halls hold multiple sports, so no team size check done
        when(sportFacilityCommunicator.getIsSportHall(anyLong())).thenReturn(true);
        when(userFacilityCommunicator.getGroupSize(anyLong())).thenReturn(5);
        when(sportFacilityCommunicator.getSportRoomMinimumCapacity(anyLong())).thenReturn(6);
        when(sportFacilityCommunicator.getSportRoomMaximumCapacity(anyLong())).thenReturn(100);

        assertThrows(InvalidReservationException.class, () -> {
            teamRoomCapacityValidator.handle(groupReservation);
        });

        verify(sportFacilityCommunicator).getIsSportHall(
            groupReservation.getSportFacilityReservedId());
        verify(userFacilityCommunicator).getGroupSize(groupReservation.getGroupId());
        verify(sportFacilityCommunicator).getSportRoomMinimumCapacity(
            groupReservation.getSportFacilityReservedId());
        verify(sportFacilityCommunicator).getSportRoomMaximumCapacity(
            groupReservation.getSportFacilityReservedId());
    }

    @Test
    public void sportHallOverMaxCapacityTest() throws InvalidReservationException {

        // Sport halls hold multiple sports, so no team size check done
        when(sportFacilityCommunicator.getIsSportHall(anyLong())).thenReturn(true);
        when(userFacilityCommunicator.getGroupSize(anyLong())).thenReturn(25);
        when(sportFacilityCommunicator.getSportRoomMinimumCapacity(anyLong())).thenReturn(6);
        // Group size exceeds max capacity!
        when(sportFacilityCommunicator.getSportRoomMaximumCapacity(anyLong())).thenReturn(24);

        assertThrows(InvalidReservationException.class, () -> {
            teamRoomCapacityValidator.handle(groupReservation);
        });

        verify(sportFacilityCommunicator).getIsSportHall(
            groupReservation.getSportFacilityReservedId());
        verify(userFacilityCommunicator).getGroupSize(groupReservation.getGroupId());
        verify(sportFacilityCommunicator).getSportRoomMinimumCapacity(
            groupReservation.getSportFacilityReservedId());
        verify(sportFacilityCommunicator).getSportRoomMaximumCapacity(
            groupReservation.getSportFacilityReservedId());
    }


    @Test
    public void sportFieldBelowMinTeamSizeTest() throws InvalidReservationException {

        // Sport fields hold 1 team sport, so team size check done
        when(sportFacilityCommunicator.getIsSportHall(anyLong())).thenReturn(false);
        when(userFacilityCommunicator.getGroupSize(anyLong())).thenReturn(5);
        when(sportFacilityCommunicator.getSportFieldSport(anyLong())).thenReturn(sportName);
        // Group size < min team size, so invalid
        when(sportFacilityCommunicator.getSportMinTeamSize(anyString())).thenReturn(8);
        when(sportFacilityCommunicator.getSportMaxTeamSize(anyString())).thenReturn(15);

        assertThrows(InvalidReservationException.class, () -> {
            teamRoomCapacityValidator.handle(groupReservation);
        });

        verify(sportFacilityCommunicator).getIsSportHall(
            groupReservation.getSportFacilityReservedId());
        verify(userFacilityCommunicator).getGroupSize(groupReservation.getGroupId());
        verify(sportFacilityCommunicator).getSportFieldSport(
            groupReservation.getSportFacilityReservedId());
        verify(sportFacilityCommunicator).getSportMinTeamSize(sportName);
        verify(sportFacilityCommunicator).getSportMaxTeamSize(sportName);
    }


    @Test
    public void sportFieldOverMaxTeamSizeTest() throws InvalidReservationException {

        // Sport fields hold 1 team sport, so team size check done
        when(sportFacilityCommunicator.getIsSportHall(anyLong())).thenReturn(false);
        when(userFacilityCommunicator.getGroupSize(anyLong())).thenReturn(51);
        when(sportFacilityCommunicator.getSportFieldSport(anyLong())).thenReturn(sportName);
        // Group size > max team size, so invalid
        when(sportFacilityCommunicator.getSportMinTeamSize(anyString())).thenReturn(8);
        when(sportFacilityCommunicator.getSportMaxTeamSize(anyString())).thenReturn(50);

        assertThrows(InvalidReservationException.class, () -> {
            teamRoomCapacityValidator.handle(groupReservation);
        });

        verify(sportFacilityCommunicator).getIsSportHall(
            groupReservation.getSportFacilityReservedId());
        verify(userFacilityCommunicator).getGroupSize(groupReservation.getGroupId());
        verify(sportFacilityCommunicator).getSportFieldSport(
            groupReservation.getSportFacilityReservedId());
        verify(sportFacilityCommunicator).getSportMinTeamSize(sportName);
        verify(sportFacilityCommunicator).getSportMaxTeamSize(sportName);
    }


    @Test
    public void validIndividualReservationTest() {

        when(sportFacilityCommunicator.getIsSportHall(anyLong())).thenReturn(true);
        when(sportFacilityCommunicator.getSportRoomMinimumCapacity(anyLong())).thenReturn(1);
        when(sportFacilityCommunicator.getSportRoomMaximumCapacity(anyLong())).thenReturn(100);

        // Individual group size is by default 1, so between min and max.
        assertDoesNotThrow(() -> {
            teamRoomCapacityValidator.handle(reservation1);
        });
    }

    @Test
    public void validGroupReservationLowerBoundaryTest() {

        // Sport fields hold 1 team sport, so team size check done
        when(sportFacilityCommunicator.getIsSportHall(anyLong())).thenReturn(false);
        when(userFacilityCommunicator.getGroupSize(anyLong())).thenReturn(8);
        when(sportFacilityCommunicator.getSportFieldSport(anyLong())).thenReturn(sportName);
        // Group size between min and max team size (exactly equal to min), so valid.
        when(sportFacilityCommunicator.getSportMinTeamSize(anyString())).thenReturn(8);
        when(sportFacilityCommunicator.getSportMaxTeamSize(anyString())).thenReturn(15);
        // Group size between min and max room capacity (exactly equal to min), so valid.
        when(sportFacilityCommunicator.getSportRoomMinimumCapacity(anyLong())).thenReturn(8);
        when(sportFacilityCommunicator.getSportRoomMaximumCapacity(anyLong())).thenReturn(24);

        assertDoesNotThrow(() -> {
            teamRoomCapacityValidator.handle(groupReservation);
        });
    }

    @Test
    public void validGroupReservationUpperBoundaryTest() {

        // Sport fields hold 1 team sport, so team size check done
        when(sportFacilityCommunicator.getIsSportHall(anyLong())).thenReturn(false);
        when(userFacilityCommunicator.getGroupSize(anyLong())).thenReturn(15);
        when(sportFacilityCommunicator.getSportFieldSport(anyLong())).thenReturn(sportName);
        // Group size between min and max team size (exactly equal to max), so valid.
        when(sportFacilityCommunicator.getSportMinTeamSize(anyString())).thenReturn(8);
        when(sportFacilityCommunicator.getSportMaxTeamSize(anyString())).thenReturn(15);
        // Group size between min and max capacity (exactly equal to max)), so valid.
        when(sportFacilityCommunicator.getSportRoomMinimumCapacity(anyLong())).thenReturn(8);
        when(sportFacilityCommunicator.getSportRoomMaximumCapacity(anyLong())).thenReturn(15);

        assertDoesNotThrow(() -> {
            teamRoomCapacityValidator.handle(groupReservation);
        });
    }


}
