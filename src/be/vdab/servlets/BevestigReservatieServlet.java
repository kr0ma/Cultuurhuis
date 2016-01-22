package be.vdab.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import be.vdab.dao.KlantDAO;
import be.vdab.dao.ReservatieDAO;
import be.vdab.dao.VoorstellingDAO;
import be.vdab.entities.Klant;
import be.vdab.entities.Reservatie;

/**
 * Servlet implementation class BevestigReservatieServlet
 */
@WebServlet("/bevestigreservatie.htm")
public class BevestigReservatieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/bevestigreservatie.jsp";
	private static final String REDIRECT_URL = "%s/overzicht.htm?";
	
	private static final String KLANT = "klant";
	private static final String KLANTID = "klantID";
	private static final String GEBRUIKERSNAAM = "gebruikersnaam";
	private static final String PASWOORD = "paswoord";
	private static final String MANDJE = "mandje";	
	
	private final transient KlantDAO klantDAO = new KlantDAO();
	private final transient VoorstellingDAO voorstellingDAO = new VoorstellingDAO();
	private final transient ReservatieDAO reservatieDAO = new ReservatieDAO();

	@Resource(name = KlantDAO.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		klantDAO.setDataSource(dataSource);			
		voorstellingDAO.setDataSource(dataSource);
		reservatieDAO.setDataSource(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(KLANTID)!= null){
			int klantID = (int) (session.getAttribute(KLANTID));
			Klant klant = klantDAO.findByKlantID(klantID);
			request.setAttribute(KLANT, klant);
		}
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("zoekmeop") != null){		
			if (request.getParameter(GEBRUIKERSNAAM) != null){
				Klant klant = klantDAO.findByGebruikersnaam(request.getParameter(GEBRUIKERSNAAM));
				if (klant != null && klant.isPaswoordJuist(request.getParameter(PASWOORD))){
					HttpSession session = request.getSession();
					session.setAttribute(KLANTID, klant.getId());					
					response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
				} else {
					request.setAttribute("fout", "Verkeerde gebruikersnaam of paswoord");
					request.getRequestDispatcher(VIEW).forward(request, response);
				}
			}else {
				request.setAttribute("fout", "Verkeerde gebruikersnaam of paswoord");
				request.getRequestDispatcher(VIEW).forward(request, response);
			}
		}
		
		
		if (request.getParameter("bevestigen") != null){
			StringBuilder redirectURL = new StringBuilder(REDIRECT_URL);
			HttpSession session = request.getSession(false);	
			if (session != null){
				if (session.getAttribute(MANDJE) != null){
					@SuppressWarnings("unchecked")
					Map<Integer, Integer> mandje = (Map<Integer, Integer>) session.getAttribute(MANDJE);
					Map<Integer, Integer> gelukteReservaties = new HashMap<>(); 
					Map<Integer, Integer> mislukteReservaties = new HashMap<>();	
					int klantID = (int) (session.getAttribute(KLANTID));
					Klant klant = klantDAO.findByKlantID(klantID);	
					List<Reservatie> reservaties = new ArrayList<>();
					for (Entry<Integer, Integer> mandjeEntry : mandje.entrySet()){
						reservaties.add(new Reservatie(klant.getId(), mandjeEntry.getKey(), mandjeEntry.getValue()));
						/*
						Reservatie reservatie = new Reservatie(klant.getId(), mandjeEntry.getKey(), mandjeEntry.getValue());
						if (reservatieDAO.create(reservatie)){
							gelukteReservaties.put(reservatie.getVoorstellingsid(), mandjeEntry.getValue());
						} else {
							mislukteReservaties.put(reservatie.getVoorstellingsid(), mandjeEntry.getValue());
						}
						*/
						
						
						/*
						Voorstelling voorstelling = voorstellingDAO.findByID(mandjeEntry.getKey());
						if (voorstelling.isAantalOK(mandjeEntry.getValue())){		
											
							Reservatie reservatie = new Reservatie(klant.getId(),voorstelling.getId(), mandjeEntry.getValue());
							voorstelling.verminderVrijePlaatsen(mandjeEntry.getValue());
							reservatieDAO.create(reservatie, voorstelling);								
							gelukteReservaties.put(voorstelling.getId(), mandjeEntry.getValue());
						} else {							
							mislukteReservaties.put(voorstelling.getId(), mandjeEntry.getValue());
						}	
						*/				
					}
					Map<Integer, Boolean> resultBatch = reservatieDAO.createBatch(reservaties);
					for (Entry<Integer, Boolean> entry : resultBatch.entrySet()){
						if (entry.getValue()){
							gelukteReservaties.put(entry.getKey(), mandje.get(entry.getKey()));
						} else {
							mislukteReservaties.put(entry.getKey(), mandje.get(entry.getKey()));
						}
					}
					
					// redirect url stuff
					if (!gelukteReservaties.isEmpty()){
						redirectURL.append("gelukt=");
						for (Entry<Integer, Integer> entry : gelukteReservaties.entrySet()){							
							redirectURL.append(entry.getKey()).append(":").append(entry.getValue()).append(",");							
						}	
						redirectURL.deleteCharAt(redirectURL.length() - 1);
					}		
					
					if (!mislukteReservaties.isEmpty()){
						redirectURL.append("&mislukt=");
						for (Entry<Integer, Integer> entry : mislukteReservaties.entrySet()){
							redirectURL.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
						}	
						redirectURL.deleteCharAt(redirectURL.length() - 1);
					}						
				}
				session.removeAttribute(MANDJE);
			}
			response.sendRedirect(String.format(redirectURL.toString(), request.getContextPath()));
		}
		
	}

}
