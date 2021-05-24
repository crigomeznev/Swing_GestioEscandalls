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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.milaifontanals.cookomatic.model.cuina.Categoria;
import org.milaifontanals.cookomatic.model.cuina.LiniaEscandall;
import org.milaifontanals.cookomatic.model.cuina.Plat;
import org.milaifontanals.persistence.EPCookomatic;

/**
 *
 * @author Usuari
 */
public class PantallaEscandall extends JDialog {

    // Dades
    private Plat plat;
    private List<LiniaEscandall> escandall = new ArrayList<>();

    // GUI
    private JPanel panellSup, panellMig, panellInf;
//    private JTextField txtPlatCodi, txtPlatNom, txtPlatDesc, txtPlatPreu, txtPlatCategoria;
    private JLabel lblPlatCodi, lblPlatNom, lblPlatDesc, lblPlatPreu, lblPlatCategoria;
    private JTextArea txtPlatDesc;
    private JCheckBox chkPlatDisponible;
    // Foto?

    // Taula escandall
    private JTable taulaEscandall;
    private String[] titolsEscandall = new String[]{"NUM", "QUANTITAT", "INGREDIENT", "UNITAT"};
    private DefaultTableModel modelEscandall = new DefaultTableModel();
    private JScrollPane scrollTaula;

    public PantallaEscandall(String titol, Plat plat, List<LiniaEscandall> escandall) {
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
            this.escandall.clear();
            while(plat.iteEscandall().hasNext()){
                escandall.add(plat.iteEscandall().next());
            }

            omplirModel();
        }
    }

    public List<LiniaEscandall> getEscandall() {
        return escandall;
    }

//    public void setEscandall(List<LiniaEscandall> escandall) {
//        this.escandall = escandall;
//    }

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
//        panellSup.setLayout(new BoxLayout(panellSup, BoxLayout.X_AXIS));
/*
        txtPlatCodi = new JTextField();
        txtPlatCodi.setEditable(false);
        txtPlatDesc = new JTextField();
        txtPlatDesc.setEditable(false);
        txtPlatPreu = new JTextField();
        txtPlatPreu.setEditable(false);
        txtPlatCategoria = new JTextField();
        txtPlatCategoria.setEditable(false);
        chkPlatDisponible = new JCheckBox("Disponible?");
        chkPlatDisponible.setEnabled(false);

        panellSup.add(txtPlatCodi);
        panellSup.add(txtPlatDesc);
        panellSup.add(txtPlatPreu);
        panellSup.add(txtPlatCategoria);
        panellSup.add(chkPlatDisponible);
         */

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

//        panellSup.add(lblPlatCodi);
//        panellSup.add(lblPlatNom);
//        panellSup.add(lblPlatDesc);
//        panellSup.add(lblPlatPreu);
//        panellSup.add(lblPlatCategoria);
//        panellSup.add(chkPlatDisponible);
//        Border brdChk = BorderFactory.createLineBorder(Color.GRAY, 1);
//        TitledBorder tbrdChk = new TitledBorder(brdChk, "Disponibilitat");
//        JPanel pnlChk = new JPanel();
//        pnlChk.add(rdoDispSi);
//        pnlChk.setBorder(tbrdChk);
        afegirTaulaEscandall();


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
        modelEscandall.setColumnCount(4);

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
                        clazz = Integer.class;
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
                }

                return clazz;
            }
        }; // definició / construcció de la taula
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

    

    private void omplirModel() {
        for (LiniaEscandall linia : escandall) {
            Object[] obj = new Object[4];

            obj[0] = linia.getNum(); // necessitem una columna que desi l'objecte plat sencer
            obj[1] = linia.getIngredient().getNom();
            obj[2] = linia.getQuantitat();
            obj[3] = linia.getUnitat().getNom();
//            obj[2] = plat.getNom();

            modelEscandall.addRow(obj);
        }
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
}
