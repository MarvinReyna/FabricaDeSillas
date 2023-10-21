package fabrica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FabricaDeSillas {

    public static void main(String[] args) {
        Connection connection = null;
        String url = "jdbc:mariadb://localhost:3306/fabrica";
        String user = "root";
        String pwd = "1234";
        
        BasedeDatos base = new BasedeDatos();
        
        try {
            connection = DriverManager.getConnection(url, user, pwd);

            Scanner scanner = new Scanner(System.in);
            int opcion;
            do {
                System.out.println("Menu:");
                System.out.println("1. Registro de Materiales");
                System.out.println("2. Inventario Materiales");
                System.out.println("3. Fabricar Productos");
                System.out.println("4. Inventario de Productos");
                System.out.println("5. Clientes");
                System.out.println("6. Pedidos");
                System.out.println("7. Factura");
                System.out.println("8. Salir");
                System.out.print("Seleccione una opcion: ");
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                case 1:
                    System.out.println("Ingrese el nombre del material");
                    String nombre = scanner.nextLine();
                    System.out.println("Ingrese cantidad de material");
                    int cantidad = scanner.nextInt();
                    Material material = new Material(nombre, cantidad);
                    insertarMaterial(connection, material);
                    break;
                case 2:
                    //System.out.println("Listado de Materiales:");
                    mostrarMateriales(connection);
                    System.out.println("\nPresione una tecla para continuar");
                    scanner.nextLine();
                    break;

                    
                case 3:
                    System.out.println("Ingrese el nombre del Producto");
                    String Pnombre = scanner.nextLine();
                    System.out.println("Ingrese cantidad de Producto");
                    int Pcantidad = scanner.nextInt();
                    System.out.println("Ingrese Precio de Producto");
                    float Pprecio = scanner.nextFloat();
                    scanner.nextLine();

                    List<Material> materialesRequeridos = new ArrayList<>();
                    boolean agregarMaterial = true;

                    while (agregarMaterial) {
                        System.out.println("Ingrese el nombre del material requerido");
                        String nombreMaterial = scanner.nextLine();
                        System.out.println("Ingrese la cantidad de este material requerido");
                        int cantidadMaterial = scanner.nextInt();
                        scanner.nextLine();

                        Material materialRequerido = new Material(nombreMaterial, cantidadMaterial);
                        materialesRequeridos.add(materialRequerido);

                        System.out.print("¿Desea agregar otro material? (S/s = si, N/n = no): ");
                        char respuesta = scanner.next().charAt(0);
                        scanner.nextLine();
                        if (respuesta == 'N' || respuesta == 'n') {
                            agregarMaterial = false;
                        }
                    }

                    // Verificar si se tienen suficientes materiales para fabricar el producto
                    boolean suficientesMateriales = true;
                    for (Material materialRequerido : materialesRequeridos) {
                        if (!existeMaterial(connection, materialRequerido.getNombre())) {
                            suficientesMateriales = false;
                            System.out.println("No hay suficiente " + materialRequerido.getNombre() + " en el inventario.");
                            break;
                        }
                        int cantidadMaterialesDisponibles = obtenerCantidadMaterial(connection, materialRequerido.getNombre());
                        if (cantidadMaterialesDisponibles < materialRequerido.getCantidad()) {
                            suficientesMateriales = false;
                            System.out.println("No hay suficiente " + materialRequerido.getNombre() + " en el inventario.");
                            break;
                        }
                    }

                    if (suficientesMateriales) {
                        for (Material materialRequerido : materialesRequeridos) {
                            reducirCantidadMaterial(connection, materialRequerido.getNombre(), materialRequerido.getCantidad());
                        }

                        int cantidadProducto = obtenerCantidadProducto(connection, Pnombre);
                        cantidadProducto += Pcantidad;
                        Producto producto = new Producto(Pnombre, cantidadProducto, Pprecio);
                        if (existeProducto(connection, Pnombre)) {
                            actualizarProducto(connection, producto);
                        } else {
                            insertarProducto(connection, producto);
                        }

                        System.out.println("Producto agregado");
                    }

                    	break;

                case 4:
                    //System.out.println("Listado de Productos:");
                    mostrarProductos(connection);
                    System.out.println("\nPresione una tecla para continuar");
                    scanner.nextLine();
                    
                    break;
                    
                    case 5:
                    	do {
	                        System.out.println("Ingrese el nit del cliente");
	                        String nit = scanner.nextLine();
	                        //entrada.nextLine();
	                        System.out.println("Ingrese el nombre del cliente");
	                        String nom = scanner.nextLine();
	                        System.out.println("Ingrese el numero de telefono del cliente");
	                        String tel = scanner.nextLine();

	
	                        Clientes cliente = new Clientes(nit, nom, tel);
	                        base.RegistrarCliente(cliente);
	
	                        System.out.print("¿Desea registrar otro cliente? (S/s = si, N/n = no): ");
	                        char respuesta = scanner.next().charAt(0);
	                        scanner.nextLine();
	                        if (respuesta == 'N' || respuesta == 'n') {
	                            break; // Sal del bucle si la respuesta es 'N' o 'n'
	                        }
	                    } while (true);
                    	

                    case 6:
                        System.out.println("Ingrese el número de Orden");
                        int numero = scanner.nextInt();
                        scanner.nextLine(); // Consumir la nueva línea restante

                        System.out.println("Ingrese la Fecha (YYYY-MM-DD)");
                        String fecha = scanner.nextLine();

                        System.out.println("Ingrese el Nit del cliente");
                        String nit = scanner.nextLine();

                        // Validar que el Nit se encuentre en la tabla de clientes
                        if (!base.existeCliente(connection, nit)) {
                            System.out.println("El Nit ingresado no se encuentra en la base de datos de clientes.");
                            break;
                        }

                        // Mostrar los productos almacenados en la tabla de productos
                        System.out.println("Productos disponibles en el inventario:");
                        base.mostrarProductos(connection);

                        // Solicitar el nombre y la cantidad del producto que está comprando
                        System.out.println("Ingrese el nombre del producto que está comprando");
                        String nombreProducto = scanner.nextLine();

                        if (!base.existeProducto(connection, nombreProducto)) {
                            System.out.println("El producto ingresado no se encuentra en el inventario.");
                            break;
                        }

                        System.out.println("Ingrese la cantidad del producto que está comprando");
                        int cantidadProducto = scanner.nextInt();

                        // Calcular el total del pedido
                        float totalPedido = base.calcularTotalPedido(connection, nombreProducto, cantidadProducto);

                        // Guardar en la base de datos en la tabla pedido el número de orden, fecha, Nit y total
                        Pedido pedido = new Pedido(numero, fecha, nit, totalPedido);
                        base.RegistrarPedido(connection, pedido);
                        base.reducirCantidadProducto(connection, nombreProducto, cantidadProducto);
                        System.out.println("Pedido registrado con éxito.");

                        break;

                    case 7:
                        System.out.println("Ingrese el número de orden para generar la factura:");
                        int numeroOrdenFactura = scanner.nextInt();
                        scanner.nextLine(); // Consumir la nueva línea restante

                        // Realiza una consulta para obtener los detalles del pedido
                        Pedido pedidoFactura = obtenerPedidoPorNumeroOrden(connection, numeroOrdenFactura);

                        if (pedidoFactura != null) {
                            System.out.println("Detalles del Pedido:");
                            System.out.println("Número de Orden: " + pedidoFactura.getNumero());
                            System.out.println("Fecha: " + pedidoFactura.getFecha());
                            System.out.println("Nit del Cliente: " + pedidoFactura.getNit());
                            System.out.println("Total: Q" + pedidoFactura.getTotal());
                            
                            System.out.println("\nLa orden se ha facturado y entregado correctamente");
                            // Puedes continuar aquí para generar la factura real o almacenarla en una tabla de Facturas.
                            // Por ejemplo, puedes crear una clase Factura y guardarla en una tabla de facturas.

                        } else {
                            System.out.println("No se encontró un pedido con el número de orden ingresado.");
                        }
                        break;


                    case 8:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } while (opcion != 5);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


	public static void insertarMaterial(Connection connection, Material material) throws SQLException {
	    // Verificar si el material ya existe
	    if (existeMaterial(connection, material.getNombre())) {
	        // Si existe, actualiza la cantidad
	        aumentarCantidadMaterial(connection, material.getNombre(), material.getCantidad());
	        System.out.println("Cantidad de material actualizada");
	    } else {
	        // Si no existe, inserta un nuevo registro
	        String sql = "INSERT INTO materiales (nombre, cantidad) VALUES (?, ?)";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	            preparedStatement.setString(1, material.getNombre());
	            preparedStatement.setInt(2, material.getCantidad());
	            preparedStatement.executeUpdate();
	            System.out.println("Material agregado");
	        }
	    }
	}


    public static void insertarProducto(Connection connection, Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (nombre, cantidad, precio) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, producto.getNombre());
            preparedStatement.setInt(2, producto.getCantidad());
            preparedStatement.setFloat(3, producto.getPrecio());
            preparedStatement.executeUpdate();
            System.out.println("Producto agregado");
        }
    }

    public static boolean existeMaterial(Connection connection, String nombre) throws SQLException {
        String sql = "SELECT * FROM materiales WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public static int obtenerCantidadMaterial(Connection connection, String nombre) throws SQLException {
        String sql = "SELECT cantidad FROM materiales WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("cantidad");
            }
            return 0;
        }
    }

    public static void reducirCantidadMaterial(Connection connection, String nombre, int cantidad) throws SQLException {
        String sql = "UPDATE materiales SET cantidad = cantidad - ? WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, cantidad);
            preparedStatement.setString(2, nombre);
            preparedStatement.executeUpdate();
        }
    }
    public static void actualizarProducto(Connection connection, Producto producto) throws SQLException {
        String sql = "UPDATE producto SET cantidad = ?, precio = ? WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, producto.getCantidad());
            preparedStatement.setFloat(2, producto.getPrecio());
            preparedStatement.setString(3, producto.getNombre());
            preparedStatement.executeUpdate();
            System.out.println("Producto actualizado");
        }
    }
    public static boolean existeProducto(Connection connection, String nombre) throws SQLException {
        String sql = "SELECT * FROM producto WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
    public static int obtenerCantidadProducto(Connection connection, String nombre) throws SQLException {
        String sql = "SELECT cantidad FROM producto WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("cantidad");
            }
            return 0;
        }
    }
    
    public static void actualizarMaterial(Connection connection, Material material) throws SQLException {
        String sql = "UPDATE materiales SET cantidad = ? WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(2, material.getCantidad());
            preparedStatement.executeUpdate();
            System.out.println("Producto actualizado");
        }
    }
    public static void aumentarCantidadMaterial(Connection connection, String nombre, int cantidad) throws SQLException {
        String sql = "UPDATE materiales SET cantidad = cantidad + ? WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, cantidad);
            preparedStatement.setString(2, nombre);
            preparedStatement.executeUpdate();
            //System.out.println("Cantidad de material actualizada");
        }
    }
    public static Pedido obtenerPedidoPorNumeroOrden(Connection connection, int Pnumero) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE numero = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Pnumero);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Construir un objeto Pedido a partir de los datos en la base de datos
                int numero = resultSet.getInt("Numero");
                String fecha = resultSet.getString("fecha");
                String nit = resultSet.getString("nit");
                float total = resultSet.getFloat("total");
                return new Pedido(numero, fecha, nit, total);
            }
            return null; // Devuelve null si no se encuentra el pedido
        }
    }
    public static void mostrarMateriales(Connection connection) throws SQLException {
        String sql = "SELECT nombre, cantidad FROM materiales";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("Listado de Materiales:");
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                int cantidad = resultSet.getInt("cantidad");
                System.out.println(nombre + " - Cantidad: " + cantidad);
            }
        }
    }

    public static void mostrarProductos(Connection connection) throws SQLException {
        String sql = "SELECT nombre, cantidad, precio FROM producto";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("Listado de Productos:");
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                int cantidad = resultSet.getInt("cantidad");
                float precio = resultSet.getFloat("precio");
                System.out.println(nombre + " - Cantidad: " + cantidad + " - Precio: " + precio);
            }
        }
    }

}
