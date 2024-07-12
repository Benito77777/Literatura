package com.aluracursos.literatura.principal;

import com.aluracursos.literatura.model.*;
import com.aluracursos.literatura.repository.AutorRepository;
import com.aluracursos.literatura.repository.LibroRepository;
import com.aluracursos.literatura.service.ConsumoAPI;
import com.aluracursos.literatura.service.ConvierteDatos;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;
    private List<Libro> listaDeLibrosBuscados;


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository ) {
        this.libroRepositorio = libroRepository;
        this.autorRepositorio = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Mostrar libros buscados
                    3 - Buscar autor por nombre
                    4 - Mostrar autores buscados
                    5 - Buscar autores vivos en determinado año
                    6 - Buscar por idioma
                    0 - Salir""";
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    buscarPorAutor();
                    break;
                case 4:
                    mostrarAutoresBuscados();
                    break;
                case 5:
                    buscasAutoresVivos();
                    break;
                case 6:
                    buscarPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "/?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();


        if (libroBuscado.isPresent()) {
            Libro libro = new Libro(libroBuscado.get());
            libroRepositorio.save(libro);
            System.out.println("Libro Encontrado ");
            System.out.println(libroBuscado.get());

        } else {
            System.out.println("Libro no encontrado");
        }

    }

    private void mostrarLibrosBuscados() {
        listaDeLibrosBuscados = libroRepositorio.findAll();
        listaDeLibrosBuscados.stream()
                .forEach(System.out::println);
    }

    private void buscarPorAutor() {

        System.out.println("ingrese el nombre del autor que desea buscar");
        var nombreAutor = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "/?search=" + nombreAutor.replace(" ","+"));
        var datosBuscados = conversor.obtenerDatos(json, Datos.class);

        List<DatosAutor> autorBuscado = datosBuscados.resultados().stream()
                .flatMap(a-> a.autor().stream())
                .collect(Collectors.toList());
        Optional<DatosAutor> autorEncontrado = autorBuscado.stream().filter(n-> n.nombre().toUpperCase().contains(nombreAutor.toUpperCase()))
                .findFirst();
        if(autorEncontrado.isPresent()) {
            Autor autor = new Autor(autorEncontrado.get());
            autorRepositorio.save(autor);
            System.out.println("Autor encontrado");
            System.out.println(autorEncontrado.get());
        } else{
            System.out.println("Autor no encontrado");
        }
    }

    private void buscarPorIdioma() {
        System.out.println("Inglese el idioma que desea buscar \"es\" para español y \"en\" para ingles");
        var idioma = teclado.nextLine();
        List<Libro> libro = libroRepositorio.findByIdioma(idioma);
        libro.forEach(libro1 -> {
            System.out.println("Idioma: " + libro1.getIdioma() + ", Libro: " + libro1.getTitulo());
        });
    }

    private void mostrarAutoresBuscados() {
        listaDeLibrosBuscados = libroRepositorio.findAll();
        listaDeLibrosBuscados.stream().map(Libro::getAutor).forEach(System.out::println);
    }

    private void buscasAutoresVivos() {
        System.out.println("Coloque una fecha y se le mostraran los autores que estaban vivos en ese año");
        var fechaDeVida = teclado.nextInt();
        List<Autor> fechaDeAutoresVivos = autorRepositorio.findByFechaDeVida(fechaDeVida);
        fechaDeAutoresVivos.forEach(vivos -> {
            System.out.println("Nombre= " + vivos.getNombre() + ", Fecha de nacimiento= " + vivos.getFechaDeNacimiento() + ", Fecha de fallecimiento= " + vivos.getFechaDeFallecimiento());

        });

    }
}