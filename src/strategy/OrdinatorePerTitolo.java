package strategy;

import model.Libro;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrdinatorePerTitolo implements Ordinatore {

    @Override
    public List<Libro> ordina(List<Libro> libri) {
        // Copia per non modificare la lista originale
        List<Libro> copia = List.copyOf(libri);
        List<Libro> ordinata = new java.util.ArrayList<>(copia);

        Collections.sort(ordinata, Comparator.comparing(Libro::getTitolo));
        return ordinata;
    }
}

