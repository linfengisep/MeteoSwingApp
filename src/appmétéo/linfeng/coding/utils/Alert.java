/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appmétéo.linfeng.coding.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author linfengwang
 */
public class Alert {
   public static void error(Component parent,String message,String title){
        JOptionPane.showMessageDialog(parent, message,title,JOptionPane.ERROR_MESSAGE);
   }
   public static void error(Component parent,String message){
        Alert.error(parent, message,"Error");
   }
   
   public static void info(Component parent,String message,String title){
        JOptionPane.showMessageDialog(parent, message,title,JOptionPane.INFORMATION_MESSAGE);
   }
   
   public static void info(Component parent,String message){
        info(parent, message,"Information");
   }
}
