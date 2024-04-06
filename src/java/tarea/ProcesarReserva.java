/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package tarea;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProcesarReserva", urlPatterns = {"/ProcesarReserva"})
public class ProcesarReserva extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Procesando Reserva</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Procesando Reserva...</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // Obtener parámetros del formulario
        String nombre = request.getParameter("nombre");
        String dni = request.getParameter("dni");
        String celular = request.getParameter("celular");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        String nroMesaStr = request.getParameter("nro_mesa");
        String fechaStr = request.getParameter("fecha");

        // Conectar a la base de datos y guardar la reserva
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservas_db", "root", "");

            String sql = "INSERT INTO reservas (nombre, dni, celular, correo, contrasena, nro_mesa, fecha) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, dni);
            pstmt.setString(3, celular);
            pstmt.setString(4, correo);
            pstmt.setString(5, contrasena);
            pstmt.setInt(6, Integer.parseInt(nroMesaStr));
            pstmt.setDate(7, Date.valueOf(fechaStr));

            pstmt.executeUpdate();

            // Redirigir a una página de confirmación
            response.sendRedirect("confirmacionReserva.jsp");
        } catch (IOException | ClassNotFoundException | NumberFormatException | SQLException e) {
            // Manejar errores
            response.sendRedirect("formularioReservas.jsp?error=true");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para procesar reservas";
    }
}
