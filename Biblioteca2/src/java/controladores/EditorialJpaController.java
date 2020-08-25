/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Entidad.Editorial;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidad.Libro;
import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import controladores.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class EditorialJpaController implements Serializable {

    public EditorialJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Editorial editorial) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (editorial.getLibroList() == null) {
            editorial.setLibroList(new ArrayList<Libro>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Libro> attachedLibroList = new ArrayList<Libro>();
            for (Libro libroListLibroToAttach : editorial.getLibroList()) {
                libroListLibroToAttach = em.getReference(libroListLibroToAttach.getClass(), libroListLibroToAttach.getId());
                attachedLibroList.add(libroListLibroToAttach);
            }
            editorial.setLibroList(attachedLibroList);
            em.persist(editorial);
            for (Libro libroListLibro : editorial.getLibroList()) {
                Editorial oldEditorialidEditorialOfLibroListLibro = libroListLibro.getEditorialidEditorial();
                libroListLibro.setEditorialidEditorial(editorial);
                libroListLibro = em.merge(libroListLibro);
                if (oldEditorialidEditorialOfLibroListLibro != null) {
                    oldEditorialidEditorialOfLibroListLibro.getLibroList().remove(libroListLibro);
                    oldEditorialidEditorialOfLibroListLibro = em.merge(oldEditorialidEditorialOfLibroListLibro);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEditorial(editorial.getIdEditorial()) != null) {
                throw new PreexistingEntityException("Editorial " + editorial + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Editorial editorial) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Editorial persistentEditorial = em.find(Editorial.class, editorial.getIdEditorial());
            List<Libro> libroListOld = persistentEditorial.getLibroList();
            List<Libro> libroListNew = editorial.getLibroList();
            List<String> illegalOrphanMessages = null;
            for (Libro libroListOldLibro : libroListOld) {
                if (!libroListNew.contains(libroListOldLibro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Libro " + libroListOldLibro + " since its editorialidEditorial field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Libro> attachedLibroListNew = new ArrayList<Libro>();
            for (Libro libroListNewLibroToAttach : libroListNew) {
                libroListNewLibroToAttach = em.getReference(libroListNewLibroToAttach.getClass(), libroListNewLibroToAttach.getId());
                attachedLibroListNew.add(libroListNewLibroToAttach);
            }
            libroListNew = attachedLibroListNew;
            editorial.setLibroList(libroListNew);
            editorial = em.merge(editorial);
            for (Libro libroListNewLibro : libroListNew) {
                if (!libroListOld.contains(libroListNewLibro)) {
                    Editorial oldEditorialidEditorialOfLibroListNewLibro = libroListNewLibro.getEditorialidEditorial();
                    libroListNewLibro.setEditorialidEditorial(editorial);
                    libroListNewLibro = em.merge(libroListNewLibro);
                    if (oldEditorialidEditorialOfLibroListNewLibro != null && !oldEditorialidEditorialOfLibroListNewLibro.equals(editorial)) {
                        oldEditorialidEditorialOfLibroListNewLibro.getLibroList().remove(libroListNewLibro);
                        oldEditorialidEditorialOfLibroListNewLibro = em.merge(oldEditorialidEditorialOfLibroListNewLibro);
                    }
                }
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
                Integer id = editorial.getIdEditorial();
                if (findEditorial(id) == null) {
                    throw new NonexistentEntityException("The editorial with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Editorial editorial;
            try {
                editorial = em.getReference(Editorial.class, id);
                editorial.getIdEditorial();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The editorial with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Libro> libroListOrphanCheck = editorial.getLibroList();
            for (Libro libroListOrphanCheckLibro : libroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Editorial (" + editorial + ") cannot be destroyed since the Libro " + libroListOrphanCheckLibro + " in its libroList field has a non-nullable editorialidEditorial field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(editorial);
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

    public List<Editorial> findEditorialEntities() {
        return findEditorialEntities(true, -1, -1);
    }

    public List<Editorial> findEditorialEntities(int maxResults, int firstResult) {
        return findEditorialEntities(false, maxResults, firstResult);
    }

    private List<Editorial> findEditorialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Editorial.class));
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

    public Editorial findEditorial(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Editorial.class, id);
        } finally {
            em.close();
        }
    }

    public int getEditorialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Editorial> rt = cq.from(Editorial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
