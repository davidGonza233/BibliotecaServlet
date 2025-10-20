package com.serbatic.BibliotecaServlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import com.google.gson.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static com.serbatic.BibliotecaServlet.utils.*;


public class Main {
    public static void main(String[] args) {
        String persistencePath = "C:\\Users\\Formacion\\IdeaProjects\\BibliotecaServlet\\src\\resources\\persistence.json";
        Set<Book> books = new HashSet<>();
        Scanner sc = new Scanner(System.in);
        Library myLibrary = new Library(books);

        // Crea un hilo que detecta cuando se cierra el programa

            //actualizar el fichero con gson
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println(red + "El programa se está cerrando..." + reset);
                try (Writer writer = new FileWriter(persistencePath)) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(myLibrary.catalog, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));




        // Leer archivo
        try (Reader reader = new FileReader(persistencePath)) {
            Gson gson = new GsonBuilder().create();

            // Parseamos el JSON a un JsonArray
            JsonArray booksArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : booksArray) {
                JsonObject b = element.getAsJsonObject();

                // Llamamos a addBookBegin con los valores del JsonObject
                myLibrary.addBookBegin(
                        b.get("title").getAsString(),
                        b.get("author").getAsString(),
                        b.get("isbn").getAsString(),
                        b.get("publicationDate").getAsString(), // string "dd/MM/yyyy"
                        b.get("amount").getAsInt()
                );
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo JSON no encontrado.");
        } catch (IOException e) {
            e.printStackTrace();
        }



        // Menu
        String option, optionSearch;
        boolean search = true, wasAdded = false, wasDeleted = false;
        do {
            System.out.println(shinyYellow + "\n<----BIBLIOTECA---->" + reset);
            System.out.println("¿Que quieres realizar?");
            System.out.println(blue + " Listar los libros (L)");
            System.out.println(green + " Añadir libros (A)");
            System.out.println(magent + " Buscar libros (B)");
            System.out.println(cyan + " Eliminar libro (E)");
            System.out.println(white + " Modificar libro (M)");
            System.out.println(yellow + " Prestar libro (P)" + reset);
            System.out.println(yellow + " Devolver libro (D)" + reset);
            System.out.println("Salir (s):");
            option = sc.nextLine().toLowerCase();

            switch (option) {
                case "s":
                    System.out.println("Adios");
                    break;
                case "l":
                    // Listar libros
                    System.out.println(shinyBlue + "\nLISTA DE LIBROS" + reset);
                    myLibrary.listBooks();
                    break;
                case "a":
                    //Añadir Libros pidiendo cada dtos y se comprubea al final en caso de que ya exista teniendo en cuenta el isbn
                    myLibrary.addBook();

                    break;
                case "b":
                    //Busqueda y dentro de esta hay un menu donde eligues pporque parametros buscaras los libros
                    do {
                        System.out.println(shinyMagent + "\nBUSCAR LIBROS" + reset);
                        System.out.println("Selecciona por que quires buscar el libro");
                        System.out.println(magent + "-Titulo (T)");
                        System.out.println("-Autor (A)" + reset);
                        System.out.println("Atras (s):");
                        optionSearch = sc.next().toLowerCase();
                        sc.nextLine();
                        switch (optionSearch) {
                            case "t":
                                System.out.println("Introduce el titulo a buscar :");
                                String titulo = sc.nextLine();
                                System.out.println(magent + " Resultados" + reset);
                                myLibrary.searchTitle(titulo);

                                break;
                            case "a":
                                System.out.println("Introduce el autor a buscar :");
                                String autor = sc.nextLine();
                                System.out.println(magent + " Resultados" + reset);
                                myLibrary.searchAuthor(autor);

                                break;
                            case "s":
                                search = false;
                                System.out.println("Saliendo de busqueda");
                                break;
                            default:
                                System.out.println("Opción invalida, reintentalo porfavor");
                                break;

                        }
                    } while (search);
                    break;
                case "e":
                    // Eliminar libro segun el isbn
                    do {
                        String isbn;


                        System.out.println(shinyCyan + "\nEliminar LIBRO" + reset);
                        System.out.println("Para eleminar un libro necesitamos el ISBN:\n(Atras s)");
                        isbn = sc.nextLine();

                        wasDeleted = myLibrary.deleteBook(isbn);


                    } while (!wasDeleted);
                    break;

                case "m":

                    myLibrary.modifyBook();
                    break;

                case "p":

                    myLibrary.lendBook();
                    break;

                case "d":

                    myLibrary.returnBook();
                    break;

                default:
                    System.out.println("Opción invalida, reintentalo porfavor");
                    break;
            }
// Salida del programa
        } while (!option.equals("s"));


        sc.close();
    }
}