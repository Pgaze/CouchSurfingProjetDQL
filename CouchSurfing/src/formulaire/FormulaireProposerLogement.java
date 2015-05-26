package formulaire;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.directory.InvalidAttributeValueException;

import modele.Adresse;
import modele.Data;
import modele.Logement;
import modele.Utilisateur;
import utilitaire.CustomDate;

public class FormulaireProposerLogement {
	
	private String batimentEscalier;
	private String numeroEtVoie;
	private String cp;
	private String residence;
	private String complementAdresse;
	private String ville;
	private Utilisateur user;
	private Date dateDebut,dateFin;
	
	/** Ne pas oublier d'utiliser setDateDebutFin !
	 * @param batimentEscalier
	 * @param numeroEtVoie
	 * @param cp
	 * @param residence
	 * @param complementAdresse
	 * @param ville
	 * @param user
	 * @param dateDebut
	 * @param dateFin
	 */
	public FormulaireProposerLogement(String batimentEscalier,
			String numeroEtVoie, String cp, String residence,
			String complementAdresse, String ville, Utilisateur user) {
		this.setBatimentEscalier(batimentEscalier);
		this.setNumeroEtVoie(numeroEtVoie);
		this.setCp(cp);
		this.setResidence(residence);
		this.setComplementAdresse(complementAdresse);
		this.setVille(ville);
		this.setUser(user);
	}
	
	public void setDateDebutFin(Date dateDebut,Date dateFin) throws InvalidAttributeValueException {
		CustomDate.checkIntegriteDates(dateDebut, dateFin);
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}
	
	public String getBatimentEscalier() {
		return batimentEscalier;
	}
	public void setBatimentEscalier(String batimentEscalier) {
		this.batimentEscalier = batimentEscalier;
	}
	public String getNumeroEtVoie() {
		return numeroEtVoie;
	}
	public void setNumeroEtVoie(String numeroEtVoie) {
		this.numeroEtVoie = numeroEtVoie;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence = residence;
	}
	public String getComplementAdresse() {
		return complementAdresse;
	}
	public void setComplementAdresse(String complementAdresse) {
		this.complementAdresse = complementAdresse;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
	
	public boolean verificationCp(){
		return this.cp.matches("[0-9]{5}");
	}
	
	/** Ajoute un logement lie a un utilisateur/hebergeur
	 * @return stateOfResult
	 * @throws SQLException
	 */
	public String procedureAjoutLogement() throws SQLException{
		String result="";
		Logement l = this.getLogement();
		if(this.user.aUnLogement()){
			Logement.deleteFromBase(user.getIdLogement());
		}
		boolean resultatInsertionLogement = l.insererDansLaBase();
		PreparedStatement update = Data.BDD_Connection.prepareStatement("UPDATE Utilisateur SET IdLogement=? WHERE IdUtilisateur=?");
		update.setInt(1, l.getIdLogement());
		update.setInt(2, this.getUser().getIdUser());
		int res = update.executeUpdate();
		if (res==1 && resultatInsertionLogement){
			user.setIdLogement(l.getIdLogement());
			result="Logement ajoute";
		}else{
			result="Echec cr�ation logement";
		}
		return result;
	}
	
	public void procedureSpecDateLogement(Date dateD, Date dateF) throws InvalidAttributeValueException, javax.management.InvalidAttributeValueException, SQLException{
		Logement logement = getLogement();
		this.setDateDebutFin(dateD, dateF);
		logement.setDateDebutFin(this.dateDebut, this.dateFin);
		logement.updateDates();
	}
	
	public Logement getLogement(){
		try {
			return new Logement(new Adresse(batimentEscalier, numeroEtVoie, cp, residence, complementAdresse, ville));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
