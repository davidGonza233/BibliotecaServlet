package com.serbatic.BibliotecaServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/")

public class Main extends HttpServlet {
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
                "            color: black;              /* Color negro */\n" +
                "            font-size: 18px;           /* Tamaño más grande */\n" +
                "            text-decoration: none;     /* Sin subrayado */\n" +
                "            font-size: 50px;            /* Se hace un poco más grande */\n" +

                "        }\n" +
                "\n" +
                "        /* Estilo cuando el mouse pasa sobre el enlace */\n" +
                "        a:hover {\n" +
                "            text-decoration: underline; /* Se subraya al pasar el mouse */\n" +
                "            font-size: 60px;            /* Se hace un poco más grande */\n" +
                "        }\n" +
                "    </style>");
        out.println("</head>");
        // Body centrado vertical y horizontal, fondo completo
        out.println("<body style=\"margin: 0; height: 100vh; display: flex; flex-direction: column; " +
                "align-items: center; justify-content: center; " +
                "background: url('https://images.pexels.com/photos/8830977/pexels-photo-8830977.jpeg') center/cover no-repeat fixed;\">");




        out.println("<a href=\"WebSearch\">Buscar libros</a><br>");
        out.println("<a href=\"WebAdd\">Añadir libros</a><br>");


        out.println("</body>");
        out.println("</html>");






    }

}

