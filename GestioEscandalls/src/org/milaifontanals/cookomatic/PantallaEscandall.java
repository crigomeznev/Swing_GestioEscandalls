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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.milaifontanals.cookomatic.model.cuina.Categoria;
import org.milaifontanals.cookomatic.model.cuina.Ingredient;
import org.milaifontanals.cookomatic.model.cuina.LiniaEscandall;
import org.milaifontanals.cookomatic.model.cuina.Plat;
import org.milaifontanals.cookomatic.model.cuina.Unitat;
import org.milaifontanals.persistence.EPCookomatic;

/**
 *
 * @author Usuari
 */
public class PantallaEscandall extends JDialog {

    // Dades
    private Plat plat;
    private List<LiniaEscandall> escandall = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Unitat> unitats = new ArrayList<>();

    // GUI
    private JPanel panellSup, panellMig, panellInf;
//    private JTextField txtPlatCodi, txtPlatNom, txtPlatDesc, txtPlatPreu, txtPlatCategoria;
    private JLabel lblPlatCodi, lblPlatNom, lblPlatDesc, lblPlatPreu, lblPlatCategoria;
    private JTextArea txtPlatDesc;
    private JCheckBox chkPlatDisponible;
    // Foto?

    // Taula escandall
    private JTable taulaEscandall;
    private String[] titolsEscandall = new String[]{"", "NUM", "INGREDIENT", "QUANTITAT", "UNITAT"};
    private DefaultTableModel modelEscandall = new DefaultTableModel();
    private JScrollPane scrollTaula;

    // DML
    private JButton btnAfegirLinia, btnEliminarLinia;

    // Hibernate
    private EPCookomatic cp;

    
    // Referència a altra window
    private SwingWindow parent;
    /*          
NUM
QUANTITAT
PLAT
INGREDIENT
UNITAT        
     */
    // Nova linia d'escandall
//    private JTextField txtNum, txtQuantitat; // TODO: numberbox
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

    private JSpinner spnNum, spnQuantitat;
    private JComboBox cboIngredients, cboUnitats;
    private MutableComboBoxModel<String> modelIngredients, modelUnitats;

    public PantallaEscandall(String titol, Plat plat, EPCookomatic cp, List<LiniaEscandall> escandall, SwingWindow parent) {
        //BD
//        this.unitats = unitats;
//        this.ingredients = ingredients;
        this.cp = cp;
        this.parent = parent;

        this.unitats = this.cp.getUnitats();
        this.ingredients = this.cp.getIngredients();

        afegirElements();
        pack();
//        subfEscandalls.setSize(250, 150);
        setSize(500, 300);
        setResizable(false);
        // centrar-lo al frame
        setLocationRelativeTo(null);
//        subfinestra.setLocationRelativeTo(f);
        setVisible(false);

        setPlat(plat);
//        setEscandall(escandall); // TODO: add, remove i ite per a escandall

    }

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
//            lblPlatDesc.setText(plat.getDescripcioMD());
            txtPlatDesc.setText(plat.getDescripcioMD());
            lblPlatPreu.setText(plat.getPreu() + "");
            lblPlatCategoria.setText(plat.getCategoria().getNom());
            chkPlatDisponible.setSelected(plat.isDisponible() ? true : false);

            // copiem escandall
//            this.escandall.clear();
            this.escandall = plat.getEscandall();
//            while(plat.iteEscandall().hasNext()){
//                escandall.add(plat.iteEscandall().next());
//            }
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

//-----------------------------------------------------------------------------------------
// MÈTODES GUI
    private void afegirElements() {
        panellSup = new JPanel();
        panellSup.setLayout(new BoxLayout(panellSup, BoxLayout.X_AXIS));
        panellMig = new JPanel();
        panellInf = new JPanel();

        panellSup.setBackground(java.awt.Color.ORANGE);
        panellMig.setBackground(java.awt.Color.cyan);
        panellInf.setBackground(java.awt.Color.magenta);

        lblPlatCodi = new JLabel();
        lblPlatNom = new JLabel();
        lblPlatDesc = new JLabel();
        txtPlatDesc = new JTextArea();
        txtPlatDesc.setEditable(false);
        txtPlatDesc.setWrapStyleWord(true);
        // Amplada fixa:
        txtPlatDesc.setColumns(150);
//        txtPlatDesc.setMaximumSize(new Dimension(200, 70));
        lblPlatPreu = new JLabel();
        lblPlatCategoria = new JLabel();
        chkPlatDisponible = new JCheckBox("Disponible?");
        chkPlatDisponible.setEnabled(false);

        JPanel panellNom = new JPanel();
        panellNom.setLayout(new BoxLayout(panellNom, BoxLayout.Y_AXIS));
        JPanel panellPreu = new JPanel();
        panellPreu.setLayout(new BoxLayout(panellPreu, BoxLayout.Y_AXIS));

        panellNom.add(lblPlatNom);
        panellNom.add(txtPlatDesc);
        panellPreu.add(lblPlatPreu);
        panellPreu.add(chkPlatDisponible);

        panellNom.setBackground(java.awt.Color.red);
        panellPreu.setBackground(java.awt.Color.yellow);

        panellSup.add(panellNom);
        panellSup.add(panellPreu);

        afegirTaulaEscandall();

        prepararFormLiniaEscandall();

        add(panellSup, BorderLayout.NORTH);
        add(panellMig, BorderLayout.CENTER);
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
                        clazz = Integer.class;
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

        // afegir la fila amb els titols
//        for (int i = 0; i < titolsEscandall.length; i++) {
//            modelEscandall.addColumn(titolsEscandall[i]);
//        }
//        omplirTaulaEscandall();
//        taulaPlats.getColumnModel().getColumn(0).setPreferredWidth(200);
//        taulaPlats.getColumnModel().getColumn(1).setPreferredWidth(100);
//        taulaPlats.getColumnModel().getColumn(2).setPreferredWidth(50);
//        taula.getSelectionModel().addListSelectionListener(new GestioFiles());
        JScrollPane scroll = new JScrollPane(taulaEscandall, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(400, 200));

//        Border marc = BorderFactory.createLineBorder(f.getBackground(),10);
        Border marc = BorderFactory.createLineBorder(java.awt.Color.GREEN, 10);
        scroll.setBorder(marc);
        // afegir JTable dins el JFrame
        panellMig.add(scroll);
    }

//    private void iniModels() {
////        cboModel.addElement("-------------");
//        modelIngredients = new DefaultComboBoxModel(ingredients.toArray());
//        modelUnitats = new DefaultComboBoxModel(unitats.toArray());
//    }
    private void omplirModel() {
        for (LiniaEscandall linia : escandall) {
            Object[] obj = new Object[5];

            obj[0] = linia; // necessitem una columna que desi l'objecte plat sencer
            obj[1] = linia.getNum(); // necessitem una columna que desi l'objecte plat sencer
            obj[2] = linia.getIngredient().getNom();
            obj[3] = linia.getQuantitat();
            obj[4] = linia.getUnitat().getNom();
//            obj[2] = plat.getNom();

            modelEscandall.addRow(obj);
        }
    }

    private void buidarModel() {
//        for (int i = 0; i < modelPlats.getRowCount(); i++) {
//            modelPlats.removeRow(i);
//        }
        modelEscandall.setRowCount(0);
    }
    
    private void actualitzarModel(){
        buidarModel();
        omplirModel();
    }

//    private void omplirTaulaEscandall() {
//        // TODO: ini titols columnes amb dades reals de la BD
//        String bdInfo[][] = obtenirMatriuEscandall();
//
//        taulaEscandall = new JTable(bdInfo, titolsEscandall);
//    }
//
//    private String[][] obtenirMatriuEscandall() {
//        String matriuInfo[][] = new String[llistaEscandall.size()][titolsEscandall.length];
//
//        for (int i = 0; i < llistaEscandall.size(); i++) {
//            LiniaEscandall l = llistaEscandall.get(i);
//
//            matriuInfo[i][0] = l.getNum() + "";
//            matriuInfo[i][1] = l.getQuantitat() + "";
//            matriuInfo[i][2] = l.getIngredient().getNom() + "";
//            matriuInfo[i][3] = l.getUnitat().getNom() + "";
//        }
//        return matriuInfo;
//    }
    private void prepararFormLiniaEscandall() {
        // Combobox i models
        cboIngredients = new JComboBox(new DefaultComboBoxModel(ingredients.toArray()));
        cboUnitats = new JComboBox(new DefaultComboBoxModel(unitats.toArray()));

        // Altres camps
        SpinnerNumberModel spnNumModel = new SpinnerNumberModel(1, 1, 30, 1);
        SpinnerNumberModel spnQuantitatModel = new SpinnerNumberModel(1, 1, 10, 1);
        spnQuantitat = new JSpinner(spnQuantitatModel);
        spnNum = new JSpinner(spnNumModel);
        // TODO: text de l'spinner no editable

        JPanel panellNum = new JPanel();
        panellNum.setLayout(new BoxLayout(panellNum, BoxLayout.Y_AXIS));
        panellNum.add(new JLabel("Número"));
        panellNum.add(spnNum);

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

        panellInf.add(panellNum);
        panellInf.add(panellIng);
        panellInf.add(panellQtat);
        panellInf.add(panellUnitat);
        panellInf.add(panellBotons);

//        panellInf.add(spnQuantitat);
    }

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
                    int num = (int)spnNum.getValue();
                    int qtat = (int)spnQuantitat.getValue();
                    Unitat u = (Unitat)cboUnitats.getSelectedItem();
                    Ingredient i = (Ingredient)cboIngredients.getSelectedItem();
                    
                    LiniaEscandall linia = new LiniaEscandall(plat.getPrimerLiniaEscandallNum(), qtat, i, u);
                    cp.inserirLiniaEscandall(linia, plat);
                    actualitzarModel();
                    parent.actualitzarPlat(plat);
                }
                
                
                
                
                
                
                
                
                

            } 
            // DELETE
            else if (boto.equals(btnEliminarLinia)) {
                System.out.println("Eliminar línia");

//                LiniaEscandall le = (LiniaEscandall)taulaEscandall.getValueAt(taulaEscandall.getSelectedRow(), 0);
//                System.out.println(le);
                if (taulaEscandall.getSelectedRow() != -1) {
                    LiniaEscandall le = escandall.get(taulaEscandall.getSelectedRow());
                    System.out.println(le);

                    cp.eliminarLiniaEscandall(le, plat);
                    actualitzarModel();
                    parent.actualitzarPlat(plat);
                    
                    
//                    ingredients = cp.getIngredients();

//                cp.remove();

                }

            }
        }
    
        public boolean comprovarFormValid(){
            return cboIngredients.getSelectedItem() != null &&
                    cboUnitats.getSelectedItem() != null;
        }
    
    }
}
