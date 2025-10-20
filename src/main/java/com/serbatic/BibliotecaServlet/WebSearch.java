package com.serbatic.BibliotecaServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/WebSearch")

public class WebSearch extends HttpServlet {
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
        out.println(" <style>\n" +
                "        /* Estilo base de los enlaces */\n" +
                "        a {\n" +
                "               margin: 10px;  " +
                "  display: inline-block;" +
                "            color: black;              /* Color negro */\n" +
                "            font-size: 18px;           /* Tamaño más grande */\n" +
                "            text-decoration: none;     /* Sin subrayado */\n" +
                "            font-size: 20px;            /* Se hace un poco más grande */\n" +

                "        }\n" +
                "\n" +
                "        /* Estilo cuando el mouse pasa sobre el enlace */\n" +
                "        a:hover {\n" +
                "            text-decoration: underline; /* Se subraya al pasar el mouse */\n" +
                "            font-size: 25px;            /* Se hace un poco más grande */\n" +
                "        }\n" +
                "    </style>");
        out.println("</head>");
        // Body centrado vertical y horizontal, fondo completo
        out.println("<body style=\"margin: 0; height: 100vh; display: flex; flex-direction: column; " +
                "align-items: center; justify-content: center; " +
                "background: url('https://images.pexels.com/photos/8834282/pexels-photo-8834282.jpeg') center/cover no-repeat fixed;\">");


        out.println("<h1>BUSQUEDA</h1>");
        out.println("<h2>Campos</h2>\n");
        out.println("<form id=\"fomulario\" action=\"/BibliotecaServlet/WebFound\" method=\"GET\"" +
                " style=\"display: flex; flex-direction: column; align-items: center;\">");
        out.println("<label>Título del libro:</label>\n");
        out.println("<input  style='margin: 8px;'type=text name=\"titulo\" placeholder=\"Titulo del libro\"><br>");

        out.println("<label>Autor del libro:</label>\n");
        out.println("<input style='margin: 8px;' type=text name=\"autor\" placeholder=\"Autor del libro\"><br>");

        out.println(" <input type=\"submit\" value=\"Buscar\"><br>");
        out.println("</form>");
        out.println("<a href=\"WebAdd\"> ➤ Añadir libros</a><br>");
        out.println("</body>");

        out.println("</html>");






    }

}

