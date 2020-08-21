package Principales;

/* Edison Andres Gamba Robayo - 20191020170
   Angello Davis Agualimpia Linares - 20191020136 */


import controlador.UsuarioJpaController;
import controlador.EditorialJpaController;
import controlador.LibroJpaController;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BaseDeDatos {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static final String PERSISTENCE_UNIT_NAME = "BibliotecaBasePU";
    static Boolean salir = false;
    private static UsuarioJpaController usr;
    private static EditorialJpaController edi;
    private static LibroJpaController lbr;
    
    public BaseDeDatos(){
        initEntityManager();
        //Controladores Tabla
        usr = new UsuarioJpaController(emf);
        lbr = new LibroJpaController(emf);
        edi = new EditorialJpaController(emf);
    }

    private static void initEntityManager() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
    }

    private static void closeEntityManager() {
        em.close();
        emf.close();
    }

    // Añadir:
    public void añadirUsuario(String n, String email, String pass) throws Exception {
        persistencia.Usuario p = new persistencia.Usuario(n, pass, email);
        usr.create(p);
    }

    public void añadirEditorial(Integer id, String nombre) throws Exception {
        persistencia.Editorial p = new persistencia.Editorial();
        p.setEdiNombre(nombre);
        p.setIdEditorial(id);
        edi.create(p);
    }

    public void añadirLibro(Integer id, String libTitulo, String libGenero, short libStock) throws Exception {
        persistencia.Libro p = new persistencia.Libro(id, libTitulo, libGenero, libStock);
        lbr.create(p);
    }

    //Eliminar
    public void elimUsuario(String us) {
        try {
            usr.destroy(us);
        } catch (NonexistentEntityException e) {
            System.out.println("el usuario no se encuentra en la base de datos");
        }
    }
    
    public void elimLibro(int id) {
        try {
            lbr.destroy(id);
        } catch (NonexistentEntityException e) {
            System.out.println("el libro no se encuentra en la base de datos");
        }
    }
    
    public void elimEditorial(Integer id) throws IllegalOrphanException {
        try {
            edi.destroy(id);
        } catch (NonexistentEntityException e) {
            System.out.println("La editorial no se encuentra en la base de datos");
        }
    }
    
    // Ver
    public String verUsuarios(){
        
        String lista=(usr.findUsuarioEntities().toString());
        return lista;
    }
    
    public String verLibros(){
        String lista=(lbr.findLibroEntities().toString());
        return lista;
    }
    
    public String verEditorial(){
        String lista=(edi.findEditorialEntities().toString());
        return lista;
    }
    
    public String areAlive(){
        String ja ="Estoy vivo :)";
        return ja;
    }
}
