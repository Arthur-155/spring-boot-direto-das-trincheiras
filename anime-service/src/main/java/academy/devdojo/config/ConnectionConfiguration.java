package academy.devdojo.config;

import external.dependency.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConnectionConfiguration {

    @Bean
    public Connection connectionMySql() {
        return new Connection("localhost","ArthurMySql", "123");
    }

    @Bean
    public Connection connectionMongo() {
        return new Connection("localhost","ArthurMongo", "123");
    }
}
