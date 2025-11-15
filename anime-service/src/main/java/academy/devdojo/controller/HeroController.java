package academy.devdojo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/heroes")
public class HeroController {
    private static final List<String>HEROES = List.of("Guts","Zoro","Kakashi", "Luffy");

    @GetMapping
    public static List<String> listAllHeroes() {
        return HEROES;
    }

    @GetMapping("filterList")
    public static List<String> listAllHeroesParam(@RequestParam String names) {
        return HEROES.stream().filter(names::contains).toList();
    }

    @GetMapping("{name}")
    public static List<String> findByName(@PathVariable String name) {
        return HEROES.stream().filter(name::contains).toList();
    }
}
