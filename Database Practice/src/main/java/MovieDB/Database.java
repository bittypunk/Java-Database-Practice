package MovieDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    /// SQL Operations

    private String databasePath;

    Database(String databasePath) {     // Constructor

        this.databasePath = databasePath;

        try (Connection connection = DriverManager.getConnection(databasePath);
        Statement statement = connection.createStatement()) {

            // creates a table as long as it doesn't exist, errors without it if it already exists
            statement.execute("CREATE TABLE IF NOT EXISTS movies " +
                    "(id PRIMARY KEY, " +
                    "name TEXT UNIQUE NOT NULL CHECK(length(trim(name)) > 0), " +
                    "stars INTEGER CHECK(stars >= 0 AND stars <= 5), " +
                    "watched BOOLEAN )");

        } catch (SQLException sqle) {
            System.err.println("Error creating movie DB table because " + sqle);
        }
    }

    public void addNewMovie(Movie movie) {
        String insertSQL = "INSERT INTO movies VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(databasePath);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, movie.name);
            preparedStatement.setInt(2, movie.stars);
            preparedStatement.setBoolean(3, movie.watched);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Error adding movie " + movie + " because " + e);
        }
    }

    public List<Movie> getAllMovies() {
        try (Connection connection = DriverManager.getConnection(databasePath);
            Statement statement = connection.createStatement() ) {

            // gets all the movies and orders them by name
            ResultSet movieResults = statement.executeQuery("SELECT * FROM movies ORDER BY name");

            List<Movie> movies = new ArrayList<>();

            // while there's a movie, add it to a new list, still sorted by name
            while (movieResults.next()) {
                int id = movieResults.getInt("id");
                String name = movieResults.getString("name");
                int stars = movieResults.getInt("stars");
                boolean watched = movieResults.getBoolean("watched");

                Movie movie = new Movie(id, name, stars, watched);
                movies.add(movie);
            }

            return movies;
        } catch (SQLException sqle) {
            System.err.println("Error querying movie DB table because " + sqle);
            return null;
        }
    }

    public List<Movie> getAllMoviesByWatched(boolean watchedStatus) {
        // uses a prepared statement to order the table by watch status
        try (Connection connection = DriverManager.getConnection(databasePath);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM movies WHERE watched = ?")) {

            preparedStatement.setBoolean(1, watchedStatus);
            ResultSet movieResults = preparedStatement.executeQuery();

            List<Movie> movies = new ArrayList<>();

            while (movieResults.next()) {
                int id = movieResults.getInt("id");
                String name = movieResults.getString("name");
                int stars = movieResults.getInt("stars");
                boolean watched = movieResults.getBoolean("watched");
                Movie movie = new Movie(id, name, stars, watched);

                movies.add(movie);
            }

            return movies;

        } catch (SQLException sqle) {
            System.err.println("Error querying movie DB table for movies by watched status because " + sqle);
            return null;
        }
    }

    public void updateMovie(Movie movie) {
        String sql = "UPDATE movies SET stars = ?, watched = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(databasePath);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(2, movie.stars);
            preparedStatement.setBoolean(3, movie.watched);
            preparedStatement.setString(1, movie.name);

            preparedStatement.executeUpdate();

        }   catch (SQLException sqle) {
            System.err.println("Error updating movie DB table for movie " + movie + " because " + sqle);
        }
    }

    public void deleteMovie(Movie movie) {
        try (Connection connection = DriverManager.getConnection(databasePath);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM movies WHERE id = ?")) {

            preparedStatement.setString(2, movie.name);
            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            System.err.println("Error deleting movie from table for movie " + movie + " because " + sqle);
        }
    }

    public List<Movie> searchMovies(String search) {

        // compare to uppercase to avoid case-sensitivity
        final String searchSQL = "SELECT * FROM movies WHERE upper(name) LIKE upper(?)";

        try (Connection connection = DriverManager.getConnection(databasePath);
        PreparedStatement searchStatement = connection.prepareStatement(searchSQL)){

            searchStatement.setString(1, "%" + search + "%");
            ResultSet movieResults = searchStatement.executeQuery();

            // store resultSet into a list
            List<Movie> movieMatches = new ArrayList<>();
            while (movieResults.next()) {
                int id = movieResults.getInt("id");
                String name = movieResults.getString("name");
                int stars = movieResults.getInt("stars");
                boolean watched = movieResults.getBoolean("watched");
                Movie movie = new Movie(id, name, stars, watched);

                movieMatches.add(movie);
            }

            return movieMatches;

        } catch (SQLException sqle) {
            System.err.println("Error searching DB table by watched status because " + sqle);
            return null;
        }
    }



}
