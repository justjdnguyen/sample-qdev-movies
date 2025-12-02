# 🏴‍☠️ Pirate's Movie Treasure Chest - Spring Boot Demo Application

Ahoy matey! Welcome to the finest movie catalog web application on the seven seas! Built with Spring Boot and infused with pirate spirit, this treasure chest demonstrates Java application development best practices while searchin' for the perfect movie adventure.

## ⚓ Features

- **🎬 Movie Treasure Chest**: Browse 12 classic movies with detailed information
- **🔍 Advanced Movie Search**: Hunt for movies by name, ID, or genre with our pirate-powered search
- **📋 Movie Details**: View comprehensive information including captain (director), year, adventure type (genre), duration, and description
- **⭐ Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **📱 Responsive Design**: Mobile-first design that works on all devices from ship to shore
- **🎨 Modern Pirate UI**: Dark theme with gradient backgrounds and smooth animations
- **🏴‍☠️ REST API**: JSON endpoints for fellow pirates to integrate with

## 🗺️ New Search & Filtering Features

### Web Interface Search
- **Search Form**: Interactive form with pirate-themed styling
- **Multiple Filters**: Search by movie name, ID, or genre
- **Real-time Results**: Instant feedback with pirate messages
- **No Results Handling**: Friendly pirate messages when no treasure is found

### REST API Search
- **Endpoint**: `/movies/search`
- **Parameters**: `name`, `id`, `genre`
- **JSON Response**: Structured data with pirate wisdom
- **Error Handling**: Comprehensive validation and pirate-themed error messages

## 🛠️ Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for templating
- **CSS3** with pirate-themed styling

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **🏴‍☠️ Movie Treasure Chest**: http://localhost:8080/movies
- **🔍 Search Movies**: Use the search form on the main page
- **📋 Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)

## 🏗️ Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoints
│   │       │   ├── MovieService.java         # Business logic with search functionality
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review business logic
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie treasure data
│       ├── mock-reviews.json                 # Mock review data
│       ├── log4j2.xml                        # Logging configuration
│       ├── static/css/
│       │   ├── movies.css                    # Pirate-themed styling
│       │   └── movie-details.css             # Detail page styling
│       └── templates/
│           ├── movies.html                   # Main page with search form
│           └── movie-details.html            # Movie detail page
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java         # Service layer tests
            ├── MoviesControllerTest.java     # Controller tests with search
            └── MovieTest.java                # Model tests
```

## 🗺️ API Endpoints

### Get All Movies (with Search)
```
GET /movies?name={name}&id={id}&genre={genre}
```
Returns an HTML page displaying movies. Supports optional search parameters.

**Parameters:**
- `name` (optional): Movie name to search for (partial matches)
- `id` (optional): Specific movie ID to find
- `genre` (optional): Genre to filter by

**Examples:**
```
http://localhost:8080/movies                    # All movies
http://localhost:8080/movies?name=prison        # Search by name
http://localhost:8080/movies?genre=Drama        # Filter by genre
http://localhost:8080/movies?id=1               # Find specific movie
```

### Search Movies API (JSON)
```
GET /movies/search?name={name}&id={id}&genre={genre}
```
Returns JSON response with search results and pirate wisdom.

**Parameters:**
- `name` (optional): Movie name to search for (partial matches)
- `id` (optional): Specific movie ID to find  
- `genre` (optional): Genre to filter by

**Response Format:**
```json
{
  "success": true,
  "movies": [...],
  "totalResults": 2,
  "message": "Ahoy! Found 2 movies in our treasure chest!",
  "pirateWisdom": "Ye've struck gold, matey! These be fine films for any seafarin' soul!",
  "searchParameters": {
    "name": "prison",
    "id": null,
    "genre": null
  }
}
```

**Examples:**
```bash
# Search by movie name
curl "http://localhost:8080/movies/search?name=prison"

# Filter by genre
curl "http://localhost:8080/movies/search?genre=Drama"

# Find specific movie by ID
curl "http://localhost:8080/movies/search?id=1"

# Combine filters
curl "http://localhost:8080/movies/search?name=the&genre=Drama"
```

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## 🔍 Search Features

### Web Interface
- **Interactive Form**: Pirate-themed search form with multiple fields
- **Genre Dropdown**: Populated with all available genres
- **Real-time Feedback**: Pirate messages for search results
- **No Results Handling**: Friendly messages when no movies match
- **Search Persistence**: Form remembers your search terms

### API Features
- **Flexible Parameters**: Search by any combination of name, ID, or genre
- **Case Insensitive**: Search works regardless of capitalization
- **Partial Matching**: Find movies with partial name or genre matches
- **Validation**: Comprehensive parameter validation with pirate-themed errors
- **Structured Response**: Consistent JSON format with metadata

### Search Logic
- **ID Priority**: If ID is provided, it takes priority over other parameters
- **Case Insensitive**: All text searches are case insensitive
- **Partial Matching**: Name and genre support partial matches
- **Whitespace Handling**: Automatic trimming of search parameters
- **Empty Results**: Graceful handling of searches with no matches

## 🧪 Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest
mvn test -Dtest=MoviesControllerTest

# Run with coverage
mvn test jacoco:report
```

### Test Coverage
- **MovieService**: Complete coverage of search functionality
- **MoviesController**: Web and API endpoint testing
- **Edge Cases**: Invalid parameters, empty results, error handling
- **Integration**: End-to-end testing of search workflows

## 🐛 Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Check that movies.json is properly loaded
2. Verify search parameters are correctly formatted
3. Check application logs for pirate error messages

### API returning 400 errors

Ensure at least one search parameter is provided:
```bash
# ❌ This will fail
curl "http://localhost:8080/movies/search"

# ✅ This will work
curl "http://localhost:8080/movies/search?name=prison"
```

## 🤝 Contributing

Ahoy! This treasure chest be open for improvements from fellow pirates:

### New Features to Add
- **Advanced Filters**: Rating range, year range, duration filters
- **Sorting Options**: Sort by rating, year, name, or duration
- **Favorites System**: Let users mark their favorite movies
- **Movie Recommendations**: Suggest similar movies
- **User Reviews**: Allow users to add their own reviews

### Code Improvements
- Add more movies to the catalog
- Enhance the pirate-themed UI/UX
- Improve responsive design for mobile devices
- Add internationalization support
- Implement caching for better performance

### Testing Enhancements
- Add integration tests
- Implement performance testing
- Add browser automation tests
- Increase test coverage

## 📜 API Documentation

### Error Responses

All API endpoints return consistent error responses:

```json
{
  "success": false,
  "message": "Arrr! Ye need to provide at least one search parameter, matey!",
  "pirateWisdom": "A pirate without a map be lost at sea! Give me somethin' to search for!",
  "error": "Validation failed"
}
```

### Status Codes
- **200 OK**: Successful search (even with 0 results)
- **400 Bad Request**: Invalid or missing parameters
- **500 Internal Server Error**: Server-side errors

## 🏴‍☠️ Pirate Language Guide

This application uses pirate-themed language throughout:

- **Movies** → Movie Treasure / Treasure Chest
- **Director** → Captain
- **Genre** → Adventure Type
- **Search** → Hunt for Treasure
- **Results** → Treasure Found
- **Errors** → Storms at Sea
- **Users** → Matey / Landlubber / Ye Scurvy Dog

## 📄 License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*Arrr! May fair winds fill yer sails as ye explore this movie treasure chest! 🏴‍☠️*
