package reservation.entities.strategy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import reservation.entities.Reservation;
import reservation.entities.strategy.ReservationSortingStrategy;

/**
 * The type Basic premium user strategy.
 */
public class BasicPremiumUserStrategy implements ReservationSortingStrategy {


    /**
     * Instantiates a new Basic premium user strategy.

     */
    public BasicPremiumUserStrategy() {

    }

    /**
     * Returns the next reservation in order.
     *
     * @param reservations list of reservations to be sorted
     */
    public Reservation getNextReservation(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            return null;
        }
        this.sort(reservations);
        return reservations.get(0);
    }

    private void sort(List<Reservation> list) {
        Collections.sort(list, new ReservationComparator());
    }

    /**
     * The type Reservation comparator.
     */
    protected static class ReservationComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {

            boolean b1 = ((Reservation) o1).getMadeByPremiumUser();
            boolean b2 = ((Reservation) o2).getMadeByPremiumUser();

            if (b1 && !b2) {
                return -1;
            }
            if (!b1 && b2) {
                return +1;
            }
            return 0;
        }
    }
}
