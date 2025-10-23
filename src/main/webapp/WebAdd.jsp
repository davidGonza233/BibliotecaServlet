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

    </style>
</head>
<body style="margin: 0; height: 100vh; display: flex; flex-direction: column;
             align-items: center; justify-content: center;
             background: url('https://images.pexels.com/photos/8834282/pexels-photo-8834282.jpeg') center/cover no-repeat fixed;">

    <h1>AÑADIR LIBROS</h1>
    <h2>Campos</h2>
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
    <form id="formulario" action="/BibliotecaServlet/WebAddServlet" method="GET">

        <label>Título del libro:</label>
        <input type="text" name="titulo" pattern=".*\S.*"
               title="No puede estar en blanco" placeholder="Título del libro" required><br>


        <label>Autor del libro:</label>
        <input type="text" name="autor" pattern=".*\S.*"
               title="No puede estar en blanco" placeholder="Autor del libro" required><br>


        <label>ISBN del libro:</label>
        <input type="text" name="isbn"
               pattern="^97[89]\d{10}$|^97[89]-\d{1,5}-\d{1,7}-\d{1,7}-\d$"
               title="Debe empezar por 978 o 979 y tener trece dígitos, si añades guiones deben ser cuatro y estar correctamente posicionados"
               placeholder="ISBN" required><br>


        <label>Cantidad del libro:</label>
        <input type="number" name="cantidad" min="1" placeholder="Cantidad de libros" required><br>


        <label>Fecha de publicación del libro:</label>
        <input type="date" name="fechaPublicacion" min="1950-01-01"
               max="${maxDate}" required><br>


        <input type="submit" name="enviar" value="Guardar"><br>
    </form>
<c:if test="${ejecutado}">
        <div class="mensaje">¡Libro guardado correctamente!</div>

    <c:if test="${guardado}">
        <div class="mensaje">¡Libro guardado correctamente!</div>
    </c:if>

    <c:if test="${!guardado}">
        <div class="mensaje">¡Libro no guardado!</div>
    </c:if>

    <c:if test="${exist}">
        <div class="mensaje">¡Libro ya existente (ISBN repetido)!</div>
    </c:if>
  </c:if>
    <a href="index.jsp"> ➤ Inicio</a><br>
</body>
</html>
