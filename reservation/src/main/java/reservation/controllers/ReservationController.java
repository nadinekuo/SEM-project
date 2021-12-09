package reservation.controllers;

import com.google.gson.Gson;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.services.ReservationService;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    public static final String sportFacilityUrl = "http://eureka-sport-facilities";
    public static final String userUrl = "http://eureka-user";
    protected static final Gson gson = new Gson();
    private final transient ReservationService reservationService;

    @Autowired
    private final transient RestTemplate restTemplate;

    /**
     * Autowired constructor for the class.
     *
     * @param reservationService the reservation service
     */
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
        this.restTemplate = reservationService.restTemplate();
    }

    /**
     * Gets reservation from id.
     *
     * @param reservationId the reservation id
     * @return the reservation
     */
    @GetMapping("/{reservationId}")
    @ResponseBody
    public Reservation getReservation(@PathVariable Long reservationId) {
        return reservationService.getReservation(reservationId);
    }

    //    @GetMapping("/{reservationId}/getStartingTime")
    //    @ResponseBody
    //    public LocalDate getReservationSportRoom(@PathVariable Long reservationId) {
    //        return reservationService.getStartingTime(reservationId);
    //    }

    /**
     * Delete reservation by id.
     *
     * @param reservationId the reservation id
     */
    @DeleteMapping("/{reservationId}")
    @ResponseBody
    public void deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }

    /**
     * Checks if sport room is available.
     *
     * @param sportRoomId the sport room id
     * @param date        the date
     * @return if its available or not
     */
    @GetMapping("/{sportRoomId}/{date}/isAvailable")
    @ResponseBody
    public boolean isAvailable(@PathVariable Long sportRoomId, @PathVariable String date) {
        return reservationService.isAvailable(sportRoomId, LocalDateTime.parse(date));
    }

    // TODO: the 2 methods below have to be combined into 1 Reservation method, which allows
    //  combining Equipment and SportRooms

    /**
     * Make sport room reservation.
     *
     * @param userId      the user id
     * @param sportRoomId the sport room id
     * @param date        the date
     * @param isCombined  if its part of a combined reservation
     * @return A response based on what happened when trying to make the reservation
     */
    @PostMapping("/{userId}/{sportRoomId}/{date}/{isCombined}/makeSportRoomBooking")
    @ResponseBody
    public ResponseEntity<String> makeSportRoomReservation(@PathVariable Long userId,
                                                           @PathVariable Long sportRoomId,
                                                           @PathVariable String date,
                                                           @PathVariable boolean isCombined) {

        // TODO: check if userId exists

        //can throw errors
        LocalDateTime dateTime = LocalDateTime.parse(date);

        if (dateTime.isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("Date and time has to be after now",
                HttpStatus.BAD_REQUEST);
        }

        // TODO: check if no other Sport room booked by that user for the same time

        if ((dateTime.getHour() < 16) || (dateTime.getHour() == 23)) {
            return new ResponseEntity<>("Time has to be between 16:00 and 23:00.",
                HttpStatus.NOT_FOUND);
        }

        // TODO: To be moved to another method (Chain of Responsibility)

        String yearMonthDay = date.substring(0, 9);
        int reservationBalanceOnDate =
            reservationService.getUserReservationCountOnDay(yearMonthDay, userId);

        // Basic users can have 1 reservation per day (Equipment and SportRoom are separated!)
        if (!getUserIsPremium(userId) && reservationBalanceOnDate == 1) {
            return new ResponseEntity<>("No more than 1 reservation per day can be made. ",
                HttpStatus.BAD_REQUEST);
        }

        // Premium users can have up to 3 reservations per day
        if (getUserIsPremium(userId) && reservationBalanceOnDate == 3) {
            return new ResponseEntity<>("No more than 3 reservations per day can be made.",
                HttpStatus.BAD_REQUEST);
        }

        if (!reservationService.isAvailable(sportRoomId, dateTime)) {
            return new ResponseEntity<>("Sport Room is already booked for this time slot.",
                HttpStatus.NOT_FOUND);
        }

        String methodSpecificUrl = "/" + sportRoomId.toString() + "/exists";

        // Call to SportRoomController in Sport Facilities microservice
        Boolean sportHallExists =
            restTemplate.getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl,
                Boolean.class);

        if (sportHallExists == null || !sportHallExists) {
            return new ResponseEntity<>("The SportRoom requested doesn't exist",
                HttpStatus.NOT_FOUND);
        }

        Reservation reservation =
            new Reservation(ReservationType.SPORTS_FACILITY, userId, sportRoomId, dateTime,
                isCombined);
        reservationService.makeSportFacilityReservation(reservation);
        return new ResponseEntity<>("Reservation Successful!", HttpStatus.OK);
    }

    /**
     * Make equipment reservation.
     *
     * @param userId        the user id
     * @param date          the date
     * @param equipmentName the equipment name
     * @param isCombined    the is combined
     * @return A response based on what happened when trying to make the reservation
     */
    @PostMapping("/{userId}/{equipmentName}/{date}/{isCombined}/makeEquipmentBooking")
    @ResponseBody
    public ResponseEntity<String> makeEquipmentReservation(@PathVariable Long userId,
                                                           @PathVariable String date,
                                                           @PathVariable String equipmentName,
                                                           @PathVariable boolean isCombined) {

        // TODO: some code duplication that should be removed when implementing chain of
        //  responsibility
        LocalDateTime dateTime = LocalDateTime.parse(date);
        if (dateTime.isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("Date and time has to be after now",
                HttpStatus.BAD_REQUEST);
        }
        if ((dateTime.getHour() < 16) || (dateTime.getHour() == 23)) {
            return new ResponseEntity<>("Time has to be between 16:00 and 23:00",
                HttpStatus.BAD_REQUEST);
        }

        // TODO: check if equipment available (enough stock)

        // TODO: To be moved to another method (Chain of Responsibility)

        String yearMonthDay = date.substring(0, 9);
        int reservationBalanceOnDate =
            reservationService.getUserReservationCountOnDay(yearMonthDay, userId);

        // Basic users can have 1 reservation per day
        if (!getUserIsPremium(userId) && reservationBalanceOnDate == 1) {
            return new ResponseEntity<>("No more than 1 reservation per day can be made.",
                HttpStatus.BAD_REQUEST);
        }

        // Premium users can have up to 3 reservations per day
        if (getUserIsPremium(userId) && reservationBalanceOnDate == 3) {
            return new ResponseEntity<>("No more than 3 reservations per day can be made. ",
                HttpStatus.BAD_REQUEST);
        }

        String methodSpecificUrl = "/equipment/" + equipmentName + "/getAvailableEquipment";

        Long response =
            restTemplate.getForObject(sportFacilityUrl + methodSpecificUrl, Long.class);

        Reservation reservation =
            new Reservation(ReservationType.EQUIPMENT, userId, response, dateTime, isCombined);
        reservationService.makeSportFacilityReservation(reservation);

        return new ResponseEntity<>("Equipment reservation was successful!", HttpStatus.OK);
    }

    /**
     * Gets if user is premium.
     *
     * @param userId the user id
     * @return if the user is premium
     */
    @GetMapping("/{userId}/isPremium")
    @ResponseBody
    public Boolean getUserIsPremium(@PathVariable Long userId) {
        String methodSpecificUrl = "/user/" + userId + "/isPremium";

        Boolean isPremium = restTemplate.getForObject(userUrl + methodSpecificUrl, Boolean.class);

        return isPremium;
    }

}
