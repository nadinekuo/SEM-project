package reservation.entities.strategy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.strategy.ReservationSortingStrategy;

public class EquipmentNameStrategy implements ReservationSortingStrategy {


    /**
     * Returns the next reservation in order.
     *
     * @param reservations list of reservations to be sorted
     */
    public Reservation getNextReservation(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            return null;
        }
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

            if (reservation1.getTypeOfReservation().equals(ReservationType.EQUIPMENT)
                && !reservation2.getTypeOfReservation().equals(ReservationType.EQUIPMENT)) {
                return -1;
            } else if (!reservation1.getTypeOfReservation().equals(ReservationType.EQUIPMENT)
                && reservation2.getTypeOfReservation().equals(ReservationType.EQUIPMENT)) {
                return 1;
            } else {

                String name1 = reservation1.getBookedItemName();
                String name2 = reservation2.getBookedItemName();

                int compareName = name1.compareTo(name2);
                if (compareName != 0) {
                    return compareName;
                }

                return reservation1.getStartingTime().compareTo(reservation2.getStartingTime());
            }
        }
    }
}

