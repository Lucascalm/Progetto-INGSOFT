package main;

import strategy.Ordinatore;
import strategy.OrdinatorePerTitolo;
import controller.LibraryFacade;
import model.StatoLettura;
import strategy.OrdinatorePerAutore;
import strategy.OrdinatorePerValutazione;
import persistence.JSONHandler;
import java.util.List;
import model.Libro;

public class Main {
    public static void main(String[] args) {
        LibraryFacade facade = new LibraryFacade();

        facade.aggiungiLibro("Il nome della rosa", "Umberto Eco", "123456789", "Giallo", 5, StatoLettura.LETTO);
        facade.aggiungiLibro("1984", "George Orwell", "987654321", "Distopia", 4, StatoLettura.LETTO);

        facade.getLibri().forEach(l -> System.out.println(l));

        Ordinatore ordinatore = new OrdinatorePerTitolo();
        System.out.println("📚 Libri ordinati per titolo:");
        ordinatore.ordina(facade.getLibri()).forEach(System.out::println);

        System.out.println("\n📚 Libri ordinati per autore:");
        Ordinatore perAutore = new OrdinatorePerAutore();
        perAutore.ordina(facade.getLibri()).forEach(System.out::println);

        System.out.println("\n📚 Libri ordinati per valutazione:");
        Ordinatore perValutazione = new OrdinatorePerValutazione();
        perValutazione.ordina(facade.getLibri()).forEach(System.out::println);

        try {
            // Salva su JSON
            JSONHandler.salvaSuFile("libri.json", facade.getLibri());

            // Carica da JSON
            List<Libro> caricati = JSONHandler.caricaDaFile("libri.json");

            System.out.println("\n📥 Libri caricati da JSON:");
            caricati.forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("❌ Errore durante salvataggio/caricamento:");
            e.printStackTrace();
        }
        
        System.out.println("\n✏️ Modifico il primo libro...");
        Libro libroModificato = new Libro(
                "Il nome della rosa - Nuova Edizione",
                "Umberto Eco",
                "123456789",
                "Giallo Storico",
                4,
                StatoLettura.IN_LETTURA
        );
        facade.modificaLibro(0, libroModificato);

        System.out.println("\n📚 Dopo modifica:");
        facade.getLibri().forEach(System.out::println);

        System.out.println("\n🗑️ Rimuovo '1984' dalla libreria:");
        boolean rimosso = facade.rimuoviLibroPerTitolo("1984");
        System.out.println("Rimosso: " + rimosso);

        System.out.println("\n📚 Dopo rimozione:");
        facade.getLibri().forEach(System.out::println);

        System.out.println("\n🔍 Cerco libri con titolo contenente 'rosa':");
        List<Libro> trovatiTitolo = facade.cercaPerTitolo("rosa");
        trovatiTitolo.forEach(System.out::println);

        System.out.println("\n🔍 Cerco libri con autore contenente 'eco':");
        List<Libro> trovatiAutore = facade.cercaPerAutore("eco");
        trovatiAutore.forEach(System.out::println);

        System.out.println("\n🔎 Filtro per genere = 'Giallo Storico':");
        List<Libro> filtroGenere = facade.filtraPerGenere("Giallo Storico");
        filtroGenere.forEach(System.out::println);

        System.out.println("\n🔎 Filtro per stato = IN_LETTURA:");
        List<Libro> filtroStato = facade.filtraPerStato(StatoLettura.IN_LETTURA);
        filtroStato.forEach(System.out::println);

    }
}

