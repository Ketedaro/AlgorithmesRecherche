package sac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Arbre {
	private Arbre sousArbreGauche, sousArbreDroit;
	private List<Objet> listeObjets;
	private float borneSuperieure;

	public Arbre() {
		this.sousArbreGauche = null;
		this.sousArbreDroit = null;
		this.listeObjets = null;
		this.borneSuperieure = 0;
	}

	public Arbre(boolean useless) {
		this.sousArbreGauche = new Arbre();
		this.sousArbreDroit = new Arbre();
		this.listeObjets = new LinkedList<>();
		this.borneSuperieure = 0;
	}

	//Permet d'ajouter un objet dans la liste
	public float ajouter(Objet objet, float poidsMax, float borneInferieure, float borneSuperieure) {
		return ajouterBis(new LinkedList<Objet>(), objet, poidsMax, borneInferieure, borneSuperieure);
	}

	//On passe en parametre la liste en plus
	private float ajouterBis(List<Objet> listeObjetsPrecedente, Objet objet, float poidsMax, float borneInferieure,
			float borneSuperieure) {
		List<Objet> listePrecedente = new LinkedList<Objet>();
		listePrecedente.addAll(listeObjetsPrecedente);
		if (this.listeObjets == null) {
			return feuilleDevientNoeud(listePrecedente, objet, poidsMax, borneInferieure, borneSuperieure);
		} else {
			float valeurGauche = 0, valeurDroit = 0;
			if (objet != null)
				if ((!(this.getPoids() + objet.getPoids() > poidsMax))) {
					valeurGauche = sousArbreGauche.ajouterBis(this.listeObjets, objet, poidsMax, borneInferieure,
							borneSuperieure);
				}
			if (sousArbreDroit.listeObjets == null)
				valeurDroit = sousArbreDroit.ajouterBis(this.listeObjets, null, poidsMax, borneInferieure,
						borneSuperieure);
			else
				valeurDroit = sousArbreDroit.ajouterBis(this.listeObjets, objet, poidsMax, borneInferieure,
						borneSuperieure);
			if (valeurGauche > valeurDroit)
				return valeurGauche;
			else
				return valeurDroit;
		}
	}

	private float feuilleDevientNoeud(List<Objet> listePrecedente, Objet objet, float poidsMax, float borneInferieure,
			float borneSuperieure) {
		float valeur = borneSuperieure;
		for (Objet obj : listePrecedente) {
			valeur += obj.getValeur();
		}
		if (valeur >= borneInferieure) {
			if (objet != null) {
				if (objet.getPoids() <= poidsMax) {
					List<Objet> listePrec = new LinkedList<>();
					listePrec.addAll(listePrecedente);
					listePrec.add(objet);
					this.listeObjets = listePrec;
				} else
					this.listeObjets = listePrecedente;
			} else
				this.listeObjets = listePrecedente;
			this.sousArbreGauche = new Arbre();
			this.sousArbreDroit = new Arbre();
			this.borneSuperieure = getValeur() + borneSuperieure;
			this.borneSuperieure -= objet != null ? objet.getValeur() : 0;
			if (this.getValeur() > borneInferieure)
				return this.getValeur();
			return borneInferieure;
		}
		return borneInferieure;
	}

	//Retourne la solution optimale
	public List<Objet> solution(float borneInferieure) {
		List<Objet> liste = new ArrayList<Objet>(), listeGauche = null, listeDroit = null;

		if (this.listeObjets == null)
			return null;
		if (this.getValeur() == borneInferieure)
			return this.listeObjets;

		if (sousArbreGauche.borneSuperieure >= borneInferieure)
			listeGauche = sousArbreGauche.solution(borneInferieure);
		if (sousArbreDroit.borneSuperieure >= borneInferieure)
			listeDroit = sousArbreDroit.solution(borneInferieure);

		if (listeGauche == null && listeDroit == null)
			return null;
		else if (listeGauche != null && listeDroit == null)
			liste.addAll(listeGauche);
		else if (listeGauche == null && listeDroit != null)
			liste.addAll(listeDroit);
		else if (listeGauche != null && listeDroit != null) {
			float valeurGauche = 0, valeurDroit = 0, poidsGauche = 0, poidsDroit = 0;

			for (Objet objet : listeGauche) {
				valeurGauche += objet.getValeur();
				poidsGauche += objet.getPoids();
			}
			for (Objet objet : listeDroit) {
				valeurDroit += objet.getValeur();
				poidsDroit += objet.getPoids();

			}
			if (valeurGauche == borneInferieure && valeurDroit != borneInferieure)
				return listeGauche;
			else if (valeurDroit == borneInferieure && valeurGauche != borneInferieure)
				return listeDroit;
			else if (valeurGauche == valeurDroit && valeurGauche == borneInferieure) {
				if (poidsGauche > poidsDroit)
					return listeDroit;
				else
					return listeGauche;
			}
		}

		float valeur = 0;
		for (Objet objet : liste)
			valeur += objet.getValeur();
		if (valeur == borneInferieure)
			return liste;
		else
			return null;
	}

	private float getPoids() {
		float poids = 0;
		for (Objet objet : listeObjets)
			poids += objet.getPoids();
		return poids;
	}

	public float getValeur() {
		float valeur = 0;
		for (Objet objet : listeObjets)
			valeur += objet.getValeur();
		return valeur;
	}



}
