
<!DOCTYPE html>
<html>
<%@ include file="entete.jsp"%>
<%@ include file="menu.jsp"%>
<body
	style="background-image:url(${pageContext.request.contextPath}/ServletBackground)">

	<div id="container" class="content-accueil">
		<h1 id="titre_Profil">Profil de ${utilisateurProfil.firstName} ${ utilisateurProfil.name }</h1>
		<div id="profile">
			<div id="info">
				<div class="infoSession">
					<div id="infoSession_rigthContainer">
						<div id="container_img_profil">
							<div id="img_profil">
								<form method="post" enctype="multipart/form-data">
									<label for="bt_img_profil"> <img id="img"
										src="${pageContext.request.contextPath}/ServletImageProfil" />
									</label> <input type="file" name="imgProfil" id="bt_img_profil"
										onchange="this.form.submit()">
								</form>
							</div>
						</div>
						<div id="containerNoteHerbergeur">
							<p id="etoile_note_hebergeur" class="icon-star noteHebergeur"></p>
							<p id="note_hebergeur" class="noteHebergeur">13/20</p>
						</div>
						<p id="nbr_avis_hebergeur">( 200 avis )</p>
					</div>
					<div id="infoSession_leftContainer">
						<div class="infoSessionContainer">
							<p class="intitule_profil">Nom</p>
							<p class="info_profil">${utilisateurProfil.name}</p>
						</div>
						<div class="infoSessionContainer">
							<p class="intitule_profil">Prenom</p>
							<p class="info_profil">${ utilisateurProfil.firstName }</p>
						</div>
						<div class="infoSessionContainer">
							<p class="intitule_profil">Email</p>
							<p class="info_profil">${ utilisateurProfil.mail }</p>
						</div>
						<div class="infoSessionContainer">
							<p class="intitule_profil">Pseudo</p>
							<p class="info_profil">${ utilisateurProfil.pseudo }</p>
						</div>
						<div class="infoSessionContainer">
							<p class="intitule_profil">Tel</p>
							<p class="info_profil">${utilisateurProfil.tel }</p>
						</div>
					</div>


				</div>

				<div class="infoSession">
					<div class="infoSessionContainer">

						<div id="serviceAdresse" class="serviceDiv">
							<p class="intitule_profil_service">Adresse</p>
							<p class="info_profil_service">${ logementUtilisateur.adresse }</p>
						</div>
						<p id="intituleService">Service � proximit�</p>
						<div id="serviceContainer">
						<c:forEach items="${logementUtilisateur.lesCriteres }" var="unCritere">
								<div id="serviceCommerce" class="serviceDiv">
									<p class="${unCritere.icone } intitule_profil_service">${unCritere.titreCritere}</p>
									<p class="info_profil_service">${unCritere.description }</p>
								</div>
							</c:forEach>
							</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<%@ include file="basdepage.jsp"%>
</body>
</html>
