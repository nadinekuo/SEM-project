package sportfacilities.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import sportfacilities.entities.Sport;
import sportfacilities.entities.SportRoom;
import sportfacilities.repositories.SportRoomRepository;

@ExtendWith(MockitoExtension.class)
public class SportRoomServiceTest {

    private transient Sport hockey;
    private transient Sport yoga;
    private transient Sport soccer;
    private final transient Long id1 = 34L;
    private transient SportRoom hallX2;

    private final transient Long id2 = 84L;
    private transient SportRoom hallX3;
    private final transient long id3 = 38L;
    private final transient long idHockey = 42L;

    private transient SportRoom hockeyField;
    private transient SportRoom hallX1;

    @Mock
    private transient SportRoomRepository sportRoomRepository;
    @Mock
    private transient SportService sportService;
    private transient SportRoomService sportRoomService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    void setup() {
        sportService = Mockito.mock(SportService.class);
        sportRoomRepository = Mockito.mock(SportRoomRepository.class);
        sportService = Mockito.mock(SportService.class);
        sportRoomService = new SportRoomService(sportRoomRepository, sportService);

        soccer = new Sport("soccer", 11, 22);
        hockey = new Sport("hockey", 5, 10);
        yoga = new Sport("yoga");

        hallX1 = new SportRoom("X1", new ArrayList<>(), 10, 50, true);
        hallX1.addSport(hockey);
        hallX1.addSport(soccer);
        hallX1.setId(id1);

        hallX2 = new SportRoom("X2", List.of(hockey, soccer), 15, 60, true);
        hallX2.setId(id2);

        hallX3 = new SportRoom("X3", List.of(yoga, soccer, hockey), 12, 55, true);
        hallX3.setId(id3);

        hockeyField = new SportRoom("hockeyfieldA", new ArrayList<>(), 10, 200, false);
        hockeyField.addSport(hockey);
        hockeyField.setId(idHockey);
    }

    @Test
    public void addSportToSportHallTest() {
        when(sportService.getSportById(yoga.getSportName())).thenReturn(yoga);
        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.ofNullable(hallX1));

        sportRoomService.addSportToSportsHall(id1, yoga.getSportName());
        verify(sportRoomRepository).save(hallX1);
        assertThat(hallX1.getSports().get(2)).isEqualTo(yoga);

    }

    @Test
    public void addSportToSportHallTestException() {
        when(sportService.getSportById(yoga.getSportName())).thenReturn(yoga);
        when(sportRoomRepository.findBySportRoomId(id2)).thenReturn(
            Optional.ofNullable(hockeyField));

        assertThrows(IllegalArgumentException.class,
            () -> sportRoomService.addSportToSportsHall(id2, yoga.getSportName()));

        verify(sportRoomRepository, times(0)).save(hockeyField);

    }

    @Test
    public void ConstructorTest() {
        assertNotNull(sportRoomService);
    }

    @Test
    public void getSportsRoomTest() {

        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.of(hallX1));

        SportRoom result = sportRoomService.getSportRoom(id1);

        assertThat(result).isNotNull();
        assertThat(result.getSportRoomId()).isEqualTo(id1);
        assertThat(result.getIsSportsHall()).isTrue();
        assertThat(result.getSportRoomName()).isEqualTo("X1");

        verify(sportRoomRepository).findBySportRoomId(id1);
    }

    @Test
    public void getNonExistingSportsRoomTest() {

        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            sportRoomService.getSportRoom(id1);
        });
    }

    @Test
    public void sportsRoomExistsTest() {

        when(sportRoomRepository.findBySportRoomId(id2)).thenReturn(Optional.of(hallX1));

        assertThat(sportRoomService.sportRoomExists(id2)).isTrue();
    }

    @Test
    public void sportsRoomDoesNotExistTest() {
        when(sportRoomRepository.findBySportRoomId(id2)).thenReturn(Optional.empty());

        assertThat(sportRoomService.sportRoomExists(id2)).isFalse();
    }

    @Test
    public void setSportRoomMinCapacityTest() {
        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.of(hallX1));
        sportRoomService.setSportRoomMinCapacity(hallX1.getSportRoomId(), 5);
        assertEquals(5, hallX1.getMinCapacity());
    }

    @Test
    public void setSportRoomMaxCapacityTest() {
        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.of(hallX1));
        sportRoomService.setSportRoomMaxCapacity(hallX1.getSportRoomId(), 5);
        assertEquals(5, hallX1.getMaxCapacity());
    }

    @Test
    public void setSportRoomNameTest() {
        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.of(hallX1));
        sportRoomService.setSportRoomName(hallX1.getSportRoomId(), "Hall 6");
        assertEquals("Hall 6", hallX1.getSportRoomName());
    }

    @Test
    public void addSportRoomTest() throws Exception {
        when(sportService.getSportById("soccer")).thenReturn(soccer);
        sportRoomService.addSportRoom("X3", "soccer", 12, 55, true);

        ArgumentCaptor<SportRoom> sportRoomArgumentCaptor =
            ArgumentCaptor.forClass(SportRoom.class);

        verify(sportRoomRepository).save(sportRoomArgumentCaptor.capture());

        SportRoom capturedSportRoom = sportRoomArgumentCaptor.getValue();
        Assertions.assertEquals(capturedSportRoom.getSportRoomName(), "X3");
    }

    @Test
    public void addSportRoomExceptionTest() throws Exception {
        when(sportService.getSportById("soBBer")).thenThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class,
            () -> sportRoomService.addSportRoom("X3", "soBBer", 12, 55, true));

    }

    @Test
    public void deleteSportRoomTest() throws Exception {
        doNothing().when(sportRoomRepository).deleteBySportRoomId(id3);
        assertDoesNotThrow(() -> sportRoomService.deleteSportRoom(id3));
    }

    @Test
    public void deleteSportRoomWithNonExistentIdTest() throws Exception {
        doThrow(new NoSuchElementException()).when(sportRoomRepository).deleteBySportRoomId(1000L);
        assertThrows(NoSuchElementException.class, () -> sportRoomService.deleteSportRoom(1000L));
    }

    @Test
    public void restTemplateTest() {
        RestTemplate restTemplate = sportRoomService.restTemplate();
        assertNotNull(restTemplate);
    }

}
