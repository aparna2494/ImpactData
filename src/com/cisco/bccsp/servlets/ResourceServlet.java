/**
 * Resource Servlet - Use this for loading various resources on Application
 * startup.
 * 
 * @author jhasting
 */

package com.cisco.bccsp.servlets;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import com.cisco.bccsp.db.util.DBUtilApoyo;
import com.cisco.bccsp.db.util.DBUtilCG1Orders;
import com.cisco.bccsp.db.util.DBUtilCaseImpact;
import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.db.util.DBUtilRemedyCases;
import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.util.ldap.LDAPUtil;


@WebServlet(name = "ResourceServlet", urlPatterns = {"/ResourceServlet"})
public class ResourceServlet extends HttpServlet {
	
    private static Logger logger = Logger.getLogger(ResourceServlet.class.getName());
    
    private DBUtilCaseImpact dbUtilCaseImpact = null;
    private DBUtilApoyo dbUtilApoyo = null;
    private DBUtilRemedyCases dbUtilRemedyCases = null;
    private DBUtilCG1Orders dbUtilCG1Orders = null;
    private DBUtilMongo dbUtilMongo = null;
    private DBUtilCiscoOpsMongo dbUtilCiscoOpsMongo = null;
    
    private LDAPUtil ldapUtil = null;
   

    // This Happens Once and is Reused
    @Override
    public void init(ServletConfig config) throws ServletException {
        
        super.init(config);
        
        String env = System.getenv("CISCO_LIFE");
        logger.info("CISCO_LIFE: " + System.getenv("CISCO_LIFE"));       
        //TODO: due prod environment not available setting to prod is env is null
        if (env == null) {
        	env = "prod";
        }
        
        logger.info("ENV: " + env);
        
        // force db connections to be created
       /* dbUtilMongo = DBUtilMongo.getInstance();*/
        dbUtilCiscoOpsMongo=DBUtilCiscoOpsMongo.getInstance(env);
       /* ldapUtil = LDAPUtil.getInstance("dev");
        dbUtilRemedyCases = DBUtilRemedyCases.getInstance(env);
        dbUtilApoyo = DBUtilApoyo.getInstance(env);*/
       /* dbUtilCaseImpact = DBUtilCaseImpact.getInstance();
       
        dbUtilCG1Orders = DBUtilCG1Orders.getInstance();*/
        
    }
    
    /**
     *
     */
    @Override
    public void destroy() {
        
        super.destroy();
        
        dbUtilCaseImpact = null;
        
    }
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ResourceServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ResourceServlet at " + request.getContextPath() + "</h1></br>");
            out.println("This should not be accessed directly");            
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
        return "ResourceServlet Loads Application Resources";
    }// </editor-fold>
}
