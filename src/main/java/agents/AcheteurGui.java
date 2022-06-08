package agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//conatiner contenat l'agent achetuer et representant son interface graphique 
public class AcheteurGui extends Application {

	protected AcheteurAgent acheteurAgent; //refernce vers l'agent
	
	protected ObservableList<String> observableList;
	public static void main(String[] args) {
		
		launch(args); //on lance l'appication javafx
		
	}
	
	//methodde contenant les propriete de l'interface graphique
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		startContainer();
		primaryStage.setTitle("Acheteur");
		BorderPane borderPane= new BorderPane();
		VBox vBox=new VBox();
	    observableList=FXCollections.observableArrayList();
		ListView<String> listView=new ListView<String>(observableList);
		vBox.getChildren().add(listView);
		borderPane.setCenter(vBox);
		Scene scene=new Scene(borderPane,600,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		
		
	}
	
	
	// methode contenant les proprites du container
	private void startContainer() throws Exception
	{
		Runtime runtime= Runtime.instance(); //on cree une instance de contaier
		ProfileImpl profileImpl=new ProfileImpl(); //on cree un container
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");//on donne @ du container
		AgentContainer agentContainer= runtime.createAgentContainer(profileImpl);//permettant de deployer un agent
		AgentController agentController=agentContainer.createNewAgent("ACHETEUR","agents.AcheteurAgent", new Object[] {this}); //on deploi l'agent
		
		agentController.start(); //on demarre le container et l'agent
	}
	
	//methode permettant d'ajouter un livre a la liste
	public void logMessage(ACLMessage aclMessage)
	{
		//permet de mettre l'instruction dans le meme contexte que cluie javafx
		Platform.runLater(()->{
			observableList.add(aclMessage.getContent()
					+", "+aclMessage.getSender().getName()
					);
		});
		
	}
	
	

}
