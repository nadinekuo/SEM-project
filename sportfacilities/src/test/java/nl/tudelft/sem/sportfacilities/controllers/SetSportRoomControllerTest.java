package nl.tudelft.sem.sportfacilities.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
class SetSportRoomControllerTest {

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
            MockMvcBuilders.standaloneSetup(new SetSportRoomController(sportRoomService)).build();
    }

    @Test
    public void setMinimumCapacityTest() throws Exception {
        mockMvc.perform(
                post("/setSportRoomServices/{sportRoomId}/{minCapacity}" + "/setMinimumCapacity"
                        + "/admin",
                    sportRoomId, minCapacity)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();
        verify(sportRoomService).setSportRoomMinCapacity(sportRoomId, minCapacity);
    }

    @Test
    public void setMaximumCapacityTest() throws Exception {
        mockMvc.perform(
                post("/setSportRoomServices/{sportRoomId}/{maxCapacity}" + "/setMaximumCapacity"
                        + "/admin",
                    sportRoomId, maxCapacity)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();
        verify(sportRoomService).setSportRoomMaxCapacity(sportRoomId, maxCapacity);
    }

    @Test
    public void setSportNameTest() throws Exception {
        mockMvc.perform(
                post("/setSportRoomServices/{sportRoomId}/{sportRoomName}" + "/setSportRoomName"
                        + "/admin",
                    sportRoomId, hallX1.getSportRoomName())).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();
        verify(sportRoomService).setSportRoomName(sportRoomId, hallX1.getSportRoomName());
    }

    @Test
    public void setSportRoomNameThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService)
            .setSportRoomName(sportRoomId, sportName);

        mockMvc.perform(
            post("/setSportRoomServices/{sportRoomId}/{sportRoomName}/setSportRoomName/admin",
                sportRoomId, sportName)).andExpect(status().isBadRequest());
    }

    @Test
    public void setMaxCapacityThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService)
            .setSportRoomMaxCapacity(sportRoomId, maxCapacity);

        mockMvc.perform(
            post("/setSportRoomServices/{sportRoomId}/{maxCapacity}" + "/setMaximumCapacity/admin",
                sportRoomId, maxCapacity)).andExpect(status().isBadRequest());
    }

    @Test
    public void setMinCapacityThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService)
            .setSportRoomMinCapacity(sportRoomId, minCapacity);

        mockMvc.perform(
            post("/setSportRoomServices/{sportRoomId}/{minCapacity}" + "/setMinimumCapacity/admin",
                sportRoomId, minCapacity)).andExpect(status().isBadRequest());
    }
}