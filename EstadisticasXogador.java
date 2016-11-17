
/**
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.1
 *
 */
public class EstadisticasXogador {

	private int Id;
	private String Nome;
	private String Tipo;
	private int Gañadas=7;
	private int Perdidas=7;
	private int PagoParcial=7;
	private int PagoTotal=7;
	
	public EstadisticasXogador(int Id, String Tipo)
	 {
		this.Id = Id;
		this.Nome = Tipo;
		this.Tipo = Tipo;
	 }
	
	public String getTipo() {
		return Tipo;
	}

	public void setTipo(String tipo) {
		Tipo = tipo;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public int getGañadas() {
		return Gañadas;
	}

	public void setGañadas(int gañadas) {
		Gañadas = gañadas;
	}

	public int getPerdidas() {
		return Perdidas;
	}

	public void setPerdidas(int perdidas) {
		Perdidas = perdidas;
	}

	public int getPagoParcial() {
		return PagoParcial;
	}

	public void setPagoParcial(int pagoParcial) {
		PagoParcial = pagoParcial;
	}

	public int getPagoTotal() {
		return PagoTotal;
	}

	public void setPagoTotal(int pagoTotal) {
		PagoTotal = pagoTotal;
	}

	@Override
	public String toString() {
	    String s="";
	    
	    s+=centrar(Tipo,14);
	    s+=centrar(Nome,14);
	    s+=centrar(Id+"",16);
	    s+=centrar(Gañadas+"",10);
	    s+=centrar(Perdidas+"",10);
	    s+=centrar(PagoParcial+"",10);
	    s+=centrar(PagoTotal+"",12);
	    
		return s;
	}
	
	
	public String centrar(String s, int width)
	 {
		int padSize, padStart;
		
		 padSize = width - s.length();
		 padStart = s.length() + padSize / 2;
		 s = String.format("%" + padStart + "s", s);
		 s = String.format("%-" + width  + "s", s);
		 return s;
	 }
	
	public void reiniciarXogador()
	 {
		Gañadas=0;
		Perdidas=0;
		PagoParcial=0;
		PagoTotal=0;
	 }
	
}
