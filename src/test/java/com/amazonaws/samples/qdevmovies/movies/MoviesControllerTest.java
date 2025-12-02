package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MoviesController including search functionality
 * Arrr! These tests be makin' sure our movie treasure huntin' endpoints work like a charm!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MockMovieService mockMovieService;
    private ReviewService mockReviewService;

    // Mock MovieService for testing
    private static class MockMovieService extends MovieService {
        private final List<Movie> testMovies;

        public MockMovieService() {
            this.testMovies = Arrays.asList(
                new Movie(1L, "The Prison Escape", "John Director", 1994, "Drama", "Test description", 142, 5.0),
                new Movie(2L, "The Family Boss", "Michael Filmmaker", 1972, "Crime/Drama", "Test description", 175, 5.0),
                new Movie(3L, "The Masked Hero", "Chris Moviemaker", 2008, "Action/Crime", "Test description", 152, 5.0)
            );
        }

        @Override
        public List<Movie> getAllMovies() {
            return new ArrayList<>(testMovies);
        }

        @Override
        public Optional<Movie> getMovieById(Long id) {
            return testMovies.stream()
                    .filter(movie -> movie.getId().equals(id))
                    .findFirst();
        }

        @Override
        public List<Movie> searchMovies(String name, Long id, String genre) {
            List<Movie> results = new ArrayList<>(testMovies);

            // Filter by ID if provided
            if (id != null && id > 0) {
                return getMovieById(id).map(Arrays::asList).orElse(new ArrayList<>());
            }

            // Filter by name if provided
            if (name != null && !name.trim().isEmpty()) {
                String searchName = name.trim().toLowerCase();
                results = results.stream()
                        .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                        .collect(java.util.stream.Collectors.toList());
            }

            // Filter by genre if provided
            if (genre != null && !genre.trim().isEmpty()) {
                String searchGenre = genre.trim().toLowerCase();
                results = results.stream()
                        .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                        .collect(java.util.stream.Collectors.toList());
            }

            return results;
        }

        @Override
        public List<String> getAllGenres() {
            return Arrays.asList("Action/Crime", "Crime/Drama", "Drama");
        }
    }

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services
        mockMovieService = new MockMovieService();
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("Should return all movies when no search parameters provided")
    public void testGetMovies_NoSearchParameters() {
        String result = moviesController.getMovies(model, null, null, null);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies);
        assertEquals(3, movies.size());
        
        assertFalse((Boolean) model.getAttribute("searchPerformed"));
        assertNotNull(model.getAttribute("pirateMessage"));
    }

    @Test
    @DisplayName("Should search movies by name")
    public void testGetMovies_SearchByName() {
        String result = moviesController.getMovies(model, "prison", null, null);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("The Prison Escape", movies.get(0).getMovieName());
        
        assertTrue((Boolean) model.getAttribute("searchPerformed"));
        assertEquals("prison", model.getAttribute("searchName"));
        assertNotNull(model.getAttribute("pirateMessage"));
    }

    @Test
    @DisplayName("Should search movies by ID")
    public void testGetMovies_SearchById() {
        String result = moviesController.getMovies(model, null, 2L, null);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals(2L, movies.get(0).getId());
        assertEquals("The Family Boss", movies.get(0).getMovieName());
        
        assertTrue((Boolean) model.getAttribute("searchPerformed"));
        assertEquals(2L, model.getAttribute("searchId"));
    }

    @Test
    @DisplayName("Should search movies by genre")
    public void testGetMovies_SearchByGenre() {
        String result = moviesController.getMovies(model, null, null, "Drama");
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies);
        assertEquals(2, movies.size()); // Should find movies with "Drama" in genre
        
        assertTrue((Boolean) model.getAttribute("searchPerformed"));
        assertEquals("Drama", model.getAttribute("searchGenre"));
    }

    @Test
    @DisplayName("Should handle no search results")
    public void testGetMovies_NoResults() {
        String result = moviesController.getMovies(model, "NonExistentMovie", null, null);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
        
        assertTrue((Boolean) model.getAttribute("searchPerformed"));
        assertTrue((Boolean) model.getAttribute("noResults"));
        assertNotNull(model.getAttribute("pirateMessage"));
    }

    @Test
    @DisplayName("Should return successful API response for valid search")
    public void testSearchMoviesApi_ValidSearch() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("prison", null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals(1, body.get("totalResults"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("The Prison Escape", movies.get(0).getMovieName());
        
        assertNotNull(body.get("message"));
        assertNotNull(body.get("pirateWisdom"));
        assertNotNull(body.get("searchParameters"));
    }

    @Test
    @DisplayName("Should return bad request for API search with no parameters")
    public void testSearchMoviesApi_NoParameters() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, null, null);
        
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertNotNull(body.get("message"));
        assertNotNull(body.get("pirateWisdom"));
    }

    @Test
    @DisplayName("Should return bad request for API search with empty parameters")
    public void testSearchMoviesApi_EmptyParameters() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("", 0L, "");
        
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
    }

    @Test
    @DisplayName("Should return successful API response with no results")
    public void testSearchMoviesApi_NoResults() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("NonExistentMovie", null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals(0, body.get("totalResults"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
        
        assertNotNull(body.get("message"));
        assertNotNull(body.get("pirateWisdom"));
    }

    @Test
    @DisplayName("Should search by ID via API")
    public void testSearchMoviesApi_ById() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, 1L, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals(1, body.get("totalResults"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals(1L, movies.get(0).getId());
    }

    @Test
    @DisplayName("Should search by genre via API")
    public void testSearchMoviesApi_ByGenre() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, null, "Crime");
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertTrue((Integer) body.get("totalResults") > 0);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        
        // Verify all results contain "Crime" in genre
        for (Movie movie : movies) {
            assertTrue(movie.getGenre().toLowerCase().contains("crime"));
        }
    }

    // Original tests
    @Test
    @DisplayName("Should get movie details successfully")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
    }

    @Test
    @DisplayName("Should return error page for non-existent movie")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
    }

    @Test
    @DisplayName("Should integrate with movie service correctly")
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(3, movies.size());
        assertEquals("The Prison Escape", movies.get(0).getMovieName());
    }
}
