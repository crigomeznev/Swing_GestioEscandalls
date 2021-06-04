/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.cookomatic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.cookomatic.model.cuina.Categoria;
import org.cookomatic.model.cuina.Plat;

/**
 *
 * @author Usuario
 */
public class PlatTableCellRenderer extends DefaultTableCellRenderer {
//    private List<Plat> plats = new ArrayList<>();
//    private List<Categoria> categories = new ArrayList<>();
//    private DefaultTableModel modelPlats;

    private Font font;
    private Border border;
    
    public PlatTableCellRenderer(List<Categoria> categories) {
//        this.categories = categories;
        font = new Font(Font.SERIF, Font.BOLD, 16);
        border = BorderFactory.createEmptyBorder(5,5,5,5);
    }
    
    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // definim les característiques de cada cel·la
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Text centrat
        setHorizontalAlignment(JLabel.CENTER);
        
        Plat p = (Plat)table.getValueAt(row, 0);
        Categoria c = p.getCategoria();
        
        Color color = new Color(c.getColorInt());

        setBackground(color);
        setForeground(new Color(getComplementaryColor(c.getColorInt())));
        setFont(font);
        setBorder(border);
        
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
    
}
