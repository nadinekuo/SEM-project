package reservationPackage.controllers;

public class ReservationController {

    private final transient ReservationService reservationService;

    /**
     * Autowired constructor for the class.
     *
     * @param reservationService sportRoomService
     */
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
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

    @GetMapping("/{reservationId}/getStartingTime")
    @ResponseBody
    public LocalDate getReservationSportRoom(@PathVariable Long reservationId) {
        return reservationService.getStartingTime(reservationId);
    }

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


    @PostMapping("/{userId}/{sportRoomId}/{date}/{type}/makeBooking")
    @ResponseBody
    public boolean makeSportRoomReservation(
        @PathVariable Long userId,
        @PathVariable Long sportRoomId,
        @PathVariable String date,
        @PathVariable String type) {

        //can throw errors
        LocalDateTime dateTime = LocalDateTime.parse(date);
        ReservationType reservationType = ReservationType.valueOf(type);

        if (!reservationService.isAvailable(sportRoomId, dateTime)) return false;

        Reservation reservation = new Reservation(userId, sportRoomId, dateTime, reservationType);
        Reservation reservationMade =
            reservationService.makeSportRoomReservation(reservation);
        return true;
    }



}
