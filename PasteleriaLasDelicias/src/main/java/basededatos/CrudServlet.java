/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package basededatos;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

/**
 *
 * @author nanet
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

@WebServlet(name = "CrudServlet", urlPatterns = {"/crud"})
public class CrudServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Acción recibida del formulario
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String productos = request.getParameter("productos");
        String especificaciones = request.getParameter("especificaciones");
        try (Connection conexion = Conexion.getConnection()){
        //try (Connection conexion = Conexion.getConnection()) {
            switch (action) {
                case "read": // Buscar por ID
                    buscarRegistro(conexion, id, request, response);
                    break;
                case "update": // Actualizar datos excepto el ID
                    actualizarRegistro(conexion, id, nombre, email, telefono, productos, especificaciones, response);
                    break;
                case "delete": // Eliminar registro por ID
                    eliminarRegistro(conexion, id, response);
                    break;
                default:
                    response.getWriter().println("Acción no válida.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error al realizar la operación: " + e.getMessage());
        }
    }

    private void buscarRegistro(Connection conexion, String id, HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String query = "SELECT * FROM pedidos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("nombre", rs.getString("nombre"));
                jsonResponse.addProperty("email", rs.getString("email"));
                jsonResponse.addProperty("telefono", rs.getString("telefono"));
                jsonResponse.addProperty("productos", rs.getString("productos"));
                jsonResponse.addProperty("especificaciones", rs.getString("especificaciones"));

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString()); 
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                response.getWriter().write("{\"error\":\"Registro no encontrado\"}");
            }
        }
    }


    private void actualizarRegistro(Connection conexion, String id, String nombre, String email, String telefono, String productos, String especificaciones, HttpServletResponse response) throws SQLException, IOException {
        String query = "UPDATE pedidos SET nombre = ?, email = ?, telefono = ?, productos = ?, especificaciones = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, telefono);
            stmt.setString(4, productos);
            stmt.setString(5, especificaciones);
            stmt.setString(6, id);  // El ID no se puede modificar
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                response.getWriter().println("Registro actualizado exitosamente.");
            } else {
                response.getWriter().println("No se encontró el registro con el ID especificado.");
            }
        }
    }


    private void eliminarRegistro(Connection conexion, String id, HttpServletResponse response) throws SQLException, IOException {
        String query = "DELETE FROM pedidos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, id);  // Eliminar el registro por ID
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                response.getWriter().println("Registro eliminado exitosamente.");
            } else {
                response.getWriter().println("No se encontró el registro con el ID especificado.");
            }
        }
    }

}
