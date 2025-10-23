<!DOCTYPE html>
<html lang="es">
<head>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <meta charset="UTF-8">
    <title>Biblioteca</title>
    <style>
        /* Estilo base de los enlaces */
        a {
            color: black;              /* Color negro */
            text-decoration: none;     /* Sin subrayado */
            font-size: 50px;           /* Tamaño más grande */
        }

        /* Estilo cuando el mouse pasa sobre el enlace */
        a:hover {
            text-decoration: underline; /* Se subraya al pasar el mouse */
            font-size: 60px;            /* Se hace un poco más grande */
        }

        body {
            margin: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            background: url('https://images.pexels.com/photos/8830977/pexels-photo-8830977.jpeg') center/cover no-repeat fixed;
        }
    </style>
</head>
<body>
    <a href="WebSearch.jsp">Buscar libros</a><br>
    <a href="WebAdd.jsp">Añadir libros</a><br>
    <a href="WebLend.jsp">Prestación libros</a><br>
     <a href="WebDelete.jsp">Eliminar libros</a><br>
</body>
</html>
