/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basededatos;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author nanet
 */
@WebServlet(name = "ServletRegistroPedido", urlPatterns = {"/registro-pedido"})
public class ServletRegistroPedido extends HttpServlet {

    private static final String query = "INSERT INTO pedidos (id, nombre, email, telefono, productos, especificaciones) VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String productos = request.getParameter("productos");
        String especificaciones = request.getParameter("especificaciones");

        System.out.println(id);
        System.out.println(telefono);
        try (Connection conexion = Conexion.getConnection(); PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, nombre);
            stmt.setString(3, email);
            stmt.setString(4, telefono);
            stmt.setString(5, productos);
            stmt.setString(6, especificaciones); // Insertamos el especificaciones
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServletRegistroPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.sendRedirect("index.html");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>Esta URL está configurada para manejar solicitudes POST únicamente</h1>");
    }

}
