package nl.tudelft.sem.sportfacilities.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.sportfacilities.entities.Sport;
import nl.tudelft.sem.sportfacilities.repositories.SportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SportServiceTest {

    @Mock
    private transient SportRepository sportRepository;

    private transient SportService sportService;

    private final transient Sport volleyball;
    private final transient Sport yoga;

    /**
     * Instantiates a new Sport service test.
     */
    public SportServiceTest() {
        volleyball = new Sport("volleyball", 4, 12);
        yoga = new Sport("yoga");
    }

    /**
     * Sets up the tests.
     */
    @BeforeEach
    void setup() {
        sportService = new SportService(sportRepository);
    }

    @Test
    public void constructorTest() {
        assertNotNull(sportService);
    }

    @Test
    public void getSportByIdTest() {

        when(sportRepository.findById(anyString())).thenReturn(Optional.of(volleyball));

        Sport result = sportService.getSportById(volleyball.getSportName());

        assertThat(result).isNotNull();
        assertThat(result.getSportName()).isEqualTo(volleyball.getSportName());
        assertThat(result.getMaxTeamSize()).isEqualTo(12);
        assertThat(result.getMinTeamSize()).isEqualTo(4);
        verify(sportRepository, times(1)).findById(volleyball.getSportName());
    }

    @Test
    public void getNonExistingSportTest() {

        when(sportRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            sportService.getSportById("test");
        });
    }

    @Test
    public void deleteSportTest() {

        when(sportRepository.findById(yoga.getSportName())).thenReturn(Optional.ofNullable(yoga));

        assertDoesNotThrow(() -> {
            sportService.deleteSport(yoga.getSportName());
        });

        verify(sportRepository).findById(yoga.getSportName());
    }

    @Test
    public void deleteNonExistingSportTest() {
        doThrow(new NoSuchElementException()).when(sportRepository).findById("hockey");

        assertThrows(NoSuchElementException.class, () -> {
            sportService.deleteSport("hockey");
        });
    }

    @Test
    public void addSportTest() {

        sportService.addSport(yoga);

        ArgumentCaptor<Sport> sportArgumentCaptor = ArgumentCaptor.forClass(Sport.class);

        verify(sportRepository).save(sportArgumentCaptor.capture());

        Sport capturedSport = sportArgumentCaptor.getValue();
        assertEquals(capturedSport.getSportName(), "yoga");
        assertEquals(capturedSport.getMaxTeamSize(), -1);
        assertEquals(capturedSport.getMinTeamSize(), 1);
    }
}
