package persistence;

import model.Libro;
import java.util.List;

public interface Salvataggio {
    void salvaSuFile(String path, List<Libro> libri) throws Exception;
    List<Libro> caricaDaFile(String path) throws Exception;
}
