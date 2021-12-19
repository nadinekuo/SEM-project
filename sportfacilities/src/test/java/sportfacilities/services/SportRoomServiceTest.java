package sportfacilities.services;

import static java.util.Arrays.asList;
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
import static org.mockito.MockitoAnnotations.initMocks;

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

/**
 * The type Sport room service test.
 */
@ExtendWith(MockitoExtension.class)
public class SportRoomServiceTest {

    List<Sport> sports = new ArrayList<>();

    private final transient Sport yoga =
        new Sport("yoga", false, 1, -1);
    private final transient Sport soccer =
        new Sport("soccer", true, 6, 11);
    private final transient Long id1 = 34L;
    private final transient Long id2 = 84L;
    private final transient long id3 = 1L;
    private final transient SportRoom field =
        new SportRoom("field1", sports, 10, 200,
            false);
    private final transient SportRoom hallX1 =
        new SportRoom("X1", sports, 10, 50,
            true);
    @Mock
    private transient SportRoomRepository sportRoomRepository;
    @Mock
    private transient SportService sportService;
    private transient SportRoomService sportRoomService;

    /**
     * Instantiates a new Sport room service test.
     */
    public SportRoomServiceTest() {

    }

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        sportService = Mockito.mock(SportService.class);
        sportRoomRepository = Mockito.mock(SportRoomRepository.class);
        sportRoomService = new SportRoomService(sportRoomRepository, sportService);
        hallX1.setId(id1);
    }

    @Test
    public void addSportToSportHallTest() {
        when(sportService.getSportById(yoga.getSportName())).thenReturn(yoga);
        when(sportRoomRepository.findBySportRoomId(id1)).thenReturn(Optional.ofNullable(hallX1));

        sportRoomService.addSportToSportsHall(id1, yoga.getSportName());
        verify(sportRoomRepository).save(hallX1);
        assertThat(hallX1.getSports().get(0)).isEqualTo(yoga);

    }

    @Test
    public void addSportToSportHallTestException() {
        when(sportService.getSportById(yoga.getSportName())).thenReturn(yoga);
        when(sportRoomRepository.findBySportRoomId(id2)).thenReturn(Optional.ofNullable(field));

        assertThrows(IllegalArgumentException.class, () -> sportRoomService
            .addSportToSportsHall(id2, yoga.getSportName()));

        verify(sportRoomRepository, times(0)).save(field);
        assertThat(field.getSports().size()).isEqualTo(0);

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

        when(sportRoomRepository.findBySportRoomId(id2)).thenReturn(Optional.of(hallX1));

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
