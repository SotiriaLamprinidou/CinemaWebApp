package FilmSelection;

public class Reservations {
    private int numberOfSeats;
    private String filmTitle;
    private String cinemaName;

    public Reservations(int numberOfSeats, String filmTitle, String cinemaName) {
        this.numberOfSeats = numberOfSeats;
        this.filmTitle = filmTitle;
        this.cinemaName = cinemaName;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public String getCinemaName() {
        return cinemaName;
    }
}
