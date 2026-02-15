package academy.devdojo.service;

import academy.devdojo.commons.users.UsersUtils;
import academy.devdojo.domain.UserService;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
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
class UserServiceServiceTest {

    @InjectMocks
    private UserServiceService service;
    @Mock
    private UserRepository repository;
    private List<UserService> usersList;
    @InjectMocks
    private UsersUtils userUtils;

    @BeforeEach
    void init() {
        usersList = userUtils.getUserServiceList();
    }

    @Test
    @DisplayName("findAllUsers Returns a list with all users when name is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(usersList);

        var users = service.findAll(null);
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(usersList);
    }

    @Test
    @DisplayName("findByFirstName Returns found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundObject_WhenNameExists() {
        var users = usersList.getFirst();
        var expectedValue = singletonList(users);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(users.getFirstName())).thenReturn(usersList);

        BDDMockito.when(service.findAll(users.getFirstName())).thenReturn(expectedValue);
        var usersResponse = service.findAll(users.getFirstName());
        Assertions.assertThat(usersList).isNotNull().containsAll(usersResponse);
    }

    @Test
    @DisplayName("findByFirstName Returns empty list when name is not found")
    @Order(3)
    void findAllUsers_RetunsEmptyList_WhenNameIsNotFound() {
        var expectedUsers = usersList.getFirst();
        BDDMockito.when(repository.findByFirstNameIgnoreCase(expectedUsers.getFirstName())).thenReturn(Collections.emptyList());

        var users = service.findAll(expectedUsers.getFirstName());

        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById Returns found object when id exists")
    @Order(4)
    void findById_RetunsFoundObject_WhenExists() {
        var expectedProducer = usersList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

        var foundProducer = service.findByIdOrThrowNotFound(expectedProducer.getId());


        Assertions.assertThat(foundProducer).isEqualTo(expectedProducer);
    }

    @Test
    @DisplayName("findById Returns ResponseStatusException when users is not found")
    @Order(5)
    void findById_ReturnsResponseStatusException_WhenProducerIsNotFound() {
        var expectedProducer = usersList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findByIdOrThrowNotFound(expectedProducer.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a users")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() {
        var usersToSave = userUtils.newUserServiceToSave();

        BDDMockito.when(repository.save(usersToSave)).thenReturn(usersToSave);
        BDDMockito.when(repository.findByEmail(usersToSave.getEmail())).thenReturn(Optional.empty());

        var savedProducer = service.save(usersToSave);

        Assertions.assertThat(savedProducer).isEqualTo(usersToSave).hasNoNullFieldsOrProperties();
    }


    @Test
    @DisplayName("delete removes a users")
    @Order(7)
    void delete_RemovesProducer_WhenSuccessful() {
        var usersToRemove = usersList.getFirst();
        BDDMockito.when(repository.findById(usersToRemove.getId())).thenReturn(Optional.of(usersToRemove));
        BDDMockito.doNothing().when(repository).delete(usersToRemove);

        Assertions.assertThatNoException().isThrownBy(() -> service.deleteByIdOrThrowNotFound(usersToRemove));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when id is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() {
        var usersToRemove = usersList.getFirst();
        BDDMockito.when(repository.findById(usersToRemove.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.deleteByIdOrThrowNotFound(usersToRemove)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a users")
    @Order(9)
    void update_UpdatesProducer_WhenSuccessful() {
        var userToUpdate = usersList.getFirst().withFirstName("ANIPLEX2");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(userToUpdate.getEmail(),userToUpdate.getId())).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.updateOrThrowNotFound(userToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when users is not found")
    @Order(10)
    void update_throwsResponseStatusException_WhenusersIsNotFound() {
        var usersToUpdate = usersList.getFirst();
        BDDMockito.when(repository.findById(usersToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.updateOrThrowNotFound(usersToUpdate)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update throws EmailAlreadyExistsException when email belongs to another user")
    @Order(11)
    void update_throwsEmailAlreadyExistsException_WhenEmailBelongsToAnotherUser() {
        var savedUser = usersList.getLast();
        var usersToUpdate = usersList.getFirst();
        var email = usersToUpdate.getEmail();
        var id = usersToUpdate.getId();

        BDDMockito.when(repository.findById(usersToUpdate.getId())).thenReturn(Optional.of(usersToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email,id)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.updateOrThrowNotFound(usersToUpdate)).isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("save throws EmailAlreadyExistsException when email belongs to another user")
    @Order(12)
    void save_throwsEmailAlreadyExistsException_WhenEmailBelongsToAnotherUser() {
        var savedUser = usersList.getLast();
        var userToSave = usersList.getFirst();
        var email = userToSave.getEmail();

        BDDMockito.when(repository.findByEmail(email)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToSave)).isInstanceOf(EmailAlreadyExistsException.class);
    }

}