package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
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
        var response = readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}