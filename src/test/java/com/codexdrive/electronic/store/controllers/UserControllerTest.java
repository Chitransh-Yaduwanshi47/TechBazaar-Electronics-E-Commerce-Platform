package com.codexdrive.electronic.store.controllers;

import com.codexdrive.electronic.store.dtos.PageableResponse;
import com.codexdrive.electronic.store.dtos.RoleDto;
import com.codexdrive.electronic.store.dtos.UserDto;
import com.codexdrive.electronic.store.entities.Role;
import com.codexdrive.electronic.store.entities.User;
import com.codexdrive.electronic.store.services.FileService;
import com.codexdrive.electronic.store.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private FileService fileService;

    private Role role;
    private User user;
    private UserDto userDto;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {

        role = Role.builder()
                .roleId("abc")
                .name("NORMAL")
                .build();

        user = User.builder()
                .userId("123")
                .name("Chitranshu")
                .email("chitranshu2411@gmail.com")
                .about("This is testig create method")
                .gender("Male")
                .imageName("abc.png")
                .password("241104@yadav")
                .roles(List.of(role))
                .build();

        RoleDto roleDto = RoleDto.builder()
                .roleId("abc")
                .name("NORMAL")
                .build();

        userDto = UserDto.builder()
                .userId("123")
                .name("Chitranshu")
                .email("chitranshu2411@gmail.com")
                .about("This is testig create method")
                .gender("Male")
                .imageName("abc.png")
                .password("241104@yadav")
                .roles(List.of(roleDto))
                .build();
    }

    @Test
    public void createUserTest() throws Exception {

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(userDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Chitranshu"));
    }

    @Test
    public void updateUserTest() throws Exception {

        String userId = "123";

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(userDto);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userId)
                        .with(user("normalUser").roles("NORMAL"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(userDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chitranshu"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        String userId = "123";

        Mockito.doNothing().when(userService).deleteUser(userId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userId)
                        .with(user("adminUser").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User is deleted Successfully !!"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void getAllUserTest() throws Exception {
        UserDto dto2 = UserDto.builder()
                .userId("456")
                .name("Rahul")
                .email("rahul@gmail.com")
                .about("Testing get all users")
                .gender("Male")
                .imageName("xyz.png")
                .password("password123")
                .build();

        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(List.of(userDto, dto2));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalElements(2);
        pageableResponse.setTotalPages(1);
        pageableResponse.setLastPage(true);

        Mockito.when(userService.getAllUser(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        String userId = "123";

        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chitranshu"));
    }

    @Test
    public void getUserByEmailTest() throws Exception {
        String email = "chitranshu2411@gmail.com";

        Mockito.when(userService.getUserByEmail(email)).thenReturn(userDto);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/email/" + email)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    public void searchUserTest() throws Exception {
        String keywords = "Chitranshu";

        Mockito.when(userService.searchUser(keywords)).thenReturn(List.of(userDto));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/search/" + keywords)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
