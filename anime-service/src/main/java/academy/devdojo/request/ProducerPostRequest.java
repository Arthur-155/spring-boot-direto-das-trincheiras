package academy.devdojo.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProducerPostRequest{
    private String name;
    private LocalDateTime createdAt;
}
