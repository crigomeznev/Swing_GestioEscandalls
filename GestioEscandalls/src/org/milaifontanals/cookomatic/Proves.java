/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.cookomatic;

import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Usuari
 */
public class Proves {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // CONNEXIÓ A LA BD
//        if (args.length != 1) {
//            System.out.println("Un únic argument amb el nom de la Unitat de Persistència");
//            System.exit(1);
//        }
//        String up = args[0];
//        String up = "UP-Hibernate";
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

            Query q = em.createNativeQuery("select codi,nom from ingredient");

            List<Object[]> l = q.getResultList();

            for (Object[] o : l) {
                System.out.println("Ingredient "
                        + o[0]
                        + " "
                        + o[1]);
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
