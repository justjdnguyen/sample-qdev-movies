package com.amazonaws.samples.qdevmovies.movies;

/**
 * Exception thrown when an error occurs during movie search operations
 * Arrr! This be thrown when the treasure hunt goes awry!
 */
public class MovieSearchException extends RuntimeException {
    
    public MovieSearchException(String message) {
        super(message);
    }
    
    public MovieSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}