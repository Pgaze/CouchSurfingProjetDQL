package vue.image;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Image;
import modele.Postule;
@WebServlet("/ServletImageProfilPageValidation")
public class ServletImageProfilPageValidation extends ServletImage {

	public ServletImageProfilPageValidation() {
		super();
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.initAttribut(req, resp);
		Postule p = (Postule)this.request.getSession().getAttribute("postule");
		try {
			int id= p.getHebergeur().getIdPhotoProfil();
			Image image;
			if(id==-1){
				image = Image.getImageById(1);
			}
			else{
				image = Image.getImageById(id);
			}
			byte[] img = Files.readAllBytes(image.getImage().toPath());
			response.setContentType("image/png");
			response.setContentLength(img.length);
			response.getOutputStream().write(img);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
