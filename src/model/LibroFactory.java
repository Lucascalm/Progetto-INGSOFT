package model;

public class LibroFactory {
    public static Libro creaLibro(String titolo, String autore, String isbn, String genere, int valutazione, StatoLettura stato) {
        return new Libro(titolo, autore, isbn, genere, valutazione, stato);
    }
}
