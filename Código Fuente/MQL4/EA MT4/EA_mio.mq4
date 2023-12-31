
#property copyright "juan antonio tora canovas"
#property link       "www.juanantoniotora.com"

extern double Lotes = 0.1;
extern int Deslizamiento = 5;
extern int Server_EST_Difference = 6;

double Punto;
int Desviacion;

int LastBars = 0;

int HourToCheck;
int DayToCheck;

static int ticket = -1; //la necesitamos visible de principio a fin del programa, por ello STATIC

int minuto = Minute();
int auxMinuto= Minute();
bool control=false;


int init()
{
	Punto = Point;
	Desviacion = Deslizamiento;
	//Checking for unconvetional Point digits number
   if ((Point == 0.00001) || (Point == 0.001)){
      Punto *= 10;
      Desviacion *= 10;
   }   
   
   //Escribo el archivo del EA para prepararlo
   string nombreDirectorio=StringConcatenate("EA_",Symbol(),".txt");
   int filehandle=FileOpen(nombreDirectorio,FILE_WRITE|FILE_TXT);
   if(filehandle!=INVALID_HANDLE) { 
      FileWrite(filehandle,""); 
      FileClose(filehandle); 
      Print("FileOpen OK");   
   } 
   
   Alert("EA mio iniciado en ",Symbol());
   return(0);
}

//+------------------------------------------------------------------+
//| Checking time and date then posting order or closing position    |
//+------------------------------------------------------------------+
int start(){

   if(Minute() != auxMinuto && Minute() == (0 || 5 || 10 || 15 || 20 || 25 || 30 || 35 || 40 || 45 || 50 || 55)){
      auxMinuto = Minute();
      control=true;
   }
   

   //Espera para la nueva Barra en la gráfica.
	if (LastBars == Bars){ 
	   return(0);
	}   
	else{ 
	   LastBars = Bars;
   }
   
   //LEO EL ARCHIVO PARA DECIDIR QUÉ OPERACIÓN EJECUTAR SELL/BUY EN EL EA
   //Lo que leo deberá ser escrito antes por JAVA
   ResetLastError(); 
   string nombreDirectorio=StringConcatenate("EA_",Symbol(),".txt");
   int file_handle=FileOpen(nombreDirectorio,FILE_READ|FILE_BIN|FILE_ANSI); 
   //VARIABLES ADICIONALES
   int    longitud_string;  //cantidad de caracteres del fichero
   string string_leido;     //cadena leida
   if(file_handle!=INVALID_HANDLE && control!=false){ 
      
      PrintFormat("%s El archivo está disponible para la lectura: ",nombreDirectorio);
      
      //LEEMOS DATOS DESDE FICHERO MIENTRAS HAYAN CARACTERES QUE LEER
      while(!FileIsEnding(file_handle)) { 
         //Averiguar cuantos caracter hay que leer esta vez 
         longitud_string=FileReadInteger(file_handle,INT_VALUE); 
         //Leer el String de longitud  
         string_leido=FileReadString(file_handle,longitud_string); 
         //Imprimimos el String leido 
         PrintFormat(string_leido); 
      } 
      
      //Cierro el fichero y libero el manejador 
      FileClose(file_handle); 
      PrintFormat("Los datos están leidos, %s fichero cerrado.",nombreDirectorio); 
     } 
   else{ 
      if(control!= false){   
         PrintFormat("Fallo al abrir %s archivo, Código ERROR = %d",nombreDirectorio,GetLastError()); 
      }
   }
   
   
   //COMPRUEBO LA CADENA LEIDA CON LAS POSIBLES
   //PARA CADA COINCIDENCIA ENVÍO UNA ORDEN AL MERCADO DISTINTA
   if (string_leido=="" && control!=false){
      //no hago orden ninguna
      control=false;
   }
   else{
      if (control!=false && (string_leido=="COMPRAR" || string_leido=="COMPRAR " || string_leido==" COMPRAR")){
         ticket = metodoBuy();
         control=false;
      }
      else{
         if (control!=false && (string_leido=="VENDER" || string_leido=="VENDER " || string_leido==" VENDER")){
            ticket = metodoSell();
            control=false;
         }
         else{
            if(control!=false){
               metodoClose();
               control=false;
            }
         }
      }
   }   

   return(0);
}




//+------------------------------------------------------------------+
//| Metodos de colocación de Ordenes en mercado                      |
//+------------------------------------------------------------------+
int metodoSell(){
	RefreshRates();
	//Pedir precio ASK para tomar ultimo PRECIO DE COMPRA
	int idoperacion_Venta = OrderSend(Symbol(), OP_SELL, Lotes, Ask, Desviacion, 0, 0, "AUD/JPY Wednesday 15:00 EST");
	if (idoperacion_Venta == -1)
	{
		int e = GetLastError();
		Print(e);
	}
	else return(idoperacion_Venta);
}


int metodoBuy(){
	RefreshRates();
	//Pedir precio BID para tomar ultimo PRECIO DE VENTA
	int idoperacion_Compra = OrderSend(Symbol(), OP_BUY, Lotes, Bid, Desviacion, 0, 0, "AUD/JPY Wednesday 15:00 EST");
	if (idoperacion_Compra == -1)
	{
		int e = GetLastError();
		Print(e);
	}
	else return(idoperacion_Compra);
}
//+------------------------------------------------------------------+
//| Metodos para cerrar Ordenes en el mercado                        |
//+------------------------------------------------------------------
void metodoClose(){
	RefreshRates();
   OrderClose(ticket, Lotes, Ask, Desviacion);
}



int deinit(){
   Alert("EA mio terminado en ",Symbol());
   return (0);
}