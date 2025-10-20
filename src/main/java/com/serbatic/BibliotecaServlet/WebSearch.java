package com.serbatic.BibliotecaServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/")

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
        out.println("<body  style='display: flex; text-align:center; flex-direction: column; align-items: center; justify-content: flex-start; padding: 20px;'>");

        out.println("<h1>BUSQUEDA</h1>");
        out.println("<h2>Campos</h2>\n");
        out.println("<form id=\"fomulario\" action=\"/BibliotecaServlet/WebFound\" method=\"GET\">");
        out.println("<input  style='margin: 8px;'type=text name=\"titulo\" placeholder=\"Titulo del libro\"><br>");
        out.println("<input style='margin: 8px;' type=text name=\"autor\" placeholder=\"Autor del libro\"><br>");
        out.println(" <input type=\"submit\" value=\"Buscar\"><br>");
        out.println("</form>");
        out.println("</body>");



        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");



    }

}

