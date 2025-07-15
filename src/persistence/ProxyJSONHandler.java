package persistence;

import model.Libro;
import java.util.List;

public class ProxyJSONHandler implements Salvataggio {
    private JSONHandlerReal realHandler;

    public ProxyJSONHandler() {
        this.realHandler = new JSONHandlerReal();
    }

    @Override
    public void salvaSuFile(String path, List<Libro> libri) throws Exception {
        System.out.println("[Proxy] Salvataggio file: " + path);
        realHandler.salvaSuFile(path, libri);
    }

    @Override
    public List<Libro> caricaDaFile(String path) throws Exception {
        System.out.println("[Proxy] Caricamento file: " + path);
        return realHandler.caricaDaFile(path);
    }
}




