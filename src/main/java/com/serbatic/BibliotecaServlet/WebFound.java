package com.serbatic.BibliotecaServlet;

import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static com.serbatic.BibliotecaServlet.utils.red;
import static com.serbatic.BibliotecaServlet.utils.reset;

@WebServlet("/WebFound")

public class WebFound extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h1>BUSQUEDA</h1>");
        out.println("<h2>Resultados</h2>\n");

        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");

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

myLibrary.searchTitle(title);
        myLibrary.searchAuthor(author);


    }

}

