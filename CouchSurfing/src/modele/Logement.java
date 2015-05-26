
package modele;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributeValueException;

import modele.Critere.TypeCritere;
import utilitaire.CustomDate;

public class Logement {

	private int idLogement;
	private Adresse adresse;
	private List<Critere> lesCriteres;
	private Date dateDebut;
	private Date dateFin;

	/**
	 * @param adresse
	 * @throws SQLException
	 */
	public Logement(Adresse adresse) throws SQLException {
		this.setAdresse(adresse);
		this.lesCriteres = new ArrayList<Critere>();
	}
	
	public Logement() {
		this.lesCriteres = new ArrayList<Critere>();
	}

/*	private void setId() throws SQLException {
		PreparedStatement select=Data.BDD_Connection.prepareStatement("SELECT IdLogement FROM Logement WHERE"
				+ " BatimentEscalier=? AND NumeroEtVoie=? AND CodePostal=? AND Residence=? "
				+ "AND ComplementAdresse=? AND Ville=?");
		select.setString(1, this.adresse.getBatimentEscalier());
		select.setString(2,this.adresse.getNumeroEtVoie());
		select.setString(3, this.adresse.getCp());
		select.setString(4, this.adresse.getResidence());
		select.setString(5, this.adresse.getComplementAdresse());
		select.setString(6,this.adresse.getVille());
		ResultSet resultSelect=select.executeQuery();
		if(resultSelect.next()){
			this.idLogement=resultSelect.getInt(1);
		}else{	
			Statement getMax=Data.BDD_Connection.createStatement();
			ResultSet resultMax=getMax.executeQuery("SELECT MAX(IdLogement) FROM Logement ");
			resultMax.next();
			this.idLogement=resultMax.getInt(1)+1;
		}

	}*/

	public boolean insererDansLaBase() throws SQLException{
		String sql = "insert into Logement (BatimentEscalier,NumeroEtVoie,CodePostal,Residence,ComplementAdresse,Ville)"
					+ "values (?,?,?,?,?,?)";
		PreparedStatement insert= Data.BDD_Connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		insert.setString(1, this.adresse.getBatimentEscalier());
		insert.setString(2,this.adresse.getNumeroEtVoie());
		insert.setString(3, this.adresse.getCp());
		insert.setString(4, this.adresse.getResidence());
		insert.setString(5, this.adresse.getComplementAdresse());
		insert.setString(6,this.adresse.getVille());
		int res=insert.executeUpdate();
		if (res==1){
			ResultSet rs = insert.getGeneratedKeys();
			rs.next();
			this.idLogement = rs.getInt(1);
			return true;
		}
		return false;
	}

	public int getIdLogement() {
		return idLogement;
	}

	public void setIdLogement(int idLogement) {
		this.idLogement = idLogement;
	}

	public Adresse getAdresse() {
		return adresse;
	}
	
	public Date getDateDebut() {
		return dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public List<Critere> getLesCriteres() {
		return lesCriteres;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	/**
	 * @param idLogement
	 * @return leLogement
	 * @throws Exception
	 */
	public static Logement getLogementById(int idLogement) throws Exception{
		Logement result= new Logement();
		PreparedStatement ps=Data.BDD_Connection.prepareStatement("select BatimentEscalier,ComplementAdresse,CodePostal,NumeroEtVoie,Residence,Ville,DateDebut,DateFin,ListCriteres from Logement where IdLogement=?");
		ps.setInt(1, idLogement);
		ResultSet rs = ps.executeQuery();
		if (rs.next()){
			String batimentEscalier = rs.getString(1);
			String complementAdresse = rs.getString(2);
			String cp = rs.getString(3);
			String numeroEtVoie = rs.getString(4);
			String residence = rs.getString(5);
			String ville = rs.getString(6);
			Date dateDebut = rs.getDate(7);
			Date dateFin = rs.getDate(8);
			result=new Logement(new Adresse(batimentEscalier, numeroEtVoie, cp, residence, complementAdresse, ville));
			result.dateDebut = dateDebut;
			result.dateFin = dateFin;
			result.setIdLogement(idLogement);
			Object temp = rs.getObject("ListCriteres");
			List<Critere> listCritere = (List<Critere>)temp;
			if(listCritere!=null){
				result.lesCriteres = listCritere;
			}
		}
		else{
			throw new Exception("Id inexistant");
		}
		return result;
	}
	
	
	public void addCritere(Critere crit){
		this.lesCriteres.add(crit);
	}
	
	public void removeCritere(TypeCritere critType){
		for (int i=0;i<this.lesCriteres.size();i++){
			if(this.lesCriteres.get(i).getType()==critType){
				this.lesCriteres.remove(i);
				break;
			}
		}
	}
	
	public void setCritereValue(Critere crit){
		for (int i=0;i<this.lesCriteres.size();i++){
			if(this.lesCriteres.get(i).getType()==crit.getType()){
				this.lesCriteres.set(i, crit);
				break;
			}
		}
	}

	public Critere getCritere(TypeCritere critType) {
		Critere result=null;
		for(Critere c : this.lesCriteres){
			if(c.getType()==critType){
				result=c;
			}
		}
		return result;
	}
	
	public int getIdPhotoLogement() throws SQLException{
		String sql = "SELECT IdImageLogement FROM Logement where IdLogement=?";
		PreparedStatement select = Data.BDD_Connection.prepareStatement(sql);
		select.setInt(1,this.getIdLogement());
		ResultSet res = select.executeQuery();
		if(res.next() && res.getInt(1)!=0){
			return res.getInt(1);
		}
		else{
			return -1;
		}
	}

	public boolean setIdImageLogement(int idImage) throws SQLException {
		String sql="UPDATE Logement set IdImageLogement=? where IdLogement=?";
		PreparedStatement update = Data.BDD_Connection.prepareStatement(sql);
		update.setInt(1, idImage);
		update.setInt(2, this.getIdLogement());
		return update.executeUpdate()==1;
	}

	public void setDateDebutFin(Date dateDebut,Date dateFin) throws InvalidAttributeValueException {
		CustomDate.checkIntegriteDates(dateDebut, dateFin);
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}
	
	public boolean setDateToNull() throws SQLException{
		String sql= "UPDATE Logement set DateDebut=? AND DateFin=? WHERE IdLogement=?";
		PreparedStatement update=Data.BDD_Connection.prepareStatement(sql);
		update.setNull(1, Types.DATE);
		update.setNull(2, Types.DATE);
		update.setInt(3, this.idLogement);
		return update.executeUpdate()==1;
	}
	
	public boolean updateDates() throws SQLException, javax.management.InvalidAttributeValueException {
		if(this.dateDebut== null || this.dateFin==null){
			throw new javax.management.InvalidAttributeValueException("DateDebut ou DateFin n'a pas ete initialise ");
		}
		String sql="UPDATE Logement SET DateDebut=? , DateFin=? WHERE IdLogement=?";
		PreparedStatement update = Data.BDD_Connection.prepareStatement(sql);
		update.setDate(1, this.dateDebut);
		update.setDate(2, this.dateFin);
		update.setInt(3, this.idLogement);
		return update.executeUpdate() ==1;
	}	
	
	public boolean updateListCritere() throws SQLException{
		String sql= "update Logement set ListCriteres=? where IdLogement=?";
		PreparedStatement update = Data.BDD_Connection.prepareStatement(sql);
		update.setObject(1, this.lesCriteres);
		update.setInt(2, this.idLogement);
		return update.executeUpdate() ==1;
	}

	public static boolean deleteFromBase(int idLogement) throws SQLException {
		String sql="delete from Logement where IdLogement=?";
		PreparedStatement delete = Data.BDD_Connection.prepareStatement(sql);
		delete.setInt(1, idLogement);
		return delete.executeUpdate()==1;
	}

	
}
