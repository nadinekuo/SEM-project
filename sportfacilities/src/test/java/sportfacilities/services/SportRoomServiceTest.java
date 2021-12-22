package sportfacilities.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

/**
 * The type Sport room service test.
 */
@ExtendWith(MockitoExtension.class)
public class SportRoomServiceTest {

    private final transient Sport hockey;
    private final transient Sport volleyball;
    private final transient Sport yoga;
    private final transient Sport zumba;
    private final transient Sport kickboxing;
    private final transient Sport soccer;
    private final transient Long id1 = 34L;
    private final transient SportRoom hallX2;

    private final transient Long id2 = 84L;
    private final transient SportRoom hallX3;
    private final transient long id3 = 38L;
    private final transient long idHockey = 42L;

    private final transient SportRoom hockeyField;
    private final transient SportRoom hallX1;

    @Mock
    private transient SportRoomRepository sportRoomRepository;
    @Mock
    private transient SportService sportService;
    private transient SportRoomService sportRoomService;

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        sportRoomRepository = Mockito.mock(SportRoomRepository.class);
        sportService = Mockito.mock(SportService.class);
        sportRoomService = new SportRoomService(sportRoomRepository, sportService);
    }

    /**
     * Instantiates a new Sport room service test.
     */
    public SportRoomServiceTest() {
        soccer = new Sport("soccer", 6, 11);
        hockey = new Sport("hockey", 7, 14);
        volleyball = new Sport("volleyball", 4, 12);
        yoga = new Sport("yoga");
        zumba = new Sport("zumba");
        kickboxing = new Sport("kickbox");

        hallX1 = new SportRoom( "X1", List.of(soccer, hockey), 10, 50, true);
        hallX1.setId(id1);
        hallX2 = new SportRoom( "X2", List.of(hockey, volleyball, zumba), 15, 60, true);
        hallX2.setId(id2);
        hallX3 = new SportRoom( "X3", List.of(yoga, zumba, kickboxing), 12, 55, true);
        hallX3.setId(id3);
        hockeyField = new SportRoom("hockeyfieldA", List.of(hockey), 10, 200, false);
        hockeyField.setId(idHockey);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(sportRoomService);
    }

    /**
     * Gets sports room.
     */
    @Test
    public void getSportsRoom() {

        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.of(hallX1));

        SportRoom result = sportRoomService.getSportRoom(id1);

        assertThat(result).isNotNull();
        assertThat(result.getSportRoomId()).isEqualTo(id1);
        assertThat(result.getIsSportsHall()).isTrue();
        assertThat(result.getSportRoomName()).isEqualTo("X1");

        verify(sportRoomRepository).findBySportRoomId(id1);
    }

    /**
     * Gets non existing sports room.
     */
    @Test
    public void getNonExistingSportsRoom() {

        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            sportRoomService.getSportRoom(id1);
        });
    }

    /**
     * Sports room exists.
     */
    @Test
    public void sportsRoomExists() {

        when(sportRoomRepository.findBySportRoomId(id2)).thenReturn(Optional.of(hallX2));

        assertThat(sportRoomService.sportRoomExists(id2)).isTrue();
    }

    /**
     * Sports room does not exist.
     */
    @Test
    public void sportsRoomDoesNotExist() {
        when(sportRoomRepository.findBySportRoomId(id2)).thenReturn(Optional.empty());

        assertThat(sportRoomService.sportRoomExists(id2)).isFalse();
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
    public void addSportRoomTestException() throws Exception {
        when(sportService.getSportById("soBBer")).thenThrow(new IllegalStateException());

        assertThrows(IllegalStateException.class,
            () -> sportRoomService.addSportRoom("X3", "soBBer", 12, 55, true));

    }

    @Test
    public void deleteSportRoomTest() throws Exception {
        doNothing().when(sportRoomRepository).deleteBySportRoomId(id3);
        assertDoesNotThrow(() -> sportRoomService.deleteSportRoom(id3));
    }

    @Test
    public void deleteSportRoomWithNonExistentId() throws Exception {
        doThrow(new NoSuchElementException()).when(sportRoomRepository).deleteBySportRoomId(1000L);
        assertThrows(NoSuchElementException.class, () -> sportRoomService.deleteSportRoom(1000L));
    }

    /**
     * Rest template test.
     */
    @Test
    public void restTemplateTest() {
        RestTemplate restTemplate = sportRoomService.restTemplate();
        assertNotNull(restTemplate);
    }

}
