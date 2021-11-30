package reservationPackage.entities;

import java.util.ArrayList;

public class Chronological implements ReservationSorting{

    @Override
    public void executeStrategy(ArrayList<Reservation> reservations) {
        // Insertion Sort
        for(int i = 1; i < reservations.size(); i++) {
            int j = i-1;
            int index = i;
            while(j >= 0) {
                if(reservations.get(index).getStartingTime().isAfter(reservations.get(i).getStartingTime())) {
                    Reservation temp = reservations.get(index);
                    reservations.set(index, reservations.get(j));
                    reservations.set(j, temp);
                    index--;
                }
                j--;
            }
        }
    }

}
