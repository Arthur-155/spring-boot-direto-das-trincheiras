package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
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
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AnimeData animeData;
    @Autowired
    private ResourceLoader resourceLoader;
    private List<Anime> animeList;
    @MockitoSpyBean
    private AnimeHardCodedRepository repository;

    @BeforeEach
    void init(){
        {
            var dateTime = "2026-01-02T22:08:58.1849778";
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
            var localDateTime = LocalDateTime.parse(dateTime, formatter);

            var katekyoHitmanReborn = Anime.builder().id(1L).name("Katekyo Hitman Reborn").createdAt(localDateTime).build();
            var codeGeass = Anime.builder().id(2L).name("Code Geass").createdAt(localDateTime).build();
            var pokemon = Anime.builder().id(3L).name("Pokémon").createdAt(localDateTime).build();
            animeList = new ArrayList<>(List.of(katekyoHitmanReborn,codeGeass,pokemon));
        }
    }

    @DisplayName("GET v1/animes returns a list with all animes")
    @Test
    @Order(1)
    void ListAll_ReturnsAllAnimes_whenNameIsNull() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = readResourceFile("anime/get-anime-null-name-201.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/animes?name=pokémon returns a list of the found object when name is found")
    @Test
    @Order(2)
    void ListAll_ReturnsFoundObject_whenNameIsFound() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = readResourceFile("anime/get-anime-name-200.json");
        var nome = "Pokémon";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("nome",nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/animes?nome=not-found returns a empty list when name is not found")
    @Test
    @Order(3)
    void ListAll_ReturnsEmptyList_whenNameIsNull() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = readResourceFile("anime/get-anime-x-nome-200.json");
        var nome = "not-found";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("nome",nome))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("GET v1/animes/{id} returns a object when the id is found")
    @Test
    @Order(4)
    void findById_ReturnsAnObject_whenIdIsFound() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var response = readResourceFile("anime/get-anime-byId-200.json");
        var id = animeList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @DisplayName("GET v1/animes/{id} returns a empty list when id is not found")
    @Test
    @Order(5)
    void findById_ReturnsAnEmptyList_whenIdIsNotFound() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var id = 9999L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().reason("Not found My Brotha"));

    }

    @DisplayName("POST v1/animes creates an object")
    @Test
    @Order(6)
    void save_createsAnObject_whenSuccessful() throws Exception{
        var AnimeToSave = Anime.builder().id(56L).name("Solo Leveling").createdAt(LocalDateTime.now()).build();
        var request = readResourceFile("anime/post-anime-request-200.json");
        var response = readResourceFile("anime/post-anime-response-201.json");
        BDDMockito.when(repository.save(ArgumentMatchers.argThat(anime -> anime.getName()
                .equals(AnimeToSave.getName())))).thenReturn(AnimeToSave);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @DisplayName("DELETE v1/animes/{id} deletes an object")
    @Test
    @Order(7)
    void delete_deleteAnObject_whenSuccessful() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var id = animeList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("DELETE v1/animes/{id} returns an exception when id is not found")
    @Test
    @Order(8)
    void delete_ReturnsAnException_whenIdIsNotFound() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var id = 999L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("UPDATE v1/animes updates an anime")
    @Test
    @Order(9)
    void update_UpdatesAnAnime_WhenSuccessful() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var request = readResourceFile("anime/put-anime-request-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("UPDATE v1/animes throws an exception when id is not found")
    @Test
    @Order(10)
    void update_ThrowsAnException_WhenIdIsNotFound() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        var request = readResourceFile("anime/put-anime-request-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}