package com.amazonaws.samples.qdevmovies.movies;

/**
 * Exception thrown when movie search parameters are invalid
 * Arrr! This be thrown when a landlubber provides invalid search criteria!
 */
public class InvalidSearchParametersException extends RuntimeException {
    
    public InvalidSearchParametersException(String message) {
        super(message);
    }
    
    public InvalidSearchParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}