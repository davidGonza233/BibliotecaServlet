<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca</title>
    <style>
        .mensaje {
            background-color: white;
            color: black;
            padding: 10px 20px;
            border-radius: 5px;
            position: fixed;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            /* Animación */
            animation: dissapear 10s forwards;
        }

        @keyframes dissapear {
            0% { opacity: 0; }
            10% { opacity: 1; }
            90% { opacity: 1; }
            100% { opacity: 0; }
        }

        /* Estilo base de los enlaces */
        a {
            margin: 10px;
            display: inline-block;
            color: black;
            font-size: 20px;
            text-decoration: none;
        }

        /* Estilo cuando el mouse pasa sobre el enlace */
        a:hover {
            text-decoration: underline;
            font-size: 25px;
        }

        body {
            margin: 0;
            height: 100%;
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
            margin: 20px;
            align-items: center;
        }

        input {
            margin: 8px;
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
<body>

    <!-- Mensaje de cantidad sobrepasada -->
    <c:if test="${numero!=null && !numero}">

        <div class="mensaje">¡Cantidad de libros devuelto sobrepasa el limite!  ${numero}</div>
    </c:if>


    <!-- Mensajes de préstamo o devolución -->
    <c:if test="${prestado}">
        <div class="mensaje">¡Libro prestado correctamente!</div>
    </c:if>
    <c:if test="${devuelto}">
        <div class="mensaje">¡Libro devuelto correctamente!</div>
    </c:if>


    <h1>PRÉSTAMO</h1>

    <form id="formulario" action="/BibliotecaServlet/WebLendServlet" method="GET">
        <label>ISBN del libro:</label>
        <input type="text" name="isbn"
               pattern="^97[89]\d{10}$|^97[89]-\d{1,5}-\d{1,7}-\d{1,7}-\d$"
               title="Debe empezar por 978 o 979 y tener trece dígitos, si añades guiones deben ser cuatro y estar correctamente posicionados"
               placeholder="ISBN"><br>

        <input type="submit" name="buscar" value="Buscar"><br>
    </form>


    <c:if test="${busquedaCompleta}">
        <table>
            <caption>Lista de Libros</caption>
            <tr>
                <th>Título</th>
                <th>Autor</th>
                <th>ISBN</th>
                <th>Fecha de publicación</th>
                <th>Cantidad</th>
                <th>Cantidad a prestar</th>
                <th>Cantidad a devolver</th>

            </tr>
            <c:forEach var="book" items="${listCompleta}">
                <tr>
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.isbn}</td>
                    <td>${book.publicationDate}</td>
                    <td>${book.amount}</td>
                    <td>
                    <c:if test="${book.amount>0}">
                        <form action="/BibliotecaServlet/WebLendServlet" method="GET" style=" align-items:center; ">
                            <input type="number" name="cantidadPrestamo" min="1" max="${book.amount}"
                                   placeholder="Cantidad" required style="width: 80px; text-align: center;">
                            <input type="hidden" name="isbn" value="${book.isbn}">
                            <input type="submit" name ="prestar"value="Prestar">
                        </form>
                    </c:if>
                    </td>
                    <td>

                        <form action="/BibliotecaServlet/WebLendServlet" method="GET" style=" align-items:center; ">
                            <input type="number" name="cantidadDevolucion" min="1" max="2147483647"
                                   placeholder="Cantidad" required style="width: 80px; text-align: center;">
                            <input type="hidden" name="isbn" value="${book.isbn}">
                            <input type="submit" name ="devolver"value="Devolver">
                        </form>

                    </td>
                </tr>
            </c:forEach>
        </table>

    </c:if>


    <c:if test="${busquedaParcial}">
        <c:if test="${hayResultados}">
            <table>
                <caption>Lista de Libros</caption>
                <tr>
                    <th>Título</th>
                    <th>Autor</th>
                    <th>ISBN</th>
                    <th>Fecha de publicación</th>
                    <th>Cantidad</th>
                    <th>Cantidad a prestar</th>
                                    <th>Cantidad a devolver</th>
                </tr>
                <c:forEach var="book" items="${listParcial}">
                    <tr>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.isbn}</td>
                        <td>${book.publicationDate}</td>
                        <td>${book.amount}</td>
                         <td>
                                            <c:if test="${book.amount>0}">
                                                <form action="/BibliotecaServlet/WebLendServlet" method="GET" style=" align-items:center; ">
                                                    <input type="number" name="cantidadPrestamo" min="1" max="${book.amount}"
                                                           placeholder="Cantidad" required style="width: 80px; text-align: center;">
                                                    <input type="hidden" name="isbn" value="${book.isbn}">
                                                    <input type="submit" name ="prestar"value="Prestar">
                                                </form>
                                            </c:if>
                                            </td>
                                            <td>

                                                <form action="/BibliotecaServlet/WebLendServlet" method="GET" style=" align-items:center; ">
                                                    <input type="number" name="cantidadDevolucion" min="1" max ="2147483647"
                                                           placeholder="Cantidad" required style="width: 80px; text-align: center;">
                                                    <input type="hidden" name="isbn" value="${book.isbn}">
                                                    <input type="submit" name ="devolver"value="Devolver">
                                                </form>

                                            </td>
                    </tr>
                </c:forEach>
            </table>

        </c:if>
        <c:if test="${!hayResultados}">
            <h2>Sin resultados</h2>
        </c:if>
    </c:if>


    <a href="index.jsp"> ➤ Inicio</a><br>


</body>
</html>
