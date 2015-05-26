package formulaire;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import modele.Data;
import modele.Logement;
import modele.Offre;
import modele.Utilisateur;
import utilitaire.CustomDate;

public class FormulaireRechercheAnnonce {

	private String ville;
	private String dateDebut;
	private String dateFin;

	public FormulaireRechercheAnnonce(String ville,String dateDebut,String dateFin) {
		this.ville = ville;
		this.setDateDebut(dateDebut);
		this.setDateFin(dateFin);
	}

	public String getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(String dateDebut) {
		this.dateDebut = CustomDate.checkFormatDate(dateDebut);
	}

	public String getDateFin() {
		return dateFin;
	}

	public void setDateFin(String dateFin) {
		this.dateFin = CustomDate.checkFormatDate(dateFin);
	}

	/**
	 * @return liste des offres pour une ville donnee
	 * @throws Exception
	 */
	public List<Offre> getListeOffre() throws Exception {
		List<Offre> result = new ArrayList<Offre>();
		boolean dateSpecifiee = this.dateDebut!=null && this.dateFin!=null;

		result.addAll(getOffreSansPostulation(dateSpecifiee));
		result.addAll(getOffreAvecPostulation(dateSpecifiee));
		if (result.isEmpty()){
			throw new Exception("Aucun logement a "+this.ville);
		}
		return result;
	}

	/**
	 * @param dateSpecifiee (si des dates ont ete specifiees pour la recherche)
	 * @return la liste des logement proposes n'ayant recu aucune demande
	 * @throws Exception
	 */
	private List<Offre> getOffreSansPostulation(boolean dateSpecifiee) throws Exception {
		List<Offre> result = new ArrayList<Offre>();
		String strReq;
		strReq = "SELECT Logement.IdLogement,Utilisateur.IdUtilisateur,Logement.DateDebut,Logement.DateFin "
				+ "FROM Utilisateur,Logement "
				+ "WHERE Logement.IdLogement=Utilisateur.IdLogement AND Logement.ville = ? "
				+ "AND (Logement.IdLogement NOT IN "
				+ "(SELECT Postule.IdLogement FROM Postule)) ";
		if(dateSpecifiee){
			strReq += "AND Logement.DateDebut <= ? AND Logement.DateFin >= ? ";
		}
		PreparedStatement s = Data.BDD_Connection.prepareStatement(strReq);
		s.setString(1, this.ville);
		if(dateSpecifiee){
			s.setDate(2, Date.valueOf(this.dateFin));
			s.setDate(3, Date.valueOf(this.dateDebut));
		}

		ResultSet rs=s.executeQuery();
		while (rs.next()){
			Logement l=Logement.getLogementById(rs.getInt(1));
			Utilisateur u=Utilisateur.getUtilisateurByIdLogement(rs.getInt(1));
			result.add(new Offre(l, u, rs.getDate(3), rs.getDate(4)));
		}
		return result;
	}

	private List<Offre> getOffreAvecPostulation(boolean dateSpecifiee) throws Exception {
		List<Offre> result = new ArrayList<Offre>();
		List<Offre> resultAccepte = getRestes(getOffreValideesCompactees(dateSpecifiee));
		List<Offre> resultNonAccepte = getNonAccepteLogementDiffDesAcceptes(dateSpecifiee);
		result.addAll(resultAccepte);
		result.addAll(resultNonAccepte);
		
		return result;
	}

	/**
	 * @param dateSpecifiee (si des dates ont ete specifiees pour la recherche)
	 * @return la liste des offres validees
	 * @throws Exception
	 */
	private List<Offre> getOffreValidees(boolean dateSpecifiee) throws Exception{
		List<Offre> resultAccepte = new ArrayList<Offre>();

		String strReqAccepte = "SELECT Postule.IdLogement,Postule.IdUtilisateur,Postule.dateDebut,Postule.dateFin "
				+ "FROM Postule,Logement "
				+ "WHERE (Postule.Status = 1) "
				+ "AND Logement.Ville = ? "
				+ "AND Postule.IdLogement = Logement.IdLogement "
				+ (dateSpecifiee ? "AND Logement.DateDebut <= ? AND Logement.DateFin >= ? " : "")
				+ "ORDER BY Postule.IdLogement DESC, Postule.dateDebut ASC ";
		PreparedStatement sAccepte = Data.BDD_Connection.prepareStatement(strReqAccepte);
		sAccepte.setString(1,this.ville);
		if(dateSpecifiee){
			sAccepte.setDate(2, Date.valueOf(this.dateFin));
			sAccepte.setDate(3, Date.valueOf(this.dateDebut));
		}
		ResultSet rsAccepte = sAccepte.executeQuery();
		while (rsAccepte.next()){
			Logement l=Logement.getLogementById(rsAccepte.getInt(1));
			Utilisateur u=Utilisateur.getUtilisateurByIdLogement(rsAccepte.getInt(1));
			resultAccepte.add(new Offre(l, u, rsAccepte.getDate(3), rsAccepte.getDate(4)));
		}
		return resultAccepte;
	}

	/**
	 * @param dateSpecifiee (si des dates ont ete specifiees pour la recherche)
	 * @return la liste des plages de dates bloquees par les offres validees, apres compactage
	 * @throws Exception
	 */
	private List<Offre> getOffreValideesCompactees(boolean dateSpecifiee) throws Exception {		
		List<Offre> resultAccepteReste = new ArrayList<Offre>();
		Offre derniereOffre = null;
		boolean toAdd = true;

		for(Offre offre : getOffreValidees(dateSpecifiee)){
			if(!resultAccepteReste.isEmpty()){
				if(offre.getLogement().getIdLogement() == derniereOffre.getLogement().getIdLogement()){
					//comparer les dates
					Date finPlusUnJ = derniereOffre.getDateFin();
					int[] tabDate = CustomDate.splitDate(finPlusUnJ.toString());
					finPlusUnJ = Date.valueOf(CustomDate.creerStringDate(tabDate[0],tabDate[1],tabDate[2]+1));
					//compacter les offres
					//et mettre a jour la liste
					if(offre.getDateDebut().toString().equals(finPlusUnJ.toString())) {
						derniereOffre.setDateFin(offre.getDateFin());
						toAdd=false;
					}
				}
			}
			derniereOffre = offre;
			if(toAdd){
				resultAccepteReste.add(offre);
				toAdd=true;
			}
		}
		return resultAccepteReste;
	}

	/**
	 * @param offreValideesCompactees 
	 * @return liste des possibilites de postulation en ne considerant que les restes laisses par les offres aceptees
	 * @throws Exception
	 */
	private List<Offre> getRestes(List<Offre> offreValideesCompactees) throws Exception {
		List<Offre> result = new ArrayList<Offre>();
		Logement nouveauLogement = null;

		for(Offre offre : offreValideesCompactees){
			if(result.isEmpty()){
				nouveauLogement = Logement.getLogementById(offre.getLogement().getIdLogement());
			}else{
				//le dernier bout a été ajouté par défaut a la passe précédante
				//si on a pas changé de logement, on supprime ce "bout", pour le corriger
				if(offre.getLogement().getIdLogement() == nouveauLogement.getIdLogement()){
					result.remove(result.size()-1);
				}
			}
			
			//il y a un reste avant la demande
			if(nouveauLogement.getDateDebut().compareTo(offre.getDateDebut()) < 0 ){
				//date moins un jour
				Date dateModif = offre.getDateDebut();
				int[] tabDate = CustomDate.splitDate(dateModif.toString());
				dateModif = Date.valueOf(CustomDate.creerStringDate(tabDate[0],tabDate[1],tabDate[2]-1));
				//ajout de la plage libre
				Utilisateur u=Utilisateur.getUtilisateurByIdLogement(nouveauLogement.getIdLogement());
				result.add(new Offre(nouveauLogement,u,nouveauLogement.getDateDebut(),dateModif));
				
				//date plus un jour
				dateModif = offre.getDateFin();
				tabDate = CustomDate.splitDate(dateModif.toString());
				dateModif = Date.valueOf(CustomDate.creerStringDate(tabDate[0],tabDate[1],tabDate[2]+1));
				//maj du debut logement libre
				nouveauLogement.setDateDebutFin(dateModif, nouveauLogement.getDateFin());
			}
			//il y a un reste apres la demande
			if(nouveauLogement.getDateFin().compareTo(offre.getDateFin()) > 0 ){
				//date plus un jour
				Date dateModif = offre.getDateFin();
				int[] tabDate = CustomDate.splitDate(dateModif.toString());
				dateModif = Date.valueOf(CustomDate.creerStringDate(tabDate[0],tabDate[1],tabDate[2]+1));
				//maj du logment => il restera que la partie de droite (voir ci dessus)
				nouveauLogement.setDateDebutFin(dateModif,nouveauLogement.getDateFin());
				//attention, date début a été mis ajour
				//ajout du logement
				Utilisateur u=Utilisateur.getUtilisateurByIdLogement(nouveauLogement.getIdLogement());
				result.add(new Offre(nouveauLogement,u,nouveauLogement.getDateDebut(),nouveauLogement.getDateFin()));
			}
		}
		return result;
	}
	
	/** 
	 * @param dateSpecifiee (si des dates ont ete specifiees pour la recherche)
	 * @return liste des possibilites de postulation, qui ne concernant pas des logements impliques dans des postulation acceptees
	 * @throws Exception
	 */
	private List<Offre> getNonAccepteLogementDiffDesAcceptes(boolean dateSpecifiee) throws Exception {
		List<Offre> result = new ArrayList<Offre>();
		
		String strReq = ""
				+ "SELECT Postule.IdLogement,Postule.IdUtilisateur "
				//Pourquoi utiliser la table utilisateur???
				+ "FROM Logement,Postule "
				+ "WHERE Postule.IdLogement = Logement.IdLogement "
				+ "AND Logement.ville = ? "
				+ "AND Postule.Status != 1 "
				+ (dateSpecifiee ? "AND Logement.DateDebut <= ? AND Logement.DateFin >= ? " : "")
				+ "AND Postule.IdLogement NOT IN "
					+ "(SELECT Postule.IdLogement "
					+ "FROM Postule "
					+ "WHERE Postule.Status=1)";
					
		PreparedStatement s = Data.BDD_Connection.prepareStatement(strReq);
		s.setString(1, this.ville);

		if(dateSpecifiee){
			s.setDate(2, Date.valueOf(this.dateFin));
			s.setDate(3, Date.valueOf(this.dateDebut));
		}

		ResultSet rs=s.executeQuery();
		while (rs.next()){
			Logement l=Logement.getLogementById(rs.getInt(1));
			Utilisateur u=Utilisateur.getUtilisateurByIdLogement(rs.getInt(1));
			result.add(new Offre(l, u, l.getDateDebut(),l.getDateFin()));
		}
		
		return result;
	}
}
