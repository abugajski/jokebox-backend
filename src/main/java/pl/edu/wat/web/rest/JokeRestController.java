package pl.edu.wat.web.rest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.dto.SimpleJokeDto;
import pl.edu.wat.service.interfaces.JokeService;

import java.util.List;

/**
 * Created by Hubert on 25.06.2017.
 */
@RestController
@RequestMapping("/joke")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JokeRestController {

    JokeService jokeService;

    @GetMapping(value = "/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimpleJokeDto>> listJokeByCategory(@PathVariable String category){
        List <SimpleJokeDto> jokes = jokeService.listJokeByCategory(category);
        return ResponseEntity.ok(jokes);
    }

    @GetMapping(value = "/test/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimpleJokeDto>> pageJokeByCategory(@PathVariable String category){
        return ResponseEntity.ok(jokeService.findAllByCategoryName(category));
    }

}
