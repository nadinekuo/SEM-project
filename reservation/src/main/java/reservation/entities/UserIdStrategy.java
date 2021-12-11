package reservation.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The type User id strategy.
 */
public class UserIdStrategy implements ReservationSortingStrategy {

    /**
     * Instantiates a new User id strategy.
     */
    public UserIdStrategy() {
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
    protected class ReservationComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Reservation reservation1 = (Reservation) o1;
            Reservation reservation2 = (Reservation) o2;
            Long userId1 = reservation1.getCustomerId();
            Long userId2 = reservation2.getCustomerId();

            return userId1.compareTo(userId2);
        }
    }
}
