package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Libro;
import model.LibraryManager;
import model.StatoLettura;

class LibraryManagerTest {

    private LibraryManager manager;

    @BeforeEach
    void setUp() {
        manager = new LibraryManager();
    }

    @Test
    void testAggiuntaDuplicati() {
        Libro libro = new Libro("1984", "George Orwell", "123", "Distopia", 5, null);
        manager.aggiungiLibro(libro);
        manager.aggiungiLibro(libro); // Duplicato

        List<Libro> libri = manager.getLibri();
        long count = libri.stream().filter(l -> l.equals(libro)).count();

        assertEquals(1, count, "Il libro non dovrebbe essere aggiunto due volte");
    }

    @Test
    void testRimuoviLibroAssente() {
        Libro libro = new Libro("Inesistente", "Nessuno", "000", "N/A", 0, null);
        boolean success = manager.rimuoviLibro(libro);

        assertFalse(success, "La rimozione di un libro non presente dovrebbe fallire");
    }

    @Test
    void testGetLibriImmutabile() {
        Libro libro = new Libro("Clean Code", "Robert C. Martin", "321", "Informatica", 5, StatoLettura.NON_LETTO);
        manager.aggiungiLibro(libro);

        List<Libro> libri = manager.getLibri();

        assertThrows(UnsupportedOperationException.class, () -> {
            libri.clear(); // Deve lanciare eccezione
        }, "La lista restituita deve essere immutabile");
    }

    @Test
    void testAggiuntaMultiplaLibriDiversi() {
        Libro l1 = new Libro("A", "Autore", "1", "Genere", 4, null);
        Libro l2 = new Libro("B", "Autore", "2", "Genere", 3, null);
        Libro l3 = new Libro("C", "Autore", "3", "Genere", 5, null);

        manager.aggiungiLibro(l1);
        manager.aggiungiLibro(l2);
        manager.aggiungiLibro(l3);

        List<Libro> libri = manager.getLibri();

        assertAll(
            () -> assertTrue(libri.contains(l1)),
            () -> assertTrue(libri.contains(l2)),
            () -> assertTrue(libri.contains(l3)),
            () -> assertEquals(3, libri.size())
        );
    }
}

