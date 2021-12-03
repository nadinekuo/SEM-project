package reservationPackage.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BasicPremiumUserStrategy implements ReservationSortingStrategy{

    public Reservation getNextReservation(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty())
            return null;
        this.sort(reservations);
        return reservations.get(0);
    }

    private void sort(List<Reservation> list) {
        Collections.sort(list, new ReservationComparator());
    }

    protected class ReservationComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Reservation reservation1 = (Reservation) o1;
            Reservation reservation2 = (Reservation) o2;
            if (reservation1.getStartingTime().isBefore(reservation2.getStartingTime()))
                return -1;
            if (reservation1.getStartingTime().isAfter(reservation2.getStartingTime()))
                return +1;
            return 0;
        }
    }
}
