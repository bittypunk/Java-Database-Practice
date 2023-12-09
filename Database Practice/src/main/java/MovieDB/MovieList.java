package MovieDB;

import java.util.List;

import static input.InputUtils.*;

public class MovieList {
    /// Main program
    
    private static final String DB_PATH = "jdbc:sqlite:movie_watchlist.sqlite";
    private static Database database;
    
    public static void main(String[] args) {
        database = new Database(DB_PATH);
        addNewMovies();
        displayAllMovies();
        checkIfWatchedAndRate();
        deleteWatchedMovies();
        searchMovies();
    }

    private static void searchMovies() {
        String src = stringInput("Enter the movie title");

        List<Movie> matches = database.searchMovies(src);

        if (matches == null) {
            System.out.println(src + " not found in the movie list.");
        } else {
            System.out.println("Matching movie(s) found!");
            // display the matching movies
            for (Movie m : matches) {
                System.out.println(m.toString());
            }
        }
    }

    private static void checkIfWatchedAndRate() {
        /* Get all unwatched movies. Loop over, ask user if they've watched it,
        if so, ask for a rating, then update DB */

        List<Movie> unwatchedMovies = database.getAllMoviesByWatched(false);

        for (Movie movie : unwatchedMovies) {
            boolean hasWatched = yesNoInput("Have you watched " + movie.name + "?");
            if (hasWatched) {
                int stars = intInput("What is your rating for " + movie.name + " out of 5 stars?");
                movie.watched = true;
                movie.stars = stars;
                database.updateMovie(movie);

            }
        }
    }

    private static void addNewMovies() {
        do {
            String movieName = stringInput("Enter the movie name");
            boolean movieWatched = yesNoInput("Have you seen this movie yet?");
            int movieStars = 0;
            if (movieWatched) {
                movieStars = positiveIntInput("What is your rating, in stars out of 5?");

                // make sure number is between 0 - 5
                while (movieStars > 5) {
                    movieStars = positiveIntInput("Please enter a rating between 0 - 5");
                }
            }
            Movie movie = new Movie(0, movieName, movieStars, movieWatched);
            database.addNewMovie(movie);
        } while (yesNoInput("Add a movie to the watchlist?"));
    }

    private static void displayAllMovies() {
        List<Movie> allMovies = database.getAllMovies();
        for (Movie movie : allMovies) {
            System.out.println(movie.toString());
        }
    }

    private static void deleteWatchedMovies() {
        // get all the watched movies
        // loop over and ask if they should be deleted

        System.out.println("Here are the movies you've watched.");

        List<Movie> watchedMovies = database.getAllMoviesByWatched(true);

        for (Movie movie : watchedMovies) {
            boolean delete = yesNoInput("Delete " + movie.name + "?");
            if (delete) {
                database.deleteMovie(movie);
            }
        }
    }
}
