package reservationPackage.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import reservationPackage.controllers.ReservationController;
import reservationPackage.services.ReservationService;

public class EquipmentNameStrategy implements ReservationSortingStrategy{

    @Autowired
    private final RestTemplate restTemplate;

    public EquipmentNameStrategy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


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
            Long equipmentId1 = reservation1.getSportFacilityReservedId();
            Long equipmentId2 = reservation2.getSportFacilityReservedId();

            String equipmentName1 =
                restTemplate.getForObject("http://localhost:8085/equipment/" + equipmentId1 +
                        "/getEquipmentName",
                    String.class);
            String equipmentName2 =
                restTemplate.getForObject("http://localhost:8085/equipment/" + equipmentId2 +
                        "/getEquipmentName",
                    String.class);

            int compareName = equipmentName1.compareTo(equipmentName2);
            if (compareName != 0) {
                return compareName;
            }

            return reservation1.getStartingTime().compareTo(reservation2.getStartingTime());
        }
    }
}

