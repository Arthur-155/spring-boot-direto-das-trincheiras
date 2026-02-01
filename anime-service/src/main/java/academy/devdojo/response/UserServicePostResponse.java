package academy.devdojo.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserServicePostResponse {
    @NotNull(message = "the field 'id' cannot be null")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
