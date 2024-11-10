package mon_projet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class projet 
{
    public static void main(String[] args) 
    {
        // Chargement du driver JDBC
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            System.err.println("Erreur lors du chargement du driver");
            System.exit(1);
        }
        try 
        {
        	System.out.println("** bienvenue sur notre application de gestion des emprunts et retour des livres ***");
        	//etape0:s'authentifier à la plateforme
        	Scanner myobj =new Scanner (System.in);
        	utilisateur user = new utilisateur();
            user=utilisateur.authentifier(myobj);
            if(user != null)
            {
            	if (!(user.getROLE().equals("bibliothécaire"))) 
                {
            			//méthode pour affecter en attente pour le statut de class emprunt 
                	    //etape1:aff liste des choix
        	        	System.out.println("Veillez choisir l'un de ces choix:\n");
        	        	System.out.println("1-consultation du catalogue des livres");
        	        	System.out.println("2-Consultation de l'historique des emprunts");
        	        	int choix1=myobj.nextInt();
        	        	if(choix1==1) //consultation du catalogue des livres
            	        {
        	        		livre l =new livre();
    						ResultSet res1=l.affiche_catalogue();
            	        	System.out.println("\n");
                	        System.out.println("1:Voulez-vous rechercher un certain Livre? \n ");
                	        	if(myobj.nextInt()==1)
                	        	{
                	        		System.out.println("Saisir l'ID de livre recherché :\n");
                	        		int ID_rech=myobj.nextInt();
                	        		boolean test=livre.rechercher_livre(ID_rech);
                	        		if(test==true)
                	        		{
                	        			System.out.println(" les Détails du Livre \n");
                    					l.affiche_details(ID_rech);
                	        			System.out.println("Veuillez choisir :\n");
                	        			//choisir entre reservation ou emprunter
                	        			if (l.getDisponibilité().equals("non disponible")) 
        	        					{
        	        						System.out.println("1: Reservation du livre ");
        	        						System.out.println("2_annuler reservation");
        	        					}
                	        			else
        	        					{
        	        						System.out.println("3: Emprunter un livre");
        	        					}
                	        			int choix = myobj.nextInt();
                	        			if(choix==1)//Reserver
                	        			{
        	        						int iduser=user.getID();
        	        						reservation reserv=new reservation();
        	        						reserv.eff_reservation(ID_rech,iduser);
                	        			}
                	        			else if(choix==2) 
            							{	//ANNULER RESERVATION
                	        				System.out.println("Donner l'id de livre ");
            								reservation.annuler_reservation(myobj.nextInt(), user.getID());
            								//reservation.annulerReservation(myobj.nextInt(), user.getID());
            							}
                	        			if(choix==3)//Emprunter
                	        			{
        	        						int iduser=user.getID();
        	        						emprunt emp = new emprunt();
        	        						emp.eff_emprunt(iduser, ID_rech);
                	        			}	
                	        		}
                	        		else
        		    				{
        		    					System.out.println("le livre est introuvable");
        		    				}
            	        }
                	}
        	        if(choix1==2)//Consultation de l'historique des emprunts
        	        {
						emprunt empt=new emprunt();
						boolean test=empt.aff_historique_emprunt(user.getID());
						//possibilitede retourner le livre emprunté si mon historique n'est pas vide
						if(test)
						{
							System.out.println("1-Voulez-vous retourner un livre specifique?");
							if(myobj.nextInt()==1)
							{	
								System.out.println("Saisir l'id du livre à retourner ");
								int id=myobj.nextInt();
								emprunt.retour_livre(id,user.getID());
								
							}
						}
						else
						{
							System.out.println("Votre historique d'emprunt est vide ");
						}
        	        }
                	
                }
                else //le rôle de bibliothécaire 
                {
                	//etape1:aff liste des choix
		            System.out.println("Veillez choisir l'un de ces choix:");
		            System.out.println("1-Notification par e-mail pour les rappels de retour");
		            System.out.println("2-Génération de rapports statistiques");
		           
		            if (myobj.nextInt()==1)
		            {	//envoie du notifications par mail
		            	emprunt.envoieMail();
		            }
		            else
		            {	//generer rapports statiques
		            	System.out.println("1-Génération de rapports statistiques sur les livres les plus empruntés ");
		            	System.out.println("2-Génération de rapports statistiques sur les utilisateurs les plus assidus ");
		            	if (myobj.nextInt()==1)
		            	{
		            		emprunt.genere_livres_plus_empruntés();
		            	}
		            	else
		            	{
		            		emprunt.genere_utilisateurs_plus_assidus();
		            	}
		            }
                }
            }
            myobj.close();
        	jdbc.closePreparedStatement();
        	jdbc.closeConnection();
        }    
    	catch (Exception e) 
        {
            System.err.println("Erreur lors de la connexion à la base" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}