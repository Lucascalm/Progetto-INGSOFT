package persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.LibroDTO;
import model.Libro;
import model.StatoLettura;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONHandler {

    private static Gson gson = new Gson();

    public static void salvaSuFile(String path, List<Libro> libri) throws Exception {
        List<LibroDTO> dtoList = new ArrayList<>();
        for (Libro l : libri) {
            dtoList.add(new LibroDTO(
                    l.getTitolo(),
                    l.getAutore(),
                    l.getIsbn(),
                    l.getGenere(),
                    l.getValutazione(),
                    l.getStato().name()
            ));
        }
        FileWriter writer = new FileWriter(path);
        gson.toJson(dtoList, writer);
        writer.close();
    }

    public static List<Libro> caricaDaFile(String path) throws Exception {
        FileReader reader = new FileReader(path);
        Type listType = new TypeToken<List<LibroDTO>>(){}.getType();
        List<LibroDTO> dtoList = gson.fromJson(reader, listType);
        reader.close();

        List<Libro> libri = new ArrayList<>();
        for (LibroDTO dto : dtoList) {
            libri.add(new Libro(
                    dto.titolo,
                    dto.autore,
                    dto.isbn,
                    dto.genere,
                    dto.valutazione,
                    StatoLettura.valueOf(dto.stato)
            ));
        }
        return libri;
    }
}
