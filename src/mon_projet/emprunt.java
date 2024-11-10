package mon_projet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class emprunt {
	private int id_emprunt;
	private String statut="en cours";
	private LocalDate date_emprunt=LocalDate.now();
	private LocalDate date_retour=date_emprunt.plusDays(15);
	/*public emprunt(int a, String st , String dte , String dtr) {
		this.id_emprunt=a;
		this.statut=st;
		this.date_emprunt=dte;
		this.date_retour=dtr;
	}*/
	public void setIDEmprunt(int a)
	{
		id_emprunt = a;
	}
	public int getIDEmprunt() 
	{
		return id_emprunt;
	}
	public void setStatut(String a)
	{
		statut = a;
	}
	public String getStatut() 
	{
		return statut;
	}
	public void setDateEmp(LocalDate a)
	{
		date_emprunt = a;
	}
	public LocalDate getDateEmp() 
	{
		return date_emprunt;
	}
	public void setDateRt(LocalDate a)
	{
		date_retour = a;
	}
	public LocalDate getDateRt() 
	{
		return date_retour;
	}
	public void eff_emprunt(int id_user,int idl) throws SQLException
	{
		if(jdbc.executeUpdate("INSERT INTO emprunt VALUES (?,?,?,?,?,?)",id_emprunt,id_user,idl,date_emprunt,
				date_retour,statut)>0)
		{
			int resup = jdbc.executeUpdate("UPDATE livre SET disponibilite = ? WHERE id_livre=?", "non disponible",idl);
			System.out.println("Opération Validée ! vous avez emprunté ce livre .");
		}
		else
		{
			System.out.println("Echec d'opération ! esssayer plus tard ..");
		}
	}
	
	public boolean aff_historique_emprunt(int iduser)throws SQLException
	{	LocalDate dateActuelle = LocalDate.now();
		ResultSet res=jdbc.executeQuery("SELECT emprunt.id_livre,livre.titre,"
				+ "emprunt.date_emprunt,emprunt.date_retour,emprunt.statut "
				+ "FROM emprunt join livre ON emprunt.id_livre=livre.id_livre WHERE id_utilisateur=?",iduser);
		boolean test=false;
		while(res.next()) 
		{	
			test=true;
			if(res.getString("statut").equals("en cours"))
			{
				if(res.getDate("date_retour").toLocalDate().isBefore(dateActuelle))
				{
					jdbc.executeUpdate("UPDATE emprunt SET statut =?"+
							" where id_livre=? and id_utilisateur=?","en retard"
							,res.getInt("emprunt.id_livre"),iduser);
					 res.updateString("statut", "en retard");
				}
			}
			System.out.println("** Voici l'historique de votre empruntes **");
			System.out.println("id_livre: "+res.getInt("id_livre")+
					 ", titre: "+res.getString("titre")+
					 ", date_emprunt: "+res.getDate("date_emprunt")+
					 " , date_retour: "+res.getDate("date_retour")+" ,statut: "+res.getString("statut"));
		}
		
		return test;
	   
	}
	
	
	public static void retour_livre(int idl,int iduser)throws SQLException
	{
		// Vérifier l'existence de l'emprunt en cours
	    ResultSet resEmprunt = jdbc.executeQuery("SELECT * FROM emprunt WHERE id_livre=? AND id_utilisateur=? AND statut=?", idl, iduser, "en cours");

	    if (resEmprunt.next()) 
	    {
	    	int up=jdbc.executeUpdate("UPDATE emprunt SET statut =?"+
					" where id_livre=? and id_utilisateur=?","terminé",idl,iduser);
			if(up!=0)		
			{
				System.out.println("votre retour du livre est effectué avec succès");
				/*ResultSet res=jdbc.executeQuery("SELECT * FROM reservation where id_livre=? "
						+ "AND statut=? ", idl,"confirmée");
				if(res.next())
				{	
					emprunt e=new emprunt();
					e.eff_emprunt(res.getInt("id_livre"),res.getInt("id_utilisateur"));
					jdbc.executeUpdate("DELETE FROM reservation  WHERE id_livre=? and statut=?",idl,"confirmée");
				}
				else
				{
					jdbc.executeUpdate("UPDATE livre SET disponibilité=? where id_livre=?","disponible",idl);
				}*/
			}
	    }
	    else
	    {
	    	System.out.println("Vous n'avez aucun livre à retourner ");
	    }
	}
	
	
	public static void envoieMail() throws SQLException {
	    ResultSet res = jdbc.executeQuery("SELECT id_utilisateur,date_retour,id_livre,date_emprunt from emprunt WHERE statut =? ",
	            "en retard");

	    if (res.next()) {
	        do {
	            ResultSet res2 = jdbc.executeQuery("SELECT login FROM utilisateur WHERE id_utilisateur=?",
	                    res.getInt("id_utilisateur"));

	            if (res2.next()) 
	            {
	                System.out.println("Une notification par e-mail est envoyée à " + res2.getString("login") +
	                        " pour les rappels de retour du livre emprunté depuis " + res.getDate("date_emprunt")+" dont l'ID "+res.getInt("id_livre"));
	            } 
	            
	        } while (res.next());
	    } else {
	        System.out.println("Les dates de retour ne sont pas encore dépassées");
	    }
	}
	
	public static void genere_livres_plus_empruntés() throws SQLException
	{	
		ResultSet res = jdbc.executeQuery2("SELECT emprunt.id_livre, livre.titre,"
				+ " COUNT(emprunt.id_livre) AS nb_emprunt FROM emprunt JOIN livre ON emprunt.id_livre = livre.id_livre " +
                "GROUP BY emprunt.id_livre, livre.titre HAVING COUNT(emprunt.id_livre) >= 2");

		
		while(res.next())
		{
			System.out.println("id_livre: "+res.getInt("id_livre")+" , Le titre: "+ res.getString("titre")+" , nb_emprunt: "+res.getInt("nb_emprunt"));
		}

	}
	
	public static void genere_utilisateurs_plus_assidus() throws SQLException
	{	
		ResultSet res = jdbc.executeQuery2("SELECT emprunt.id_utilisateur, utilisateur.nom, utilisateur.prenom, " +
	            "COUNT(emprunt.id_utilisateur) AS nb_emprunt FROM emprunt " +
	            "JOIN utilisateur ON emprunt.id_utilisateur = utilisateur.id_utilisateur " +
	            "GROUP BY emprunt.id_utilisateur, utilisateur.nom, utilisateur.prenom HAVING COUNT(emprunt.id_utilisateur) >= 2");

	    while(res.next())
	    {
	        System.out.println("nom: " + res.getString("nom") +
	                " , prenom: " + res.getString("prenom") +
	                " , nb_emprunt: " + res.getInt("nb_emprunt"));
	    }

	}
	
}
