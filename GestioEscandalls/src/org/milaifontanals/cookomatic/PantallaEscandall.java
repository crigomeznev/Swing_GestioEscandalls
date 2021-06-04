/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.cookomatic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javafx.scene.paint.Color;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.cookomatic.model.cuina.Categoria;
import org.cookomatic.model.cuina.Ingredient;
import org.cookomatic.model.cuina.LiniaEscandall;
import org.cookomatic.model.cuina.Plat;
import org.cookomatic.model.cuina.Unitat;
import org.milaifontanals.persistence.EPCookomatic;

/**
 *
 * @author Usuari
 */
public class PantallaEscandall extends JDialog {
    // CONSTANTS
    private final int MAXQUANTITAT = 10000;
    
    // Dades
    private Plat plat;
    private List<LiniaEscandall> escandall = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Unitat> unitats = new ArrayList<>();

    // GUI
    private JPanel panellSup, panellInf;
    private JLabel lblPlatCodi, lblPlatNom, lblPlatPreu, lblPlatCategoria;
    private JTextArea txtPlatDesc;
    private JLabel lblPlatDisponible;

    // Taula escandall
    private JTable taulaEscandall;
    private String[] titolsEscandall = new String[]{"", "NUM", "INGREDIENT", "QUANTITAT", "UNITAT"};
    private DefaultTableModel modelEscandall = new DefaultTableModel();
    private JButton btnAfegirLinia, btnEliminarLinia;
    private JSpinner spnQuantitat;
    private JComboBox cboIngredients, cboUnitats;

    // Hibernate
    private EPCookomatic cp;

    // Referència a altra window
    private PantallaPlats parent;



    public PantallaEscandall(String titol, Plat plat, EPCookomatic cp, List<LiniaEscandall> escandall, PantallaPlats parent) {
        //BD
        this.cp = cp;
        this.parent = parent;

        this.unitats = this.cp.getUnitats();
        this.ingredients = this.cp.getIngredients();

        afegirElements();
        pack();
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(false);
        setPlat(plat);
    }


//-----------------------------------------------------------------------------------------
// GETTERS & SETTERS
    public Plat getPlat() {
        return plat;
    }

    public void setPlat(Plat plat) {
        this.plat = plat;

        if (plat != null) {
            // Canvis en la GUI
            lblPlatCodi.setText(plat.getCodi() + "");
            lblPlatNom.setText(plat.getNom());
            txtPlatDesc.setText(plat.getDescripcioMD());
            lblPlatPreu.setText("Preu: " + plat.getPreu() + "€");
            lblPlatCategoria.setText("Categoria: " + plat.getCategoria().getNom());
            lblPlatDisponible.setText(plat.isDisponible()? "Disponible" : "No disponible");

            // copiem escandall
            this.escandall = plat.getEscandall();
            buidarModel();
            omplirModel();
        }
    }

    public List<LiniaEscandall> getEscandall() {
        return escandall;
    }

    public void setEscandall(List<LiniaEscandall> escandall) {
        this.escandall = escandall;
    }

    // Nova linia d'escandall
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Unitat> getUnitats() {
        return unitats;
    }

    public void setUnitats(List<Unitat> unitats) {
        this.unitats = unitats;
    }
    



//-----------------------------------------------------------------------------------------
// MÈTODES GUI
    private void afegirElements() {
        panellSup = new JPanel();
        panellSup.setLayout(new BorderLayout());
        panellInf = new JPanel();

        lblPlatCodi = new JLabel();
        lblPlatNom = new JLabel();
        lblPlatNom.setFont(new Font("Serif", Font.ITALIC, 20));
        txtPlatDesc = new JTextArea();
        txtPlatDesc.setEditable(false);
        txtPlatDesc.setLineWrap(true);
        txtPlatDesc.setWrapStyleWord(true);
        txtPlatDesc.setColumns(50);
        txtPlatDesc.setRows(5);
        
        // Amplada fixa:
        txtPlatDesc.setColumns(150);
        lblPlatPreu = new JLabel();
        lblPlatPreu.setFont(new Font("Serif", Font.PLAIN, 14));
        lblPlatCategoria = new JLabel();
        lblPlatCategoria.setFont(new Font("Serif", Font.PLAIN, 14));
        lblPlatDisponible = new JLabel();
        lblPlatDisponible.setFont(new Font("Serif", Font.PLAIN, 14));

        JPanel panellNom = new JPanel();
        panellNom.setLayout(new BoxLayout(panellNom, BoxLayout.PAGE_AXIS));
        JPanel panellPreu = new JPanel();
        panellPreu.setLayout(new BoxLayout(panellPreu, BoxLayout.Y_AXIS));

        panellNom.add(lblPlatNom);
        panellNom.add(txtPlatDesc);
        panellPreu.add(lblPlatPreu);
        panellPreu.add(lblPlatCategoria);
        panellPreu.add(lblPlatDisponible);

        panellNom.setBorder(new EmptyBorder(10, 10, 10, 10));
        panellPreu.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        Color colorBeige = Color.BEIGE;
        java.awt.Color colorAwt = new java.awt.Color((float)colorBeige.getRed(), (float)colorBeige.getGreen(), (float)colorBeige.getBlue());
        
        panellNom.setBackground(colorAwt);

        panellSup.add(panellNom, BorderLayout.CENTER);
        panellSup.add(panellPreu, BorderLayout.EAST);

        afegirTaulaEscandall();

        prepararFormLiniaEscandall();

        add(panellSup, BorderLayout.NORTH);
        add(panellInf, BorderLayout.SOUTH);
    }

    
    // Taula escandall
    private void afegirTaulaEscandall() {
        // ini model
        modelEscandall = new DefaultTableModel();
        modelEscandall.setColumnIdentifiers(titolsEscandall);
        for (int i = 0; i < titolsEscandall.length; i++) {
            modelEscandall.addColumn(titolsEscandall[i]);
        }
        modelEscandall.setColumnCount(5);

        // ini taula
        taulaEscandall = new JTable(modelEscandall) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // retorna el tipus que ha de gestionar la classe
            @Override
            public Class<?> getColumnClass(int column) {
                Class clazz = String.class;
                switch (column) {
                    case 0:
                        clazz = LiniaEscandall.class;
                        break;
                    case 1:
                        clazz = String.class;
                        break;
                    case 2:
                        clazz = String.class;
                        break;
                    case 3:
                        clazz = String.class;
                        break;
                    case 4:
                        clazz = String.class;
                        break;
                }

                return clazz;
            }
        }; // definició / construcció de la taula

        // Amaguem la primera columna
        taulaEscandall.getColumnModel().getColumn(0).setMinWidth(0);
        taulaEscandall.getColumnModel().getColumn(0).setMaxWidth(0);
        
        // Text centrat
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        taulaEscandall.setDefaultRenderer(String.class, centerRenderer);
        
        JScrollPane scroll = new JScrollPane(taulaEscandall, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(400, 200));

        Border marc = BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY, 10);
        scroll.setBorder(marc);

        // Títol per a la taula d'escandalls
        JPanel panellTaula = new JPanel();
        panellTaula.setLayout(new BoxLayout(panellTaula, BoxLayout.PAGE_AXIS));

        JLabel lblTaula = new JLabel("Escandall");
        lblTaula.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        lblTaula.setAlignmentX(SwingConstants.CENTER);
        
        panellTaula.add(lblTaula);
        panellTaula.add(scroll);
        
//        add(scroll, BorderLayout.CENTER);
        add(panellTaula, BorderLayout.CENTER);
    }

    
    private void prepararFormLiniaEscandall() {
        // Combobox i models
        cboIngredients = new JComboBox(new DefaultComboBoxModel(ingredients.toArray()));
        cboUnitats = new JComboBox(new DefaultComboBoxModel(unitats.toArray()));

        // Altres camps
        SpinnerNumberModel spnQuantitatModel = new SpinnerNumberModel(1, 1, MAXQUANTITAT, 1); // TODO: spinner diferent depenent de l'unitat triada
        spnQuantitat = new JSpinner(spnQuantitatModel);

        JPanel panellIng = new JPanel();
        panellIng.setLayout(new BoxLayout(panellIng, BoxLayout.Y_AXIS));
        panellIng.add(new JLabel("Ingredient"));
        panellIng.add(cboIngredients);

        JPanel panellQtat = new JPanel();
        panellQtat.setLayout(new BoxLayout(panellQtat, BoxLayout.Y_AXIS));
        panellQtat.add(new JLabel("Quantitat"));
        panellQtat.add(spnQuantitat);

        JPanel panellUnitat = new JPanel();
        panellUnitat.setLayout(new BoxLayout(panellUnitat, BoxLayout.Y_AXIS));
        panellUnitat.add(new JLabel("Unitats"));
        panellUnitat.add(cboUnitats);

        JPanel panellBotons = new JPanel();
        panellBotons.setLayout(new BoxLayout(panellBotons, BoxLayout.Y_AXIS));
        btnAfegirLinia = new JButton("Afegir línia");
        btnAfegirLinia.addActionListener(new GestioLiniaEscandall());
        btnEliminarLinia = new JButton("Eliminar línia");
        btnEliminarLinia.addActionListener(new GestioLiniaEscandall());
        panellBotons.add(btnAfegirLinia);
        panellBotons.add(btnEliminarLinia);

        panellInf.add(panellIng);
        panellInf.add(panellQtat);
        panellInf.add(panellUnitat);
        panellInf.add(panellBotons);
    }

    
    
    
//-----------------------------------------------------------------------------------------
// MODEL
    private void omplirModel() {
        for (LiniaEscandall linia : escandall) {
            Object[] obj = new Object[5];

            obj[0] = linia; // necessitem una columna que desi l'objecte plat sencer
            obj[1] = linia.getNum(); // necessitem una columna que desi l'objecte plat sencer
            obj[2] = linia.getIngredient().getNom();
            obj[3] = linia.getQuantitat();
            obj[4] = linia.getUnitat().getNom();

            modelEscandall.addRow(obj);
        }
    }

    
    private void buidarModel() {
        modelEscandall.setRowCount(0);
    }
    
    
    private void actualitzarModel(){
        buidarModel();
        omplirModel();
    }





//-----------------------------------------------------------------------------------------
// BOTONS
    private class GestioLiniaEscandall implements ActionListener {

        public GestioLiniaEscandall() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton boto = (JButton) e.getSource();

            // INSERT
            if (boto.equals(btnAfegirLinia)) {
                System.out.println("Afegir línia");
                
                if (comprovarFormValid()){
                    int num = plat.getMinimNumDisponibleLiniaEscandall();
                    int qtat = (int)spnQuantitat.getValue();
                    Unitat u = (Unitat)cboUnitats.getSelectedItem();
                    Ingredient i = (Ingredient)cboIngredients.getSelectedItem();
                    
                    LiniaEscandall linia = new LiniaEscandall(num, qtat, i, u);
                    cp.inserirLiniaEscandall(linia, plat);
                    actualitzarModel();
                    parent.actualitzarPlat(plat);
                }
                
            } 
            // DELETE
            else if (boto.equals(btnEliminarLinia)) {
                System.out.println("Eliminar línia");

                if (taulaEscandall.getSelectedRow() != -1) {
                    LiniaEscandall le = escandall.get(taulaEscandall.getSelectedRow());
                    System.out.println(le);

                    cp.eliminarLiniaEscandall(le, plat);
                    actualitzarModel();
                    parent.actualitzarPlat(plat);
                }
            }
        }
    
        public boolean comprovarFormValid(){
            return cboIngredients.getSelectedItem() != null &&
                    cboUnitats.getSelectedItem() != null;
        }
    
    }
}
