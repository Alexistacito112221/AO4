import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

// Esta clase se encarga de todo lo que es leer y escribir el archivo alumnos.txt
// aca usamos java.nio.file como pide el trabajo
public class AlumnoDAO {

    private final Path archivo = Path.of("alumnos.txt");
    private static final String SEPARADOR = " - ";

    // cuando se crea el DAO revisamos que el archivo exista, si no esta lo crea solo
    public AlumnoDAO() throws IOException {
        if (!Files.exists(archivo)) {
            Files.createFile(archivo);
        }
    }

    // agrega un alumno al final del archivo sin pisar lo que ya hay
    public void agregar(Alumno a) throws IOException {
        String linea = a.getId() + SEPARADOR + a.getNombre() + System.lineSeparator();
        Files.write(archivo, linea.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    // lee todas las lineas del archivo y las pasa a objetos Alumno
    public List<Alumno> leerTodos() throws IOException {
        List<Alumno> lista = new ArrayList<>();
        List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);

        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.trim().isEmpty()) {
                continue; // por si quedo alguna linea vacia
            }
            String[] partes = linea.split(SEPARADOR, 2);
            if (partes.length != 2
                    || partes[0].trim().isEmpty()
                    || partes[1].trim().isEmpty()) {
                throw new IOException(
                        "Formato incorrecto en alumnos.txt, linea " + (i + 1) + ".");
            }

            int id;
            try {
                id = Integer.parseInt(partes[0].trim());
            } catch (NumberFormatException e) {
                throw new IOException(
                        "El ID no es valido en alumnos.txt, linea " + (i + 1) + ".", e);
            }
            String nombre = partes[1].trim();
            lista.add(new Alumno(id, nombre));
        }
        return lista;
    }

    // busca un alumno por id, devuelve null si no esta
    public Alumno buscarPorId(int idBuscado) throws IOException {
        List<Alumno> lista = leerTodos();
        for (Alumno a : lista) {
            if (a.getId() == idBuscado) {
                return a;
            }
        }
        return null;
    }

    // sirve para revisar que no se repita el id al registrar
    public boolean existeId(int id) throws IOException {
        return buscarPorId(id) != null;
    }

    // actualiza el nombre del alumno que tenga ese id
    public boolean actualizar(int id, String nuevoNombre) throws IOException {
        List<Alumno> lista = leerTodos();
        boolean encontrado = false;

        for (Alumno a : lista) {
            if (a.getId() == id) {
                a.setNombre(nuevoNombre);
                encontrado = true;
            }
        }

        if (encontrado) {
            guardarLista(lista);
        }
        return encontrado;
    }

    // elimina la linea del alumno con ese id
    public boolean eliminar(int id) throws IOException {
        List<Alumno> lista = leerTodos();
        boolean encontrado = lista.removeIf(a -> a.getId() == id);

        if (encontrado) {
            guardarLista(lista);
        }
        return encontrado;
    }

    // metodo auxiliar que escribe la lista completa de nuevo en el archivo
    // (esto pisa el archivo pero con los datos ya actualizados)
    private void guardarLista(List<Alumno> lista) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (Alumno a : lista) {
            lineas.add(a.toString());
        }
        Files.write(
                archivo,
                lineas,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
