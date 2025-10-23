package com.serbatic.BibliotecaServlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;


import static com.serbatic.BibliotecaServlet.utils.*;


public class Library {
    public Set<Book> catalog;

    public Library(Set<Book> catalog) {
        this.catalog = catalog;
    }

    // Añade libro recibiendo y validando los datos tambien si el libro esta repetido por el isbn te permite añadir cantidad
    public boolean addBook() {
        Scanner sc = new Scanner(System.in);

        String title, author, isbn, publicationDat;
        Integer amount = 0;
        boolean titleValid = false,
                authorvalid = false,
                isbnValid = false,
                publicationDatValid = false,
                amountValid = false,
                exit = false;
        LocalDate date = null;

        System.out.println(shinyGreen + "\nAÑADIR LIBRO" + reset);
        System.out.println("Para añadir un libro necesitamos el titulo, autor, ISBN y la fecha de publicacion");
        while (!exit) {
            // Añadir titulo
            do {
                System.out.println("Dame el título (Salir s):");
                title = sc.nextLine().trim();

                if (title.equalsIgnoreCase("s")) {
                    System.out.println("Saliendo");
                    break;
                }

                if (title.isBlank()) {
                    System.out.println("    -Titulo invalido");
                } else {
                    titleValid = true;
                }
            } while (!titleValid);
            if (!titleValid) {
                break;
            }

            // Añadir autor
            do {
                System.out.println("Dame el autor (Salir s):");
                author = sc.nextLine().trim();

                if (author.equalsIgnoreCase("s")) {
                    System.out.println("Saliendo");
                    break;
                }

                if (author.isBlank()) {
                    System.out.println("    -Autor invalido");

                } else {
                    authorvalid = true;
                }
            } while (!authorvalid);
            if (!authorvalid) {
                break;
            }

            // Añadir isbn
            do {
                System.out.println("Dame el ISBN (Salir s):");
                isbn = sc.nextLine().trim();

                if (isbn.equalsIgnoreCase("s")) {
                    System.out.println("Saliendo");
                    break;
                }

                if (isbn.isBlank()) {
                    System.out.println("    -Isbn vacio");

                } else if (catalog.contains(new Book("default", "defautl", isbn, null, null))) {
                    System.out.println("Libro ya existente");
                    for (Book book : catalog) {
                        if (book.getIsbn().equalsIgnoreCase(isbn)) {
                            System.out.println(book);
                            System.out.println("¿Quieres añadir ejemplares de este libro? (s/n)");
                            String elect = sc.nextLine();
                            switch (elect.toLowerCase()) {

                                case "n":
                                    break;


                                case "s":
                                    //añdir ejemplares si esta repetido
                                    do {
                                        System.out.println("Dame la cantidad de ejemplares a añadir (Atras a):");
                                        try {
                                            amount = sc.nextInt();

                                            if (amount == 0) {
                                                System.out.println("    -Debes añadir al menos un ejemplar");
                                            } else if (amount < 0) {
                                                System.out.println("    -Cantidad inválida, no puedes añadir una cantidad negativa");
                                            } else {
                                                book.setAmount(book.getAmount() + amount);
                                                System.out.println("Añadidos " + amount + " libros, hay " + book.getAmount() + "\n");
                                                sc.nextLine();
                                                return true;
                                            }

                                        } catch (InputMismatchException e) {


                                            if (sc.nextLine().equalsIgnoreCase("a")) {

                                                break;
                                            } else {
                                                System.out.println("    -Cantidad inválida");

                                            }

                                        }
                                    } while (!amountValid);
                                    break;
                                default:
                                    System.out.println("    -Eleccion no valida");
                            }
                        }
                    }
                } else {
                    isbnValid = checkIsbn(isbn);

                }

            } while (!isbnValid);
            if (!isbnValid) {
                break;
            }

            // Añadir fecha
            do {
                System.out.println("Dame la fecha de publicación (dd/mm/yyyy) (Salir s):");
                publicationDat = sc.nextLine().trim();

                if (publicationDat.equalsIgnoreCase("s")) {
                    System.out.println("Saliendo");
                    break;
                }

                //validamos la date si me la transforma y no da error me la guarda si no sale el mensaje

                if (checkDate(publicationDat)) {
                    date = LocalDate.parse(publicationDat, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    publicationDatValid = true;
                }
            } while (!publicationDatValid);
            if (!publicationDatValid) {
                break;
            }

            // Añadir cantidad
            do {
                System.out.println("Dame la cantidad de ejemplares a añadir (Salir s):");
                try {
                    amount = sc.nextInt();
                    if (amount == 0) {
                        System.out.println("    -Debes añadir al menos un ejemplar");
                    } else if (amount < 0) {
                        System.out.println("    -Cantidad inválida, no puedes añadir una cantidad negativa");
                    } else {

                        amountValid = true;
                    }

                } catch (InputMismatchException e) {
                    System.out.println("    -Cantidad inválida");

                    if (amount.toString().equalsIgnoreCase("s")) {
                        System.out.println("Saliendo");
                        break;
                    }
                }
            } while (!amountValid);
            if (!amountValid) {
                break;
            }
            Book book = new Book(title, author, isbn, date, amount);
            catalog.add(book);
            System.out.println("Libro añadido correctamente");
            exit = true;
        }
        ;


        return true;

    }

    // Añade libro recibiendo y validando los datos para añadir los datos del archivo csv
    public boolean addBookBegin(String title, String author, String isbn, String publicationDate, int amount) {
        boolean bookComplete = true;

        if (title == null || title.trim().isEmpty()) {

            bookComplete = false;
            System.out.println("titulo mal");
        }
        if (author == null || author.trim().isEmpty()) {

            bookComplete = false;
            System.out.println("autor mal");

        }
        if (isbn == null || isbn.trim().isEmpty()) {


            bookComplete = false;
            System.out.println("isbn mal");

        } else {


            //libro ya añadido
            if (catalog.contains(new Book("default", "defautl", isbn.replaceAll("-",""), null, null))) {
                bookComplete = false;
                System.out.println("libro ya existente ");


            }
            if (!checkIsbn(isbn)) {
                bookComplete = false;
                System.out.println("isbn  mal");
            }

        }

        if (publicationDate == null || publicationDate.trim().isEmpty() || !checkDateBegin(publicationDate)) {
            bookComplete = false;
            System.out.println("fecha mal");
        }


        if (amount < 0) {
            bookComplete = false;
            System.out.println("cantidad mal");
        }

        if (bookComplete) {
            Book book = new Book(title, author, isbn, LocalDate.parse(publicationDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")), amount);

            catalog.add(book);

        } else {
            System.out.println(bookComplete);
            System.out.println(
                    "   -Libro no añadido:\n" +
                            "Título: " + title + "\n" +
                            "Autor: " + author + "\n" +
                            "ISBN: " + isbn + "\n" +
                            "Fecha de publicación: " + publicationDate + "\n" +
                            "Cantidad: " + amount
            );
        }

        return bookComplete;
    }

    // Borra libro segun el isbn
    public void deleteBook(String isbnFound) {

        Book bookDelete = new Book();
        for (Book book : catalog) {
            if (book.getIsbn().equals(isbnFound.trim())) {
                bookDelete = book;
            }
        }
        catalog.remove(bookDelete);
saveLibraryToJson();

    }

    // Muestra todos los libros con toString
    public ArrayList<Book> listBooks() {

        ArrayList<Book> list = new ArrayList<>();
        for (Book book : catalog) {

            list.add(book);


        }
        return list;
    }

    // Busqueda por titulo
    public ArrayList<Book> searchTitle(String titleFound) {


        ArrayList<Book> list = new ArrayList<>();

        for (Book book : catalog) {


            if (Normalizer.normalize(book.getTitle().toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .contains(Normalizer.normalize(titleFound.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").trim())) {
                list.add(book);
            }

        }
        return list;
    }

    // Busqueda por Autor
    public ArrayList<Book> searchAuthor(String authorFound) {


        ArrayList<Book> list = new ArrayList<>();

        for (Book book : catalog) {


            if (Normalizer.normalize(book.getAuthor().toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .contains(Normalizer.normalize(authorFound.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").trim())) {

                list.add(book);
            }

        }
        return list;


    }

    // Busqueda por isbn
    public ArrayList<Book> searchIsbn(String isbnFound) {


        ArrayList<Book> list = new ArrayList<>();

        for (Book book : catalog) {


            if (Normalizer.normalize(book.getIsbn().toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").contains(Normalizer.normalize(isbnFound.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").trim())) {

                list.add(book);
            }

        }
        return list;


    }

    //metodo para comprobar si el isbn es correcto
    public boolean checkIsbn(String isbnChek) {

        String isbnSinGuiones = isbnChek.replaceAll("-", "").trim();
        if (isbnSinGuiones.matches("^97[89]\\d{10}$")) {
            if (!isbnSinGuiones.equals(isbnChek)) {

                String regex = "^97[89]-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$";

                if (!isbnChek.matches(regex)) {
                    System.out.println("    -Introdujiste un ISBN no valido, revisa los numeros");
                    return false;
                }
                return true;
            }
        } else {
            //en este apartado si quitas el valir= false te dice si lo has metido bien pero luego te lo busca igual independientemente
            //del resultado podria servir para gestio de erres de isbn en la bd
            System.out.println("    -Introdujiste un ISBN no valido no tiene 13 digitos, revisa los numero (Debe comenzar por 978 o 979)");
            return false;


        }
        return true;
    }

    // Metodo para comprobar si la fecha es correcta
    private boolean checkDateBegin(String publicationDate) {
        try {
            LocalDate date = LocalDate.parse(publicationDate.trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));


            LocalDate earliest = LocalDate.of(1950, 1, 1); // Fecha mínima permitida
            LocalDate today = LocalDate.now();             // Fecha máxima permitida

            if (date.isBefore(earliest)) {
                return false;
            }

            if (date.isAfter(today)) {
                return false;
            }

            return true; // La fecha es válida

        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Metodo para comprobar si la fecha es correcta con feedback
    private boolean checkDate(String publicationDate) {
        try {
            LocalDate date = LocalDate.parse(publicationDate.trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));


            LocalDate earliest = LocalDate.of(1950, 1, 1); // Fecha mínima permitida
            LocalDate today = LocalDate.now();             // Fecha máxima permitida

            if (date.isBefore(earliest)) {
                System.out.println("    -Fecha inválida: no puede ser anterior a 01/01/1950");
                return false;
            }

            if (date.isAfter(today)) {
                System.out.println("    -Fecha inválida: no puede ser posterior a hoy");
                return false;
            }

            return true; // La fecha es válida

        } catch (DateTimeParseException e) {
            System.out.println("    -Fecha inválida, por favor ingresala de nuevo (dd/MM/yyyy)");
            return false;
        }
    }


    public boolean modifyBook() {
        Scanner sc = new Scanner(System.in);
        String isbnFound, password;
        //Modifcar si sabes la contraseña
        System.out.println(shinyWhite + "\nModificar LIBRO" + reset);
        System.out.println("Para modificar un libro necesitamos el ISBN y la contraseña:\n(Atras s)");


        do {
            System.out.println("Dame la contraseña (Salir s):");
            password = sc.nextLine().trim();
            if (password.equalsIgnoreCase("s")) {
                return false;
            }
            if (password.equals("F0rm4c10n")) {
                do {

                    System.out.println("Dame el ISBN (Salir s):");
                    isbnFound = sc.nextLine().trim();

                    if (isbnFound.equalsIgnoreCase("s")) {
                        System.out.println("Saliendo");
                        return false;
                    }

                    if (isbnFound.isBlank()) {
                        System.out.println("    -ISBN vacio");
                        checkIsbn(isbnFound);
                    }
                    if (catalog.contains(new Book("default", "defautl", isbnFound, null, null))) {

                        for (Book book : catalog) {
                            if (book.getIsbn().replaceAll("-", "").trim().equals(isbnFound.replaceAll("-", ""))) {
                                System.out.println("Libro encontrado");
                                String election;

                                do {
                                    System.out.println("Libro a modificar: " + book.toString());
                                    System.out.println("¿Que deseas modificar?");
                                    System.out.println("-Tiutlo (T)");
                                    System.out.println("-Autor (A)");
                                    System.out.println("-Fecha de publicacion (F)");
                                    System.out.println("Salir (s)");
                                    election = sc.nextLine().toLowerCase();
                                    switch (election) {
                                        case "s":
                                            System.out.println("Saliendo");
                                            break;

                                        case "t":
                                            System.out.println("Titulo antiguo: " + book.getTitle());
                                            System.out.println("Titulo nuevo: ");
                                            book.setTitle(sc.nextLine().trim());
                                            break;

                                        case "a":
                                            System.out.println("Autor antiguo: " + book.getAuthor());
                                            System.out.println("Autor nuevo: ");
                                            book.setAuthor(sc.nextLine().trim());
                                            break;


                                        case "f":
                                            System.out.println("Fecha antigua: " + book.getPublicationDate());
                                            System.out.println("Fecha nueva: ");
                                            String fecha = sc.nextLine().trim();
                                            if (checkDate(fecha)) {
                                                book.setPublicationDate(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                            } else {
                                                System.out.println("Fecha no valida");

                                            }

                                            break;


                                        default:
                                            System.out.println("Opcion no valida intentalo de nuevo");
                                            break;
                                    }

                                } while (!election.equals("s"));

                            }
                        }
                    } else {
                        System.out.println("Libro no encontrado");
                    }
                } while (!isbnFound.equalsIgnoreCase("s"));
            } else {
                System.out.println("Acceso denegado");
                password = "";
            }

        } while (!password.equalsIgnoreCase("s"));
        return false;
    }

    //Prestar libros elemina de cantidad
    public void lendBook(int amount, String isbn) {


        for (Book book : catalog) {
            if (book.getIsbn().replaceAll("-", "").trim().equals(isbn.replaceAll("-", ""))) {
                System.out.println("Libro seleccionado: " + book.toString());

                book.setAmount(book.getAmount() - amount);
                System.out.println("Prestados " + amount + " libros, quedan " + book.getAmount() + "\n");

                saveLibraryToJson();
            }
        }


    }

    // Devuelve libros añade a cantidad
    public void returnBook(int amount, String isbn) {


        for (Book book : catalog) {
            if (book.getIsbn().replaceAll("-", "").trim().equals(isbn.replaceAll("-", ""))) {
                System.out.println("Libro seleccionado: " + book.toString());

                book.setAmount(book.getAmount() + amount);
                System.out.println("Devueltos " + amount + " libros, quedan " + book.getAmount() + "\n");

                saveLibraryToJson();
            }
        }
    }

    public void saveLibraryToJson() {

        try (Writer writer = new BufferedWriter(new FileWriter(persistencePath))) {
            JsonArray booksArray = new JsonArray();

            //crea el objeto y los añade a la lista para luego añadirlos al json

            for (Book book : catalog) {

                JsonObject obj = new JsonObject();
                obj.addProperty("title", book.getTitle());
                obj.addProperty("author", book.getAuthor());
                obj.addProperty("isbn", book.getIsbn());
                obj.addProperty("publicationDate", book.getPublicationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                obj.addProperty("amount", book.getAmount());

                booksArray.add(obj);
            }

            // Para que quede bonito con indentación
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(booksArray, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
