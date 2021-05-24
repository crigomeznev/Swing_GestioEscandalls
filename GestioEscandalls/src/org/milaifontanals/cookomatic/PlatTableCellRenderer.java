/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.cookomatic;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.milaifontanals.cookomatic.model.cuina.Categoria;
import org.milaifontanals.cookomatic.model.cuina.Plat;

/**
 *
 * @author Usuario
 */
public class PlatTableCellRenderer extends DefaultTableCellRenderer {
//    private List<Plat> plats = new ArrayList<>();
    private List<Categoria> categories = new ArrayList<>();
//    private DefaultTableModel modelPlats;

    public PlatTableCellRenderer(List<Categoria> categories) {
        this.categories = categories;
    }
    
    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // definim les característiques de cada cel·la
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        
//        modelPlats.ge
        Plat p = (Plat)table.getValueAt(row, 0);
        Categoria c = p.getCategoria();
        
        Color color = new Color(c.getColorInt());
        
        
        
        setBackground(color);
        
        setForeground(new Color(getComplementaryColor(c.getColorInt())));
//        if (isColorDark(color.getRGB()))
//            setForeground(Color.white);
//        else
//            setForeground(Color.BLACK);
        return this;
    }
    
    private int getComplementaryColor( int color) {
        int R = color & 255;
        int G = (color >> 8) & 255;
        int B = (color >> 16) & 255;
        int A = (color >> 24) & 255;
        R = 255 - R;
        G = 255 - G;
        B = 255 - B;
        return R + (G << 8) + ( B << 16) + ( A << 24);
    }  
    
    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red.getRGB() + 0.587*Color.green.getRGB() + 0.114*Color.blue.getRGB())/255;
        if(darkness<1){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }    
    
}
