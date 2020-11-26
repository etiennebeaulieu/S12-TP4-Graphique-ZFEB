package calculateur.controleur;

import java.net.URL;
import java.util.ResourceBundle;

import calculateur.CalculatriceApp;
import calculateur.model.Enregistre;
import calculateur.model.Fonctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

//Étienne Beaulieu et Zacharie Forest
public class FonctionController implements Initializable
{
	private MenuItem menuNom;
	private MenuItem menuF1;
	private MenuItem menuF2;
	private MenuItem menuF3;
	private MenuItem menuF4;
	private ContextMenu contextMenu;
	private static Enregistre memoire;

	@FXML
	private ListView<Fonctions> listeFonctions;

	@FXML
	private TextField fonction;

	@FXML
	private TextField nomFonction;

	@FXML
	void ajouteFonction(ActionEvent event)
	{
		// Valide la fonction
		if (Fonctions.validerFonction(fonction.getText()))
		{
			// Remplace les caractères de la fonctions pour qu'elle soit plus
			// belle
			String str = fonction.getText();
			str = str.replace('/', '÷');
			str = str.replace('*', '×');
			str = str.replace('.', ',');
			memoire.getListe().add(
					new Fonctions(fonction.getText(), nomFonction.getText()));
			// Vide les textField pour les rendre prêt à utiliser
			fonction.clear();
			nomFonction.clear();
		}
		else
			fonction.setText("Fonction invalide");
	}

	@FXML
	void deleteFonction(ActionEvent event)
	{
		memoire.supprimer(
				listeFonctions.getSelectionModel().getSelectedIndex());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		menuNom = new MenuItem("");
		menuNom.setStyle("-fx-font-size: 15");
		menuNom.setStyle("-fx-font-weight: bold");
		menuF1 = new MenuItem("Mettre dans F1");
		menuF2 = new MenuItem("Mettre dans F2");
		menuF3 = new MenuItem("Mettre dans F3");
		menuF4 = new MenuItem("Mettre dans F4");
		contextMenu = new ContextMenu(menuNom, new SeparatorMenuItem(), menuF1,
				menuF2, menuF3, menuF4);
		listeFonctions.setContextMenu(contextMenu);

		setMemoire();

		// Modifie en temps réel le texte du menuNom pour qu'il concorde avec le
		// nom de la fonction sélectionné
		listeFonctions.getSelectionModel().selectedItemProperty()
				.addListener((a, o, n) -> {
					if (n != null)
						menuNom.setText(n.getName());
				});

		menuF1.setOnAction((e) -> {
			if (listeFonctions.getSelectionModel().getSelectedItem() != null)
				memoire.setF1(listeFonctions.getSelectionModel()
						.getSelectedItem().getFonction());
		});
		menuF2.setOnAction((e) -> {
			if (listeFonctions.getSelectionModel().getSelectedItem() != null)
				memoire.setF2(listeFonctions.getSelectionModel()
						.getSelectedItem().getFonction());
		});
		menuF3.setOnAction((e) -> {
			if (listeFonctions.getSelectionModel().getSelectedItem() != null)
				memoire.setF3(listeFonctions.getSelectionModel()
						.getSelectedItem().getFonction());
		});
		menuF4.setOnAction((e) -> {
			if (listeFonctions.getSelectionModel().getSelectedItem() != null)
				memoire.setF4(listeFonctions.getSelectionModel()
						.getSelectedItem().getFonction());
		});

		listeFonctions.setTooltip(
				new Tooltip("Clic droit pour assigner une fonction"));

	}

	public void setMemoire()
	{
		memoire = CalculatriceApp.getMemoire();
		listeFonctions.itemsProperty().bindBidirectional(memoire.getListe());
	}

}