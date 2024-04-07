import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
            out.println("<title>Servlet ProcesarReserva</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProcesarReserva at " + request.getContextPath() + "</h1>");
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

        //VALIDACIONES
        ArrayList<String> errores = new ArrayList<>();

        if (!validarContrasena(contrasena)) {
            errores.add("Error al digitar la contraseña");
        }

        if (!validarDNI(dni)) {
            errores.add("Error al digitar el DNI");
        }

        if (!validarCelular(celular)) {
            errores.add("Error al digitar el celular");
        }

        // Validar fecha actual
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaIngresada = LocalDate.parse(fechaStr);
        if (fechaIngresada.isBefore(fechaActual)) {
            errores.add("La fecha no puede ser menor que la fecha actual");
        }

        if (!errores.isEmpty()) {
            mostrarErrores(errores, response);
            return;
        }

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
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar errores
            response.sendRedirect("formularioReservas.jsp?error=true");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para procesar reservas";
    }
    
    // Método para validar la contraseña
    private boolean validarContrasena(String contrasena) {
        // Verificar la longitud
        if (contrasena.length() <= 5) {
            return false;
        }

        // Verificar si contiene al menos una letra mayúscula, una letra minúscula y dos números
        boolean contieneMayuscula = false;
        boolean contieneMinuscula = false;
        int contadorNumeros = 0;

        for (char c : contrasena.toCharArray()) {
            if (Character.isUpperCase(c)) {
                contieneMayuscula = true;
            } else if (Character.isLowerCase(c)) {
                contieneMinuscula = true;
            } else if (Character.isDigit(c)) {
                contadorNumeros++;
            }
        }

        return contieneMayuscula && contieneMinuscula && contadorNumeros >= 2;
    }
    
    // Método para validar el DNI
    private boolean validarDNI(String dni) {
        return dni.length() == 8 && dni.matches("\\d{8}");
    }

    // Método para validar el número de celular
    private boolean validarCelular(String celular) {
        return celular.length() == 9 && celular.matches("\\d{9}");
    }

    // Método para mostrar errores
    private void mostrarErrores(ArrayList<String> errores, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("var mensaje = 'Se han encontrado los siguientes errores:\\n';");
        for (String error : errores) {
            out.println("mensaje += '- " + error + "\\n';");
        }
        out.println("alert(mensaje);");
        out.println("window.location.href = 'formularioReservas.jsp';");
        out.println("</script>");
    }
}
