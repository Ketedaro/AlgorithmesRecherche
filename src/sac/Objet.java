package sac;

public class Objet {
	private String nom;
	private float poids;
	private float valeur;
	
	public Objet(String nom, float poids, float valeur) {
		super();
		this.nom = nom;
		this.poids = poids;
		this.valeur = valeur;
	}

	public String toString() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getPoids() {
		return poids;
	}

	public void setPoids(float poids) {
		this.poids = poids;
	}

	public float getValeur() {
		return valeur;
	}

	public void setValeur(float valeur) {
		this.valeur = valeur;
	}
	

}
