package mon_projet;

import java.sql.Connection;
import java.sql.*;

public class jdbc {
	
	    // Les informations de connexion à la base de données
	    private static final String URL = "jdbc:mysql://localhost:3306/db";
	    private static final String USER = "htc";
	    private static final String PASSWORD = "root";
	    //static Connection connection;
	    static Connection connection ;
        static PreparedStatement preparedStatement;
        static ResultSet resultSet ;

	    // Méthode pour établir la connexion à la base de données
	    public static Connection connectToDatabase() throws SQLException 
	    {
	        return DriverManager.getConnection(URL, USER, PASSWORD);
	    }

	    // Méthode pour fermer la connexion à la base de données
	    public static void closeConnection() {
	        try {
	            if (connection != null && !connection.isClosed()) {
	                connection.close();
	                //System.out.println("Connection fermée avec succès.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static void closePreparedStatement() {
	        try {
	            if (preparedStatement != null && !preparedStatement.isClosed()) {
	                preparedStatement.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Méthode pour exécuter une requête de sélection SQL
	    public static ResultSet executeQuery(String sql, Object... parameters) throws SQLException {//object pour accepter un nbre de parametres variables 
	        /*Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;*/
	            connection = connectToDatabase();
	            preparedStatement = connection.prepareStatement(sql);

	            // Définir les paramètres s'il y en a
	            for (int i = 0; i < parameters.length; i++) {
	                preparedStatement.setObject(i + 1, parameters[i]);
	            }

	            // Exécuter la requête de sélection
	            resultSet = preparedStatement.executeQuery();

	            return resultSet;
	        
	    }
	    public static ResultSet executeQuery2(String sql) throws SQLException 
	    {//object pour accepter un nbre de parametres variables 
	        
	            connection = connectToDatabase();
	            preparedStatement = connection.prepareStatement(sql);

	            // Exécuter la requête de sélection
	            resultSet = preparedStatement.executeQuery();

	            return resultSet;
	    }
	    // Méthode pour exécuter une mise à jour SQL (insert, update, delete)
	    public static int executeUpdate(String sql, Object... parameters) throws SQLException {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	            connection = connectToDatabase();
	            preparedStatement = connection.prepareStatement(sql);

	            // Définir les paramètres s'il y en a
	            for (int i = 0; i < parameters.length; i++) {
	                preparedStatement.setObject(i + 1, parameters[i]);
	            }

	            // Exécuter la mise à jour
	            return preparedStatement.executeUpdate();
	    }
}
