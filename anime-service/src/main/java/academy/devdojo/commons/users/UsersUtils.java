package academy.devdojo.commons.users;

import academy.devdojo.domain.UserService;
import academy.devdojo.domain.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsersUtils {

    public List<UserService> getUserServiceList() {
        var dateTime = "2026-01-02T22:08:58.1849778";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var Arthur = UserService.builder().id(1L).firstName("Arthur").lastName("Vieira").email("arthur_bergamo@db1.com.br").build();
        var Kingdons = UserService.builder().id(2L).firstName("Kingdons").lastName("PLL").email("PLL_Kingdons@hotmail.com").build();
        var Adv = UserService.builder().id(3L).firstName("Daniela").lastName("Vieira").email("daniela_vieira@hotmail.com").build();
        return new ArrayList<>(List.of(Arthur, Kingdons, Adv));
    }

    public UserService newUserServiceToSave() {
        return UserService.builder().id(999L).firstName("digest").lastName("advogado").email("adv_digest@gmail.com").build();
    }
}
