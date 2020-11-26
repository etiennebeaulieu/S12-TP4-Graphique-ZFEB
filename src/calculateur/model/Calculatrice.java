package calculateur.model;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import javafx.beans.property.StringProperty;

/** Étienne Beaulieu et Zacharie Forest */

public class Calculatrice
{

	/**
	 * Méthode qui transforme les symboles particuliers en opérateur pour
	 * ensuite calculer la valeur de l'équation reçu en paramètre.
	 * 
	 * @param calculProperty: Équation à calculer
	 * @return Résultat du calcul reçu
	 */
	public static String calculeEquation(StringProperty calculProperty)
	{

		String calcul = calculProperty.get();
		calcul = calcul.replace('÷', '/');
		calcul = calcul.replace('×', '*');
		calcul = calcul.replace(',', '.');

		return (new Expression(calcul).calculate() + "").replace('.', ',');
	}

	/**
	 * Méthode permettant de calculer le résultat de l'évaluation d'une fonction
	 * reçue en paramètre
	 * 
	 * @param fonction: Fonction sous forme de String à calculer
	 * @param valeur1: Valeur à remplacer dans la fonction pour l'évaluer
	 * @return Résultat de l'évaluation de la fonction
	 */
	public static String calculeFonction(String fonction, int valeur1)
	{
		fonction = fonction.replace('÷', '/');
		fonction = fonction.replace('×', '*');
		Function f = new Function(fonction);
		return (fonction.charAt(0) + "(" + valeur1 + ") = "
				+ f.calculate(valeur1)).replace('.', ',');
	}

	/**
	 * Méthode permettant de calculer le résultat de l'évaluation d'une fonction
	 * reçue en paramètre qui posède 2 inconnus
	 * 
	 * @param fonction: Fonction sous forme de String à calculer
	 * @param valeur1: Valeur à remplacer dans la fonction pour
	 *            l'évaluer(remplace le premier inconnu)
	 * @param valeur2: Valeur à remplacer dans la fonction pour
	 *            l'évaluer(remplace le deuxième inconnu)
	 * @return Résultat de l'évaluation de la fonction
	 */
	public static String calculeFonction(String fonction, int valeur1,
			int valeur2)
	{
		fonction = fonction.replace('÷', '/');
		fonction = fonction.replace('×', '*');
		Function f = new Function(fonction);
		return (fonction.charAt(0) + "(" + valeur1 + ", " + valeur2 + ") = "
				+ f.calculate(valeur1, valeur2)).replace('.', ',');
	}

}
