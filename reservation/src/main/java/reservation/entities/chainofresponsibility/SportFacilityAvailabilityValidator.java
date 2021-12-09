package reservation.entities.chainofresponsibility;

import reservation.entities.Reservation;
import reservation.entities.chainofresponsibility.BaseValidator;
import reservation.entities.chainofresponsibility.InvalidReservationException;

public class SportFacilityAvailabilityValidator extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        //
        //        if (dateTime.isBefore(LocalDateTime.now())) {
        //            return new ResponseEntity<>("Date and time has to be after now",
        //                HttpStatus.BAD_REQUEST);
        //        }
        //
        //        if ((dateTime.getHour() < 16) || (dateTime.getHour() == 23)) {
        //            return new ResponseEntity<>("Time has to be between 16:00 and 23:00.",
        //                HttpStatus.NOT_FOUND);
        //        }
        //
        //        if (!reservationService.isAvailable(sportRoomId, dateTime)) {
        //            return new ResponseEntity<>("Sport Room is already booked for this time slot.",
        //                HttpStatus.NOT_FOUND);
        //        }
        //
        //        String methodSpecificUrl = "/" + sportRoomId.toString() + "/exists";
        //
        //        // Call to SportRoomController in Sport Facilities microservice
        //        Boolean sportHallExists = restTemplate
        //            .getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl, Boolean.class);
        //
        //        if (sportHallExists == null || !sportHallExists) {
        //            return new ResponseEntity<>("The SportRoom requested doesn't exist",
        //                HttpStatus.NOT_FOUND);
        //        }

        return super.checkNext(reservation);
    }
}
