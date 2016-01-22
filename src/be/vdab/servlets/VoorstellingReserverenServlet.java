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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import be.vdab.dao.GenreDAO;
import be.vdab.dao.VoorstellingDAO;
import be.vdab.entities.Voorstelling;

/**
 * Servlet implementation class VoorstellingReserverenServlet
 */
@WebServlet("/voorstelling/reserveren.htm")
public class VoorstellingReserverenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/voorstellingreserveren.jsp";

	private static final String REDIRECT_URL = "%s/reservatiemandje.htm";

	private static final String MANDJE = "mandje";
	private static final String VOORSTELLING = "voorstelling";
	
	private final static Logger logger = Logger.getLogger(VoorstellingenServlet.class.getName());
	private final transient VoorstellingDAO voorstellingDAO = new VoorstellingDAO();

	@Resource(name = GenreDAO.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		voorstellingDAO.setDataSource(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("voorstellingID") != null) {
			try {
				int voorstellingID = Integer.parseInt(request
						.getParameter("voorstellingID"));
				Voorstelling voorstelling = voorstellingDAO
						.findByID(voorstellingID);
				if (voorstelling != null) {
					request.setAttribute(VOORSTELLING, voorstelling);
					// previous reservation for that show ?
					HttpSession session = request.getSession(false);
					if (session != null) {
						@SuppressWarnings("unchecked")
						Map<Integer, Integer> mandje = (Map<Integer, Integer>) session.getAttribute(MANDJE);						
						if (mandje != null) {
							if (mandje.containsKey(voorstelling.getId())) {
								request.setAttribute("aantalGereserveerd",mandje.get(voorstelling.getId()));
							}
						}
					}
				}
			} catch (NumberFormatException ex) {
				logger.log(Level.SEVERE, "Some hacker did something!", ex);
			}
		}
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("voorstellingID") != null) {
			Voorstelling voorstelling = voorstellingDAO.findByID(Integer
					.parseInt(request.getParameter("voorstellingID")));

			if (request.getParameter("aantal") != null) {
				int aantalReserveren;
				try {
					aantalReserveren = Integer.parseInt(request
							.getParameter("aantal"));
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, "Some hacker did something!", ex);
					aantalReserveren = 0;
				}

				// amount of seats ok ?
				if (voorstelling.isAantalOK(aantalReserveren)) {
					HttpSession session = request.getSession();
					@SuppressWarnings("unchecked")
					Map<Integer, Integer> mandje = (Map<Integer, Integer>) session.getAttribute(MANDJE);
					if (mandje == null) {
						mandje = new HashMap<Integer, Integer>();
					}					
					mandje.put(voorstelling.getId(), aantalReserveren);
					session.setAttribute(MANDJE, mandje);
					response.sendRedirect(String.format(REDIRECT_URL, request.getContextPath()));
				} else {
					request.setAttribute(VOORSTELLING, voorstelling);
					request.setAttribute("fout", "Tik een getal tussen 1 en "
							+ voorstelling.getVrijeplaatsen());
					request.getRequestDispatcher(VIEW).forward(request,response);
				}
			}
		}
	}

}
