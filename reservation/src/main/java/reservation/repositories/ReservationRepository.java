package reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reservation.entities.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long reservationId);

    @Query(value = "SELECT reservation_id " + "FROM reservations "
        + "WHERE sport_facility_reserved_id = ?1 AND starting_time = ?2", nativeQuery = true)
    Optional<Long> findBySportFacilityReservedIdAndTime(Long sportsFacilityId, LocalDateTime time);

    LocalDate findStartingTimeByReservationId(long reservationId);

    List<Reservation> findReservationByStartingTimeBetweenAndCustomerId(LocalDateTime start,
                                                                        LocalDateTime end,
                                                                         Long customerId);

    List<Reservation> findReservationsBySportFacilityReservedId(Long sportFacilityReservedId);

    @Transactional
    void deleteById(Long reservationId);

    @Query(value = "SELECT MIN(reservation_id) " + "FROM reservations "
        + "WHERE group_id = ?1 AND starting_time = ?2", nativeQuery = true)
    Optional<Long> findByGroupIdAndTime(Long groupId, LocalDateTime time);
}