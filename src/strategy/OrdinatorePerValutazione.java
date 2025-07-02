package strategy;

import model.Libro;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class OrdinatorePerValutazione implements Ordinatore {

    @Override
    public List<Libro> ordina(List<Libro> libri) {
        List<Libro> ordinata = new ArrayList<>(libri);
        // Ordina dalla valutazione più alta alla più bassa
        Collections.sort(ordinata, Comparator.comparing(Libro::getValutazione).reversed());
        return ordinata;
    }
}

