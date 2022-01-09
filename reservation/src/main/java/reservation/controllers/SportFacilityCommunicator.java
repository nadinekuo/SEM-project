package reservation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class SportFacilityCommunicator {

    private static final String sportFacilityUrl = "http://eureka-sport-facilities";

    private final transient RestTemplate restTemplate;

    /**
     * Instantiates a new Sport facility communicator.
     *
     * @param restTemplate the rest template
     */
    public SportFacilityCommunicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getSportFacilityUrl() {
        return sportFacilityUrl;
    }

    /**
     * Gets sports room exists.
     *
     * @param sportsRoomId the sports room id
     * @return the sports room exists
     */
    public Boolean getSportsRoomExists(Long sportsRoomId) throws HttpClientErrorException {

        String methodSpecificUrl = "/sportRoom/" + sportsRoomId.toString() + "/exists";

        // Call to SportRoomController in Sport Facilities microservice
        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        return Boolean.valueOf(response.getBody());
    }

    /**
     * Gets is sport hall.
     *
     * @param sportRoomId the sport room id
     * @return the is sport hall
     */
    public Boolean getIsSportHall(Long sportRoomId) throws HttpClientErrorException {

        String methodSpecificUrl = "/sportRoom/" + sportRoomId.toString() + "/isHall";

        // Call to SportRoomController in Sport Facilities microservice
        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        return Boolean.valueOf(response.getBody());
    }

    /**
     * Gets sport room maximum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room maximum capacity
     */
    public int getSportRoomMaximumCapacity(Long sportRoomId) throws HttpClientErrorException {

        String methodSpecificUrl =
            "/getSportRoomServices/" + sportRoomId.toString() + "/getMaximumCapacity";

        // Call to SportRoomController in Sport Facilities microservice
        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        return Integer.parseInt(response.getBody());
    }

    /**
     * Gets sport room minimum capacity.
     *
     * @param sportRoomId the sport room id
     * @return the sport room minimum capacity
     */
    public int getSportRoomMinimumCapacity(Long sportRoomId) {
        String methodSpecificUrl =
            "/getSportRoomServices/" + sportRoomId.toString() + "/getMinimumCapacity";

        // Call to SportRoomController in Sport Facilities microservice
        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        return Integer.parseInt(response.getBody());
    }

    /**
     * Gets first available equipment id.
     *
     * @param equipmentName the equipment name
     * @return the first available equipment id
     */
    public Long getFirstAvailableEquipmentId(String equipmentName) throws HttpClientErrorException {

        String methodSpecificUrl = "/equipment/" + equipmentName + "/getAvailableEquipment";

        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        Long equipmentId = Long.valueOf(response.getBody());

        setEquipmentToInUse(equipmentId);   // Makes equipment (id) unavailable

        return equipmentId;
    }

    /**
     * Sets reserved piece of equipment (id) to "in use".
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentToInUse(Long equipmentId) throws HttpClientErrorException {

        String methodSpecificUrl = "/equipment/" + equipmentId.toString() + "/reserved";
        restTemplate.put(sportFacilityUrl + methodSpecificUrl, String.class);
    }

    /**
     * Gets sport field sport.
     *
     * @param sportFieldId - id of sport field to be reserved
     * @return String - name of related Sport (id of Sport)
     */
    public String getSportFieldSport(Long sportFieldId) throws HttpClientErrorException {

        String methodSpecificUrl = "/getSportRoomServices/" + sportFieldId.toString() + "/getSport";

        // Call to SportRoomController in Sport Facilities microservice
        ResponseEntity<String> relatedSport =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        return relatedSport.getBody();
    }

    /**
     * Gets sport max team size.
     *
     * @param sportName the sport name
     * @return the sport max team size
     */
    public int getSportMaxTeamSize(String sportName) throws HttpClientErrorException {

        String methodSpecificUrl = "/sport/" + sportName + "/getMaxTeamSize";

        // Call to SportRoomController in Sport Facilities microservice
        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        return Integer.parseInt(response.getBody());
    }

    /**
     * Gets sport min team size.
     *
     * @param sportName the sport name
     * @return the sport min team size
     */
    public int getSportMinTeamSize(String sportName) throws HttpClientErrorException {

        String methodSpecificUrl = "/sport/" + sportName + "/getMinTeamSize";

        // Call to SportRoomController in Sport Facilities microservice
        ResponseEntity<String> response =
            restTemplate.getForEntity(sportFacilityUrl + methodSpecificUrl, String.class);

        return Integer.parseInt(response.getBody());
    }

}
