/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import Entidad.Libro;
import controladores.LibroJpaController;
import controladores.exceptions.RollbackFailureException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "newLib", urlPatterns = {"/newLib"})
public class newLib extends HttpServlet {
    
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
    @SuppressWarnings("empty-statement")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        em = emf.createEntityManager();

        Libro lib = new Libro();
        lib.setLibTitulo(request.getParameter("titulo"));
        lib.setId(Integer.parseInt(request.getParameter("id")));
        lib.setLibGenero(request.getParameter("genero"));
        lib.setLibPrecio(Integer.parseInt(request.getParameter("precio")));
        
        if ("si".equals(request.getParameter("stock"))){
            Short v = 1;
            lib.setLibStock(v);
        }else{
            Short v = 0;
            lib.setLibStock(v);
        }
        
        
        LibroJpaController lbr = new LibroJpaController(utx, emf);
        
        String respuesta = "el libro se ha añadido correctamente";
        try {
            lbr.create(lib);
        } catch (RollbackFailureException ex) {
                respuesta = "El libro ya se encuentra en la base de datos";
            } catch (Exception ex) {
                respuesta = "Error al conectar a la base de datos";
            }
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet newLib</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Añadir Libro:</h1>");
            out.println(respuesta);
            out.println("<a href='./bootstrap/adminPage.html'>Regresar a la pagina principal</a>");
            out.println("</body>");
            out.println("</html>");
        }
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
