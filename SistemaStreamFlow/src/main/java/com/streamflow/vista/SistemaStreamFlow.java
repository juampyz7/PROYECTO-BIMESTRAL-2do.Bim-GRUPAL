package com.streamflow.vista;

import com.streamflow.config.ConexionSQLite;
import com.streamflow.controlador.ContenidoControlador;
import com.streamflow.controlador.SuscripcionControlador;
import com.streamflow.controlador.UsuarioControlador;
import com.streamflow.dao.ContenidoDAO;
import com.streamflow.dao.ContenidoDAOSQLite;
import com.streamflow.dao.SuscripcionDAO;
import com.streamflow.dao.SuscripcionDAOSQLite;
import com.streamflow.dao.UsuarioDAO;
import com.streamflow.dao.UsuarioDAOSQLite;
import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.ContenidoGenerico;
import com.streamflow.modelo.Genero;
import com.streamflow.modelo.Pelicula;
import com.streamflow.modelo.Serie;
import com.streamflow.modelo.Documental;
import com.streamflow.modelo.Suscripcion;
import com.streamflow.modelo.Usuario;
import com.streamflow.servicio.RecomendacionServicio;
import com.streamflow.servicio.SuscripcionServicio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SistemaStreamFlow {

    private static final Scanner sc = new Scanner(System.in);

    private static final ContenidoDAO contenidoDAO = new ContenidoDAOSQLite(ConexionSQLite.URL_BASE_DATOS);
    private static final SuscripcionDAO suscripcionDAO = new SuscripcionDAOSQLite(ConexionSQLite.URL_BASE_DATOS);
    private static final UsuarioDAO usuarioDAO = new UsuarioDAOSQLite(ConexionSQLite.URL_BASE_DATOS);

    private static final ContenidoControlador contenidoControlador = new ContenidoControlador(contenidoDAO);
    private static final UsuarioControlador usuarioControlador = new UsuarioControlador(usuarioDAO);
    private static final SuscripcionControlador suscripcionControlador =
            new SuscripcionControlador(new SuscripcionServicio(suscripcionDAO, usuarioDAO));
    private static final RecomendacionServicio recomendacionServicio = new RecomendacionServicio(contenidoDAO);

    private static int siguienteIdContenido = calcularSiguienteIdContenido();
    private static int siguienteIdSuscripcion = calcularSiguienteIdSuscripcion();

    private static int calcularSiguienteIdContenido() {
        int maximo = 0;
        for (Contenido contenido : contenidoDAO.listarTodos()) {
            maximo = Math.max(maximo, contenido.getId());
        }
        return maximo + 1;
    }

    private static int calcularSiguienteIdSuscripcion() {
        int maximo = 0;
        for (Suscripcion suscripcion : suscripcionDAO.listarTodas()) {
            maximo = Math.max(maximo, suscripcion.getId());
        }
        return maximo + 1;
    }

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = Integer.parseInt(sc.nextLine());
            switch (opcion) {
                case 1:
                    agregarPelicula();
                    break;
                case 2:
                    agregarSerie();
                    break;
                case 3:
                    agregarDocumental();
                    break;
                case 4:
                    agregarContenidoPersonalizado();
                    break;
                case 5:
                    listarContenido();
                    break;
                case 6:
                    recomendarPorGenero();
                    break;
                case 7:
                    registrarSuscripcion();
                    break;
                case 8:
                    eliminarSuscripcion();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("");
        System.out.println("============ StreamFlow ============");
        System.out.println("1. Agregar pelicula");
        System.out.println("2. Agregar serie");
        System.out.println("3. Agregar documental");
        System.out.println("4. Agregar nuevo tipo de contenido");
        System.out.println("5. Listar contenido");
        System.out.println("6. Recomendar por genero");
        System.out.println("7. Registrar suscripcion");
        System.out.println("8. Eliminar suscripcion");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private static void agregarPelicula() {
        System.out.print("Titulo: ");
        String titulo = sc.nextLine();
        Genero genero = pedirGenero();
        CalidadStreaming calidad = pedirCalidad();
        System.out.print("Duracion en minutos: ");
        int duracion = Integer.parseInt(sc.nextLine());
        System.out.print("Director: ");
        String director = sc.nextLine();

        Contenido pelicula = new Pelicula(siguienteIdContenido, titulo, genero, calidad, duracion, director);
        contenidoControlador.agregarContenido(pelicula);
        siguienteIdContenido++;
        System.out.println("Pelicula agregada correctamente");
    }

    private static void agregarSerie() {
        System.out.print("Titulo: ");
        String titulo = sc.nextLine();
        Genero genero = pedirGenero();
        CalidadStreaming calidad = pedirCalidad();
        System.out.print("Duracion promedio por episodio en minutos: ");
        int duracion = Integer.parseInt(sc.nextLine());
        System.out.print("Numero de temporadas: ");
        int temporadas = Integer.parseInt(sc.nextLine());
        System.out.print("Episodios por temporada: ");
        int episodios = Integer.parseInt(sc.nextLine());

        Contenido serie = new Serie(siguienteIdContenido, titulo, genero, calidad, duracion, temporadas, episodios);
        contenidoControlador.agregarContenido(serie);
        siguienteIdContenido++;
        System.out.println("Serie agregada correctamente");
    }

    private static void agregarDocumental() {
        System.out.print("Titulo: ");
        String titulo = sc.nextLine();
        Genero genero = pedirGenero();
        CalidadStreaming calidad = pedirCalidad();
        System.out.print("Duracion en minutos: ");
        int duracion = Integer.parseInt(sc.nextLine());
        System.out.print("Tema: ");
        String tema = sc.nextLine();

        Contenido documental = new Documental(siguienteIdContenido, titulo, genero, calidad, duracion, tema);
        contenidoControlador.agregarContenido(documental);
        siguienteIdContenido++;
        System.out.println("Documental agregado correctamente");
    }

    private static void agregarContenidoPersonalizado() {
        System.out.print("Titulo: ");
        String titulo = sc.nextLine();
        Genero genero = pedirGenero();
        CalidadStreaming calidad = pedirCalidadPersonalizada();
        System.out.print("Duracion en minutos: ");
        int duracion = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre del nuevo tipo de contenido (ejemplo: Podcast, AudioLibro): ");
        String tipoPersonalizado = sc.nextLine();

        Map<String, String> atributos = new LinkedHashMap<>();
        String continuar;
        do {
            System.out.print("Nombre del atributo a agregar (ejemplo: anfitrion, narrador): ");
            String nombreAtributo = sc.nextLine();
            System.out.print("Valor de " + nombreAtributo + ": ");
            String valorAtributo = sc.nextLine();
            atributos.put(nombreAtributo, valorAtributo);
            System.out.print("Desea agregar otro atributo? (s/n): ");
            continuar = sc.nextLine();
        } while (continuar.equalsIgnoreCase("s"));

        Contenido contenido = new ContenidoGenerico(siguienteIdContenido, titulo, genero, calidad, duracion, tipoPersonalizado, atributos);
        contenidoControlador.agregarContenido(contenido);
        siguienteIdContenido++;
        System.out.println(tipoPersonalizado + " agregado correctamente");
    }

    private static void listarContenido() {
        List<Contenido> lista = contenidoControlador.listarContenido();
        if (lista.isEmpty()) {
            System.out.println("No hay contenido registrado");
            return;
        }
        for (Contenido contenido : lista) {
            System.out.println(contenido.obtenerDetalles());
        }
    }

    private static void recomendarPorGenero() {
        Genero genero = pedirGenero();
        List<Contenido> recomendados = recomendacionServicio.recomendarPorGenero(genero);
        if (recomendados.isEmpty()) {
            System.out.println("No hay recomendaciones para ese genero");
            return;
        }
        for (Contenido contenido : recomendados) {
            System.out.println(recomendacionServicio.obtenerResumenReproduccion(contenido));
        }
    }

    private static void registrarSuscripcion() {
        String cedula = pedirCedula();

        if (!usuarioControlador.existeUsuario(cedula)) {
            System.out.print("Nombres: ");
            String nombres = sc.nextLine();
            System.out.print("Email (opcional, enter para omitir): ");
            String email = sc.nextLine();

            try {
                usuarioControlador.registrarUsuario(cedula, nombres, email.isBlank() ? null : email);
                System.out.println("Usuario registrado correctamente");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("No se pudo registrar el usuario: " + e.getMessage());
                return;
            }
        } else {
            System.out.println("El usuario ya estaba registrado, continuamos con la suscripcion");
        }

        CalidadStreaming calidad = pedirCalidad();
        try {
            Suscripcion suscripcion = suscripcionControlador.registrarSuscripcion(siguienteIdSuscripcion, cedula, calidad);
            siguienteIdSuscripcion++;
            System.out.println("Suscripcion creada con costo mensual de " + suscripcion.getCostoMensual());
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo crear la suscripcion: " + e.getMessage());
        }
    }

    private static void eliminarSuscripcion() {
        String cedula = pedirCedula();
        if (!usuarioControlador.existeUsuario(cedula)) {
            System.out.println("No existe un usuario registrado con esa cedula");
            return;
        }
        List<Suscripcion> suscripciones = suscripcionControlador.obtenerSuscripciones(cedula);
        if (suscripciones.isEmpty()) {
            System.out.println("Ese usuario no tiene suscripciones registradas");
            return;
        }
        System.out.println("Suscripciones del usuario:");
        for (Suscripcion suscripcion : suscripciones) {
            System.out.println("Id: " + suscripcion.getId() + " | Calidad: " + suscripcion.getCalidad()
                    + " | Costo: " + suscripcion.getCostoMensual() + " | Activa: " + suscripcion.isActiva());
        }
        System.out.print("Id de la suscripcion a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            suscripcionControlador.eliminarSuscripcion(id);
            System.out.println("Suscripcion eliminada correctamente");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo eliminar la suscripcion: " + e.getMessage());
        }
    }

    private static String pedirCedula() {
        String cedula;
        do {
            System.out.print("Cedula (10 digitos): ");
            cedula = sc.nextLine();
            if (!cedula.matches("\\d{10}")) {
                System.out.println("La cedula debe tener exactamente 10 digitos numericos");
            }
        } while (!cedula.matches("\\d{10}"));
        return cedula;
    }

    private static Genero pedirGenero() {
        int opcion;
        Genero[] generos = Genero.values();
        do {
            System.out.println("Seleccione genero:");
            for (int i = 0; i < generos.length; i++) {
                System.out.println((i + 1) + ". " + generos[i]);
            }
            opcion = Integer.parseInt(sc.nextLine());
        } while (opcion < 1 || opcion > generos.length);
        return generos[opcion - 1];
    }

    private static CalidadStreaming pedirCalidad() {
        int opcion;
        CalidadStreaming[] calidades = CalidadStreaming.values();
        do {
            System.out.println("Seleccione calidad:");
            for (int i = 0; i < calidades.length; i++) {
                System.out.println((i + 1) + ". " + calidades[i]);
            }
            opcion = Integer.parseInt(sc.nextLine());
        } while (opcion < 1 || opcion > calidades.length);
        return calidades[opcion - 1];
    }

    private static CalidadStreaming pedirCalidadPersonalizada() {
        int opcion;
        do {
            System.out.println("Seleccione la calidad:");
            System.out.println("1. Baja");
            System.out.println("2. Estandar");
            System.out.println("3. Alta");
            opcion = Integer.parseInt(sc.nextLine());
        } while (opcion < 1 || opcion > 3);
        return switch (opcion) {
            case 1 -> CalidadStreaming.SD;
            case 2 -> CalidadStreaming.HD;
            default -> CalidadStreaming.UHD_4K;
        };
    }
}
