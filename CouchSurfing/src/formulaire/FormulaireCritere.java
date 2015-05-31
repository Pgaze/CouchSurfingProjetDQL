package formulaire;

import java.sql.Date;

import javax.naming.directory.InvalidAttributeValueException;

import modele.Critere;
import modele.Critere.TypeCritere;
import modele.Logement;

public class FormulaireCritere {
	
	private String dateDebut;
	private String dateFin;
	private String crCommerce;
	private String crHopitaux;
	private String crRestaurants;
	private String crTransports;
	private String crAnimaux;
	private String crInternet;
	private String crHandicapes;
	private String crFumeur;
	private String crParking;
	
	public FormulaireCritere(String crCommerce, String crHopitaux,
			String crRestaurants, String crTransports, String crAnimaux,
			String crInternet, String crHandicapes, String crFumeur,
			String crParking, String dateDebut, String dateFin) {
		super();
		this.crCommerce = crCommerce;
		this.crHopitaux = crHopitaux;
		this.crRestaurants = crRestaurants;
		this.crTransports = crTransports;
		this.crAnimaux = crAnimaux;
		this.crInternet = crInternet;
		this.crHandicapes = crHandicapes;
		this.crFumeur = crFumeur;
		this.crParking = crParking;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}
	
	public void setCritereOnLogement(Logement l) {
		this.addCritere(l, TypeCritere.ANIMAUX, this.crAnimaux);
		this.addCritere(l, TypeCritere.COMMERCE, this.crCommerce);
		this.addCritere(l, TypeCritere.FUMEUR, this.crFumeur);
		this.addCritere(l, TypeCritere.HANDICAPE, this.crHandicapes);
		this.addCritere(l, TypeCritere.INTERNET, this.crInternet);
		this.addCritere(l, TypeCritere.PARKING, this.crParking);
		this.addCritere(l, TypeCritere.RESTAURANT, this.crRestaurants);
		this.addCritere(l, TypeCritere.SOINS, this.crHopitaux);
		this.addCritere(l, TypeCritere.TRANSPORT, this.crTransports);
	}
	
	public void setDateOnLogement(Logement l)throws InvalidAttributeValueException{
		if(!(this.dateDebut.equals("") && this.dateFin.equals(""))){
			l.setDateDebutFin(Date.valueOf(this.dateDebut), Date.valueOf(this.dateFin));	
		}
	}

	private void addCritere(Logement l,TypeCritere t, String s) {
		if(!s.equals("")){
			l.addCritere(new Critere(t, true, s));
		}
	}
}
