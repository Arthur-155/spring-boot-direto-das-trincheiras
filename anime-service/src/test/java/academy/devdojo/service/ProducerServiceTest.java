package academy.devdojo.service;

import academy.devdojo.commons.producers.ProducerUtils;
import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;
    @Mock
    private ProducerHardCodedRepository repository;
    private List<Producer> producerList;
    @InjectMocks
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producerList = producerUtils.getProducerList();
    }

    @Test
    @DisplayName("findAll Returns a list with all producers when name is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        var producers = service.findAll(null);

        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("findAll Returns found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundObject_WhenNameExists() {
        var producer = producerList.getFirst();
        var expectedValue = singletonList(producer);

        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(producerList);

        BDDMockito.when(service.findAll(producer.getName())).thenReturn(expectedValue);
        var producerResponse = service.findAll(producer.getName());

        Assertions.assertThat(producerList).isNotNull().containsAll(producerResponse);
    }

    @Test
    @DisplayName("findByName Returns empty list when name is not found")
    @Order(3)
    void findByName_RetunsEmptyList_WhenNameIsNotFound() {
        var producer = producerList.getFirst();
        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(Collections.emptyList());

        var producers = service.findAll(producer.getName());

        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById Returns found object when id exists")
    @Order(4)
    void findById_RetunsFoundObject_WhenExists() {
        var expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

        var foundProducer = service.findByIdOrThrowNotFound(expectedProducer.getId());


        Assertions.assertThat(foundProducer).isEqualTo(expectedProducer);
    }

    @Test
    @DisplayName("findById Returns ResponseStatusException when producer is not found")
    @Order(5)
    void findById_ReturnsResponseStatusException_WhenProducerIsNotFound() {
        var expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedProducer.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() {
        var producerToSave = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);
        var savedProducer = repository.save(producerToSave);

        Assertions.assertThat(savedProducer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a producer")
    @Order(7)
    void delete_RemovesProducer_WhenSuccessful() {
        var producerToRemove = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToRemove.getId())).thenReturn(Optional.of(producerToRemove));
        BDDMockito.doNothing().when(repository).deleteById(producerToRemove);

        Assertions.assertThatNoException().isThrownBy(() -> service.deleteByIdOrThrowNotFound(producerToRemove.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when id is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() {
        var producerToRemove = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToRemove.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.deleteByIdOrThrowNotFound(producerToRemove.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a producer")
    @Order(9)
    void update_UpdatesProducer_WhenSuccessful() {
        var producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("ANIPLEX2");
        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));
        BDDMockito.doNothing().when(repository).update(producerToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.updateOrThrowNotFound(producerToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when producer is not found")
    @Order(10)
    void update_throwsResponseStatusException_WhenproducerIsNotFound() {
        var producerToUpdate = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.updateOrThrowNotFound(producerToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }


}