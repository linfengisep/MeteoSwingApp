/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appmétéo.linfeng.coding;

import appmétéo.linfeng.coding.utils.Alert;
import appmétéo.linfeng.coding.utils.ApiUrlBuilder;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 * @author linfengwang
 */
public class MainFrame extends JFrame {
    public MainFrame(String title){
        super(title);
        double logitude = 37.8267;
        double latitude = -122.4233;
        String meteoUrl = ApiUrlBuilder.buildUrlWithPosition(logitude, latitude);

        System.out.println("Avant le requête.");
          OkHttpClient client = new OkHttpClient();
                 Request request = new Request
                                    .Builder()
                                    .url(meteoUrl)
                                    .build();
                 client.newCall(request).enqueue(new Callback(){
                   @Override
                   public void onFailure(Call call, IOException ioe) {
                       ioe.printStackTrace();
                       Alert.error(MainFrame.this,"Check your internet connection");
                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException { 
                       System.out.println("Current thread: "+Thread.currentThread().getName());
                       try(ResponseBody responseBody = response.body()){
                           if(! response.isSuccessful())
                                Alert.error(MainFrame.this,"Hooops,an error is encountered");
                           Headers responseHeaders = response.headers();
                           for(int i=0,size = responseHeaders.size();i<size;i++){
                               //System.out.println(responseHeaders.name(i)+", "+responseHeaders.value(i));
                           }
                           System.out.println(responseBody.string());
                       }
                   }
                 });
        System.out.println("Après le requête.");
    }
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(500,300);
    } 
}
