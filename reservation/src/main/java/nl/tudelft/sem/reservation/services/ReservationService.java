package nl.tudelft.sem.reservation.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import nl.tudelft.sem.reservation.entities.Reservation;
import nl.tudelft.sem.reservation.entities.ReservationType;
import nl.tudelft.sem.reservation.repositories.ReservationRepository;

@Service
@Configuration
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
     * Gets user reservation count (for sport rooms, not equipment) on given day.
     *
     * @param start      the start
     * @param end        the end
     * @param customerId the customer id
     * @return the user reservation count on day
     */
    public int getUserReservationCountOnDay(LocalDateTime start, LocalDateTime end,
                                            Long customerId) {

        List<Reservation> reservationsOnDay = reservationRepository
            .findReservationByStartingTimeBetweenAndCustomerId(start, end, customerId);
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


