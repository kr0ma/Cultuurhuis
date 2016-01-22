package be.vdab.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import be.vdab.dao.GenreDAO;
import be.vdab.dao.VoorstellingDAO;
import be.vdab.entities.Voorstelling;

/**
 * Servlet implementation class ReservatieMandjeServlet
 */
@WebServlet("/reservatiemandje.htm")
public class ReservatieMandjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/reservatiemandje.jsp";
	
	private static final String MANDJE = "mandje";
	private static final String VOORSTELLINGINMANDJE = "voorstellinginmandje";
	private static final String TOTALE_PRIJS = "totaleprijs";
	
	private final transient VoorstellingDAO voorstellingDAO = new VoorstellingDAO();
	
	@Resource(name = GenreDAO.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		voorstellingDAO.setDataSource(dataSource);	
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null){
			@SuppressWarnings("unchecked")
			Map<Integer, Integer> mandje = (Map<Integer, Integer>) session.getAttribute(MANDJE);
			List<Voorstelling> voorstellingInMandje = voorstellingDAO.findByIDS(mandje.keySet());
			BigDecimal prijs = BigDecimal.ZERO;
			for (Voorstelling voorstelling : voorstellingInMandje){
				prijs = prijs.add(voorstelling.getPrijs().multiply(BigDecimal.valueOf(mandje.get(voorstelling.getId()))));				
			}
			request.setAttribute(VOORSTELLINGINMANDJE, voorstellingInMandje);
			request.setAttribute(TOTALE_PRIJS, prijs);
		}
		request.getRequestDispatcher(VIEW).forward(request, response);
	}	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null){
			if (request.getParameterValues("id") != null){
				@SuppressWarnings("unchecked")
				Map<Integer, Integer> mandje = (Map<Integer, Integer>) session.getAttribute(MANDJE);
				for (String id : request.getParameterValues("id")){
					mandje.remove(Integer.parseInt(id));
				}				
			}
		} 
		response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
	}
	
	

}
