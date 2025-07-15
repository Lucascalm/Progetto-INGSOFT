package model;

public class Libro implements ILibro{
    private String titolo;
    private String autore;
    private String isbn;
    private String genere;
    private int valutazione; // da 1 a 5
    private StatoLettura stato;

    public Libro() {
    }

    public Libro(String titolo, String autore, String isbn, String genere, int valutazione, StatoLettura stato) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
        this.valutazione = valutazione;
        this.stato = stato;
    }

    // Getters e Setters
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getAutore() { return autore; }
    public void setAutore(String autore) { this.autore = autore; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getGenere() { return genere; }
    public void setGenere(String genere) { this.genere = genere; }

    public int getValutazione() { return valutazione; }
    public void setValutazione(int valutazione) { this.valutazione = valutazione; }

    public StatoLettura getStato() { return stato; }
    public void setStato(StatoLettura stato) { this.stato = stato; }

    @Override
    public String toString() {
        return titolo + " di " + autore + " (" + stato + ", " + valutazione + "â˜…)";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Libro libro = (Libro) o;

        return isbn != null ? isbn.equals(libro.isbn) : libro.isbn == null;
    }

    @Override
    public int hashCode() {
        return isbn != null ? isbn.hashCode() : 0;
    }
    
}



