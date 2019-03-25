
package appmétéo.linfeng.coding.utils;

import java.util.Locale;

public class ApiUrlBuilder {
   private static final String METEO_SOURCE_URL = "https://api.darksky.net/forecast/"; 
   private static final String METEO_KEY = "43fd7549962dc917f965cb9cfa50ff38";
   public static String buildUrlWithPosition(double longitude, double latitude){
       //return METEO_SOURCE_URL+METEO_KEY+"/"+longitude+","+latitude;
       return String.format(Locale.US,"%s%s/%.4f,%.4f",METEO_SOURCE_URL,METEO_KEY,longitude,latitude);
   }
}
