package model;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private List<Libro> libri;

    public LibraryManager() {
        libri = new ArrayList<>();
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

