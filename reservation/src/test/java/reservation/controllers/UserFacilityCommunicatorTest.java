package reservation.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import reservation.entities.chainofresponsibility.ReservationChecker;
import reservation.services.ReservationService;

/**
 * The type User facility communicator test.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserFacilityCommunicatorTest {

    private static final String sportFacilityUrl = "http://eureka-user";

    /**
     * The User facility communicator.
     */
    UserFacilityCommunicator userFacilityCommunicator;

    /**
     * The Reservation service.
     */
    @Mock
    transient ReservationService reservationService;

    /**
     * The Reservation checker.
     */
    @Mock
    transient ReservationChecker reservationChecker;

    /**
     * The Rest template.
     */
    @Mock
    transient RestTemplate restTemplate;

    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets .
     */
    @BeforeEach
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void setup() {
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);
        userFacilityCommunicator = new UserFacilityCommunicator(restTemplate);
    }

    /**
     * Gets user url test.
     */
    @Test
    public void getUserUrlTest() {
        assertEquals("http://eureka-user", userFacilityCommunicator.getUserUrl());
    }

    /**
     * Gets group size test.
     */
    @Test
    public void getGroupSizeTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, userFacilityCommunicator.getGroupSize(1L));
    }
}
