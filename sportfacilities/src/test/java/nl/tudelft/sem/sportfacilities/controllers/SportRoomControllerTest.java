package nl.tudelft.sem.sportfacilities.controllers;

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
import nl.tudelft.sem.sportfacilities.entities.Sport;
import nl.tudelft.sem.sportfacilities.entities.SportRoom;
import nl.tudelft.sem.sportfacilities.services.SportRoomService;
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

    @Mock
    transient SportRoomService sportRoomService;
    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new SportRoomController(sportRoomService)).build();
    }

    @Test
    public void getSportRoomExistsTest() throws Exception {
        mockMvc.perform(get("/sportRoom/{sportRoomId}/exists", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(sportRoomService).sportRoomExists(sportRoomId);
    }

    @Test
    public void getSportRoomIsHallTest() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/isHall", sportRoomId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    @Test
    public void getSportRoomIsHallThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).getSportRoom(sportRoomId);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/isHall", sportRoomId))
            .andExpect(status().isBadRequest());
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
        doThrow(NoSuchElementException.class).when(sportRoomService).deleteSportRoom(sportRoomId);

        mockMvc.perform(delete("/sportRoom/{sportRoomId}/deleteSportRoom/admin", sportRoomId))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void addSportToSportRoomTest() throws Exception {

        mockMvc.perform(
                post("/sportRoom/{sportRoomId}/{sportName}/addSportToSportHall/admin", sportRoomId,
                    sportName)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
            .andReturn();

        verify(sportRoomService).addSportToSportsHall(sportRoomId, sportName);
    }

    @Test
    public void addSportToSportRoomExceptionTest() throws Exception {
        doThrow(new NoSuchElementException("test")).when(sportRoomService)
            .addSportToSportsHall(sportRoomId, sportName);

        mockMvc.perform(
                post("/sportRoom/{sportRoomId}/{sportName}/addSportToSportHall/admin",
                    sportRoomId, sportName))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print())
            .andReturn();

        verify(sportRoomService, times(1)).addSportToSportsHall(sportRoomId, sportName);
    }

    @Test
    public void addSportToSportRoomExceptionTest2() throws Exception {
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
