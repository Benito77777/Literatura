package com.aluracursos.literatura.model;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idioma;
    private Double numeroDeDescargas;

    public Libro(DatosLibros datosLibros){
        this.titulo = datosLibros.titulo();
        this.autor = datosLibros.autor().toString();
        this.idioma = String.join(",", datosLibros.idioma());
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }

    public Libro() {

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(List<DatosAutor> autor) {
        this.autor = autor.toString();
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return
                "titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", idiomas=" + idioma +
                ", numeroDeDescargas=" + numeroDeDescargas +
                '}';
    }

}
