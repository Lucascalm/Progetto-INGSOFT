package model;

public class ConcreteLibroCreator extends LibroCreator {

    @Override
    protected ILibro creaLibro(String titolo, String autore, String isbn, String genere, int valutazione, StatoLettura stato) {
        return new Libro(titolo, autore, isbn, genere, valutazione, stato);
    }
}


