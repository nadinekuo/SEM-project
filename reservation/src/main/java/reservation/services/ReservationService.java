package reservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.*;
import reservation.repositories.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final transient ReservationRepository reservationRepository;


    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Gets Reservation object corresponding to id, if existing.
     *
     * @param reservationId - Long
     * @return Reservation
     */
    public Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
            () -> new IllegalStateException(
                "Reservation with id " + reservationId + " does not exist!"));
    }

    /**
     * Deletes reservation, if id is valid.
     *
     * @param reservationId - Long
     */
    public void deleteReservation(Long reservationId) {
        boolean exists = reservationRepository.existsById(reservationId);
        if (!exists) {
            throw new IllegalStateException(
                "Reservation with id " + reservationId + " does not " + "exist!");
        }
        reservationRepository.deleteById(reservationId);
    }

    // All Reservations start at full hours, so only start time has to be checked.
    public boolean sportsFacilityIsAvailable(Long sportFacilityId, LocalDateTime time) {
        return reservationRepository.findBySportFacilityReservedIdAndTime(sportFacilityId, time)
            .isEmpty();
    }

    public Reservation makeSportFacilityReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    /**
     * Gets user reservation count on day.
     *
     * @param start      - start of day we want to count reservations for: yyyy-MM-ddT00:00:00
     * @param end        - end of day we want to count reservations for: yyyy-MM-ddT23:59:59
     * @param customerId the customer id
     * @return the user reservation count for a day
     */
    public int getUserReservationCountOnDay(LocalDateTime start, LocalDateTime end,
                                            long customerId) {

        List<Reservation> reservationsOnDay = reservationRepository
            .findReservationByStartingTimeBetweenAndCustomerId(start, end, customerId);
        int count = 0;

        // Customers have a limit on the number of sport rooms to be reserved
        // Basic: 1 per day, premium: 3 per day
        for (Reservation reservation : reservationsOnDay) {
            if (reservation.getTypeOfReservation() == ReservationType.SPORTS_FACILITY) {
                count++;
            }
        }
        return count;
    }

    /**
     * Passes the Reservation object through the chain of responsibility.
     *
     * @param reservation           - Reservation object to be passed through Chain of
     *                              Responsibility.
     * @param reservationController - API needed by Validator chain to communicate with other
     *                              microservices.
     * @return - true if reservation is valid, else false.
     */
    public boolean checkReservation(Reservation reservation,
                                    ReservationController reservationController) {

        // Start chain of responsibility
        ReservationValidator userBalanceHandler =
            new UserReservationBalanceValidator(this, reservationController);
        ReservationValidator sportFacilityHandler =
            new SportFacilityAvailabilityValidator(this, reservationController);
        userBalanceHandler.setNext(sportFacilityHandler);

        // Only for sports room reservations, we check the room capacity/team size
        if (reservation.getTypeOfReservation() == ReservationType.SPORTS_FACILITY) {
            ReservationValidator capacityHandler =
                new TeamRoomCapacityValidator(this, reservationController);
            sportFacilityHandler.setNext(capacityHandler);
        }

        try {
            return userBalanceHandler.handle(reservation);
        } catch (InvalidReservationException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param groupId - groupId of the reservation
     * @param time - time of the reservation
     * @return the reservation Id corresponding to the groupId and time
     */
    public Long findByGroupIdAndTime(Long groupId, LocalDateTime time) {
        return reservationRepository.findByGroupIdAndTime(groupId, time).orElse(null);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}


