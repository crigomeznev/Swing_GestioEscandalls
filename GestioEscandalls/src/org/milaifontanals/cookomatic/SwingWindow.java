/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.cookomatic;

import java.awt.BorderLayout;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
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
import javax.swing.JTable;
import javax.swing.MutableComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuari
 */
public class SwingWindow {
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
    private String[] columnes = new String[] {"CODI","NOM","PREU","DISPONIBLE","CATEGORIA"};
    private DefaultTableModel taulaModel = new DefaultTableModel();
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


    public SwingWindow(String titol) {
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
        
        cboCategories = new JComboBox<String>();
        // TODO: carregar cboCategories amb dades BD

        // Ini cboModel
        categories.add("categoria1");
        categories.add("categoria2");
        categories.add("categoria3");
        categories.add("categoria4");
        categories.add("categoria5");
        cboModel = new DefaultComboBoxModel<>();
        for (int i = 0; i < categories.size(); i++) {
            cboModel.addElement(categories.get(i));
        }
        cboCategories = new JComboBox(cboModel);
        
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
        
        f.add(panellEsq, BorderLayout.WEST);
        
        
        
        // panell central: llista de plats
        
        taulaModel.setColumnIdentifiers(columnes);
        taulaPlats = new JTable(taulaModel);
        
        omplirTaula();
        
//        taulaModel.addRow(new Object[]{"1","patates fregides","5","sí","pica-pica"});
//        taulaModel.addRow(new Object[]{"1","patates fregides","5","sí","pica-pica"});
//        taulaModel.addRow(new Object[]{"1","patates fregides","5","sí","pica-pica"});
//        taulaModel.addRow(new Object[]{"1","patates fregides","5","sí","pica-pica"});
//        taulaModel.addRow(new Object[]{"1","patates fregides","5","sí","pica-pica"});


        panellDta.add(taulaPlats);
        
        f.add(panellDta, BorderLayout.CENTER);
    }

    
    
    // NOMÉS DE PROVA, ELIMINAR!!!!
    private void omplirTaula() {
        String up = "UP-EclipseLink";

        Properties props = new Properties();

        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            em = null;
            emf = null;

            props.load(new FileReader("connexio.properties"));

            System.out.println("Intent amb " + up);
            emf = Persistence.createEntityManagerFactory(up, props);
            System.out.println("EntityManagerFactory creada");
            em = emf.createEntityManager();
            System.out.println();
            System.out.println("EntityManager creat");

            // "CODI","NOM","PREU","DISPONIBLE","CATEGORIA"
            Query q = em.createNativeQuery("select codi,nom,PREU,DISPONIBLE,CATEGORIA from PLAT");

            List<Object[]> l = q.getResultList();

            for (Object[] o : l) {
                long codi = (long)o[0];
                String nom = (String)o[1];
                BigDecimal preu = (BigDecimal)o[2];
                boolean disponible = (boolean)o[3];
                long categoria = (long)o[4];
                
                DefaultTableModel tblModel = (DefaultTableModel)taulaPlats.getModel();
                
                String s1 = String.valueOf(codi);
                String s3 = String.valueOf(preu);
                String s4 = String.valueOf(disponible);
                String s5 = String.valueOf(categoria);
                
                
                
                tblModel.addRow(new String[]{s1, nom, s3, s4, s5});
//                System.out.println("Ingredient "
//                        + o[0]
//                        + " "
//                        + o[1]);
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            System.out.print(ex.getCause() != null ? "Caused by:" + ex.getCause().getMessage() + "\n" : "");
            System.out.println("Traça:");
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
                System.out.println("EntityManager tancat");
            }
            if (emf != null) {
                emf.close();
                System.out.println("EntityManagerFactory tancada");
            }
        }
        
    }
    
}
