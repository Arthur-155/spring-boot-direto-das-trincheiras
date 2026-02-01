package academy.devdojo.controller;

import academy.devdojo.commons.animes.AnimeUtils;
import academy.devdojo.commons.producers.FileUtils;
import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
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
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest
@TestMethodOrder(MethodOrderer.class)
@ComponentScan(basePackages = "academy.devdojo")
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AnimeData animeData;
    private List<Anime> animeList;
    @MockitoSpyBean
    private AnimeHardCodedRepository repository;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private AnimeUtils animeUtils;
    private final String URL = "/v1/animes";

    @BeforeEach
    void init() {
        {
            animeList = animeUtils.getAnimeList();
        }
    }

    @DisplayName("GET v1/animes returns a list with all animes")
    @Test
    @Order(1)
    void ListAll_ReturnsAllAnimes_whenNameIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/get-anime-null-name-201.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/animes?name=pokémon returns a list of the found object when name is found")
    @Test
    @Order(2)
    void ListAll_ReturnsFoundObject_whenNameIsFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/get-anime-name-200.json");
        var nome = "Pokémon";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/animes?nome=not-found returns a empty list when name is not found")
    @Test
    @Order(3)
    void ListAll_ReturnsEmptyList_whenNameIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/get-anime-x-nome-200.json");
        var nome = "not-found";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("nome", nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/animes/{id} returns a object when the id is found")
    @Test
    @Order(4)
    void findById_ReturnsAnObject_whenIdIsFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/get-anime-byId-200.json");
        var id = animeList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @DisplayName("GET v1/animes/{id} returns a empty list when id is not found")
    @Test
    @Order(5)
    void findById_ReturnsAnEmptyList_whenIdIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/get-anime-byId-404.json");
        var id = 9999L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @DisplayName("POST v1/animes creates an object")
    @Test
    @Order(6)
    void save_createsAnObject_whenSuccessful() throws Exception {
        var AnimeToSave = animeUtils.newAnimeToSave();
        var request = fileUtils.readResourceFile("anime/post-anime-request-200.json");
        var response = fileUtils.readResourceFile("anime/post-anime-response-201.json");
        BDDMockito.when(repository.save(ArgumentMatchers.argThat(anime -> anime.getName()
                .equals(AnimeToSave.getName())))).thenReturn(AnimeToSave);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("DELETE v1/animes/{id} deletes an object")
    @Test
    @Order(7)
    void delete_deleteAnObject_whenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var id = animeList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("DELETE v1/animes/{id} returns an NotFound when id is not found")
    @Test
    @Order(8)
    void delete_ReturnsNotFound_whenIdIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/delete-anime-byId-404.json");
        var id = 999L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("UPDATE v1/animes updates an anime")
    @Test
    @Order(9)
    void update_UpdatesAnAnime_WhenSuccessful() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var request = fileUtils.readResourceFile("anime/put-anime-request-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("UPDATE v1/animes throws an NotFound when id is not found")
    @Test
    @Order(10)
    void update_ThrowsNotFound_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var request = fileUtils.readResourceFile("anime/put-anime-request-404.json");
        var response = fileUtils.readResourceFile("anime/put-response-anime-byId-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postFilePathsSource")
    @DisplayName("POST v1/animes returns bad request when fields are invalid")
    @Order(11)
    void save_returnsBadRequest_whenFieldsAreInvalid(String filePaths, List<String>requiredErrors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(filePaths));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var mockMvcResult = mvcResult.getResolvedException();

        Assertions.assertThat(mockMvcResult).isNotNull();

        Assertions.assertThat(mockMvcResult.getMessage()).contains(requiredErrors);
    }

    private static Stream<Arguments>postFilePathsSource(){
        var requiredErrors = listOfRequiredErrors();
        return Stream.of(
                Arguments.of("post-anime-request-null-400.json",requiredErrors),
                Arguments.of("post-anime-request-blank-400.json",requiredErrors)
        );
    }

    @DisplayName("PUT v1/animes returns bad request when fields are invalid")
    @ParameterizedTest
    @MethodSource("putFilePathsSource")
    @Order(12)
    void update_returnsBadRequest_whenFieldsAreInvalid(String filePath, List<String>errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(filePath));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var mockMvcResult = mvcResult.getResolvedException();

        Assertions.assertThat(mockMvcResult).isNotNull();
        Assertions.assertThat(mockMvcResult.getMessage()).contains(errors);
    }

    private static Stream<Arguments>putFilePathsSource(){
        var requiredErrors = listOfRequiredErrors();
        requiredErrors.add("the field 'id' cannot be null");
        return Stream.of(
                Arguments.of("put-anime-request-null-400.json",requiredErrors),
                Arguments.of("put-anime-request-blank-400.json",requiredErrors)
        );
    }

    private static List<String>listOfRequiredErrors(){
        var requiredError = "the field 'name' is required";
        return new ArrayList<>(List.of(requiredError));
    }
}