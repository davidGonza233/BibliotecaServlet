<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca</title>
    <style>
       body {
           margin: 0;
           height: 100vh;
           display: flex;
           flex-direction: column;
           align-items: center;
           justify-content: center;
           background: url('https://images.pexels.com/photos/8834282/pexels-photo-8834282.jpeg') center/cover no-repeat fixed;
           font-family: Arial, sans-serif;
       }



       form {
           display: flex;
           flex-direction: column;
           align-items: center;

           width: 300px; /* Ancho fijo del formulario */
       }






       .mensaje {
           background-color: white;
           color: black;
           padding: 10px 20px;
           border-radius: 5px;
           position: fixed;
           top: 20px;
           left: 50%;
           transform: translateX(-50%);
           animation: dissapear 7s forwards;
           z-index: 10;
       }

       @keyframes dissapear {
           0% { opacity: 0; }
           10% { opacity: 1; }
           90% { opacity: 1; }
           100% { opacity: 0; }
       }

       a {
           margin: 10px;
           display: inline-block;
           color: black;
           font-size: 20px;
           text-decoration: none;
           transition: all 0.3s ease;
       }

       a:hover {
           text-decoration: underline;
           font-size: 25px;
       }
        table {
                   border-collapse: collapse;
                   margin-top: 20px;
                   width: 90%;

                   text-align: left;
               }

               th, td {
               background-color: white;
                   padding: 8px 12px;
                   border: 1px solid #ddd;
               }

               th {
                   background-color: #f2f2f2;
               }

               caption {
                   caption-side: top;
                   font-weight: bold;
                   font-size: 18px;
                   margin-bottom: 10px;
               }

    </style>
</head>
<body style="margin: 0; height: 100vh; display: flex; flex-direction: column;
             align-items: center; justify-content: center;
             background: url('https://images.pexels.com/photos/8834282/pexels-photo-8834282.jpeg') center/cover no-repeat fixed;">

    <h1>Modificar LIBROS</h1>

<%
    java.time.LocalDate hoy = java.time.LocalDate.now();

    int year = hoy.getYear();
    int month = hoy.getMonthValue();
    int day = hoy.getDayOfMonth();

    // Para que el mes y día tengan formato 2 dígitos (01, 02, ...)
    String strMonth = (month < 10 ? "0" : "") + month;
    String strDay = (day < 10 ? "0" : "") + day;

    String maxDate = year + "-" + strMonth + "-" + strDay;

    request.setAttribute("maxDate", maxDate);
%>
<table>
  <caption>Libro a modificar</caption>
<tr>
                <th>Título</th>
                <th>Autor</th>
                <th>ISBN</th>
                <th>Cantidad</th>
                 <th>Fecha de publicación</th>
            </tr>
            <tr>
<td>${chosen.title}</td>
<td>${chosen.author}</td>
<td>${chosen.isbn}</td>
<td>${chosen.amount}</td>
<td>${chosen.publicationDate}</td>
 </tr>
</table>
<h2>Campos </h2>
<p>(Dejar vacios para mantener lo anterior)</p>
    <form id="formulario" action="/BibliotecaServlet/WebSearchModifyServlet" method="POST">
  <input type="hidden" name="isbn" value ="${chosen.isbn}" ><br>
        <label>Título del libro:</label>
        <input type="text" name="titulo" pattern=".*\S.*"
               title="No puede estar en blanco" placeholder="Título del libro" ><br>


        <label>Autor del libro:</label>
        <input type="text" name="autor" pattern=".*\S.*"
               title="No puede estar en blanco" placeholder="Autor del libro" ><br>

        <label>Cantidad del libro:</label>
        <input type="number" name="cantidad" min="1" max ="2147483647" placeholder="Cantidad" ><br>


        <label>Fecha de publicación del libro:</label>
        <input type="date" name="fechaPublicacion" min="1950-01-01"
               max="${maxDate}" ><br>


        <input type="submit" name="enviar" value="Guardar"><br>
    </form>

    <a href="index.jsp"> ➤ Inicio</a><br>
</body>
</html>
