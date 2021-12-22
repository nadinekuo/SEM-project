package sportfacilities.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sportfacilities.entities.Sport;
import sportfacilities.entities.SportRoom;
import sportfacilities.services.SportRoomService;
import sportfacilities.services.SportService;

//TODO put test in every method name

/**
 * The type Sport room controller test.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class SportRoomControllerTest {

    private final transient long sportRoomId = 42L;
    private final transient long sportFieldId = 13L;
    private final transient int minCapacity = 2;
    private final transient int maxCapacity = 12;
    private final transient Sport soccer = new Sport("soccer", true, 6, 11);
    private final transient Sport hockey = new Sport("hockey", true, 7, 14);
    private final transient SportRoom hallX1 =
        new SportRoom("X1", List.of(soccer, hockey), 10, 50, true);
    private final transient SportRoom hockeyField =
        new SportRoom("hockeyFieldA", List.of(hockey), 10, 200, true);
    private final transient String sportName = "judo";
    @Mock
    transient SportRoomService sportRoomService;
    @Mock
    transient SportService sportService;
    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets .
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new SportRoomController(sportRoomService, sportService))
                .build();
    }

    /**
     * Gets sport room test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getSportRoomTest() throws Exception {
        mockMvc.perform(get("/sportRoom/{sportRoomId}", sportRoomId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    /**
     * Gets sport room exists.
     *
     * @throws Exception the exception
     */
    @Test
    public void getSportRoomExists() throws Exception {
        mockMvc.perform(get("/sportRoom/{sportRoomId}/exists", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(sportRoomService).sportRoomExists(sportRoomId);
    }

    /**
     * Gets sport room is hall.
     *
     * @throws Exception the exception
     */
    @Test
    public void getSportRoomIsHall() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/isHall", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    /**
     * Gets maximum capacity.
     *
     * @throws Exception the exception
     */
    @Test
    public void getMaximumCapacity() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getMaximumCapacity", sportRoomId))
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

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getMinimumCapacity", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    /**
     * Gets field sport.
     *
     * @throws Exception the exception
     */
    @Test
    public void getFieldSport() throws Exception {
        given(sportRoomService.getSportRoom(sportFieldId)).willReturn(hockeyField);

        mockMvc.perform(get("/sportRoom/{sportFieldId}/getSport", sportFieldId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportFieldId);
    }

    @Test
    public void addSportRoomTest() throws Exception {
        String sportRoomName = "Hall 4";
        mockMvc.perform(put("/sportRoom/{name}/{sport}/{minCapacity}/{maxCapacity}/{isSportHall}"
                + "/addSportRoom/admin", sportRoomName, sportName, minCapacity, maxCapacity, true))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).addSportRoom(sportRoomName, sportName, minCapacity, maxCapacity,
            true);
    }

    @Test
    public void deleteSportRoomTest() throws Exception {

        mockMvc.perform(delete("/sportRoom/{sportRoomId}/deleteSportRoom/admin", sportFieldId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).deleteSportRoom(sportFieldId);
    }

    @Test
    public void deleteSportRoomWithInvalidIdTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).deleteSportRoom(1000L);

        mockMvc.perform(delete("/sportRoom/{sportRoomId}/deleteSportRoom/admin", 1000L))
            .andExpect(status().isBadRequest());
    }

}
