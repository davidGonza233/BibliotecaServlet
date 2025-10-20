package com.serbatic.BibliotecaServlet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.serbatic.BibliotecaServlet.utils.*;


public class Book {
    private String title;
    private String author;
    private String isbn;
    private LocalDate publicationDate;
    private Integer amount;

    public Book(String titulo, String author, String isbn, LocalDate publicationDate, Integer amount) {
        this.title = titulo;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.amount = amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicationDate() {

        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {

        this.publicationDate = publicationDate;
    }

    @Override
    public String toString() {

        return title + ";" + author + ";" + isbn + ";" + amount + "; " + publicationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    }

    // Hay que sobrescribir lo metodos equals y has para que solo compare los isbn
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;             // Mismo objeto en memoria atajo
        if (obj == null) return false;            // Comparación con null
        if (getClass() != obj.getClass()) return false; // Tipos diferentes

        Book book = (Book) obj;                // Hacemos casting
        return Objects.equals(this.isbn, book.isbn); // Comparación por ISBN
    }

    // Hay que modificar el hascode porque luego el set los utiliza para saber donde estan
    // ya que este genera uno del objeto entero pero si lo modificamos para que
    // lo cree segun el isbn luego lo podremos comparar en el equals
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

}
