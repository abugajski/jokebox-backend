package pl.edu.wat.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wat.dto.SimpleJokeDto;
import pl.edu.wat.dto.SimpleJokeInputDto;
import pl.edu.wat.domain.Joke;
import pl.edu.wat.repository.CategoryRepository;
import pl.edu.wat.repository.JokeRepository;
import pl.edu.wat.service.interfaces.JokeService;
import pl.edu.wat.utils.StringUtils;
import pl.edu.wat.web.rest.errors.DuplicateJokeExeption;
import pl.edu.wat.web.rest.errors.NoSuchCategoryException;
import pl.edu.wat.web.rest.errors.NoSuchJokeException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hubert on 25.06.2017.
 */
@Transactional
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JokeServiceImpl implements JokeService {

    private final Logger log = LoggerFactory.getLogger(JokeServiceImpl.class);

    JokeRepository jokeRepository;
    CategoryRepository categoryRepository;


    @Override
    public Joke addSimpleJoke(SimpleJokeInputDto simpleJokeInputDto) {
        log.debug("Request to add joke by form");
        if( jokeRepository.findAllByCategoryName(simpleJokeInputDto.getCategoryName())
                .stream()
                .map(joke -> StringUtils.similarity(simpleJokeInputDto.getJokeContent(), joke.getContent()))
                .anyMatch(similarity -> 1 == similarity.compareTo(0.85))){
            throw new DuplicateJokeExeption();
        }
        return categoryRepository.findOneByName(simpleJokeInputDto.getCategoryName())
                .map(category -> {
                    Joke joke = new Joke(simpleJokeInputDto.getJokeContent(), category);
                    jokeRepository.save(joke);
                    return joke;
                }).orElseThrow(() -> new NoSuchCategoryException());
    }


    @Override
    public void addJoke(Joke joke) {
        log.debug("Request to add joke");
        jokeRepository.save(joke);
    }

    @Override
    public Joke deleteJoke(int id) {
        Joke joke = jokeRepository.findOne(id);
        if(joke == null) {
            throw new NoSuchJokeException();
        }
        jokeRepository.delete(id);
        return joke;
    }

    @Override
    public List<SimpleJokeDto> listJokeByCategory(String categoryName) {
        log.debug("Request to list all Jokes by category (list)");
        return jokeRepository.findAllByCategoryRequestparam(categoryName)
                .stream()
                .map(joke -> new SimpleJokeDto(joke))
                .collect(Collectors.toList());
    }

    @Override
    public void cleanJokes() {
        log.debug("Request to delete all Jokes");
        jokeRepository.deleteAll();
    }

    @Override
    public SimpleJokeDto likeOrUnlikeJoke(int id, String likeOrUnlike) {
        log.debug("Request to like joke");
        Joke joke = jokeRepository.findOne(id);
        if (joke == null){
            throw new NoSuchJokeException();
        }else {
            if ("like".equalsIgnoreCase(likeOrUnlike)){
                joke.setLikeNumber(joke.getLikeNumber()+1);
                jokeRepository.save(joke);
            }else if("unlike".equalsIgnoreCase(likeOrUnlike)){
                joke.setUnlikeNumber(joke.getUnlikeNumber()+1);
                jokeRepository.save(joke);
            }else {
                throw new IllegalArgumentException();
            }
        }
        return new SimpleJokeDto(joke);

    }

}

