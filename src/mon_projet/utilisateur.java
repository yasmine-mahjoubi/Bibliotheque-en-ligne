package mon_projet;
import java.sql.*;
import java.util.Scanner;

public class utilisateur {
	private int id_utilisateur;
	private String nom,prenom,login,pwd,role;
	/*public utilisateur(int id_utilisateur, String nom, String prenom, String login, String pwd, String role) {
		 this.id_utilisateur=id_utilisateur;
		 this.nom=nom;
		 this.prenom=prenom;
		 this.login=login;
		 this.pwd=pwd;
		 this.role=role;
	}*/
	public utilisateur() {
		
	}
	public static utilisateur authentifier(Scanner ent) throws SQLException
	{
		utilisateur user = new utilisateur();
		System.out.println("saisir votre login: ");
        
        String logi=ent.nextLine();
        System.out.println("saisir votre mot de passe: ");
        
        String pw=ent.nextLine();
        ResultSet res=jdbc.executeQuery("SELECT * FROM utilisateur WHERE login=? AND pwd=?",logi,pw);
        
        if (res.next()) 
        {
        	user.id_utilisateur=res.getInt("id_utilisateur");
            user.role = res.getString("role");
            user.nom=res.getString("nom");
            user.prenom=res.getString("prenom");
            user.login=res.getString("login");
            user.pwd=res.getString("pwd");
            System.out.println("Bienvenue Mr/Ms "+user.prenom+", vous etes un "+user.role );
            res.close();
            return user;
        } else 
        {
        	 System.out.println("echec de login , utilisateur introuvé!!");
        }
        return null;
	}
	
	public void setID(int a)
	{
		id_utilisateur = a;
	}
	public int getID() 
	{
		return id_utilisateur;
	}
	public void setNOM(String a)
	{
		nom = a;
	}
	public String getNOM() 
	{
		return nom;
	}
	public void setPRENOM(String a)
	{
		prenom = a;
	}
	public String getPRENOM() 
	{
		return prenom;
	}
	public void setLOGIN(String a)
	{
		login = a;
	}
	public String getLOGIN() 
	{
		return login;
	}
	public void setPWD(String a)
	{
		pwd = a;
	}
	public String getPWD() 
	{
		return pwd;
	}
	public void setROLE(String a)
	{
		role = a;
	}
	public String getROLE() 
	{
		return role;
	}
}
