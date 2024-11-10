package mon_projet;
import java.sql.*;

public class livre 
{
	private int id_livre;
	private String titre,auteur,genre,disponibilité;
	public ResultSet affiche_catalogue() throws SQLException
	{
		System.out.println("Notre catalogue : \n");
		ResultSet res1=jdbc.executeQuery2("SELECT id_livre,titre, genre FROM livre");
		
		int i=1;
		while(res1.next()) 
		{
			System.out.println("Livre "+i+": ( id_livre--> "+res1.getInt("id_livre")+", Titre_livre--> "+res1.getString("titre")+
					", Le genre--> "+res1.getString("genre")+") \n");
			i++;
		}
		
		return res1;
	}
	public void setID(int a)
	{
		id_livre = a;
	}
	public int getID() 
	{
		return id_livre;
	}
	public void setTitre(String a)
	{
		titre = a;
	}
	public String getTitre() 
	{
		return titre;
	}
	public void setAuteur(String a)
	{
		auteur = a;
	}
	public String getAuteur() 
	{
		return auteur;
	}
	public void setGenre(String a)
	{
		genre = a;
	}
	public String getGenre() 
	{
		return genre;
	}
	public void setDisponibilité(String a)
	{
		disponibilité = a;
	}
	public String getDisponibilité() 
	{
		return disponibilité;
	}
	
	
	public static boolean rechercher_livre(int ID_rech)throws SQLException
	{   
		ResultSet res1=jdbc.executeQuery2("SELECT id_livre,titre, genre FROM livre");
		boolean test=false;
		while((res1.next()) & (test==false)) 
		{
			if(res1.getInt("id_livre")==ID_rech)
			{	
				test=true;
			}
		}	
		return test;
	}
	
	
	public void affiche_details(int ID_rech) throws SQLException
	{
		ResultSet res2 = jdbc.executeQuery("SELECT * FROM livre WHERE id_livre=? ",ID_rech);
		if (res2.next()) {
		id_livre=res2.getInt("id_livre");
		titre=res2.getString("titre");
		auteur=res2.getString("auteur");
		genre=res2.getString("genre");
		disponibilité=res2.getString("disponibilite");
		
		System.out.println("id_livre-->  "+id_livre+", titre-->  "+titre +", auteur-->  "+auteur+", genre-->  "+genre
				+" ,  disponibilité-->  "+disponibilité);
		}
	}
}
