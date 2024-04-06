<!-- formularioReservas.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulario de Reservas</title>
</head>
<body>
    <h1>Formulario de Reservas</h1>
    <form action="ProcesarReserva" method="post">
        <!-- Campos del formulario -->
        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" required><br>
        <label for="dni">DNI:</label>
        <input type="text" id="dni" name="dni" required><br>
        <label for="celular">Celular:</label>
        <input type="text" id="celular" name="celular" required><br>
        <label for="correo">Correo:</label>
        <input type="email" id="correo" name="correo" required><br>
        <label for="contrasena">Contraseña:</label>
        <input type="password" id="contrasena" name="contrasena" required><br>
        <label for="nro_mesa">Número de Mesa:</label>
        <input type="number" id="nro_mesa" name="nro_mesa" required><br>
        <label for="fecha">Fecha:</label>
        <input type="date" id="fecha" name="fecha" required><br>
        <!-- Botón de envío -->
        <button type="submit">Enviar Reserva</button>
    </form>
    <%-- Mostrar mensaje de error si es necesario --%>
    <% if (request.getParameter("error") != null && request.getParameter("error").equals("true")) { %>
        <p style="color: red;">Error al procesar la reserva. Inténtalo de nuevo.</p>
    <% } %>
</body>
</html>
