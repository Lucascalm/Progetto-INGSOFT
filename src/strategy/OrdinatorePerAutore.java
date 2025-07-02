package strategy;

import model.Libro;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class OrdinatorePerAutore implements Ordinatore {

    @Override
    public List<Libro> ordina(List<Libro> libri) {
        List<Libro> ordinata = new ArrayList<>(libri);
        Collections.sort(ordinata, Comparator.comparing(Libro::getAutore));
        return ordinata;
    }
}

