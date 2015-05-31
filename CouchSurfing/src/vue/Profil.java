package vue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Critere;
import modele.Data;
import modele.Image;
import modele.Logement;
import modele.Utilisateur;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class Profil
 */
@WebServlet("/Profil")
public class Profil extends SuperServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Profil() {
		super();
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		super.afficherMenu();
		Utilisateur user = super.getUtilisateurInSession();
		this.request.setAttribute("utilisateurProfil", user);
		try {
			if(user.getIdLogement() != 0){
				afficherLogementUser( user);
			}
			this.getServletContext().getRequestDispatcher("/WEB-INF/profil.jsp")
			.forward(this.request, this.response);

		} catch (Exception e) {
			e.printStackTrace();
			this.request.setAttribute("errorMessage",e.getMessage());
			this.getServletContext().getRequestDispatcher("/WEB-INF/erreur.jsp").forward(this.request, this.response);
		}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		super.afficherMenu();
		Utilisateur user = super.getUtilisateurInSession();
		this.request.setAttribute("utilisateurProfil", user);
		try {
			this.afficherLogementUser(user);
			Image imageUploaded = getFileFromRequest();
			if(imageUploaded!=null){
				boolean test = imageUploaded.insererDansLaBase();
				System.out.println("imageInseré:"+test);
				boolean test2 = user.setIdAvatar(imageUploaded.getIdImage());
				System.out.println("userUpdated:"+test2);
				Data.BDD_Connection.commit();
				this.getServletContext().getRequestDispatcher("/WEB-INF/profil.jsp")
				.forward(this.request, this.response);

			}
		} catch (Exception e) {
			this.request.setAttribute("errorMessage",e.getMessage());
			this.getServletContext().getRequestDispatcher("/WEB-INF/erreur.jsp").forward(this.request, this.response);

		}

	}



	private Image getFileFromRequest() {
		Image result = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = this.getServletConfig()
				.getServletContext();
		File repository = (File) servletContext
				.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(this.request);
			if (items != null && items.size() > 0) {
				// iterates over form's fields
				for (FileItem item : items) {
					// processes only fields that are not form fields
					if (!item.isFormField()) {
						String filename = item.getName();
						File file= new File(filename);
						item.write(file);
						result = new Image(file, filename);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	protected void afficherLogementUser(Utilisateur user) throws Exception {
		Logement logementUtilisateur = Logement.getLogementById(user.getIdLogement());
		if (logementUtilisateur != null) {
			this.setCritereOnRequest(logementUtilisateur);
			this.request.setAttribute("logementUtilisateur", logementUtilisateur);
		} else {
			this.request.setAttribute(
					"logementUtilisateur",
					"<p>Vous n'avez pas de logement enregistr�. <a href='nouvelle'>Cr�ez en un !</a></p>");
		}
	}
	
	private void setCritereOnRequest(Logement l){
		for (Critere c : l.getLesCriteres()){
			this.request.setAttribute(c.getNomCritere(), c.getDescription());
		}
	}
}
