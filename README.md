# Control de alumnos

Programa de consola para registrar, consultar, actualizar y eliminar alumnos. Los datos se guardan en `alumnos.txt`, un registro por línea, con este formato:

```text
ID - Nombre completo
```

Se necesita Java 11 o posterior. La carpeta incluye `pom.xml` para abrir el proyecto desde NetBeans. Desde NetBeans, usa **File > Open Project** y selecciona esta carpeta; la clase principal es `Main`.

También se puede compilar y ejecutar desde una terminal abierta en esta carpeta:

```text
javac Alumno.java AlumnoDAO.java Main.java
java Main
```

El programa crea `alumnos.txt` al iniciarse si todavía no existe. El menú permite registrar un alumno, ver la lista, buscar por ID, cambiar el nombre y eliminar un registro con confirmación.
