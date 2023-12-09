package MovieDB;

public class Movie {
    /// Stores movie data for one movie
    int id;
    public String name;
    public int stars;
    public boolean watched;

    Movie(String name) {this.name = name; }

    Movie(int id, String name, int stars, boolean watched) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.watched = watched;
    }
    /*
    // getters/setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
     */

    // generate toString
    @Override
    public String toString() {
        String str = "Movie id" + id + ". " + name + ". " + stars + " stars.";
        if (!watched) {
            str = "Movie id" + id + ". " + name + ". UNWATCHED";
        }

        return str;
    }
}
