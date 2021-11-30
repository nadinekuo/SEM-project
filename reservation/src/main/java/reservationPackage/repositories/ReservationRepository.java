package reservationPackage.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reservationPackage.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findById(long reservationId);

    @Query(value = "SELECT id FROM reservations WHERE sportRoomId = sportRoomId AND startingTime "
        + "= time", nativeQuery = true)
    Reservation findByIdAndTime(String sportRoomId, LocalDateTime time);

    LocalDate findStartingTimeByReservationId(long reservationId);


    @Transactional
    void deleteById(Long reservationId);

}
