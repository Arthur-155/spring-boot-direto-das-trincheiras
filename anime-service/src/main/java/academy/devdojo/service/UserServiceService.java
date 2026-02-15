package academy.devdojo.service;

import academy.devdojo.domain.UserService;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceService {
    private final UserRepository repository;

    public List<UserService> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }

    public UserService findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("not-found"));
    }

    public UserService save(UserService userService) {
        assertEmailDoesNotExist(userService.getEmail());
        return repository.save(userService);
    }

    public void deleteByIdOrThrowNotFound(UserService userService) {
        var user = findByIdOrThrowNotFound(userService.getId());
        repository.delete(user);
    }

    public void updateOrThrowNotFound(UserService userService) {
        assertUserExists(userService.getId());
        assertEmailDoesNotExist(userService.getEmail(),userService.getId());
        repository.save(userService);
    }

    public void assertUserExists(Long id){
        findByIdOrThrowNotFound(id);
    }

    public void assertEmailDoesNotExist(String email){
        repository.findByEmail(email)
                .ifPresent(this::throwEmailExistException);

    }public void assertEmailDoesNotExist(String email,Long id){
        repository.findByEmailAndIdNot(email,id)
                .ifPresent(this::throwEmailExistException);
    }

    private void throwEmailExistException(UserService user) {
        throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(user.getEmail()));
    }
}
