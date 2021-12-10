package reservation.entities.chainofresponsibility;

import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.chainofresponsibility.BaseValidator;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.services.ReservationService;

public class TeamRoomCapacityValidator extends BaseValidator {


    private ReservationService reservationService;
    private ReservationController reservationController;

    public TeamRoomCapacityValidator(ReservationService reservationService,
                                     ReservationController reservationController) {
        this.reservationService = reservationService;
        this.reservationController = reservationController;
    }


    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        // Todo: if isSportHall == false --> check team size for team Sport


        // Todo: check min/max capacity of sport room (hall/field)

        return super.checkNext(reservation);
    }
}
