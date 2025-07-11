package model;

public abstract class LibroCreator {

    protected abstract ILibro creaLibro(String titolo, String autore, String isbn, String genere, int valutazione, StatoLettura stato);

    public ILibro crea(String titolo, String autore, String isbn, String genere, int valutazione, StatoLettura stato) {
        ILibro libro = creaLibro(titolo, autore, isbn, genere, valutazione, stato);
        return libro;
    }
}

