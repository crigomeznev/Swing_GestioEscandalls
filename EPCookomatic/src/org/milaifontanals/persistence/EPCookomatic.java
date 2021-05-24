/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.milaifontanals.cookomatic.model.cuina.Categoria;
import org.milaifontanals.cookomatic.model.cuina.LiniaEscandall;
import org.milaifontanals.cookomatic.model.cuina.Plat;

/**
 *
 * @author Usuari
 */
public class EPCookomatic {

    private EntityManager em;

    /**
     * Constructor que estableix connexió amb el servidor a partir de les dades
     * informades en fitxer de propietats de nom EPCookomatic.properties.
     *
     * @throws EPCookomaticException si hi ha algun problema en el fitxer de
     * propietats o en establir la connexió
     */
    public EPCookomatic() {
        this("EPCookomatic.properties");
    }

    /**
     * Constructor que estableix connexió amb el servidor a partir de les dades
     * informades en fitxer de propietats, i en cas de ser null cercarà el
     * fitxer de nom EPCookomatic.properties.
     *
     * @param nomFitxerPropietats
     * @throws EPCookomaticException si hi ha algun problema en el fitxer de
     * propietats o en establir la connexió
     */
    public EPCookomatic(String nomFitxerPropietats) {
        if (nomFitxerPropietats == null) {
            nomFitxerPropietats = "EPCookomatic.properties";
        }
        Properties props = new Properties();
        try {
            props.load(new FileReader(nomFitxerPropietats));
        } catch (FileNotFoundException ex) {
            throw new EPCookomaticException("No es troba fitxer de propietats", ex);
        } catch (IOException ex) {
            throw new EPCookomaticException("Error en carregar fitxer de propietats", ex);
        }

        String up = props.getProperty("up");
        if (up == null) {
            throw new EPCookomaticException("Fitxer de propietats no conté propietat obligatòria <up>");
        }
        props.remove("up");

        EntityManagerFactory emf = null;
        try {
            HashMap<String, String> propietats = new HashMap(props);
            emf = Persistence.createEntityManagerFactory(up, propietats);
//            System.out.println("EntityManagerFactory creada");
            em = emf.createEntityManager();
//            System.out.println("EntityManager creat");
        } catch (Exception ex) {
            if (emf != null) {
                emf.close();
            }
            throw new EPCookomaticException("Error en crear EntityManagerFactory o EntityManager", ex);
        }
    }

    /**
     * Intenta recuperar l'empleat amb codi indicat per paràmetre.
     *
     * @param codi de l'empleat a recuperar
     * @return L'objecte Empleat trobat o null si no existeix
     * @throws EPJPAException si es produeix algun error (introduir un codi
     * inadequat genera també aquesta excepció)
     */
    public List<Categoria> getCategories() {
        List<Categoria> categories = new ArrayList<>();

        Query q = em.createNamedQuery("Categories");

        categories = (List<Categoria>) q.getResultList();
        for (Categoria c : categories) {
            System.out.println("categoria: " + c);
        }

        return categories;
//        return em.find(Empleat.class, (short) codi);
    }

    public List<Plat> getPlats() {
        List<Plat> plats = new ArrayList<>();

        Query q = em.createNamedQuery("Plats");

        plats = (List<Plat>) q.getResultList();
        for (Plat p : plats) {
            System.out.println("plats: " + p);
        }

        return plats;
//        return em.find(Empleat.class, (short) codi);
    }

    public List<Plat> getPlatsFiltrats(Categoria c, Boolean disponible) {
        List<Plat> plats = new ArrayList<>();
        Query q = null;

        if (c == null) {
            q = em.createNamedQuery("PlatsFiltratsDisponibilitat");
        } else {
            q = em.createNamedQuery("PlatsFiltratsCategoriaDisp");
            q.setParameter("categoria", c);
        }

/*        
        if (disponible==null)
            q.setParameter("disponible", "disponible");
        else
            q.setParameter("disponible", disponible);
*/        
        List<Boolean> disponibilitats = new ArrayList<>();

        disponibilitats.add(true);
        disponibilitats.add(false);
        if (disponible != null) {
            if (disponible) {
                disponibilitats.remove(false);
            } else if (!disponible) {
                disponibilitats.remove(true);
            }
        }
        q.setParameter("disponible", disponibilitats);

        // Opció: Disponibilitat: TOTES
//        if (disponible==null){
//            q = em.createNamedQuery("PlatsPerCategoria");
//        } else {
//            q = em.createNamedQuery("PlatsFiltratsCategoriaDisponibilitat");
//            q.setParameter("disponible", disponible);
//        }
//        q.setParameter("categoria", c);
        plats = (List<Plat>) q.getResultList();
        for (Plat p : plats) {
            System.out.println("plats: " + p);
        }

        return plats;
//        return em.find(Empleat.class, (short) codi);
    }

    public List<Plat> getPlatsPerDisponibilitat(boolean disponible) {
        List<Plat> plats = new ArrayList<>();

        Query q = em.createNamedQuery("PlatsPerDisponibilitat");
        q.setParameter("disponible", disponible);

        plats = (List<Plat>) q.getResultList();
        for (Plat p : plats) {
            System.out.println("plats: " + p);
        }

        return plats;
//        return em.find(Empleat.class, (short) codi);
    }

//    public List<LiniaEscandall> getEscandallPerPlat(Plat plat) {
//        List<LiniaEscandall> escandall = new ArrayList<>();
//
//        Query q = em.createNamedQuery("PlatsPerDisponibilitat");
//        q.setParameter("disponible", disponible);
//
//        escandall = (List<Plat>) q.getResultList();
//        for (Plat p : escandall) {
//            System.out.println("plats: " + p);
//        }
//
//        return escandall;
////        return em.find(Empleat.class, (short) codi);
//    }

    /**
     * Tanca la capa de persistència, tancant la connexió amb la BD.
     *
     * @throws EPCookomaticException si hi ha algun problema en tancar la
     * connexió
     */
    public void close() {
        EntityManagerFactory emf = null;
        try {
            emf = em.getEntityManagerFactory();
            em.close();
            em = null;
        } catch (Exception ex) {
            throw new EPCookomaticException("Error en tancar EntityManager", ex);
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }

    /**
     * Tanca la transacció activa validant els canvis a la BD.
     *
     * @throws EPCookomaticException si hi ha algun problema
     */
    public void commit() {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new EPCookomaticException("Error en fer commit", ex);
        }
    }

    /**
     * Tanca la transacció activa sense validar els canvis a la BD.
     *
     * @throws EPCookomaticException si hi ha algun problema
     */
    public void rollback() {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.getTransaction().rollback();
        } catch (Exception ex) {
            throw new EPCookomaticException("Error en fer rollback", ex);
        }
    }

}
