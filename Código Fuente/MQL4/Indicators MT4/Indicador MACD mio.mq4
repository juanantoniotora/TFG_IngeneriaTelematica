

//Retocar para temporalidades de 1h o 30 minutos





//+------------------------------------------------------------------+
//|                                                  Custom MACD.mq4 |
//|                   Copyright 2005-2014, MetaQuotes Software Corp. |
//|                                              http://www.mql4.com |
//+------------------------------------------------------------------+

#property copyright   "2005-2014, MetaQuotes Software Corp."
#property link        "http://www.mql4.com"
#property description "Moving Averages Convergence/Divergence"
#property strict

#include <MovingAverages.mqh>

//--- indicator settings
#property  indicator_separate_window
#property  indicator_buffers 4      //antes era 2
#property  indicator_color1  Red
#property  indicator_color2  DeepPink
#property  indicator_color3  Lime
#property  indicator_color4  White

#property  indicator_width1  4     //antes era 2
//--- indicator parameters
input int InpFastEMA=5;   // Fast EMA Period
input int InpSlowEMA=13;   // Slow EMA Period
input int InpSignalSMA=2;  // Signal SMA Period
//--- indicator buffers
double    ExtMacdBufferUp[];
double    ExtMacdBufferDown[];
double    ExtSignalBuffer[];
double    ExtSignalBufferNew[];
//--- right input parameters flag
bool      ExtParameters=false;
//--- static var
static int variableInicial=0;
static bool disponible=true;
static int auxMinuto=Minute()-1;
static int minutoAux=Minute()-1;

//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int OnInit(){
   Alert("Arrancado MACD mio ", Symbol()," a las ",Hour(),":",Minute()," ",Symbol());
   IndicatorDigits(Digits+1);
//--- drawing settings
   SetIndexStyle(0,DRAW_HISTOGRAM);
   SetIndexStyle(1,DRAW_LINE);
   SetIndexStyle(2,DRAW_HISTOGRAM);
   SetIndexStyle(3,DRAW_LINE);
   SetIndexDrawBegin(1,InpSignalSMA);
//--- indicator buffers mapping
   SetIndexBuffer(0,ExtMacdBufferDown);
   SetIndexBuffer(1,ExtSignalBuffer);
   SetIndexBuffer(2,ExtMacdBufferUp);
   SetIndexBuffer(3,ExtSignalBufferNew);
//--- name for DataWindow and indicator subwindow label
   IndicatorShortName("MACD ("+IntegerToString(InpFastEMA)+","+IntegerToString(InpSlowEMA)+","+IntegerToString(InpSignalSMA)+") Juan Antonio Tora ");
   SetIndexLabel(0,"MACD");
   SetIndexLabel(1,"Signal");
//--- check for input parameters
   if(InpFastEMA<=1 || InpSlowEMA<=1 || InpSignalSMA<=1 || InpFastEMA>=InpSlowEMA)
     {
      Print("Wrong input parameters");
      ExtParameters=false;
      return(INIT_FAILED);
     }
   else
      ExtParameters=true;
//--- initialization done
   return(INIT_SUCCEEDED);
  }
//+------------------------------------------------------------------+
//| Moving Averages Convergence/Divergence                           |
//+------------------------------------------------------------------+
int OnCalculate (const int rates_total,
                 const int prev_calculated,
                 const datetime& time[],
                 const double& open[],
                 const double& high[],
                 const double& low[],
                 const double& close[],
                 const long& tick_volume[],
                 const long& volume[],
                 const int& spread[])
  {
   
   
   int i,limit;
//---
   if(rates_total<=InpSignalSMA || !ExtParameters)
      return(0);
//--- last counted bar will be recounted
   limit=rates_total-prev_calculated;
   if(prev_calculated>0)
      limit++;
//--- Cálculo de MACD y SIGNAL
   for(i=0; i<limit; i++){
      ExtMacdBufferUp[i]=iMA(NULL,0,InpFastEMA,0,MODE_EMA,PRICE_CLOSE,i)-
                    iMA(NULL,0,InpSlowEMA,0,MODE_EMA,PRICE_CLOSE,i);
      ExtMacdBufferDown[i]=iMA(NULL,0,InpFastEMA,0,MODE_EMA,PRICE_CLOSE,i)-
                    iMA(NULL,0,InpSlowEMA,0,MODE_EMA,PRICE_CLOSE,i);
      ExtSignalBufferNew[i]=iMA(NULL,0,InpFastEMA,0,MODE_EMA,PRICE_CLOSE,i)-
                    iMA(NULL,0,InpSlowEMA,0,MODE_EMA,PRICE_CLOSE,i);
   } 
   
   SimpleMAOnBuffer(rates_total,prev_calculated,0,InpSignalSMA,ExtMacdBufferUp,ExtSignalBuffer);   
       
                   
//--- Eliminacion de señales sobrantes para el gráfico
   for(int ii=0; ii<limit; ii++){
      //Elimino VERDE para que solo vea el ROJO
      if(ExtMacdBufferUp[ii]<ExtSignalBuffer[ii] && ExtMacdBufferUp[ii]>0){
         ExtMacdBufferUp[ii]=0;
      }  if(ExtMacdBufferUp[ii]<ExtSignalBuffer[ii] && ExtMacdBufferUp[ii]<0){
            ExtMacdBufferUp[ii]=0;
         }
      
       //Elimino ROJO para que solo vea el VERDE
      if(ExtMacdBufferDown[ii]>ExtSignalBuffer[ii] && ExtMacdBufferDown[ii]<0){
         ExtMacdBufferDown[ii]=0;
      }  if(ExtMacdBufferDown[ii]>ExtSignalBuffer[ii] && ExtMacdBufferDown[ii]>0){
            ExtMacdBufferDown[ii]=0;
         }
   }
  
   //Cálculo de condiciones de alerta sonora para invertir a 1 vela de 15 minutos
   if(Minute()!=minutoAux){
      minutoAux=Minute();
      //creamos el fichero y lo modificamos
      string nombreDirectorio=StringConcatenate("MACD_",Symbol(),".txt");
      int filehandle=FileOpen(nombreDirectorio,FILE_WRITE|FILE_TXT);
   
      if(ExtMacdBufferDown[1]>0){      //si existe ROJO
         if(filehandle!=INVALID_HANDLE) { 
            FileWrite(filehandle,"ROJO"); 
            FileClose(filehandle); 
            Print("FileOpen OK");   
         }
      } 
      if(ExtMacdBufferUp[1]>0){     //si existe VERDE
         if(filehandle!=INVALID_HANDLE) {
            FileWrite(filehandle,"VERDE"); 
            FileClose(filehandle); 
            Print("FileOpen OK");   
         }
      }
            
   }
      
  
/*-----------------------------------------------------------------------------------*/     
   
  
   //Etiqueta que muestro en pantalla
   string texto="MACD Juan Antonio Tora";
   ObjectDelete("TEXTO");
   ObjectCreate("TEXTO", OBJ_LABEL, 0, 0, 0);
   ObjectSet("TEXTO", OBJPROP_CORNER, 2);
   ObjectSet("TEXTO", OBJPROP_XDISTANCE, 30);
   ObjectSet("TEXTO", OBJPROP_YDISTANCE, 5);
   ObjectSetText("TEXTO", texto, 20, "Georgia", DeepPink);


//---  return del método
   return(rates_total);
}
  
  
int deinit(){
   Alert("MACD mio terminado en ",Symbol());
   string nombreDirectorio=StringConcatenate("MACD_",Symbol(),".txt");
   int filehandle=FileOpen(nombreDirectorio,FILE_WRITE|FILE_TXT);
   FileDelete(filehandle, FILE_COMMON);
   return(0);
}  
//+------------------------------------------------------------------+