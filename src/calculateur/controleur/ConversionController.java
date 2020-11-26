package calculateur.controleur;

import java.net.URL;
import java.util.ResourceBundle;

import calculateur.model.Calculatrice;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

//Étienne Beaulieu et Zacharie Forest
public class ConversionController implements Initializable
{
	private DoubleProperty entreeProperty;
	private DoubleProperty sortieProperty;
	private StringProperty sortiePropertyString;
	private StringConverter<Number> numberStringConverter;

	@FXML
	private ComboBox<String> typeConversion;

	@FXML
	private Label valeurConvertie;

	private String metrePied, kiloLivre, coudeeLieue, pintesBarils, aucune;

	private CalculatriceController calculatriceController;

	private StringProperty entree;

	// Renvoi la valeur du label de conversion dans la calculatrice quand le
	// bouton utiliser est cliqué
	@FXML
	void utiliser(ActionEvent event)
	{
		calculatriceController.getChaine()
				.set(valeurConvertie.getText().replace('.', ','));
	}

	// Ajoute les différents types de conversion dans la ComboBox
	private void remplirComboBox()
	{
		metrePied = "Mètres ► Pieds";
		kiloLivre = "Kilogrammes ► Livres";
		coudeeLieue = "Coudées ► Lieues";
		pintesBarils = "Pintes ► Barils";
		aucune = "Aucune conversion";
		typeConversion.getItems().addAll(metrePied, kiloLivre, coudeeLieue,
				pintesBarils, aucune);
	}

	public void lierLabel()
	{
		entree = new SimpleStringProperty();
		entreeProperty = new SimpleDoubleProperty();
		sortieProperty = new SimpleDoubleProperty();
		sortiePropertyString = new SimpleStringProperty();
		numberStringConverter = new NumberStringConverter()
		{

			// Redéfinition des méthodes du NumberStringConverter pour que
			// l'arrondissement fonctionne
			@Override
			public String toString(Number object)
			{
				if (object != null)
					return Double.toString((double) object);
				else
					return null;
			}

			// Redéfinition des méthodes du NumberStringConverter pour que
			// l'arrondissement fonctionne
			@Override
			public Double fromString(String string)
			{
				return Double.parseDouble(string);
			}

		};

		// Assigne metrePiedWhen à son équivalent dans le comboBox
		When metrePiedWhen = Bindings.when(typeConversion.getSelectionModel()
				.selectedItemProperty().isEqualTo(metrePied));
		// Assigne kiloLivreWhen à son équivalent dans le comboBox
		When kiloLivreWhen = Bindings.when(typeConversion.getSelectionModel()
				.selectedItemProperty().isEqualTo(kiloLivre));
		// Assigne coudeeLieueWhen à son équivalent dans le comboBox
		When coudeeLieueWhen = Bindings.when(typeConversion.getSelectionModel()
				.selectedItemProperty().isEqualTo(coudeeLieue));
		// Assigne pintesBarilsWhen à son équivalent dans le comboBox
		When pintesBarilsWhen = Bindings.when(typeConversion.getSelectionModel()
				.selectedItemProperty().isEqualTo(pintesBarils));
		// Assigne aucuneWhen à son équivalent dans le comboBox
		When aucuneWhen = Bindings.when(typeConversion.getSelectionModel()
				.selectedItemProperty().isEqualTo(aucune));

		// Lie l'entrée de conversion à la réponse de ce qui est dans la zone
		// d'affichage de la calculatrice
		calculatriceController.getLabelReponse().textProperty()
				.addListener((a, o, n) -> {
					entree.setValue(Calculatrice
							.calculeEquation(new SimpleStringProperty(n))
							.replace(',', '.'));
				});

		// Lie l'entrée texte à l'entrée en double
		entree.bindBidirectional(entreeProperty, numberStringConverter);

		// Lie la sortie de la conversion au bon taux de conversion déterminer
		// selon le when
		sortieProperty.bind(metrePiedWhen.then(entreeProperty.multiply(3.28084))
				.otherwise(kiloLivreWhen.then(entreeProperty.multiply(2.20462))
						.otherwise(coudeeLieueWhen
								.then(entreeProperty.multiply(0.0000822894))
								.otherwise(pintesBarilsWhen
										.then(entreeProperty.multiply(0.003472))
										.otherwise(aucuneWhen
												.then(entreeProperty)
												.otherwise(entreeProperty))))));

		// Lie d'abord la sortie en double à celle en texte pour ensuite lier
		// celle-ci au label de valeurConvertie
		sortiePropertyString.bindBidirectional(sortieProperty,
				numberStringConverter);
		valeurConvertie.textProperty().bind(sortiePropertyString);

	}

	public void setCalculatriceController(CalculatriceController controleur)
	{
		calculatriceController = controleur;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		remplirComboBox();

	}

}
