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
import javafx.scene.control.TextField;
import java.time.Duration;

public class GraphiqueController implements Initializable
{

	@FXML
	private LineChart<Number, Number> graphique;

	@FXML
	private TextField aMinLabel;

	@FXML
	private TextField aMaxLabel;

	@FXML
	private TextField dureeLabel;

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

	@FXML
	void animer(ActionEvent event)
	{
		
	}

	@FXML
	void annuler(ActionEvent event)
	{

	}

	@FXML
	void effacer(ActionEvent event)
	{
		graphique.getData().clear();
	}

	@FXML
	void tracer(ActionEvent event)
	{
		graphique.getData().clear();
		
		Fonctions select = fonctionsListe.getSelectionModel().getSelectedItem();
		Function fonction = new Function(select.getFonction());
		
		Series<Number,Number> series = new Series<>();
		series.setName(select.getName());
		
		for(double i = select.getxMin(); i<=select.getxMax(); i+=((select.getxMax()-select.getxMin())/select.getSampling()))
		{
			
			series.getData().add(new Data<Number, Number>(i, fonction.calculate(i, Double.parseDouble(aMinLabel.getText()))));
		}
		
		graphique.setCreateSymbols(false);
		graphique.getData().add(series);
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		setMemoire();
		
		horloge();
		
	}

	private void horloge()
	{
		Service<String> horlogeService = new Service<String>() {

			@Override
			protected Task<String> createTask()
			{
				return new Task<String>() {

					@Override
					protected String call() throws Exception
					{
						String retour = "";
						
						while(!isCancelled())
						{
							Clock local = Clock.systemDefaultZone();
							Clock.tick(local, Duration.ofHours(1));
							
							retour =
							
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