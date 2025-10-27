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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;


@WebServlet("/WebSearchModifyServlet")

public class WebSearchModifyServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        Library myLibrary = (Library) getServletContext().getAttribute("library");
        Book theChosenOne = new Book();

        boolean busquedaCompleta = false, busquedaParcial = false, modificado = false, hayResultados = false, ejecutado = false;

        String buscarBtn = request.getParameter("buscar");
        String enviar = request.getParameter("enviar");
        String botonModificar = request.getParameter("modificar");

        String isbn = request.getParameter("isbn");


        if (botonModificar != null) {
            ejecutado = true;
            // Se ha clicado el botón "modificar"
            isbn = request.getParameter("isbn");

            theChosenOne = myLibrary.searchIsbn(isbn).get(0);
            request.setAttribute("chosen", theChosenOne);
            request.getRequestDispatcher("/WebSearchModify.jsp").forward(request, response);

        }


        String title;
        String author;
        int amount;
        String publicationDate;


        if (enviar != null) {

            theChosenOne = myLibrary.searchIsbn(request.getParameter("isbn")).get(0);
            title = request.getParameter("titulo").trim();
            author = request.getParameter("autor").trim();
            if (request.getParameter("cantidad") != null && !request.getParameter("cantidad").isBlank()) {
                amount = Integer.parseInt(request.getParameter("cantidad"));
            } else {

                amount = 0;
            }
            publicationDate = request.getParameter("fechaPublicacion");
            String finalDate;
            // Reformatear la fecha si es necesario
            if (publicationDate != null && !publicationDate.isBlank()) {
                LocalDate fecha = LocalDate.parse(publicationDate);
                finalDate = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                myLibrary.modifyBook(title, author, isbn, amount, finalDate);
            } else {
                finalDate = theChosenOne.getPublicationDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }


           modificado= myLibrary.modifyBook(title, author, isbn, amount, finalDate);


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


        request.setAttribute("chosen", theChosenOne);
        request.setAttribute("modificado", modificado);

        request.setAttribute("ejecutado", ejecutado);
        request.setAttribute("busquedaCompleta", busquedaCompleta);
        request.setAttribute("busquedaParcial", busquedaParcial);
        request.setAttribute("hayResultados", hayResultados);
        request.getRequestDispatcher("/WebSearchModify.jsp").forward(request, response);
    }


}

