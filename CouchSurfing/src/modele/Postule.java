package modele;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Postule {
	
	private Utilisateur postulant;
	private Utilisateur hebergeur;
	private Logement logement;
	private int status;
	private String dateDebut;
	private String dateFin;
	private String dateValidite;
	
	public Postule(Utilisateur postulant, Utilisateur hebergeur, Logement logement) {
		super();
		this.postulant = postulant;
		this.hebergeur = hebergeur;
		this.logement = logement;
		this.status = 2;
	}
	
	

	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}



	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}



	/**
	 * @return Liste des Postulation de l'utilisateur qui sont encore valides
	 * @throws Exception 
	 */
	public static ArrayList<Postule> getDemandeEnvoyeByUser(Utilisateur user) throws Exception{
		ArrayList<Postule> tablePostulation = new ArrayList<Postule>();
		String sql = "SELECT IdLogement,Status,DateDebut,DateFin FROM Postule "
				+ "WHERE IdUtilisateur=?  ";
		PreparedStatement select = Data.BDD_Connection.prepareStatement(sql);
		select.setInt(1, user.getIdUser());
		
		ResultSet resultSelect=select.executeQuery();
		
		while(resultSelect.next()){
			Logement logement=Logement.getLogementById(resultSelect.getInt("IdLogement"));
			Utilisateur hebergeur = Utilisateur.getUtilisateurByIdLogement(resultSelect.getInt("IdLogement"));
			Postule temp = new Postule(user, hebergeur, logement);
			temp.status = resultSelect.getInt(2);
			temp.dateDebut = resultSelect.getDate(3).toString();
			temp.dateFin = resultSelect.getDate(4).toString();
			tablePostulation.add(temp);
		}
		return tablePostulation;
	}
	
	/*public static ArrayList<Integer> deletePostulationsPerimees() throws SQLException{
		ArrayList<Integer> table = getPostulationsPerimees();
		//suppresion
		PreparedStatement delete = Data.BDD_Connection.prepareStatement("DELETE FROM Postule WHERE DateInvalidite < CURDATE()");
		delete.executeUpdate();
		return table;
	}*/
	
	
	/**
	 * @return element supprimé
	 * @throws SQLException
	 */
	public static void deletePostulationByIdLogement(int idLogement) throws SQLException{
		//suppresion
		PreparedStatement delete = Data.BDD_Connection.prepareStatement("DELETE FROM Postule WHERE IdLogement=?");
		delete.setInt(1, idLogement);
		delete.executeUpdate();
	}
	
	/**
	 * @param theIdOffre
	 * @return status
	 * @throws SQLException
	 */
	public  boolean postulerAUneOffre() throws SQLException {
		Date today = new Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String myDate = sdf.format(today);
		PreparedStatement ps=Data.BDD_Connection.prepareStatement("INSERT INTO Postule (IdUtilisateur,IdLogement,DatePostule,Status,DateDebut,DateFin) values(?,?,?,?,?,?)");
		ps.setInt(1, this.postulant.getIdUser());
		ps.setInt(2, this.logement.getIdLogement());
		ps.setString(3, myDate);
		ps.setInt(4, 2);
		ps.setDate(5, java.sql.Date.valueOf(dateDebut));
		ps.setDate(6, java.sql.Date.valueOf(dateFin));
		
		return ps.executeUpdate() == 1;
	}

	public Utilisateur getPostulant() {
		return postulant;
	}

	public Utilisateur getHebergeur() {
		return hebergeur;
	}

	public Logement getLogement() {
		return logement;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getAffichagePlage(){
		return "Du "+this.dateDebut +" au "+this.dateFin;
	}

	public static List<Postule> getDemandeRecuByUser(Utilisateur user) throws Exception {
		List<Postule> result = new ArrayList<Postule>();
		String sql = "select Postule.IdUtilisateur,Postule.IdLogement,Status,DateDebut,DateFin from Postule,Utilisateur "
				+ "where Postule.IdLogement=Utilisateur.IdLogement "
				+ "and Utilisateur.IdUtilisateur = ?";
		PreparedStatement select = Data.BDD_Connection.prepareStatement(sql);
		select.setInt(1, user.getIdUser());
		ResultSet resultSelect = select.executeQuery();
		while(resultSelect.next()){
			Utilisateur postulant = Utilisateur.getUtilisateurById(resultSelect.getInt(1));
			Logement logement = Logement.getLogementById(resultSelect.getInt(2));
			Postule temp= new Postule(postulant, user, logement);
			temp.status = resultSelect.getInt(3);
			temp.dateDebut = resultSelect.getDate(4).toString();
			temp.dateFin = resultSelect.getDate(5).toString();
			result.add(temp);
		}
		
		
		return result;
	}
	
	public boolean existInBase() throws SQLException{
		String sql = "SELECT count(*) from Postule where IdUtilisateur=? and IdLogement=? ";
		PreparedStatement count = Data.BDD_Connection.prepareStatement(sql);
		count.setInt(1,this.postulant.getIdUser());
		count.setInt(2, this.logement.getIdLogement());
		ResultSet resultcount = count.executeQuery();
		resultcount.next();
		if(resultcount.getInt(1)!=0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean updateStatus(int newStatus) throws SQLException{
		String sql= "update Postule set Status = ? where IdUtilisateur=? and IdLogement=?";
		PreparedStatement update = Data.BDD_Connection.prepareStatement(sql);
		update.setInt(1, newStatus);
		update.setInt(2, this.postulant.getIdUser());
		update.setInt(3, this.logement.getIdLogement());
		return update.executeUpdate() ==1;
	}
}
