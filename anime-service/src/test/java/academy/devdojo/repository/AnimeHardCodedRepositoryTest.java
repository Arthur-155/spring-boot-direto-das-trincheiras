package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class AnimeHardCodedRepositoryTest {
    @InjectMocks
    private AnimeHardCodedRepository repository;
    @Mock
    private AnimeData animeData;
    private List<Anime> animeList = new ArrayList<>();
    @BeforeEach
    void init(){
        {
            var TokyoGhoul = Anime.builder().id(1L).name("Tokyo Ghoul").createdAt(LocalDateTime.now()).build();
            var DeathNote = Anime.builder().id(2L).name("Death Note").createdAt(LocalDateTime.now()).build();
            var OnePiece = Anime.builder().id(3L).name("One Piece").createdAt(LocalDateTime.now()).build();
            animeList.addAll(List.of(TokyoGhoul,DeathNote,OnePiece));
            BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
        }
    }

    @DisplayName("Find All Will returns a list with all products in the list")
    @Test
    @Order(1)
    void findAll_ReturnsListWithAllAnimes_WhenSuccessful(){
        var animes = repository.findAll();
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @DisplayName("Find by name returns an empty list when the name is null")
    @Test
    @Order(2)
    void findByName_ReturnsEmptyList_WhenNameIsNull(){
        var animeNameReceived = repository.findByName(null);

        Assertions.assertThat(animeNameReceived).isNotNull().isEmpty();
    }

    @DisplayName("Find by name returns the found name when the name exists")
    @Test
    @Order(3)
    void findByName_ReturnsFoundName_WhenNameExists(){
        var expectedValue = animeList.getFirst();
        var anime= repository.findByName(expectedValue.getName());

        Assertions.assertThat(anime).isNotNull().contains(expectedValue);
    }

    @DisplayName("Find by id returns a list with the found id")
    @Test
    @Order(4)
    void findById_ReturnsFoundList_WhenIdIsFound(){
        var expectedValue = animeList.getFirst();
        var animeIdReceived = repository.findById(expectedValue.getId());

        Assertions.assertThat(animeIdReceived).isNotNull().isPresent().contains(expectedValue);
    }

    @DisplayName("save create an anime in the list")
    @Test
    @Order(5)
    void save_createAnAnime_WhenIsSuccessful(){
        var animeToSave = Anime.builder()
                .id(4L).name("Naruto").createdAt(LocalDateTime.now()).build();

        var anime = repository.save(animeToSave);

        Assertions.assertThat(anime).isNotNull().isEqualTo(animeToSave);

        var animeOptionalSaved = repository.findById(anime.getId());

        Assertions.assertThat(animeOptionalSaved).isPresent().contains(anime);
    }

    @DisplayName("delete removes a anime")
    @Test
    @Order(6)
    void delete_removesAnAnime_WhenIsSuccessful(){
        var animeToUpdate = animeList.getFirst();
        repository.delete(animeToUpdate);
        var animesList = repository.findAll();
        Assertions.assertThat(animesList).isNotNull().doesNotContain(animeToUpdate);
    }

    @DisplayName("update updates a anime")
    @Test
    @Order(7)
    void update_updatesAnAnime_WhenIsSuccessful(){
        var animeToUpdate = animeList.getFirst();

        animeToUpdate.setName("Jujutsu Kaisen");

        repository.update(animeToUpdate);

        var animeList = repository.findAll();

        Assertions.assertThat(animeList).contains(animeToUpdate);

        var animeOptionalSaved = repository.findById(animeToUpdate.getId());

        Assertions.assertThat(animeOptionalSaved).isPresent();
        Assertions.assertThat(animeOptionalSaved.get().getName()).isEqualTo(animeToUpdate.getName());
    }


}