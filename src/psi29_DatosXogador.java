import jade.core.AID;

/**
 * 
 * Conterá as estadisticas de cada xogador do xogo.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
public class psi29_DatosXogador implements Comparable<psi29_DatosXogador> {

	private static int idXog=0;
	
	//Datos do xogador
	private int Id;
	private String Nome;
	private String Tipo;
	private int posInList;
	private AID axente;

	//Estadisticas do xogador
	private int RondasGanhadas=0;			//Rondas Gañadas na partida actual
	private int RondasPerdidas=0;			//Rondas Perdidas na partida actual
	private int PartidasGanhadas=0;			//Partidas Gañadas no xogo actual
	private int PartidasPerdidas=0;			//Partidas Perdidas no xogo actual
	private int PagoParcial=0;				//Payoff na partida actual
	private int PagoTotal=0;				//Payoff acumulado no xogo actual
	
	/**
	 * Costructor das estadisticas dos xogadores.
	 * @param Id: Identificador do xogodaor.
	 * @param Tipo: String co tipo de Xogador, o iniciar o xogo este tamén sera o seu nome.
	 */
	public psi29_DatosXogador(AID axente, String tipo)
	 {
		this.Id = idXog;
		this.Nome = axente.getLocalName();
		if(tipo.equals("")) tipo="Descoñecido";		//Se non pasamos o tipo marcamos descoñecido
		this.Tipo = tipo;
		this.posInList = Id;
		this.axente = axente;
		idXog++;
	 }
	
	
	/*------------------------------------------------------
	   ------------  Getters e setters da clase ------------
	   -----------------------------------------------------*/
	
	public AID getAxente() {
		return axente;
	}


	public void setAxente(AID axente) {
		this.axente = axente;
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

	public int getRondasGanhadas() {
		return RondasGanhadas;
	}

	public void setRondasGanhadas(int ganhadas) {
		RondasGanhadas = ganhadas;
	}

	public int getRondasPerdidas() {
		return RondasPerdidas;
	}

	public void setRondasPerdidas(int perdidas) {
		RondasPerdidas = perdidas;
	}

	public int getPartidasGañadas() {
		return PartidasGanhadas;
	}


	public void setPartidasGanhadas(int partidasGanhadas) {
		PartidasGanhadas = partidasGanhadas;
	}


	public int getPartidasPerdidas() {
		return PartidasPerdidas;
	}


	public void setPartidasPerdidas(int partidasPerdidas) {
		PartidasPerdidas = partidasPerdidas;
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
	
	public int getPosInList() {
		return posInList;
	}


	public void setPosInList(int posInList) {
		this.posInList = posInList;
	}
	
	
	
	/*------------------------------------------------------
	   --------------  Métodos para incrementar ------------
	   -------------  varios parámetros do xogo ------------
	   -----------------------------------------------------*/
	
	/**
	 * Engade unha unidade as rondas gañadas
	 */
	public void addRondaGanhada() {
		RondasGanhadas++;
	}//addRondaGanhada
	
	/**
	 * Engade unha unidade as rondas perdidas
	 */
	public void addRondaPerdida() {
		RondasPerdidas++;
	}//addRondaPerdida
	
	/**
	 * Engade unha unidade as partidas gañadas
	 */
	public void addPartidaGanhada() {
		PartidasGanhadas++;
	}//addPartidaGanhada
	
	/**
	 * Engade unha unidade as partidas perdidas
	 */
	public void addPartidaPerdida() {
		PartidasPerdidas++;
	}//addPartidaPerdida
	
	/**
	 * Engade o pago parcial e total unha cantidade
	 * @param pago Cantidade a engadir.
	 */
	public void addPagoParcial(int pago) {
		PagoParcial+=pago;
		PagoTotal+=pago;
	}//addPagoParcial
	
	/**
	 * Reinicia os datos de unha partida
	 * Rondas gañadas, rondas perdidas e pago Parcial.
	 */
	public void finPartida() {
		RondasGanhadas=0;
		RondasPerdidas=0;
		PagoParcial=0;
	}//finPartida
	
	@Override
	/**
	 * Devolve un string cos datos dos xogadores de maneira ordenada e centrada para mostrar na tabla coa lista destes.
	 */
	public String toString() {
	    String s="";
	    
	    s+=centrar(Tipo,16);
	    s+=centrar(Nome,17);
	    s+=centrar(Id+"",14);
	    s+=centrar(PartidasGanhadas+"",12);
	    s+=centrar(PartidasPerdidas+"",12);
//	    s+=centrar(PagoParcial+"",10);
	    s+=centrar(PagoTotal+"",13);
	    
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
		RondasGanhadas=0;
		RondasPerdidas=0;
		PartidasGanhadas=0;
		PartidasPerdidas=0;
		PagoParcial=0;
		PagoTotal=0;
	 }//reiniciarXogador


	@Override
	/**
	 * Comparador de dous xogadores
	 * Compara según as partidas gañadas, en caso de empate comproba as partidas perdidas e finalmente o payoff total.
	 */
	public int compareTo(psi29_DatosXogador xog) {
		
		if(PartidasGanhadas>xog.getPartidasGañadas()) return 1;
		if(PartidasGanhadas<xog.getPartidasGañadas()) return -1;
		if(PartidasPerdidas>xog.getPartidasPerdidas()) return -1;
		if(PartidasPerdidas<xog.getPartidasPerdidas()) return 1;
		if(PagoTotal>xog.getPagoTotal()) return 1;
		if(PagoTotal<xog.getPagoTotal()) return -1;
		
		return 0;
	}//compareTo
	
}//Clase EstadisticasXogador
