/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.cookomatic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
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
public class SwingWindow {
    private JFrame f;
    private JPanel panellEsq;
    
//    private DefaultListModel modelLlista;
    private MutableComboBoxModel<String> cboModel;
    private ArrayList<String> categories;// = new ArrayList<>(){};
    private JComboBox cboCategories;
    private JButton btnAnullarSeleccioCat;
    private JRadioButton rdoDispSi, rdoDispNo, rdoDispTotes;
    private ButtonGroup grupRadios;
    
    private JButton btnCerca, btnEditEscandall;
    
    
    // Subfinestra d'escandalls
    private PantallaEscandall subfEscandall;
    
//    private JDialog subfEscandalls;
    private JPanel esc_panellSup, esc_panellInf;
    private JTextField esc_txtNom;
    private TextArea esc_txtDescripcio;
    
    
    // Capa de persistència
    private EPCookomatic cp;
    private String nomFitxerPropietats;
    private List<Categoria> llistaCategories = new ArrayList<>();
    private List<Plat> llistaPlats;// = new ArrayList<>();
    private List<LiniaEscandall> llistaEscandall = new ArrayList<>();

    // Per a la subfinestra d'escandalls
    private List<Ingredient> llistaIngredients = new ArrayList<>();
    private List<Unitat> llistaUnitats = new ArrayList<>();
  
    // Taula plats
    private JScrollPane scrollTaula;
    private JTable taulaPlats;
    private String[] titolsColumnes = new String[] {"NOM","PREU"};
    private DefaultTableModel modelPlats = new DefaultTableModel();

    

    
    
    
    public SwingWindow(String titol, String nomFitxerPropietats) {
        f = new JFrame(titol);
        
        cp = new EPCookomatic(nomFitxerPropietats);

        llistaIngredients = cp.getIngredients();
        llistaUnitats = cp.getUnitats();
        
        
        afegirElements();
//        iniTaula();
        prepararSubfinestra();
        f.setVisible(true);
        f.pack();
        f.setSize(700, 400);
        f.setResizable(false); // no permetre modificar la mida de la finestra
        f.setLocation(10, 300); // ubicar l'aplicació al monitor tant pixels x,y respecte el punt 0,0, superior,esquerra
        // +x, més a la dreta
        // +y, més avall
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void iniCboModel(){
//        cboModel.addElement("-------------");
        cboModel = new DefaultComboBoxModel(llistaCategories.toArray());
//        cboModel.insertElementAt("------------", 0);
        cboModel.addElement("-------------");
    }

    private void afegirElements() {
        panellEsq = new JPanel();
        panellEsq.setLayout(new BoxLayout(panellEsq, BoxLayout.X_AXIS));
        
        cboCategories = new JComboBox<String>();
        // TODO: carregar cboCategories amb dades BD

        // Ini cboModel
        llistaCategories = new ArrayList<>();
        llistaCategories = cp.getCategories();
        llistaPlats = new ArrayList<>();
//        llistaPlats = cp.getPlats();
        
        iniCboModel();
        
//        cboCategories = new JComboBox(new DefaultComboBoxModel(llistaCategories.toArray()));
        cboCategories = new JComboBox(new DefaultComboBoxModel(llistaCategories.toArray()));
        btnAnullarSeleccioCat = new JButton("Anul·la la selecció");
        btnAnullarSeleccioCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cboCategories.setSelectedItem(null);
            }
        });
        JPanel panellCategories = new JPanel();
        panellCategories.setLayout(new BoxLayout(panellCategories, BoxLayout.Y_AXIS));
        panellCategories.add(cboCategories);
        panellCategories.add(btnAnullarSeleccioCat);
        panellEsq.add(panellCategories);
        
        // RadioButtons: si, no, totes
        rdoDispSi = new JRadioButton("Sí");
        rdoDispNo = new JRadioButton("No");
        rdoDispTotes = new JRadioButton("Totes");
        rdoDispTotes.setSelected(true);
//        rdoDispTotes.setEnabled(true);
        grupRadios = new ButtonGroup();
        
        grupRadios.add(rdoDispSi);
        grupRadios.add(rdoDispNo);
        grupRadios.add(rdoDispTotes);

        Border brdChk = BorderFactory.createLineBorder(Color.GRAY, 1);
        TitledBorder tbrdChk = new TitledBorder(brdChk, "Disponibilitat");
        JPanel pnlChk = new JPanel();
        pnlChk.add(rdoDispSi);
        pnlChk.add(rdoDispNo);
        pnlChk.add(rdoDispTotes);
        pnlChk.setBorder(tbrdChk);

        panellEsq.add(pnlChk);

        btnCerca = new JButton("Cerca!");
        btnCerca.addActionListener(new FiltreCerca());

//        btnEditEscandall = new JButton("Editar escandall");
//        btnEditEscandall.addActionListener(new GestioEscandall());
        
        panellEsq.add(btnCerca);
//        panellEsq.add(btnEditEscandall);
        
        
        
        
        iniTaula();

        
        f.add(panellEsq, BorderLayout.NORTH);
//        f.add(panellDta, BorderLayout.CENTER);
    }

    
    private void prepararSubfinestra() {
        subfEscandall = new PantallaEscandall("Escandall", null, cp, null, this);

//        subfinestra.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    }
    
    
    private void iniModelPlats() {
        modelPlats = new DefaultTableModel();
        modelPlats.setColumnIdentifiers(titolsColumnes);
        for (int i = 0; i < titolsColumnes.length; i++) {
            modelPlats.addColumn(titolsColumnes[i]);
        }
        modelPlats.setColumnCount(2);
    }
    
    private void iniTaula() {
//        taulaPlats = new JTable(modelPlats);
        iniModelPlats();
        omplirModel();
        
        taulaPlats = new JTable(modelPlats) {
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
                        clazz = Plat.class;
                        break;
                    case 1:
                        clazz = Plat.class;
                        break;
                    case 2:
                        clazz = Plat.class;
                        break;
                }
                return clazz;
            }
        }; // definició / construcció de la taula
        // afegir la fila amb els titols
//        omplirTaula();

        
        // donar color a les files de la taula
        PlatTableCellRenderer renderer = new PlatTableCellRenderer(llistaCategories);
        taulaPlats.setDefaultRenderer(taulaPlats.getColumnClass(0), renderer);


        iniScrollTaula();
        
        // Escoltador que obrirà menu d'escandall quan fem doble-click sobre la taula
        taulaPlats.addMouseListener(new ObrirEscandall());
        
        // afegir JTable dins el JFrame
        f.add(scrollTaula, BorderLayout.CENTER);
    }
    
    private void iniScrollTaula(){
        scrollTaula = new JScrollPane(taulaPlats, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTaula.setPreferredSize(new Dimension(400, 200));
        
        Border marc = BorderFactory.createLineBorder(Color.DARK_GRAY,5);
        scrollTaula.setBorder(marc);        
    }
    
    
    //---------------------------------------------------------------------------------------------------------------------------------------------
    // MODEL DE TAULA PLATS
    private void omplirModel(){
        for (Plat plat : llistaPlats){
            Object[] obj = new Object[2];
            
            obj[0] = plat; // necessitem una columna que desi l'objecte plat sencer
            obj[1] = plat.getPreu();
//            obj[2] = plat.getNom();
            
            modelPlats.addRow(obj);
        }
    }
    
    private void buidarModel(){
//        for (int i = 0; i < modelPlats.getRowCount(); i++) {
//            modelPlats.removeRow(i);
//        }
        modelPlats.setRowCount(0);
    }
    
    private void actualitzarModel(){
        buidarModel();
        omplirModel();
    }
    
    private void buidarTaula(){
        System.out.println("buidant taula");
        
        buidarModel();
//        modelPlats.fireTableDataChanged();
    }
    
    

    // Subfinestra actualitza el plat:
    public void actualitzarPlat(Plat platActualitzat){
//        Plat plat = (Plat)taulaPlats.getValueAt(0, taulaPlats.getSelectedRow());
//        if (plat!=null){
//            plat = platActualitzat;
//        }
        System.out.println("actualitzar plats");
        llistaPlats = cp.getPlats();
        actualitzarModel();

    }

    
//    private void omplirTaula() {
//        // TODO: ini titols columnes amb dades reals de la BD
//        String bdInfo[][] = obtenirMatriu();
//        
//        taulaPlats = new JTable(bdInfo, titolsColumnes);
//    }
    
    
//    private String[][] obtenirMatriuEscandall() {
//        String matriuInfo[][] = new String[llistaEscandall.size()][titolsEscandall.length];
//        
//        for (int i = 0; i < llistaEscandall.size(); i++) {
//            LiniaEscandall l = llistaEscandall.get(i);
//            
//            matriuInfo[i][0] = l.getNum()+"";
//            matriuInfo[i][1] = l.getQuantitat()+"";
//            matriuInfo[i][2] = l.getIngredient().getNom()+"";
//            matriuInfo[i][3] = l.getUnitat().getNom()+"";
//        }
//        return matriuInfo;
//    }    



    private class FiltreCerca implements ActionListener {

        public FiltreCerca() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Categoria catSeleccionada = null;

            // si han seleccionat una categoria
            if (cboCategories.getSelectedItem() != null){
                catSeleccionada = (Categoria)cboCategories.getSelectedItem();
            }

            // Filtrar per disponibilitat
            Boolean disponible = null;
            if (rdoDispSi.isSelected())
                disponible = true;
            else if (rdoDispNo.isSelected())
                disponible = false;

            llistaPlats = cp.getPlatsFiltrats(catSeleccionada, disponible);
            actualitzarModel();
        }
    }


    private class ObrirEscandall implements MouseListener{
//        JTable taulaPlats;
        
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("has clicat sobre taula");
            if (e.getClickCount()==2){
                System.out.println("doble click");
                
                if (taulaPlats.getSelectedRow() != -1){
                    // Han seleccionat un plat, procedim a veure el seu escandall
                    Plat plat = (Plat)taulaPlats.getValueAt(taulaPlats.getSelectedRow(), 0);
                    
//                    List<LiniaEscandall> escandall = cp.getEscandallPerPlat(plat);
                    
//                    plat.setEscandall(escandall);
                    for(LiniaEscandall le : plat.getEscandall()){
                        System.out.println(le);
                    }

                    // "actualitzem" plat (el tornem a carregar de la bd
                    plat = cp.getPlatPerCodi(plat.getCodi());
                    subfEscandall.setPlat(plat);
                    subfEscandall.setVisible(true);
                    
                }
                
                
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        // HOVER
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
        
    }
    
    
//    private class GestioEscandall implements ActionListener {
//
//        public GestioEscandall() {
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            subfEscandalls.setVisible(true);
//        }
//    }
    
    
}
