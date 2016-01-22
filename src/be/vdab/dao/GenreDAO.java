package be.vdab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import be.vdab.entities.Genre;

public class GenreDAO extends AbstractDAO {
	private static final String BEGIN_SELECT = "select id, naam from genres";
	private static final String SELECT_BY_ID = BEGIN_SELECT + " where id = ?";
	
	public List<Genre> findAll(){
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(BEGIN_SELECT)){
			List<Genre> genres = new ArrayList<>();
			while (resultSet.next()){
				genres.add(resultSetRijNaarGenre(resultSet));
			}
			return genres;
		} catch (SQLException ex){
			throw new DAOException(ex);
		}
	}
	
	public Genre findByID(int id){
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)){
			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()){
				return (resultSet.next() ? resultSetRijNaarGenre(resultSet):null);				
			}			
		} catch (SQLException ex){
			throw new DAOException(ex);
		}
	}
	
	private Genre resultSetRijNaarGenre(ResultSet resultSet)
			throws SQLException {
		return new Genre(resultSet.getInt("id"), resultSet.getString("naam"));
	}

}
