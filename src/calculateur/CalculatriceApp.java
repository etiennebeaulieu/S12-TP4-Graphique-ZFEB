package calculateur;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import calculateur.controleur.CalculatriceController;
import calculateur.controleur.ConversionController;
import calculateur.controleur.FonctionController;
import calculateur.controleur.GraphiqueController;
import calculateur.model.Enregistre;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//Étienne Beaulieu et Zacharie Forest
public class CalculatriceApp extends Application
{
	private static Stage fenetreConversion;
	private static Stage fenetreFonction;
	private static Stage fenetreCalculatrice;
	private static Stage fenetreGraphique;
	private static CalculatriceController calculatriceController;
	private static ConversionController conversionController;
	private static FonctionController fonctionController;
	private static GraphiqueController graphiqueController;
	private static Enregistre memoire;

	public static void main(String[] args)
	{
		// Crée une instance d'enregistrement qui sera commun à tous les
		// controlleurs
		memoire = new Enregistre();
		launch();

	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{

		// Load, configure et affiche la fenêtre principale de la calculatrice
		FXMLLoader loader = new FXMLLoader(
				this.getClass().getResource("fxml/Calculatrice.fxml"));
		VBox vbox = loader.load();
		calculatriceController = loader.getController();

		fenetreCalculatrice = new Stage();
		fenetreCalculatrice.setScene(new Scene(vbox));
		fenetreCalculatrice
				.setTitle("Calculatrice Étienne Beaulieu et Zacharie Forest");
		fenetreCalculatrice.getIcons().add(new Image(
				this.getClass().getResourceAsStream("/images/logoCalcu.png")));
		fenetreCalculatrice.setWidth(600);
		fenetreCalculatrice.setResizable(false);
		fenetreCalculatrice.show();

		// Load et configure la fenêtre secondaire pour le module de conversion
		FXMLLoader loader2 = new FXMLLoader(
				this.getClass().getResource("fxml/Conversion.fxml"));
		VBox vbox2 = loader2.load();
		conversionController = loader2.getController();
		conversionController.setCalculatriceController(loader.getController());
		calculatriceController.setConversionController(loader2.getController());

		fenetreConversion = new Stage();
		fenetreConversion.setScene(new Scene(vbox2));
		fenetreConversion.setTitle("Module de conversion Étienne et Zacharie");
		fenetreConversion.getIcons().add(new Image(
				this.getClass().getResourceAsStream("/images/logoConv.png")));
		fenetreConversion.setWidth(290);
		fenetreConversion.setResizable(false);
		fenetreConversion.initOwner(fenetreCalculatrice);

		// Load et configure la fenêtre secondaire pour le module de fonctions
		FXMLLoader loader3 = new FXMLLoader(
				this.getClass().getResource("fxml/Fonction.fxml"));
		HBox hbox = loader3.load();
		fonctionController = loader3.getController();

		fenetreFonction = new Stage();
		fenetreFonction.setScene(new Scene(hbox));
		fenetreFonction.setTitle("Module de fonction Étienne et Zacharie");
		fenetreFonction.getIcons().add(new Image(
				this.getClass().getResourceAsStream("/images/logoFonc.png")));
		fenetreFonction.setResizable(false);
		fenetreFonction.initOwner(fenetreCalculatrice);
		
		
		FXMLLoader loader4 = new FXMLLoader(this.getClass().getResource("fxml/Graphique.fxml"));
		BorderPane bP = loader4.load();
		graphiqueController = loader4.getController();
		
		fenetreGraphique = new Stage();
		fenetreGraphique.setScene(new Scene(bP));
		fenetreGraphique.setTitle("Module du graphique Étienne et Zacharie");
		fenetreGraphique.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/logoGraph.png")));
		fenetreGraphique.setResizable(false);
		fenetreGraphique.initOwner(fenetreCalculatrice);
		

	}

	public static void showConversion()
	{
		fenetreConversion.show();
	}

	public static void showFonction()
	{
		fenetreFonction.show();
	}
	
	public static void showGraphique()
	{
		fenetreGraphique.show();
	}

	// Sérialise l'objet de type Enregistre et l'écrit dans un fichier envoyé en
	// paramètre
	public static void enregistrerMemoire(FileChooser fileChooser)
	{
		// Convertie la ListProperty en ArrayList pour qu'elle soit serialisable
		memoire.convertirListe();
		File fichier = fileChooser.showSaveDialog(fenetreCalculatrice);

		ObjectOutputStream obj = null;

		try
		{
			obj = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(fichier)));
			obj.writeObject(memoire);

			obj.flush();
			obj.close();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

	// Lit un fichier reçu en paramètre et le dé-sérialise en objet de type
	// Enregistre
	public static void lireMemoire(FileChooser fileChooser)
	{
		File fichier = fileChooser.showOpenDialog(fenetreCalculatrice);

		ObjectInputStream obj = null;

		try
		{
			obj = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(fichier)));
			memoire = (Enregistre) obj.readObject();

			obj.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

		// Convertie l'arrayList en ListProperty pour qu'elle soit lié à la
		// ListView
		memoire.convertirArray();

		setMemoire();
	}

	// Partage la même instance de mémoire aux controlleurs pour que ceux-ci ai
	// accès à la même mémoire
	private static void setMemoire()
	{
		calculatriceController.setMemoire();
		fonctionController.setMemoire();
		graphiqueController.setMemoire();

	}

	public static Enregistre getMemoire()
	{
		return memoire;
	}

}
