package be.vdab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import be.vdab.entities.Klant;

public class KlantDAO extends AbstractDAO {
	private static final String BEGIN_SELECT = "select id, voornaam, familienaam, straat, huisnr, postcode, gemeente, gebruikersnaam, paswoord from klanten";
	private static final String SELECT_BY_GEBRUIKERSNAAM = BEGIN_SELECT + " where gebruikersnaam = ?";
	private static final String SELECT_BY_KLANTID = BEGIN_SELECT + " where id = ?";
	private static final String INSERT_KLANT = "INSERT INTO klanten (voornaam, familienaam, straat, huisnr, postcode, gemeente, gebruikersnaam, paswoord) VALUES (?, ?, ?, ?, ?, ?, ? , ?)";
		
	public Klant findByGebruikersnaam(String naam) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(SELECT_BY_GEBRUIKERSNAAM)) {
			statement.setString(1, naam);
			try (ResultSet resultSet = statement.executeQuery()) {								
				return (resultSet.next() ? resultSetRijNaarKlant(resultSet):null);
			}
		} catch (SQLException ex) {
			throw new DAOException(ex);
		}
	}
	
	public Klant findByKlantID(int id){
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(SELECT_BY_KLANTID)) {
			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {								
				return (resultSet.next() ? resultSetRijNaarKlant(resultSet):null);
			}
		} catch (SQLException ex) {
			throw new DAOException(ex);
		}
	}
	
	public void create(Klant klant){
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_KLANT, Statement.RETURN_GENERATED_KEYS)){
			//(`voornaam`, `familienaam`, `straat`, `huisnr`, `postcode`, `gemeente`, `gebruikersnaam`, `paswoord`)
			statement.setString(1, klant.getVoornaam());
			statement.setString(2, klant.getFamilienaam());
			statement.setString(3, klant.getStraat());
			statement.setString(4, klant.getHuisnr());
			statement.setString(5, klant.getPostcode());
			statement.setString(6, klant.getGemeente());
			statement.setString(7, klant.getGebruikersnaam());
			statement.setString(8, klant.getPaswoord());
			statement.executeUpdate();
			try (ResultSet resultSet = statement.getGeneratedKeys()) {
				if (resultSet.next()){
					klant.setId(resultSet.getInt(1));
				}				
			}
		} catch (SQLException ex){
			throw new DAOException(ex);
		}
	}

	// convert to entity
	private Klant resultSetRijNaarKlant(ResultSet resultSet)
			throws SQLException {
		return new Klant(resultSet.getInt("id"),
				resultSet.getString("voornaam"),
				resultSet.getString("familienaam"),
				resultSet.getString("straat"), resultSet.getString("huisnr"),
				resultSet.getString("postcode"),
				resultSet.getString("gemeente"),
				resultSet.getString("gebruikersnaam"),
				resultSet.getString("paswoord"));
	}
}
