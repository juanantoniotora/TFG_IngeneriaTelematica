//+------------------------------------------------------------------+
//|                                 Z_RSI_JuanAntonioToraCanovas.mq4 |
//|                      Copyright 20017, Juan Antonio Tora Canovas  |
//|                                   http://www.juanantoniotora.com |
//+------------------------------------------------------------------+

//propiedades de copyRight
#property copyright   "Juan Antonio Tora Canovas"
#property link        "http://www.juanantoniotora.com"
#property description "Own Modification of Relative Strength Index"
#property strict

//propiedades iniciales del indiador RSI modificado 
#property indicator_separate_window //coloca RSI en ventana distinta al gráfico
#property indicator_minimum    0
#property indicator_maximum    100
#property indicator_buffers    1
#property indicator_color1     DodgerBlue
#property indicator_level1     30.0 //Niveles RSI inicial 30
#property indicator_level2     70.0 //Niveles RSI inicial 70
#property indicator_level3     50.0 //Niveles RSI inicial 50
#property indicator_levelcolor clrSilver//Color para escritura niveles
#property indicator_levelstyle STYLE_DOT//Letra para escritura niveles

//--- input parameters
input int InpRSIPeriod=6; // RSI Period entrante inicial

//--- buffers
double ExtRSIBuffer[];  //Externo    general
double ExtPosBuffer[];  //Posiciones positivas
double ExtNegBuffer[];  //Posiciones negativas

//--- Variables Estáticas Creadas
//Importante que sean estáticas para que se llamen a si mismas  
static int  minuto=Minute();
static int  hora=Hour();
static int  minAux=Minute()-1;
static bool control=false;
static bool variableInicial=true;
static int  contador=0;


//+------------------------------------------------------------------+
//| Función que inicia el indicador Cliente                         |
//+------------------------------------------------------------------+
int OnInit(){
   string short_name;
   Alert("RSI comenzado en ",Symbol());
// --- Se utilizan 2 buffers adicionales para conteo ---//
   IndicatorBuffers(3); //1 inicial + 2 añadidos 
   SetIndexBuffer(0,ExtRSIBuffer);
   SetIndexBuffer(1,ExtPosBuffer);
   SetIndexBuffer(2,ExtNegBuffer);
//--- Cómo se traza la línea del RSI principal
   SetIndexStyle(0,DRAW_LINE);
   SetIndexBuffer(0,ExtRSIBuffer);
//--- Etiqueta de indicador y Nombres en la ventana del indicador
   short_name="RSI Juan Antonio Tora"; //En esta versión el usuario no ve el periodo del RSI en gráfica
   IndicatorShortName(short_name);
   SetIndexLabel(0,short_name);        //Pongo la etiqueta anterior sobre la gráfica

//--- Comprobación para el periodo
   if(InpRSIPeriod<2){
      Print("Incorrect value for input variable InpRSIPeriod = ",InpRSIPeriod);
      return(INIT_FAILED);
   }
//---
   SetIndexDrawBegin(0,InpRSIPeriod);
//--- initialización hecha
   return(INIT_SUCCEEDED);
  }



//+------------------------------------------------------------------+
//| Funcion Principal del Relative Strength Index                    |
//+------------------------------------------------------------------+
int OnCalculate(const int rates_total,
                const int prev_calculated,
                const datetime &time[],
                const double &open[],
                const double &high[],
                const double &low[],
                const double &close[],
                const long &tick_volume[],
                const long &volume[],
                const int &spread[])
  {
   
   //Variable modificada para poder condicionar alarmas del RSI
   if(variableInicial==true){
      Alert("\"RSI mio\" arrancado en ",Symbol()," a las ",hora,":", minuto,":",Seconds());
      variableInicial=false;  //pongo en OFF la variable de alarmas
   }
   
   //Vriables generales para cálculo de RSI
   int    i,pos;
   double diff;

   //--- Cada minuto preparo el controlador a TRUE
   if(Minute()!=minAux){
      control=false; //"control" actualizado cada minuto
   }
   minAux=Minute();  //"minAux" es actualizada para que no se modifique "control" antes de tiempo
   
   if(Bars<=InpRSIPeriod || InpRSIPeriod<2)
      return(0);
//--- counting from 0 to rates_total
   ArraySetAsSeries(ExtRSIBuffer,false);
   ArraySetAsSeries(ExtPosBuffer,false);
   ArraySetAsSeries(ExtNegBuffer,false);
   ArraySetAsSeries(close,false);
//--- preliminary calculations
   pos=prev_calculated-1;
   if(pos<=InpRSIPeriod){
      //--- first RSIPeriod values of the indicator are not calculated
      ExtRSIBuffer[0]=0.0;
      ExtPosBuffer[0]=0.0;
      ExtNegBuffer[0]=0.0;
      double sump=0.0;
      double sumn=0.0;
      for(i=1; i<=InpRSIPeriod; i++){
         ExtRSIBuffer[i]=0.0;
         ExtPosBuffer[i]=0.0;
         ExtNegBuffer[i]=0.0;
         diff=close[i]-close[i-1];
         if(diff>0){
            sump+=diff;
         }   
         else
            sumn-=diff;
      }
      //--- calculate first visible value
      ExtPosBuffer[InpRSIPeriod]=sump/InpRSIPeriod;
      ExtNegBuffer[InpRSIPeriod]=sumn/InpRSIPeriod;
      if(ExtNegBuffer[InpRSIPeriod]!=0.0){
         ExtRSIBuffer[InpRSIPeriod]=100.0-(100.0/(1.0+ExtPosBuffer[InpRSIPeriod]/ExtNegBuffer[InpRSIPeriod]));
      }   
      else{
         if(ExtPosBuffer[InpRSIPeriod]!=0.0){
            ExtRSIBuffer[InpRSIPeriod]=100.0;
         }
         else{
            ExtRSIBuffer[InpRSIPeriod]=50.0;
         }   
      }
      //--- prepare the position value for main calculation
      pos=InpRSIPeriod+1;
   }


   //--- Bucle Principal De Cálculos
   for(i=pos; i<rates_total && !IsStopped(); i++){
      diff=close[i]-close[i-1];
      ExtPosBuffer[i]=(ExtPosBuffer[i-1]*(InpRSIPeriod-1)+(diff>0.0?diff:0.0))/InpRSIPeriod;
      ExtNegBuffer[i]=(ExtNegBuffer[i-1]*(InpRSIPeriod-1)+(diff<0.0?-diff:0.0))/InpRSIPeriod;
      
      if(ExtNegBuffer[i]!=0.0){  //El RSI debe ser >0
         
         ExtRSIBuffer[i]=100.0-100.0/(1+ExtPosBuffer[i]/ExtNegBuffer[i]);  //calculo RSI
         double RSI_normalizado=NormalizeDouble(ExtRSIBuffer[i-1], 2); //normalizo el valor del RSI a 2 decimales con redondeo al alza
         
         if(control==false && i==rates_total-1){  //rates-1  es la "i" ultima... y "a" tiene guardada kla penúltima
            
            ResetLastError(); 
            string nombreDirectorio=StringConcatenate("RSI_",Symbol(),".txt");   //Preparo el nombre deldirectorio
            int filehandle=FileOpen(nombreDirectorio,FILE_WRITE|FILE_TXT);       //está bajo directorio: /MQL4/FILES/... 
            
            if(filehandle!=INVALID_HANDLE) {
               FileWrite(filehandle,RSI_normalizado); 
               FileClose(filehandle); 
               Alert("RSI en ",Symbol()," al ",RSI_normalizado,"% a las ",Hour(),":",Minute(),":",Seconds(),".");  
            }        
            else {
               FileWrite(filehandle,"FALLÓ"); 
               FileClose(filehandle); 
               Alert("RSI en ",Symbol()," al ",RSI_normalizado,"% a las ",Hour(),":",Minute(),":",Seconds(),".");
            }
            control=true;  //Pongo "control" a TRUE para que en ese minuto no escriba más veces
         }   
      }
      else{ //Para RSI =0
         if(ExtPosBuffer[i]!=0.0){
            ExtRSIBuffer[i]=100.0; 
            double RSI_normalizado=NormalizeDouble(ExtRSIBuffer[i-1], 2);
            if(control==false && ExtRSIBuffer[i]>80 && i==rates_total-1){  
               ResetLastError(); 
               int filehandle=FileOpen("mi.txt",FILE_WRITE|FILE_TXT); //se aloja bajo directorio de la carpeta de datos: /MQL4/FILES/ 
               if(filehandle!=INVALID_HANDLE) {
                  FileWrite(filehandle,"RSI ",RSI_normalizado); 
                  FileClose(filehandle); 
                  Print("FileOpen OK");   
               }        
               else {
                  Print("Operation FileOpen failed, error ",GetLastError()); 
               }
               //Alert("RSI en ",Symbol()," al ",a,"% a las ",Hour(),":",Minute(),":",Seconds(),".");
               control=true;
            }   
         }
         else{
            ExtRSIBuffer[i]=50.0;
            if(control==false && i==rates_total - 1){  
               //Alert("RSI a ",ExtRSIBuffer[i]," %");
               control=true;
            }   
         }
      }   
   }
   return(rates_total);
}


//+------------------------------------------------------------------+
//| Funcion de Finalización                                          |
//+------------------------------------------------------------------+
int deinit(){
   //Alert("RSI finalizado en ",Symbol());
   return(0);
}
//+------------------------------------------------------------------+


