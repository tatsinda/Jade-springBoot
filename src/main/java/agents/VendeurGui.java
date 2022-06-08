package agents;


import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//conatiner contenat l'agent achetuer et representant son interface graphique 
public class VendeurGui extends Application {

	protected VendeurAgent vendeurAgent; //refernce vers l'agent
	
	protected ObservableList<String> observableList;
	
	AgentContainer agentContainer;
	public static void main(String[] args) {
		
		launch(args); //on lance l'appication javafx
		
	}
	
	//methodde contenant les propriete de l'interface graphique
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		startContainer();
		primaryStage.setTitle("Vendeur");
		
		HBox hBox=new HBox();
		hBox.setPadding(new Insets(10));
		hBox.setSpacing(10);
		Label label=new Label("Agent name");
		TextField textFieldAgentName=new TextField();
		Button buttonDeploy=new Button("addAgent");
		hBox.getChildren().addAll(label,textFieldAgentName,buttonDeploy);
		
		
		BorderPane borderPane= new BorderPane();
		VBox vBox=new VBox();
	    observableList=FXCollections.observableArrayList();
		ListView<String> listView=new ListView<String>(observableList);
		vBox.getChildren().add(listView);
		borderPane.setTop(hBox);
		borderPane.setCenter(vBox);
		Scene scene=new Scene(borderPane,600,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		buttonDeploy.setOnAction((evt)->{ //methode permettante de deploayer un agent vendeur apres click sur le boutton
			
			AgentController agentController;
			try {
				String name=textFieldAgentName.getText();
				agentController = agentContainer.createNewAgent(name,"agents.VendeurAgent", new Object[] {this});
				agentController.start(); //on demarre le container et l'agent
			
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //on deploi l'agent
			
			
			
		});
		
		
	}
	
	
	// methode contenant les proprites du container
	private void startContainer() throws Exception
	{
		Runtime runtime= Runtime.instance(); //on cree une instance de contaier
		ProfileImpl profileImpl=new ProfileImpl(); //on cree un container
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");//on donne @ du container
		agentContainer= runtime.createAgentContainer(profileImpl);//permettant de deployer un agent
		
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
