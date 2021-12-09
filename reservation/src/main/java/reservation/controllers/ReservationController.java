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
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.ReservationValidator;
import reservation.entities.chainofresponsibility.SportFacilityAvailabilityValidator;
import reservation.entities.chainofresponsibility.TeamRoomCapacityValidator;
import reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
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
     * @return A response based on what happened when trying to make the reservation
     */
    @PostMapping("/{userId}/{sportRoomId}/{date}/makeSportRoomBooking")
    @ResponseBody
    public ResponseEntity<String> makeSportRoomReservation(@PathVariable Long userId,
                                                           @PathVariable Long sportRoomId,
                                                           @PathVariable String date) {

        LocalDateTime dateTime = LocalDateTime.parse(date);

        Reservation reservation =
            new Reservation(ReservationType.SPORTS_FACILITY, userId, sportRoomId, dateTime);
        reservationService.makeSportFacilityReservation(reservation);

        boolean isPremium = getUserIsPremium(userId);
        boolean isValid = reservationService.checkSportRoomReservation(reservation, isPremium);

        if (isValid) {
            return new ResponseEntity<>("Reservation Successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation could not be made.",
                HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Make equipment reservation.
     *
     * @param userId        the user id
     * @param date          the date
     * @param equipmentName the equipment name
     * @return A response based on what happened when trying to make the reservation
     */
    @PostMapping("/{userId}/{equipmentName}/{date}/makeEquipmentBooking")
    @ResponseBody
    public ResponseEntity<String> makeEquipmentReservation(@PathVariable Long userId,
                                                           @PathVariable String date,
                                                           @PathVariable String equipmentName) {

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

        String methodSpecificUrl = "/equipment/" + equipmentName + "/getAvailableEquipment";

        Long response = restTemplate.getForObject(sportFacilityUrl + methodSpecificUrl, Long.class);

        Reservation reservation =
            new Reservation(ReservationType.EQUIPMENT, userId, response, dateTime);
        reservationService.makeSportFacilityReservation(reservation);

        // todo: Send through chain of responsibility

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
