package be.vdab.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import be.vdab.dao.GenreDAO;
import be.vdab.dao.KlantDAO;
import be.vdab.entities.Klant;

/**
 * Servlet implementation class NieuweKlantServlet
 */
@WebServlet("/nieuweklant.htm")
public class NieuweKlantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/nieuweklant.jsp";	
	private static final String REDIRECT_URL = "%s/bevestigreservatie.htm";
	
	private static final String KLANTID = "klantID";
	
	private static final String VOORNAAM = "voornaam";
	private static final String FAMILIENAAM = "familienaam";
	private static final String STRAAT = "straat";
	private static final String HUISNR = "huisnr";
	private static final String POSTCODE = "postcode";
	private static final String GEMEENTE = "gemeente";
	private static final String GEBRUIKERSNAAM = "gebruikersnaam";
	private static final String PASWOORD = "paswoord";
	private static final String VERIFPASWOORD = "verifpaswoord";
	
	private final transient KlantDAO klantDAO = new KlantDAO();

	@Resource(name = GenreDAO.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		klantDAO.setDataSource(dataSource);			
	}


	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> fouten = new ArrayList<>();
		if (!Klant.isInputValid(request.getParameter(VOORNAAM))){
			fouten.add(VOORNAAM + " niet ingevuld");
		}		
		if (!Klant.isInputValid(request.getParameter(FAMILIENAAM))){
			fouten.add(FAMILIENAAM + " niet ingevuld");
		}
		if (!Klant.isInputValid(request.getParameter(STRAAT))){
			fouten.add(STRAAT + " niet ingevuld");
		}
		if (!Klant.isInputValid(request.getParameter(HUISNR))){
			fouten.add(HUISNR + " niet ingevuld");
		}
		if (!Klant.isInputValid(request.getParameter(POSTCODE))){
			fouten.add(POSTCODE + " niet ingevuld");
		}
		if (!Klant.isInputValid(request.getParameter(GEMEENTE))){
			fouten.add(GEMEENTE + " niet ingevuld");
		}
		if (!Klant.isInputValid(request.getParameter(GEBRUIKERSNAAM))){
			fouten.add(GEBRUIKERSNAAM + " niet ingevuld");
		} else {
			if (klantDAO.findByGebruikersnaam(request.getParameter(GEBRUIKERSNAAM)) != null){
				fouten.add("Gebruikersnaam bestaat al!");
			}
		}
		if (!Klant.isInputValid(request.getParameter(PASWOORD))){
			fouten.add(PASWOORD + " niet ingevuld");
		}else {
			if (!request.getParameter(PASWOORD).equals(request.getParameter(VERIFPASWOORD))){
				fouten.add("paswoord & herhaal paswoord zijn verschillend");
			}
		}			
		
		if (fouten.isEmpty()){
			HttpSession session = request.getSession();
			Klant klant = new Klant(request.getParameter(VOORNAAM), 
					request.getParameter(FAMILIENAAM), 
					request.getParameter(STRAAT), 
					request.getParameter(HUISNR), 
					request.getParameter(POSTCODE), 
					request.getParameter(GEMEENTE), 
					request.getParameter(GEBRUIKERSNAAM), 
					request.getParameter(PASWOORD));
			klantDAO.create(klant);	
			session.setAttribute(KLANTID, klant.getId());			
			response.sendRedirect(String.format(REDIRECT_URL, request.getContextPath()));
		} else {
			request.setAttribute("fouten", fouten);
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
