package com.serbatic.BibliotecaServlet;

import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;


@WebServlet("/WebFoundServlet")

public class WebFoundServlet extends HttpServlet {
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


        //PrintWriter out = response.getWriter();

        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");

        Library myLibrary = (Library) getServletContext().getAttribute("library");


        boolean estanBlancos = false;
        boolean hayResultados = false;
        boolean hayCoincidencias = false;
        boolean hayTitulos = false;
        boolean hayAutores = false;
        boolean hayLibros = false;


        // Crea un hilo que detecta cuando se cierra el programa


        ArrayList<Book> foundAuthorList= new ArrayList<>();
        ArrayList<Book> foundTitleList= new ArrayList<>();
        ArrayList<Book> foundList = new ArrayList<>();
        ;

        //los dos campos vacios
        // Ambos campos vacíos → mostrar todos los libros

        if ((title == null || title.isBlank()) && (author == null || author.isBlank())) {
            estanBlancos = true;
            foundList.addAll(myLibrary.listBooks());
        }
        // rellenar los dos campo de texto y comparar
        else if (!title.isBlank() && !author.isBlank()) {
            foundTitleList = myLibrary.searchTitle(title);
            foundAuthorList = myLibrary.searchAuthor(author);
            ArrayList<Book> list = new ArrayList<>();

            if (!foundAuthorList.isEmpty() || !foundTitleList.isEmpty()) {
                hayLibros = true;
            }
            // Comparacion de los dos arraylist
            for (Book t : foundTitleList) {
                for (Book a : foundAuthorList) {
                    if (Objects.equals(t, a)) { // compara contenido del array
                        list.add(t);
                    }
                }
            }
// si no esta vacia la busqueda hace esto
            if (!list.isEmpty()) {


                hayCoincidencias = true;
                request.setAttribute("coincidenciasList", list);


            } else {
                // Cuando no encuntra resultados es porque no hay coinicidencias y de alguna manera mostrar algo

                if (!foundTitleList.isEmpty()) {
                    hayTitulos = true;
                }
                if (!foundAuthorList.isEmpty()) {
                    hayAutores = true;
                }

            }

            //si el titulo no esta vacio
        } else if (!title.isBlank()) {
            foundTitleList = myLibrary.searchTitle(title);

            if (!foundTitleList.isEmpty()) {
                hayTitulos = true;
            }

            //si el autor no esta vacio
        } else if (!author.isBlank()) {
            foundAuthorList = myLibrary.searchAuthor(author);
            if (!foundAuthorList.isEmpty()) {
               hayAutores = true;
        }

    }




            // Supongamos que ya tienes estas variables calculadas
            request.setAttribute("estanBlancos", estanBlancos);
            request.setAttribute("hayResultados", hayResultados);
            request.setAttribute("hayCoincidencias", hayCoincidencias);
            request.setAttribute("hayTitulos", hayTitulos);
            request.setAttribute("hayAutores", hayAutores);
            request.setAttribute("hayLibros", hayLibros);

// También mandamos las listas
            request.setAttribute("foundAuthorList", foundAuthorList);
            request.setAttribute("foundTitleList", foundTitleList);
            request.setAttribute("foundList", foundList);


            request.getRequestDispatcher("/WebFound.jsp").forward(request, response);

        }
    }




