package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import security.controllers.AuthenticationController;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {

    transient String customerInfoUrl =
            "/user/{userName}/getCustomerInfo";

    transient String adminInfoUrl =
            "/user/{userName}/getAdminInfo";

    @Mock
    transient RestTemplate restTemplate;

    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
                MockMvcBuilders.standaloneSetup(new AuthenticationController(restTemplate)).build();
    }

    @Test
    public void getCustomerInfoTest() {

        Mockito.when(restTemplate.getForEntity(AuthenticationController.))








        Mockito.when(restTemplate.getForEntity(
                        ReservationController.sportFacilityUrl + "/equipment/" + equipmentNameValid
                                + "/getAvailableEquipment", String.class))
                .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(1L))));

        MvcResult result = mockMvc.perform(
                        post(equipmentBookingUrl, userId, equipmentNameValid, date, madeByPremiumUser))
                .andExpect(status().is4xxClientError()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(
                "Reservation could not be made.");
        verify(reservationService, never()).makeSportFacilityReservation(reservation);

    }
}
