package com.serbatic.BibliotecaServlet;

import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;


@WebServlet("/WebFound")

public class WebFound extends HttpServlet {
    @Override
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Biblioteca</title>");
        out.println("<body>");
        out.println("<h1>BUSQUEDA</h1>");


        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");

        Library myLibrary = (Library) getServletContext().getAttribute("library");

        String persistencePath = "C:\\Users\\Formacion\\IdeaProjects\\BibliotecaServlet\\src\\resources\\persistence.json";

        Scanner sc = new Scanner(System.in);


        // Crea un hilo que detecta cuando se cierra el programa


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
        ArrayList<String[]> foundAuthorList;
        ArrayList<String[]> foundTitleList;

        //los dos campos vacios
        if (title.isBlank() && author.isBlank()) {

            ArrayList<String[]> foundList = myLibrary.listBooks();
            //si la lista no esta vacia de campos vacios
            if (!foundList.isEmpty()) {
                out.println("<h2>Resultados</h2>\n");
                // Crear la tabla y el encabezado con bordes sólidos y padding
                out.println("<table style='border-collapse: collapse; width: 100%; border: 1px solid black;'>");
                out.println(" <caption style=\"caption-side: top; font-weight: bold; font-size: 18px; margin-bottom: 10px;\">\n" +
                        "        Lista de Libros\n" +
                        "    </caption>");
                out.println(InsertTableHeading());

// Recorrer la lista de resultados

                printData(out, foundList);

            } else {
                out.println("<h2>Sin resultados</h2>\n");

            }
            //rellenar los dos campo de texto y comparar
        } else if (!title.isBlank() && !author.isBlank()) {
            foundTitleList = myLibrary.searchTitle(title);
            foundAuthorList = myLibrary.searchAuthor(author);
            ArrayList<String[]> list = new ArrayList<>();


            // Comparacion de los dos arraylist
            for (String[] t : foundTitleList) {
                for (String[] a : foundAuthorList) {
                    if (Arrays.equals(t, a)) { // compara contenido del array
                        list.add(t);
                    }
                }
            }
// si no esta vacia la busqueda hace esto
            if (!list.isEmpty()) {
                out.println("<h2>Resultados</h2>\n");
                // Crear la tabla y el encabezado con bordes sólidos y padding
                out.println("<table style='border-collapse: collapse; width: 100%; border: 1px solid black;'>");
                out.println(" <caption style=\"caption-side: top; font-weight: bold; font-size: 18px; margin-bottom: 10px;\">\n" +
                        "        Lista de Libros de autor y libro\n" +
                        "    </caption>");
                out.println(InsertTableHeading());

// Recorrer la lista de resultados

                printData(out, list);

            } else {
                // Cuando no encuntra resultados es porque no hay coinicidencias y de alguna manera mostrar algo
                out.println("<h2>Sin resultados</h2>\n");
                out.println("<h2>Libros encontrados por busquedas separadas</h2>\n");
                if (!foundTitleList.isEmpty()) {
                    out.println("<h2>Resultados por busqueda de titulo</h2>\n");
                    // Crear la tabla y el encabezado con bordes sólidos y padding
                    out.println("<table style='border-collapse: collapse; width: 100%; border: 1px solid black;'>");
                    out.println(" <caption style=\"caption-side: top; font-weight: bold; font-size: 18px; margin-bottom: 10px;\">\n" +
                            "        Lista de Libros por titulo\n" +
                            "    </caption>");
                    out.println(InsertTableHeading());

// Recorrer la lista de resultados
                    printData(out, foundTitleList);

                }
                if (!foundAuthorList.isEmpty()) {
                    out.println("<h2>Resultados encontrados por busqueda de autor</h2>\n");
                    // Crear la tabla y el encabezado con bordes sólidos y padding
                    out.println("<table style='border-collapse: collapse; width: 100%; border: 1px solid black;'>");
                    out.println(" <caption style=\"caption-side: top; font-weight: bold; font-size: 18px; margin-bottom: 10px;\">\n" +
                            "        Lista de Libros por autor\n" +
                            "    </caption>");
                    out.println(InsertTableHeading());

// Recorrer la lista de resultados
                    printData(out, foundAuthorList);

                }

            }
            //si el titulo no esta vacio
        } else if (!title.isBlank()) {
            foundTitleList = myLibrary.searchTitle(title);
            System.out.println("Titulo : " + title);
            if (!foundTitleList.isEmpty()) {
                out.println("<h2>Resultados</h2>\n");
                // Crear la tabla y el encabezado con bordes sólidos y padding
                out.println("<table style='border-collapse: collapse; width: 100%; border: 1px solid black;'>");
                out.println(" <caption style=\"caption-side: top; font-weight: bold; font-size: 18px; margin-bottom: 10px;\">\n" +
                        "        Lista de Libros por titulo\n" +
                        "    </caption>");
                out.println(InsertTableHeading());

// Recorrer la lista de resultados

                printData(out, foundTitleList);

            } else {
                out.println("<h2>Sin resultados</h2>\n");


            }
            //si el autor no esta vacio
        } else if (!author.isBlank()) {
            foundAuthorList = myLibrary.searchAuthor(author);
            if (!foundAuthorList.isEmpty()) {
                out.println("<h2>Resultados</h2>\n");
                // Crear la tabla y el encabezado con bordes sólidos y padding
                out.println("<table style='border-collapse: collapse; width: 100%; border: 1px solid black;'>");
                out.println(" <caption style=\"caption-side: top; font-weight: bold; font-size: 18px; margin-bottom: 10px;\">\n" +
                        "        Lista de Libros por autor\n" +
                        "    </caption>");
                out.println(InsertTableHeading());

// Recorrer la lista de resultados

                printData(out, foundAuthorList);

            } else {
                out.println("<h2>Sin resultados</h2>\n");

            }
        }

        out.println("</body>");
        out.println("</html>");

    }


    public static String InsertTableHeading() {

        String textHeading =
                // Crear la tabla y el encabezado con bordes sólidos y padding
                "<tr style='background-color: #f2f2f2;'>" +
                        "<th style='border: 1px solid black; padding: 10px;'>Título</th>" +
                        "<th style='border: 1px solid black; padding: 10px;'>Autor</th>" +
                        "<th style='border: 1px solid black; padding: 10px;'>ISBN</th>" +
                        "<th style='border: 1px solid black; padding: 10px;'>Cantidad</th>" +
                        "<th style='border: 1px solid black; padding: 10px;'>Fecha de publicación</th>" +
                        "</tr>";

        return textHeading;
    }

    private void printData(PrintWriter out, ArrayList<String[]> list) {
        for (String[] data : list) {
            out.println("<tr>");
            for (String dat : data) {
                out.println("<td style='border: 1px solid black; padding: 10px;'>" + dat + "</td>");
            }
            out.println("</tr>");
        }

        out.println("</table>");
    }
}

