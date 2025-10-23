<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">


<head>
    <meta charset="UTF-8">
    <title>Biblioteca</title>
    <style>
        /* Estilo base de los enlaces */
        a {
            margin: 10px;
            display: inline-block;
            color: black;              /* Color negro */
            font-size: 20px;           /* Tamaño más grande */
            text-decoration: none;     /* Sin subrayado */
        }

        /* Estilo cuando el mouse pasa sobre el enlace */
        a:hover {
            text-decoration: underline;
            font-size: 25px;            /* Se hace un poco más grande */
        }

        body {
            margin: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            background: url('https://images.pexels.com/photos/8834282/pexels-photo-8834282.jpeg')
                        center/cover no-repeat fixed;
        }

        form {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        input {
            margin: 8px;
        }
    </style>
</head>
<body>
    <h1>BUSQUEDA</h1>
    <h2>Campos</h2>
    <form id="formulario" action="/BibliotecaServlet/WebFoundServlet" method="GET">
        <label>Título del libro:</label>
        <input type="text" name="titulo" placeholder="Titulo del libro"><br>

        <label>Autor del libro:</label>
        <input type="text" name="autor" placeholder="Autor del libro"><br>

        <input type="submit" value="Buscar"><br>
    </form>

    <a href="index.jsp"> ➤ Inicio</a><br>
</body>
</html>
