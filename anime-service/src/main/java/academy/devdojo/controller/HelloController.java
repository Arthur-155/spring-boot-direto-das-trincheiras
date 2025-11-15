package academy.devdojo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/greetins")
@Slf4j
public class HelloController {
    @GetMapping
    public String hi (){
        return "Hello World";
    }

    @PostMapping
    public String save(@RequestBody String nome){
        //return "foi salvo otário";
        log.info("save '{}' ", nome);
        return nome;
    }
}
