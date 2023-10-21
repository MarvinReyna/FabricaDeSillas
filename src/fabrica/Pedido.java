package fabrica;

public class Pedido {
	private int Numero;
	private String Fecha;
	private String Nit;
	private float Total;
	public Pedido(int numero, String fecha, String nit, float total) {
		super();
		Numero = numero;
		Fecha = fecha;
		Nit = nit;
		Total = total;
	}
	public int getNumero() {
		return Numero;
	}
	public void setNumero(int numero) {
		Numero = numero;
	}
	public String getFecha() {
		return Fecha;
	}
	public void setFecha(String fecha) {
		Fecha = fecha;
	}
	public String getNit() {
		return Nit;
	}
	public void setNit(String nit) {
		Nit = nit;
	}
	public float getTotal() {
		return Total;
	}
	public void setTotal(float total) {
		Total = total;
	}
	
}
