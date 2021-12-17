package reservation.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reservation.entities.Reservation;

/**
 * The interface Reservation repository.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long reservationId);

    @Transactional
    void deleteById(Long reservationId);

    /**
     * Find by sport facility reserved id and time optional.
     *
     * @param sportsFacilityId the sports facility id
     * @param time             the time
     * @return the optional
     */
    @Query(value = "SELECT reservation_id " + "FROM reservations "
        + "WHERE sport_facility_reserved_id = ?1 AND starting_time = ?2", nativeQuery = true)
    Optional<Long> findBySportFacilityReservedIdAndTime(Long sportsFacilityId, LocalDateTime time);

    /**
     * Find starting time by reservation id local date.
     *
     * @param reservationId the reservation id
     * @return the local date
     */
    LocalDate findStartingTimeByReservationId(long reservationId);

    /**
     * Find reservation by starting time between and customer id list.
     *
     * @param start      the start
     * @param end        the end
     * @param customerId the customer id
     * @return the list
     */
    List<Reservation> findReservationByStartingTimeBetweenAndCustomerId(LocalDateTime start,
                                                                        LocalDateTime end,
                                                                        Long customerId);

    /**
     * Find reservations by sport facility reserved id list.
     *
     * @param sportFacilityReservedId the sport facility reserved id
     * @return the list
     */
    List<Reservation> findReservationsBySportFacilityReservedId(Long sportFacilityReservedId);

    /**
     * Find by group id and time optional.
     *
     * @param groupId the group id
     * @param time    the time
     * @return the optional
     */
    @Query(value = "SELECT MIN(reservation_id) " + "FROM reservations "
        + "WHERE group_id = ?1 AND starting_time = ?2", nativeQuery = true)
    Optional<Long> findByGroupIdAndTime(Long groupId, LocalDateTime time);
}
