package calculateur.controleur;

import java.net.URL;
import java.time.Clock;
import java.util.ResourceBundle;

import org.mariuszgromada.math.mxparser.Function;

import com.sun.xml.internal.ws.api.model.ExceptionType;

import calculateur.CalculatriceApp;
import calculateur.model.Enregistre;
import calculateur.model.Fonctions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.util.converter.NumberStringConverter;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

	@FXML
	void animer(ActionEvent event)
	{
		erreurLabel.setText("");
		if (!validerAnimer().equals(""))
			erreurLabel.setText(validerAnimer());
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

	@FXML
	void annuler(ActionEvent event)
	{
		if (seriesService.isRunning())
			seriesService.cancel();
		else
			erreurLabel.setText("Aucune animation à annuler");
	}

	@FXML
	void effacer(ActionEvent event)
	{
		erreurLabel.setText("");
		graphique.getData().clear();
	}

	@FXML
	void tracer(ActionEvent event)
	{
		erreurLabel.setText("");

		if (validerTracer().equals(""))
		{

			Series<Number, Number> series = tracerFonction(
					Double.parseDouble(aMinText.getText()));
			graphique.setCreateSymbols(false);
			if(!series.getData().isEmpty())
				graphique.getData().add(series);
			else
				erreurLabel.setText("Fonction indéfinie");
		}
		else
			erreurLabel.setText(validerTracer());

	}

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

			
			if(!((Double)fonction.calculate(i,a)).equals(Double.NaN))
				series.getData().add(new Data<Number, Number>(i, fonction.calculate(i, a)));
		}

		return series;

	}

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
							if(!tracerFonction(i).getData().isEmpty())
								retour = tracerFonction(i);
							updateValue(retour);
							updateProgress(-1 * min + i, max - min);

							try
							{
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
							ZoneId zone = ZoneId.of("UTC-5");
							Clock seconde = Clock.tickSeconds(zone);
							DateTimeFormatter formatter = DateTimeFormatter
									.ofPattern("HH:mm:ss\nyyyy-MM-dd")
									.withZone(zone);
							retour = formatter.format(Instant.now(seconde));

							updateValue(retour);

							try
							{
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

		horlogeLabel.textProperty().bind(horlogeService.valueProperty());
		horlogeService.start();

	}

	public void setMemoire()
	{
		memoire = CalculatriceApp.getMemoire();
		fonctionsListe.itemsProperty().bindBidirectional(memoire.getListe());

	}

}