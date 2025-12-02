package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MovieService search functionality
 * Arrr! These tests be makin' sure our treasure huntin' methods work properly!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Should return all movies when no search parameters provided")
    public void testSearchMovies_NoParameters() {
        List<Movie> results = movieService.searchMovies(null, null, null);
        
        assertNotNull(results);
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    @DisplayName("Should return all movies when empty search parameters provided")
    public void testSearchMovies_EmptyParameters() {
        List<Movie> results = movieService.searchMovies("", null, "");
        
        assertNotNull(results);
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    @DisplayName("Should find movie by exact ID")
    public void testSearchMovies_ByValidId() {
        List<Movie> results = movieService.searchMovies(null, 1L, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should return empty list for invalid ID")
    public void testSearchMovies_ByInvalidId() {
        List<Movie> results = movieService.searchMovies(null, 999L, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for negative ID")
    public void testSearchMovies_ByNegativeId() {
        List<Movie> results = movieService.searchMovies(null, -1L, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should find movies by partial name match (case insensitive)")
    public void testSearchMovies_ByPartialName() {
        List<Movie> results = movieService.searchMovies("prison", null, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should find movies by partial name match with different case")
    public void testSearchMovies_ByPartialNameDifferentCase() {
        List<Movie> results = movieService.searchMovies("PRISON", null, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should return empty list for non-existent movie name")
    public void testSearchMovies_ByNonExistentName() {
        List<Movie> results = movieService.searchMovies("NonExistentMovie", null, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should find movies by exact genre match")
    public void testSearchMovies_ByExactGenre() {
        List<Movie> results = movieService.searchMovies(null, null, "Drama");
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        // Verify all results contain "Drama" in genre
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("drama"));
        }
    }

    @Test
    @DisplayName("Should find movies by partial genre match")
    public void testSearchMovies_ByPartialGenre() {
        List<Movie> results = movieService.searchMovies(null, null, "Crime");
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        // Verify all results contain "Crime" in genre
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("crime"));
        }
    }

    @Test
    @DisplayName("Should return empty list for non-existent genre")
    public void testSearchMovies_ByNonExistentGenre() {
        List<Movie> results = movieService.searchMovies(null, null, "NonExistentGenre");
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should combine name and genre filters")
    public void testSearchMovies_CombineNameAndGenre() {
        List<Movie> results = movieService.searchMovies("the", null, "Drama");
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        // Verify all results match both criteria
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains("the"));
            assertTrue(movie.getGenre().toLowerCase().contains("drama"));
        }
    }

    @Test
    @DisplayName("Should prioritize ID search over other parameters")
    public void testSearchMovies_IdTakesPriority() {
        // Search with ID and other parameters - ID should take priority
        List<Movie> results = movieService.searchMovies("SomeOtherName", 1L, "SomeOtherGenre");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
    }

    @Test
    @DisplayName("Should return all unique genres")
    public void testGetAllGenres() {
        List<String> genres = movieService.getAllGenres();
        
        assertNotNull(genres);
        assertFalse(genres.isEmpty());
        
        // Check that genres are sorted and unique
        for (int i = 1; i < genres.size(); i++) {
            assertTrue(genres.get(i).compareTo(genres.get(i-1)) >= 0, 
                      "Genres should be sorted alphabetically");
        }
        
        // Verify no duplicates
        long uniqueCount = genres.stream().distinct().count();
        assertEquals(genres.size(), uniqueCount, "All genres should be unique");
    }

    @Test
    @DisplayName("Should handle whitespace in search parameters")
    public void testSearchMovies_WithWhitespace() {
        List<Movie> results = movieService.searchMovies("  prison  ", null, "  drama  ");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should return existing movie by ID using getMovieById")
    public void testGetMovieById_ValidId() {
        Optional<Movie> result = movieService.getMovieById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("The Prison Escape", result.get().getMovieName());
    }

    @Test
    @DisplayName("Should return empty optional for invalid ID using getMovieById")
    public void testGetMovieById_InvalidId() {
        Optional<Movie> result = movieService.getMovieById(999L);
        
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return empty optional for null ID using getMovieById")
    public void testGetMovieById_NullId() {
        Optional<Movie> result = movieService.getMovieById(null);
        
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return empty optional for negative ID using getMovieById")
    public void testGetMovieById_NegativeId() {
        Optional<Movie> result = movieService.getMovieById(-1L);
        
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return all movies using getAllMovies")
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        
        // Verify we have the expected test movies
        assertTrue(movies.size() >= 12, "Should have at least 12 movies from the JSON file");
    }
}