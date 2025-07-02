package dto;

public class LibroDTO {
    public String titolo;
    public String autore;
    public String isbn;
    public String genere;
    public int valutazione;
    public String stato;

    public LibroDTO() {
        // Costruttore vuoto richiesto da Gson
    }

    public LibroDTO(String titolo, String autore, String isbn, String genere, int valutazione, String stato) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
        this.valutazione = valutazione;
        this.stato = stato;
    }
}

