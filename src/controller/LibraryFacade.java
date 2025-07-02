package controller;

import model.*;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryFacade {

    private LibraryManager manager;

    public LibraryFacade() {
        manager = LibraryManager.getInstance();
    }

    // ✅ AGGIUNGI LIBRO
    public void aggiungiLibro(String titolo, String autore, String isbn, String genere, int valutazione, StatoLettura stato) {
        // Verifica se esiste già un libro con lo stesso ISBN
        boolean esiste = manager.getLibri().stream()
            .anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn));
        
        if (!esiste) {
            Libro libro = LibroFactory.creaLibro(titolo, autore, isbn, genere, valutazione, stato);
            manager.aggiungiLibro(libro);
        } else {
        	JOptionPane.showMessageDialog(null,
                    "Un libro con lo stesso ISBN è già presente!",
                    "Errore di aggiunta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // ✅ RIMUOVI LIBRO DIRETTO
    public void rimuoviLibro(Libro libro) {
        manager.rimuoviLibro(libro);
    }

    // ✅ RIMUOVI PER TITOLO
    public boolean rimuoviLibroPerTitolo(String titolo) {
        return manager.getLibri().removeIf(l -> l.getTitolo().equalsIgnoreCase(titolo));
    }

    // ✅ OTTIENI TUTTI I LIBRI
    public List<Libro> getLibri() {
        return manager.getLibri();
    }

    // ✅ MODIFICA LIBRO A INDICE
    public void modificaLibro(int index, Libro nuovoLibro) {
        if (index >= 0 && index < manager.getLibri().size()) {
            manager.getLibri().set(index, nuovoLibro);
        }
    }

    // ✅ RICERCA PER TITOLO
    public List<Libro> cercaPerTitolo(String titolo) {
        return manager.getLibri().stream()
                .filter(l -> l.getTitolo().toLowerCase().contains(titolo.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ✅ RICERCA PER AUTORE
    public List<Libro> cercaPerAutore(String autore) {
        return manager.getLibri().stream()
                .filter(l -> l.getAutore().toLowerCase().contains(autore.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ✅ FILTRO PER STATO DI LETTURA
    public List<Libro> filtraPerStato(StatoLettura stato) {
        return manager.getLibri().stream()
                .filter(l -> l.getStato() == stato)
                .collect(Collectors.toList());
    }

    // ✅ FILTRO PER GENERE
    public List<Libro> filtraPerGenere(String genere) {
        return manager.getLibri().stream()
                .filter(l -> l.getGenere().equalsIgnoreCase(genere))
                .collect(Collectors.toList());
    }

    // ✅ SALVATAGGIO SU JSON
    public void salvaJSON(String path) throws Exception {
        JSONHandler.salvaSuFile(path, manager.getLibri());
    }

    // ✅ CARICAMENTO DA JSON
    public void caricaJSON(String path) throws Exception {
        List<Libro> caricati = JSONHandler.caricaDaFile(path);
        manager.getLibri().clear();
        manager.getLibri().addAll(caricati);
    }
}

