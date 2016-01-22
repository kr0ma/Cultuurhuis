package be.vdab.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import be.vdab.dao.GenreDAO;
import be.vdab.dao.VoorstellingDAO;
import be.vdab.entities.Genre;
import be.vdab.entities.Voorstelling;

/**
 * Servlet implementation class VoorstellingenServlet
 */
@WebServlet("/index.htm")
public class VoorstellingenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/voorstellingen.jsp";
	
	private static final String GENREID = "genreID";
	private static final String GENRES = "genres";
	private static final String HUIDIG_GENRE = "huidigGenre";
	
	private static final String VOORSTELLINGEN = "voorstellingen";	
	
	private final static Logger logger = Logger.getLogger(VoorstellingenServlet.class.getName());

	
	private final transient GenreDAO genreDAO = new GenreDAO();
	private final transient VoorstellingDAO voorstellingDAO = new VoorstellingDAO();
	
	@Resource(name = GenreDAO.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		genreDAO.setDataSource(dataSource);		
		voorstellingDAO.setDataSource(dataSource);	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter(GENREID) != null){
			try {
				int genreid = Integer.parseInt(request.getParameter(GENREID)); 
				Genre huidigGenre = genreDAO.findByID(genreid);
				if (huidigGenre != null){
					List<Voorstelling> voorstellingen = new ArrayList<>(voorstellingDAO.findByGenreID(huidigGenre.getId()));					
					request.setAttribute(HUIDIG_GENRE, huidigGenre);
					request.setAttribute(VOORSTELLINGEN	, voorstellingen);
				}				
			} catch (NumberFormatException ex){
				logger.log(Level.SEVERE, "Some hacker did something!", ex);
			}			
		}
		request.setAttribute(GENRES, genreDAO.findAll());
		request.getRequestDispatcher(VIEW).forward(request, response);
	}
}
