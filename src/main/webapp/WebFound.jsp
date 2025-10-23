<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca</title>
    <style>
        a {
            margin: 10px;
            display: inline-block;
            color: black;
            font-size: 20px;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
            font-size: 25px;
        }

        body {
            background-color: #EFECDE;
            font-family: Arial, sans-serif;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 15px;
        }

        th, td {
            border: 1px solid black;
            padding: 10px;
        }

        th {
            background-color: #f2f2f2;
        }

        td {
            background-color: white;
        }
    </style>
</head>
<body>

<c:choose>

    <c:when test="${estanBlancos}">
        <h2>Resultados</h2><br>
        <table>
            <tr>
                <th>Título</th>
                <th>Autor</th>
                <th>ISBN</th>
                <th>Fecha de publicación</th>
                <th>Cantidad</th>
            </tr>
            <c:forEach var="book" items="${foundList}">
                <tr>
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.isbn}</td>
                    <td>${book.publicationDate}</td>
                    <td>${book.amount}</td>
                </tr>
            </c:forEach>
        </table>
    </c:when>

    <c:when test="${hayCoincidencias}">
        <h2>Resultados</h2>
        <table>
            <tr>
                <th>Título</th>
                <th>Autor</th>
                <th>ISBN</th>
                <th>Fecha de publicación</th>
                <th>Cantidad</th>
            </tr>
            <c:forEach var="book" items="${coincidenciasList}">
                <tr>
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.isbn}</td>
                    <td>${book.publicationDate}</td>
                    <td>${book.amount}</td>
                </tr>
            </c:forEach>
        </table>

    </c:when>
     <c:when test="${hayLibros}">
                <c:if test="${hayTitulos or hayAutores}">
                <h2>Sin resultados</h2>
                    <h2>Libros encontrados por busquedas separadas</h2>

                    <c:if test="${hayAutores}">
                        <h2>Resultados por autor</h2>
                        <table>
                            <tr>
                                <th>Título</th>
                                <th>Autor</th>
                                <th>ISBN</th>
                                <th>Fecha de publicación</th>
                                <th>Cantidad</th>
                            </tr>
                            <c:forEach var="book" items="${foundAuthorList}">
                                <tr>
                                    <td>${book.title}</td>
                                    <td>${book.author}</td>
                                    <td>${book.isbn}</td>
                                    <td>${book.publicationDate}</td>
                                    <td>${book.amount}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>

                    <c:if test="${hayTitulos}">
                        <h2>Resultados por titulo</h2>
                        <table>
                            <tr>
                                <th>Título</th>
                                <th>Autor</th>
                                <th>ISBN</th>
                                <th>Fecha de publicación</th>
                                <th>Cantidad</th>
                            </tr>
                            <c:forEach var="book" items="${foundTitleList}">
                                <tr>
                                    <td>${book.title}</td>
                                    <td>${book.author}</td>
                                    <td>${book.isbn}</td>
                                    <td>${book.publicationDate}</td>
                                    <td>${book.amount}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>

                </c:if>
            </c:when>
            <c:when test="${hayAutores}">
                                    <h2>Resultados por autor</h2>
                                    <table>
                                        <tr>
                                            <th>Título</th>
                                            <th>Autor</th>
                                            <th>ISBN</th>
                                            <th>Fecha de publicación</th>
                                            <th>Cantidad</th>
                                        </tr>
                                        <c:forEach var="book" items="${foundAuthorList}">
                                            <tr>
                                                <td>${book.title}</td>
                                                <td>${book.author}</td>
                                                <td>${book.isbn}</td>
                                                <td>${book.publicationDate}</td>
                                                <td>${book.amount}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </c:when>
                     <c:when test="${hayTitulos}">
                                             <h2>Resultados por titulo</h2>
                                             <table>
                                                 <tr>
                                                     <th>Título</th>
                                                     <th>Autor</th>
                                                     <th>ISBN</th>
                                                     <th>Fecha de publicación</th>
                                                     <th>Cantidad</th>
                                                 </tr>
                                                 <c:forEach var="book" items="${foundTitleList}">
                                                     <tr>
                                                         <td>${book.title}</td>
                                                         <td>${book.author}</td>
                                                         <td>${book.isbn}</td>
                                                         <td>${book.publicationDate}</td>
                                                         <td>${book.amount}</td>
                                                     </tr>
                                                 </c:forEach>
                                             </table>


                                     </c:when>

    <c:otherwise>
        <h2>No se encontraron resultados</h2>
    </c:otherwise>

</c:choose>

<a href="index.jsp"> ➤ Incicio</a><br>


</body>
</html>
