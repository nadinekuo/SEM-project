package sportfacilities.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sportfacilities.entities.Sport;
import sportfacilities.entities.SportRoom;
import sportfacilities.services.SportRoomService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class SportRoomControllerTest {


    private final transient long sportRoomId = 42L;
    private final transient long sportFieldId = 13L;
    private final transient Sport soccer = new Sport("soccer", true, 6, 11);
    private final transient Sport hockey = new Sport("hockey", true, 7, 14);
    private final transient SportRoom hallX1 =
        new SportRoom(sportRoomId, "X1", List.of(soccer, hockey), 10, 50);
    private final transient SportRoom hockeyField = new SportRoom(42L, "hockeyfieldA",
        List.of(hockey), 10, 200);

    @Autowired
    private transient MockMvc mockMvc;
    @Mock
    transient SportRoomService sportRoomService;


    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new SportRoomController(sportRoomService))
                .build();
    }

    @Test
    public void getSportRoomTest() throws Exception {
        mockMvc.perform(get("/sportRoom/{sportRoomId}", sportRoomId))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(sportRoomService).getSportRoom(sportRoomId);
    }


    @Test
    public void getSportRoomExists() throws Exception {
        mockMvc.perform(get("/sportRoom/{sportRoomId}/exists", sportRoomId))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(sportRoomService).sportRoomExists(sportRoomId);
    }

    @Test
    public void getSportRoomIsHall() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/isHall", sportRoomId))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    @Test
    public void getMaximumCapacity() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getMaximumCapacity", sportRoomId))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }


    @Test
    public void getMinimumCapacity() throws Exception {
        given(sportRoomService.getSportRoom(sportRoomId)).willReturn(hallX1);

        mockMvc.perform(get("/sportRoom/{sportRoomId}/getMinimumCapacity", sportRoomId))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportRoomId);
    }

    @Test
    public void getFieldSport() throws Exception {
        given(sportRoomService.getSportRoom(sportFieldId)).willReturn(hockeyField);

        mockMvc.perform(get("/sportRoom/{sportFieldId}/getSport", sportFieldId))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print()).andReturn();

        verify(sportRoomService).getSportRoom(sportFieldId);
    }






}
