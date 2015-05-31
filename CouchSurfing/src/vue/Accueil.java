package vue;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modele.Utilisateur;
import formulaire.FormulaireConnexion;

/**
 * Servlet implementation class Accueil
 */
@WebServlet("/Accueil")
public class Accueil extends SuperServlet {
	private static final long serialVersionUID = 1L;


	public Accueil() {
		super();
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		if(super.getMailInCookie(request)!= null){
			try{
				Utilisateur user = Utilisateur.getUtilisateurParMail(super.getMailInCookie(this.request));
				this.request.getSession().setAttribute("sessionUtilisateur", user);
				this.response.sendRedirect( "recherche" );
				return;	
			}
			catch(SQLException e){
				this.request.setAttribute("errorMessage",e.getMessage());
				this.getServletContext().getRequestDispatcher("/WEB-INF/erreur.jsp").forward(this.request, this.response);
			}
		}
		if (super.getUtilisateurInSession() == null ) {
			this.afficherMenu();
			this.getServletContext().getRequestDispatcher("/WEB-INF/accueil.jsp").forward(this.request, this.response);
		}else {
			this.response.sendRedirect( "recherche" );
			return;	
		}
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request,response);
		super.afficherMenu();
		HttpSession sessionUtilisateur = this.request.getSession();
		try{
			String logA = this.request.getParameter("login");
			String mdpA = this.request.getParameter("mdp");
			FormulaireConnexion form =new FormulaireConnexion(logA,mdpA);

			if (form.verificationCoupleMailMotDePasse()){
				Utilisateur user= Utilisateur.getUtilisateurParMail(logA);
				sessionUtilisateur.setAttribute("sessionUtilisateur", user);
				this.response.addCookie(new Cookie("cookieUtilisateur", logA));
				this.response.sendRedirect( "recherche" );
				return;				

			} else {
				sessionUtilisateur.setAttribute("sessionUtilisateur", null);
				this.request.setAttribute("resultat","Echec authentification" );
			}
			this.getServletContext().getRequestDispatcher("/WEB-INF/accueil.jsp").forward(this.request, this.response);
		}
		catch(Exception e){
			this.request.setAttribute("errorMessage",e.getMessage());
			this.getServletContext().getRequestDispatcher("/WEB-INF/erreur.jsp").forward(this.request, this.response);
		}
	}

	
}


