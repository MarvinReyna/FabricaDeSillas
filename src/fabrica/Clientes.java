package fabrica;

public class Clientes {
	private String Nit;
	private String Nombre;
	private String Telefono;
	public Clientes(String nit, String nombre, String telefono) {
		super();
		Nit = nit;
		Nombre = nombre;
		Telefono = telefono;
	}
	public String getNit() {
		return Nit;
	}
	public void setNit(String nit) {
		Nit = nit;
	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public String getTelefono() {
		return Telefono;
	}
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}
	
}
