package com.streamflow.vista;

import com.streamflow.controlador.ContenidoControlador;
import com.streamflow.controlador.SuscripcionControlador;
import com.streamflow.dao.ContenidoDAO;
import com.streamflow.dao.ContenidoDAOMemoria;
import com.streamflow.dao.SuscripcionDAO;
import com.streamflow.dao.SuscripcionDAOMemoria;
import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;
import com.streamflow.modelo.Pelicula;
import com.streamflow.modelo.Serie;
import com.streamflow.modelo.Documental;
import com.streamflow.modelo.Suscripcion;
import com.streamflow.servicio.RecomendacionServicio;
import com.streamflow.servicio.SuscripcionServicio;

import java.util.List;
import java.util.Scanner;

public class SistemaStreamFlow {

    private static final Scanner sc = new Scanner(System.in);

    private static final ContenidoDAO contenidoDAO = new ContenidoDAOMemoria();
    private static final SuscripcionDAO suscripcionDAO = new SuscripcionDAOMemoria();

    private static final ContenidoControlador ContenidoControlador = new ContenidoControlador(contenidoDAO);
    private static final SuscripcionControlador SuscripcionControlador =
            new SuscripcionControlador(new SuscripcionServicio(suscripcionDAO));
    private static final RecomendacionServicio recomendacionServicio = new RecomendacionServicio(contenidoDAO);

    private static int siguienteIdContenido = 1;
    private static int siguienteIdSuscripcion = 1;

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
                    listarContenido();
                    break;
                case 5:
                    recomendarPorGenero();
                    break;
                case 6:
                    crearSuscripcion();
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
        System.out.println("=== StreamFlow ===");
        System.out.println("1. Agregar pelicula");
        System.out.println("2. Agregar serie");
        System.out.println("3. Agregar documental");
        System.out.println("4. Listar contenido");
        System.out.println("5. Recomendar por genero");
        System.out.println("6. Crear suscripcion");
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
        ContenidoControlador.agregarContenido(pelicula);
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
        ContenidoControlador.agregarContenido(serie);
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
        ContenidoControlador.agregarContenido(documental);
        siguienteIdContenido++;
        System.out.println("Documental agregado correctamente");
    }

    private static void listarContenido() {
        List<Contenido> lista = ContenidoControlador.listarContenido();
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

    private static void crearSuscripcion() {
        System.out.print("Id de usuario: ");
        int usuarioId = Integer.parseInt(sc.nextLine());
        CalidadStreaming calidad = pedirCalidad();
        Suscripcion suscripcion = SuscripcionControlador.registrarSuscripcion(siguienteIdSuscripcion, usuarioId, calidad);
        siguienteIdSuscripcion++;
        System.out.println("Suscripcion creada con costo mensual de " + suscripcion.getCostoMensual());
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
}
