package vue.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vue.SuperServlet;

public class ServletImage extends SuperServlet {
	
	protected String name;
	protected String type;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.initAttribut(req, resp);
		this.response.setContentType("text/html;charset=UTF-8");
	    File image = new File(getServletContext().getRealPath("/images")
	            + File.separator + this.name + "."+this.type);
		byte[] img = Files.readAllBytes(image.toPath());
		this.response.setContentType("images/"+this.type);
		this.response.setContentLength(img.length);
		this.response.getOutputStream().write(img);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}
	
	

}
