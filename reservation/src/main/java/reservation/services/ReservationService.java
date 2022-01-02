package reservation.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.ReservationValidator;
import reservation.entities.chainofresponsibility.SportFacilityAvailabilityValidator;
import reservation.entities.chainofresponsibility.TeamRoomCapacityValidator;
import reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
import reservation.repositories.ReservationRepository;

/**
 * The type Reservation service.
 */
@Service
public class ReservationService {

    private final transient ReservationRepository reservationRepository;

    /**
     * Instantiates a new Reservation service.
     *
     * @param reservationRepository the reservation repository
     */
    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Gets reservation from repository.
     *
     * @param reservationId the reservation id to search for
     * @return the found reservation, or throw exception if non-existent id
     */
    public Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
            () -> new NoSuchElementException(
                "Reservation with id " + reservationId + " does not exist!"));
    }

    /**
     * Delete reservation if id exists.
     *
     * @param reservationId the reservation id
     */
    public boolean deleteReservation(Long reservationId) {
        boolean exists = reservationRepository.existsById(reservationId);
        if (!exists) {
            throw new NoSuchElementException(
                "Reservation with id " + reservationId + " does not " + "exist!");
        }
        reservationRepository.deleteById(reservationId);
        return true;
    }

    /**
     * Check reservation by passing the object through Chain of Responsibility.
     * Various checks to be done by different validators.
     * If the reservation was not valid, that means one or more checks (in some validator)
     * were violated -> exception thrown.
     *
     * @param reservation           the reservation
     * @param reservationController the reservation controller through which API calls to other
     *                              microservices are made
     * @return boolean - true if Reservation can be made, else false.
     */
    public boolean checkReservation(Reservation reservation,
                                    ReservationController reservationController) {

        // Returns first validator in chain created for this reservation
        ReservationValidator reservationValidator =
            createChainOfResponsibility(reservation, reservationController);

        try {
            return reservationValidator.handle(reservation);   // Start of chain
        } catch (InvalidReservationException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates Chain of Responsibility object.
     * Having a separate method for this creation, facilitates testability.
     *
     * @param reservation           - Reservation to be checked
     * @param reservationController - API to communicate with other microservices
     * @return - The first validator in the chain of responsibility created
     */
    public ReservationValidator createChainOfResponsibility(
        Reservation reservation,
        ReservationController reservationController) {

        // Checks whether or not customers have exceeded their daily
        // reservation limit for sport rooms
        ReservationValidator userBalanceHandler =
            new UserReservationBalanceValidator(this, reservationController);

        // Checks whether the reserved sports room or equipment is available for reservation
        ReservationValidator sportFacilityHandler =
            new SportFacilityAvailabilityValidator(this, reservationController);
        userBalanceHandler.setNext(sportFacilityHandler);

        // Only for sports room reservations, we check the room capacity/team size
        if (reservation.getTypeOfReservation() == ReservationType.SPORTS_ROOM) {

            //  Checks the compatibility of the reserved sports room (hall/field) capacity
            //  with the group size of the customers who want to reserve that sports room
            // For sport fields (hold 1 sport),
            // the team size requirements of that sport is also checked
            ReservationValidator capacityHandler =
                new TeamRoomCapacityValidator(this, reservationController);
            sportFacilityHandler.setNext(capacityHandler);
        }
        return userBalanceHandler;
    }

    /**
     * Gets user reservation count (for sport rooms, not equipment) on given day.
     *
     * @param start      the start
     * @param end        the end
     * @param customerId the customer id
     * @return the user reservation count on day
     */
    public int getUserReservationCountOnDay(LocalDateTime start, LocalDateTime end,
                                            long customerId) {

        List<Reservation> reservationsOnDay =
            reservationRepository.findReservationByStartingTimeBetweenAndCustomerId(start, end,
                customerId);
        int count = 0;

        // Customers have a limit on the number of sport rooms to be reserved
        // Basic: 1 per day, premium: 3 per day
        for (Reservation reservation : reservationsOnDay) {
            if (reservation.getTypeOfReservation() == ReservationType.SPORTS_ROOM) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if sports facility is available at given time.
     * All Reservations start at full hours, so only start time has to be checked.
     *
     * @param sportFacilityId the sport facility id to check
     * @param time            the time to check
     * @return boolean - true if sport facility is not associated with any reservation yet.
     */
    public boolean sportsFacilityIsAvailable(Long sportFacilityId, LocalDateTime time) {
        return reservationRepository.findBySportFacilityReservedIdAndTime(sportFacilityId, time)
            .isEmpty();
    }

    /**
     * Make sport facility (Equipment, Sport Room, Lesson) reservation.
     *
     * @param reservation - reservation object to be saved in database
     * @return the reservation
     */
    public Reservation makeSportFacilityReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    /**
     * Find reservation by group id and time given.
     *
     * @param groupId the group id
     * @param time    the time
     * @return the Long corresponding to reservation found, null if not found.
     */
    public Long findByGroupIdAndTime(Long groupId, LocalDateTime time) {
        return reservationRepository.findByGroupIdAndTime(groupId, time).orElse(null);
    }

    /**
     * Gets last person that used equipment.
     *
     * @param equipmentId the equipment id
     * @return the last person that used equipment
     */
    public Long getLastPersonThatUsedEquipment(Long equipmentId) {
        List<Reservation> reservations =
            reservationRepository.findReservationsBySportFacilityReservedId(equipmentId);

        reservations.sort(Comparator.comparing(Reservation::getStartingTime).reversed());
        return reservations.get(0).getCustomerId();
    }

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}


