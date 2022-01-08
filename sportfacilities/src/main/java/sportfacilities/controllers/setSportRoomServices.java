package sportfacilities.controllers;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sportfacilities.services.SportRoomService;

public class setSportRoomServices {


    private final transient SportRoomService sportRoomService;

    public setSportRoomServices(SportRoomService sportRoomService) {
        this.sportRoomService = sportRoomService;
    }

    public ResponseEntity<?> setSportRoomName(Long sportRoomId, String sportRoomName) {
        try {
            sportRoomService.setSportRoomName(sportRoomId, sportRoomName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> setSportRoomMinimumCapacity(Long sportRoomId, int minCapacity) {
        try {
            sportRoomService.setSportRoomMinCapacity(sportRoomId, minCapacity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> setSportRoomMaximumCapacity(Long sportRoomId, int maxCapacity) {
        try {
            sportRoomService.setSportRoomMaxCapacity(sportRoomId, maxCapacity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}

