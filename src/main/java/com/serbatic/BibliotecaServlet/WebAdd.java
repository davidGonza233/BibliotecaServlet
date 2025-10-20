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

@WebServlet("/WebAdd")

public class WebAdd extends HttpServlet {
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
        String boton = request.getParameter("enviar");
        String title = "";
        String author = "";
        String isbn = "";
        int amount = 0;
        String publicationDate = "";

        boolean exist = false;


        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Biblioteca</title>");
        out.println("<style>\n" +
                ".mensaje {\n" +
                "    background-color: white;\n" +
                "    color: black;\n" +
                "    padding: 10px 20px;\n" +
                "    border-radius: 5px;\n" +
                "    position: fixed;\n" +
                "    top: 20px;\n" +
                "    left: 50%;\n" +
                "    transform: translateX(-50%);\n" +
                "    \n" +
                "    /* Animación */\n" +
                "    animation: dissapear 10s forwards;\n" +
                "}\n" +
                "\n" +
                "@keyframes dissapear {\n" +
                "    0% { opacity: 0; }\n" +
                "    10% { opacity: 1; }\n" +
                "    90% { opacity: 1; }\n" +
                "    100% { opacity: 0; }\n" +
                "}\n" +
                "a {\n" +
                "    margin: 10px;              /* Márgenes en todos los lados */\n" +
                "    display: inline-block;     /* Permite que los márgenes verticales funcionen */\n" +
                "    color: black;              /* Color negro */\n" +
                "    font-size: 20px;           /* Tamaño del texto */\n" +
                "    text-decoration: none;     /* Sin subrayado */\n" +
                "    transition: all 0.3s ease; /* Transición suave al hacer hover */\n" +
                "}\n" +
                "\n" +
                "/* Estilo cuando el mouse pasa sobre el enlace */\n" +
                "a:hover {\n" +
                "    text-decoration: underline; /* Se subraya al pasar el mouse */\n" +
                "    font-size: 25px;            /* Se hace un poco más grande */\n" +
                "}\n" +
                "</style>");
        out.println("</head>");
        // Body centrado vertical y horizontal, fondo completo
        out.println("<body style=\"margin: 0; height: 100vh; display: flex; flex-direction: column; " +
                "align-items: center; justify-content: center; " +
                "background: url('https://images.pexels.com/photos/8834282/pexels-photo-8834282.jpeg') center/cover no-repeat fixed;\">");

        if (boton != null) {
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
                out.println("<div class='mensaje'>¡Libro guardado correctamente!</div>");
            } else {
                if (myLibrary.catalog.contains(new Book("default", "defautl", isbn, null, null))) {

                    out.println("<div class='mensaje'>¡Libro ya existente (ISBN repetido)!</div>");
                    exist = true;

                } else {
                    out.println("<div class='mensaje'>¡Libro no guardado!</div>");

                }


            }


        }
        out.println("<h1>AÑADIR LIBROS</h1>");
        out.println("<h2>Campos</h2>\n");
        out.println("<form id=\"fomulario\" action=\"/BibliotecaServlet/WebAdd\" method=\"GET\"" +
                " style=\"display: flex; flex-direction: column; align-items: center;\">");
        //el titutlo y el autor es requerido y con ese patron  significa que debe haber al menso una caracter que no sea un espacio
        // Título
        out.println("<label>Título del libro:</label>");
        if (exist) {
            out.println("<input style='margin: 8px;' type='text' name='titulo' value='" + title +
                    "' pattern='.*\\S.*' title='No puede estar en blanco' placeholder='Título del libro' required><br>");
        } else {
            out.println("<input style='margin: 8px;' type='text' name='titulo' pattern='.*\\S.*' title='No puede estar en blanco' placeholder='Título del libro' required><br>");
        }

// Autor
        out.println("<label>Autor del libro:</label>");
        if (exist) {
            out.println("<input style='margin: 8px;' type='text' name='autor' value='" + author +
                    "' pattern='.*\\S.*' title='No puede estar en blanco' placeholder='Autor del libro' required><br>");
        } else {
            out.println("<input style='margin: 8px;' type='text' name='autor' pattern='.*\\S.*' title='No puede estar en blanco' placeholder='Autor del libro' required><br>");
        }

// ISBN
        out.println("<label>ISBN del libro:</label>");
      
        out.println("<input style='margin: 8px;' type='text' name='isbn' pattern='^97[89]\\d{10}$|^97[89]-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d$' title='Debe empezar por 978 o 979 y tener trece dígitos, si añades guiones deben ser cuatro y estar correctamente posicionados' placeholder='ISBN' required><br>");


// Cantidad
        out.println("<label>Cantidad del libro:</label>");
        if (exist) {
            out.println("<input style='margin: 8px;' type='number' name='cantidad' value='" + amount + "' min=1 placeholder='Cantidad de libros' required><br>");
        } else {
            out.println("<input style='margin: 8px;' type='number' name='cantidad' min=1 placeholder='Cantidad de libros' required><br>");
        }

// Fecha de publicación
        out.println("<label>Fecha de publicación del libro:</label>");
        if (exist) {
            out.println("<input style='margin: 8px;' type='date' name='fechaPublicacion' value='" + publicationDate + "' min='1950-01-01' max='2025-12-31' required><br>");
        } else {
            LocalDate hoy = LocalDate.now(); // Fecha actual

            int year = hoy.getYear();    // Obtiene el año
            int month = hoy.getMonthValue(); // Obtiene el mes como número (1-12)
            int day = hoy.getDayOfMonth(); // Obtiene el día del mes

            out.println("<input style='margin: 8px;' type='date' name='fechaPublicacion' min='1950-01-01' max='"+year+"-"+month+"-"+day+"' required><br>");
        }


        out.println(" <input type=\"submit\"name =\"enviar\" value=\"Guardar\"><br>");
        out.println("</form>");
        out.println("<a href=\"WebSearch\"> ➤ Buscar libros</a><br>");
        out.println("</body>");

        out.println("</html>");


    }

}

