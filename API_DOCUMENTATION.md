# 🏴‍☠️ Pirate's Movie API Documentation

Ahoy matey! This be the complete treasure map for integrating with our movie search API. Whether ye be a seasoned developer or a landlubber just startin' out, this guide will help ye navigate our movie treasure chest like a true pirate!

## 🗺️ API Overview

The Pirate's Movie API provides RESTful endpoints for searching and retrieving movie information. All responses include pirate-themed messages and comprehensive error handling to make integration as smooth as sailing on calm seas.

**Base URL**: `http://localhost:8080`

## 🔍 Search Endpoints

### Movie Search API

**Endpoint**: `GET /movies/search`

**Description**: Search for movies by name, ID, or genre with flexible filtering options.

**Parameters**:
- `name` (string, optional): Movie name to search for (supports partial matching, case insensitive)
- `id` (integer, optional): Specific movie ID to find (takes priority over other parameters)
- `genre` (string, optional): Genre to filter by (supports partial matching, case insensitive)

**Validation Rules**:
- At least one parameter must be provided
- `id` must be a positive integer if provided
- Empty strings are treated as null values
- Whitespace is automatically trimmed

**Response Format**:
```json
{
  "success": boolean,
  "movies": [Movie],
  "totalResults": integer,
  "message": string,
  "pirateWisdom": string,
  "searchParameters": {
    "name": string|null,
    "id": integer|null,
    "genre": string|null
  }
}
```

**Movie Object Structure**:
```json
{
  "id": integer,
  "movieName": string,
  "director": string,
  "year": integer,
  "genre": string,
  "description": string,
  "duration": integer,
  "imdbRating": double,
  "icon": string
}
```

## 📋 Example Requests and Responses

### Search by Movie Name

**Request**:
```bash
GET /movies/search?name=prison
```

**Response** (200 OK):
```json
{
  "success": true,
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0,
      "icon": "🏛️"
    }
  ],
  "totalResults": 1,
  "message": "Ahoy! Found 1 movies in our treasure chest!",
  "pirateWisdom": "Ye've struck gold, matey! These be fine films for any seafarin' soul!",
  "searchParameters": {
    "name": "prison",
    "id": null,
    "genre": null
  }
}
```

### Search by Genre

**Request**:
```bash
GET /movies/search?genre=Crime
```

**Response** (200 OK):
```json
{
  "success": true,
  "movies": [
    {
      "id": 2,
      "movieName": "The Family Boss",
      "director": "Michael Filmmaker",
      "year": 1972,
      "genre": "Crime/Drama",
      "description": "The aging patriarch of an organized crime dynasty...",
      "duration": 175,
      "imdbRating": 5.0,
      "icon": "👑"
    },
    {
      "id": 3,
      "movieName": "The Masked Hero",
      "director": "Chris Moviemaker",
      "year": 2008,
      "genre": "Action/Crime",
      "description": "When a menacing villain wreaks havoc...",
      "duration": 152,
      "imdbRating": 5.0,
      "icon": "🦸"
    }
  ],
  "totalResults": 2,
  "message": "Ahoy! Found 2 movies in our treasure chest!",
  "pirateWisdom": "Ye've struck gold, matey! These be fine films for any seafarin' soul!",
  "searchParameters": {
    "name": null,
    "id": null,
    "genre": "Crime"
  }
}
```

### Search by ID

**Request**:
```bash
GET /movies/search?id=1
```

**Response** (200 OK):
```json
{
  "success": true,
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0,
      "icon": "🏛️"
    }
  ],
  "totalResults": 1,
  "message": "Ahoy! Found 1 movies in our treasure chest!",
  "pirateWisdom": "Ye've struck gold, matey! These be fine films for any seafarin' soul!",
  "searchParameters": {
    "name": null,
    "id": 1,
    "genre": null
  }
}
```

### No Results Found

**Request**:
```bash
GET /movies/search?name=nonexistent
```

**Response** (200 OK):
```json
{
  "success": true,
  "movies": [],
  "totalResults": 0,
  "message": "No movies found matching yer search criteria, ye scurvy dog!",
  "pirateWisdom": "The seas be vast, but no treasure matches yer map. Try different search terms!",
  "searchParameters": {
    "name": "nonexistent",
    "id": null,
    "genre": null
  }
}
```

## ⚠️ Error Responses

### Missing Parameters

**Request**:
```bash
GET /movies/search
```

**Response** (400 Bad Request):
```json
{
  "success": false,
  "message": "Arrr! Ye need to provide at least one search parameter, matey! Use 'name', 'id', or 'genre'.",
  "pirateWisdom": "A pirate without a map be lost at sea! Give me somethin' to search for!"
}
```

### Invalid ID Parameter

**Request**:
```bash
GET /movies/search?id=-1
```

**Response** (200 OK):
```json
{
  "success": true,
  "movies": [],
  "totalResults": 0,
  "message": "No movies found matching yer search criteria, ye scurvy dog!",
  "pirateWisdom": "The seas be vast, but no treasure matches yer map. Try different search terms!",
  "searchParameters": {
    "name": null,
    "id": -1,
    "genre": null
  }
}
```

### Server Error

**Response** (500 Internal Server Error):
```json
{
  "success": false,
  "message": "Shiver me timbers! An error occurred while searchin' for movies.",
  "pirateWisdom": "Even the best pirates face storms at sea. Try again later, matey!",
  "error": "Internal server error details"
}
```

## 🌐 Web Interface Endpoints

### Movies Page with Search

**Endpoint**: `GET /movies`

**Description**: Returns HTML page with movie listings and search form.

**Parameters**:
- `name` (string, optional): Movie name to search for
- `id` (integer, optional): Specific movie ID to find
- `genre` (string, optional): Genre to filter by

**Response**: HTML page with search form and movie results

**Examples**:
```bash
GET /movies                    # All movies
GET /movies?name=prison        # Search by name
GET /movies?genre=Drama        # Filter by genre
GET /movies?id=1               # Find specific movie
```

### Movie Details

**Endpoint**: `GET /movies/{id}/details`

**Description**: Returns HTML page with detailed movie information.

**Parameters**:
- `id` (path parameter): Movie ID (1-12)

**Response**: HTML page with movie details and reviews

## 🔧 Integration Examples

### JavaScript/Fetch API

```javascript
// Search for movies by name
async function searchMovies(name) {
  try {
    const response = await fetch(`/movies/search?name=${encodeURIComponent(name)}`);
    const data = await response.json();
    
    if (data.success) {
      console.log(`Found ${data.totalResults} movies:`, data.movies);
      return data.movies;
    } else {
      console.error('Search failed:', data.message);
      return [];
    }
  } catch (error) {
    console.error('Network error:', error);
    return [];
  }
}

// Search with multiple parameters
async function advancedSearch(name, genre) {
  const params = new URLSearchParams();
  if (name) params.append('name', name);
  if (genre) params.append('genre', genre);
  
  const response = await fetch(`/movies/search?${params}`);
  return await response.json();
}
```

### Python/Requests

```python
import requests

def search_movies(name=None, movie_id=None, genre=None):
    """Search for movies using the API"""
    base_url = "http://localhost:8080/movies/search"
    params = {}
    
    if name:
        params['name'] = name
    if movie_id:
        params['id'] = movie_id
    if genre:
        params['genre'] = genre
    
    if not params:
        raise ValueError("At least one search parameter is required")
    
    try:
        response = requests.get(base_url, params=params)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error searching movies: {e}")
        return None

# Example usage
results = search_movies(name="prison")
if results and results['success']:
    print(f"Found {results['totalResults']} movies")
    for movie in results['movies']:
        print(f"- {movie['movieName']} ({movie['year']})")
```

### cURL Examples

```bash
# Basic search by name
curl "http://localhost:8080/movies/search?name=prison"

# Search by genre with spaces (URL encoded)
curl "http://localhost:8080/movies/search?genre=Crime%2FDrama"

# Multiple parameters
curl "http://localhost:8080/movies/search?name=the&genre=Drama"

# With headers for JSON response
curl -H "Accept: application/json" \
     "http://localhost:8080/movies/search?name=prison"

# Pretty print JSON response
curl -s "http://localhost:8080/movies/search?name=prison" | jq '.'
```

## 📊 Response Status Codes

| Status Code | Description | When It Occurs |
|-------------|-------------|----------------|
| 200 OK | Successful request | Valid search parameters provided |
| 400 Bad Request | Invalid request | No search parameters provided |
| 500 Internal Server Error | Server error | Unexpected server-side error |

## 🎯 Best Practices

### Parameter Handling
- Always URL-encode parameter values
- Handle both successful searches with no results (empty array) and error responses
- Implement proper error handling for network failures
- Use case-insensitive searches for better user experience

### Performance Considerations
- The API searches through a static JSON file, so performance is consistent
- No rate limiting is currently implemented
- Consider caching results on the client side for repeated searches

### Error Handling
```javascript
async function robustMovieSearch(searchParams) {
  try {
    const response = await fetch(`/movies/search?${new URLSearchParams(searchParams)}`);
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    
    if (!data.success) {
      console.warn('Search unsuccessful:', data.message);
      return { success: false, error: data.message };
    }
    
    return { success: true, movies: data.movies, total: data.totalResults };
    
  } catch (error) {
    console.error('Search failed:', error);
    return { success: false, error: error.message };
  }
}
```

## 🧪 Testing the API

### Manual Testing
```bash
# Test all endpoints
curl "http://localhost:8080/movies/search?name=prison"
curl "http://localhost:8080/movies/search?id=1"
curl "http://localhost:8080/movies/search?genre=Drama"
curl "http://localhost:8080/movies/search?name=the&genre=Drama"

# Test error cases
curl "http://localhost:8080/movies/search"  # Should return 400
curl "http://localhost:8080/movies/search?id=999"  # Should return empty results
```

### Automated Testing
The API includes comprehensive unit tests covering:
- Valid search scenarios
- Edge cases (empty results, invalid parameters)
- Error handling
- Response format validation

Run tests with:
```bash
mvn test -Dtest=MoviesControllerTest
```

## 🔮 Future Enhancements

Planned API improvements:
- **Pagination**: Support for large result sets
- **Sorting**: Sort results by rating, year, name, or duration
- **Advanced Filters**: Rating range, year range, duration filters
- **Rate Limiting**: Prevent API abuse
- **Authentication**: Secure API access
- **Caching**: Improve performance with response caching

---

*Arrr! May this API documentation guide ye to successful integration with our movie treasure chest! 🏴‍☠️*