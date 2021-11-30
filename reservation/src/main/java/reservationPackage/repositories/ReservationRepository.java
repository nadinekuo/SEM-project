package reservationPackage.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reservationPackage.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findById(long reservationId);

    @Query(value =
        "SELECT reservation_id " +
        "FROM reservations " +
            "WHERE sport_facility_reserved_id = ?1 AND starting_time = ?2",
        nativeQuery = true)
    Optional <Long> findBySportRoomIdAndTime(Long sportRoomId, LocalDateTime time);

    LocalDate findStartingTimeByReservationId(long reservationId);


    @Transactional
    void deleteById(Long reservationId);

}
