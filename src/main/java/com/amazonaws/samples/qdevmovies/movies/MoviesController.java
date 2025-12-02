package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) Long id,
                           @RequestParam(required = false) String genre) {
        logger.info("Ahoy! Fetchin' movies with search parameters - name: {}, id: {}, genre: {}", name, id, genre);
        
        List<Movie> movies;
        boolean isSearch = (name != null && !name.trim().isEmpty()) || 
                          (id != null && id > 0) || 
                          (genre != null && !genre.trim().isEmpty());
        
        if (isSearch) {
            movies = movieService.searchMovies(name, id, genre);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            if (movies.isEmpty()) {
                model.addAttribute("noResults", true);
                model.addAttribute("pirateMessage", "Arrr! No treasure found with those search terms, matey! Try different keywords to find yer movie bounty!");
            } else {
                model.addAttribute("pirateMessage", "Ahoy! Found " + movies.size() + " movies in our treasure chest!");
            }
        } else {
            movies = movieService.getAllMovies();
            model.addAttribute("searchPerformed", false);
            model.addAttribute("pirateMessage", "Welcome to our movie treasure chest, ye landlubber! All our finest films be displayed below!");
        }
        
        model.addAttribute("movies", movies);
        model.addAttribute("allGenres", movieService.getAllGenres());
        return "movies";
    }

    /**
     * REST API endpoint for movie search - returns JSON response
     * Arrr! This be the treasure map for other ships to find our movies!
     */
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMoviesApi(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String genre) {
        
        logger.info("Ahoy! API search request - name: {}, id: {}, genre: {}", name, id, genre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate parameters
            if ((name == null || name.trim().isEmpty()) && 
                (id == null || id <= 0) && 
                (genre == null || genre.trim().isEmpty())) {
                
                throw new InvalidSearchParametersException("At least one search parameter must be provided: name, id, or genre");
            }
            
            List<Movie> movies = movieService.searchMovies(name, id, genre);
            
            response.put("success", true);
            response.put("movies", movies);
            response.put("totalResults", movies.size());
            
            if (movies.isEmpty()) {
                response.put("message", "No movies found matching yer search criteria, ye scurvy dog!");
                response.put("pirateWisdom", "The seas be vast, but no treasure matches yer map. Try different search terms!");
            } else {
                response.put("message", "Ahoy! Found " + movies.size() + " movies in our treasure chest!");
                response.put("pirateWisdom", "Ye've struck gold, matey! These be fine films for any seafarin' soul!");
            }
            
            // Add search parameters to response for reference
            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("name", name);
            searchParams.put("id", id);
            searchParams.put("genre", genre);
            response.put("searchParameters", searchParams);
            
            return ResponseEntity.ok(response);
            
        } catch (InvalidSearchParametersException e) {
            logger.warn("Arrr! Invalid search parameters provided: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Arrr! Ye need to provide at least one search parameter, matey! Use 'name', 'id', or 'genre'.");
            response.put("pirateWisdom", "A pirate without a map be lost at sea! Give me somethin' to search for!");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (MovieSearchException e) {
            logger.error("Arrr! Movie search operation failed: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Shiver me timbers! An error occurred while searchin' for movies.");
            response.put("pirateWisdom", "Even the best pirates face storms at sea. Try again later, matey!");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Arrr! Invalid argument provided for movie search: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Arrr! Invalid search parameters provided, ye scallywag!");
            response.put("pirateWisdom", "Check yer search terms and try again, matey!");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            logger.error("Arrr! Unexpected error during movie search: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Shiver me timbers! An unexpected error occurred while searchin' for movies.");
            response.put("pirateWisdom", "Even the best pirates face unexpected storms at sea. Try again later, matey!");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}