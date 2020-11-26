package calculateur.controleur;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import calculateur.CalculatriceApp;
import calculateur.model.Calculatrice;
import calculateur.model.Enregistre;

//Étienne Beaulieu et Zacharie Forest
public class CalculatriceController implements Initializable
{
	private StringProperty chaine;
	private static Enregistre memoire;
	private ConversionController conversionController;
	private String fonction;

	private Button[] arrayBouton;

	@FXML
	private Label labelReponse;

	@FXML
	private Button bouton7;

	@FXML
	private Button bouton8;

	@FXML
	private Button bouton4;

	@FXML
	private Button bouton9;

	@FXML
	private Button boutonDiv;

	@FXML
	private Button bouton5;

	@FXML
	private Button bouton6;

	@FXML
	private Button boutonMul;

	@FXML
	private Button bouton1;

	@FXML
	private Button bouton2;

	@FXML
	private Button bouton3;

	@FXML
	private Button boutonMoins;

	@FXML
	private Button boutonVirgule;

	@FXML
	private Button bouton0;

	@FXML
	private Button boutonEgal;

	@FXML
	private Button boutonPlus;

	@FXML
	private Button boutonParDroite;

	@FXML
	private Button boutonParGauche;

	@FXML
	private Button boutonF1;

	@FXML
	private Button boutonF2;

	@FXML
	private Button boutonF3;

	@FXML
	private Button boutonF4;

	@FXML
	void enregistrer(ActionEvent event)
	{
		CalculatriceApp.enregistrerMemoire(
				construireDialogue("Enregistrer la mémoire"));
	}

	@FXML
	void ouvrir(ActionEvent event)
	{
		CalculatriceApp
				.lireMemoire(construireDialogue("Sélectionner la mémoire"));
	}

	// Construit le dialogue d'enregistrement/de lecture
	private FileChooser construireDialogue(String titre)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(titre);
		fileChooser
				.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters()
				.add(new ExtensionFilter("Mémoire", "*.mem"));
		return fileChooser;
	}

	@FXML
	void fermer(ActionEvent event)
	{
		Platform.exit();
	}

	@FXML
	void ouvrirConversion(ActionEvent event)
	{
		// Ouvre la fenêtre du module de conversion
		CalculatriceApp.showConversion();
		conversionController.lierLabel();
	}

	@FXML
	void ouvrirFonction(ActionEvent event)
	{
		// Ouvre la fenêtre du module de fonction
		CalculatriceApp.showFonction();
	}
	
	@FXML
	void ouvrirGraphique(ActionEvent event)
	{
		//Ouvre la fenêtre du module du graphique
		CalculatriceApp.showGraphique();
	}

	@FXML
	void reset(ActionEvent event)
	{
		chaine.set("");

	}

	@FXML
	void getA(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getA()));

	}

	@FXML
	void getB(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getB()));

	}

	@FXML
	void getC(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getC()));

	}

	@FXML
	void getD(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getD()));

	}

	@FXML
	void getE(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getE()));

	}

	@FXML
	void getF(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getF()));

	}

	@FXML
	void setA(ActionEvent event)
	{
		memoire.setA(labelReponse.getText());
	}

	@FXML
	void setB(ActionEvent event)
	{
		memoire.setB(labelReponse.getText());
	}

	@FXML
	void setC(ActionEvent event)
	{
		memoire.setC(labelReponse.getText());
	}

	@FXML
	void setD(ActionEvent event)
	{
		memoire.setD(labelReponse.getText());
	}

	@FXML
	void setE(ActionEvent event)
	{
		memoire.setE(labelReponse.getText());
	}

	@FXML
	void setF(ActionEvent event)
	{
		memoire.setF(labelReponse.getText());
	}

	@FXML
	void getF1(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getF1()));

	}

	@FXML
	void getF2(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getF2()));

	}

	@FXML
	void getF3(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getF3()));

	}

	@FXML
	void getF4(ActionEvent event)
	{
		chaine.setValue(chaine.getValue().concat(memoire.getF4()));

	}

	@FXML
	void calcule(ActionEvent event)
	{
		String str = chaine.get();

		// Si la chaine contient le symbole = c'est qu'il s'agit d'une fonction,
		// il faut donc entrer en mode fonction et afficher "f(" pour que
		// l'utilisateur entre la valeur à laquelle il veut évaluer la fonction
		if (str.contains("="))
		{
			chaine.setValue(str.charAt(0) + "(");
			// Instancie la String fonction pour garder en mémoire la fonction
			fonction = str;
		}
		// Si fonction n'est pas null c'est que l'utilisateur a évaluer une
		// fonction
		else
			if (fonction != null)
			{
				int indexPar1 = str.indexOf("(");
				int indexPar2 = str.indexOf(")");
				int indexVir = str.indexOf(",");

				// Si la chaine contient une virgule c'est qu'il y a une
				// fonction à deux variables et qu'il faut donc fournir deux
				// valeurs pour l'évaluer
				if (str.contains(","))
				{
					chaine.setValue(Calculatrice.calculeFonction(fonction,
							Integer.parseInt(
									str.substring((indexPar1 + 1), indexVir)),
							Integer.parseInt(
									str.substring((indexVir + 1), indexPar2))));
					fonction = null;
				}
				// Si la chaine ne contient pas de virgule c'est qu'il y a une
				// fonction à une variable et qu'il faut donc fournir une valeur
				// pour l'évaluer
				else
				{
					chaine.setValue(Calculatrice.calculeFonction(fonction,
							Integer.parseInt(str.substring((indexPar1 + 1),
									indexPar2))));
					fonction = null;
				}
			}
			// Si la chaine représente une équation il faut la calculer par le
			// modèle Calculatrice
			else
				chaine.setValue(Calculatrice.calculeEquation(chaine));

	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		chaine = new SimpleStringProperty("");
		labelReponse.textProperty().bindBidirectional(chaine);
		fonction = null;
		Tooltip tt = new Tooltip(
				"Appuyer sur Enter pour évaluer une\nfonction puis entrez la valeur à\nlaquelle l'évaluer entre paranthèse");

		setMemoire();

		// Array contenenant tous les boutons pour facilement effectuer une
		// action à appliquer sur tous les boutons
		arrayBouton = new Button[]
		{ bouton0, bouton1, bouton2, bouton3, bouton4, bouton5, bouton6,
				bouton7, bouton8, bouton9, boutonDiv, boutonMul, boutonMoins,
				boutonPlus, boutonVirgule, boutonParGauche, boutonParDroite };

		// Event handler qui ajoute dans l'affichage le caractère du bouton qui
		// vient d'être sélectionné
		EventHandler<MouseEvent> clic = (MouseEvent e) -> {
			chaine.setValue(chaine.getValue()
					.concat(((Button) e.getSource()).getText()));
		};

		for (Button bouton : arrayBouton)
		{
			bouton.addEventFilter(MouseEvent.MOUSE_CLICKED, clic);
		}

		boutonF1.setTooltip(tt);
		boutonF2.setTooltip(tt);
		boutonF3.setTooltip(tt);
		boutonF4.setTooltip(tt);

	}

	public void setMemoire()
	{
		memoire = CalculatriceApp.getMemoire();

	}

	public void setConversionController(ConversionController controleur)
	{
		conversionController = controleur;
	}

	public StringProperty getChaine()
	{
		return chaine;
	}

	public Label getLabelReponse()
	{
		return labelReponse;
	}

}
