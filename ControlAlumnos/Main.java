import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// Clase principal con el menu del programa
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AlumnoDAO dao;
        try {
            dao = new AlumnoDAO();
        } catch (IOException e) {
            System.out.println("No se pudo preparar alumnos.txt: " + e.getMessage());
            sc.close();
            return;
        }
        int opcion = 0;

        do {
            System.out.println();
            System.out.println("=== MENU GESTION DE ALUMNOS (JAVA NIO) ===");
            System.out.println("1. Registrar nuevo alumno");
            System.out.println("2. Ver todos los alumnos");
            System.out.println("3. Buscar alumno por ID");
            System.out.println("4. Actualizar nombre de alumno");
            System.out.println("5. Eliminar alumno");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: tiene que ingresar un numero del 1 al 6");
                sc.nextLine(); // limpiar el buffer para que no se tilde
                continue;
            }
            sc.nextLine(); // limpiar el enter que queda

            switch (opcion) {
                case 1:
                    registrarAlumno(sc, dao);
                    break;
                case 2:
                    mostrarTodos(dao);
                    break;
                case 3:
                    buscarAlumno(sc, dao);
                    break;
                case 4:
                    actualizarAlumno(sc, dao);
                    break;
                case 5:
                    eliminarAlumno(sc, dao);
                    break;
                case 6:
                    System.out.println("Saliendo del programa... chau!");
                    break;
                default:
                    System.out.println("Opcion no valida, proba de nuevo");
            }
        } while (opcion != 6);

        sc.close();
    }

    // opcion 1: registrar alumno nuevo
    private static void registrarAlumno(Scanner sc, AlumnoDAO dao) {
        System.out.println();
        System.out.println("--- Registrar Alumno ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(sc.nextLine());

            // validamos que el id no este repetido en el archivo
            if (dao.existeId(id)) {
                System.out.println("Error: ya existe un alumno con el ID " + id);
                return;
            }

            System.out.print("Ingrese Nombre: ");
            String nombre = sc.nextLine();

            if (nombre.trim().isEmpty()) {
                System.out.println("Error: el nombre no puede estar vacio");
                return;
            }

            dao.agregar(new Alumno(id, nombre));
            System.out.println("Alumno registrado con exito!");

        } catch (NumberFormatException e) {
            System.out.println("Error: el ID tiene que ser un numero");
        } catch (IOException e) {
            System.out.println("Error al guardar en el archivo: " + e.getMessage());
        }
    }

    // opcion 2: mostrar todos los alumnos del archivo
    private static void mostrarTodos(AlumnoDAO dao) {
        System.out.println();
        System.out.println("--- Lista de Alumnos ---");
        try {
            List<Alumno> lista = dao.leerTodos();

            if (lista.isEmpty()) {
                System.out.println("No hay alumnos cargados todavia");
                return;
            }

            for (Alumno a : lista) {
                System.out.println(a);
            }
            System.out.println("Total: " + lista.size() + " alumno/s");

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // opcion 3: buscar un alumno por su id
    private static void buscarAlumno(Scanner sc, AlumnoDAO dao) {
        System.out.println();
        System.out.println("--- Buscar Alumno ---");
        try {
            System.out.print("Ingrese el ID a buscar: ");
            int id = Integer.parseInt(sc.nextLine());

            Alumno a = dao.buscarPorId(id);
            if (a != null) {
                System.out.println("Alumno encontrado: " + a);
            } else {
                System.out.println("No se encontro ningun alumno con el ID " + id);
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: el ID tiene que ser un numero");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // opcion 4: cambiar el nombre de un alumno
    private static void actualizarAlumno(Scanner sc, AlumnoDAO dao) {
        System.out.println();
        System.out.println("--- Actualizar Alumno ---");
        try {
            System.out.print("Ingrese el ID del alumno a modificar: ");
            int id = Integer.parseInt(sc.nextLine());

            Alumno a = dao.buscarPorId(id);
            if (a == null) {
                System.out.println("Error: no existe un alumno con el ID " + id);
                return;
            }

            System.out.println("Alumno actual: " + a);
            System.out.print("Ingrese el nuevo nombre: ");
            String nuevoNombre = sc.nextLine();

            if (nuevoNombre.trim().isEmpty()) {
                System.out.println("Error: el nombre no puede estar vacio");
                return;
            }

            dao.actualizar(id, nuevoNombre);
            System.out.println("Nombre actualizado correctamente!");

        } catch (NumberFormatException e) {
            System.out.println("Error: el ID tiene que ser un numero");
        } catch (IOException e) {
            System.out.println("Error al actualizar el archivo: " + e.getMessage());
        }
    }

    // opcion 5: eliminar un alumno
    private static void eliminarAlumno(Scanner sc, AlumnoDAO dao) {
        System.out.println();
        System.out.println("--- Eliminar Alumno ---");
        try {
            System.out.print("Ingrese el ID del alumno a eliminar: ");
            int id = Integer.parseInt(sc.nextLine());

            // primero vemos si existe para avisar bien
            Alumno a = dao.buscarPorId(id);
            if (a == null) {
                System.out.println("Error: no existe un alumno con el ID " + id);
                return;
            }

            System.out.println("Se va a eliminar a: " + a);
            System.out.print("Esta seguro? (s/n): ");
            String respuesta = sc.nextLine();

            if (respuesta.equalsIgnoreCase("s")) {
                dao.eliminar(id);
                System.out.println("Alumno eliminado del archivo");
            } else {
                System.out.println("Operacion cancelada, no se borro nada");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: el ID tiene que ser un numero");
        } catch (IOException e) {
            System.out.println("Error al modificar el archivo: " + e.getMessage());
        }
    }
}
