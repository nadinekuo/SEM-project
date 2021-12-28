package reservation.controllers;

import java.time.LocalDateTime;
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
import reservation.services.ReservationService;

/**
 * The type Reservation controller.
 */
@RestController
@RequestMapping("reservation")
public class ReservationController {

    /**
     * The constant sportFacilityUrl.
     */
    public static final String sportFacilityUrl = "http://eureka-sport-facilities";
    /**
     * The constant userUrl.
     */
    public static final String userUrl = "http://eureka-user";

    private final transient ReservationService reservationService;

    @Autowired
    private final transient RestTemplate restTemplate;

    /**
     * Instantiates a new Reservation controller.
     *
     * @param reservationService the reservation service
     */
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
        this.restTemplate = reservationService.restTemplate();
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
    @PostMapping("/{userId}/{groupId}/{sportRoomId}/{date}/{madeByPremiumUser}"
        + "/makeSportRoomBooking")
    @ResponseBody
    public ResponseEntity<?> makeSportRoomReservation(@PathVariable Long userId,
                                                           @PathVariable Long groupId,
                                                           @PathVariable Long sportRoomId,
                                                           @PathVariable String date,
                                                           @PathVariable Boolean madeByPremiumUser) {


        LocalDateTime dateTime = LocalDateTime.parse(date);

        String methodSpecificUrl = "/sportRoom/" + sportRoomId + "/getName";
        String sportRoomName = restTemplate.getForObject(sportFacilityUrl + methodSpecificUrl,
            String.class);

        // Create reservation object, to be passed through chain of responsibility
        Reservation reservation =
            new Reservation(ReservationType.SPORTS_ROOM, sportRoomName, userId, sportRoomId,
                dateTime, groupId, madeByPremiumUser);

        // Chain of responsibility:
        // If any condition to be checked is violated by this reservation, the respective
        // validator will throw an InvalidReservationException with appropriate message
        boolean isValid = reservationService.checkReservation(reservation, this);

        if (isValid) {
            reservationService.makeSportFacilityReservation(reservation);
            return new ResponseEntity<>("Reservation successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation could not be made.", HttpStatus.FORBIDDEN);
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
<<<<<<< reservation/src/main/java/reservation/controllers/ReservationController.java
    public ResponseEntity<String> makeEquipmentReservation(@PathVariable Long userId,
                                                           @PathVariable String equipmentName,
                                                           @PathVariable String date,
                                                           @PathVariable Boolean madeByPremiumUser) {
=======
    public ResponseEntity<?> makeEquipmentReservation(@PathVariable Long userId,
                                                      @PathVariable String equipmentName,
                                                      @PathVariable String date) {
>>>>>>> reservation/src/main/java/reservation/controllers/ReservationController.java

        LocalDateTime dateTime = LocalDateTime.parse(date);
        Long equipmentId;
        try {
            equipmentId = getFirstAvailableEquipmentId(equipmentName);
            System.out.println(
                "######### First available equipment id for " + equipmentName + " =" + " "
                    + equipmentId);
        } catch (HttpClientErrorException e) {
            equipmentId = -1L;
            System.out.println(
                "############ Equipment " + equipmentName + " not available / " + "non-existent!!! "
                    + "Set id to -1L in ReservationController.");
        }

        Reservation reservation =
            new Reservation(ReservationType.EQUIPMENT, equipmentName, userId, equipmentId,
                dateTime, madeByPremiumUser);

        // Chain of responsibility:
        // If any condition to be checked is violated by this reservation, the respective
        // validator will throw an InvalidReservationException with appropriate message
        boolean isValid = reservationService.checkReservation(reservation, this);

        if (isValid) {
            reservationService.makeSportFacilityReservation(reservation);
            return new ResponseEntity<>("Reservation successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation could not be made.", HttpStatus.FORBIDDEN);
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

    //  -------------------  The methods below communicate with other microservices
    // Any exceptions that may occur, are caught in that respective microservice.

<<<<<<< reservation/src/main/java/reservation/controllers/ReservationController.java
=======
    /**
     * Gets user is premium.
     *
     * @param userId the user id
     * @return the user is premium
     */
    //TODO change all the get for object to exchanges
    public Boolean getUserIsPremium(Long userId) {
        String methodSpecificUrl = "/user/" + userId + "/isPremium";
        String response = restTemplate.getForObject(userUrl + methodSpecificUrl, String.class);
        Boolean isPremium = Boolean.valueOf(response);
        return isPremium;
    }
>>>>>>> reservation/src/main/java/reservation/controllers/ReservationController.java

    /**
     * Gets sports room exists.
     *
     * @param sportsRoomId the sports room id
     * @return the sports room exists
     */
    public Boolean getSportsRoomExists(Long sportsRoomId) {

        String methodSpecificUrl = "/sportRoom/" + sportsRoomId.toString() + "/exists";

        // Call to SportRoomController in Sport Facilities microservice
        String response = restTemplate
            .getForObject(sportFacilityUrl + methodSpecificUrl, String.class);
        Boolean sportRoomExists = Boolean.valueOf(response);
        return sportRoomExists;
    }

    /**
     * Gets is sport hall.
     *
     * @param sportRoomId the sport room id
     * @return the is sport hall
     */
    public Boolean getIsSportHall(Long sportRoomId) {

        String methodSpecificUrl = "/sportRoom/" + sportRoomId.toString() + "/isHall";

        // Call to SportRoomController in Sport Facilities microservice
        String response = restTemplate
            .getForObject(sportFacilityUrl + methodSpecificUrl, String.class);
        Boolean sportRoomIsHall = Boolean.valueOf(response);

        return sportRoomIsHall;
    }

    /**
     * Gets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room maximum capacity
     */
    public int getSportRoomMaximumCapacity(Long sportRoomId) {

        String methodSpecificUrl = "/sportRoom/" + sportRoomId.toString() + "/getMaximumCapacity";

        // Call to SportRoomController in Sport Facilities microservice
        String response = restTemplate
            .getForObject(sportFacilityUrl + methodSpecificUrl, String.class);
        int maxCapacity = Integer.valueOf(response);

        return maxCapacity;
    }

    /**
     * Gets sport room minimum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room minimum capacity
     */
    public int getSportRoomMinimumCapacity(Long sportRoomId) {
        String methodSpecificUrl = "/sportRoom/" + sportRoomId.toString() + "/getMinimumCapacity";

        // Call to SportRoomController in Sport Facilities microservice
        String response = restTemplate
            .getForObject(sportFacilityUrl + methodSpecificUrl, String.class);
        int minCapacity = Integer.valueOf(response);

        return minCapacity;
    }

    /**
     * Gets first available equipment id.
     *
     * @param equipmentName the equipment name
     * @return the first available equipment id
     */
    public Long getFirstAvailableEquipmentId(String equipmentName) {

        String methodSpecificUrl = "/equipment/" + equipmentName + "/getAvailableEquipment";

        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl,
                String.class);

        Long equipmentId = Long.valueOf(response.getBody());

        setEquipmentToInUse(equipmentId);   // Makes equipment (id) unavailable

        return equipmentId;
    }

    /**
     * Sets reserved piece of equipment (id) to "in use".
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToInUse(Long equipmentId) {

        String methodSpecificUrl = "/equipment/" + equipmentId.toString() + "/reserved";
        restTemplate.put(sportFacilityUrl + methodSpecificUrl, String.class);
    }



    /**
     * Gets sport field sport.
     *
     * @param sportFieldId - id of sport field to be reserved
     * @return String - name of related Sport (id of Sport)
     */
    public String getSportFieldSport(Long sportFieldId) {

        String methodSpecificUrl = "/sportRoom/" + sportFieldId.toString() + "/getSport";

        // Call to SportRoomController in Sport Facilities microservice
        String relatedSport = restTemplate
            .getForObject(sportFacilityUrl + methodSpecificUrl, String.class);

        return relatedSport;
    }

    /**
     * Gets sport max team size.
     *
     * @param sportName the sport name
     * @return the sport max team size
     */
    public int getSportMaxTeamSize(String sportName) {

        String methodSpecificUrl = "/sport/" + sportName + "/getMaxTeamSize";

        // Call to SportRoomController in Sport Facilities microservice
        String response = restTemplate
            .getForObject(sportFacilityUrl + methodSpecificUrl, String.class);
        int maxTeamSize = Integer.valueOf(response);

        return maxTeamSize;
    }

    /**
     * Gets sport min team size.
     *
     * @param sportName the sport name
     * @return the sport min team size
     */
    public int getSportMinTeamSize(String sportName) {

        String methodSpecificUrl = "/sport/" + sportName + "/getMinTeamSize";

        // Call to SportRoomController in Sport Facilities microservice
        String response = restTemplate
            .getForObject(sportFacilityUrl + methodSpecificUrl, String.class);
        int minTeamSize = Integer.valueOf(response);

        return minTeamSize;
    }

    /**
     * Gets group size.
     *
     * @param groupId the group id
     * @return the group size
     */
    public int getGroupSize(Long groupId) {

        String methodSpecificUrl = "/group/" + groupId.toString() + "/getGroupSize";

        // Call to GroupController in User microservice
        String response =
            restTemplate.getForObject(userUrl + methodSpecificUrl, String.class);
        int groupSize = Integer.valueOf(response);
        return groupSize;
    }

}
