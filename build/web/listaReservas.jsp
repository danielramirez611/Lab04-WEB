<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Listado de Reservas</title>
    <!-- Incluir la CDN de Bootstrap -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h1 class="mt-5">Listado de Reservas</h1>
        <table class="table table-bordered mt-3">
            <thead class="thead-dark">
                <tr>
                    <th>Nombre</th>
                    <th>DNI</th>
                    <th>Celular</th>
                    <th>Correo</th>
                    <th>Número de Mesa</th>
                    <th>Fecha</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    Connection conn = null;
                    Statement stmt = null;
                    ResultSet rs = null;

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservas_db", "root", "");

                        String sql = "SELECT * FROM reservas";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                %>
                            <tr>
                                <td><%= rs.getString("nombre") %></td>
                                <td><%= rs.getString("dni") %></td>
                                <td><%= rs.getString("celular") %></td>
                                <td><%= rs.getString("correo") %></td>
                                <td><%= rs.getInt("nro_mesa") %></td>
                                <td><%= rs.getDate("fecha") %></td>
                            </tr>
                <%
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (rs != null) rs.close();
                            if (stmt != null) stmt.close();
                            if (conn != null) conn.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                %>
            </tbody>
        </table>
            <!-- Botón para regresar al formulario -->
        <a href="formularioReservas.jsp" class="btn btn-primary">Regresar</a>
    </div>
</body>
</html>
