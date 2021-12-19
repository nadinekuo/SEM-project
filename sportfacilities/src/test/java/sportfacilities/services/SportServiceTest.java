package sportfacilities.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sportfacilities.entities.Sport;
import sportfacilities.entities.SportRoom;
import sportfacilities.repositories.SportRepository;

@ExtendWith(MockitoExtension.class)
public class SportServiceTest {

    @Mock
    private transient SportRepository sportRepository;

    private transient SportService sportService;


    private transient Sport volleyball;
    private transient Sport yoga;

    /**
     * Sets each test.
     */
    @BeforeEach
    void setup() {
        sportService = new SportService(sportRepository);
    }

    /**
     * Instantiates a new Sport service test.
     */
    public SportServiceTest() {
        volleyball = new Sport("volleyball", 4, 12);
        yoga = new Sport("yoga");
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(sportService);
    }

    /**
     * Gets valid sport.
     */
    @Test
    public void getSportByIdTest() {

        when(sportRepository.findById(anyString())).thenReturn(Optional.of(volleyball));

        Sport result = sportService.getSportById("volleyball");

        assertThat(result).isNotNull();
        assertThat(result.getSportName()).isEqualTo("volleyball");
        assertThat(result.getMaxTeamSize()).isEqualTo(12);
        assertThat(result.getMinTeamSize()).isEqualTo(4);
        verify(sportRepository, times(1)).findById("volleyball");
    }


    /**
     * Gets non existing sport.
     */
    @Test
    public void getNonExistingSport() {

        when(sportRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            sportService.getSportById("test");
        });
    }


    /**
     * Deletes valid sport.
     */
    @Test
    public void deleteSport() {

        when(sportRepository.existsById(anyString())).thenReturn(true);

        assertDoesNotThrow(() -> {
            sportService.deleteSport("hockey");
        });

        verify(sportRepository).existsById("hockey");
    }


    /**
     * Deletes invalid sport.
     */
    @Test
    public void deleteNonExistingSport() {

        when(sportRepository.existsById(anyString())).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> {
            sportService.deleteSport("test");
        });
    }



    /**
     * Add sport.
     */
    @Test
    public void addSport() {

        sportService.addSport(yoga);

        ArgumentCaptor<Sport> sportArgumentCaptor =
            ArgumentCaptor.forClass(Sport.class);

        verify(sportRepository).save(sportArgumentCaptor.capture());

        Sport capturedSport = sportArgumentCaptor.getValue();
        assertEquals(capturedSport.getSportName(), "yoga");
        assertEquals(capturedSport.getMaxTeamSize(), -1);
        assertEquals(capturedSport.getMinTeamSize(), 1);
    }





}
