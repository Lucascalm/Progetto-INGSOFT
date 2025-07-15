package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import model.*;
import strategy.OrdinatorePerTitolo;

class OrdinatorePerTitoloTest {

    @Test
    void testOrdinaTitoliConMaiuscoleMinuscoleSpazi() {
        List<Libro> lista = new ArrayList<>();
        lista.add(new Libro("zeta", "Autore1", "1", "genere", 3, StatoLettura.NON_LETTO));
        lista.add(new Libro(" Alfa", "Autore2", "2", "genere", 3, StatoLettura.NON_LETTO));
        lista.add(new Libro("beta", "Autore3", "3", "genere", 3, StatoLettura.NON_LETTO));
        lista.add(new Libro("Alfa", "Autore4", "4", "genere", 3, StatoLettura.NON_LETTO));
        lista = new OrdinatorePerTitolo().ordina(lista);


        List<String> titoliOrdinati = lista.stream()
            .map(Libro::getTitolo)
            .collect(Collectors.toList());

        List<String> attesi = Arrays.asList(" Alfa", "Alfa", "beta", "zeta");
        assertEquals(attesi, titoliOrdinati);
    }

    @Test
    void testOrdinaTitoliConCaratteriSpecialiNumeriAccentati() {
        List<Libro> lista = new ArrayList<>();
        lista.add(new Libro("Zorro", "Autore1", "1", "genere", 3, StatoLettura.NON_LETTO));
        lista.add(new Libro("àlba", "Autore2", "2", "genere", 3, StatoLettura.NON_LETTO));
        lista.add(new Libro("123 Avventure", "Autore3", "3", "genere", 3, StatoLettura.NON_LETTO));
        lista.add(new Libro("?Domanda", "Autore4", "4", "genere", 3, StatoLettura.NON_LETTO));
        lista.add(new Libro("!Esclamazione", "Autore5", "5", "genere", 3, StatoLettura.NON_LETTO));
        lista.add(new Libro("època", "Autore6", "6", "genere", 3, StatoLettura.NON_LETTO));
        
        lista = new OrdinatorePerTitolo().ordina(lista);

        List<String> titoliOrdinati = lista.stream()
            .map(Libro::getTitolo)
            .collect(Collectors.toList());

        List<String> attesi = Arrays.asList("!Esclamazione", "123 Avventure", "?Domanda", "Zorro", "àlba", "època");;
        assertEquals(attesi, titoliOrdinati);
    }
    
    
}


