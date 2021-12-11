package user.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import user.services.GroupService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class GroupControllerTest {

    @Autowired
    private transient MockMvc mockMvc;
    @Mock
    transient GroupService groupService;

    private final transient long groupId = 15L;


    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new GroupController(groupService))
                .build();
    }

    @Test
    public void getGroupSizeTest() throws Exception {
        mockMvc.perform(get("/group/{groupId}/getGroupSize", groupId))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(groupService).getGroupSizeById(groupId);
    }





}
