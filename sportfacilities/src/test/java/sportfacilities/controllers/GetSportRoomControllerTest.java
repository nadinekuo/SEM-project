package sportfacilities.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sportfacilities.entities.Sport;
import sportfacilities.entities.SportRoom;
import sportfacilities.services.SportRoomService;

/**
 * The type Sport room controller test.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class GetSportRoomControllerTest {

    private final transient long sportRoomId = 42L;
    private final transient long sportFieldId = 13L;
    private final transient int minCapacity = 2;
    private final transient int maxCapacity = 12;
    private final transient Sport soccer = new Sport("soccer", 6, 11);
    private final transient Sport hockey = new Sport("hockey", 7, 14);
    private final transient SportRoom hallX1 =
        new SportRoom("X1", List.of(soccer, hockey), 10, 50, true);
    private final transient SportRoom hockeyField =
        new SportRoom("hockeyFieldA", List.of(hockey), 10, 200, true);
    private final transient String sportName = "judo";
    /**
     * The Sport room service.
     */
    @Mock
    transient SportRoomService sportRoomService;
    /**
     * The Sport service.
     */
    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets .
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new GetSportRoomController(sportRoomService)).build();
    }

    /**
     * Gets sport room test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getSportRoomTest() throws Exception {
        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    @Test
    public void getSportRoomThrowsExceptionTest() throws Exception {
        Mockito.when(sportRoomService.getSportRoom(sportRoomId))
            .thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}", sportRoomId))
            .andExpect(status().isBadRequest());
    }

    /**
     * Gets sport room name test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getSportRoomNameTest() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}/getName", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    /**
     * Gets sport room name throws exception test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getSportRoomNameThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).getSportRoom(sportRoomId);

        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}/getName", sportRoomId))
            .andExpect(status().isBadRequest());
    }

    /**
     * Gets maximum capacity.
     *
     * @throws Exception the exception
     */
    @Test
    public void getMaximumCapacity() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}/getMaximumCapacity", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    /**
     * Gets minimum capacity.
     *
     * @throws Exception the exception
     */
    @Test
    public void getMinimumCapacity() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}/getMinimumCapacity", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    /**
     * Gets max capacity throws exception test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getMaxCapacityThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).getSportRoom(sportRoomId);

        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}/getMaximumCapacity", sportRoomId))
            .andExpect(status().isBadRequest());
    }

    /**
     * Gets min capacity throws exception test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getMinCapacityThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).getSportRoom(sportRoomId);

        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}/getMinimumCapacity", sportRoomId))
            .andExpect(status().isBadRequest());
    }

    /**
     * Gets field sport.
     *
     * @throws Exception the exception
     */
    @Test
    public void getFieldSport() throws Exception {
        given(sportRoomService.getSportRoom(sportFieldId)).willReturn(hockeyField);

        mockMvc.perform(get("/getSportRoomServices/{sportFieldId}/getSport", sportFieldId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportFieldId);
    }

    /**
     * Gets field sport throws exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void getFieldSportThrowsException() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).getSportRoom(sportRoomId);

        mockMvc.perform(get("/getSportRoomServices/{sportRoomId}/getSport", sportRoomId))
            .andExpect(status().isBadRequest());
    }
}