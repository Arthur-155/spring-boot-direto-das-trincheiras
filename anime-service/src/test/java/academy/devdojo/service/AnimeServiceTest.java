package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;
    @Mock
    private AnimeHardCodedRepository repository;
    private List<Anime> animes = new ArrayList<>();

    @BeforeEach
    void init() {
        var vagabond = Anime.builder().id(1L).name("Vagabond").createdAt(LocalDateTime.now()).build();
        var KurokoNoBasket = Anime.builder().id(2L).name("Kuroko No Basket").createdAt(LocalDateTime.now()).build();
        var KingInYellow = Anime.builder().id(3L).name("King In Yellow").createdAt(LocalDateTime.now()).build();
        animes.addAll(List.of(vagabond, KurokoNoBasket, KingInYellow));
    }

    @DisplayName("list All returns a list with all animes")
    @Test
    @Order(1)
    void ListAll_ReturnsAllAnimes_whenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animes);
        var expectedValue = service.listAll(null);
        Assertions.assertThat(expectedValue).isNotNull().hasSameElementsAs(animes);
    }

    @DisplayName("list All returns a list of the found object when name is found")
    @Test
    @Order(2)
    void ListAll_ReturnsFoundObject_whenNameIsFound() {
        var foundObject = animes.getFirst();
        var expectedAnimeFound = Collections.singletonList(foundObject);

        BDDMockito.when(repository.findByName(foundObject.getName())).thenReturn(expectedAnimeFound);

        var expectedValue = service.listAll(foundObject.getName());
        Assertions.assertThat(expectedValue).containsAll(expectedAnimeFound);
    }

    @DisplayName("list All returns a empty list when name is null")
    @Test
    @Order(3)
    void ListAll_ReturnsEmptyList_whenNameIsNull() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());
        var expectedValue = service.listAll(name);
        Assertions.assertThat(expectedValue).isNotNull().isEmpty();
    }

    @DisplayName("find by id returns a object when the id is found")
    @Test
    @Order(4)
    void findById_ReturnsAnObject_whenIdIsFound() {
        var objectReceived = animes.getFirst();

        BDDMockito.when(repository.findById(objectReceived.getId())).thenReturn(Optional.of(objectReceived));

        var expectedValue = service.findByIdOrThrowNotFound(objectReceived.getId());

        Assertions.assertThat(expectedValue).isEqualTo(objectReceived);
    }

    @DisplayName("find by id returns a empty list when id is not found")
    @Test
    @Order(5)
    void findById_ReturnsAnEmptyList_whenIdIsNotFound() {
        var objectReceived = animes.getFirst();

        BDDMockito.when(repository.findById(objectReceived.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(objectReceived.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @DisplayName("save creates an object")
    @Test
    @Order(6)
    void save_createsAnObject_whenSuccessful() {
        var animeToSave = Anime.builder().id(99L).name("Attack On Titan").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        var animeSaved = service.save(animeToSave);

        Assertions.assertThat(animeSaved).isEqualTo(animeSaved).hasNoNullFieldsOrProperties();
    }

    @DisplayName("delete deletes a object")
    @Test
    @Order(7)
    void delete_deleteAnObject_whenSuccessful() {
        var animeToDelete = animes.getFirst();

        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));

        service.deleteOrThrowNotFound(animeToDelete.getId());
        var list = repository.findAll();

        Assertions.assertThat(list).doesNotContain(animeToDelete);
    }

    @DisplayName("delete returns an exception when id is not found")
    @Test
    @Order(8)
    void delete_ReturnsAnException_whenIdIsNotFound() {
        var animeToDelete = animes.getFirst();

        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @DisplayName("update updates an anime")
    @Test
    @Order(9)
    void update_UpdatesAnAnime_WhenSuccessful() {
        var animeToUpdate = animes.getFirst();
        animeToUpdate.setName("Blue Lock");
        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.doNothing().when(repository).update(animeToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.updateOrThrowNotFound(animeToUpdate));
    }

    @DisplayName("update throws an exception when id is not found")
    @Test
    @Order(10)
    void update_ThrowsAnException_WhenIdIsNotFound() {
        var animeToUpdate = animes.getFirst();
        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.updateOrThrowNotFound(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }


}