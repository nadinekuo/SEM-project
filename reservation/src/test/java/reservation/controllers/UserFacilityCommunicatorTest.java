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

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserFacilityCommunicatorTest {

    UserFacilityCommunicator userFacilityCommunicator;

    @Mock
    transient ReservationService reservationService;

    @Mock
    transient RestTemplate restTemplate;

    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets up the tests .
     */
    @BeforeEach
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void setup() {
        Mockito.when(reservationService.restTemplate()).thenReturn(restTemplate);
        userFacilityCommunicator = new UserFacilityCommunicator(restTemplate);
    }

    @Test
    public void getUserUrlTest() {
        assertEquals("http://eureka-user", userFacilityCommunicator.getUserUrl());
    }

    @Test
    public void getGroupSizeTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
            .thenReturn(ResponseEntity.of(Optional.of(String.valueOf(5))));
        assertEquals(5, userFacilityCommunicator.getGroupSize(1L));
    }
}
