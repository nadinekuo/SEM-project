package reservation.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.ReservationChecker;
import reservation.services.ReservationService;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    private final transient ReservationService reservationService;
    @Autowired
    private final transient RestTemplate restTemplate;
    @Autowired
    private final transient ReservationChecker reservationChecker;

    private final SportFacilityCommunicator sportFacilityCommunicator;

    private final UserFacilityCommunicator userFacilityCommunicator;

    /**
     * Instantiates a new Reservation controller.
     *
     * @param reservationService the reservation service
     */
    @Autowired
    public ReservationController(ReservationService reservationService,
                                 ReservationChecker reservationChecker) {
        this.reservationService = reservationService;
        this.restTemplate = reservationService.restTemplate();
        this.reservationChecker = reservationChecker;
        this.sportFacilityCommunicator = new SportFacilityCommunicator(this.restTemplate);
        this.userFacilityCommunicator = new UserFacilityCommunicator(this.restTemplate);
    }

    public SportFacilityCommunicator getSportFacilityCommunicator() {
        return sportFacilityCommunicator;
    }

    public UserFacilityCommunicator getUserFacilityCommunicator() {
        return userFacilityCommunicator;
    }

    /**
     * Gets reservation.
     *
     * @param reservationId the reservation id
     * @return the reservation
     */
    @GetMapping("/{reservationId}")
    @ResponseBody
    public ResponseEntity<?> getReservation(@PathVariable Long reservationId) {
        try {
            reservationService.getReservation(reservationId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete reservation response entity.
     *
     * @param reservationId the reservation id
     * @return the response entity
     */
    @DeleteMapping("/{reservationId}")
    @ResponseBody
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId) {
        try {
            reservationService.deleteReservation(reservationId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Make sport room reservation response entity.
     *
     * @param userId      the user id
     * @param groupId     the group id
     * @param sportRoomId the sport room id
     * @param date        the date
     * @return the response entity
     */
    @PostMapping(
        "/{userId}/{groupId}/{sportRoomId}/{date}/{madeByPremiumUser}" + "/makeSportRoomBooking")
    @ResponseBody
    public ResponseEntity<?> makeSportRoomReservation(@PathVariable Long userId,
                                                      @PathVariable Long groupId,
                                                      @PathVariable Long sportRoomId,
                                                      @PathVariable String date,
                                                      @PathVariable Boolean madeByPremiumUser) {
        try {

            // Can throw DateTimeParseException if the date is wrongly formatted
            LocalDateTime dateTime = LocalDateTime.parse(date);

            String methodSpecificUrl = "/getSportRoomServices/" + sportRoomId + "/getName";

            // Can throw HttpClientException if status is not OK
            ResponseEntity<String> response = restTemplate.getForEntity(
                sportFacilityCommunicator.getSportFacilityUrl() + methodSpecificUrl, String.class);
            String sportRoomName = response.getBody();

            // Create reservation object, to be passed through chain of responsibility
            Reservation reservation =
                new Reservation(ReservationType.SPORTS_ROOM, sportRoomName, userId, sportRoomId,
                    dateTime, groupId, madeByPremiumUser);

            // Chain of responsibility:
            // If any condition to be checked is violated by this reservation, the respective
            // validator will throw an InvalidReservationException with appropriate message
            reservationChecker.checkReservation(reservation, this);
            reservationService.makeSportFacilityReservation(reservation);
            return new ResponseEntity<>("Reservation successful!", HttpStatus.OK);
        } catch (InvalidReservationException | DateTimeParseException
            | HttpClientErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Make equipment reservation response entity.
     *
     * @param userId        the user id
     * @param equipmentName the equipment name
     * @param date          the date
     * @return the response entity
     */
    @PostMapping("/{userId}/{equipmentName}/{date}/{madeByPremiumUser}/makeEquipmentBooking")
    @ResponseBody
    public ResponseEntity<?> makeEquipmentReservation(@PathVariable Long userId,
                                                      @PathVariable String equipmentName,
                                                      @PathVariable String date,
                                                      @PathVariable Boolean madeByPremiumUser) {
        Reservation reservation;
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date);
            reservation =
                    new Reservation(ReservationType.EQUIPMENT, equipmentName,
                            createEquipmentId(equipmentName), userId, dateTime, madeByPremiumUser);
            // Chain of responsibility
            reservationChecker.checkReservation(reservation, this);
        } catch (InvalidReservationException
                | HttpClientErrorException | DateTimeParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        reservationService.makeSportFacilityReservation(reservation);
        return new ResponseEntity<>("Reservation successful!", HttpStatus.OK);
    }


    /**
     * Creates equipment Id.
     *
     * @param equipmentName equipment name
     * @return Long equipmentId
     */
    public Long createEquipmentId(String equipmentName) {
        Long equipmentId;
        try {
            equipmentId = sportFacilityCommunicator.getFirstAvailableEquipmentId(equipmentName);
        } catch (HttpClientErrorException e) {
            equipmentId = -1L;
        }
        return equipmentId;
    }

    /**
     * Make lesson reservation response entity.
     *
     * @param userId   the user id
     * @param lessonId the lesson id
     * @return the response entity
     */
    @PostMapping("/{userId}/{lessonId}/makeLessonBooking")
    @ResponseBody
    public ResponseEntity<?> makeLessonReservation(@PathVariable Long userId,
                                                   @PathVariable Long lessonId) {

        try {
            String lessonName = sportFacilityCommunicator.getLessonName(lessonId);

            LocalDateTime lessonBeginning = sportFacilityCommunicator.getLessonBeginning(lessonId);

            Boolean madeByPremiumUser = userFacilityCommunicator.getUserIsPremium(userId);
            Reservation reservation =
                        new Reservation(ReservationType.LESSON, lessonName, userId, lessonId,
                            lessonBeginning, madeByPremiumUser);
            reservationService.makeSportFacilityReservation(reservation);

            return new ResponseEntity<>("Lesson booking was successful!", HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets last person that used equipment.
     *
     * @param equipmentId the equipment id
     * @return the last person that used equipment
     */
    @GetMapping("/{equipmentId}/lastPersonThatUsedEquipment")
    @ResponseBody
    public ResponseEntity<?> getLastPersonThatUsedEquipment(@PathVariable Long equipmentId) {
        try {
            Long lastPerson = reservationService.getLastPersonThatUsedEquipment(equipmentId);
            return new ResponseEntity<>(lastPerson, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
