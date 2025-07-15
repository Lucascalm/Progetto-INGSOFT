package controller;

import model.*;
import persistence.ProxyJSONHandler;
import persistence.Salvataggio;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryFacade {

    private LibraryManager manager;
    private String nomeUtente;
    private Salvataggio handler;

    public LibraryFacade(LibraryManager manager, String nomeUtente) {
        this.manager = manager;
        this.nomeUtente = nomeUtente;
        this.handler = new ProxyJSONHandler();
    }

    public static LibraryFacade creaConUtente() {
        String nomeUtente = "";

        while (nomeUtente == null || nomeUtente.isBlank()) {
            nomeUtente = JOptionPane.showInputDialog(null, "Inserisci il tuo nome utente:", "Login", JOptionPane.QUESTION_MESSAGE);
            if (nomeUtente == null) {
                System.exit(0);  // chiude il programma se clicca "Annulla"
            }
            if (nomeUtente.isBlank()) {
                JOptionPane.showMessageDialog(null, "⚠ Devi inserire un nome utente valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }

        LibraryManager manager = new LibraryManager();
        LibraryFacade facade = new LibraryFacade(manager, nomeUtente);

        String filePath = "libri_" + nomeUtente + ".json";
        try {
            List<Libro> caricati = facade.handler.caricaDaFile(filePath);
            manager.getLibri().addAll(caricati);
        } catch (Exception e) {
            System.out.println("Nessun file da caricare per " + nomeUtente);
        }

        return facade;
    }


    public void salvaSuFile() {
        String filePath = "libri_" + nomeUtente + ".json";
        try {
            handler.salvaSuFile(filePath, manager.getLibri());
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    public void aggiungiLibro(String titolo, String autore, String isbn, String genere, int valutazione, StatoLettura stato) {
        boolean esiste = manager.getLibri().stream()
                .anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn));

        if (!esiste) {
            LibroCreator creator = new ConcreteLibroCreator();
            ILibro libro = creator.crea(titolo, autore, isbn, genere, valutazione, stato);
            manager.aggiungiLibro((Libro) libro);  // downcasting da ILibro a Libro
        } else {
            JOptionPane.showMessageDialog(null,
                    "Un libro con lo stesso ISBN Ã¨ giÃ  presente!",
                    "Errore di aggiunta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public void rimuoviLibro(Libro libro) {
        manager.rimuoviLibro(libro);
    }

    public boolean rimuoviLibroPerTitolo(String titolo) {
        return manager.getLibri().removeIf(l -> l.getTitolo().equalsIgnoreCase(titolo));
    }

    public List<Libro> getLibri() {
        return manager.getLibri();
    }

    public void modificaLibro(int index, Libro nuovoLibro) {
        if (index >= 0 && index < manager.getLibri().size()) {
            manager.getLibri().set(index, nuovoLibro);
        }
    }

    public List<Libro> cercaPerTitolo(String titolo) {
        return manager.getLibri().stream()
                .filter(l -> l.getTitolo().toLowerCase().contains(titolo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Libro> cercaPerAutore(String autore) {
        return manager.getLibri().stream()
                .filter(l -> l.getAutore().toLowerCase().contains(autore.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Libro> filtraPerStato(StatoLettura stato) {
        return manager.getLibri().stream()
                .filter(l -> l.getStato() == stato)
                .collect(Collectors.toList());
    }

    public List<Libro> filtraPerGenere(String genere) {
        return manager.getLibri().stream()
                .filter(l -> l.getGenere().equalsIgnoreCase(genere))
                .collect(Collectors.toList());
    }

    public void salvaJSON(String path) throws Exception {
        handler.salvaSuFile(path, manager.getLibri());
    }

    public void caricaJSON(String path) throws Exception {
        List<Libro> caricati = handler.caricaDaFile(path);
        manager.getLibri().clear();
        manager.getLibri().addAll(caricati);
    }

    public String getNomeUtente() {
        return nomeUtente;
    }
}

