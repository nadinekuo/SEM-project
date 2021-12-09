package reservation.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.repositories.ReservationRepository;

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

    /**
     * Gets user reservation count on day.
     *
     * @param start - start of day we want to count reservations for: yyyy-MM-ddT00:00:00
     * @param end - end of day we want to count reservations for: yyyy-MM-ddT23:59:59
     * @param customerId the customer id
     * @return the user reservation count for a day
     */
    public int getUserReservationCountOnDay(LocalDateTime start,
                                            LocalDateTime end, long customerId) {

        List<Reservation> reservationsOnDay =
            reservationRepository.findReservationByStartingTimeBetweenAndCustomerId(start,
                end, customerId);
        int count = 0;

        // Customers have a limit on the number of sport rooms to be reserved
        // Basic: 1 per day
        // Premium: 3 per day
        for (Reservation reservation : reservationsOnDay) {
            if (reservation.getTypeOfReservation() == ReservationType.SPORTS_FACILITY) {
                count++;
            }
        }
        return count;
    }


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


