/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hetec;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 *
 * @author Melanie
 */
public class HETEC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
          
        // TODO code application logic here
        new interfaz.Ingreso().setVisible(true);
    }
 
/*TODO ESTO ES PARA EL MENU QUE ESTA POR LA HORA
    static TrayIcon trayIcon;
    public HETEC() {
           show();
    }
    public static void show(){
        
        
        if (!SystemTray.isSupported()) {
            System.exit(0);
        }        
        trayIcon = new TrayIcon(createIcon("/Imagenes/TrayIcon.png","Icon"));
        trayIcon.setToolTip("HETEC Transaccional");
        trayIcon.setImageAutoSize(true);
        
        final SystemTray tray = SystemTray.getSystemTray();
        
        final PopupMenu menu = new PopupMenu();
        
        MenuItem about = new MenuItem("Informacion");
        MenuItem exit = new MenuItem("Salir");
        
        menu.add(about);
        menu.addSeparator();
        menu.add(exit);
        
    about.addActionListener((ActionEvent e) -> {
        JOptionPane.showMessageDialog(null, "HETEC Transaccional versión 1.2.6 /nAuthor by: /ItasoSolutions");
        });
    
     exit.addActionListener((ActionEvent e) -> {
         System.exit(0);
        });
            
 
 
     
        trayIcon.setPopupMenu(menu);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            
        }
        
    }
    
    protected static Image createIcon(String path, String desc){
        URL imageURL = HETEC.class.getResource(path);
        return (new ImageIcon(imageURL,desc)).getImage();
    }
    /*TODO ESTO ES PARA EL MENU QUE ESTA POR LA HORA*/ 
    
}


