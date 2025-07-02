package model;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private static LibraryManager instance = null;
    private List<Libro> libri;

    private LibraryManager() {
        libri = new ArrayList<>();
    }

    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    public void aggiungiLibro(Libro l) {
        libri.add(l);
    }

    public void rimuoviLibro(Libro l) {
        libri.remove(l);
    }

    public List<Libro> getLibri() {
        return libri;
    }
}
