import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Clase principal do xogo.
 * Controlará a lóxica do xogo e almacenará os parámetros do mesmo
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.2
 *
 */
public class psi29_Xogo implements Runnable {

	//Parámetros do xogo. Engádense algúns por defecto
	public int numRondas=100;									//Número de rondas por cada partida
	public int numCambMatriz=50;								//Número de rondas que pasan para o cambio da matriz
	public int tamMatriz=5;										//Tamaño da matriz
	public int porCambMatriz=100;								//Pocentaxe de renovación da Matriz.
	public int[][][] Matriz = new int[2][tamMatriz][tamMatriz];	//Matriz do xogo
	public int numRondasXog=0;									//Número de rondas que xa se xogaron en cada partida.
	public boolean comentarios=true;							//Activar/Desactivar comentarios
	public boolean comentarios_long=false;						//Activar/Desactivar comentarios longos.
	public psi29_MainAg arbitro;								//MainAgent encargado de buscar os xogadores e comunicarse con eles
	public boolean xogoIniciado=false;							//Indica se o xogo está iniciado
	public boolean xogoPausado=true;							//Indica se o xogo está pausado.
	
	//Lista de xogadores coas estadísticas dos mesmos.
	public ArrayList<psi29_DatosXogador> xogadores = new ArrayList<psi29_DatosXogador>();
	
	//Ventá principal.
	private psi29_Xanela x;
	
	
	/**
	 * 
	 * Constructor do xogo, crea a matriz e engade os xogadores iniciais.
	 * @param x Obxecto coa ventá principal
	 * @param arbitro MainAgent principal
	 */
	public psi29_Xogo(psi29_Xanela x, psi29_MainAg arbitro)
	 {
		this.x=x;
		this.arbitro = arbitro;
					
	 }//Xogo
	
	
	/**
	 * Engade unha cadena de texto o Textarea de log, incluíndo un Timestamp coa hora na que se xerou.
	 * @param msx: Mensaxe que se quere mostrar
	 * @param tipo: Tipo do mensaxe (0 Tipo normal (carga inmediata),1 Tipo normal (Carga con espera), 2 Tipo detallado) (Os de tipo 2 só se mostran cando está habilitada a opción de comentarios longos)
	 */
	public void engadirLog(String msx, int tipo)
	 {
		String date;
		
		if(comentarios && (tipo<=1 || comentarios_long)) {
			
			date = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss.SSS").format(new Date());		//Obtemos o timestamp		

			if(tipo==0) {
				x.taLog.append("["+date+"] - "+msx+"\n");
				return;
			}
			
			//Para evitar problemas de sincronización e desborde do buffer de log, utilizamos a función inokeAndWait
			//desta forma esperamos a que acabe de facer o volcado en pantalla antes de continuar.
			try {
				java.awt.EventQueue.invokeAndWait(new Runnable(){
					@Override
					public void run() {
						x.taLog.append("["+date+"] - "+msx+"\n");
					}});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	 	  }

	 }//engadirLog
	
	/**
	 * Inicio do Xogo
	 */
	public void iniciarXogo()
	 {
						
		Thread hilo = new Thread(this);
        hilo.start();
                
	 }//iniciarXogo
	
	/**
	 * Elimina un listado de xogadores, asi como as suas estadísticas e volve imprimir na venta o listado co resto de xogadores.
	 * @param ids: Posición que ocupa o xogador na lista (comezando a contar polo 0)
	 */
	public void borrarXogadores(int[] ids)
	 {
		psi29_DatosXogador Xog;
		
		//Eliminamos cada un dos xogadores
 		for(int i=0; i<ids.length; i++)
 		 {
 			Xog=xogadores.get(ids[i]-i);
			engadirLog("Eliminouse o xogador: "+Xog.getNome()+" ("+Xog.getId()+")", 0);
 			xogadores.remove(ids[i]-i);
 			x.ListaXog.remove(ids[i]-i);
 		 }
 		
 		for(int i=0; i<xogadores.size(); i++) {
 			xogadores.get(i).setPosInList(i);
 		}
 		
 		//Modificamos a lista e os datos referentes os xogadores na ventá
 		x.EtiqNXogadores.setText(psi29_Xanela.TextNXogadores+xogadores.size());
		x.EtiqNPartidas.setText(psi29_Xanela.TextNPartidas+coefbin(xogadores.size(), 2));
	 }//borrarXogadores

	/**
	 * Pon os parámetros dos xogadores a 0. Ademáis volve imprimir os parámetros dos xogadores actualizados.
	 * @param ids: Posición que ocupa o xogador na lista (comezando a contar polo 0)
	 */
	public void resetearXogadores(int[] ids)
	 {
		psi29_DatosXogador Xog;
		for(int i=0; i<ids.length; i++)
		 {
			Xog=xogadores.get(ids[i]);
			Xog.reiniciarXogador();
			x.ListaXog.replaceItem(Xog.toString(), ids[i]);
			engadirLog("Reiniciaronse os datos do xogador: "+Xog.getNome()+" ("+Xog.getId()+")", 0);
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
 			psi29_DatosXogador Xog;
			for(int i=0; i<x.ListaXog.getSelectedIndexes().length; i++)
			 {
				Xog=xogadores.get(x.ListaXog.getSelectedIndexes()[i]);
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
 			val=x.getValor("Novo nome do xogador:","Renomear xogador", xogadores.get(x.ListaXog.getSelectedIndex()).getNome());
 			if(val==null)
 				return;
 			psi29_DatosXogador Xog;
 			Xog = xogadores.get(x.ListaXog.getSelectedIndex());
 			engadirLog("O Xogador ("+Xog.getId()+") con nome "+Xog.getNome()+", cambiouse a "+val, 0);
 			xogadores.get(x.ListaXog.getSelectedIndex()).setNome(val);
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
			psi29_DatosXogador Xog;
			for(int i=0; i<x.ListaXog.getSelectedIndexes().length; i++)
			 {
				Xog=xogadores.get(x.ListaXog.getSelectedIndexes()[i]);
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
	 * Resetea os parámetros de todos os xogadores, solicitando un mensaxe de confirmación.
	 * Actualiza os parámetros na lista da ventá.
	 */
	public void resetAllXogadores() {
		String msx="Quere reiniciar os valores de todos os xogadores.";
		
		//Mostramos o mensaxe, e en caso de ser afirmativo, reseteaos.
		int opt = x.mostrarConfirmacion(msx, "Reiniciar Xogadores");
		if(opt==JOptionPane.YES_OPTION)
		 {
			for(int i=0; i<xogadores.size(); i++)
			 {
				xogadores.get(i).reiniciarXogador();
				x.ListaXog.replaceItem(xogadores.get(i).toString(), xogadores.get(i).getPosInList());
			 }
			engadirLog("Reiniciaronse os datos de todos os xogadores", 0);
		 }
	}//resetAllXogadores
	
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
 			if(tam>250 || tam<1)
 				x.mostrarMensaxeError("O número de filas e columnas da matriz, non pode superar as 250 nin ser menor a 1");
 			else //Se todo esta correcto chamamos a función para cambiar a matriz e xerala.
 			 {
 				engadirLog("A matriz pasa de tamaño "+tamMatriz+" a tamaño "+tam, 0);
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
 				engadirLog("Cambiouse o número de rondas por partida de "+numRondas+" a "+tam, 0);
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
 				engadirLog("Cambiouse o número de rondas para que cambia a matriz, de "+numCambMatriz+" a "+tam, 0);
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
 				engadirLog("O porcentaxe de cambio da matriz pasou do "+porCambMatriz+"% a "+tam+"%", 0);
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
		if(pCambio==0)
			return;
		//Calculamos as posicións da matriz que deben ser cambiadas con respecto o pCambio e o tamaño total da matriz
		int cambioNum=Math.round((tamMatriz*tamMatriz)*((float)pCambio/100));
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
				if(fila!=columna)
					Matriz[1][fila][columna]=(int)(Math.random()*10);
				else
					Matriz[1][fila][columna]=Matriz[0][fila][columna];
				
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
		
		//Para evitar problemas de sincronización e desborde do buffer da matriz, utilizamos a función inokeAndWait
		//desta forma esperamos a que acabe de facer o volcado en pantalla antes de continuar.
		if(java.awt.EventQueue.isDispatchThread())
			x.taMatriz.setText(toStringMatriz());
		else {
		
			try {
				java.awt.EventQueue.invokeAndWait(new Runnable(){
					@Override
					public void run() {
						x.taMatriz.setText(toStringMatriz());
					}});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
		
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
    
    
    /**
     * Reinicia os parámetros de todos os xogadores
     */
    public void reiniciarXogadores() {
    	for(int i=0; i<xogadores.size(); i++) {
    		xogadores.get(i).reiniciarXogador();
    		x.ListaXog.replaceItem(xogadores.get(i).toString(), xogadores.get(i).getPosInList());
    	}
    }//reiniciarXogadores
    
    /**
     * Actualiza os parámetros de dous na táboa de partida.
     * @param xog1
     * @param xog2
     */
    public void actualizarDatosRondas(psi29_DatosXogador xog1, psi29_DatosXogador xog2) {
    	x.xog1_pvp.setText(xog1.getNome());
    	x.xog1_g_pvp.setText(xog1.getRondasGanhadas()+"");
    	x.xog1_p_pvp.setText(xog1.getRondasPerdidas()+"");
    	x.xog1_pag_pvp.setText(xog1.getPagoParcial()+"");
    	x.xog2_pvp.setText(xog2.getNome());
    	x.xog2_g_pvp.setText(xog2.getRondasGanhadas()+"");
    	x.xog2_p_pvp.setText(xog2.getRondasPerdidas()+"");
    	x.xog2_pag_pvp.setText(xog2.getPagoParcial()+"");
    	x.EtiqNRondasXog.setText(psi29_Xanela.TextNRondasXog+numRondasXog);
    }//actualizarDatosDondas
    
    /**
     * Actualiza os parámetros de dous xogadores na lista da ventá
     * @param xog1
     * @param xog2
     */
    public void actualizarDatosPartida(psi29_DatosXogador xog1, psi29_DatosXogador xog2) {
    	x.ListaXog.replaceItem(xog1.toString(), xog1.getPosInList());
    	x.ListaXog.replaceItem(xog2.toString(), xog2.getPosInList());
    }//actualizarDatosPartida

    
    /**
     * Esta función inicia unha nova partida entre dous xogadores.
     * Xogarán todas as rondas e o finalizar calcula o gañador da partida e actualiza os parámetros. Recalcula toda a matriz o rematar a partida.
     * @param xog1 Xogador co menor id (Xoga con filas)
     * @param xog2 Xogador co maior id (Xoga con columnnas)
     */
	public void novaPartida(psi29_DatosXogador xog1, psi29_DatosXogador xog2) {
		
		//Contador de número de rondas para o próximo cambio da matriz
		int cont=0;
		
		//Actualizamos os parámetros da taboa de partida e enviamos o mensaxe de nova partida os xogadores correspondentes.
		actualizarDatosRondas(xog1, xog2);
		engadirLog("Nova partida entre os xogadores "+xog1.getNome()+" e "+xog2.getNome(), 1);
		arbitro.mensaxeNovaPartida(xog1, xog2);
		
		//Bucle de rondas
		for(int i=0; i<numRondas; i++) {
			
			//En caso de que esté marcado o xogo como pausado, detemos o xogo antes de iniciar outra ronda.
			if(xogoPausado){
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//Solicitamos a cada xogador que escolla un número (fila/columna)
			int escollaXog1 = arbitro.escollaXog(xog1);
			int escollaXog2 = arbitro.escollaXog(xog2);
			int pagoXog1 = Matriz[0][escollaXog1][escollaXog2];
			int pagoXog2 = Matriz[1][escollaXog1][escollaXog2];
			
			engadirLog("O xogador "+xog1.getNome()+" escolleu a fila "+(escollaXog1+1)+".", 2);
			engadirLog("O xogador "+xog2.getNome()+" escolleu a columna "+(escollaXog2+1)+".", 2);
			
			//Engadimos o payoff correspontente.
			xog1.addPagoParcial(pagoXog1);
			xog2.addPagoParcial(pagoXog2);
			
			//Comprobamos quen gañou a ronda.
			if(pagoXog1>pagoXog2) {
				engadirLog("O xogador "+xog1.getNome()+" gañou a ronda con resultado "+pagoXog1+" - "+pagoXog2, 2);
				xog1.addRondaGanhada();
				xog2.addRondaPerdida();
			}
			else if(pagoXog1<pagoXog2) {
				engadirLog("O xogador "+xog2.getNome()+" gañou a ronda con resultado "+pagoXog1+" - "+pagoXog2, 2);
				xog1.addRondaPerdida();
				xog2.addRondaGanhada();
			}
			else {
				engadirLog("Os xogadores "+xog1.getNome()+" e "+xog2.getNome()+" empataron a ronda con resultado "+pagoXog1+" - "+pagoXog2, 2);
			}
			
			//Informamos do resultado os xogadores
			arbitro.informarResultados(xog1, xog2, escollaXog1, escollaXog2, pagoXog1, pagoXog2);
			
			//Comprobamos se debemos actualizar a matriz.
			cont++;
			if(cont>=numCambMatriz && numCambMatriz>0) {
				cambiarMatriz(porCambMatriz);
				arbitro.menCambiaMatriz(xog1, xog2, porCambMatriz);
				cont=0;
			}
			
			numRondasXog++;
			actualizarDatosRondas(xog1, xog2);
		}
		
		engadirLog("Partida rematada", 1);
		
		//Comprobamos quen gañou a partida.
		if(xog1.getPagoParcial()>xog2.getPagoParcial()) {
			engadirLog("O xogador "+xog1.getNome()+" gañou a partida por "+xog1.getPagoParcial()+" - "+xog2.getPagoParcial(), 1);
			xog1.addPartidaGanhada();
			xog2.addPartidaPerdida();
		}
		else if(xog1.getPagoParcial()<xog2.getPagoParcial()) {
			engadirLog("O xogador "+xog2.getNome()+" gañou a partida por "+xog1.getPagoParcial()+" - "+xog2.getPagoParcial(), 1);
			xog1.addPartidaPerdida();
			xog2.addPartidaGanhada();
		}
		else {
			engadirLog("Os xogadores "+xog1.getNome()+" e "+xog2.getNome()+" empataron a partida "+xog1.getRondasGanhadas()+" - "+xog2.getRondasGanhadas(), 1);
		}
		
		arbitro.finPartida(xog1, xog2);
		
		//Recalculamos a matriz e reiniciamos os parámetros de partida dos xogadores.
		cambiarMatriz(100);
		xog1.finPartida();
		xog2.finPartida();
		numRondasXog = 0;
	}//novaPartida
    
	/**
	 * Activa a variable de pausa de xogo, modifica a ventá para permitir un start.
	 * @param print Mostrar mensaxe de log
	 */
	public synchronized void pause(boolean print) {
		x.ButStartStop.setLabel("Start");
 		x.ButStartStop.setActionCommand("start");
 		x.MenStartStop.setActionCommand("start");
 		if(print)engadirLog("Xogo pausado", 0);
		xogoPausado=true;
	}//pause
	
	/**
	 * Desactiva a variable de pausa de xogo, volve iniciar o xogo e modifica a ventá para permitir un pause.
	 * @param print Mostrar mensaxe de log
	 */
	public synchronized void resume(boolean print) {
		x.ButStartStop.setLabel("Stop");
 		x.ButStartStop.setActionCommand("stop");
 		x.MenStartStop.setActionCommand("stop");
 		if(print) engadirLog("Xogo Iniciado", 0);
		xogoPausado=false;
		notify();
	}//resume
    
	@Override
	/**
	 * Función do xogo en un Thread a parte.
	 * Realiza os bucles para as diferentes partidas entre os xogadores e calcula quen gañou o xogo ó final.
	 */
	public void run() {
		
 		if(xogadores.size()<2)
 			return;
 		
		engadirLog("COMEZA UN NOVO XOGO", 1);
 		
		xogoIniciado=true;
		
		resume(false);
		
		reiniciarXogadores();
		arbitro.mensaxeNovoXogo();
		
		
		//Realizamos os bucles de partidas entre xogadores.
		for(int i=0; i<xogadores.size(); i++) {
			for(int j=i+1; j<xogadores.size(); j++) {
				//actualizarDatosPartida(xogadores.get(i), xogadores.get(j));
				novaPartida(xogadores.get(i), xogadores.get(j));
				actualizarDatosPartida(xogadores.get(i), xogadores.get(j));
			}
		}
		
		@SuppressWarnings("unchecked")
		//Copiamos a lista de xogadores para reordenala según o número de partidas gañadas.
		ArrayList<psi29_DatosXogador> xog_temp =  (ArrayList<psi29_DatosXogador>) xogadores.clone();
		Collections.sort(xog_temp, Collections.reverseOrder());
		psi29_DatosXogador gañador = xog_temp.get(0);
		engadirLog("O Xogador "+gañador.getNome()+" gañou o Xogo.",1);
		engadirLog("XOGO REMATADO\n",1);
		
//		String elementos="";
//		for(int i=0; i<xog_temp.size() && i<3; i++)
//			elementos+="<li>"+xog_temp.get(i).getNome()+"</li>";
			
		x.mostrarMensaxeInfo("O gañador é o xogador "+gañador.getNome()+"("+gañador.getId()+") con "+gañador.getPartidasGañadas()+" victorias, "+gañador.getPartidasPerdidas()+" derrotas e un payoff de "+gañador.getPagoTotal()+".", "Fin");
		
		xog_temp.clear();
			
		pause(false);
		xogoIniciado=false;
		
	}//run
	
 }//Clase Xogo