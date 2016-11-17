
/**
 * 
 * Conterá as estadisticas de cada xogador do xogo.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.1
 *
 */
public class EstadisticasXogador {

	private int Id;
	private String Nome;
	private String Tipo;
	private int Gañadas=0;
	private int Perdidas=0;
	private int PagoParcial=0;
	private int PagoTotal=0;
	
	/**
	 * Costructor das estadisticas dos xogadores.
	 * @param Id: Identificador do xogodaor.
	 * @param Tipo: String co tipo de Xogador, o iniciar o xogo este tamén sera o seu nome.
	 */
	public EstadisticasXogador(int Id, String Tipo)
	 {
		this.Id = Id;
		this.Nome = Tipo;
		this.Tipo = Tipo;
	 }
	
	
	/*------------------------------------------------------
	   ------------  Getters e setters da clase ------------
	   -----------------------------------------------------*/
	
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
	/**
	 * Devolve un string cos datos dos xogadores de maneira ordenada e centrada para mostrar na tabla coa lista destes.
	 */
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
	}//toString
	
	/**
	 * Centra unha palabra en un número determinado de caracteres, enchendo os hocos da esquerda e dereita con espazos en branco.
	 * @param s: palabra que se quere centrar.
	 * @param width: número de caracters que se quere que teña o string de saída
	 * @return String centrado
	 */
	public String centrar(String s, int width)
	 {
		int padSize, padStart;
		
		 padSize = width - s.length();
		 padStart = s.length() + padSize / 2;
		 s = String.format("%" + padStart + "s", s);
		 s = String.format("%-" + width  + "s", s);
		 return s;
	 }//centrar
	
	
	/**
	 * Pon a cero todas as estadisticas do xogador
	 */
	public void reiniciarXogador()
	 {
		Gañadas=0;
		Perdidas=0;
		PagoParcial=0;
		PagoTotal=0;
	 }//reiniciarXogador
	
}//Clase EstadisticasXogador
