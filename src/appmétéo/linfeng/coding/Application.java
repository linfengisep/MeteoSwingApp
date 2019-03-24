/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appmétéo.linfeng.coding;

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author linfengwang
 */
public class Application {
    public static void main(String[] args){
        System.out.println("Current thread name:"+Thread.currentThread().getName());
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                MainFrame mainFrame = new MainFrame("Météo App");
                mainFrame.setResizable(false);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.pack();//Causes this Window to be sized to fit the preferred size and layouts of its subcomponents.
                mainFrame.setLocationRelativeTo(null);//null, the window is placed in the center of the screen
                mainFrame.setVisible(true);
            }
        });
    }
}
