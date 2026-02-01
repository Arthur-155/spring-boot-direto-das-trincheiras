package academy.devdojo.repository;

import academy.devdojo.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserServiceHardCodedRepository {
    private final UserServiceData userData;

    public List<UserService>findAllUsers(){
        return userData.getUsers();
    }

    public List<UserService> findByFirstName(String name){
        return userData.getUsers()
                .stream()
                .filter(user -> user.getFirstName().equalsIgnoreCase(name))
                .toList();
    }

    public Optional<UserService> findById(Long id){
        return userData.getUsers()
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public UserService save(UserService userService){
        userData.getUsers().add(userService);
        return userService;
    }

    public void deleteById(UserService userService){
        userData.getUsers().remove(userService);
    }

    public void update(UserService userService){
        deleteById(userService);
        save(userService);
    }
}
