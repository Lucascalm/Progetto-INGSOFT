package test;

import controller.LibraryFacade;
import model.*;
import org.junit.jupiter.api.Test;
import strategy.OrdinatorePerTitolo;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class LibraryFacadeTest {

    private LibraryFacade creaFacade() {
        LibraryManager manager = new LibraryManager();
        return new LibraryFacade(manager, "utenteTest");
    }

    @Test
    void testAggiuntaEDuplicatiTramiteFacade() {
        LibraryFacade facade = creaFacade();

        facade.aggiungiLibro("Zoo", "AutoreX", "001", "Fantascienza", 5, StatoLettura.LETTO);
        facade.aggiungiLibro("Alpha", "AutoreY", "002", "Fantasy", 4, StatoLettura.NON_LETTO);
        facade.aggiungiLibro("Zoo", "AutoreX", "001", "Fantascienza", 5, StatoLettura.LETTO); // duplicato

        List<Libro> ordinati = new OrdinatorePerTitolo().ordina(facade.getLibri());
        List<String> titoli = ordinati.stream().map(Libro::getTitolo).collect(Collectors.toList());

        assertEquals(2, ordinati.size(), "Duplicato non deve essere aggiunto");
        assertEquals(List.of("Alpha", "Zoo"), titoli);
    }

    @Test
    void testRimozioneLibroEsistenteENon() {
        LibraryFacade facade = creaFacade();

        facade.aggiungiLibro("Libro A", "Autore", "001", "Genere", 3, StatoLettura.LETTO);
        facade.aggiungiLibro("Libro B", "Autore", "002", "Genere", 3, StatoLettura.LETTO);

        Libro daRimuovere = facade.getLibri().get(0);
        facade.rimuoviLibro(daRimuovere);

        assertEquals(1, facade.getLibri().size(), "Libro A deve essere stato rimosso");

        // provo a rimuovere un libro non presente
        Libro inesistente = new Libro("Libro X", "Autore", "999", "Genere", 3, StatoLettura.LETTO);
        facade.rimuoviLibro(inesistente); // non fa nulla

        assertEquals(1, facade.getLibri().size(), "Libro X non esiste, quindi la size deve restare invariata");
    }

    @Test
    void testOrdinamentoTitoliConCaratteriMistiViaFacade() {
        LibraryFacade facade = creaFacade();

        facade.aggiungiLibro("Zorro", "A", "1", "x", 3, StatoLettura.NON_LETTO);
        facade.aggiungiLibro("123 Avventure", "B", "2", "x", 3, StatoLettura.NON_LETTO);
        facade.aggiungiLibro("Boom", "C", "3", "x", 3, StatoLettura.NON_LETTO);
        facade.aggiungiLibro("Èpico", "D", "4", "x", 3, StatoLettura.NON_LETTO);

        List<String> attesi = List.of("123 Avventure", "Boom", "Zorro", "Èpico");

        List<String> ottenuti = new OrdinatorePerTitolo()
                .ordina(facade.getLibri())
                .stream().map(Libro::getTitolo).collect(Collectors.toList());

        assertEquals(attesi, ottenuti, "L'ordinamento deve rispettare l'ordine ASCII");
    }

    @Test
    void testValutazioniEstremeOrdinamentoNonInfluenza() {
        LibraryFacade facade = creaFacade();

        facade.aggiungiLibro("Uno", "A", "1", "X", 0, StatoLettura.NON_LETTO);   // valutazione minima
        facade.aggiungiLibro("Due", "B", "2", "X", 5, StatoLettura.LETTO);         // valutazione massima
        facade.aggiungiLibro("Tre", "C", "3", "X", 3, StatoLettura.IN_LETTURA);    // valutazione media

        List<String> attesi = List.of("Due", "Tre", "Uno");

        List<String> ottenuti = new OrdinatorePerTitolo()
                .ordina(facade.getLibri())
                .stream().map(Libro::getTitolo).collect(Collectors.toList());

        assertEquals(attesi, ottenuti, "Valutazione non deve influenzare ordinamento per titolo");
    }
}


