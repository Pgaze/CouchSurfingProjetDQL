package vue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Offre;
import modele.Postule;
import modele.Utilisateur;
import formulaire.FormulaireRechercheAnnonce;

/**
 * Servlet implementation class Recherche
 */
@WebServlet("/Recherche")
public class Recherche extends SuperServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Recherche() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		super.afficherMenu();
		this.getServletContext().getRequestDispatcher("/WEB-INF/recherche.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		super.afficherMenu();
		try{
			//Cas du bouton recherche
			if (request.getParameter("btCherche")!=null){
				FormulaireRechercheAnnonce form= 
						new FormulaireRechercheAnnonce(this.request.getParameter("ville"),
								this.request.getParameter("dateDebut"),this.request.getParameter("dateFin"));
				List<Offre> lesOffres=form.getListeOffre();
				this.request.setAttribute("lesOffres", lesOffres);
				this.getServletContext().getRequestDispatcher("/WEB-INF/recherche.jsp").forward(this.request, this.response);


			}
			//Appui sur un autre bouton
			else{
				Offre offrePostulee;
				offrePostulee = Offre.getOffreByIdLogement(getBoutonClique());
				Utilisateur user= super.getUtilisateurInSession();
				Postule postule = new Postule(user, offrePostulee.getHebergeur(), offrePostulee.getLogement());
				if(!postule.existInBase()){
					this.request.getSession().setAttribute("offrePostulee", offrePostulee);
					this.request.getSession().setAttribute("postule", postule);
					this.response.sendRedirect("pageValidation");
					return ;
				}
				else{
					this.request.setAttribute("erreur", "Vous avez deja postule à cette offre");
					this.getServletContext().getRequestDispatcher("/WEB-INF/recherche.jsp").forward(this.request, this.response);
				}
			}
		}
		catch (Exception e){
			this.request.setAttribute("erreur", e.getMessage());
			this.getServletContext().getRequestDispatcher("/WEB-INF/recherche.jsp").forward(this.request, this.response);
		}
	}

	private int getBoutonClique(){
		Map<String,String[]> mapParameter = this.request.getParameterMap();
		for (Map.Entry<String, String[]> entry : mapParameter.entrySet()){
			if(entry.getValue()[0].contentEquals("Postuler")){
				return Integer.parseInt(entry.getKey());
			}
		}
		return -1;
	}
}
