package com.serbatic.BibliotecaServlet;

import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

@WebServlet("/WebModifyServlet")

public class WebModifyServlet extends HttpServlet {
    public void init() throws ServletException {
        super.init();
        String persistencePath = "C:\\Users\\Formacion\\IdeaProjects\\BibliotecaServlet\\src\\resources\\persistence.json";

        Library myLibrary = new Library(new HashSet<>());

        // Leer archivo JSON y cargar libros
        try (Reader reader = new FileReader(persistencePath)) {
            JsonArray booksArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : booksArray) {
                JsonObject b = element.getAsJsonObject();
                myLibrary.addBookBegin(
                        b.get("title").getAsString(),
                        b.get("author").getAsString(),
                        b.get("isbn").getAsString(),
                        b.get("publicationDate").getAsString(),
                        b.get("amount").getAsInt()
                );
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo JSON no encontrado.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Guardar en ServletContext
        getServletContext().setAttribute("library", myLibrary);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String boton = request.getParameter("modificar");

        String isbn;

        boolean guardado = false, ejecutado = false;

        Library myLibrary = (Library) getServletContext().getAttribute("library");

        Book theChosenOne = new Book();
        isbn = request.getParameter("isbn");
       // System.out.println(isbn);
        theChosenOne = myLibrary.searchIsbn(isbn).get(0);


        if (boton != null) {


            request.setAttribute("chosen", theChosenOne);

            request.getRequestDispatcher("/WebModify.jsp").forward(request, response);


        }

    }

}