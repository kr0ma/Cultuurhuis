package be.vdab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import be.vdab.entities.Voorstelling;

public class VoorstellingDAO extends AbstractDAO {
	private static final String BEGIN_SELECT = "select id, titel, uitvoerders, datum, genreid, prijs, vrijeplaatsen from voorstellingen";
	private static final String SELECT_UPCOMING = " datum > Now()";
	private static final String SELECT_BY_GENRE = BEGIN_SELECT + " where genreid = ? and" + SELECT_UPCOMING;	
	private static final String SELECT_BY_ID = BEGIN_SELECT + " where id = ?";
	private static final String SELECT_BY_IDS = BEGIN_SELECT + " where id in (";
	
	// Lists
	public List<Voorstelling> findAll() {
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(BEGIN_SELECT)) {
			List<Voorstelling> voorstellingen = new ArrayList<>();
			while (resultSet.next()) {
				voorstellingen.add(resultSetRijNaarVoorstelling(resultSet));
			}
			return voorstellingen;
		} catch (SQLException ex) {
			throw new DAOException(ex);
		}
	}
	
	public List<Voorstelling> findByGenreID(int id){
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_GENRE)){
			statement.setInt(1, id);			
			try (ResultSet resultSet = statement.executeQuery()){				
				List<Voorstelling> voorstellingen = new ArrayList<>();				
				while (resultSet.next()){
					voorstellingen.add(resultSetRijNaarVoorstelling(resultSet));
				}
				return voorstellingen;
			}			
		} catch (SQLException ex){
			throw new DAOException(ex);
		}
	}	
	
	//singlerow
	public Voorstelling findByID(int id){
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)){
			statement.setInt(1, id);			
			try (ResultSet resultSet = statement.executeQuery()){
				return (resultSet.next() ? resultSetRijNaarVoorstelling(resultSet):null);
			}				
		} catch (SQLException ex){
			throw new DAOException(ex);
		}
	}
	
	//batchID
	public List<Voorstelling> findByIDS(Set<Integer> ids){
		StringBuilder query = new StringBuilder(SELECT_BY_IDS);		
		for (int i = 0 ; i < ids.size(); i++){
			query.append("?,");
		}
		query.deleteCharAt(query.length() - 1).append(")");
		List<Voorstelling> voorstellingen = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(query.toString())){
			int index = 1;
			for (int id : ids){
				statement.setInt(index, id);
				index++;
			}
			try (ResultSet resultSet = statement.executeQuery()){
				while (resultSet.next()){
					voorstellingen.add(resultSetRijNaarVoorstelling(resultSet));
				}
				return voorstellingen;
			}
		} catch (SQLException ex){
			throw new DAOException(ex);
		}
	}

	//convert to entity
	private Voorstelling resultSetRijNaarVoorstelling(ResultSet resultSet)
			throws SQLException {
		return new Voorstelling(resultSet.getInt("id"),
								resultSet.getInt("genreid"),
								resultSet.getInt("vrijeplaatsen"),
								resultSet.getString("titel"),
								resultSet.getString("uitvoerders"),
								resultSet.getTimestamp("datum"),
								resultSet.getBigDecimal("prijs"));
	}
}
