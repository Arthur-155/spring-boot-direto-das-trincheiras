package academy.devdojo.service;

import academy.devdojo.domain.UserService;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserServiceData;
import academy.devdojo.repository.UserServiceHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceService {
    private final UserServiceHardCodedRepository repository;

    public List<UserService>findAll(String name){
        return name == null ? repository.findAllUsers() : repository.findByFirstName(name);
    }

    public UserService findByIdOrThrowNotFound(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("not-found"));
    }

    public UserService save(UserService userService){
        return repository.save(userService);
    }

    public void deleteByIdOrThrowNotFound(UserService userService){
        var delete = findByIdOrThrowNotFound(userService.getId());
        repository.deleteById(delete);
    }

    public void updateOrThrowNotFound(UserService userService){
        var update = findByIdOrThrowNotFound(userService.getId());
        repository.update(update);
    }
}
