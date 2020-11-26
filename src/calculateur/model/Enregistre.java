package calculateur.model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Étienne Beaulieu et Zacharie Forest */
public class Enregistre implements Serializable
{

	private static final long serialVersionUID = 3181866755332880002L;
	private String a;
	private String b;
	private String c;
	private String d;
	private String e;
	private String f;
	private String f1;
	private String f2;
	private String f3;
	private String f4;
	private transient ListProperty<Fonctions> liste;
	private ArrayList<Fonctions> array;

	public ListProperty<Fonctions> getListe()
	{
		return liste;
	}

	/**
	 * Constructeur qui permet d'instancier les variables à enregistrer
	 */
	public Enregistre()
	{
		a = "";
		b = "";
		c = "";
		d = "";
		e = "";
		f = "";
		f1 = "";
		f2 = "";
		f3 = "";
		f4 = "";
		ObservableList<Fonctions> observableList = FXCollections
				.observableArrayList();
		liste = new SimpleListProperty<Fonctions>(observableList);
		array = new ArrayList<Fonctions>();
	}

	/**
	 * Méthode permettant de convertir la ListProperty en Arraylist
	 */
	public void convertirListe()
	{
		array.clear();
		array.addAll(liste);
	}

	/**
	 * Méthode permettant de convertir l'ArrayList en ListProperty
	 */
	public void convertirArray()
	{
		ObservableList<Fonctions> observableList = FXCollections
				.observableArrayList();
		liste = new SimpleListProperty<Fonctions>(observableList);
		liste.setAll(array);
	}

	public String getA()
	{
		return a;
	}

	public void setA(String a)
	{
		this.a = a;
	}

	public String getB()
	{
		return b;
	}

	public void setB(String b)
	{
		this.b = b;
	}

	public String getC()
	{
		return c;
	}

	public void setC(String c)
	{
		this.c = c;
	}

	public String getD()
	{
		return d;
	}

	public void setD(String d)
	{
		this.d = d;
	}

	public String getE()
	{
		return e;
	}

	public void setE(String e)
	{
		this.e = e;
	}

	public String getF()
	{
		return f;
	}

	public void setF(String f)
	{
		this.f = f;
	}

	public String getF1()
	{
		return f1;
	}

	public void setF1(String f1)
	{
		this.f1 = f1;
	}

	public String getF2()
	{
		return f2;
	}

	public void setF2(String f2)
	{
		this.f2 = f2;
	}

	public String getF3()
	{
		return f3;
	}

	public void setF3(String f3)
	{
		this.f3 = f3;
	}

	public String getF4()
	{
		return f4;
	}

	public void setF4(String f4)
	{
		this.f4 = f4;
	}

	/**
	 * Permet d'ajouter une fonction à la liste
	 * 
	 * @param fonction à ajouter
	 */
	public void ajouter(Fonctions fonction)
	{
		liste.add(fonction);
	}

	/**
	 * Permet de supprimer une fonction de la liste
	 * 
	 * @param indice Indice de la fonction à supprimer
	 */
	public void supprimer(int indice)
	{
		liste.remove(indice);
	}
}
