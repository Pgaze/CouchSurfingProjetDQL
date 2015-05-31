package vue;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Data;
import modele.Offre;
import modele.Postule;

/**
 * Servlet implementation class PageValidation
 */
//@WebServlet("/pageValidation")
public class PageValidation extends SuperServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PageValidation() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		super.afficherMenu();
		this.request.setAttribute("postule", this.request.getSession().getAttribute("postule"));
		this.request.setAttribute("offre", this.request.getSession().getAttribute("offrePostulee"));
		this.getServletContext().getRequestDispatcher("/WEB-INF/pageValidation.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.initAttribut(request, response);
		super.afficherMenu();
		try {
			Postule postule= (Postule)this.request.getSession().getAttribute("postule");
			String dateDebut = this.request.getParameter("dateDebut");
			String dateFin = this.request.getParameter("dateFin");
			if (!dateDebut.equals("") && !dateFin.equals("")){
				if(this.checkDatePostuleInDateReservation()){
					postule.setDateDebut(dateDebut);
					postule.setDateFin(dateFin);	
					if(postule.postulerAUneOffre()){
						Data.BDD_Connection.commit();
						this.response.sendRedirect("demandes");
						return ;
					}
				}
				else{
					this.request.setAttribute("postule", this.request.getSession().getAttribute("postule"));
					this.request.setAttribute("offre", this.request.getSession().getAttribute("offrePostulee"));
					this.request.setAttribute("erreur", "Cette plage de date ne corespond pas a cette offre");
					this.request.getRequestDispatcher("/WEB-INF/pageValidation.jsp").forward(this.request, this.response);
				}

			}
		} catch (SQLException e) {
			super.afficherPageErreur(e.getMessage());
		}

	}

	private boolean checkDatePostuleInDateReservation() {
		Date dateDebutSaisi = Date.valueOf(this.request.getParameter("dateDebut"));
		Date dateFinSaisi = Date.valueOf(this.request.getParameter("dateFin"));
		Offre offrePostulee = (Offre)this.request.getSession().getAttribute("offrePostulee");
		boolean result = offrePostulee.getDateDebut().compareTo(dateDebutSaisi) <=0;
		result = result && offrePostulee.getDateFin().compareTo(dateFinSaisi)>=0;
		return result;
	}

}
