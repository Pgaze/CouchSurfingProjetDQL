package modele;

import java.io.Serializable;

public class Critere implements Serializable {

	private static final long serialVersionUID = -8425933680785263266L;

	public enum TypeCritere {
		COMMERCE, SOINS, RESTAURANT, INTERNET, HANDICAPE, TRANSPORT, FUMEUR, ANIMAUX, PARKING
	};

	private TypeCritere type;
	private Boolean valeur;
	private String description;

	public Critere(TypeCritere type, Boolean valeur, String description) {
		this.type = type;
		this.valeur = valeur;
		this.description = description;
	}

	public String getNomCritere() {
		switch (this.type) {
		case COMMERCE:
			return "crCommerce";
		case SOINS:
			return "crSoins";
		case ANIMAUX:
			return "crAnimaux";
		case FUMEUR:
			return  "crFumeur";
		case HANDICAPE:
			return "crHandicape";
		case INTERNET:
			return "crInternet";
		case PARKING:
			return "crParking";
		case RESTAURANT:
			return "crRestaurant";
		case TRANSPORT:
			return "crTransport";
		default:
			return "";
		}
	}

	public String getTitreCritere() {
		switch (this.type) {
		case COMMERCE:
			return "Commerce";
		case SOINS:
			return "Soins";
		case ANIMAUX:
			return "Animaux";
		case FUMEUR:
			return  "Fumeur";
		case HANDICAPE:
			return "Handicape";
		case INTERNET:
			return "Internet";
		case PARKING:
			return "Parking";
		case RESTAURANT:
			return "Restaurant";
		case TRANSPORT:
			return "Transport";
		default:
			return "";
		}
	}

	public String getIcone() {
		switch (this.type) {
		case COMMERCE:
			return "icon-basket-1";
		case SOINS:
			return "icon-h-sigh";
		case ANIMAUX:
			return "icon-paw";
		case FUMEUR:
			return  "icon-fire";
		case HANDICAPE:
			return "icon-wheelchair";
		case INTERNET:
			return "icon-signal";
		case PARKING:
			return "icon-cab";
		case RESTAURANT:
			return "icon-restaurant";
		case TRANSPORT:
			return "icon-bus";
		default:
			return "";
		}
	}


	public TypeCritere getType() {
		return type;
	}

	public void setType(TypeCritere type) {
		this.type = type;
	}

	public Boolean getValeur() {
		return valeur;
	}

	public void setValeur(Boolean valeur) {
		this.valeur = valeur;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
