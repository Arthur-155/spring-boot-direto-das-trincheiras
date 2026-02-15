package academy.devdojo.controller;


import academy.devdojo.commons.users.UserFileUtils;
import academy.devdojo.commons.users.UsersUtils;
import academy.devdojo.domain.UserService;
import academy.devdojo.repository.UserRepository;
import academy.devdojo.repository.UserServiceData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


@WebMvcTest
@TestMethodOrder(MethodOrderer.class)
@ComponentScan(basePackages = "academy.devdojo")
class UserServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserServiceData userData;
    private List<UserService> userList;
    @Autowired
    private UserFileUtils fileUtils;
    @Autowired
    private UsersUtils usersUtils;
    @MockitoBean
    private UserRepository repository;
    private final String URL = "/v1/users";


    @BeforeEach
    void init() {
        userList = usersUtils.getUserServiceList();
    }

    @Test
    @DisplayName("GET v1/users Returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(userList);
        var response = fileUtils.readResourceFile("users/get-users-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=Arthur Returns a list with the found object")
    @Order(2)
    void findAll_ReturnTheObject_WhenArgumentExits() throws Exception {
        var response = fileUtils.readResourceFile("users/get-users-name-200.json");
        var nome = "Arthur";
        var arthur = userList.stream().filter(userService -> userService.getFirstName().equals(nome)).findFirst().orElse(null);
        BDDMockito.when(repository.findByFirstNameIgnoreCase(nome)).thenReturn(Collections.singletonList(arthur));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=x Returns a empty list")
    @Order(3)
    void findAll_ReturnAnEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = fileUtils.readResourceFile("users/get-users-x-name-404.json");
        var nome = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 Returns a list with the found object")
    @Order(4)
    void findById_RetunsFoundObject_WhenExists() throws Exception {
        var response = fileUtils.readResourceFile("users/get-users-id-200.json");
        var id = 1L;
        var foundObject = userList.stream().filter(userService -> userService.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundObject);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 Returns NotFound when users is not found")
    @Order(5)
    void findById_ReturnsNotFound_WhenUserIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = fileUtils.readResourceFile("users/get-user-byId-404.json");
        var id = 9999L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/users creates a users")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("users/post-request-user-200.json");
        var response = fileUtils.readResourceFile("users/post-response-user-201.json");
        var userToSave = usersUtils.newUserServiceToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("DELETE v1/users/{id} delete a users")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() throws Exception {
        var id = userList.getFirst().getId();
        var foundObject = userList.stream().filter(userService -> userService.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundObject);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }



    @Test
    @DisplayName("DELETE v1/users/{id} throws NotFound when id is not found")
    @Order(8)
    void delete_ThrowsNotFound_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var response = fileUtils.readResourceFile("users/delete-user-byId-404.json");
        var id = 9999L;


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("UPDATE v1/users/{id} updates a users")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() throws Exception {
        var id = userList.getFirst().getId();
        var foundObject = userList.stream().filter(userService -> userService.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundObject);
        var request = fileUtils.readResourceFile("users/put-request-user-204.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("UPDATE v1/users/{id} throws NotFound when users is not found")
    @Order(10)
    void update_throwsNotFound_WhenusersIsNotFound() throws Exception {
        var id = userList.getFirst().getId();
        var foundObject = userList.stream().filter(userService -> userService.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundObject);
        var request = fileUtils.readResourceFile("users/put-request-user-404.json");
        var response = fileUtils.readResourceFile("users/put-response-user-byId-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are empty")
    @Order(11)
    void save_returnsBadRequest_WhenFieldsAreEmpty(String filesName, List<String>errors) throws Exception {
        var request = fileUtils.readResourceFile("users/%s".formatted(filesName));


        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var expectedResponse = mvcResult.getResolvedException();

        Assertions.assertThat(expectedResponse).isNotNull();


        Assertions.assertThat(expectedResponse.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> postUserBadRequestSource() {

        var requiredErrors = listOfRequiredErrors();
        var invalidErrors = listOfInvalidErrors();
        return Stream.of(

                Arguments.of("post-request-user-400.json",requiredErrors),
                Arguments.of("post-request-blank-fields-user-400.json",requiredErrors),
                Arguments.of("post-request-user-email-badRequest-400.json",invalidErrors)
        );
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are empty")
    @Order(12)
    void put_returnsBadRequest_WhenFieldsAreEmpty(String filesName, List<String>errors) throws Exception {
        var request = fileUtils.readResourceFile("users/%s".formatted(filesName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var expectedResponse = mvcResult.getResolvedException();

        Assertions.assertThat(expectedResponse).isNotNull();

        Assertions.assertThat(expectedResponse.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments>putUserBadRequestSource(){
        var requiredErrors = listOfRequiredErrors();
        requiredErrors.add("the field 'id' cannot be null");
        var invalidErrors = listOfInvalidErrors();

        return Stream.of(
                Arguments.of("put-request-user-400.json",requiredErrors),
                Arguments.of("put-request-blank-fields-user-400.json",requiredErrors),
                Arguments.of("put-request-user-email-badRequest-400.json",invalidErrors)
        );
    }

    private static List<String>listOfRequiredErrors(){
        var firstNameValidError = "the field 'firstName' is required";
        var lastNameValidError = "the field 'lastName' is required";
        var emailRequiredError = "the field 'email' is required";
        return new ArrayList<>(List.of(firstNameValidError,lastNameValidError,emailRequiredError));
    }

    private static List<String>listOfInvalidErrors(){
        var emailValidError = "The field 'email' is not valid";
        return List.of(emailValidError);
    }

}