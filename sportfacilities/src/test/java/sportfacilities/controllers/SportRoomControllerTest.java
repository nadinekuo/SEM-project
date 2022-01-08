package sportfacilities.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
            MockMvcBuilders.standaloneSetup(new SportRoomController(sportRoomService)).build();
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
     * Gets sport room exists throws exception.
     *
     * @throws Exception the exception
     */
    @Disabled
    public void getSportRoomExistsThrowsException() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).sportRoomExists(sportRoomId);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/exists", sportRoomId))
            .andExpect(status().isBadRequest());
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
     * Gets sport room is hall throws exception test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getSportRoomIsHallThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).getSportRoom(sportRoomId);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/isHall", sportRoomId))
            .andExpect(status().isBadRequest());
    }

    /**
     * Add sport room test.
     *
     * @throws Exception the exception
     */
    @Test
    public void addSportRoomTest() throws Exception {
        String sportRoomName = "Hall 4";
        mockMvc.perform(put("/sportRoom/{name}/{sport}/{minCapacity}/{maxCapacity}/{isSportHall}"
                + "/addSportRoom/admin", sportRoomName, sportName, minCapacity, maxCapacity, true))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).addSportRoom(sportRoomName, sportName, minCapacity, maxCapacity,
            true);
    }

    /**
     * Delete sport room test.
     *
     * @throws Exception the exception
     */
    @Test
    public void deleteSportRoomTest() throws Exception {

        mockMvc.perform(delete("/sportRoom/{sportRoomId}/deleteSportRoom/admin", sportFieldId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).deleteSportRoom(sportFieldId);
    }

    /**
     * Delete sport room with invalid id test.
     *
     * @throws Exception the exception
     */
    @Test
    public void deleteSportRoomWithInvalidIdTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).deleteSportRoom(sportRoomId);

        mockMvc.perform(delete("/sportRoom/{sportRoomId}/deleteSportRoom/admin", sportRoomId))
            .andExpect(status().isBadRequest());
    }

    /**
     * Add sport to sport room test.
     *
     * @throws Exception the exception
     */
    @Test
    public void addSportToSportRoomTest() throws Exception {

        mockMvc.perform(
                post("/sportRoom/{sportRoomId}/{sportName}/addSportToSportHall/admin", sportRoomId,
                    sportName)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
            .andReturn();

        verify(sportRoomService).addSportToSportsHall(sportRoomId, sportName);
    }

    /**
     * Add sport to sport room test exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void addSportToSportRoomTestException() throws Exception {
        doThrow(new NoSuchElementException("test")).when(sportRoomService)
            .addSportToSportsHall(sportRoomId, sportName);

        mockMvc.perform(
                post("/sportRoom/{sportRoomId}/{sportName}/addSportToSportHall/admin",
                    sportRoomId, sportName))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print())
            .andReturn();

        verify(sportRoomService, times(1)).addSportToSportsHall(sportRoomId, sportName);
    }

    /**
     * Add sport to sport room test exception 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void addSportToSportRoomTestException2() throws Exception {
        doThrow(new IllegalArgumentException("test")).when(sportRoomService)
            .addSportToSportsHall(sportRoomId, sportName);

        mockMvc.perform(
                post("/sportRoom/{sportRoomId}/{sportName}/addSportToSportHall/admin",
                    sportRoomId, sportName))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print())
            .andReturn();

        verify(sportRoomService, times(1)).addSportToSportsHall(sportRoomId, sportName);
    }

}
