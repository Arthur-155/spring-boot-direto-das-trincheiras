package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProducerPostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
    private LocalDateTime createdAt;
}
