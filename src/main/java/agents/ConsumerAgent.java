package agents;

import java.nio.channels.NonWritableChannelException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import containers.ConsumerContainer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Service;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.tools.sniffer.Message;


//Agent representant le client qui veut acheter un livre
//herite de la classe GuiAgent afin de lui donner une interface graphique
public class ConsumerAgent extends GuiAgent {

	private transient ConsumerContainer gui; // //on cree une reference vers l'agent afin de pouvoir communiquer avec lui //le mot transient veut dire de ne pas seriaiser l'intrface graphique lors d la migration de l'agent vers un autre container
	//methode executer avant le demarrage de l'agent pour la publication de son ID dans le conteneur
	@Override
	protected void setup()
	{
		System.out.println("************************");
		System.out.println("Agent initialisation........" +this.getAID().getName()); //on affiche le nom de l'agent
		
		//on fait un test avant d recuperer le parametre envoyer a l#agent
		if(this.getArguments().length==1)
		{
			//System.out.println("je vais tenter d'acheter le livre "+getArguments()[0]); //on recupere le parametre se trouvant au premier indice du tableau
			gui=(ConsumerContainer)getArguments()[0]; //on recupere le premier param envoyer t n le donn comm intrface graphique
			gui.setConsumerAgent(this); //on donne a l#interface graphique son agent
		}
		
		ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		
		//comportement pemettant de recevoir un message d'un agent
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				ACLMessage aclMessage=receive();
				if(aclMessage!=null)
				{
					
					switch (aclMessage.getPerformative()) {
					
					case ACLMessage.CONFIRM:
						
						gui.logMessage(aclMessage);
						
						break;

					default:
						break;
					}
					
					
				}
				else block();
			}
		});
		
		
		
		
		
		System.out.println("************************");
		
		
		//creation d'un comportement generique de l'agent (comportement definir par nous meme) etpas predefinir
		
		
		
		 //methode permettante de definir un comportement (une tache) de l'agent
	 /*	addBehaviour(new Behaviour() {
			private int compteur=0;
			
			//methode qui definir si les tache executer par l'agent sont termines ou pas
			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				if(compteur==10) return true;
				else return false;
			}
			
			//methode contenat la tache de l'agent(soncomportement) et l'agent execute cette tache n fonction de la methode done
			@Override
			public void action() {
				// TODO Auto-generated method stub
				//tache executer par l'agent
				++compteur;
				System.out.println("Etape: "+compteur);  
			}
		});*/
		
		//*****************************************************************
		
		
		
		
		//creation d'un comportement non generique (grace au methode predenir par JADE)
		
		
		//executant la tache de l'agent une seule fois
	/*	addBehaviour(new OneShotBehaviour() {
			
			//comportemnt executer une seule fois
			@Override
			public void action() {
				// TODO Auto-generated method stub
				System.out.println("Action ........");
				
			}
		});*/
		
		
		
		
		/*//Creation d'un WakeBehaviour
		DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss"); //defnition du format de la date a inserer
		
		try {
			//creation d'un comportement qui s'executera apres une date planifiee
			addBehaviour(new WakerBehaviour(this,dateFormat.parse("11/07/2021:20:20:00")) {
				
				//tavhe a executee
				@Override
				protected void onWake() {
					// TODO Auto-generated method stub
					System.out.println("Tache plamifiee");
				}
				
			});
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		*/
		
		//******************************************************************
		
		//creation d'un ParallelBehaviour pour executer le comportement de facon paralleele
		
		
		
		/*ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
		addBehaviour(parallelBehaviour); //on execute le parallelBehaviour
		
		
		//comportement s'executant apres des secondes defnies en milliSeconde differement de AwareBehaviour qui execute l comporte apres une periode en fonction de  la date, les heures et les  secondes
		//on ajout le comportement au parallelBehaviour
		
		
		parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,1000) { //on donne une sxconde en parametre
			
			//execute la tache apres chaque "une seconde"
			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				
				System.out.println("TIC");
			}
		});
		
		
		//executant la tache de l'agent repetitivement
		//on ajout le comportement au parallelBehaviour
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			//comportemnt executer a l#infini
			@Override
			public void action() {
				// TODO Auto-generated method stub
				
				System.out.println("Cyclique..........");
			}
		}); */
		
		//************************************************
		
		
		//envoi et recption d'un message entr les agents
		
		/*ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
		addBehaviour(parallelBehaviour);*/
		
		// la reception d'un message et reply s font dans Cycle.........
		
		/*parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
			
				//creation d'un messgae template pour les condition de reception d'un message en fonction de l'ontology et du lart de communication
				MessageTemplate message=MessageTemplate.and(
						MessageTemplate.MatchOntology("vente-livre"),
						MessageTemplate.MatchPerformative(ACLMessage.CFP));
						
				
				ACLMessage aclMessage=receive(message); //on definir l'objet qui receverra le message
				
				if(aclMessage!=null) //on test si un message a ete recuperer dans la messagerie
				{
					System.out.println("**************************");
					System.out.println(aclMessage.getContent()); //on recupre le contenu du message
					System.out.println(aclMessage.getSender().getName());//on recupere le nom de l#agent expediteur
					System.out.println(aclMessage.getPerformative());// le numero de l#agent expediteur
					System.out.println(aclMessage.getLanguage());//le langage utiliser par l'agent expediteur
					System.out.println(aclMessage.getOntology()); // le contexte dans lequel se trouve le message
					System.out.println("**************************");
					
					
					//on envoi un reply vers l'expediteur  apres la reception du message
					
					ACLMessage reply = aclMessage.createReply(); //on cree le reply
					reply.setContent("Weekend libr oh non"); //ontenue du reply
					send(reply); //envoi du message
					
					
				}
				else  block(); // cette instruction permet de ne pas repeter la methode action tant qu'il ne recoit une notification d'un message dans sa messagerie
				
				
				
			}
		});*/
		
		//******************************************
		//publication du service de l'agent Consumer dans l'agent DF
		
		/*parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				// TODO Auto-generated method stub
				DFAgentDescription agentDescription=new DFAgentDescription();
				agentDescription.setName(getAID());//on donne le nom de l'agent
				ServiceDescription serviceDescription=new ServiceDescription();
				serviceDescription.setType("transactions"); //on defnir le type de service
				serviceDescription.setName("vente-livre");//on definir le service
				agentDescription.addServices(serviceDescription);//on ajoute le service a publiee
				
				try {
					
					DFService.register(myAgent, agentDescription);//on publie le service dans l'annuaire
					
				} catch (Exception e) {
					// TODO: handle exception
					
					System.out.println("Probleme lors de la publication du service");
				}
				
			}
		});*/
		
		
		
	}
	 

	//executer avant la migration de l'agent vers un autre containeur
	@Override
	protected void beforeMove()
	{
		System.out.println("*****************");
		System.out.println("Avant migration.....");
		System.out.println("******************");
	}
	
	
	//executer apres la migration de l'agent vers un autre containeur
		@Override
		protected void afterMove()
		{
			System.out.println("*****************");
			System.out.println("apres migration.....");
			System.out.println("******************");
		}
		
		
	//executer avant la mort ou la suppresson de l'agent
	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
	
		System.out.println("*****************");
		System.out.println("I'm going to die.....");
		System.out.println("******************");
		
	}

	//methode appeler lorsqu'il seproduira un evenement dan l#interface graphiqu de l'agent
	//methode utiliser pour envoyer un message a l'agent acheteur
	@Override
	public void onGuiEvent(GuiEvent params) {
		// TODO Auto-generated method stub
		
		if(params.getType()==1) //on verifie le type de l'eveneement avant d'executer une action
		{
			System.out.println("methode OnGuiEvent executee");
			String livre=params.getParameter(0).toString();//on recupere l premier param de l'evenement
			ACLMessage aclMessage= new ACLMessage(ACLMessage.REQUEST); //on definir un ACLMessage avec l'art de communication Requet
			aclMessage.setContent(livre);//definition du contenu du message
			aclMessage.addReceiver(new AID("ACHETEUR",AID.ISLOCALNAME)); // envoi du message al'agent acheteur
			send(aclMessage); //envoi d'un message
		}
		
	}
	
}
