package strategy;

import model.Libro;
import java.util.List;

public interface Ordinatore {
    List<Libro> ordina(List<Libro> libri);
}
