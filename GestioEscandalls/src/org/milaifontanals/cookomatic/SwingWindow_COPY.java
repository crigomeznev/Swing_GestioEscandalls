/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.cookomatic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.MutableComboBoxModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import org.milaifontanals.cookomatic.model.cuina.Categoria;
import org.milaifontanals.cookomatic.model.cuina.Plat;
import org.milaifontanals.persistence.EPCookomatic;

/**
 *
 * @author Usuari
 */
public class SwingWindow_COPY {
    private JFrame f;
    private JPanel panellEsq, panellDta;
    
//    private DefaultListModel modelLlista;
    private MutableComboBoxModel<String> cboModel;
    private ArrayList<String> categories = new ArrayList<>(){};
    private JComboBox cboCategories;
    private JRadioButton rdoDispSi, rdoDispNo, rdoDispTotes;
    private ButtonGroup grupRadios;
    
    private JButton btnCerca;
    
    private JTable taulaPlats;
    private DefaultTableModel taulaPlatsModel;
    private String[] titolsColumnes = new String[] {"CODI","NOM","PREU","DISPONIBLE","CATEGORIA"};


    // Capa de persistència
    private EPCookomatic cp;
    private String nomFitxerPropietats;
    private List<Categoria> llistaCategories = new ArrayList<>();
    private List<Plat> llistaPlats = new ArrayList<>();

//    private DefaultTableModel model = new DefaultTableModel();


/*
Visualitzar la llista de plats, i poder filtrar per:
    - Categoria (amb un desplegable),
    de manera que si no hi ha categoria seleccionada visualitzi tots els plats.
    Els plats cal visualitzar-los en el color assignat a la seva categoria.
    
    - Disponibilitat, amb 3 botons de selecció (Si, No, Totes) 
    
    L’aplicació ha de facilitar un botó Cercar per a que l’usuari executi la cerca.
    En posar en marxa l’aplicació, no hi haurà cap categoria seleccionada i botó
    Disponibilitat estarà a Totes, però sense efectuar cap cerca fins que l’usuari
    l’executa via botó Cercar.     
    
    */


    public SwingWindow_COPY(String titol, String nomFitxerPropietats) {
        // crear capa de persistència
        cp = new EPCookomatic(nomFitxerPropietats);
                
        f= new JFrame(titol);
        
        afegirElements();
        
        f.setVisible(true);
        f.pack();
        f.setResizable(false); // no permetre modificar la mida de la finestra
        f.setLocation(10,300); // ubicar l'aplicació al monitor tant pixels x,y respecte el punt 0,0, superior,esquerra
        // +x, més a la dreta
        // +y, més avall
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
    }

    private void afegirElements() {
        panellEsq = new JPanel();
        panellEsq.setLayout(new BoxLayout(panellEsq, BoxLayout.Y_AXIS));
        panellDta = new JPanel();
        panellDta.setLayout(new FlowLayout());
        
        cboCategories = new JComboBox<String>();
        // TODO: carregar cboCategories amb dades BD

        // Ini cboModel
        llistaCategories = cp.getCategories();
        llistaPlats = cp.getPlats();
        
        cboCategories = new JComboBox(new DefaultComboBoxModel(llistaCategories.toArray()));
        
        panellEsq.add(cboCategories);
        
        // RadioButtons: si, no, totes
        rdoDispSi = new JRadioButton("Sí");
        rdoDispNo = new JRadioButton("No");
        rdoDispTotes = new JRadioButton("Totes");
        grupRadios = new ButtonGroup();
        
        grupRadios.add(rdoDispSi);
        grupRadios.add(rdoDispNo);
        grupRadios.add(rdoDispTotes);

        panellEsq.add(rdoDispSi);
        panellEsq.add(rdoDispNo);
        panellEsq.add(rdoDispTotes);
        
        btnCerca = new JButton("Cerca!");
        btnCerca.addActionListener(null);

        panellEsq.add(btnCerca);
        
        f.add(panellEsq, BorderLayout.NORTH);
        
        
        
        // panell central: llista de plats
        
        
//        omplirTaula();
        construirTaula();
        

        panellDta.add(taulaPlats);
        
        f.add(panellDta, BorderLayout.CENTER);
    }

    
    private void omplirTaula() {
        // TODO: ini titols columnes amb dades reals de la BD
        String bdInfo[][] = obtenirMatriu();
        
        taulaPlats = new JTable(bdInfo, titolsColumnes);
        
    }

    
    private String[][] obtenirMatriu() {
        String matriuInfo[][] = new String[llistaPlats.size()][titolsColumnes.length];
        
        for (int i = 0; i < llistaPlats.size(); i++) {
            Plat p = llistaPlats.get(i);
            
            matriuInfo[i][0] = p.getNom();
            matriuInfo[i][1] = p.getDescripcioMD();
            matriuInfo[i][2] = p.getPreu()+"";
//            matriuInfo[i][0] = p.getNom(); // TODO GET FOTO
            matriuInfo[i][3] = p.isDisponible()+"";
            matriuInfo[i][4] = p.getCategoria().toString();
        }
        return matriuInfo;
    }
    
    // NOMÉS DE PROVA, ELIMINAR!!!!
    private void construirTaula() {
        taulaPlatsModel = new DefaultTableModel();
        taulaPlatsModel.setColumnIdentifiers(titolsColumnes);
        taulaPlats = new JTable(taulaPlatsModel) {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column)
            {
                Class clazz = String.class;
                switch (column)
                {
                    case 0: case 1: clazz = String.class;
                                    break;
                    case 2:         clazz = Integer.class;
                                    break;
                }
                return clazz;
            }
        }; // definició / construcció de la taula
        // afegir la fila amb els titols
        
        for (int i=0;i<titolsColumnes.length;i++)
        {
            taulaPlatsModel.addColumn(titolsColumnes[i]);
        }
        
        // ajustar mida de columnes
//        taulaPlats.getColumnModel().getColumn(0).setPreferredWidth(170);
//        taulaPlats.getColumnModel().getColumn(1).setPreferredWidth(130);
//        taulaPlats.getColumnModel().getColumn(2).setPreferredWidth(50);
        
        // Omplir taula amb dades de la BD
//        omplirTaula();
        
//        taula.getSelectionModel().addListSelectionListener(new GestioFiles());
        
//        JScrollPane scroll = new JScrollPane(taulaPlats,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        
//        scroll.setPreferredSize(new Dimension(400,200));
//        
//        Border marc = BorderFactory.createLineBorder(f.getBackground(),10);
//        scroll.setBorder(marc);
        
        
        // afegir JScrollPane dins el JFrame
//        f.add(scroll, BorderLayout.CENTER);

            panellDta.add(taulaPlats);
        
    }

    
}
