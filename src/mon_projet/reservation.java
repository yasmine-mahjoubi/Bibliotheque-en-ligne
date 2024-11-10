package mon_projet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class reservation {
	private int id_reservation;
	private String statut;
	private LocalDate date_reservation = LocalDate.now(); 
	
	public void setIDresv(int a)
	{
		id_reservation = a;
	}
	public int getIDresv() 
	{
		return id_reservation;
	}
	public void setStatut(String a)
	{
		statut = a;
	}
	public String getStatut() 
	{
		return statut;
	}
	public void setDateRes(LocalDate a)
	{
		date_reservation = a;
	}
	public LocalDate getDateRes() 
	{
		return date_reservation;
	}
	
	public void eff_reservation(int idl,int iduser)throws SQLException
	{	//preciser le statut de la reservation
		
		ResultSet res3 = jdbc.executeQuery("SELECT * from reservation where id_livre=?",idl);
		
		if((res3.next()==false) || (res3.getString("statut").equals("annulée")))
		{	
			statut="confirmée";
		}
		else
		{
			statut="en attente";
		}
		
		
		//inserer l'utilisateur dans la table reservation 
		jdbc.executeUpdate("INSERT INTO reservation VALUES (?,?,?,?,?)",id_reservation,iduser,idl,date_reservation,statut);
		System.out.println("votre reservation est  "+statut);
		
	}
	
	public static void annuler_reservation(int idl,int iduser) throws SQLException
	{
			System.out.println("****ANNULATION D'UNE RESERVATION****");
			//ResultSet res3 = jdbc.executeQuery("SELECT * FROM reservation where id_livre=? AND id_utilisateur=?",idl,iduser);
			/*if(res3.next())
			{
				int resUpdate = jdbc.executeUpdate("UPDATE reservation SET statut=? WHERE id_utilisateur=? AND id_livre=?","annulée",iduser,idl);
	        	jdbc.executeUpdate("DELETE FROM reservation  WHERE statut=?","annulée");
				System.out.println("votre reservation a été  annulée");
			}
			else
			{
				System.out.println("Acunne reservation trouvée");
			}*/
			int resUpdate = jdbc.executeUpdate("UPDATE reservation SET statut=? WHERE id_utilisateur=? AND id_livre=?","Annulée",iduser,idl);
        	jdbc.executeUpdate("DELETE FROM reservation  WHERE statut=?","annulée");
			System.out.println("votre reservation a été  annulée");
     
	}
	
	public static void annulerReservation(int idLivre, int idUtilisateur) throws SQLException {
        // Vérifier si la réservation existe
        ResultSet res = jdbc.executeQuery("SELECT * FROM reservation WHERE id_livre=? AND id_utilisateur=? AND statut=?",
                idLivre, idUtilisateur, "en attente");

        if (res.next()) {
            // La réservation existe, l'annuler
            jdbc.executeUpdate("DELETE FROM reservation WHERE id_livre=? AND id_utilisateur=? AND statut=?",
                    idLivre, idUtilisateur, "en attente");

            // Mettre à jour la disponibilité du livre
            jdbc.executeUpdate("UPDATE livre SET disponibilite=? WHERE id_livre=?", "disponible", idLivre);

            System.out.println("La réservation a été annulée avec succès.");
        } else {
            System.out.println("Aucune réservation en attente trouvée pour ce livre et cet utilisateur.");
        }
    }
}
