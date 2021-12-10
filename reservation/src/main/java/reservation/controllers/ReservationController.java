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

//    /**
//     * Checks if sport room is available.
//     *
//     * @param sportFacilityId the sport room id
//     * @param date        the date
//     * @return if its available or not
//     */
//    @GetMapping("/{sportRoomId}/{date}/isAvailable")
//    @ResponseBody
//    public boolean isAvailable(@PathVariable Long sportFacilityId, @PathVariable String date) {
//        return reservationService.sportsFacilityIsAvailable(sportFacilityId,
//            LocalDateTime.parse(date));
//    }



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

        // Create reservation object, to be passed through chain of responsibility
        Reservation reservation =
            new Reservation(ReservationType.SPORTS_FACILITY, userId, sportRoomId, dateTime);

        // Creates chain of responsibility
        boolean isValid = reservationService.checkReservation(reservation, this);

        // TODO: check if request gives 200

        if (isValid) {
            reservationService.makeSportFacilityReservation(reservation);
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

        LocalDateTime dateTime = LocalDateTime.parse(date);

        // Gets first available instance of this equipment name specified
        String methodSpecificUrl = "/equipment/" + equipmentName + "/getAvailableEquipment";

        String response =
            restTemplate.getForObject(sportFacilityUrl + methodSpecificUrl, String.class);
        Long equipmentId = Long.valueOf(response);

        Reservation reservation =
            new Reservation(ReservationType.EQUIPMENT, userId, equipmentId, dateTime);

        // TODO: check if request gives 200

        // Creates chain of responsibility
        boolean isValid = reservationService.checkReservation(reservation, this);

        if (isValid) {
            reservationService.makeSportFacilityReservation(reservation);
            return new ResponseEntity<>("Reservation Successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation could not be made.",
                HttpStatus.FORBIDDEN);
        }
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

    /**
     * Gets if sports room to be reserved exists.
     *
     * @param sportsRoomId the sports room id
     * @return if the user is premium
     */
    @GetMapping("/{sportsRoomId}/exists")
    @ResponseBody
    public Boolean getSportsRoomExists(@PathVariable Long sportsRoomId) {

        String methodSpecificUrl = "/" + sportsRoomId.toString() + "/exists";

        // Call to SportRoomController in Sport Facilities microservice
        Boolean sportRoomExists = restTemplate
            .getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl, Boolean.class);

        if (sportRoomExists == null) {
            return false;
        }
        return sportRoomExists;
    }


    /**
     * @param sportRoomId the sports room id
     * @return if the to be reserved sports room is a hall, meaning it holds multiple sports.
     */
    @GetMapping("/{sportRoomId}/isHall")
    @ResponseBody
    public Boolean getIsSportHall(@PathVariable Long sportRoomId) {

        String methodSpecificUrl = "/" + sportRoomId.toString() + "/isHall";

        // Call to SportRoomController in Sport Facilities microservice
        Boolean sportRoomIsHall = restTemplate
            .getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl, Boolean.class);

        if (sportRoomIsHall == null) {
            return false;
        }
        return sportRoomIsHall;
    }


    /**
     * Gets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room maximum capacity
     */
    @GetMapping("/{sportRoomId}/getMaximumCapacity")
    @ResponseBody
    public int getSportRoomMaximumCapacity(@PathVariable Long sportRoomId) {

        String methodSpecificUrl = "/" + sportRoomId.toString() + "/getMaximumCapacity";

        // Call to SportRoomController in Sport Facilities microservice
        int maxCapacity = restTemplate
            .getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl, Integer.class);

        return maxCapacity;    // may be -1, but exception is caught in SportRoomController
    }


    /**
     * Gets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room maximum capacity
     */
    @GetMapping("/{sportRoomId}/getMinimumCapacity")
    @ResponseBody
    public int getSportRoomMinimumCapacity(@PathVariable Long sportRoomId) {

        String methodSpecificUrl = "/" + sportRoomId.toString() + "/getMinimumCapacity";

        // Call to SportRoomController in Sport Facilities microservice
        int minCapacity = restTemplate
            .getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl, Integer.class);

        return minCapacity;    // may be -1
    }



    /**
     * @param sportFieldId - id of sport field to be reserved
     * @return String - name of related Sport (id of Sport)
     *    example: soccer, hockey, ...
     */
    @GetMapping("/{sportFieldId}/getSport")
    @ResponseBody
    public String getSportFieldSport(@PathVariable Long sportFieldId) {

        String methodSpecificUrl = "/" + sportFieldId.toString() + "/getSport";

        // Call to SportRoomController in Sport Facilities microservice
        String relatedSport = restTemplate
            .getForObject(sportFacilityUrl + "/sportRoom/" + methodSpecificUrl, String.class);

        return relatedSport;
    }



    /**
     * @param sportName the sport id
     * @return the max team size for this sport
     */
    @GetMapping("/{sportName}/getMaxTeamSize")
    @ResponseBody
    public int getSportMaxTeamSize(@PathVariable String sportName) {

        String methodSpecificUrl = "/" + sportName.toString() + "/getMaxTeamSize";

        // Call to SportRoomController in Sport Facilities microservice
        int maxTeamSize = restTemplate
            .getForObject(sportFacilityUrl + "/sport/" + methodSpecificUrl, Integer.class);

        return maxTeamSize;
    }



    /**
     * @param sportName the sport id
     * @return the min team size for this sport
     */
    @GetMapping("/{sportName}/getMinTeamSize")
    @ResponseBody
    public int getSportMinTeamSize(@PathVariable String sportName) {

        String methodSpecificUrl = "/" + sportName.toString() + "/getMinTeamSize";

        // Call to SportRoomController in Sport Facilities microservice
        int minTeamSize = restTemplate
            .getForObject(sportFacilityUrl + "/sports" + methodSpecificUrl, Integer.class);

        return minTeamSize;
    }


    @GetMapping("/{groupId}/getGroupSize")
    @ResponseBody
    public int getGroupSize(@PathVariable Long groupId) {

        String methodSpecificUrl = "/" + groupId.toString() + "/getGroupSize";

        // Call to GroupController in User microservice
        int groupSize = restTemplate
            .getForObject(userUrl + "/group/" + methodSpecificUrl, Integer.class);

        return groupSize;

    }


}
