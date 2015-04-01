package vue;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.Menu;
import formulaire.FormulaireInscription;

/**
 * Servlet implementation class incription
 */
@WebServlet("/incription")
public class Inscription extends LaBifleDuMoyenAgeANosJours {
	private static final long serialVersionUID = 1L;

	public Inscription() {
		super();
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		this.request.setAttribute("menu", super.getMenuInscription());
		this.getServletContext().getRequestDispatcher("/WEB-INF/inscription.jsp").forward(request, response);
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		this.request.setAttribute("menu", super.getMenuInscription());
		try{
			String nom=this.request.getParameter("nom");
			String prenom=this.request.getParameter("prenom");
			String mail=this.request.getParameter("mail");
			String mdp=this.request.getParameter("mdp");
			String confirmMdp=this.request.getParameter("mdpC");
			String pseudo = this.request.getParameter("pseudo");
			String tel = this.request.getParameter("tel");
			FormulaireInscription form=new FormulaireInscription(nom,prenom,mail,mdp,confirmMdp,pseudo,tel);
			String resultatInscription=form.procedureInscription();
			List<String> infosRetenus = form.getRetourInfos();
			this.request.setAttribute("infosRetenus", infosRetenus);
			this.request.setAttribute("resultat", resultatInscription);
			this.getServletContext().getRequestDispatcher("/WEB-INF/inscription.jsp").forward(this.request, this.response);
			
		} catch(Exception e){
			this.request.setAttribute("errorMessage",e.getMessage());
			this.response.sendRedirect("erreur");
			return ;

		}
	}

}
