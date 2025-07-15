package model;

import model.Utente;
import model.UtenteManager;

public class Utente {
    private String username;
    private LibraryManager manager;
    

    public Utente(String username) {
        this.username = username;
        this.manager = new LibraryManager(); // ogni utente ha la propria libreria
    }

    public String getUsername() {
        return username;
    }

    public LibraryManager getManager() {
        return manager;
    }
}
