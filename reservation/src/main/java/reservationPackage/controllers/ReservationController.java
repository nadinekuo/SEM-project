package reservationPackage.controllers;

import java.time.LocalDate;
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
import reservationPackage.entities.Reservation;
import reservationPackage.entities.ReservationType;
import reservationPackage.services.ReservationService;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    private final transient ReservationService reservationService;

    static String equipmentUrl = "http://eureka-sport-facilities/equipment";

    @Autowired
    private final RestTemplate restTemplate;

    /**
     * Autowired constructor for the class.
     *
     * @param reservationService sportRoomService
     */
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
        this.restTemplate = reservationService.restTemplate();
    }

    @GetMapping("/hey")
    @ResponseBody
    public Long getReservation() {
        return reservationService.getReservation(1L).getCustomerId();
    }

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

    @DeleteMapping("/{reservationId}")
    @ResponseBody
    public void deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }

    @GetMapping("/{sportRoomId}/{date}/isAvailable")
    @ResponseBody
    public boolean isAvailable(@PathVariable Long sportRoomId, @PathVariable String date) {
        return reservationService.isAvailable(sportRoomId, LocalDateTime.parse(date));
    }


    @PostMapping("/{userId}/{sportRoomId}/{date}/makeSportRoomBooking")
    @ResponseBody
    public boolean makeSportRoomReservation(
        @PathVariable Long userId,
        @PathVariable Long sportRoomId,
        @PathVariable String date) {

        //can throw errors
        LocalDateTime dateTime = LocalDateTime.parse(date);

        if (!reservationService.isAvailable(sportRoomId, dateTime))
            return new ResponseEntity<>("Sport Room is NOT available.", HttpStatus.NOT_FOUND);

        Reservation reservation = new Reservation(ReservationType.SPORTS_FACILITY, userId,
            sportRoomId, dateTime);
        Reservation reservationMade =
            reservationService.makeSportFacilityReservation(reservation);
        return new ResponseEntity<>("Reservation Successful!", HttpStatus.OK);
    }

    @PostMapping("/{userId}/{equipmentName}/{date}/makeEquipmentBooking")
    @ResponseBody
    public boolean makeEquipmentReservation(
        @PathVariable Long userId,
        @PathVariable String date,
        @PathVariable String equipmentName
        ) {
        String methodSpecificUrl = "/" + equipmentName + "/getAvailableEquipment";

        //can throw errors
        LocalDateTime dateTime = LocalDateTime.parse(date);

        Long equipmentId = restTemplate.getForObject(equipmentUrl + methodSpecificUrl, Long.class);
        if (equipmentId == null) return false;

        Reservation reservation = new Reservation(ReservationType.EQUIPMENT, userId,
            equipmentId, dateTime);
        Reservation reservationMade =
            reservationService.makeSportFacilityReservation(reservation);
        return true;
        //return Response.status(Response.Status.ACCEPTED);
    }




}
