package reservation.controllers;

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
        String response = restTemplate.getForObject(userUrl + methodSpecificUrl, String.class);
        int groupSize = Integer.valueOf(response);
        return groupSize;
    }

}
