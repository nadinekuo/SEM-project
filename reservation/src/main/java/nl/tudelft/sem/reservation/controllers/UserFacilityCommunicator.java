package nl.tudelft.sem.reservation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class UserFacilityCommunicator {

    /**
     * The constant userUrl.
     */
    private static final String userUrl = "http://eureka-user";

    private final transient RestTemplate restTemplate;

    public UserFacilityCommunicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUserUrl() {
        return userUrl;
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
        ResponseEntity<String> response = restTemplate.getForEntity(userUrl + methodSpecificUrl,
            String.class);
        return Integer.parseInt(response.getBody());
    }

    /**
     * Gets if  exists.
     *
     * @param userId the user id
     * @return if the user exists
     * @throws HttpClientErrorException thrown if user does not exist
     */
    public boolean getUserExists(Long userId) throws HttpClientErrorException {

        String methodSpecificUrl = "/customer/" + userId;

        ResponseEntity<String> response = restTemplate.getForEntity(userUrl + methodSpecificUrl,
            String.class);

        return true;

    }

    /**
     * Get boolean whether user is premium.
     *
     * @param userId user id
     * @return boolean userIsPremium
     */
    public boolean getUserIsPremium(Long userId) {

        String methodSpecificUrl = "/customer/" + userId + "/isPremiumUser";

        // Call to GroupController in User microservice
        ResponseEntity<String> response = restTemplate.getForEntity(userUrl + methodSpecificUrl,
            String.class);
        return Boolean.parseBoolean(response.getBody());
    }

}
