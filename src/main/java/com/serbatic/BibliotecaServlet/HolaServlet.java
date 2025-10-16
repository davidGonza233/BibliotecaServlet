package com.serbatic.BibliotecaServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hola")
public class HolaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charser=UTL-8");
        PrintWriter out = response.getWriter();
        out.println("<h1>Hola Mundo desde el Servlet :)</h1>");
        out.println("<p><a href=\"adios\">Despedida HolaServlet</a></p>\n");
    }
}
