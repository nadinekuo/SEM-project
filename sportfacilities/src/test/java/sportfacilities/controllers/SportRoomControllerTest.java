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
            MockMvcBuilders.standaloneSetup(new SportRoomController(sportRoomService))
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


    @Test
    public void getSportRoomThrowsExceptionTest() throws Exception {
        Mockito.when(sportRoomService.getSportRoom(sportRoomId)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/sportRoom/{sportRoomId}", sportRoomId))
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

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getName", sportRoomId))
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

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getName", sportRoomId))
            .andExpect(status().isBadRequest());
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
     * Sets minimum capacity.
     *
     * @throws Exception the exception
     */
    @Test
    public void setMinimumCapacity() throws Exception {
        mockMvc.perform(
                post("/sportRoom/{sportRoomId}/{minCapacity}/setMinimumCapacity/admin", sportRoomId,
                    minCapacity)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
            .andReturn();
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
                post("/sportRoom/{sportRoomId}/{maxCapacity}/setMaximumCapacity/admin", sportRoomId,
                    maxCapacity)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
            .andReturn();
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
                post("/sportRoom/{sportRoomId}/{sportRoomName}/setSportRoomName/admin", sportRoomId,
                    hallX1.getSportRoomName())).andExpect(status().isOk())
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
            post("/sportRoom/{sportRoomId}/{sportRoomName}/setSportRoomName/admin", sportRoomId,
                sportName)).andExpect(status().isBadRequest());
    }

    /**
     * Gets max capacity throws exception test.
     *
     * @throws Exception the exception
     */
    @Test
    public void getMaxCapacityThrowsExceptionTest() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).getSportRoom(sportRoomId);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getMaximumCapacity", sportRoomId))
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

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getMinimumCapacity", sportRoomId))
            .andExpect(status().isBadRequest());
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
            post("/sportRoom/{sportRoomId}/{maxCapacity}/setMaximumCapacity/admin", sportRoomId,
                maxCapacity)).andExpect(status().isBadRequest());
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
            post("/sportRoom/{sportRoomId}/{minCapacity}/setMinimumCapacity/admin", sportRoomId,
                minCapacity)).andExpect(status().isBadRequest());
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

    /**
     * Gets field sport throws exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void getFieldSportThrowsException() throws Exception {
        doThrow(NoSuchElementException.class).when(sportRoomService).getSportRoom(sportRoomId);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getSport", sportRoomId))
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
            .andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print())
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
            .andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        verify(sportRoomService, times(1)).addSportToSportsHall(sportRoomId, sportName);
    }

}
