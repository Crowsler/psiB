import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

//import jade.core.AID;
//import jade.core.Agent;
//import jade.lang.acl.ACLMessage;

/**
 * Clase principal do xogo.
 * Controlará a lóxica do xogo e almacenará os parámetros do mesmo
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.1
 *
 */
public class Xogo {

	public int numRondas=100;
	public int numCambMatriz=50;
	public int tamMatriz=5;
	public int porCambMatriz=100;
	public int[][][] Matriz = new int[2][tamMatriz][tamMatriz];
	public int numRondasXog=0;
	public boolean comentarios=true;
	public boolean comentarios_long=false;
	
	public ArrayList<Xogador> xogadores = new ArrayList<Xogador>();
	public ArrayList<EstadisticasXogador> estadisticas = new ArrayList<EstadisticasXogador>();
	
	private Xanela x;
	//private Arbitro arbitro;
	
	public Xogo(Xanela x)
	 {
		this.x=x;
		
		//Creamos a matriz completamente nova.
		cambiarMatriz(100);
		
		Xogador xogador;
		EstadisticasXogador eXogador;
		
		for(int i=0; i<6; i++)
		 {
			xogador = new Xogador();
			eXogador = new EstadisticasXogador(xogador.getId(), xogador.getTipo());
			xogadores.add(xogador);
			estadisticas.add(eXogador);
		 }
		
		//arbitro = new Arbitro();
		
	 }
	
	public void engadirLog(String msx, int tipo)
	 {
		String date = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss.SSS").format(new Date());
		if(comentarios)
		 {
			if(tipo==1 || comentarios_long)
				x.taLog.append("["+date+"] - "+msx+"\n");
		 }
	 }
	
	public void iniciarPartida()
	 {
		
	 }
	
	public void borrarXogadores(int[] ids)
	 {
		EstadisticasXogador Xog;
 		for(int i=0; i<ids.length; i++)
 		 {
 			Xog=estadisticas.get(ids[i]-i);
			engadirLog("Eliminouse o xogador: "+Xog.getNome()+" ("+Xog.getId()+")", 1);
 			xogadores.remove(ids[i]-i);
 			estadisticas.remove(ids[i]-i);
 			x.ListaXog.remove(ids[i]-i);
 		 }
 		x.EtiqNXogadores.setText(Xanela.TextNXogadores+estadisticas.size());
		x.EtiqNPartidas.setText(Xanela.TextNPartidas+coefbin(estadisticas.size(), 2));
	 }
	
	public void resetearXogadores(int[] ids)
	 {
		EstadisticasXogador Xog;
		for(int i=0; i<ids.length; i++)
		 {
			Xog=estadisticas.get(ids[i]);
			Xog.reiniciarXogador();
			x.ListaXog.replaceItem(Xog.toString(), ids[i]);
			engadirLog("Reiniciaronse os datos do xogador: "+Xog.getNome()+" ("+Xog.getId()+")", 1);
		 }
	 }
	
	
	
	/*------------------------------------------------------
	   ---- Funciones para a modificación de parámetros ----
	   --------------- do xogo de maneira visual -----------
	   -----------------------------------------------------*/
	
	public void delXogadoresSelect()
	 {
		if(x.ListaXog.getSelectedIndexes().length<=0)
 			x.mostrarMensaxeError("Non seleccionou ningún xogador");
 		else
 		 {
 			String msx="Quere eliminar os seguientes xogadores:<br><ul>";
 			EstadisticasXogador Xog;
			for(int i=0; i<x.ListaXog.getSelectedIndexes().length; i++)
			 {
				Xog=estadisticas.get(x.ListaXog.getSelectedIndexes()[i]);
				msx+="<li>"+Xog.getNome()+" ("+Xog.getId()+")</li>";
			 }
			msx+="</ul>";
	 		int opt = x.mostrarConfirmacion(msx, "Eliminar Xogador");
	 		if(opt==JOptionPane.YES_OPTION)
	 		 {
	 			borrarXogadores(x.ListaXog.getSelectedIndexes());
	 		 }
 		 }
	 }
	
	
	public void renameXogadoresSelect()
	 {
		String val;
		if(x.ListaXog.getSelectedIndexes().length<=0)
 			x.mostrarMensaxeError("Non seleccionou ningún xogador");
 		else if(x.ListaXog.getSelectedIndexes().length>1)
 			x.mostrarMensaxeError("Para renomear só pode seleccionar un xogador");
 		else
 		 {
 			val=x.getValor("Novo nome do xogador:","Renomear xogador", estadisticas.get(x.ListaXog.getSelectedIndex()).getNome());
 			if(val==null)
 				return;
 			EstadisticasXogador Xog;
 			Xog = estadisticas.get(x.ListaXog.getSelectedIndex());
 			engadirLog("O Xogador ("+Xog.getId()+") con nome "+Xog.getNome()+", cambiouse a "+val, 1);
 			estadisticas.get(x.ListaXog.getSelectedIndex()).setNome(val);
 			x.ListaXog.replaceItem(Xog.toString(), x.ListaXog.getSelectedIndex());
 		 }
	 }

	public void resetXogadoresSelect()
	 {
		if(x.ListaXog.getSelectedIndexes().length<=0)
				x.mostrarMensaxeError("Non seleccionou ningún xogador");
		else
		 {
			String msx="Quere reiniciar os valores dos xogadores:<br><ul>";
			EstadisticasXogador Xog;
			for(int i=0; i<x.ListaXog.getSelectedIndexes().length; i++)
			 {
				Xog=estadisticas.get(x.ListaXog.getSelectedIndexes()[i]);
				msx+="<li>"+Xog.getNome()+" ("+Xog.getId()+")</li>";
			 }
	
			msx+="</ul>";
	 		int opt = x.mostrarConfirmacion(msx, "Reiniciar Xogador");
	 		if(opt==JOptionPane.YES_OPTION)
	 		 {
	 			resetearXogadores(x.ListaXog.getSelectedIndexes());
	 		 }
		 }
	 }
	
	public void resizeMatriz()
	 {
		String val;
		int tam;
 		val=x.getValor("Filas/Columnas da matriz:","Tamaño Matriz",tamMatriz+"");
 		if(val==null) return;
 		try
 		 {
 			tam=Integer.parseInt(val);
 			if(tam>250 || tam<0)
 				x.mostrarMensaxeError("O número de filas e columnas da matriz, non pode superar as 250 nin ser negativo");
 			else
 				engadirLog("A matriz pasa de tamaño "+tamMatriz+" a tamaño "+tam, 1);
 				setTamMatriz(tam);
 		 }
 		catch(NumberFormatException nfe)
 		 {
 			x.mostrarMensaxeError("Debe introducir un número");
 		 }
	 }
	
	public void setRondas()
	 {
		String val;
		int tam;
		
		val=x.getValor("Número de rondas por partida:","Número rondas",numRondas+"");
 		if(val==null) return;
 		try
 		 {
 			tam=Integer.parseInt(val);
 			if(tam<0)
 				x.mostrarMensaxeError("O número de rondas non pode ser negativo");
 			else
 			 {
 				engadirLog("Cambiouse o número de rondas por partida de "+numRondas+" a "+tam, 1);
 				numRondas=tam;
 				x.EtiqNRondas.setText(Xanela.TextNRondas+numRondas);
 			 }
 		 }
 		catch(NumberFormatException nfe)
 		 {
 			x.mostrarMensaxeError("Debe introducir un número");
 		 }
	 }
	
	public void setRondasMatriz()
	 {
		String val;
		int tam;
		
		val=x.getValor("Número de rondas para que cambie a matriz:","Número rondas cambio matriz",numCambMatriz+"");
 		if(val==null) return;
 		try
 		 {
 			tam=Integer.parseInt(val);
 			if(tam<0)
 			 {
 				x.mostrarMensaxeError("O número de rondas para que cambie a matriz non pode ser negativo");
 			 }
 			else
 			 {
 				engadirLog("Cambiouse o número de rondas para que cambia a matriz, de "+numCambMatriz+" a "+tam, 1);
 				numCambMatriz=tam;
 				x.EtiqNCamMatriz.setText(Xanela.TextNCamMatriz+tam);
 			 }
 		 }
 		catch(NumberFormatException nfe)
 		 {
 			x.mostrarMensaxeError("Debe introducir un número");
 		 }
	 }
	
	public void setPorcentajeMatriz()
	 {
		String val;
		int tam;
		
		val=x.getValor("Porcentaje que cambia a Matriz:","Porcentaje cambio matriz",porCambMatriz+"");
			if(val==null) return;
 		try
 		 {
 			tam=Integer.parseInt(val);
 			if(tam<0 || tam>100)
 				x.mostrarMensaxeError("O porcentaxe ten que estar entre 0 - 100");
 			else
 			 {
 				engadirLog("O porcentaxe de cambio da matriz pasou do "+porCambMatriz+"% a "+tam+"%", 1);
 				porCambMatriz=tam;
 				x.EtiqPCamMatriz.setText(Xanela.TextPCamMatriz+tam+"%");
 			 }
 		 }
 		catch(NumberFormatException nfe)
 		 {
 			x.mostrarMensaxeError("Debe introducir un número");
 		 }
	 }
	

	
	/*------------------------------------------------------
	   --------  Funcions para traballar coa Matriz --------
	   -----------------------------------------------------*/
	
	/**
	 * Función para completar a parte simétrica da matriz.
	 */
	public void completarMatriz()
	 {
		for(int i=1; i<tamMatriz; i++)
		 {
			for(int j=0; j<i; j++)
			 {
				Matriz[0][i][j]=Matriz[1][j][i];
				Matriz[1][i][j]=Matriz[0][j][i];
			 }
		 }
	 }
	
	
	public void cambiarMatriz(int pCambio)
	 {
		int cambioNum=Math.round((tamMatriz*tamMatriz)/((float)pCambio/100));
		int fila, columna;
		ArrayList<String> recorridos = new ArrayList<String>();
				
		while(cambioNum>0)
		 {
			fila=(int)(Math.random()*tamMatriz);
			columna=(int)(Math.random()*tamMatriz);
			if(fila<=columna && !recorridos.contains(fila+"|"+columna))
			 {
				Matriz[0][fila][columna]=(int)(Math.random()*10);
				Matriz[1][fila][columna]=(int)(Math.random()*10);
				recorridos.add(fila+"|"+columna);
				if(columna==fila)
					cambioNum--;
				else
					cambioNum-=2;
			 }
		 }

		completarMatriz();
		
	 }
	
	
	/**
	 * Está matriz devolve un string cos valores de matriz mostrados en filas e columna e mostrando vectores.
	 * @return String, valores da matriz mostrados de forma visual.
	 */
	public String toStringMatriz()
	 {
		String sMatriz="";
		for(int i=0; i<tamMatriz; i++)
		 {
			for(int j=0; j<tamMatriz; j++)
			 {
				sMatriz+="("+Matriz[0][i][j]+",";
				sMatriz+=Matriz[1][i][j]+") ";
			 }
			sMatriz+="\n";
		 }
		return sMatriz;
	 }
	
	public void setTamMatriz(int tam)
	 {
		tamMatriz=tam;
		Matriz = new int[2][tam][tam];
		cambiarMatriz(100);
		x.taMatriz.setText(toStringMatriz());
	 }
	
	
	
	
    public static int coefbin(int n, int k){ 
        if(k == 0 || k == n) 
             return 1; 
        else if(k > n)
             return 0;
        else 
             return coefbin(n-1, k-1) + coefbin(n-1, k); 
   } 
	
 }