/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appmétéo.linfeng.coding;

import appmétéo.linfeng.coding.models.Weather;
import appmétéo.linfeng.coding.utils.Alert;
import appmétéo.linfeng.coding.utils.ApiUrlBuilder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 *
 * @author linfengwang
 */
public class MainFrame extends JFrame {
    private static final Color BLUE_COLOR = Color.decode("#8EA2C6");
    private static final Color WHITE_COLOR = Color.WHITE;
    private static final Color FONT_COLOR = new Color(255,255,255,120);
    
    private JLabel tempLabel;
    private JLabel timeLabel;
    private JLabel locationLabel;
    private JLabel summaryLabel;
    
    private JLabel humidityLabel;
    private JLabel humidityValueLabel;
    
    private JLabel preciProbabilityLabel;
    private JLabel preciProbabilityValueLabel;
    
    private JPanel infoPanel;
    
    private Weather currentWeather;
    
    public MainFrame(String title) {
        super(title);
        
        Container contentPane = getContentPane();
        
        contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.Y_AXIS));
        
        contentPane.setBackground(BLUE_COLOR);
        
        tempLabel = new JLabel("<html>100&deg;</html>");
        tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tempLabel.setForeground(WHITE_COLOR);
        tempLabel.setFont(new Font("San Fransisco",Font.BOLD,80));
        //tempLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        timeLabel = new JLabel("time is coming soon");
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(FONT_COLOR);
        timeLabel.setFont(new Font("San Fransisco",Font.BOLD,15));
        //timeLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        
        locationLabel = new JLabel("location");
        locationLabel.setFont(new Font("San Fransisco",Font.BOLD,30));
        locationLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        locationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationLabel.setForeground(WHITE_COLOR);
        
        humidityLabel = new JLabel("Humidité",SwingConstants.CENTER);
        humidityLabel.setFont(new Font("San Fransisco",Font.BOLD,15));
        humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidityLabel.setForeground(WHITE_COLOR);
        
        humidityValueLabel = new JLabel("0.84",SwingConstants.CENTER);
        humidityValueLabel.setFont(new Font("San Fransisco",Font.BOLD,15));
        humidityValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidityValueLabel.setForeground(WHITE_COLOR);
        
        preciProbabilityLabel = new JLabel("Risque de pluie",SwingConstants.CENTER);
        preciProbabilityLabel.setFont(new Font("San Fransisco",Font.BOLD,15));
        preciProbabilityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        preciProbabilityLabel.setForeground(WHITE_COLOR);
        
        preciProbabilityValueLabel = new JLabel("0%",SwingConstants.CENTER);
        preciProbabilityValueLabel.setFont(new Font("San Fransisco",Font.BOLD,15));
        preciProbabilityValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        preciProbabilityValueLabel.setForeground(WHITE_COLOR);
        
        infoPanel = new JPanel(new GridLayout(2,2));
        infoPanel.setBackground(BLUE_COLOR);
        infoPanel.add(humidityLabel);
        infoPanel.add(preciProbabilityLabel);
        infoPanel.add(humidityValueLabel);
        infoPanel.add(preciProbabilityValueLabel);
        
        
        summaryLabel = new JLabel("SummaryLabel");
        summaryLabel.setFont(new Font("San Fransisco",Font.BOLD,20));
        summaryLabel.setForeground(FONT_COLOR);
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        summaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        this.add(locationLabel);
        this.add(timeLabel);
        this.add(tempLabel);
        this.add(infoPanel);
        this.add(summaryLabel);
        
        double logitude = 2.3522;
        double latitude = 48.8566;
        String meteoUrl = ApiUrlBuilder.buildUrlWithPosition(logitude, latitude);
        System.out.println("Avant le requête.");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(meteoUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                ioe.printStackTrace();
                Alert.error(MainFrame.this, "Check your internet connection");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Current thread: " + Thread.currentThread().getName());
                try (ResponseBody body = response.body()) {
                    if (!response.isSuccessful()) {
                        Alert.error(MainFrame.this, "Hooops,an error is encountered");
                    }
                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        //System.out.println(responseHeaders.name(i)+", "+responseHeaders.value(i));
                    }
                    String responseJsonData = body.string();
                    System.out.println(responseJsonData);
                    currentWeather = getWeather(responseJsonData);
                    
                    updateScreen();

                } catch (ParseException | IOException e) {
                    Alert.error(MainFrame.this, "json data parse exception");
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Après le requête.");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 400);
    }

    public Weather getWeather(String responseJsonData) throws ParseException {
        JSONObject meteoObject = (JSONObject) JSONValue.parseWithException(responseJsonData);
        JSONObject currentWeatherObject = (JSONObject) meteoObject.get("currently");
        String timezone = (String) meteoObject.get("timezone");
        //System.out.println("timezone: "+timezone);
        String summary = (String) currentWeatherObject.get("summary");
        double precipProbability = Double.parseDouble(currentWeatherObject.get("precipProbability")+"");
        double temperature = Double.parseDouble(currentWeatherObject.get("temperature") + "");
        double humidity = Double.parseDouble(currentWeatherObject.get("humidity") + "");
        long time = (long) currentWeatherObject.get("time");
     
        Weather weather = new Weather();
        weather.setHumidity(humidity);
        weather.setTemperature(temperature);
        weather.setTime(time);
        weather.setPreciProbability(precipProbability);
        weather.setSummary(summary);
        weather.setTimezone("Europe/Paris");
        return weather;
    }
    
    protected void  updateScreen(){
        timeLabel.setText("Il est: "+currentWeather.getFormattedTime());
        //locationLabel.setText(currentWeather.getMyTimezone());
        locationLabel.setText("Paris,Fr");
        System.out.println("temp : "+currentWeather.getTemperature());
        //tempLabel.setText(String.format("%.1f°",convertFahrToCel(currentWeather.getTemperature())));
        tempLabel.setText(String.format("%.1f°",11.2));
        humidityValueLabel.setText(currentWeather.getHumidity()+"");
        summaryLabel.setText(currentWeather.getSummary());
    }
    
    private double convertFahrToCel(double fahr){
           return (fahr - 32.0)/1.8;
    }
}
