package modele;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Image {

	private File image;
	private int idImage;
	private String nom;

	public Image(File image, String nom) {
		this.image = image;
		this.nom = nom;
	}

	public Image(){
	}

	public boolean insererDansLaBase() throws Exception{
		String sql= "INSERT INTO Image (Nom,Image) VALUES (?,?)";
		boolean result=false;
		PreparedStatement insert = Data.BDD_Connection.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
		insert.setString(1, this.nom);
		FileInputStream inputStream= new FileInputStream(this.image);
		insert.setBinaryStream(2, inputStream);
		result = insert.executeUpdate()==1;
		ResultSet rs= insert.getGeneratedKeys();
		if(rs.next()){
			System.out.println(rs.getInt(1));
			this.idImage=rs.getInt(1);
		}
		return result;
	}

	public static Image getImageById(int idImage) throws SQLException, IOException{
		Image result=new Image();
		String sql= "SELECT Nom,Image FROM Image WHERE idImage=?";
		PreparedStatement select= Data.BDD_Connection.prepareStatement(sql);
		select.setInt(1, idImage);
		ResultSet res= select.executeQuery();
		if(res.next()){
			result.setNom(res.getString(1));
			result.setIdImage(idImage);
			InputStream is =  res.getBinaryStream(2);
			File f = new File(result.getNom());
			FileOutputStream os = new FileOutputStream(f);
			byte[] bytes = new byte[1024];
			while(is.read(bytes)!=-1){
				os.write(bytes);
			}
			result.setImage(f);
			os.close();
		}

		return result;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public int getIdImage() {
		return idImage;
	}

	public void setIdImage(int idImage) {
		this.idImage = idImage;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
