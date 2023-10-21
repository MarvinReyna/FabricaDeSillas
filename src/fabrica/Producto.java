package fabrica;

public class Producto {
	private String Nombre;
	private int Cantidad;
	private float Precio;
	public Producto(String nombre, int cantidad, float precio) {
		super();
		Nombre = nombre;
		Cantidad = cantidad;
		Precio = precio;
	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public int getCantidad() {
		return Cantidad;
	}
	public void setCantidad(int cantidad) {
		Cantidad = cantidad;
	}
	public float getPrecio() {
		return Precio;
	}
	public void setPrecio(float precio) {
		Precio = precio;
	}
	
}
