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
          min-height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
          background: url('https://images.pexels.com/photos/8834282/pexels-photo-8834282.jpeg') center/cover no-repeat fixed;
          font-family: Arial, sans-serif;
      }

      #contenido {
          display: flex;
          flex-direction: column;
          align-items: center;   /* Centra elementos horizontalmente */
          justify-content: flex-start;
          width: auto;           /* Se adapta al contenido */
          max-width: 90%;


      }

      h1 {
          text-align: center;
          margin-bottom: 20px;
      }

      form {
          display: flex;
          flex-direction: column;
          align-items: center;
          margin-bottom: 20px;
      }

      input {
          margin: 8px;
      }

      /* Tabla centrada y ajustada al contenido */
      table {
          border-collapse: collapse;
          margin: 20px auto;       /* Centra la tabla horizontalmente */
          width: auto;             /* Ocupa solo el espacio necesario */
          text-align: center;      /* Centra el contenido de celdas */
      }

      th, td {
          background-color: white;
          padding: 8px 12px;
          border: 1px solid #ddd;
          white-space: nowrap;     /* Evita que el contenido se rompa */
      }

      th {
          background-color: #f2f2f2;
      }

      caption {
          caption-side: top;
          font-weight: bold;
          font-size: 18px;
          margin-bottom: 10px;
          text-align: center;
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
          animation: dissapear 10s forwards;
      }

      @keyframes dissapear {
          0% { opacity: 0; }
          10% { opacity: 1; }
          90% { opacity: 1; }
          100% { opacity: 0; }
      }

      a {
          margin: 10px auto;        /* Centra el enlace horizontalmente */
          display: block;
          color: black;
          font-size: 20px;
          text-decoration: none;
          text-align: center;
          transition: all 0.3s ease;
      }

      a:hover {
          text-decoration: underline;
          font-size: 25px;
      }

    </style>

    <script>
        window.onload = function() {
            const passwordCorrecta = "1234"; // Cambia esto a tu contraseña
            let acceso = sessionStorage.getItem("accesoCorrecto"); // revisa si ya se ha validado

            if(acceso === "true") {
                // Ya ha pasado la verificación, mostramos el contenido
                document.getElementById("contenido").style.display = "block";
                return;
            }

            let intentos = 3;
            acceso = false;

            while(intentos > 0 && !acceso) {
                let input = prompt("Introduce la contraseña para acceder:");

                if(input === null) {
                    // Usuario cancela
                    break;
                }

                if(input === passwordCorrecta) {
                    acceso = true;
                    sessionStorage.setItem("accesoCorrecto", "true"); // recordamos que ya pasó
                    document.getElementById("contenido").style.display = "flex";
                    break;
                } else {
                    intentos--;
                    alert("Contraseña incorrecta. Te quedan " + intentos + " intentos.");
                }
            }

            if(!acceso) {
                // Redirigir al index.jsp
                window.location.href = "index.jsp";
            }
        }
    </script>

</head>
<body>

<!-- Contenido protegido -->
<div id="contenido">

    <h1>MODIFICAR LIBROS</h1>

    <form id="formulario" action="/BibliotecaServlet/WebSearchModifyServlet" method="POST">
        <label>ISBN del libro:</label>
        <input type="text" name="isbn"
               pattern="^97[89]\d{10}$|^97[89]-\d{1,5}-\d{1,7}-\d{1,7}-\d$"
               title="Debe empezar por 978 o 979 y tener trece dígitos, si añades guiones deben ser cuatro y estar correctamente posicionados"
               placeholder="ISBN"><br>
        <input type="submit" name="buscar" value="Buscar"><br>
    </form>

    <c:if test="${modificado}">
        <div class="mensaje">¡Libro modificado correctamente!</div>
    </c:if>

    <c:if test="${busquedaCompleta}">
        <table>
            <caption>Lista de Libros</caption>
            <tr>
                <th>Título</th>
                <th>Autor</th>
                <th>ISBN</th>
                <th>Fecha de publicación</th>
                <th>Cantidad</th>

            </tr>
            <c:forEach var="book" items="${listCompleta}">
                <tr>
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.isbn}</td>
                    <td>${book.publicationDate}</td>
                    <td>${book.amount}</td>
                    <td>
                        <form action="/BibliotecaServlet/WebModifyServlet" method="POST">
                            <input type="hidden" name="isbn" value="${book.isbn}">
                            <input type="submit" name="modificar" value="Modificar">
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

                </tr>
                <c:forEach var="book" items="${listParcial}">
                    <tr>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.isbn}</td>
                        <td>${book.publicationDate}</td>
                        <td>${book.amount}</td>
                        <td>
                            <form action="/BibliotecaServlet/WebModifyServlet" method="POST">
                                <input type="hidden" name="isbn" value="${book.isbn}">
                                <input type="submit" name="modificar" value="Modificar">
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

    <c:if test="${ejecutado}">
        <c:if test="${modificado}">
            <div class="mensaje">¡Libro modificado correctamente!</div>
        </c:if>
        <c:if test="${!modificado}">
            <div class="mensaje">¡Libro no modificado!</div>
        </c:if>
    </c:if>

    <a href="index.jsp"> ➤ Inicio</a><br>

</div>

</body>
</html>
