package TFG_Ejecutable;

import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parse_HTML {

	//Instanciamos el framework que obtiene resolución de mi pantalla
	Toolkit t = Toolkit.getDefaultToolkit();
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**MÉTODO DE APERTURA DEL NAVEGADOR Y PÁGINA WEB*/
	public static void metodoOpenURL(String url) {
		String nombreSistemaOperativo = System.getProperty("os.name");
	    try {
	        if (nombreSistemaOperativo.startsWith("Windows")) {
	            Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + url);
	        } 
	        else{ 
	        	if (nombreSistemaOperativo.startsWith("Mac OS X")) {
	        		Runtime.getRuntime().exec("Abrir " + url);
	        	} 
	        	else {
	        		System.out.println("Por favor, abrir un navegador e ir a "+ url);
	        	}
	    	}
	    } 
		catch (IOException e) {
	        System.out.println("Failed to start a browser to open the url " + url);
	        e.printStackTrace();
	    }
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**MÉTODO QUE OBTIENE RESOLUCIÓN DE TU PANTALLA*/
	public static int [] metodoObtenerDimensiones(){
		//Utilizo un método de Toolkit para obtener dimensiones
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//El objeto Dimension almacena simensiones de objetos de AWT
		int ancho = screenSize.width;
		int alto = screenSize.height;
		int []arrayAncho_x_Alto = {ancho, alto};
		
		return arrayAncho_x_Alto;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**MÉTODO LECTURA DE FICHERO HTML*/
	public static String metodoLecturaArchivo(String archivo) throws FileNotFoundException, IOException {
	    String cadena="";							//"variable" que contine lo que se lea
	    String cadena_aux;
	    FileReader f = new FileReader(archivo);	//"lector de fichero" que contiene el "fichero" para leer
	    BufferedReader b = new BufferedReader(f);	//"buffer de lectura" que contiene al "lector de fichero"
	    while((cadena_aux = b.readLine())!=null) {	//while que mantenemos mientas hayan líneas que leer del buffer
	    	  cadena=cadena+cadena_aux;
	    }
	    b.close();
	    return cadena;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**MÉTODO QUE CUENTA Nº VECES QUE APARECE UNA SUBCADENA EN UN TEXTO*/
	public static int contarIteracionesSubcadena(String texto, String subcadena) {
        int posicion, contador = 0;
        //se busca la primera vez que aparece
        posicion = texto.indexOf(subcadena);
        while (posicion != -1) { //mientras se encuentre el caracter
            contador++;           //se cuenta
            //se sigue buscando a partir de la posición siguiente a la encontrada
            posicion = texto.indexOf(subcadena, posicion + 1);
        }
        return contador;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**MÉTODO QUE DETECTA NOTICIAS FUNDAMENTALES*/
	public static String[] metodoDetectaNoticiasFundamentales(String HTMLentrante){
		String cadenaParaImprimir="";
		int indiceMaxImportancia=0;
		int indiceVARIABLE=0;
		int indicePrevMaxImportancia_Aux=0;
		int indicePrevMaxImportancia=0;
		int cantidadNoticiasMaxImportancia=0;
		
		/**BUSCAMOS LAS NOTICIAS DE MÁXIMA IMPORTANCIA DEL USD*/
		//Lo primero es preguntar cuantas noticias del USD hay y lengh de su definición en HTML
		int numeroIteraciones=contarIteracionesSubcadena(HTMLentrante, "<td class=\"left flagCur noWrap\"><span title=\"Estados Unidos\" class=\"ceFlags United_States\" data-img_key=\"United_States\">&nbsp;</span> USD</td>");
//		cadenaParaImprimir+="-->USD "+numeroIteraciones+" noticias. \n -----------------------\n";
//		System.out.println("-->USD "+numeroIteraciones+" noticias. \n -----------------------");
		int longitudStringBandera = "<td class=\"left flagCur noWrap\"><span title=\"Estados Unidos\" class=\"ceFlags United_States\" data-img_key=\"United_States\">&nbsp;</span> USD</td>".length();
		//Vamos iterando y comprobando si hay alguna de ellas con MAXIMA importancia
		for(int i=0; i<numeroIteraciones; i++){
			//Guardo primera posición del HTML q especifica la columna de la bsndera
			indicePrevMaxImportancia=HTMLentrante.indexOf("<td class=\"left flagCur noWrap\"><span title=\"Estados Unidos\" class=\"ceFlags United_States\" data-img_key=\"United_States\">&nbsp;</span> USD</td>", indicePrevMaxImportancia_Aux);
			//Ahora calculo el índice donde acaba la cadena y lo guardo en lugar del 1º
			indicePrevMaxImportancia+=longitudStringBandera;
			//Guardo primera posicion del HTML de la columna de importancia
			indiceMaxImportancia=HTMLentrante.indexOf("<td class=\"left textNum sentiment noWrap\" title=\"Alta volatilidad esperada\" data-img_key=\"bull3\"><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i></td>", indicePrevMaxImportancia);
			int diferencia = indiceMaxImportancia - indicePrevMaxImportancia;
			if (diferencia<30 && indiceMaxImportancia!=-1){	//Si entro es xk es noticia de MAX import. en USD
				//Estoy ante una noticia de USD de máxima importancia
				indicePrevMaxImportancia_Aux=indicePrevMaxImportancia;	//Guardo este indice para proxima busqueda
				indiceVARIABLE=indiceMaxImportancia-18;	//Nos colocamos a la izq de 1ª letra del nombre de la moneda
				char caracter=HTMLentrante.charAt(indiceVARIABLE);	//Apunta a la 1ªletra de la divisa
				//Nos movemos a la izquierda hasta llegar a la columna horaria
				indiceVARIABLE= indicePrevMaxImportancia - 30;	//Me posiciono en un caracter de la HORA
				caracter=HTMLentrante.charAt(indiceVARIABLE);	//Devuelve el caracter de dicho índice
				indiceVARIABLE = HTMLentrante.lastIndexOf("m\">", indiceVARIABLE) +3;//Me coloco 3 caracteres antes de la hora desde donde estoy
				//Voy guardando los carcteres que indican la HORA de mi noticia
				char caracter1=HTMLentrante.charAt(indiceVARIABLE);		//Hora
				char caracter2=HTMLentrante.charAt(indiceVARIABLE+1);	//Hora
				char caracter3=HTMLentrante.charAt(indiceVARIABLE+2);	//:
				char caracter4=HTMLentrante.charAt(indiceVARIABLE+3);	//Min
				char caracter5=HTMLentrante.charAt(indiceVARIABLE+4);	//Min
				//Paso esta hora dada en CARACTERES a String
				String string1 = ""+caracter1;
				String string2 = ""+caracter2;
				String string3 = ""+caracter3;
				String string4 = ""+caracter4;
				String string5 = ""+caracter5;
				//Paso hora y minutos de String a Int
				String stringHora	= string1 + string2;
				String stringMinutos= string4 + string5;
				int hora= Integer.parseInt(stringHora);
				int minutos = Integer.parseInt(stringMinutos);
				//Comprobaciones en consola
				cadenaParaImprimir+="USD max. importancia a las "+string1+string2+string3+string4+string5+",";
				cantidadNoticiasMaxImportancia++;
			}
			indicePrevMaxImportancia_Aux = indicePrevMaxImportancia;
		}

		/**BUSCAMOS LAS NOTICIAS DE MÁXIMA IMPORTANCIA DEL EUR alemania*/
		indiceMaxImportancia=0;
		indiceVARIABLE=0;
		indicePrevMaxImportancia_Aux=0;
		indicePrevMaxImportancia=0;
		//Lo primero es preguntar cuantas noticias del USD hay y lengh de su definición en HTML
		numeroIteraciones=contarIteracionesSubcadena(HTMLentrante, "<td class=\"left flagCur noWrap\"><span title=\"Alemania\" class=\"ceFlags Germany\" data-img_key=\"Germany\">&nbsp;</span> EUR</td>");
//		cadenaParaImprimir+="-->EUR Alemania "+numeroIteraciones+" noticias. \n -----------------------\n";
//		System.out.println("-->EUR Alemania "+numeroIteraciones+" noticias. \n -----------------------");
		longitudStringBandera = "<td class=\"left flagCur noWrap\"><span title=\"Alemania\" class=\"ceFlags Germany\" data-img_key=\"Germany\">&nbsp;</span> EUR</td>".length();
		//Vamos iterando y comprobando si hay alguna de ellas con MAXIMA importancia
		for(int i=0; i<numeroIteraciones; i++){
			//Guardo primera posición del HTML q especifica la columna de la bsndera
			indicePrevMaxImportancia=HTMLentrante.indexOf("<td class=\"left flagCur noWrap\"><span title=\"Alemania\" class=\"ceFlags Germany\" data-img_key=\"Germany\">&nbsp;</span> EUR</td>", indicePrevMaxImportancia_Aux);
			//Ahora calculo el índice donde acaba la cadena y lo guardo en lugar del 1º
			indicePrevMaxImportancia+=longitudStringBandera;
			//Guardo primera posicion del HTML de la columna de importancia
			indiceMaxImportancia=HTMLentrante.indexOf("<td class=\"left textNum sentiment noWrap\" title=\"Alta volatilidad esperada\" data-img_key=\"bull3\"><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i></td>", indicePrevMaxImportancia);
			int diferencia = indiceMaxImportancia - indicePrevMaxImportancia;
			if (diferencia<30 && indiceMaxImportancia!=-1){	//Si entro es xk es noticia de MAX import. en USD
				//Estoy ante una noticia de USD de máxima importancia
				indicePrevMaxImportancia_Aux=indicePrevMaxImportancia;	//Guardo este indice para proxima busqueda
				indiceVARIABLE=indiceMaxImportancia-18;	//Nos colocamos a la izq de 1ª letra del nombre de la moneda
				char caracter=HTMLentrante.charAt(indiceVARIABLE);	//Apunta a la 1ªletra de la divisa
				//Nos movemos a la izquierda hasta llegar a la columna horaria
				indiceVARIABLE= indicePrevMaxImportancia - 30;	//Me posiciono en un caracter de la HORA
				caracter=HTMLentrante.charAt(indiceVARIABLE);	//Devuelve el caracter de dicho índice
				indiceVARIABLE = HTMLentrante.lastIndexOf("m\">", indiceVARIABLE) +3;//Me coloco 3 caracteres antes de la hora desde donde estoy
				//Voy guardando los carcteres que indican la HORA de mi noticia
				char caracter1=HTMLentrante.charAt(indiceVARIABLE);		//Hora
				char caracter2=HTMLentrante.charAt(indiceVARIABLE+1);	//Hora
				char caracter3=HTMLentrante.charAt(indiceVARIABLE+2);	//:
				char caracter4=HTMLentrante.charAt(indiceVARIABLE+3);	//Min
				char caracter5=HTMLentrante.charAt(indiceVARIABLE+4);	//Min
				//Paso esta hora dada en CARACTERES a String
				String string1 = ""+caracter1;
				String string2 = ""+caracter2;
				String string3 = ""+caracter3;
				String string4 = ""+caracter4;
				String string5 = ""+caracter5;
				//Paso hora y minutos de String a Int
				String stringHora	= string1 + string2;
				String stringMinutos= string4 + string5;
				int hora= Integer.parseInt(stringHora);
				int minutos = Integer.parseInt(stringMinutos);
				//Comprobaciones en consola
				cadenaParaImprimir+="EUR max. importancia a las "+string1+string2+string3+string4+string5+",";
				cantidadNoticiasMaxImportancia++;
			}
			indicePrevMaxImportancia_Aux = indicePrevMaxImportancia;
		}
		
		/**BUSCAMOS LAS NOTICIAS DE MÁXIMA IMPORTANCIA DEL EUR Eurozona*/
		indiceMaxImportancia=0;
		indiceVARIABLE=0;
		indicePrevMaxImportancia_Aux=0;
		indicePrevMaxImportancia=0;
		//Lo primero es preguntar cuantas noticias del USD hay y lengh de su definición en HTML
		numeroIteraciones=contarIteracionesSubcadena(HTMLentrante, "<td class=\"left flagCur noWrap\"><span title=\"Eurozona\" class=\"ceFlags Europe\" data-img_key=\"Europe\">&nbsp;</span> EUR</td>");
//		cadenaParaImprimir+="-->EUR Eurozona "+numeroIteraciones+" noticias. \n -----------------------\n";
//		System.out.println("-->EUR Eurozona "+numeroIteraciones+" noticias. \n -----------------------");
		longitudStringBandera = "<td class=\"left flagCur noWrap\"><span title=\"Eurozona\" class=\"ceFlags Europe\" data-img_key=\"Germany\">&nbsp;</span> EUR</td>".length();
		//Vamos iterando y comprobando si hay alguna de ellas con MAXIMA importancia
		for(int i=0; i<numeroIteraciones; i++){
			//Guardo primera posición del HTML q especifica la columna de la bsndera
			indicePrevMaxImportancia=HTMLentrante.indexOf("<td class=\"left flagCur noWrap\"><span title=\"Eurozona\" class=\"ceFlags Europe\" data-img_key=\"Europe\">&nbsp;</span> EUR</td>", indicePrevMaxImportancia_Aux);
			//Ahora calculo el índice donde acaba la cadena y lo guardo en lugar del 1º
			indicePrevMaxImportancia+=longitudStringBandera;
			//Guardo primera posicion del HTML de la columna de importancia
			indiceMaxImportancia=HTMLentrante.indexOf("<td class=\"left textNum sentiment noWrap\" title=\"Alta volatilidad esperada\" data-img_key=\"bull3\"><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i></td>", indicePrevMaxImportancia);
			int diferencia = indiceMaxImportancia - indicePrevMaxImportancia;
			if (diferencia<30 && indiceMaxImportancia!=-1){	//Si entro es xk es noticia de MAX import. en USD
				//Estoy ante una noticia de USD de máxima importancia
				indicePrevMaxImportancia_Aux=indicePrevMaxImportancia;	//Guardo este indice para proxima busqueda
				indiceVARIABLE=indiceMaxImportancia-18;	//Nos colocamos a la izq de 1ª letra del nombre de la moneda
				char caracter=HTMLentrante.charAt(indiceVARIABLE);	//Apunta a la 1ªletra de la divisa
				//Nos movemos a la izquierda hasta llegar a la columna horaria
				indiceVARIABLE= indicePrevMaxImportancia - 30;	//Me posiciono en un caracter de la HORA
				caracter=HTMLentrante.charAt(indiceVARIABLE);	//Devuelve el caracter de dicho índice
				indiceVARIABLE = HTMLentrante.lastIndexOf("m\">", indiceVARIABLE) +3;//Me coloco 3 caracteres antes de la hora desde donde estoy
				//Voy guardando los carcteres que indican la HORA de mi noticia
				char caracter1=HTMLentrante.charAt(indiceVARIABLE);		//Hora
				char caracter2=HTMLentrante.charAt(indiceVARIABLE+1);	//Hora
				char caracter3=HTMLentrante.charAt(indiceVARIABLE+2);	//:
				char caracter4=HTMLentrante.charAt(indiceVARIABLE+3);	//Min
				char caracter5=HTMLentrante.charAt(indiceVARIABLE+4);	//Min
				//Paso esta hora dada en CARACTERES a String
				String string1 = ""+caracter1;
				String string2 = ""+caracter2;
				String string3 = ""+caracter3;
				String string4 = ""+caracter4;
				String string5 = ""+caracter5;
				//Paso hora y minutos de String a Int
				String stringHora	= string1 + string2;
				String stringMinutos= string4 + string5;
				int hora= Integer.parseInt(stringHora);
				int minutos = Integer.parseInt(stringMinutos);
				//Comprobaciones en consola
				cadenaParaImprimir+="EUR max. importancia a las "+string1+string2+string3+string4+string5+",";
				cantidadNoticiasMaxImportancia++;
			}
			indicePrevMaxImportancia_Aux = indicePrevMaxImportancia;
		}
		/**BUSCAMOS LAS NOTICIAS DE MÁXIMA IMPORTANCIA DEL EUR Francia*/
		indiceMaxImportancia=0;
		indiceVARIABLE=0;
		indicePrevMaxImportancia_Aux=0;
		indicePrevMaxImportancia=0;
		//Lo primero es preguntar cuantas noticias del USD hay y lengh de su definición en HTML
		numeroIteraciones=contarIteracionesSubcadena(HTMLentrante, "<td class=\"left flagCur noWrap\"><span title=\"Francia\" class=\"ceFlags France\" data-img_key=\"France\">&nbsp;</span> EUR</td>");
//		cadenaParaImprimir+="-->EUR Francia "+numeroIteraciones+" noticias. \n -----------------------\n";
//		System.out.println("-->EUR Francia "+numeroIteraciones+" noticias. \n -----------------------");
		longitudStringBandera = "<td class=\"left flagCur noWrap\"><span title=\"Francia\" class=\"ceFlags France\" data-img_key=\"France\">&nbsp;</span> EUR</td>".length();
		//Vamos iterando y comprobando si hay alguna de ellas con MAXIMA importancia
		for(int i=0; i<numeroIteraciones; i++){
			//Guardo primera posición del HTML q especifica la columna de la bsndera
			indicePrevMaxImportancia=HTMLentrante.indexOf("<td class=\"left flagCur noWrap\"><span title=\"Francia\" class=\"ceFlags France\" data-img_key=\"France\">&nbsp;</span> EUR</td>", indicePrevMaxImportancia_Aux);
			//Ahora calculo el índice donde acaba la cadena y lo guardo en lugar del 1º
			indicePrevMaxImportancia+=longitudStringBandera;
			//Guardo primera posicion del HTML de la columna de importancia
			indiceMaxImportancia=HTMLentrante.indexOf("<td class=\"left textNum sentiment noWrap\" title=\"Alta volatilidad esperada\" data-img_key=\"bull3\"><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i></td>", indicePrevMaxImportancia);
			int diferencia = indiceMaxImportancia - indicePrevMaxImportancia;
			if (diferencia<30 && indiceMaxImportancia!=-1){	//Si entro es xk es noticia de MAX import. en EUR
				//Estoy ante una noticia de USD de máxima importancia
				indicePrevMaxImportancia_Aux=indicePrevMaxImportancia;	//Guardo este indice para proxima busqueda
				indiceVARIABLE=indiceMaxImportancia-18;	//Nos colocamos a la izq de 1ª letra del nombre de la moneda
				char caracter=HTMLentrante.charAt(indiceVARIABLE);	//Apunta a la 1ªletra de la divisa
				//Nos movemos a la izquierda hasta llegar a la columna horaria
				indiceVARIABLE= indicePrevMaxImportancia - 30;	//Me posiciono en un caracter de la HORA
				caracter=HTMLentrante.charAt(indiceVARIABLE);	//Devuelve el caracter de dicho índice
				indiceVARIABLE = HTMLentrante.lastIndexOf("m\">", indiceVARIABLE) +3;//Me coloco 3 caracteres antes de la hora desde donde estoy
				//Voy guardando los carcteres que indican la HORA de mi noticia
				char caracter1=HTMLentrante.charAt(indiceVARIABLE);		//Hora
				char caracter2=HTMLentrante.charAt(indiceVARIABLE+1);	//Hora
				char caracter3=HTMLentrante.charAt(indiceVARIABLE+2);	//:
				char caracter4=HTMLentrante.charAt(indiceVARIABLE+3);	//Min
				char caracter5=HTMLentrante.charAt(indiceVARIABLE+4);	//Min
				//Paso esta hora dada en CARACTERES a String
				String string1 = ""+caracter1;
				String string2 = ""+caracter2;
				String string3 = ""+caracter3;
				String string4 = ""+caracter4;
				String string5 = ""+caracter5;
				//Paso hora y minutos de String a Int
				String stringHora	= string1 + string2;
				String stringMinutos= string4 + string5;
				int hora= Integer.parseInt(stringHora);
				int minutos = Integer.parseInt(stringMinutos);
				//Comprobaciones en consola
				cadenaParaImprimir+="EUR max. importancia a las "+string1+string2+string3+string4+string5+",";	
				cantidadNoticiasMaxImportancia++;
			}
			indicePrevMaxImportancia_Aux = indicePrevMaxImportancia;
		}
		
		/**BUSCAMOS LAS NOTICIAS DE MÁXIMA IMPORTANCIA DEL EUR Italia*/
		indiceMaxImportancia=0;
		indiceVARIABLE=0;
		indicePrevMaxImportancia_Aux=0;
		indicePrevMaxImportancia=0;
		//Lo primero es preguntar cuantas noticias del USD hay y lengh de su definición en HTML
		numeroIteraciones=contarIteracionesSubcadena(HTMLentrante, "<td class=\"left flagCur noWrap\"><span title=\"Italia\" class=\"ceFlags Italy\" data-img_key=\"Italy\">&nbsp;</span> EUR</td>");
//		cadenaParaImprimir+="-->EUR Italia "+numeroIteraciones+" noticias. \n -----------------------\n";
//		System.out.println("-->EUR Italia "+numeroIteraciones+" noticias. \n -----------------------");
		longitudStringBandera = "<td class=\"left flagCur noWrap\"><span title=\"Italia\" class=\"ceFlags Italy\" data-img_key=\"Italy\">&nbsp;</span> EUR</td>".length();
		//Vamos iterando y comprobando si hay alguna de ellas con MAXIMA importancia
		for(int i=0; i<numeroIteraciones; i++){
			//Guardo primera posición del HTML q especifica la columna de la bsndera
			indicePrevMaxImportancia=HTMLentrante.indexOf("<td class=\"left flagCur noWrap\"><span title=\"Francia\" class=\"ceFlags France\" data-img_key=\"France\">&nbsp;</span> EUR</td>", indicePrevMaxImportancia_Aux);
			//Ahora calculo el índice donde acaba la cadena y lo guardo en lugar del 1º
			indicePrevMaxImportancia+=longitudStringBandera;
			//Guardo primera posicion del HTML de la columna de importancia
			indiceMaxImportancia=HTMLentrante.indexOf("<td class=\"left textNum sentiment noWrap\" title=\"Alta volatilidad esperada\" data-img_key=\"bull3\"><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i></td>", indicePrevMaxImportancia);
			int diferencia = indiceMaxImportancia - indicePrevMaxImportancia;
			if (diferencia<30 && indiceMaxImportancia!=-1){	//Si entro es xk es noticia de MAX import. en EUR
				//Estoy ante una noticia de USD de máxima importancia
				indicePrevMaxImportancia_Aux=indicePrevMaxImportancia;	//Guardo este indice para proxima busqueda
				indiceVARIABLE=indiceMaxImportancia-18;	//Nos colocamos a la izq de 1ª letra del nombre de la moneda
				char caracter=HTMLentrante.charAt(indiceVARIABLE);	//Apunta a la 1ªletra de la divisa
				//Nos movemos a la izquierda hasta llegar a la columna horaria
				indiceVARIABLE= indicePrevMaxImportancia - 30;	//Me posiciono en un caracter de la HORA
				caracter=HTMLentrante.charAt(indiceVARIABLE);	//Devuelve el caracter de dicho índice
				indiceVARIABLE = HTMLentrante.lastIndexOf("m\">", indiceVARIABLE) +3;//Me coloco 3 caracteres antes de la hora desde donde estoy
				//Voy guardando los carcteres que indican la HORA de mi noticia
				char caracter1=HTMLentrante.charAt(indiceVARIABLE);		//Hora
				char caracter2=HTMLentrante.charAt(indiceVARIABLE+1);	//Hora
				char caracter3=HTMLentrante.charAt(indiceVARIABLE+2);	//:
				char caracter4=HTMLentrante.charAt(indiceVARIABLE+3);	//Min
				char caracter5=HTMLentrante.charAt(indiceVARIABLE+4);	//Min
				//Paso esta hora dada en CARACTERES a String
				String string1 = ""+caracter1;
				String string2 = ""+caracter2;
				String string3 = ""+caracter3;
				String string4 = ""+caracter4;
				String string5 = ""+caracter5;
				//Paso hora y minutos de String a Int
				String stringHora	= string1 + string2;
				String stringMinutos= string4 + string5;
				int hora= Integer.parseInt(stringHora);
				int minutos = Integer.parseInt(stringMinutos);
				//Comprobaciones en consola
				cadenaParaImprimir+="EUR max. importancia a las "+string1+string2+string3+string4+string5+",";
				cantidadNoticiasMaxImportancia++;
			}
			indicePrevMaxImportancia_Aux = indicePrevMaxImportancia;
		}
		
		/**BUSCAMOS LAS NOTICIAS DE MÁXIMA IMPORTANCIA DEL JPY*/
		indiceMaxImportancia=0;
		indiceVARIABLE=0;
		indicePrevMaxImportancia_Aux=0;
		indicePrevMaxImportancia=0;
		//Lo primero es preguntar cuantas noticias del USD hay y lengh de su definición en HTML
		numeroIteraciones=contarIteracionesSubcadena(HTMLentrante, "<td class=\"left flagCur noWrap\"><span title=\"Jap");
//		cadenaParaImprimir+="-->JPY "+numeroIteraciones+" noticias. \n -----------------------\n";
//		System.out.println("-->JPY "+numeroIteraciones+" noticias. \n -----------------------");
		longitudStringBandera = "<td class=\"left flagCur noWrap\"><span title=\"Japón\" class=\"ceFlags Japan\" data-img_key=\"Japan\">&nbsp;</span> JPY</td>".length();
		//Vamos iterando y comprobando si hay alguna de ellas con MAXIMA importancia
		for(int i=0; i<numeroIteraciones; i++){
			//Guardo primera posición del HTML q especifica la columna de la bsndera
			indicePrevMaxImportancia=HTMLentrante.indexOf("<td class=\"left flagCur noWrap\"><span title=\"Jap", indicePrevMaxImportancia_Aux);
			//Ahora calculo el índice donde acaba la cadena y lo guardo en lugar del 1º
			indicePrevMaxImportancia+=longitudStringBandera;
			//Guardo primera posicion del HTML de la columna de importancia
			indiceMaxImportancia=HTMLentrante.indexOf("<td class=\"left textNum sentiment noWrap\" title=\"Alta volatilidad esperada\" data-img_key=\"bull3\"><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i></td>", indicePrevMaxImportancia);
			int diferencia = indiceMaxImportancia - indicePrevMaxImportancia;
			if (diferencia<30 && indiceMaxImportancia!=-1){	//Si entro es xk es noticia de MAX import. en USD
				//Estoy ante una noticia de USD de máxima importancia
				indicePrevMaxImportancia_Aux=indicePrevMaxImportancia;	//Guardo este indice para proxima busqueda
				indiceVARIABLE=indiceMaxImportancia-18;	//Nos colocamos a la izq de 1ª letra del nombre de la moneda
				char caracter=HTMLentrante.charAt(indiceVARIABLE);	//Apunta a la 1ªletra de la divisa
				//Nos movemos a la izquierda hasta llegar a la columna horaria
				indiceVARIABLE= indicePrevMaxImportancia - 30;	//Me posiciono en un caracter de la HORA
				caracter=HTMLentrante.charAt(indiceVARIABLE);	//Devuelve el caracter de dicho índice
				indiceVARIABLE = HTMLentrante.lastIndexOf("m\">", indiceVARIABLE) +3;//Me coloco 3 caracteres antes de la hora desde donde estoy
				//Voy guardando los carcteres que indican la HORA de mi noticia
				char caracter1=HTMLentrante.charAt(indiceVARIABLE);		//Hora
				char caracter2=HTMLentrante.charAt(indiceVARIABLE+1);	//Hora
				char caracter3=HTMLentrante.charAt(indiceVARIABLE+2);	//:
				char caracter4=HTMLentrante.charAt(indiceVARIABLE+3);	//Min
				char caracter5=HTMLentrante.charAt(indiceVARIABLE+4);	//Min
				//Paso esta hora dada en CARACTERES a String
				String string1 = ""+caracter1;
				String string2 = ""+caracter2;
				String string3 = ""+caracter3;
				String string4 = ""+caracter4;
				String string5 = ""+caracter5;
				//Paso hora y minutos de String a Int
				String stringHora	= string1 + string2;
				String stringMinutos= string4 + string5;
				int hora= Integer.parseInt(stringHora);
				int minutos = Integer.parseInt(stringMinutos);
				//Comprobaciones en consola
				cadenaParaImprimir+="JPY max. importancia a las "+string1+string2+string3+string4+string5+",";
				cantidadNoticiasMaxImportancia++;
			}
			indicePrevMaxImportancia_Aux = indicePrevMaxImportancia;
		}
		
		/**BUSCAMOS LAS NOTICIAS DE MÁXIMA IMPORTANCIA DEL GBP*/
		indiceMaxImportancia=0;
		indiceVARIABLE=0;
		indicePrevMaxImportancia_Aux=0;
		indicePrevMaxImportancia=0;
		//Lo primero es preguntar cuantas noticias del USD hay y lengh de su definición en HTML
		numeroIteraciones=contarIteracionesSubcadena(HTMLentrante, "<td class=\"left flagCur noWrap\"><span title=\"Reino Unido\" class=\"ceFlags United_Kingdom\" data-img_key=\"United_Kingdom\">&nbsp;</span> GBP</td>");
//		cadenaParaImprimir+="-->GBP "+numeroIteraciones+" noticias. \n -----------------------\n";
//		System.out.println("-->GBP "+numeroIteraciones+" noticias. \n -----------------------");
		longitudStringBandera = "<td class=\"left flagCur noWrap\"><span title=\"Reino Unido\" class=\"ceFlags United_Kingdom\" data-img_key=\"United_Kingdom\">&nbsp;</span> GBP</td>".length();
		//Vamos iterando y comprobando si hay alguna de ellas con MAXIMA importancia
		for(int i=0; i<numeroIteraciones; i++){
			//Guardo primera posición del HTML q especifica la columna de la bsndera
			indicePrevMaxImportancia=HTMLentrante.indexOf("<td class=\"left flagCur noWrap\"><span title=\"Reino Unido\" class=\"ceFlags United_Kingdom\" data-img_key=\"United_Kingdom\">&nbsp;</span> GBP</td>", indicePrevMaxImportancia_Aux);
			//Ahora calculo el índice donde acaba la cadena y lo guardo en lugar del 1º
			indicePrevMaxImportancia+=longitudStringBandera;
			//Guardo primera posicion del HTML de la columna de importancia
			indiceMaxImportancia=HTMLentrante.indexOf("<td class=\"left textNum sentiment noWrap\" title=\"Alta volatilidad esperada\" data-img_key=\"bull3\"><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i><i class=\"grayFullBullishIcon\"></i></td>", indicePrevMaxImportancia);
			int diferencia = indiceMaxImportancia - indicePrevMaxImportancia;
			if (diferencia<30 && indiceMaxImportancia!=-1){	//Si entro es xk es noticia de MAX import. en USD
				//Estoy ante una noticia de USD de máxima importancia
				indicePrevMaxImportancia_Aux=indicePrevMaxImportancia;	//Guardo este indice para proxima busqueda
				indiceVARIABLE=indiceMaxImportancia-30;	//Nos colocamos a la izq de 1ª letra del nombre de la moneda
				char caracter=HTMLentrante.charAt(indiceVARIABLE);	//Apunta a la 1ªletra de la divisa
				//Nos movemos a la izquierda hasta llegar a la columna horaria
				indiceVARIABLE= indicePrevMaxImportancia - 30;	//Me posiciono en un caracter de la HORA
				caracter=HTMLentrante.charAt(indiceVARIABLE);	//Devuelve el caracter de dicho índice
				indiceVARIABLE = HTMLentrante.lastIndexOf("m\">", indiceVARIABLE) +3;//Me coloco 3 caracteres antes de la hora desde donde estoy
				//Voy guardando los carcteres que indican la HORA de mi noticia
				char caracter1=HTMLentrante.charAt(indiceVARIABLE);		//Hora
				char caracter2=HTMLentrante.charAt(indiceVARIABLE+1);	//Hora
				char caracter3=HTMLentrante.charAt(indiceVARIABLE+2);	//:
				char caracter4=HTMLentrante.charAt(indiceVARIABLE+3);	//Min
				char caracter5=HTMLentrante.charAt(indiceVARIABLE+4);	//Min
				//Paso esta hora dada en CARACTERES a String
				String string1 = ""+caracter1;
				String string2 = ""+caracter2;
				String string3 = ""+caracter3;
				String string4 = ""+caracter4;
				String string5 = ""+caracter5;
				//Paso hora y minutos de String a Int
				String stringHora	= string1 + string2;
				String stringMinutos= string4 + string5;
				int hora= Integer.parseInt(stringHora);
				int minutos = Integer.parseInt(stringMinutos);
				//Comprobaciones en consola
				cadenaParaImprimir+="GBP max. importancia a las "+string1+string2+string3+string4+string5+",";	
				cantidadNoticiasMaxImportancia++;
			}
			indicePrevMaxImportancia_Aux = indicePrevMaxImportancia;
		}	
		
		/**Ahora tenemos un String con todas las noticias concatenadas y además el Nº de noticias que son.
		 * Meter en un Array 1º en Nº y después los String separadamente
		 * */
		System.out.print(cadenaParaImprimir);
		String numString =Integer.toString(cantidadNoticiasMaxImportancia);
		String []arrayDevuelto={numString, cadenaParaImprimir};	//El String de noticias va separado por ","	para deserializarlo en recepción 
	
		
		//Eliminamos el fichero HTML tras acabar su lectura
		File fichero = new File("C:/Users/juan antonio/Downloads/Calendario Económico _ Agenda Económica - Investing.com.html");
		if (fichero.delete())
			System.out.println("El fichero ha sido borrado satisfactoriamente");
		else
			System.out.println("El fichero no puede ser borrado");
		
		
		
		return arrayDevuelto;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String []args) throws FileNotFoundException, InterruptedException, IOException{
		metodoPrincipal();
	}
	
	/**MÉTODO PRINCIPAL DE LA CLASE
	 * @throws IOException 
	 * @throws FileNotFoundException */
	public static String[] metodoPrincipal() throws InterruptedException, FileNotFoundException, IOException{
		//Instancio clase Robot por si la necesito
		Robot robot = null;
	    try{
	    	robot = new Robot();
	    }
	    catch(Exception e){
	    	System.out.println( e.toString() ); 
	    }
		
	    //Abro el navegador y cargo el calendario económico de Investing.com
		metodoOpenURL("http://es.investing.com/economic-calendar/");
		
		//Utilizamos el teclado para descargarnos el archivo HTML de la web
		Thread.currentThread().sleep(10000);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.currentThread().sleep(1000);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		
		//Esperar 10 segundos a la descarga
		Thread.currentThread().sleep(10000);
		
		/**ENTRAR AL FICHERO Y LEERLO, TOMAR NOTICIAS Y SACAR LASA HORAS IMPORTANTES*/
		String cadenaHTMLleido="";
		cadenaHTMLleido=metodoLecturaArchivo("C:/Users/juan antonio/Downloads/Calendario Económico _ Agenda Económica - Investing.com.html");
		String [] textoFundamentales = metodoDetectaNoticiasFundamentales(cadenaHTMLleido);
		
		//Devuelve un array (2,1) Sting: en 1ªposición está el Nº de Noticias y en la 2ªlas noticias separada por ","
		return textoFundamentales;
	}
}