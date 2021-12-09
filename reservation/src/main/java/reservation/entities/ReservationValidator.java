package reservation.entities;

public interface ReservationValidator {


    void setNext(ReservationValidator handler);

    boolean handle(Reservation reservation) throws InvalidReservationException;


}
