package be.vdab.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import be.vdab.dao.VoorstellingDAO;
import be.vdab.entities.Voorstelling;

/**
 * Servlet implementation class OverzichtServlet
 */
@WebServlet("/overzicht.htm")
public class OverzichtServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String VIEW = "/WEB-INF/JSP/overzicht.jsp";	
	
	private final transient VoorstellingDAO voorstellingDAO = new VoorstellingDAO();
	private final static Logger logger = Logger.getLogger(OverzichtServlet.class.getName());

	
	private static final String GELUKTERESERVATIES = "gelukteReservaties";
	private static final String MISLUKTERESERVATIES = "mislukteReservaties";

	@Resource(name = VoorstellingDAO.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		voorstellingDAO.setDataSource(dataSource);	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("gelukt") != null){
			Map<Voorstelling, Integer> gelukteReservaties = new HashMap<>();
			String[] geluktParameters = request.getParameter("gelukt").split(",");
			for (String geluktParam : geluktParameters){
				try {
					int voorstellingID = Integer.parseInt(geluktParam.substring(0, geluktParam.indexOf(":")));
					int aantal = Integer.parseInt(geluktParam.substring(geluktParam.indexOf(":") + 1));
					gelukteReservaties.put(voorstellingDAO.findByID(voorstellingID), aantal);
				} catch (NumberFormatException ex){
					logger.log(Level.SEVERE, "Some hacker did something!", ex);

				}
				
			}
			request.setAttribute(GELUKTERESERVATIES, gelukteReservaties);
		}
		
		if (request.getParameter("mislukt") != null){
			Map<Voorstelling, Integer> mislukteReservaties = new HashMap<>();
			String[] misluktParameters = request.getParameter("mislukt").split(",");
			for (String misluktParam : misluktParameters){
				try {
					int voorstellingID = Integer.parseInt(misluktParam.substring(0, misluktParam.indexOf(":")));
					int aantal = Integer.parseInt(misluktParam.substring(misluktParam.indexOf(":") + 1));
					mislukteReservaties.put(voorstellingDAO.findByID(voorstellingID), aantal);
				} catch (NumberFormatException ex){
					logger.log(Level.SEVERE, "Some hacker did something!", ex);

				}				
			}
			request.setAttribute(MISLUKTERESERVATIES, mislukteReservaties);
		}		
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

}
