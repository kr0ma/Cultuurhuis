package be.vdab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.vdab.entities.Reservatie;


public class ReservatieDAO extends AbstractDAO {
	private static final String INSERT_RESERVATIE = "INSERT INTO reservaties (klantid, voorstellingsid, plaatsen) VALUES (?, ?, ?)";
	private static final String UPDATE_PLAATSEN_VAN_VOORSTELLING = "UPDATE voorstellingen SET vrijeplaatsen= vrijeplaatsen - ? WHERE id= ? and vrijeplaatsen >= ?";
	
	
	public Boolean create(Reservatie reservatie){
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_RESERVATIE, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement statementVoorstellingPlaatsen = connection.prepareStatement(UPDATE_PLAATSEN_VAN_VOORSTELLING)){
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			statementVoorstellingPlaatsen.setInt(1, reservatie.getPlaatsen());
			statementVoorstellingPlaatsen.setInt(2, reservatie.getVoorstellingsid());
			statementVoorstellingPlaatsen.setInt(3, reservatie.getPlaatsen());				
			
			if (statementVoorstellingPlaatsen.executeUpdate() == 1){
				statement.setInt(1, reservatie.getKlantid());
				statement.setInt(2, reservatie.getVoorstellingsid());			
				statement.setInt(3, reservatie.getPlaatsen());			
				statement.executeUpdate();			
				try (ResultSet resultSet = statement.getGeneratedKeys()) {
					if (resultSet.next()){						
						reservatie.setId(resultSet.getInt(1));
						connection.commit();	
						return true;
					} else {
						return false;
					}
				}				
			} else {
				return false;
			}
		} catch (SQLException ex){
			throw new DAOException(ex);
		}
	}
	
	public Map<Integer, Boolean> createBatch (List<Reservatie> reservaties){
		Map <Integer, Boolean> resultBatch = new HashMap<>();
		try (Connection connection = dataSource.getConnection()){
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			try (PreparedStatement statement = connection.prepareStatement(INSERT_RESERVATIE, Statement.RETURN_GENERATED_KEYS);
				 PreparedStatement statementVoorstellingPlaatsen = connection.prepareStatement(UPDATE_PLAATSEN_VAN_VOORSTELLING)){
				for (Reservatie reservatie : reservaties){				
					// try updating each voorstelling
					statementVoorstellingPlaatsen.setInt(1, reservatie.getPlaatsen());
					statementVoorstellingPlaatsen.setInt(2, reservatie.getVoorstellingsid());
					statementVoorstellingPlaatsen.setInt(3, reservatie.getPlaatsen());	
					if (statementVoorstellingPlaatsen.executeUpdate() == 1){
						// update gelukt = insert reservatie
						statement.setInt(1, reservatie.getKlantid());
						statement.setInt(2, reservatie.getVoorstellingsid());			
						statement.setInt(3, reservatie.getPlaatsen());			
						statement.executeUpdate();
						try (ResultSet resultSet = statement.getGeneratedKeys()) {
							if (resultSet.next()){	
								// insert reservatie gelukt
								reservatie.setId(resultSet.getInt(1));
								resultBatch.put(reservatie.getVoorstellingsid(), true);
							} else {
								// insert reservatie niet gelukt
								resultBatch.put(reservatie.getVoorstellingsid(), false);
							}								
						}
					} else {
						// update niet gelukt
						resultBatch.put(reservatie.getVoorstellingsid(), false);
					}					
				}
			}	
			connection.commit();
			return resultBatch;			 
		} catch (SQLException ex){
			throw new DAOException(ex);
		}
	}
}
