package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class JSONHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void salvaSuFile(String path, List<Libro> libri) throws Exception {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), libri);
    }

    public static List<Libro> caricaDaFile(String path) throws Exception {
        return mapper.readValue(new File(path), new TypeReference<List<Libro>>() {});
    }
}

