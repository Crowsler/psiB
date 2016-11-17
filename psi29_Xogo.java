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
public class psi29_Xogo {

	//Parámetros do xogo. Engádense algúns por defecto
	public int numRondas=100;									//Número de rondas por cada partida
	public int numCambMatriz=50;								//Número de rondas que pasan para o cambio da matriz
	public int tamMatriz=5;										//Tamaño da matriz
	public int porCambMatriz=100;								//Pocentaxe de renovación da Matriz.
	public int[][][] Matriz = new int[2][tamMatriz][tamMatriz];	//Matriz do xogo
	public int numRondasXog=0;									//Número de rondas que xa se xogaron en cada partida.
	public boolean comentarios=true;							//Activar/Desactivar comentarios
	public boolean comentarios_long=false;						//Activar/Desactivar comentarios longos.
	
	//Lista de xogadores e estadísticas dos mesmos.
	public ArrayList<psi29_Xogador> xogadores = new ArrayList<psi29_Xogador>();
	public ArrayList<psi29_EstadisticasXogador> estadisticas = new ArrayList<psi29_EstadisticasXogador>();
	
	//Ventá principal.
	private psi29_Xanela x;
	//private Arbitro arbitro;
	
	
	/**
	 * Constructor do xogo, crea a matriz e engade os xogadores iniciais.
	 * @param x Obxecto coa ventá principal
	 */
	public psi29_Xogo(psi29_Xanela x)
	 {
		this.x=x;
		
		//Creamos a matriz completamente nova.
		cambiarMatriz(100);
		
		psi29_Xogador xogador;
		psi29_EstadisticasXogador eXogador;
		
		for(int i=0; i<6; i++)
		 {
			xogador = new psi29_Xogador();
			eXogador = new psi29_EstadisticasXogador(xogador.getId(), xogador.getTipo());
			xogadores.add(xogador);
			estadisticas.add(eXogador);
		 }
		
		//arbitro = new Arbitro();
		
	 }//Xogo
	
	
	/**
	 * Engade unha cadena de texto o Textarea de log, incluíndo un Timestamp coa hora na que se xerou.
	 * @param msx: Mensaxe que se quere mostrar
	 * @param tipo: Tipo do mensaxe (1 Tipo normal, 2 Tipo detallado) (Os de tipo 2 só se mostran cando está habilitada a opción de comentarios longos)
	 */
	public void engadirLog(String msx, int tipo)
	 {
		String date = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss.SSS").format(new Date());		//Obtemos o timestamp
		if(comentarios)
		 {
			if(tipo==1 || comentarios_long)
				x.taLog.append("["+date+"] - "+msx+"\n");
		 }
	 }//engadirLog
	
	/**
	 * Inicio da partida
	 */
	public void iniciarPartida()
	 {
		
	 }//iniciarPartida
	
	/**
	 * Elimina un listado de xogadores, asi como as suas estadísticas e volve imprimir na venta o listado co resto de xogadores.
	 * @param ids: Posición que ocupa o xogador na lista (comezando a contar polo 0)
	 */
	public void borrarXogadores(int[] ids)
	 {
		psi29_EstadisticasXogador Xog;
		
		//Eliminamos cada un dos xogadores
 		for(int i=0; i<ids.length; i++)
 		 {
 			Xog=estadisticas.get(ids[i]-i);
			engadirLog("Eliminouse o xogador: "+Xog.getNome()+" ("+Xog.getId()+")", 1);
 			xogadores.remove(ids[i]-i);
 			estadisticas.remove(ids[i]-i);
 			x.ListaXog.remove(ids[i]-i);
 		 }
 		
 		//Modificamos a lista e os datos referentes os xogadores na ventá
 		x.EtiqNXogadores.setText(psi29_Xanela.TextNXogadores+estadisticas.size());
		x.EtiqNPartidas.setText(psi29_Xanela.TextNPartidas+coefbin(estadisticas.size(), 2));
	 }//borrarXogadores

	/**
	 * Pon os parámetros dos xogadores a 0. Ademáis volve imprimir os parámetros dos xogadores actualizados.
	 * @param ids: Posición que ocupa o xogador na lista (comezando a contar polo 0)
	 */
	public void resetearXogadores(int[] ids)
	 {
		psi29_EstadisticasXogador Xog;
		for(int i=0; i<ids.length; i++)
		 {
			Xog=estadisticas.get(ids[i]);
			Xog.reiniciarXogador();
			x.ListaXog.replaceItem(Xog.toString(), ids[i]);
			engadirLog("Reiniciaronse os datos do xogador: "+Xog.getNome()+" ("+Xog.getId()+")", 1);
		 }
	 }//resetearXogadores
	
	
	
	/*------------------------------------------------------
	   ----  Funcions para a modificación de parámetros ----
	   --------------- do xogo de maneira visual -----------
	   -----------------------------------------------------*/
	/**
	 * Borra os xogadores que estén seleccionados en ese momento.
	 * Antes mostra unha ventá pedindo a confirmación de borrado de eses xogadores.
	 */
	public void delXogadoresSelect()
	 {
		if(x.ListaXog.getSelectedIndexes().length<=0)	//En caso de non ter seleccioado ningún xogador mostramos mensaxe de error.
 			x.mostrarMensaxeError("Non seleccionou ningún xogador");
 		else
 		 {
 			//Preparamos unha mensaxe de confirmación, cos ids de todos os xogadores.
 			String msx="Quere eliminar os seguientes xogadores:<br><ul>";
 			psi29_EstadisticasXogador Xog;
			for(int i=0; i<x.ListaXog.getSelectedIndexes().length; i++)
			 {
				Xog=estadisticas.get(x.ListaXog.getSelectedIndexes()[i]);
				msx+="<li>"+Xog.getNome()+" ("+Xog.getId()+")</li>";
			 }
			msx+="</ul>";
			
			//Mostramos o mensaxe, e en caso de ser afirmativo, eliminamolos.
	 		int opt = x.mostrarConfirmacion(msx, "Eliminar Xogador");
	 		if(opt==JOptionPane.YES_OPTION)
	 		 {
	 			borrarXogadores(x.ListaXog.getSelectedIndexes());
	 		 }
 		 }
	 }//delXogadroesSelect
	
	/**
	 * Cambia o nome do xogador seleccionado na lista.
	 */
	public void renameXogadoresSelect()
	 {
		String val;
		if(x.ListaXog.getSelectedIndexes().length<=0)	//En caso de non ter seleccioado ningún xogador mostramos mensaxe de error.
 			x.mostrarMensaxeError("Non seleccionou ningún xogador");
 		else if(x.ListaXog.getSelectedIndexes().length>1)	//En caso de seleccionar mais de un xogador mostramos mensaxe de error.
 			x.mostrarMensaxeError("Para renomear só pode seleccionar un xogador");
 		else
 		 {
 			//Mostrá a venta para solicitar o nome e en caso de ser válido, sustitueo e actializa o valor na lista.
 			val=x.getValor("Novo nome do xogador:","Renomear xogador", estadisticas.get(x.ListaXog.getSelectedIndex()).getNome());
 			if(val==null)
 				return;
 			psi29_EstadisticasXogador Xog;
 			Xog = estadisticas.get(x.ListaXog.getSelectedIndex());
 			engadirLog("O Xogador ("+Xog.getId()+") con nome "+Xog.getNome()+", cambiouse a "+val, 1);
 			estadisticas.get(x.ListaXog.getSelectedIndex()).setNome(val);
 			x.ListaXog.replaceItem(Xog.toString(), x.ListaXog.getSelectedIndex());
 		 }
	 }//renameXogadoresSelect

	/**
	 * Resetea os parámetros do xogadores seleccionados
	 * Antes solicita confirmación
	 */
	public void resetXogadoresSelect()
	 {
		if(x.ListaXog.getSelectedIndexes().length<=0)	//En caso de non ter seleccioado ningún xogador mostramos mensaxe de error.
				x.mostrarMensaxeError("Non seleccionou ningún xogador");
		else
		 {
			//Preparamos unha mensaxe de confirmación, cos ids de todos os xogadores.
			String msx="Quere reiniciar os valores dos xogadores:<br><ul>";
			psi29_EstadisticasXogador Xog;
			for(int i=0; i<x.ListaXog.getSelectedIndexes().length; i++)
			 {
				Xog=estadisticas.get(x.ListaXog.getSelectedIndexes()[i]);
				msx+="<li>"+Xog.getNome()+" ("+Xog.getId()+")</li>";
			 }
			msx+="</ul>";
			
			//Mostramos o mensaxe, e en caso de ser afirmativo, reseteaos.
	 		int opt = x.mostrarConfirmacion(msx, "Reiniciar Xogador");
	 		if(opt==JOptionPane.YES_OPTION)
	 		 {
	 			resetearXogadores(x.ListaXog.getSelectedIndexes());
	 		 }
		 }
	 }//resetXogadoresSelect
	
	/**
	 * Permite insertar un número co novo tamaño da matriz, en caso de ser un número válido,
	 * chama a función para cambiarlle o tamaño e xerar unha nova matriz.
	 */
	public void resizeMatriz()
	 {
		String val;
		int tam;
		//Solicita o usuario o novo tamaño da matriz
 		val=x.getValor("Filas/Columnas da matriz:","Tamaño Matriz",tamMatriz+"");
 		if(val==null) return;
 		try
 		 {
 			//Para non ter ningún problema, a matriz non pode ser superior a 250, xa que o cálculo de esta sería moi lento.
 			tam=Integer.parseInt(val);
 			if(tam>250 || tam<0)
 				x.mostrarMensaxeError("O número de filas e columnas da matriz, non pode superar as 250 nin ser negativo");
 			else //Se todo esta correcto chamamos a función para cambiar a matriz e xerala.
 			 {
 				engadirLog("A matriz pasa de tamaño "+tamMatriz+" a tamaño "+tam, 1);
 				setTamMatriz(tam);
 			 }
 		 }
 		catch(NumberFormatException nfe)
 		 {
 			x.mostrarMensaxeError("Debe introducir un número");
 		 }
	 }//resizeMatriz
	
	/**
	 * Modifica o número de rondas de cada partida según o número que inserte o usuario.
	 */
	public void setRondas()
	 {
		String val;
		int tam;
		//Solicita o número de rondas que se queren por partida.
		val=x.getValor("Número de rondas por partida:","Número rondas",numRondas+"");
 		if(val==null) return;
 		try
 		 {
 			tam=Integer.parseInt(val);
 			if(tam<0)
 				x.mostrarMensaxeError("O número de rondas non pode ser negativo");
 			else
 			 {
 				//Actualiza o parámetro tanto no sistema coma na ventá.
 				engadirLog("Cambiouse o número de rondas por partida de "+numRondas+" a "+tam, 1);
 				numRondas=tam;
 				x.EtiqNRondas.setText(psi29_Xanela.TextNRondas+numRondas);
 			 }
 		 }
 		catch(NumberFormatException nfe)
 		 {
 			x.mostrarMensaxeError("Debe introducir un número");
 		 }
	 }//setRondas
	
	/**
	 * Modifica o número de rondas que teñen que pasar para que cambie a matriz.
	 */
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
 				//Actualiza o parámetro tanto no sistema coma na ventá.
 				engadirLog("Cambiouse o número de rondas para que cambia a matriz, de "+numCambMatriz+" a "+tam, 1);
 				numCambMatriz=tam;
 				x.EtiqNCamMatriz.setText(psi29_Xanela.TextNCamMatriz+tam);
 			 }
 		 }
 		catch(NumberFormatException nfe)
 		 {
 			x.mostrarMensaxeError("Debe introducir un número");
 		 }
	 }//setRondasMatriz
	
	/**
	 * Modifica o parámetro co porcentaxe da matriz que cambia de cada vez.
	 */
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
 				//Actualiza o parámetro tanto no sistema coma na ventá.
 				engadirLog("O porcentaxe de cambio da matriz pasou do "+porCambMatriz+"% a "+tam+"%", 1);
 				porCambMatriz=tam;
 				x.EtiqPCamMatriz.setText(psi29_Xanela.TextPCamMatriz+tam+"%");
 			 }
 		 }
 		catch(NumberFormatException nfe)
 		 {
 			x.mostrarMensaxeError("Debe introducir un número");
 		 }
	 }//setPorcentajeMatriz
	

	
	/*------------------------------------------------------
	   --------  Funcions para traballar coa Matriz --------
	   -----------------------------------------------------*/
	
	/**
	 * Cambia a matriz, xerando de maneira aleatoria unicamente a parte superior da matriz, pero so cambia o porcentaxe da matriz que se lle pasa por parámetro
	 * @param pCambio: porcentaxe da matriz que se vai a renovar.
	 */
	public void cambiarMatriz(int pCambio)
	 {
		//Calculamos as posicións da matriz que deben ser cambiadas con respecto o pCambio e o tamaño total da matriz
		int cambioNum=Math.round((tamMatriz*tamMatriz)/((float)pCambio/100));
		int fila, columna;
		ArrayList<String> recorridos = new ArrayList<String>();
		
		//Realizamos este bucle ata que cambiemos tantos número da matriz como se nos solicitou.
		while(cambioNum>0)
		 {
			//Calculamos un random para escoller que fila e columna da matriz imos modificar
			fila=(int)(Math.random()*tamMatriz);
			columna=(int)(Math.random()*tamMatriz);
			
			//Só collemo os que son da parte superior da matriz (Xa que é simétrica), e os que non foron modificados anteriormente.
			if(fila<=columna && !recorridos.contains(fila+"|"+columna))
			 {
				//Obtemos un número aleatório entre 0 e 9 para gardar no vector da matriz.
				Matriz[0][fila][columna]=(int)(Math.random()*10);
				Matriz[1][fila][columna]=(int)(Math.random()*10);
				recorridos.add(fila+"|"+columna);
				//Se cambiamos un número na diagonal principal conta como 1, mentres que outro calquera conta como 2.
				if(columna==fila)
					cambioNum--;
				else
					cambioNum-=2;
			 }
		 }

		//Chamamos a función que nos completará a parte inferior da matriz, de maneira simétrica.
		completarMatriz();
	 }//cambiarMatriz
	
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
	 }//completarMatriz
	
	
	/**
	 * Este método devolve un string cos valores de matriz mostrados en filas e columna e mostrando vectores.
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
	 }//toStringMatriz
	
	/**
	 * Crea unha nova matriz baleira con un tamaño novo. Xera esa matriz e mostraa por pantalla.
	 * @param tam
	 */
	public void setTamMatriz(int tam)
	 {
		tamMatriz=tam;
		Matriz = new int[2][tam][tam];
		cambiarMatriz(100);
		x.taMatriz.setText(toStringMatriz());
	 }//setTamMatriz
	
	
	
	/**
	 * Cálculo dun coeficiente binario
	 * @param n: Número superior
	 * @param k: Número inferior
	 * @return coeficinte binario
	 */
    public static int coefbin(int n, int k){ 
        if(k == 0 || k == n) 
             return 1; 
        else if(k > n)
             return 0;
        else 
             return coefbin(n-1, k-1) + coefbin(n-1, k); 
   }//coefbin
	
 }//Clase Xogo