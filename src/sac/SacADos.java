package sac;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SacADos {
	private float poidsMax;
	private List<Objet> objetsDansSac;
	private List<Objet> objetsPossibles;

	public SacADos() {
		this("", 0);
	}

	public SacADos(String cheminFichier, float poidsMax) {
		this.poidsMax = poidsMax;
		this.objetsDansSac = new LinkedList<Objet>(); // LinkedList car adaptee
														// aux listes dynamiques
		this.objetsPossibles = new ArrayList<Objet>(); // ArrayList pour listes
														// statiques
		this.remplir(cheminFichier);
	}

	// Remplit la liste des objets possibles du fichier passe en parametre
	private void remplir(String cheminFichier) {
		FileInputStream fichier;
		try {
			fichier = new FileInputStream(cheminFichier);
			Scanner scanner = new Scanner(fichier);
			while (scanner.hasNextLine()) {
				String[] donnees = scanner.nextLine().split(" ; ");
				if (donnees.length == 3) {
					int i = 0;
					this.objetsPossibles.add(
							new Objet(donnees[i++], Float.parseFloat(donnees[i++]), Float.parseFloat(donnees[i++])));
				}
			}
			System.out.println(this.objetsPossibles);
		} catch (FileNotFoundException e) {
			System.out.println("Impossible de trouver le fichier");
			System.exit(1);
		}
	}

	// Resolution avec la methode gloutonne
	public void resoudreGloutonne() {
		List<Objet> listeTriee = new ArrayList<Objet>();
		listeTriee.addAll(this.objetsPossibles);
		triRapide(listeTriee);
		for (int i = 0; i < this.objetsPossibles.size() && this.getPoidsActuel() < this.poidsMax; i++)
			if (this.poidsMax >= this.getPoidsActuel() + listeTriee.get(i).getPoids())
				this.objetsDansSac.add(listeTriee.get(i));

	}

	// Tri la liste des objets passee en parametre
	private void triRapide(List<Objet> liste) {
		triRapideRecursif(liste, 0, liste.size() - 1);
	}

	// Tri rapide recursif
	private void triRapideRecursif(List<Objet> liste, int premier, int dernier) {
		if (premier < dernier) {
			int pivot = (premier + dernier) / 2;
			pivot = repartition(liste, premier, dernier, pivot);
			triRapideRecursif(liste, premier, pivot - 1);
			triRapideRecursif(liste, pivot + 1, dernier);
		}

	}

	// Tri par repartition
	private int repartition(List<Objet> liste, int premier, int dernier, int pivot) {
		permuter(liste, dernier, pivot);
		int temp = premier;
		for (int i = premier; i < dernier; ++i) {
			if (liste.get(i).getPoids() / liste.get(i).getValeur() < liste.get(dernier).getPoids()
					/ liste.get(dernier).getValeur()) {
				permuter(liste, i, temp);
				++temp;
			}
		}
		permuter(liste, dernier, temp);
		return temp;
	}

	// Permute deux elements de la liste passee en parametre
	private void permuter(List<Objet> liste, int i, int temp) {
		Objet objet = liste.get(temp);
		liste.set(temp, liste.get(i));
		liste.set(i, objet);
	}

	// Resolution avec la methode dynamique
	public float resoudreDynamique() {
		this.objetsDansSac.clear();
		int rapport = rapportEntier();
		float matrice[][] = new float[this.objetsPossibles.size()][(int) (this.poidsMax * rapport) + 1];
		for (int i = 0; i < matrice.length; ++i)
			Arrays.fill(matrice[i], 0);

		for (int colonne = 0; colonne < matrice[0].length; ++colonne)
			if (this.objetsPossibles.get(0).getPoids() * rapport > colonne)
				matrice[0][colonne] = 0;
			else
				matrice[0][colonne] = this.objetsPossibles.get(0).getValeur();

		for (int numObj = 1; numObj < matrice.length; ++numObj)
			for (int colonnePoids = 0; colonnePoids < matrice[0].length; ++colonnePoids)
				if (this.objetsPossibles.get(numObj).getPoids() * rapport > colonnePoids)
					matrice[numObj][colonnePoids] = matrice[numObj - 1][colonnePoids];
				else
					matrice[numObj][colonnePoids] = Math.max(matrice[numObj - 1][colonnePoids],
							matrice[numObj - 1][colonnePoids
									- (int) (this.objetsPossibles.get(numObj).getPoids() * rapport)]
									+ this.objetsPossibles.get(numObj).getValeur());

		int numObjet = matrice.length - 1;
		int colObjet = matrice[0].length - 1;
		while (matrice[matrice.length - 1][matrice[0].length - 1] == matrice[matrice.length - 1][colObjet - 1]) {
			--colObjet;
		}
		while (colObjet > 0 && numObjet >= 0) {
			while (numObjet > 0 && matrice[numObjet][colObjet] == matrice[numObjet - 1][colObjet]) {
				--numObjet;
			}
			colObjet -= this.objetsPossibles.get(numObjet).getPoids() * rapport;
			if (colObjet >= 0)
				this.objetsDansSac.add(this.objetsPossibles.get(numObjet));
			--numObjet;
		}
		return this.getValeurActuelle();
	}

	// Retourne un multiple de dix tel que la plus petite valeur multipliee par
	// cette valeur soit un entier
	private int rapportEntier() {
		int rapport = 1;
		boolean temp = false;
		for (Objet objet : this.objetsPossibles) {
			do {
				if ((objet.getPoids() * rapport) % 1 != 0) {
					rapport *= 10;
					temp = true;
				} else
					temp = false;
			} while (temp);
		}
		return rapport;
	}

	// Retourne le poids actuel du sac
	private float getPoidsActuel() {
		float poidsActuel = 0;
		for (Objet objet : this.objetsDansSac)
			poidsActuel += objet.getPoids();
		return poidsActuel;
	}

	// Retourne la valeur actuelle du sac
	private float getValeurActuelle() {
		return this.valeurObjets(this.objetsDansSac);
	}

	// Resolution avec la methode PSE
	public void resoudrePSE() {
		this.objetsDansSac.clear();
		float borneInferieure = resoudreDynamique();
		this.objetsDansSac.clear();
		Arbre arbre = new Arbre(true);
		List<Objet> objets = new LinkedList<Objet>();
		objets.addAll(this.objetsPossibles);
		triRapide(objets);
		while (!objets.isEmpty()) {
			borneInferieure = arbre.ajouter(objets.get(0), this.poidsMax, borneInferieure, this.valeurObjets(objets));
			objets.remove(0);
		}
		this.objetsDansSac.addAll(arbre.solution(borneInferieure));
	}

	// Retourne la valeur totale des objets de la liste passee en parametre
	private float valeurObjets(List<Objet> liste) {
		float valeur = 0;
		for (Objet objet : liste)
			valeur += objet.getValeur();
		return valeur;
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Poids : " + getPoidsActuel() + "/" + this.poidsMax + System.getProperty("line.separator")
				+ "Valeur : " + getValeurActuelle() + System.getProperty("line.separator") + this.objetsDansSac
				+ System.getProperty("line.separator"));
		return stringBuilder.toString();
	}

}
