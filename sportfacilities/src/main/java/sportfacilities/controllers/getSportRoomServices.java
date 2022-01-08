package sportfacilities.controllers;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sportfacilities.entities.Sport;
import sportfacilities.entities.SportRoom;
import sportfacilities.services.SportRoomService;

public class getSportRoomServices {


    private final transient SportRoomService sportRoomService;

    public getSportRoomServices(SportRoomService sportRoomService) {
        this.sportRoomService = sportRoomService;
    }

    public ResponseEntity<?> getSportRoom(Long sportRoomId) {
        try {
            SportRoom sportRoom = sportRoomService.getSportRoom(sportRoomId);
            return new ResponseEntity<>(sportRoom, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> getSportRoomName(Long sportRoomId) {
        try {
            SportRoom sportRoom = sportRoomService.getSportRoom(sportRoomId);
            return new ResponseEntity<String>(sportRoom.getSportRoomName(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> getSportRoomMaximumCapacity(Long sportRoomId) {
        try {
            Integer maxCapacity = sportRoomService.getSportRoom(sportRoomId).getMaxCapacity();
            return new ResponseEntity<>(maxCapacity.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> getSportRoomMinimumCapacity(Long sportRoomId) {
        try {
            Integer minCapacity = sportRoomService.getSportRoom(sportRoomId).getMinCapacity();
            return new ResponseEntity<>(minCapacity.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> getSportFieldSport(Long sportFieldId) {
        try {
            Sport relatedSport = sportRoomService.getSportRoom(sportFieldId).getSports().get(0);
            return new ResponseEntity<>(relatedSport.getSportName(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
