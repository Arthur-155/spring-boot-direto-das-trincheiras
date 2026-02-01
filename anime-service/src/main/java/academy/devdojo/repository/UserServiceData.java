package academy.devdojo.repository;

import academy.devdojo.domain.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceData {
    private List<UserService>users = new ArrayList<>();

    {
        var Arthur = UserService.builder().id(1L).firstName("Arthur").lastName("Vieira").email("arthur_bergamo@db1.com.br").build();
        var Kingdons = UserService.builder().id(2L).firstName("Kingdons").lastName("PLL").email("PLL_Kingdons@hotmail.com").build();
        var Adv = UserService.builder().id(3L).firstName("Daniela").lastName("Vieira").email("daniela_vieira@hotmail.com").build();
        users.addAll(List.of(Arthur,Kingdons,Adv));
    }

    public List<UserService>getUsers(){
        return users;
    }
}
