package academy.devdojo.controller;

import academy.devdojo.commons.producers.FileUtils;
import academy.devdojo.commons.producers.ProducerUtils;
import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
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
//@ActiveProfiles("test")
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProducerData producerData;
    private List<Producer> producerList;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProducerUtils producerUtils;
    @MockitoSpyBean
    private ProducerHardCodedRepository repository;
    private final String URL = "/v1/producers";


    @BeforeEach
    void init() {
        producerList = producerUtils.getProducerList();
    }

    @Test
    @DisplayName("GET v1/producers Returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=ufotable Returns a list with the found object")
    @Order(2)
    void findAll_ReturnTheObject_WhenArgumentExits() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-name-200.json");
        var nome = "ufotable";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x Returns a empty list")
    @Order(3)
    void findAll_ReturnAnEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-x-name-404.json");
        var nome = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 Returns a list with the found object")
    @Order(4)
    void findById_RetunsFoundObject_WhenExists() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-id-200.json");
        var id = 1L;
        System.out.println("A PORRA DO RESPONSE " + response);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 Returns NotFound when producer is not found")
    @Order(5)
    void findById_ReturnsNotFound_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-byId-404.json");
        var id = 9999L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("producer/post-request-200.json");
        var response = fileUtils.readResourceFile("producer/post-response-201.json");
        var producerToSave = producerUtils.newProducerToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .header("x-api-key", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("DELETE v1/producers/{id} delete a producer")
    @Order(7)
    void delete_RemovesProducer_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var id = producerList.getFirst().getId();


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producers/{id} throws NotFound when id is not found")
    @Order(8)
    void delete_ThrowsNotFound_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/delete-producer-byId-404.json");
        var id = 9999L;


        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("UPDATE v1/producers/{id} updates a producer")
    @Order(9)
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var request = fileUtils.readResourceFile("producer/put-request-204.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("UPDATE v1/producers/{id} throws NotFound when producer is not found")
    @Order(10)
    void update_throwsNotFound_WhenproducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/delete-producer-byId-404.json");
        var request = fileUtils.readResourceFile("producer/put-request-404.json");

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
    @MethodSource("postFilesSource")
    @DisplayName("POST v1/producers returns bad request when fields are invalid")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String filePaths, List<String>listOfErrors) throws Exception {
        var request = fileUtils.readResourceFile("producer/%s".formatted(filePaths));


        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .header("x-api-key", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var mockMvcResult = mvcResult.getResolvedException();

        Assertions.assertThat(mockMvcResult).isNotNull();

        Assertions.assertThat(mockMvcResult.getMessage()).contains(listOfErrors);
    }

    private static Stream<Arguments>postFilesSource (){
        var requiredErrors = listOfRequiredErrors();
        return Stream.of(
                Arguments.of("post-request-400.json",requiredErrors),
                Arguments.of("post-request-blank-400.json",requiredErrors)
        );
    }

    @ParameterizedTest
    @MethodSource("putFilesSource")
    @DisplayName("PUT v1/producers returns bad request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String filesPath, List<String>listOfErrors) throws Exception {
        var request = fileUtils.readResourceFile("producer/%s".formatted(filesPath));


        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var mockMvcResult = mvcResult.getResolvedException();

        Assertions.assertThat(mockMvcResult).isNotNull();
        Assertions.assertThat(mockMvcResult.getMessage()).contains(listOfErrors);
    }

    private static Stream<Arguments>putFilesSource (){
        var requiredErrors = listOfRequiredErrors();
        requiredErrors.add("The field 'id' cannot be null");
        return Stream.of(
                Arguments.of("put-request-400.json",requiredErrors),
                Arguments.of("put-request-blank-400.json",requiredErrors)
        );
    }

    private static List<String> listOfRequiredErrors(){
        var nameRequiredError = "The field 'name' is required";
        return new ArrayList<>(List.of(nameRequiredError));
    }
}