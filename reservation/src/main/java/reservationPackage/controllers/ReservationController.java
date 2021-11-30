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

        @GetMapping("/{sportRoomId}/{date}/isAvailable")
        @ResponseBody
        public boolean isAvailable(@PathVariable Long sportRoomId, @PathVariable LocalDateTime
        date) {
            return reservationService.isAvailable(sportRoomId, date);
        }

}
