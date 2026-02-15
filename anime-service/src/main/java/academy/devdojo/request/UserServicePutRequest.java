package academy.devdojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserServicePutRequest {
    @NotNull(message = "the field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "the field 'firstName' is required")
    private String firstName;
    @NotBlank(message = "the field 'lastName' is required")
    private String lastName;
    @NotBlank(message = "the field 'email' is required")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,10}$", message = "The field 'email' is not valid")
    private String email;
}
