package academy.devdojo.repository;

import academy.devdojo.domain.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserService,Long> {

    List<UserService>findByFirstNameIgnoreCase(String firstName);
    Optional<UserService> findByEmail(String email);
    Optional<UserService> findByEmailAndIdNot(String email,Long id);
}
