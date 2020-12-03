package calculateur.controleur;

import java.net.URL;
import java.time.Clock;
import java.util.ResourceBundle;

import org.mariuszgromada.math.mxparser.Function;

import calculateur.CalculatriceApp;
import calculateur.model.Enregistre;
import calculateur.model.Fonctions;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

//Zacharie Forest et Étienne Beaulieu
public class GraphiqueController implements Initializable
{

	@FXML
	private LineChart<Number, Number> graphique;

	@FXML
	private TextField aMinText;

	@FXML
	private TextField aMaxText;

	@FXML
	private TextField dureeText;

	@FXML
	private TextField xMinText;

	@FXML
	private TextField xMaxText;

	@FXML
	private TextField samplingText;

	@FXML
	private Label erreurLabel;

	@FXML
	private ProgressBar progressAnimer;

	@FXML
	private Button tracerBtn;

	@FXML
	private Button effacerBtn;

	@FXML
	private Button animerBtn;

	@FXML
	private Button annulerBtn;

	@FXML
	private ListView<Fonctions> fonctionsListe;

	@FXML
	private Label horlogeLabel;

	private static Enregistre memoire;

	Service<Series<Number, Number>> seriesService;

	//Méthode se mettant en fonction lorsque le bouton animer est activé
	@FXML
	void animer(ActionEvent event)
	{
		erreurLabel.setText("");
		if (!validerAnimer().equals(""))
			erreurLabel.setText(validerAnimer());
		//Entre dans le else si il n'y a aucun problème, complète le service et le lance
		else
		{
			seriesService.valueProperty().addListener((a, o, n) -> {
				graphique.getData().clear();
				graphique.getData().add(n);
			});

			progressAnimer.progressProperty()
					.bind(seriesService.progressProperty());

			seriesService.setOnFailed((e)->{
				graphique.getData().clear();
				erreurLabel.setText("Erreur dans l'animation");
				progressAnimer.progressProperty().unbind();
				progressAnimer.setProgress(0);
				desactiverBouton(false);
			});
			
			seriesService.setOnCancelled((e) -> {
				graphique.getData().clear();
				erreurLabel.setText("Animation annulée");
				progressAnimer.progressProperty().unbind();
				progressAnimer.setProgress(0);
				desactiverBouton(false);
			});
			seriesService.setOnSucceeded((e) -> {
				progressAnimer.progressProperty().unbind();
				progressAnimer.setProgress(0);
				desactiverBouton(false);
			});

			seriesService.restart();

		}

	}

	//Annule l'animation s'il y en a une
	@FXML
	void annuler(ActionEvent event)
	{
		if (seriesService.isRunning())
			seriesService.cancel();
		else
			erreurLabel.setText("Aucune animation à annuler");
	}

	//Efface le graphique tracé
	@FXML
	void effacer(ActionEvent event)
	{
		erreurLabel.setText("");
		graphique.getData().clear();
	}

	//Trace le graphique
	@FXML
	void tracer(ActionEvent event)
	{
		erreurLabel.setText("");

		if (validerTracer().equals(""))
		{

			Series<Number, Number> series = tracerFonction(
					Double.parseDouble(aMinText.getText()));
			graphique.setCreateSymbols(false);
			
			//Vérifie si la fonction est définie
			if(!series.getData().isEmpty())
				graphique.getData().add(series);
			else
				erreurLabel.setText("Fonction indéfinie");
		}
		else
			erreurLabel.setText(validerTracer());

	}

	//Vérifie les différentes possibilitées d'erreur
	private String validerTracer()
	{
		String erreur = "";

		if (fonctionsListe.getSelectionModel().isEmpty())
			erreur += "Aucune fonction sélectionnée\n";
		else
			if (!fonctionsListe.getSelectionModel().getSelectedItem()
					.deuxVariable())
				erreur += "Fonction invalide. Veuillez choisir une fonction à deux variables\n";
		if (xMinText.getText().isEmpty() || xMaxText.getText().isEmpty())
			erreur += "Bornes non spécifiées\n";
		else
			if (Double.parseDouble(xMaxText.getText()) <= Double.parseDouble(xMinText.getText()))
				erreur += "Bornes invalides\n";
		if (samplingText.getText().isEmpty())
			erreur += "Résolution non spécifiée\n";
		else
			if (Double.parseDouble(samplingText.getText()) <= 0)
				erreur += "Résolution invalide\n";

		return erreur;

	}

	//Vérifie les différentes possibilitées d'erreur
	private String validerAnimer()
	{
		String erreur = "";
		erreur += validerTracer();

		if (aMinText.getText().isEmpty() || aMaxText.getText().isEmpty())
			erreur += "Bornes de a non spécifiées\n";
		else
			if (Double.parseDouble(aMaxText.getText()) <= Double
					.parseDouble(aMinText.getText()))
				erreur += "Bornes de a invalides\n";
		if (dureeText.getText().isEmpty())
			erreur += "Durée non spécifiée\n";
		else
			if (Double.parseDouble(dureeText.getText()) <= 0)
				erreur += "Durée invalide\n";

		return erreur;
	}

	//Crée une série et la remplie
	private Series<Number, Number> tracerFonction(double a)
	{
		Series<Number, Number> series = new Series<>();

		Fonctions select = fonctionsListe.getSelectionModel().getSelectedItem();
		Function fonction = new Function(select.getFonction());
		double min = Double.parseDouble(xMinText.getText());
		double max = Double.parseDouble(xMaxText.getText());
		double sampling = Double.parseDouble(samplingText.getText());
		double incrementation = ((max - min) / sampling);

		series.setName(select.getName());

		for (double i = min; i <= max; i += incrementation)
		{

			//S'assurer que le point est défini avant de l'ajouter
			if(!((Double)fonction.calculate(i,a)).equals(Double.NaN))
				series.getData().add(new Data<Number, Number>(i, fonction.calculate(i, a)));
		}

		return series;

	}

	//Désactive les boutons
	private void desactiverBouton(boolean b)
	{
		tracerBtn.setDisable(b);
		effacerBtn.setDisable(b);
		animerBtn.setDisable(b);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		setMemoire();
		horloge();
		creerServiceAnimation();

	}

	//Instancie le service qui s'occupe de l'animation
	private void creerServiceAnimation()
	{
		seriesService = new Service<Series<Number, Number>>()
		{

			@Override
			protected Task<Series<Number, Number>> createTask()
			{
				return new Task<Series<Number, Number>>()
				{

					double min = Double.parseDouble(aMinText.getText());
					double max = Double.parseDouble(aMaxText.getText());
					double duree = Double.parseDouble(dureeText.getText());
					double incrementation = (max - min) / (duree * 30);
					{
						desactiverBouton(true);
						graphique.setCreateSymbols(false);
					}

					@Override
					protected Series<Number, Number> call() throws Exception
					{

						Series<Number, Number> retour = new Series<Number, Number>();

						for (double i = min; i <= max
								&& !isCancelled(); i += incrementation)
						{
							//S'assure que le graphique est défini 
							if(!tracerFonction(i).getData().isEmpty())
								retour = tracerFonction(i);
							updateValue(retour);
							updateProgress(-1 * min + i, max - min);

							try
							{
								//Générer trente graphiques par seconde
								Thread.sleep(33);
							}
							catch (Exception e)
							{
							}
						}
						return retour;
					}

				};
			}

		};

	}

	private void horloge()
	{
		Service<String> horlogeService = new Service<String>()
		{

			@Override
			protected Task<String> createTask()
			{
				return new Task<String>()
				{

					@Override
					protected String call() throws Exception
					{
						String retour = "";

						while (!isCancelled())
						{
							//Sélectionne le bon fuseau horaire
							ZoneId zone = ZoneId.of("UTC-5");
							//Génère une horloge précise aux secondes
							Clock seconde = Clock.tickSeconds(zone);
							//Transforme la date sous le bon format
							DateTimeFormatter formatter = DateTimeFormatter
									.ofPattern("HH:mm:ss\nyyyy-MM-dd")
									.withZone(zone);
							retour = formatter.format(Instant.now(seconde));

							updateValue(retour);

							try
							{
								//Actualise à tous les secondes
								Thread.sleep(1000);
							}
							catch (Exception e)
							{
							}

						}

						return retour;
					}

				};
			}

		};

		//Bind le service avec le label
		horlogeLabel.textProperty().bind(horlogeService.valueProperty());
		horlogeService.start();

	}

	//Acceder et lier la mémoire
	public void setMemoire()
	{
		memoire = CalculatriceApp.getMemoire();
		fonctionsListe.itemsProperty().bindBidirectional(memoire.getListe());

	}

}