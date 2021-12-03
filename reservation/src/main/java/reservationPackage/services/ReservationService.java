package reservationPackage.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reservationPackage.entities.Reservation;
import reservationPackage.repositories.ReservationRepository;

@Service
public class ReservationService {

    private final transient ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation getReservation(long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    // All Reservations start at full hours, so only start time has to be checked.
    public boolean isAvailable(Long sportRoomId, LocalDateTime time) {
        return reservationRepository.findBySportRoomIdAndTime(sportRoomId, time).isEmpty();
    }

    public Reservation makeSportFacilityReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public int getUserReservationCountOnDay(String date, long userId) {
        return reservationRepository.findReservationByStartingTimeContains(date).size();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


