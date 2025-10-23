package com.serbatic.BibliotecaServlet;

import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

import java.util.*;


@WebServlet("/WebLendServlet")

public class WebLendServlet extends HttpServlet {
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


        boolean busquedaCompleta = false, busquedaParcial = false, prestado = false, hayResultados = false,devuelto = false;

        String buscarBtn = request.getParameter("buscar");
        String isbn = request.getParameter("isbn");

        String botonPrestar = request.getParameter("prestar");

        if (botonPrestar != null) {
            // Se ha clicado el botón "Prestar"
             isbn = request.getParameter("isbn");
            int cantidad = Integer.parseInt(request.getParameter("cantidadPrestamo"));

           myLibrary.lendBook(cantidad,isbn);

            prestado = true;
        }//todo hacer lo del salirse del int

        String botonDevolver = request.getParameter("devolver");

        if (botonDevolver != null) {
            // Se ha clicado el botón "Prestar"
            isbn = request.getParameter("isbn");
            int cantidad = Integer.parseInt(request.getParameter("cantidadDevolucion"));

            myLibrary.returnBook(cantidad,isbn);

            devuelto = true;
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
        request.setAttribute("prestado", prestado);
        request.setAttribute("devuelto", devuelto);
        request.setAttribute("busquedaCompleta", busquedaCompleta);
        request.setAttribute("busquedaParcial", busquedaParcial);
        request.setAttribute("hayResultados", hayResultados);
        request.getRequestDispatcher("/WebLend.jsp").forward(request, response);
    }


}

