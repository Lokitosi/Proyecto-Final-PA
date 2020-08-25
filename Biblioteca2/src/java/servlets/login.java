package servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import controladores.UsuarioJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entidad.Usuario;

/**
 *
 * @author andre
 */
@WebServlet(urlPatterns = {"/loginx"})
public class login extends HttpServlet {
    
     
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");  
        
        String usuario = request.getParameter("nick");
        String pass = request.getParameter("password");
        
        Usuario u = new Usuario(usuario,pass," ",0);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        em = emf.createEntityManager();
        
        UsuarioJpaController usr = new UsuarioJpaController(utx , emf);
        Usuario c = usr.findUsuario(usuario);
                
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link href=\"style.css\" rel=\"stylesheet\">");
            out.println("<title>Login </title>");            
            out.println("</head>");
            out.println("<body>"); 
            out.println("<div id='resp'");
            if (u.getContraseña().compareTo(c.getContraseña())==0){
                out.println("<p>Bienvenido a la biblioteca</b>");
                if (c.getRol()==1){
                    out.println("<a href='./bootstrap/adminPage.html' id='boton'>Pagina principal Administrativa</a>");
                }else{
                   out.println("<a href='./bootstrap/userPage.html' id='boton'>Pagina principal </a>"); 
                }  
            }else{
                out.println("<p>Contraseña incorrecta </p>");
                out.println("<a href='login.html' id='boton'>Intentar de nuevo</a>");
            }
            out.println("</div>");
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
