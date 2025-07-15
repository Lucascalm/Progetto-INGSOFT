package model;

import java.util.HashMap;
import java.util.Map;

public class UtenteManager {
    private static Map<String, Utente> utenti = new HashMap<>();

    public static Utente getOrCreateUtente(String username) {
        return utenti.computeIfAbsent(username, Utente::new);
    }
}

