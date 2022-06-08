package containers;


import agents.ConsumerAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
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

//creation du container contenant l'agent ConsumerAgent et representant son interface graphique
public class ConsumerContainer extends Application{
	
	
	protected ConsumerAgent consumerAgent; //on cree une reference vers l'intrface graphique afin de pouvoir communiquer avec elle
	ObservableList<String> observableList;
	public static void main(String[] args) throws ControllerException {
		
		launch(args); //methode appeler pour demarrage l'application javaFx
		
		
	}
	
	public void startContainer() throws Exception{ //methode contenant notre container
		
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl=new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");//on indique au conatinerou se trouve le main container
		AgentContainer container=runtime.createAgentContainer(profileImpl);
		//on cree une instance de l'agent dans le containeur et on le deploie
		AgentController agentController=container 
				.createNewAgent("Consumer", "agents.ConsumerAgent", new Object[] {this});//on passe en parametre: le nom de l'agent, la classe ou se trouve l'agent et le troisieme ces params envoyer a l'agent(this : pour direa l'agent lors du deployement que voila la reference vers son interface graphique)
		agentController.start();//on deploie l'agent et le container
		
	}
	
	public void setConsumerAgent(ConsumerAgent consumerAgent) {
		this.consumerAgent = consumerAgent;
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		
		startContainer();
		
		primaryStage.setTitle("Consommateur");
		//creation d l'elemet parent HBOX pour les champs horizontaux
		HBox hBox=new HBox();
		hBox.setPadding(new Insets(10)); //on met une pagination entre les elements du padding
		hBox.setSpacing(10); //on cre un espace de 10 avec la fentre et les elements du Hbox
		
		//on cree les elements enfant du hBox
		Label label=new Label("Livre");
		TextField textField=new TextField();
		Button buttonAcheter =new Button("Acheter");
		
		hBox.getChildren().addAll(label,textField,buttonAcheter);//on ajoute ls elements enfant au hbox
		
		
		//creation d l'elemet parent VBOX pour les champs verticaux
		VBox vBox=new VBox();
		vBox.setPadding(new Insets(10));
		observableList=FXCollections.observableArrayList(); // definition d'un Observable pour contenir le message a envoyer dans le listview
		ListView<String> listViewMessages=new ListView<String>(observableList); //creation d'un lisview pour contenir lés messages dans un observable
		vBox.getChildren().add(listViewMessages); //ajout du listView dans le vBox
		
		//creation d l'element parnt contenant ls autres
		BorderPane borderPane=new BorderPane();
		borderPane.setTop(hBox);
		borderPane.setCenter(vBox);
		
		//creation d'une scene
		Scene scene= new Scene(borderPane, 600,400); //creation d'une scene avec 600 de longueur et 400 de largeur
		primaryStage.setScene(scene); //on defnir la scene de la fenetre
		primaryStage.show(); //on affche la fenetre
		
		
		//execution a faire apres le click sur le boutton
		
		buttonAcheter.setOnAction(evt->{
			
			String livre= textField.getText(); // on recupere le contnu du textField
			System.out.println("livre: "+textField.getText());
			//observableList.add(livre);// on ajout l livre au observable donc  a la listView
			GuiEvent event = new GuiEvent(this,1);//on donne la source de l'evenemet et le type de l'evenement
			event.addParameter(livre); //on envvoi le parametre lire a la methode onGuiEvent()
			consumerAgent.onGuiEvent(event);// on donne a la methode OnGuiEvent l'evenement event
			
			
		});
	
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
