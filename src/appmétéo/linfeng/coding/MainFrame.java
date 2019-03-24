/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appmétéo.linfeng.coding;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author linfengwang
 */
public class MainFrame extends JFrame {
    public MainFrame(String title){
        super(title);
        final String apiKey="43fd7549962dc917f965cb9cfa50ff38";
        String sources = "https://api.darksky.net/forecast/";
        double logitude = 37.8267;
        double latitude = -122.4233;
        //String meteoUrl = String.format(Locale.FRANCE,"%s/forecast/%s/%.4f,%.4f",sources,apiKey,logitude,latitude);
        //System.out.println("Url:"+meteoUrl);
        String meteoUrl = sources+apiKey+"/"+logitude+","+latitude;

        System.out.println("Avant le requête.");
        new MeteoWorker(meteoUrl).execute();
        System.out.println("Après le requête.");
        
    }
    
    class MeteoWorker extends SwingWorker<String,Void>{
        private String meteoUrl;
        public MeteoWorker(String url){
            this.meteoUrl = url;
        }

       @Override
            protected String doInBackground() throws Exception {
               System.out.println("Current Tread in Swingworker:"+Thread.currentThread().getName());
               OkHttpClient client = new OkHttpClient();
                 Request request = new Request
                                    .Builder()
                                    .url(this.meteoUrl)
                                    .build();
                 
                Call call = client.newCall(request);
                try{
                    Response response = call.execute();
                    //return "result";
                    if(response.isSuccessful()){
                        return response.body().string();
                    }
                }catch(IOException e){
                    System.err.println(e);
                }    
                return null;
            }
            
            @Override
            protected void done(){
                try {
                    //super.done();
                    System.out.println("Response: "+get());
                } catch (InterruptedException | ExecutionException ex) {
                    System.err.println(ex);
                }
            }
    }
}
