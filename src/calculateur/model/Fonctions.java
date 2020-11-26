package calculateur.model;

import java.io.Serializable;

/** Étienne Beaulieu et Zacharie Forest */
public class Fonctions implements Serializable
{

	private static final long serialVersionUID = 1682111380115922536L;
	private String fonction;
	private String nom;

	/**
	 * Constructeur permettant d'instancier un objet Fonctions contenant sa
	 * fonction et son nom
	 * 
	 * @param fonction
	 * @param nomFonction
	 */
	public Fonctions(String fonction, String nomFonction)
	{
		this.fonction = fonction;
		nom = nomFonction;
	}

	/**
	 * Méthode qui permet de valider si la fonction est saisie correctement en essayant de l'évaluer
	 * @param fonction
	 * @return Retourne true si la fonction est valide false sinon
	 */
	public static boolean validerFonction(String fonction)
	{
		Boolean estValide = true;
		if (Calculatrice.calculeFonction(fonction, 2).contains("NaN")
				&& Calculatrice.calculeFonction(fonction, 2, 3).contains("NaN"))
		{
			estValide = false;
		}
		return estValide;
	}

	public String getName()
	{
		return nom;
	}

	public String getFonction()
	{
		return fonction;
	}

	public String toString()
	{
		return fonction;
	}
}
