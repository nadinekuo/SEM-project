package sportfacilities.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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

/**
 * The type Sport room controller test.
 */
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
            MockMvcBuilders.standaloneSetup(new SetSportRoomController(sportRoomService)).build();
    }

    /**
     * Sets minimum capacity.
     *
     * @throws Exception the exception
     */
    @Test
    public void setMinimumCapacity() throws Exception {
        mockMvc.perform(
                post("/setSportRoomServices/{sportRoomId}/{minCapacity}/setMinimumCapacity/admin",
                    sportRoomId, minCapacity)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();
        verify(sportRoomService).setSportRoomMinCapacity(sportRoomId, minCapacity);
    }

    /**
     * Sets maximum capacity.
     *
     * @throws Exception the exception
     */
    @Test
    public void setMaximumCapacity() throws Exception {
        mockMvc.perform(
                post("/setSportRoomServices/{sportRoomId}/{maxCapacity}/setMaximumCapacity/admin",
                    sportRoomId, maxCapacity)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();
        verify(sportRoomService).setSportRoomMaxCapacity(sportRoomId, maxCapacity);
    }

    /**
     * Sets sport name test.
     *
     * @throws Exception the exception
     */
    @Test
    public void setSportNameTest() throws Exception {
        mockMvc.perform(
                post("/setSportRoomServices/{sportRoomId}/{sportRoomName}/setSportRoomName/admin",
                    sportRoomId, hallX1.getSportRoomName())).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();
        verify(sportRoomService).setSportRoomName(sportRoomId, hallX1.getSportRoomName());
    }

    /**
     * Sets sport room name throws exception test.
     *
     * @throws Exception the exception
     */
    @Test
    public void setSportRoomNameThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService)
            .setSportRoomName(sportRoomId, sportName);

        mockMvc.perform(
            post("/setSportRoomServices/{sportRoomId}/{sportRoomName}/setSportRoomName/admin",
                sportRoomId, sportName)).andExpect(status().isBadRequest());
    }

    /**
     * Sets max capacity throws exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void setMaxCapacityThrowsException() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService)
            .setSportRoomMaxCapacity(sportRoomId, maxCapacity);

        mockMvc.perform(
            post("/setSportRoomServices/{sportRoomId}/{maxCapacity}/setMaximumCapacity/admin",
                sportRoomId, maxCapacity)).andExpect(status().isBadRequest());
    }

    /**
     * Sets min capacity throws exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void setMinCapacityThrowsException() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService)
            .setSportRoomMinCapacity(sportRoomId, minCapacity);

        mockMvc.perform(
            post("/setSportRoomServices/{sportRoomId}/{minCapacity}/setMinimumCapacity/admin",
                sportRoomId, minCapacity)).andExpect(status().isBadRequest());
    }
}