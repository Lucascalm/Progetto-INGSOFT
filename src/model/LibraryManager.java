package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class LibraryManager {
    private List<Libro> libri;

    public LibraryManager() {
        libri = new ArrayList<>();
    }

    public void aggiungiLibro(Libro l) {
        if (!libri.contains(l)) {
            libri.add(l);
        }
    }


    public boolean rimuoviLibro(Libro libro) {
        return libri.remove(libro);
    }


    public List<Libro> getLibri() {
    	return Collections.unmodifiableList(libri);
    }
}

