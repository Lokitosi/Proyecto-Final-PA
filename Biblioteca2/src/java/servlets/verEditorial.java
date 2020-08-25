/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import controladores.EditorialJpaController;
import controladores.UsuarioJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author andre
 */
@WebServlet(name = "verEditorial", urlPatterns = {"/verEditorial"})
public class verEditorial extends HttpServlet {

    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        em = emf.createEntityManager();

        String editoriales = "";
        EditorialJpaController edt = new EditorialJpaController(utx, emf);
        List leditorial = edt.findEditorialEntities();
        Iterator iter = leditorial.iterator();
        while (iter.hasNext()) {
            editoriales += (iter.next());
        }

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet verUsuarios</title>");
            out.println("<link rel=\"stylesheet\" href=\"bootstrap/css/bootstrap.min.css\">\n"
                    + "  <link rel=\"stylesheet\" href=\"bootstrap/css/styles.css\">\n"
                    + "  <link href=\"https://fonts.googleapis.com/css2?family=Mukta:wght@200;300;400;500;600;700&display=swap\"\n"
                    + "    rel=\"stylesheet\">\n"
                    + "  <link href=\"https://fonts.googleapis.com/css2?family=Anton&display=swap\" rel=\"stylesheet\" type=\"text/css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("  <header>\n"
                    + "    <nav id=\"header-nav\" class=\"navbar navbar-default\">\n"
                    + "      <div class=\"container\">\n"
                    + "        <div class=\"navbar-header\">\n"
                    + "          <a href=\"./bootstrap/adminPage.html\" class=\"pull-left visible-md visible-lg\">\n"
                    + "            <div id=\"logo-img\"></div>\n"
                    + "          </a>\n"
                    + "\n"
                    + "          <div class=\"navbar-brand\">\n"
                    + "            <a href=\"./bootstrap/adminPage.html\">\n"
                    + "              <h1>BiblioBogota</h1>\n"
                    + "            </a>\n"
                    + "            <p>\n"
                    + "              <span class=\"glyphicon glyphicon-book\"></span>\n"
                    + "              <span>Red de Bibliotecas publicas</span>\n"
                    + "            </p>\n"
                    + "          </div>\n"
                    + "        </div>\n"
                    + "      </div><!-- .container -->\n"
                    + "    </nav><!-- #header-nav -->\n"
                    + "  </header>");
            out.println("<h1>Lista de Editoriales Y sus datos:</h1>");
            
            // contenido de la lista 
            out.println("<div id='listaUsr'>" + editoriales + "</div>");
            
            //formulario para eliminar
            out.println("<h1>Eliminar Editorial:</h1>");
            out.println("<div id=\"del\">");
            out.println("<form action=\"deleteEdit\" method=\"POST\">\n"
                    + "                Inserte id de la editorial:\n"
                    + "                <input type=\"number\" name=\"nombre\">"
                    + "                 <input type=\"submit\">");
            out.println("</form>");
            out.println("</div >");
            
            //formulario para Agregar
            out.println("<h1>Nueva Editorial:</h1>");
            out.println("<div id=\"del\">");
            out.println("<form action=\"newEdit\" method=\"POST\">\n"
                    + "                 Id:\n"
                    + "                <input type=\"number\" name=\"id\">"
                    + "                Nombre:\n"
                    + "                <input type=\"text\" name=\"titulo\">"
                    + "                 <input type=\"submit\">");
            out.println("</form>");
            out.println("</div >");
            
            // Footer
            out.println("<footer class=\"panel-footer\">\n"
                    + "    <div class=\"container\">\n"
                    + "      <div class=\"row\">\n"
                    + "        <section id=\"info\" class=\"col-sm-4\">\n"
                    + "          <span>\n"
                    + "            Trabajamos constantemente para brindar el mejor servicio a nuestros usuarios, es por esto que se mantiene la\n"
                    + "            constante actualizacion de esta pagina\n"
                    + "          </span>\n"
                    + "          <hr class=\"visible-xs\">\n"
                    + "        </section>\n"
                    + "        <section id=\"ayuda\" class=\"col-sm-4\">\n"
                    + "          <span>\n"
                    + "            cualquier duda por favor comunicarse con el web-master el cual se encargara de solucionarlo lo mas pronto\n"
                    + "          </span>\n"
                    + "          <hr class=\"visible-xs\">\n"
                    + "        </section>\n"
                    + "        <section id=\"menu\" class=\"col-sm-4\">\n"
                    + "          <span>\n"
                    + "            numeros de contacto , proximamente seran añadidos con otras cosas mas , por el momento lo mas relevante\n"
                    + "          </span>\n"
                    + "          <hr class=\"visible-xs\">\n"
                    + "        </section>\n"
                    + "      </div>\n"
                    + "      <div class=\"text-center\">\n"
                    + "        &copy; Lokitosi 2020\n"
                    + "      </div>\n"
                    + "    </div>\n"
                    + "  </footer>");
            out.println("</body>");
            out.println("</html>");
        }

        emf.close();

        em.close();
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
