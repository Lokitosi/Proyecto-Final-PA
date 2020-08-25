/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidad.Editorial;
import Entidad.Libro;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import controladores.exceptions.RollbackFailureException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class LibroJpaController implements Serializable {

    public LibroJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libro libro) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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

    public void edit(Libro libro) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
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
