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
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
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
public class PantallaPlats {
    // Elements UI
    private JFrame f;
    private JPanel panellSup, panellCtl;
    private JComboBox cboCategories;
    private JButton btnAnullarSeleccioCat;
    private JRadioButton rdoDispSi, rdoDispNo, rdoDispTotes;
    private ButtonGroup grupRadios;
    private JButton btnCerca;

    // Subfinestra d'escandalls
    private PantallaEscandall subfEscandall;

    // Capa de persistència
    private EPCookomatic cp;
    private List<Categoria> llistaCategories = new ArrayList<>();
    private List<Plat> llistaPlats;// = new ArrayList<>();
    private List<LiniaEscandall> llistaEscandall = new ArrayList<>();

    // Taula plats
    private JScrollPane scrollTaula;
    private JTable taulaPlats;
    private String[] titolsColumnes = new String[]{"NOM DEL PLAT", "PREU"};
    private DefaultTableModel modelPlats = new DefaultTableModel();

    
    public PantallaPlats(String titol, String nomFitxerPropietats) {
        f = new JFrame(titol);
        cp = new EPCookomatic(nomFitxerPropietats);

        afegirElements();
        prepararSubfinestra();
        
        f.setVisible(true);
        f.pack();
        f.setSize(700, 400);
        f.setResizable(false); // no permetre modificar la mida de la finestra
        f.setLocationRelativeTo(null); // ubicar l'aplicació al monitor tant pixels x,y respecte el punt 0,0, superior,esquerra
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // MOLT IMPORTANT: tancar connexio i capa de persistència
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Tancant cp");
                cp.close();
                System.out.println("cp tancada");
            }
        });        
    }

    
    

    private void afegirElements() {
        panellCtl = new JPanel();
        panellCtl.setLayout(new BoxLayout(panellCtl, BoxLayout.X_AXIS));

        // Ini models
        llistaCategories = new ArrayList<>();
        llistaCategories = cp.getCategories();
        llistaPlats = new ArrayList<>();

        // Combobox categories
        cboCategories = new JComboBox(new DefaultComboBoxModel(llistaCategories.toArray()));
        cboCategories.setSelectedItem(null); // inicialment cap categoria seleccionada

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
        panellCategories.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        

        panellCtl.add(panellCategories);


        // RadioButtons: si, no, totes
        rdoDispSi = new JRadioButton("Sí");
        rdoDispNo = new JRadioButton("No");
        rdoDispTotes = new JRadioButton("Totes");
        rdoDispTotes.setSelected(true);
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

        panellCtl.add(pnlChk);

        
        // Botó de cerca
        btnCerca = new JButton("Cerca!");
        btnCerca.addActionListener(new FiltreCerca());
        JPanel panellBtn = new JPanel();
        panellBtn.add(btnCerca);
        
        panellCtl.add(panellBtn);
        panellCtl.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));


        // Títol
        panellSup = new JPanel();
        panellSup.setLayout(new BoxLayout(panellSup, BoxLayout.PAGE_AXIS));
        panellSup.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel panellTitol = new JPanel();
        panellTitol.setLayout(new BoxLayout(panellTitol, BoxLayout.PAGE_AXIS));

        JLabel lblTitol = new JLabel("Restaurant El Bon Profit");
        lblTitol.setFont(new Font("Serif", Font.ITALIC, 30));
        lblTitol.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblSubtitol = new JLabel("Gestió d'escandalls");
        lblSubtitol.setFont(new Font("Serif", Font.ITALIC, 20));
        lblSubtitol.setHorizontalAlignment(SwingConstants.CENTER);

        panellTitol.add(lblTitol);
        panellTitol.add(lblSubtitol);
        
        panellTitol.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        panellSup.add(panellTitol);
        panellSup.add(panellCtl);
        panellSup.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        
        f.add(panellSup, BorderLayout.NORTH);
        iniTaula();
    }

    
    private void prepararSubfinestra() {
        subfEscandall = new PantallaEscandall("Escandall", null, cp, null, this);
    }

    

    //---------------------------------------------------------------------------------------------------------------------------------------------
    // TAULA DE PLATS
    
    private void iniTaula() {
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

        // donar color a les files de la taula
        PlatTableCellRenderer renderer = new PlatTableCellRenderer(llistaCategories);
        taulaPlats.setDefaultRenderer(taulaPlats.getColumnClass(0), renderer);

        iniScrollTaula();

        // Escoltador que obrirà menu d'escandall quan fem doble-click sobre la taula
        taulaPlats.addMouseListener(new ObrirEscandall());

        // afegir JTable dins el JFrame
        f.add(scrollTaula, BorderLayout.CENTER);
    }

    
    private void iniScrollTaula() {
        scrollTaula = new JScrollPane(taulaPlats, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTaula.setPreferredSize(new Dimension(400, 200));

        Border marc = BorderFactory.createLineBorder(Color.DARK_GRAY, 5);
        scrollTaula.setBorder(marc);
    }


    private void iniModelPlats() {
        modelPlats = new DefaultTableModel();
        modelPlats.setColumnIdentifiers(titolsColumnes);
        for (int i = 0; i < titolsColumnes.length; i++) {
            modelPlats.addColumn(titolsColumnes[i]);
        }
        modelPlats.setColumnCount(2);
    }


    private void omplirModel() {
        for (Plat plat : llistaPlats) {
            Object[] obj = new Object[2];

            obj[0] = plat; // necessitem una columna que desi l'objecte plat sencer
            obj[1] = plat.getPreu();

            modelPlats.addRow(obj);
        }
    }

    
    private void buidarModel() {
        modelPlats.setRowCount(0);
    }

    
    private void actualitzarModel() {
        buidarModel();
        omplirModel();
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------
    // SUBFINESTRA ESCANDALLS
    // Subfinestra actualitza el plat:
    public void actualitzarPlat(Plat platActualitzat) {
        llistaPlats = cp.getPlats();
        actualitzarModel();
    }


    
    //---------------------------------------------------------------------------------------------------------------------------------------------
    // LISTENERS

    // Doble click sobre fila i s'obre subfinestra escandalls
    private class ObrirEscandall implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("has clicat sobre taula");
            if (e.getClickCount() == 2) {
                System.out.println("doble click");

                if (taulaPlats.getSelectedRow() != -1) {
                    // Han seleccionat un plat, procedim a veure el seu escandall
                    Plat plat = (Plat) taulaPlats.getValueAt(taulaPlats.getSelectedRow(), 0);

                    for (LiniaEscandall le : plat.getEscandall()) {
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
    
    // Click sobre botó Cerca i s'executa aquesta
    private class FiltreCerca implements ActionListener {

        public FiltreCerca() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Categoria catSeleccionada = null;

            // si han seleccionat una categoria
            if (cboCategories.getSelectedItem() != null) {
                catSeleccionada = (Categoria) cboCategories.getSelectedItem();
            }

            // Filtrar per disponibilitat
            Boolean disponible = null;
            if (rdoDispSi.isSelected()) {
                disponible = true;
            } else if (rdoDispNo.isSelected()) {
                disponible = false;
            }

            llistaPlats = cp.getPlatsFiltrats(catSeleccionada, disponible);
            actualitzarModel();
        }
    }


}
