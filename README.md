# listout
"Tout pour faire des Listes."
site de liste en java (11) / spark / freemarker / h2 / sql2o sur intelliJ IDEA

</br>
<img src="/src/main/ressources/image/todoliste.png" alt="My cool logo"/>
</br>

(mais pas fini)

Licence publique générale GNU (GPL3)

<pre>

Action : -recherche en rapport au titre ou a la description de n'importe quel élément
	 -afficher toutes les listes
	 -afficher la liste ou l'élément selectionné (clique sur la ligne du tableau)
	 -création de liste avec un titre et une decription
	 -ajout d'element avec un titre et une decription un etat et une liste de tag (qui peuvent a leur tour devenir des listes)
	 -modification liste et élement (titre et description)
	 -voir l'accueil
	 -voir la page d'information/contact
	 -connection (pas mis en place)

src/
main/
	java/
		autre/
			log4jConf.java : log configuration du serveur spark
		controleur/
			MainController.java : Gère l'ensemble des routes du serveur web et effectuer les actions necessaire en consequence (a simplifier avec Classe intermediaire pour classer les différentes actions)
		DAO/
			UnSql2oModele.java : Ensemble des requetes sql de création de table et d'insertion/suppression/ajout d'élement dans ces tables
		main/
			Main.java : Initialistation de la base de donnée (h2/Sql2oCréation), des tables, et des éléments testes 
		model/
			AListe.java : Element mere du patron de conception composite
			LaListe.java : Element Fille "Liste" du patron de conception composite
			Tag.java : tag est un element string (qui pourra se complexifier)
			UnElement.java :  Element Fille "atomique" du patron de conception composite, possedant une liste de tag


	ressources/ : ensemble des éléments partagés
		css/
			style.css : ensemble du style de toutes les pages du sites (a fractionner)
		image/ 
			abr.png : logo du site
			connexion.png : logo de connexion
			todoliste.png : image d'acceuil
		templates/
			accueil.ftl : page d'acceuil
			ajoutlist.ftl : page d'ajout/modification de Liste/Element
			connexion.ftl : page de connexion (non mise en place)
			footer.ftl : footer du site integre dans chaque page avec un #include
			header.ftl : header du site integre dans chaque page avec un #include
			info.ftl : page d'information / contact (mails,...)
			listes.ftl : page de recherche, d'affichage des listes/elements 

		test/ : test graphique
			tagsinput.css : code css de boostrap  pour les listes de tag
			tagsinput.min.js : code javascript de boostrap  pour les listes de tag

Base de donnée

        TABLE ELEMENT -> ensemble des element liste ou "feuille" de liste
                    id INTEGER not NULL -> identifiant de l'element (généré par aléatoirement)
                    idListe INTEGER not NULL -> inutile (cas d'un seul element pere)
                    dateCreation DATE -> Date de création de l'element
                    dateDerModif DATE -> Derniere date modification de l'element
                    titre VARCHAR(255) -> (taille arbitraire)
                    description VARCHAR(255) -> description en elle meme (taille arbitraire)
                    PRIMARY KEY ( id ) -> id unique

	TABLE POSSEDE : table permettant de créer des relation de hierarchie entre les elements
                    id INTEGER not NULL -> identifiant d'un ELEMENT FILS
                    idListe INTEGER not NULL -> identifiant d'un ELEMENT PERE
                    PRIMARY KEY ( id ) -> 
                    FOREIGN KEY ( idListe ) REFERENCES ELEMENT ( id ) -> en reference a ELEMENT.id
                    FOREIGN KEY ( id ) REFERENCES ELEMENT ( id ) -> en reference a ELEMENT.id

	TABLE TAG : table permettant d'établir une liste de tag pour un element donné
                    id INTEGER not NULL -> identifiant d'un ELEMENT
                    tag VARCHAR(255) -> tag en lui meme (taille arbitraire)
                    FOREIGN KEY ( id ) REFERENCES ELEMENT ( id ) -> en reference a ELEMENT.id



Injection dans les formulaire non géré; Element dans une autre Liste non géré; connexion non géré; ceratin visuel a revoir</pre>
