/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import persistencia.Editorial;
import persistencia.Libro;

/**
 *
 * @author andre
 */
public class LibroJpaController implements Serializable {

    public LibroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libro libro) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Editorial editorialidEditorial = libro.getEditorialidEditorial();
            if (editorialidEditorial != null) {
                editorialidEditorial = em.getReference(editorialidEditorial.getClass(), editorialidEditorial.getIdEditorial());
                libro.setEditorialidEditorial(editorialidEditorial);
            }
            em.persist(libro);
            if (editorialidEditorial != null) {
                editorialidEditorial.getLibroList().add(libro);
                editorialidEditorial = em.merge(editorialidEditorial);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLibro(libro.getId()) != null) {
                throw new PreexistingEntityException("Libro " + libro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Libro libro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro persistentLibro = em.find(Libro.class, libro.getId());
            Editorial editorialidEditorialOld = persistentLibro.getEditorialidEditorial();
            Editorial editorialidEditorialNew = libro.getEditorialidEditorial();
            if (editorialidEditorialNew != null) {
                editorialidEditorialNew = em.getReference(editorialidEditorialNew.getClass(), editorialidEditorialNew.getIdEditorial());
                libro.setEditorialidEditorial(editorialidEditorialNew);
            }
            libro = em.merge(libro);
            if (editorialidEditorialOld != null && !editorialidEditorialOld.equals(editorialidEditorialNew)) {
                editorialidEditorialOld.getLibroList().remove(libro);
                editorialidEditorialOld = em.merge(editorialidEditorialOld);
            }
            if (editorialidEditorialNew != null && !editorialidEditorialNew.equals(editorialidEditorialOld)) {
                editorialidEditorialNew.getLibroList().add(libro);
                editorialidEditorialNew = em.merge(editorialidEditorialNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = libro.getId();
                if (findLibro(id) == null) {
                    throw new NonexistentEntityException("The libro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro libro;
            try {
                libro = em.getReference(Libro.class, id);
                libro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libro with id " + id + " no longer exists.", enfe);
            }
            Editorial editorialidEditorial = libro.getEditorialidEditorial();
            if (editorialidEditorial != null) {
                editorialidEditorial.getLibroList().remove(libro);
                editorialidEditorial = em.merge(editorialidEditorial);
            }
            em.remove(libro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Libro> findLibroEntities() {
        return findLibroEntities(true, -1, -1);
    }

    public List<Libro> findLibroEntities(int maxResults, int firstResult) {
        return findLibroEntities(false, maxResults, firstResult);
    }

    private List<Libro> findLibroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libro.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Libro findLibro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libro> rt = cq.from(Libro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
