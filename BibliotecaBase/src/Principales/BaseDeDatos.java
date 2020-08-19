package Principales;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import controlador.UsuarioJpaController;
import controlador.EditorialJpaController;
import controlador.LibroJpaController;
import controlador.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/* Nombres : Edison Andres Gamba Robayo - 20191020170
Angello Davis Agualimpia Linares - 20191020136 */
public class BaseDeDatos {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static final String PERSISTENCE_UNIT_NAME = "BibliotecaBasePU";
    static Boolean salir = false;
	static int ids = 0;

    private static void initEntityManager() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
    }

    private static void closeEntityManager() {
        em.close();
        emf.close();
    }

	public static void main(String[] args) {
		 initEntityManager();
	     UsuarioJpaController usr = new UsuarioJpaController(emf);
             LibroJpaController lbr = new LibroJpaController(emf);
             EditorialJpaController edi = new EditorialJpaController(emf);
	     ids = usr.getUsuarioCount();
		System.out.println("Bienvenido a la BD:\n");

		
		while (salir != true) {
			String menu = "1. ver BD \n 2.Acutalizar Datos \n 3.Agregar usuario \n 4.Eliminar usuario \n 5:salir";
			int opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));

			switch (opcion) {
			case 1://imprime los usuarios de la base de datos
				System.out.println("\n Usuarios en la base");
				System.out.println(usr.findUsuarioEntities().toString());
				break;

			/*case 2://actualiza los datos de algun usuario
				int id = Integer.parseInt(JOptionPane.showInputDialog("ingrese el id"));
				Persona p1 = p.buscarID(id);
				p1.setApellido(JOptionPane.showInputDialog("ingrese el Apellido"));
				p1.setEdad(Integer.parseInt(JOptionPane.showInputDialog("ingrese la edad")));
				p1.setNombre(JOptionPane.showInputDialog("ingrese el Nombre"));
				p.salvar();
				break;

			case 3:// agrega un usuario a la bd
				String apellido = (JOptionPane.showInputDialog("ingrese el Apellido"));
				int edad = (Integer.parseInt(JOptionPane.showInputDialog("ingrese la edad")));
				String nombre = (JOptionPane.showInputDialog("ingrese el Nombre"));
				p.agregar(ids + 1, nombre, apellido, edad);
				ids += 1;
				break;
				
			case 4://Elimina una persona 
				int ud = (Integer.parseInt(JOptionPane.showInputDialog("ingrese el id a eliminar")));
				try {
					jpa.destroy(ud);
				} catch (NonexistentEntityException e) {
					System.out.println("el usuario no se encuentra en la base de datos");
					e.printStackTrace();
				}
				ids = jpa.getPersonaCount();
					break;

                        */
			case 5://Cierra el programa :)
   			closeEntityManager();
				salir = true;
				break;

			}
		}
        }
}

