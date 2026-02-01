package academy.devdojo.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private LocalDateTime createdAt;


    //@JsonProperties é uma tradução de recebimento de JSON, para aceitar nomes diferentes dos cadastrados no Model
    //Consumes = pode ser setado como JSON ou XML, quer dizer que a aplicação só aceita XML ou JSON.
    //Produces = Poder ser XML ou JSON também, significa que a aplicação só produz esses 2 tipos de arquivo.
    //Headers = significa que você obriga um header a ser enviado na requisição para a requisição ser aceita.
    //@RequestHeader = siginifica que você quer receber todos os Headers usados na requisição recebida.


}
