package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest
@TestMethodOrder(MethodOrderer.class)
@ComponentScan(basePackages = "academy.devdojo")
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProducerData producerData;
    private List<Producer> producerList;
    @Autowired
    private ResourceLoader resourceLoader;
    @MockitoSpyBean
    private ProducerHardCodedRepository repository;

    @BeforeEach
    void init() {
        var dateTime = "2026-01-02T22:08:58.1849778";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var ufotable = Producer.builder().id(1L).name("ufotable").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(2L).name("witStudio").createdAt(localDateTime).build();
        var studioGhibli = Producer.builder().id(3L).name("studioGhibli").createdAt(localDateTime).build();
        producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }

    @Test
    @DisplayName("GET v1/producers Returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=ufotable Returns a list with the found object")
    @Order(2)
    void findAll_ReturnTheObject_WhenArgumentExits() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = readResourceFile("producer/get-producer-name-200.json");
        var nome = "ufotable";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x Returns a empty list")
    @Order(3)
    void findAll_ReturnAnEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = readResourceFile("producer/get-producer-x-name-404.json");
        var nome = "x";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 Returns a list with the found object")
    @Order(4)
    void findById_RetunsFoundObject_WhenExists() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = readResourceFile("producer/get-producer-id-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 Returns ResponseStatusException when producer is not found")
    @Order(5)
    void findById_ReturnsResponseStatusException_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = readResourceFile("producer/get-producer-id-200.json");
        var id = 9999L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().reason("id not found"));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = readResourceFile("producer/post-request-200.json");
        var response = readResourceFile("producer/post-response-201.json");
        var producerToSave = Producer.builder().id(999L).name("MAPPA").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/producers")
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


        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producers/{id} throws ResponseStatusException when id is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var id = 9999L;


        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().reason("id not found"));
    }

    @Test
    @DisplayName("UPDATE v1/producers/{id} updates a producer")
    @Order(9)
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var request = readResourceFile("producer/put-request-204.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("UPDATE v1/producers/{id} throws ResponseStatusException when producer is not found")
    @Order(10)
    void update_throwsResponseStatusException_WhenproducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var request = readResourceFile("producer/put-request-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}