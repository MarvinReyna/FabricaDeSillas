package fabrica;

import java.sql.Connection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;

public class BasedeDatos {
	private static final String URL = "jdbc:mariadb://localhost:3306/fabrica";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "1234";
	public void RegistrarCliente(Clientes cliente) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    try {
	        connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

	        // Consulta SQL para insertar un nuevo cliente en la tabla
	        String insertSql = "INSERT INTO cliente (nit, nombre, telefono) VALUES (?, ?, ?)";
	        preparedStatement = connection.prepareStatement(insertSql);
	        preparedStatement.setString(1, cliente.getNit());
	        preparedStatement.setString(2, cliente.getNombre());
	        preparedStatement.setString(3, cliente.getTelefono());

	        // Ejecutar la consulta para insertar el cliente
	        int filasAfectadas = preparedStatement.executeUpdate();

	        if (filasAfectadas > 0) {
	            System.out.println("Cliente registrado exitosamente en la base de datos.");
	        } else {
	            System.out.println("Error al registrar el cliente en la base de datos.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	public boolean existeCliente(Connection connection, String nit) throws SQLException {
	    String sql = "SELECT COUNT(*) AS count FROM cliente WHERE nit = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, nit);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            int count = resultSet.getInt("count");
	            return count > 0;
	        }
	        return false;
	    }
	}
	public void mostrarProductos(Connection connection) throws SQLException {
	    String sql = "SELECT nombre, cantidad, precio FROM producto";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
	         ResultSet resultSet = preparedStatement.executeQuery()) {
	        System.out.println("Productos en el inventario:");
	        while (resultSet.next()) {
	            String nombre = resultSet.getString("nombre");
	            int cantidad = resultSet.getInt("cantidad");
	            float precio = resultSet.getFloat("precio");
	            System.out.println("Nombre: " + nombre + ", Cantidad: " + cantidad + ", Precio: " + precio);
	        }
	    }
	}
	public boolean existeProducto(Connection connection, String nombreProducto) throws SQLException {
	    String sql = "SELECT nombre FROM producto WHERE nombre = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, nombreProducto);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        return resultSet.next(); // Retorna true si existe, false si no existe
	    }
	}
	public float calcularTotalPedido(Connection connection, String nombreProducto, int cantidad) {
	    float precioUnitario = obtenerPrecioProducto(connection, nombreProducto);
	    return precioUnitario * cantidad;
	}

	public float obtenerPrecioProducto(Connection connection, String nombreProducto) {
	    String sql = "SELECT precio FROM producto WHERE nombre = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, nombreProducto);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            return resultSet.getFloat("precio");
	        }
	        return 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	public void RegistrarPedido(Connection connection, Pedido pedido) throws SQLException {
	    String sql = "INSERT INTO pedido (numero, fecha, nit, total) VALUES (?, ?, ?, ?)";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, pedido.getNumero());
	        preparedStatement.setString(2, pedido.getFecha());
	        preparedStatement.setString(3, pedido.getNit());
	        preparedStatement.setFloat(4, pedido.getTotal());
	        preparedStatement.executeUpdate();
	        System.out.println("Pedido registrado con éxito");
	    }
	}
	public void reducirCantidadProducto(Connection connection, String nombreProducto, int cantidad) {
	    String sql = "UPDATE producto SET cantidad = cantidad - ? WHERE nombre = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, cantidad);
	        preparedStatement.setString(2, nombreProducto);
	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
