package TFG_Ejecutable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class DecidirOperaciones {

	//Control de conexion con MT4
	static int variableOn_Off = -1;	// -1 equivale a OFF
	static String rutaSincronizacion= "C:/Users/juan antonio/AppData/Roaming/MetaQuotes/Terminal/93231E64ACB41DD159EEC4E406D1AD88/MQL4/Files/";
	
	
	//Control de incio/stop de trading
	static boolean variableEjecucionTrading  = false;	//Default en Stop
	
	//variables de compra y venta hechas al mercado
	static boolean variableEUR_USD_compra_Hecha 	= false;
	static boolean variableEUR_USD_venta_Hecha		= false;
	static boolean variableEUR_GBP_compra_Hecha 	= false;
	static boolean variableEUR_GBP_venta_Hecha		= false;
	static boolean variableEUR_JPY_compra_Hecha 	= false;
	static boolean variableEUR_JPY_venta_Hecha		= false;
	
	
	/**
	 * Método GET de estado ON_OFF MetaTrader4	
	 * */
	public static int getOn_Off(String rutaEntrada){
		rutaSincronizacion=rutaEntrada;
		String rutaFichero1 = rutaSincronizacion + "MACD_EURUSD.txt";
		File fichero1 = new File(rutaFichero1);
		String rutaFichero2 = rutaSincronizacion + "MACD_EURGBP.txt";
		File fichero2 = new File(rutaFichero2);
		String rutaFichero3 = rutaSincronizacion + "MACD_EURJPY.txt";
		File fichero3 = new File(rutaFichero3);
	
		if (fichero1.exists() || fichero2.exists() || fichero3.exists()){
			variableOn_Off = 1;
			variableEjecucionTrading=true;
		}
		else{
			variableOn_Off = -1;
			variableEjecucionTrading=false;
		}
		
		
		return variableOn_Off;
	}
	
	/**
	 * Métodos SET para INICIO/STOP trading	
	 * */
	public static void metodoInciarTrading(){
		variableEjecucionTrading = true;
	}
	public static void metodoStopTrading(){
		variableEjecucionTrading = false;
	}

	
	
	
	/**
	 * MÉTODOS DE LECTURA 
	 * */
	public static String metodo_Lectura_archivo(String archivo) throws FileNotFoundException, IOException {
	    String cadena="";							//"variable" que contine lo que se lea
	    String cadena_aux;
	    FileReader f = new FileReader(archivo);	//"lector de fichero" que contiene el "fichero" para leer
		
	    //Compruebo que el fichero existe y despues si es así lo leo
	    File ficheroPrueba = new File(archivo);
	    if(ficheroPrueba.exists()){	    
	    	BufferedReader b = new BufferedReader(f);	//"buffer de lectura" que contiene al "lector de fichero"
		    while((cadena_aux = b.readLine())!=null) {	//while que mantenemos mientas hayan líneas que leer del buffer
		    	  cadena=cadena_aux;
		    }
		    b.close();
		    return cadena;
		}
	    else{
	    	return "-1";
	    }
	}

	
	/**
	 * MÉTODOS DE ESCRITURA 
	 * */
	//Paso a String los numerales leidos del RSI--- Los archivos de MACD ya están con ROJO o verde por defecto
	public static void metodo_Escritura_RSI_STRING_EURUSD(double decimalInterno) throws IOException {
		String ruta1 = rutaSincronizacion+"RSI_EURUSD_STRING.txt";
        File archivo1 = new File(ruta1);
        BufferedWriter bw1;
        bw1 = new BufferedWriter(new FileWriter(archivo1));
        if(decimalInterno == -1){
        	bw1.write("APAGADO");
        }
        else{
        	if(decimalInterno<=20.00){
	        	bw1.write("SOBRE VENTA");
	        }
	        else{
	        	if(decimalInterno>=80.00){
	        		bw1.write("SOBRE COMPRA");
	        	}
	        	else{
	        		bw1.write("QUIETO");
	        	}
	        }
        }	
        bw1.close();
    }  
	public static void metodo_Escritura_RSI_STRING_EURGBP(double decimalInterno) throws IOException {
		String ruta1 = rutaSincronizacion+"RSI_EURGBP_STRING.txt";
        File archivo1 = new File(ruta1);
        BufferedWriter bw1;
        bw1 = new BufferedWriter(new FileWriter(archivo1));
        if(decimalInterno == -1){
        	bw1.write("APAGADO");
        }
        else{
        	if(decimalInterno<=20.00){
	        	bw1.write("SOBRE VENTA");
	        }
	        else{
	        	if(decimalInterno>=80.00){
	        		bw1.write("SOBRE COMPRA");
	        	}
	        	else{
	        		bw1.write("QUIETO");
	        	}
	        }
        }	
        bw1.close();
    }  
	public static void metodo_Escritura_RSI_STRING_EURJPY(double decimalInterno) throws IOException {
		String ruta1 = rutaSincronizacion+"RSI_EURJPY_STRING.txt";
        File archivo1 = new File(ruta1);
        BufferedWriter bw1;
        bw1 = new BufferedWriter(new FileWriter(archivo1));
        if(decimalInterno == -1){
        	bw1.write("APAGADO");
        }
        else{
        	if(decimalInterno<=20.00){
	        	bw1.write("SOBRE VENTA");
	        }
	        else{
	        	if(decimalInterno>=80.00){
	        		bw1.write("SOBRE COMPRA");
	        	}
	        	else{
	        		bw1.write("QUIETO");
	        	}
	        }
        }	
        bw1.close();
    }  

	//Escribo el fichero EA con la acción a realizar por el MT4
	public static void metodo_Escritura_EA_EURUSD(String escritura) throws IOException {
		String ruta1 = rutaSincronizacion+"EA_EURUSD.txt";
        File archivo1 = new File(ruta1);
        BufferedWriter bw1;
        bw1 = new BufferedWriter(new FileWriter(archivo1));
        bw1.write(escritura);
        bw1.close();
	}    
	public static void metodo_Escritura_EA_EURGBP(String escritura) throws IOException {
		String ruta1 = rutaSincronizacion+"EA_EURGBP.txt";
        File archivo1 = new File(ruta1);
        BufferedWriter bw1;
        bw1 = new BufferedWriter(new FileWriter(archivo1));
        bw1.write(escritura);
        bw1.close();
	}    
	public static void metodo_Escritura_EA_EURJPY(String escritura) throws IOException {
		String ruta1 = rutaSincronizacion+"EA_EURJPY.txt";
        File archivo1 = new File(ruta1);
        BufferedWriter bw1;
        bw1 = new BufferedWriter(new FileWriter(archivo1));
        bw1.write(escritura);
        bw1.close();
	}    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * MÉTODO PRINCIPAL
	 * @throws InterruptedException 
	 * */
	public static void metodoPrincipal(String rutaSincronizacionEntrante, boolean ejecucion) throws IOException, InterruptedException {
		if(ejecucion==true){
			rutaSincronizacion = rutaSincronizacionEntrante;
			System.out.println("Inicio trading Ejecutado");
	
			/**INICIALIZACIONES DEL MÉTODO*/
			//Pido la hora del sistema
			int hora_aux=-1, minuto_aux=-1, segundos_aux=-1;
			Calendar calendario = Calendar.getInstance();
			int 	hora, minutos, segundos;
			hora =calendario.get(Calendar.HOUR_OF_DAY);
			minutos= calendario.get(Calendar.MINUTE);
			segundos = calendario.get(Calendar.SECOND);
			//Auxiliares de tiempos
			hora_aux= hora;
			minuto_aux= minutos;
			segundos_aux=segundos;
				
				
			/**LECTURA DE TODOS LOS INDICADORES DEL MT4*/
			//LEEMOS los MACD's QUE SE ESCRIBIERON EN MT4
			String rutaMACD_EURUSD=rutaSincronizacion+"MACD_EURUSD.txt";
			String lecturaMACD_EURUSD=metodo_Lectura_archivo(rutaMACD_EURUSD);
			System.out.println(lecturaMACD_EURUSD);
			
			String rutaMACD_EURGBP=rutaSincronizacion+"MACD_EURGBP.txt";
			String lecturaMACD_EURGBP=metodo_Lectura_archivo(rutaMACD_EURGBP);
			System.out.println(lecturaMACD_EURGBP);
			
			String rutaMACD_EURJPY=rutaSincronizacion+"MACD_EURJPY.txt";
			String lecturaMACD_EURJPY=metodo_Lectura_archivo(rutaMACD_EURJPY);
			System.out.println(lecturaMACD_EURJPY);
			
			//LEEMOS LOS RSI's QUE VA EN STRING Y LOS PASAMOS A DECIMAL
			String rutaRSI_EURUSD=rutaSincronizacion+"RSI_EURUSD.txt";
			String lecturaRSI_EURUSD=metodo_Lectura_archivo(rutaRSI_EURUSD);		
			double dDouble=Double.parseDouble(lecturaRSI_EURUSD);
			System.out.println(dDouble);
			
			String rutaRSI_EURGBP=rutaSincronizacion+"RSI_EURGBP.txt";
			String lecturaRSI_EURGBP=metodo_Lectura_archivo(rutaRSI_EURGBP);		
			double eDouble=Double.parseDouble(lecturaRSI_EURGBP);
			System.out.println(eDouble);
			
			String rutaRSI_EURJPY=rutaSincronizacion+"RSI_EURJPY.txt";
			String lecturaRSI_EURJPY=metodo_Lectura_archivo(rutaRSI_EURJPY);		
			double fDouble=Double.parseDouble(lecturaRSI_EURJPY);
			System.out.println(fDouble);
			
	/** ------------------------------------------------------------------------------------------------------------------------------------------------- */
			/** PREPARAMOS LOS RSI PARA VER SI HAY SOBRECOMPRAS O SOBRE VENTAS*/
			
			//ACTUALMENTE YA TENGO EL COLOR DE CADA MACD Y EL % DE CADA RSI 
			//ESCRIBO EL % DEL RSI EN OTRO FICHERO DEL QUE LEI 
			metodo_Escritura_RSI_STRING_EURUSD(dDouble);
			metodo_Escritura_RSI_STRING_EURGBP(dDouble);
			metodo_Escritura_RSI_STRING_EURJPY(dDouble);
			
			//cuando el EA MT4 vea que hay "-1" escrito en fichero //
			
			
			//YA SÉ SI MACD ESTÁ ROJO/VERDE/NO-EXISTE y si RSI ESTÁ SOBRECOMPRA/SOBREVENTA/NADA/NULO
			
			
			//COMPROBAMOS LO QUE ACABA DE ESCRIBIRSE (SOBRECOMPRA O SOBREVENTA)
			String rutaRSI_STRING_EURUSD=rutaSincronizacion+"RSI_EURUSD_STRING.txt";
			String lecturaRSI_STRING_EURUSD=metodo_Lectura_archivo(rutaRSI_STRING_EURUSD);		
			System.out.println(lecturaRSI_STRING_EURUSD);
			
			String rutaRSI_STRING_EURGBP=rutaSincronizacion+"RSI_EURGBP_STRING.txt";
			String lecturaRSI_STRING_EURGBP=metodo_Lectura_archivo(rutaRSI_STRING_EURGBP);		
			System.out.println(lecturaRSI_STRING_EURGBP);
	
			String rutaRSI_STRING_EURJPY=rutaSincronizacion+"RSI_EURJPY_STRING.txt";
			String lecturaRSI_STRING_EURJPY=metodo_Lectura_archivo(rutaRSI_STRING_EURUSD);		
			System.out.println(lecturaRSI_STRING_EURJPY);
	
	/** ------------------------------------------------------------------------------------------------------------------------------------------------- */
			/**ESCRIBIR EN LOS EA's SEGÚN CIERTAS CONDICIONES Y LOS DATOS RECABADOS*/
			String archivo2 = "C:/Users/juan antonio/AppData/Roaming/MetaQuotes/Terminal/93231E64ACB41DD159EEC4E406D1AD88/MQL4/Files/RSI_EURUSD_STRING.txt"; 
			File ficheroPrueba2 = new File(archivo2);
			if(ficheroPrueba2.exists()){	    
				// ---- Escribo EA EURUSD --- 
				if(lecturaRSI_STRING_EURUSD=="SOBRE COMPRA"){
					if(lecturaMACD_EURUSD != "-1" && lecturaMACD_EURUSD != "VERDE"){
						//voy leyendo el archivo y le deberé meter un delay con Threads
						rutaMACD_EURUSD=rutaSincronizacion+"MACD_EURUSD.txt";
						lecturaMACD_EURUSD=metodo_Lectura_archivo(rutaMACD_EURUSD);
					}
					if(lecturaMACD_EURUSD != "-1" && lecturaMACD_EURUSD == "ROJO"){
						metodo_Escritura_EA_EURUSD("VENDER");
						variableEUR_USD_compra_Hecha=false;
						variableEUR_USD_venta_Hecha=true;
					}
				}
				if(lecturaRSI_STRING_EURUSD=="SOBRE VENTA"){
					if(lecturaMACD_EURUSD != "-1" && lecturaMACD_EURUSD != "ROJO"){
						//voy leyendo el archivo y le deberé meter un delay con Threads
						rutaMACD_EURUSD=rutaSincronizacion+"MACD_EURUSD.txt";
						lecturaMACD_EURUSD=metodo_Lectura_archivo(rutaMACD_EURUSD);
					}
					//Será verde al salir del while y escribimos la orden en el EA (borrar la orden al ejecutarla desde MT4)
					if(lecturaMACD_EURUSD != "-1" && lecturaMACD_EURUSD == "VERDE"){
						metodo_Escritura_EA_EURUSD("COMPRAR");
						variableEUR_USD_compra_Hecha=true;
						variableEUR_USD_venta_Hecha=false;	
					}
				}
				//Orden de salida del mercado cuando se escribe QUIETO 
				if(lecturaRSI_STRING_EURUSD=="QUIETO"){
					//pongo a FALSE variables compra y venta para salirme del mercado
					variableEUR_USD_compra_Hecha=false;
					variableEUR_USD_venta_Hecha=false;
					metodo_Escritura_EA_EURUSD("SALIR");
				}
			}
				
			
			String archivo3 = "C:/Users/juan antonio/AppData/Roaming/MetaQuotes/Terminal/93231E64ACB41DD159EEC4E406D1AD88/MQL4/Files/RSI_EURGBP_STRING.txt"; 
			File ficheroPrueba3 = new File(archivo2);
			if(ficheroPrueba2.exists()){
				// ---- Escribo EA EURGBP --- 
				if(lecturaRSI_STRING_EURGBP=="SOBRE COMPRA"){
					if(lecturaMACD_EURGBP != "-1" && lecturaMACD_EURGBP != "VERDE"){
						//voy leyendo el archivo y le deberé meter un delay con Threads
						rutaMACD_EURGBP=rutaSincronizacion+"MACD_EURGBP.txt";
						lecturaMACD_EURGBP=metodo_Lectura_archivo(rutaMACD_EURGBP);
					}
					if(lecturaMACD_EURGBP != "-1" && lecturaMACD_EURGBP == "ROJO"){
						metodo_Escritura_EA_EURGBP("VENDER");
						variableEUR_GBP_compra_Hecha=false;
						variableEUR_GBP_venta_Hecha=true;
					}
				}
				if(lecturaRSI_STRING_EURGBP=="SOBRE VENTA"){
					if(lecturaMACD_EURGBP != "-1" && lecturaMACD_EURGBP != "ROJO"){
						//voy leyendo el archivo y le deberé meter un delay con Threads
						rutaMACD_EURGBP=rutaSincronizacion+"MACD_EURGBP.txt";
						lecturaMACD_EURGBP=metodo_Lectura_archivo(rutaMACD_EURGBP);
					}
					//Será verde al salir del while y escribimos la orden en el EA (borrar la orden al ejecutarla desde MT4)
					if(lecturaMACD_EURGBP != "-1" && lecturaMACD_EURGBP == "VERDE"){
						metodo_Escritura_EA_EURGBP("COMPRAR");
						variableEUR_GBP_compra_Hecha=true;
						variableEUR_GBP_venta_Hecha=false;	
					}
				}
				//Orden de salida del mercado cuando se escribe QUIETO 
				if(lecturaRSI_STRING_EURGBP=="QUIETO"){
					//pongo a FALSE variables compra y venta para salirme del mercado
					variableEUR_GBP_compra_Hecha=false;
					variableEUR_GBP_venta_Hecha=false;
					metodo_Escritura_EA_EURGBP("SALIR");
				}
			}
			
			
			String archivo4 = "C:/Users/juan antonio/AppData/Roaming/MetaQuotes/Terminal/93231E64ACB41DD159EEC4E406D1AD88/MQL4/Files/RSI_EURJPY_STRING.txt"; 
			File ficheroPrueba4 = new File(archivo2);
			if(ficheroPrueba4.exists()){
				// ---- Escribo EA EURJPY --- 
				if(lecturaRSI_STRING_EURJPY=="SOBRE COMPRA"){
					if(lecturaMACD_EURJPY != "-1" && lecturaMACD_EURJPY != "VERDE"){
						//voy leyendo el archivo y le deberé meter un delay con Threads
						rutaMACD_EURJPY=rutaSincronizacion+"MACD_EURJPY.txt";
						lecturaMACD_EURJPY=metodo_Lectura_archivo(rutaMACD_EURJPY);
					}
					if(lecturaMACD_EURJPY != "-1" && lecturaMACD_EURJPY == "ROJO"){
						metodo_Escritura_EA_EURJPY("VENDER");
						variableEUR_JPY_compra_Hecha=false;
						variableEUR_JPY_venta_Hecha=true;
					}
				}
				if(lecturaRSI_STRING_EURJPY=="SOBRE VENTA"){
					if(lecturaMACD_EURJPY != "-1" && lecturaMACD_EURJPY != "ROJO"){
						//voy leyendo el archivo y le deberé meter un delay con Threads
						rutaMACD_EURJPY=rutaSincronizacion+"MACD_EURJPY.txt";
						lecturaMACD_EURJPY=metodo_Lectura_archivo(rutaMACD_EURJPY);
					}
					//Será verde al salir del while y escribimos la orden en el EA (borrar la orden al ejecutarla desde MT4)
					if(lecturaMACD_EURJPY != "-1" && lecturaMACD_EURJPY == "VERDE"){
						metodo_Escritura_EA_EURJPY("COMPRAR");
						variableEUR_JPY_compra_Hecha=true;
						variableEUR_JPY_venta_Hecha=false;	
					}
				}
				//Orden de salida del mercado cuando se escribe QUIETO 
				if(lecturaRSI_STRING_EURJPY=="QUIETO"){
					//pongo a FALSE variables compra y venta para salirme del mercado
					variableEUR_JPY_compra_Hecha=false;
					variableEUR_JPY_venta_Hecha=false;
					metodo_Escritura_EA_EURJPY("SALIR");
				}
			}
			System.out.println("Stop trading Ejecutado");
		}	
		
		else{
			System.out.println("Stop pulsado");
		}
	}
	
}
//---------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	
//El EA en lenguaje MQL4 leerá el texto final y decidirá si entrar o no al mercado en funcion de el texot leido 
//Leer ficheros en temporalidades diferentes entre JAVA y MQL4 para evitar colisiones de manejadores de archivos	
