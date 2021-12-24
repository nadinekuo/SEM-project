package reservation.controllers;

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
     * Make sport room reservation.
     *
     * @param userId      the user id
     * @param groupId     the group id, passed -1 if it's not a group reservation
     * @param sportRoomId the sport room id
     * @param date        the date
     * @return A response based on what happened when trying to make the reservation
     */
    @PostMapping("/{userId}/{groupId}/{sportRoomId}/{date}/makeSportRoomBooking")
    @ResponseBody
    public ResponseEntity<String> makeSportRoomReservation(@PathVariable Long userId,
                                                           @PathVariable Long groupId,
                                                           @PathVariable Long sportRoomId,
                                                           @PathVariable String date) {

        LocalDateTime dateTime = LocalDateTime.parse(date);

        // Create reservation object, to be passed through chain of responsibility
        Reservation reservation =
            new Reservation(ReservationType.SPORTS_ROOM, userId, sportRoomId, dateTime, groupId);

        // Creates chain of responsibility
        boolean isValid = reservationService.checkReservation(reservation, this);

        if (isValid) {
            reservationService.makeSportFacilityReservation(reservation);
            return new ResponseEntity<>("Reservation successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation could not be made.", HttpStatus.FORBIDDEN);
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
                                                           @PathVariable String equipmentName,
                                                           @PathVariable String date) {

        LocalDateTime dateTime = LocalDateTime.parse(date);
        Long equipmentId;
        try {
            equipmentId = getFirstAvailableEquipmentId(equipmentName);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Reservation reservation =
            new Reservation(ReservationType.EQUIPMENT, userId, equipmentId, dateTime);

        // Creates chain of responsibility
        boolean isValid = reservationService.checkReservation(reservation, this);

        if (isValid) {
            reservationService.makeSportFacilityReservation(reservation);
            return new ResponseEntity<>("Reservation successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation could not be made.", HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/{equipmentId}/lastPersonThatUsedEquipment")
    @ResponseBody
    public Long getLastPersonThatUsedEquipment(@PathVariable Long equipmentId) {
        return reservationService.getLastPersonThatUsedEquipment(equipmentId);
    }

    //  -------------------  The methods below communicate with other microservices.

    /**
     * Gets if user is premium.
     *
     * @param userId the user id
     * @return if the user is premium
     */
    public Boolean getUserIsPremium(Long userId) {
        String methodSpecificUrl = "/user/" + userId + "/isPremium";
        String response = restTemplate.getForObject(userUrl + methodSpecificUrl, String.class);
        Boolean isPremium = Boolean.valueOf(response);
        return isPremium;
    }

    /**
     * Gets if sports room to be reserved exists.
     *
     * @param sportsRoomId the sports room id
     * @return if the user is premium
     */
    public Boolean getSportsRoomExists(Long sportsRoomId) {

        String methodSpecificUrl = "/" + sportsRoomId.toString() + "/exists";

        // Call to SportRoomController in Sport Facilities microservice
        String response =
            restTemplate.getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl,
                String.class);
        Boolean sportRoomExists = Boolean.valueOf(response);
        return sportRoomExists;
    }

    /**
     * Returns whether or not this room is a sport hall.
     *
     * @param sportRoomId the sports room id
     * @return if the to be reserved sports room is a hall, meaning it holds multiple sports.
     */
    public Boolean getIsSportHall(Long sportRoomId) {

        String methodSpecificUrl = "/" + sportRoomId.toString() + "/isHall";

        // Call to SportRoomController in Sport Facilities microservice
        String response =
            restTemplate.getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl,
                String.class);
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

        String methodSpecificUrl = "/" + sportRoomId.toString() + "/getMaximumCapacity";

        // Call to SportRoomController in Sport Facilities microservice
        String response =
            restTemplate.getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl,
                String.class);
        int maxCapacity = Integer.valueOf(response);

        return maxCapacity;
    }

    /**
     * Gets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room maximum capacity
     */
    public int getSportRoomMinimumCapacity(Long sportRoomId) {
        String methodSpecificUrl = "/" + sportRoomId.toString() + "/getMinimumCapacity";

        // Call to SportRoomController in Sport Facilities microservice
        String response =
            restTemplate.getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl,
                String.class);
        int minCapacity = Integer.valueOf(response);

        return minCapacity;
    }

    /**
     * Gets sport room maximum capacity.
     *
     * @param equipmentName the sport room id
     * @return first available instance of this equipment name specified
     */
    public Long getFirstAvailableEquipmentId(String equipmentName) {

        String methodSpecificUrl = "/equipment/" + equipmentName + "/getAvailableEquipment";

        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        return Long.valueOf(response.getBody());
    }

    /**
     * Gets the related sport of the sport field.
     *
     * @param sportFieldId - id of sport field to be reserved
     * @return String - name of related Sport (id of Sport)
     */
    public String getSportFieldSport(Long sportFieldId) {

        String methodSpecificUrl = "/" + sportFieldId.toString() + "/getSport";

        // Call to SportRoomController in Sport Facilities microservice
        String relatedSport =
            restTemplate.getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl,
                String.class);

        return relatedSport;
    }

    /**
     * Gets the max team size needed to reserve a field for this sport.
     *
     * @param sportName the sport id
     * @return the max team size for this sport
     */
    public int getSportMaxTeamSize(String sportName) {

        String methodSpecificUrl = "/" + sportName + "/getMaxTeamSize";

        // Call to SportRoomController in Sport Facilities microservice
        String response =
            restTemplate.getForObject(sportFacilityUrl + "/sport/" + methodSpecificUrl,
                String.class);
        int maxTeamSize = Integer.valueOf(response);

        return maxTeamSize;
    }

    /**
     * Gets the min team size needed to reserve a field for this sport.
     *
     * @param sportName the sport id
     * @return the min team size for this sport
     */
    public int getSportMinTeamSize(String sportName) {

        String methodSpecificUrl = "/" + sportName + "/getMinTeamSize";

        // Call to SportRoomController in Sport Facilities microservice
        String response = restTemplate.getForObject(sportFacilityUrl + "/sport" + methodSpecificUrl,
            String.class);
        int minTeamSize = Integer.valueOf(response);

        return minTeamSize;
    }

    /**
     * Returns how many members are part of this group.
     *
     * @param groupId - Long
     * @return group size - int
     */
    public int getGroupSize(Long groupId) {

        String methodSpecificUrl = "/" + groupId.toString() + "/getGroupSize";

        // Call to GroupController in User microservice
        String response =
            restTemplate.getForObject(userUrl + "/group/" + methodSpecificUrl, String.class);
        int groupSize = Integer.valueOf(response);
        return groupSize;
    }

}
