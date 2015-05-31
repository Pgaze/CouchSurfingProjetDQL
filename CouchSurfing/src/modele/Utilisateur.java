
package modele;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utilitaire.Password;

public class Utilisateur {

	private int idUser;
	private String mail;
	private String password;
	private String name;
	private String firstName;
	private String pseudo;
	private String tel;
	private float indiceConfort;
	private int nbVoteConfort;
	private float indiceConfiance;
	private int nbVoteConfiance;
	private int idLogement;

	/**
	 * @param mail
	 * @param password
	 * @param name
	 * @param firstName
	 * @throws SQLException 
	 */
	public Utilisateur(String mail, String password, String name, String firstName,String pseudo,String tel) throws SQLException {
		this.setMail(mail);
		this.setPassword(password);
		this.setName(name);
		this.setFirstName(firstName);
		this.setPseudo(pseudo);
		this.setTel(tel);
		this.setIdLogement(0);
		this.setIndiceConfiance(0);
		this.setIndiceConfort(0);
		this.setNbVoteConfiance(0);
		this.setNbVoteConfort(0);
	}

	public Utilisateur(String mail){
		this.mail=mail;
	}

	public Utilisateur() {}
	
	public static Utilisateur getUtilisateurParMail(String mail) throws SQLException{
		Utilisateur result = new Utilisateur(mail);
		PreparedStatement select = Data.BDD_Connection.prepareStatement("" +
				"select IdUtilisateur,Nom,Prenom,Mdp,Pseudo,IdLogement,IndiceConfort,NVoteConfort,IndiceConfiance,NVoteConfiance,Telephone from Utilisateur where Mail=?");
		select.setString(1, mail);
		ResultSet rs=select.executeQuery();
		if(rs.next()){
			result.idUser = rs.getInt(1);
			result.setName(rs.getString(2));
			result.setFirstName(rs.getString(3));
			result.setPassword(rs.getString(4));
			result.setPseudo(rs.getString(5));
			result.setIdLogement(rs.getInt(6));
			result.setIndiceConfort(rs.getInt(7));
			result.setNbVoteConfort(rs.getInt(8));
			result.setIndiceConfiance(rs.getInt(9));
			result.setNbVoteConfiance(rs.getInt(10));
			result.tel = rs.getString(11);
		}
		else {
			result = null;
		}
		return result;
	}

	/**
	 * @param idUtilisateur
	 * @return l'utilisateur
	 * @throws SQLException
	 */
	public static Utilisateur getUtilisateurById(int idUtilisateur) throws SQLException{
		Utilisateur result = new Utilisateur();
		PreparedStatement select = Data.BDD_Connection.prepareStatement("" +
				"select Nom,Prenom,Mdp,Pseudo,IdLogement,Mail,IndiceConfort,NVoteConfort,IndiceConfiance,NVoteConfiance,Telephone from Utilisateur where IdUtilisateur=?");
		select.setInt(1, idUtilisateur);
		ResultSet rs=select.executeQuery();
		if(rs.next()){
			result.setName(rs.getString(1));
			result.setFirstName(rs.getString(2));
			result.setPassword(rs.getString(3));
			result.setPseudo(rs.getString(4));
			result.setIdLogement(rs.getInt(5));
			result.setMail(rs.getString(6));
			result.setIndiceConfort(rs.getInt(7));
			result.setNbVoteConfort(rs.getInt(8));
			result.setIndiceConfiance(rs.getInt(9));
			result.setNbVoteConfiance(rs.getInt(10));
			result.tel = rs.getString(11);
			result.idUser = idUtilisateur;
		}
		else{
			result =null;
		}
		return result;

	}


	/** Effectue un vote confiance
	 * @param valeurVote
	 */
	public void voteConfiance(int valeurVote){
		int nbVote=this.getNbVoteConfiance();
		this.setIndiceConfiance((nbVote*this.getIndiceConfiance() + valeurVote)/(nbVote+1));
		this.setNbVoteConfiance(nbVote+1);

	}

	/** Effectue un vote confort
	 * @param valeurVote 
	 */
	public void voteConfort(int valeurVote){
		int nbVote=this.getNbVoteConfort();
		this.setIndiceConfort((nbVote*this.getIndiceConfort() + valeurVote)/(nbVote+1));
		this.setNbVoteConfort(nbVote+1);
	}
	
	/**
	 * @return met a jour la base avec l'indice de confort stocké dans l'objet
	 * @throws SQLException 
	 */
	public boolean updateConfort() throws SQLException{
		PreparedStatement updtConfort=Data.BDD_Connection.prepareStatement("UPDATE Utilisateur SET IndiceConfort=? AND NVoteConfort=? WHERE IdUtilisateur=?");
		updtConfort.setFloat(1, this.getIndiceConfort());
		updtConfort.setInt(2, this.getNbVoteConfort());
		updtConfort.setInt(3, this.getIdUser());
		if(updtConfort.executeUpdate() ==1){
			return true;
		}
		return false;
	}

	/**
	 * @return met a jour la base avec l'indice de confiance stocké dans l'objet
	 * @throws SQLException 
	 */
	public boolean updateConfiance() throws SQLException{
		PreparedStatement updtConfiance=Data.BDD_Connection.prepareStatement("UPDATE Utilisateur SET IndiceConfiance=? AND NVoteConfiance=? WHERE IdUtilisateur=?");
		updtConfiance.setFloat(1, this.getIndiceConfiance());
		updtConfiance.setInt(2, this.getNbVoteConfiance());
		updtConfiance.setInt(3, this.getIdUser());
		if(updtConfiance.executeUpdate() ==1){
			return true;
		}
		return false;
	}

	public boolean insererDansLaBase() throws SQLException{
		String sql = "insert into Utilisateur (Nom,Prenom,Mail,Pseudo,Mdp,Telephone) values(?,?,?,?,?,?)";
		PreparedStatement ps=Data.BDD_Connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, this.name);
		ps.setString(2, this.firstName);
		ps.setString(3, this.mail);
		ps.setString(4, this.pseudo);
		ps.setString(5, Password.encrypt(this.password));
		ps.setString(6, this.tel);
		if(ps.executeUpdate() ==1){
			ResultSet rs= ps.getGeneratedKeys();
			rs.next();
			this.idUser=rs.getInt(1);
			return true;
		}
		return false;

	}

	public int getIdPhotoProfil() throws SQLException{
		String sql = "SELECT IdImageProfil FROM Utilisateur where IdUtilisateur=?";
		PreparedStatement select = Data.BDD_Connection.prepareStatement(sql);
		select.setInt(1,this.getIdUser());
		ResultSet res = select.executeQuery();
		if(res.next() && res.getInt(1)!=0){
			return res.getInt(1);
		}
		else{
			return -1;
		}
	}

	public boolean setIdAvatar(int idImage) throws SQLException {
		String sql="UPDATE Utilisateur set IdImageProfil=? where IdUtilisateur=?";
		PreparedStatement update = Data.BDD_Connection.prepareStatement(sql);
		update.setInt(1, idImage);
		update.setInt(2, this.getIdUser());
		boolean result=false;
		if(update.executeUpdate()==1){
			result=true;
		}
		return result;
	}

	public static Utilisateur getUtilisateurByIdLogement(int idLogement) throws Exception{
		Utilisateur result = null;
		PreparedStatement ps=Data.BDD_Connection.prepareStatement("SELECT IdUtilisateur FROM Utilisateur WHERE IdLogement=?");
		ps.setInt(1, idLogement);
		ResultSet rs= ps.executeQuery();
		if (rs.next()){
			result = getUtilisateurById(rs.getInt(1));
		}
		else{
			throw new Exception("Id inexistant");
		}
		return result;
	}

	public boolean aUnLogement() throws SQLException {
		String sql="select count(IdLogement) from Utilisateur where IdUtilisateur = ?";
		PreparedStatement select = Data.BDD_Connection.prepareStatement(sql);
		select.setInt(1, this.idUser);
		ResultSet resultSelect = select.executeQuery();
		resultSelect.next();
		return resultSelect.getInt(1)!=0;
	}

	public int getIdLogement() {
		return idLogement;
	}

	public void setIdLogement(int theId){
		this.idLogement=theId;
	}

	public int getAvgConfort(){
		return Math.round(this.getIndiceConfort());
	}

	public int getAvgConfiance(){
		return Math.round(this.getIndiceConfiance());
	}
	
	public String getTel() {
		return (this.tel!=null ? this.tel :"Non renseigné");
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	private float getIndiceConfort() {
		return indiceConfort;
	}

	private void setIndiceConfort(float indiceConfort) {
		this.indiceConfort = indiceConfort;
	}

	private float getIndiceConfiance() {
		return indiceConfiance;
	}

	private void setIndiceConfiance(float indiceConfiance) {
		this.indiceConfiance = indiceConfiance;
	}

	private int getNbVoteConfort() {
		return nbVoteConfort;
	}

	private void setNbVoteConfort(int nbVoteConfort) {
		this.nbVoteConfort = nbVoteConfort;
	}

	private int getNbVoteConfiance() {
		return nbVoteConfiance;
	}

	private void setNbVoteConfiance(int nbVoteConfiance) {
		this.nbVoteConfiance = nbVoteConfiance;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getIdUser() {
		return idUser;
	}

	@Override
	public String toString() {
		return name + " " + firstName  ;
	}
}
