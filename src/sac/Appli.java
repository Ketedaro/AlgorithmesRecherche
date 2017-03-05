package sac;

import java.util.Scanner;

public class Appli {
	public static void main(String[] args) {
		System.out.println("Commande : resoudre-sac-a-dos cheminFichier poidsDuSac methodeResolution");
		Scanner scanner = new Scanner(System.in);
		boolean temp = false;
		while (!scanner.next().equals("resoudre-sac-a-dos")) {
			System.out.println("Veuillez bien ecrire la commande");
		}
		String cheminFichier = scanner.next();
		Float poidsMax = new Float(0);
		while (!temp) {
			try {
				poidsMax = Float.parseFloat(scanner.next());
				temp = true;
			} catch (NumberFormatException e) {
				System.out.println("Veuillez entrer une valeur correcte");
			}
		}

		SacADos sac = new SacADos(cheminFichier, poidsMax);
		String methode = "";
		temp = false;
		while (!temp) {
			try {
				methode = scanner.next().toLowerCase();
				switch (methode) {
				case "gloutonne": {
					sac.resoudreGloutonne();
					temp = true;
					break;
				}
				case "dynamique": {
					sac.resoudreDynamique();
					temp = true;
					break;
				}
				case "pse": {
					sac.resoudrePSE();
					temp = true;
					break;
				}
				default:
					throw new RuntimeException("Veuillez entrer 'gloutonne', 'dynamique' ou 'pse'");
				}
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}

		System.out.println("Methode utilisee " + methode + " : ");
		System.out.println(sac + "\n");
	}

}
