package com.serbatic.BibliotecaServlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;


@WebServlet("/WebDeleteServlet")

public class WebDeleteServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();

        Library myLibrary = new Library(new HashSet<>());

        // Leer archivo JSON y cargar libros
        try (Reader reader = new FileReader(utils.persistencePath)) {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        Library myLibrary = (Library) getServletContext().getAttribute("library");


        boolean busquedaCompleta = false, busquedaParcial = false, eliminado = false, hayResultados = false;

        String buscarBtn = request.getParameter("buscar");
        String isbn = request.getParameter("isbn");

        String botonEliminar = request.getParameter("eliminar");

        if (botonEliminar != null) {
            // Se ha clicado el botón "Prestar"
             isbn = request.getParameter("isbn");


           myLibrary.deleteBook(isbn);

            eliminado = true;
        }



        if (buscarBtn != null) {
            if (isbn.isBlank()) {
                busquedaCompleta = true;

                //muestra de incio
                ArrayList<Book> List = myLibrary.listBooks();




                //si la lista no esta vacia de campos vacios
                if (!List.isEmpty()) {
                    request.setAttribute("listCompleta", List);

                }

            } else {
                busquedaParcial = true;

                if (!isbn.isBlank() && isbn != null) {

                    ArrayList<Book> foundList = myLibrary.searchIsbn(isbn);


                    //si la lista no esta vacia de campos vacios
                    if (!foundList.isEmpty()) {
                        hayResultados = true;
                    }
                    request.setAttribute("listParcial", foundList);
                }
            }
        }
        request.setAttribute("eliminado", eliminado);

        request.setAttribute("busquedaCompleta", busquedaCompleta);
        request.setAttribute("busquedaParcial", busquedaParcial);
        request.setAttribute("hayResultados", hayResultados);
        request.getRequestDispatcher("/WebDelete.jsp").forward(request, response);
    }


}

