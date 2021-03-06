
<!DOCTYPE html>
<html>
	<%@ include file="entete.jsp" %>
	<%@ include file="menu.jsp" %>
    <body style="background-image:url(${pageContext.request.contextPath}/ServletBackground)">
        <div id="container">	        	
			<div class="contentAccueil" id="accueilBlock1" style="background-image:url(${pageContext.request.contextPath}/ServletImageAcceuil)">
			</div>      	
			<div class="contentAccueil" id="presentation">
				<p class="text_intro"> CouchSurfing est un service qui relie les membres d'une communaut� fran�aise de voyageurs.</p>
				<p class="text_intro"> Utilisez CouchSurfing pour trouver un endroit o� rester et faire partager votre maison et votre ville natale avec les voyageurs.</p>
				<p class="text_intro"> Les CouchSurfeurs organisent r�guli�rement des �v�nements dans 2 000 villes � travers la France. Il y a toujours quelque chose � d�couvrir, de nouveaux amis � rencontrer.</p>
				<p class="text_intro"> Venez vite les rencontrer ! </p>
			</div>      	
			<div class="contentAccueil" id="connexion">
				<div id="formConnexion">
					<form action="accueil" method="post" id="formConnexion">
						<input required type="text" name="login" placeholder="Votre mail"/>
						<input required type="password" name="mdp" placeholder="Votre mot de passe"/>
						<input required type="Submit" name="valLogin"/>
					</form>
					 Vous n'avez pas de compte ? <a href="inscription">Inscrivez vous !</a>
				</div>
				${ resultat }
			</div>
        </div>
    </body>
    
	<%@ include file="basdepage.jsp" %>
</html>
