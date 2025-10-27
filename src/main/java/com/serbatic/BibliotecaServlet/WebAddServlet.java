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

@WebServlet("/WebAddServlet")

public class WebAddServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//todo cuando el isbn este repetido es decir que exista hacer que los otros datos no se borren
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String boton = request.getParameter("enviar");
        String title;
        String author;
        String isbn;
        int amount;
        String publicationDate;
        boolean guardado = false, exist = false, ejecutado = false;


        if (boton != null) {
            ejecutado = true;
            title = request.getParameter("titulo").trim();
            author = request.getParameter("autor").trim();
            isbn = request.getParameter("isbn").trim();
            amount = Integer.parseInt(request.getParameter("cantidad"));
            publicationDate = request.getParameter("fechaPublicacion");// "2025-10-20"// Detecta si se pulsó enviar
            // Convertir a LocalDate
            LocalDate fecha = LocalDate.parse(publicationDate);
// Formatear a dd/MM/yyyy
            String finalDate = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));


            Library myLibrary = (Library) getServletContext().getAttribute("library");

// Verificar si el ISBN ya existe
            exist = !myLibrary.searchIsbn(isbn).isEmpty();

            if (exist) {
                // No agregar, marcar que ya existe
                request.setAttribute("exist", true);
                request.setAttribute("title", title);
                request.setAttribute("author", author);
                request.setAttribute("date", publicationDate);
                request.setAttribute("amount", String.valueOf(amount));
            } else {
                // Agregar libro
                boolean added = myLibrary.addBookBegin(title, author, isbn, finalDate, amount);
                if (added) {
                    try (Writer writer = new BufferedWriter(new FileWriter(utils.persistencePath))) {
                        JsonArray booksArray = new JsonArray();

                        //crea el objeto y los añade a la lista para luego añadirlos al json

                        for (Book book : myLibrary.catalog) {
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

                    guardado = true;
                    request.setAttribute("guardado", true);
                }
            }


        }


        request.setAttribute("guardado", guardado);
        request.setAttribute("exist", exist);
        request.setAttribute("ejecutado", ejecutado);
        request.getRequestDispatcher("/WebAdd.jsp").forward(request, response);


    }

}

