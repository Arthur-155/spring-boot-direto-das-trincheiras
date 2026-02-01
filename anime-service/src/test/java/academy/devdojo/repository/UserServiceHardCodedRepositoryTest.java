package academy.devdojo.repository;

import academy.devdojo.commons.users.UsersUtils;
import academy.devdojo.domain.UserService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceHardCodedRepositoryTest {

    @InjectMocks
    private UserServiceHardCodedRepository repository;
    @Mock
    private UserServiceData userData;
    private List<UserService> producerList;
    @InjectMocks
    private UsersUtils UserUtils;

    @BeforeEach
    void init() {
        producerList = UserUtils.getUserServiceList();
        BDDMockito.when(userData.getUsers()).thenReturn(producerList);
    }

    @Test
    @DisplayName("findAll Returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllUserServices_WhenSuccessful() {
        var users = repository.findAllUsers();
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("findById Returns a producer with given id")
    @Order(2)
    void findById_RetunsUserServiceById_WhenSuccessful() {
        var expectedUserService = producerList.getFirst();
        var users = repository.findById(expectedUserService.getId());
        Assertions.assertThat(users).isNotNull().isPresent().contains(expectedUserService);
    }

    @Test
    @DisplayName("findByName Returns empty list when name is null")
    @Order(3)
    void findByName_RetunsEmptyList_WhenIsNull() {
        var users = repository.findByFirstName(null);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName Returns list with found object when name is exists")
    @Order(4)
    void findByName_RetunsFoundUserServiceList_WhenNameIsFound() {
        var expectedUserService = producerList.getFirst();
        var users = repository.findByFirstName(expectedUserService.getFirstName());
        Assertions.assertThat(users).isNotNull().contains(expectedUserService);
    }

    @Test
    @DisplayName("save creates a producer")
    @Order(5)
    void save_CreatesUserService_WhenSuccessful() {

        var producerToSave = UserUtils.newUserServiceToSave();
        var producer = repository.save(producerToSave);

        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();

        var producerSavedOptional = repository.findById(producerToSave.getId());

        Assertions.assertThat(producerSavedOptional).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("delete removes a producer")
    @Order(6)
    void delete_RemovesUserService_WhenSuccessful() {

        var producerToRemove = producerList.getFirst();
        repository.deleteById(producerToRemove);
        var users = repository.findAllUsers();
        Assertions.assertThat(users).isNotEmpty().doesNotContain(producerToRemove);
    }

    @Test
    @DisplayName("update updates a producer")
    @Order(7)
    void update_UpdatesUserService_WhenSuccessful() {

        var producerToUpdate = producerList.getFirst();
        producerToUpdate.setFirstName("ANIPLEX");

        repository.update(producerToUpdate);

        Assertions.assertThat(this.producerList).contains(producerToUpdate);

        var producerUpdatedOptional = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(producerUpdatedOptional).isPresent();
        Assertions.assertThat(producerUpdatedOptional.get().getFirstName()).isEqualTo(producerToUpdate.getFirstName());
    }
    
}