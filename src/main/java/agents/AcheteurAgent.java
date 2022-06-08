package agents;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

//Agent qui se charge d'acheter le livre souhaiter par le client
public class AcheteurAgent extends GuiAgent {

	protected AcheteurGui gui; //reference vers l'interface graphique
	protected AID[] vendeurs; //tableau qui contiendra la liste des vendeurs
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
		//on couple l'agent et son interface grapique
		if(getArguments().length==1)
		{
			gui=(AcheteurGui) getArguments()[0];
			gui.acheteurAgent=this;
		}
		
		//on donne des comportement  l'agent
		
		ParallelBehaviour parallelBehaviour= new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		 
		//Comportement qui permettant de recuprer dans l'annuaire la liste des agent offrant un service 
		parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,50000) {
			//methode s'executant toute les  5 secondes
			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				
				DFAgentDescription dfAgentDescription=new DFAgentDescription();
				ServiceDescription serviceDescription=new ServiceDescription();
				serviceDescription.setType("transaction"); //type du service a rechercher
				serviceDescription.setName("vente-livres");//nom du service a rehercher
				dfAgentDescription.addServices(serviceDescription);//on ajoute le service
				
				try {
						
					DFAgentDescription[] results= DFService.search(myAgent, dfAgentDescription);//dans l'annuaire on recherche les agent correspondant au service
					//pour chauque agent correspondant au service on stocke son nom
					vendeurs= new AID[results.length];
					for(int i=0; i<vendeurs.length; i++)
					{
						vendeurs[i]=results[i].getName(); //on recupere le nom des agents
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
		});
		
		
	
		//Behaviour permettant de recevoir le message de l'agent et lui envoyer un reply
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			private int counter=0; //variable pour compter le nomtre de message avec l'art proposal envoye
			private List<ACLMessage> replies=new ArrayList<ACLMessage>(); //liste pour garder les messages des agents
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				
				//on filtre la reception des message en fonction ducontext ou du language utiliser
				/*MessageTemplate messageTemplate=
						MessageTemplate.or(
								MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
								MessageTemplate.MatchLanguage("fr"));
				
				
				ACLMessage aclMessage=receive(messageTemplate);*/
				//*************************************
				
				//on filtre la reception des message en fonction ducontext et on accepte aucun autres messages d'autres contextes
				MessageTemplate messageTemplate=
						MessageTemplate.or(
								MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
								MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
										MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.AGREE),MessageTemplate.MatchPerformative(ACLMessage.REFUSE)
														)));
				
				
				ACLMessage aclMessage=receive(messageTemplate);
				if(aclMessage!=null)
				{
					//on effectue des actions en fonctions du type de performative recu par l'agent acheteur
					switch (aclMessage.getPerformative()) {
					
					case ACLMessage.REQUEST:
						
						String livre=aclMessage.getContent();
						ACLMessage aclMessage2=new ACLMessage(ACLMessage.CFP);//on definir un message avc l'art de communication : call for propose
						aclMessage2.setContent(livre);
						for(AID aid:vendeurs)
						{
							aclMessage2.addReceiver(aid);
						}
						send(aclMessage2);
							
						
						break;
						
					case ACLMessage.PROPOSE:
						++counter;
						replies.add(aclMessage);//ajout du message a la liste
						
						if(counter==vendeurs.length)
						{
							ACLMessage meilleurOffre=replies.get(0); //on definir la meilleur offre
							double mini=Double.parseDouble(meilleurOffre.getContent());//on recupere le contenu de la meilleur offre
							
							for(ACLMessage offre:replies)
							{
								double price=Double.parseDouble(offre.getContent());//on recupre le contenu d'un message dans la liste
								
								if(price<mini) //on recupere la meillur offre et le meileur prix
								{
									meilleurOffre = offre;
									mini=price;
								}
							}
							
							ACLMessage aclMessageAccept=meilleurOffre.createReply(); //on cree un message de reply vrs l'agent vendeur qui a la meillur offre
							aclMessage.setPerformative(ACLMessage.ACCEPT_PROPOSAL); //l'art de communication
							send(aclMessageAccept);
						}
						
						break;
					case ACLMessage.AGREE:
						
						ACLMessage aclMessage3= new ACLMessage(ACLMessage.CONFIRM);
						aclMessage3.addReceiver(new AID("Consumer",AID.ISLOCALNAME));
						aclMessage3.setContent(aclMessage.getContent());
						send(aclMessage3);
						
						break;
					case ACLMessage.REFUSE:
						
						break;

					default:
						
						break;
					}
					
					
					
					
					String livre=aclMessage.getContent();
					gui.logMessage(aclMessage); //envoi du message vers l'interface graphique de l'agent
					
					//on envoi un reply
					ACLMessage reply=aclMessage.createReply();
					reply.setContent("OK pour "+aclMessage.getContent());//contenu du reply
					send(reply);//on envoi
					
					//envoi du message a l'agent vendeur pour la proposition
					ACLMessage aclMessage3=new ACLMessage(ACLMessage.CFP);
					aclMessage3.setContent(livre);
					aclMessage3.addReceiver(new AID("VENDEUR",AID.ISLOCALNAME));
					send(aclMessage3);
					
					
				}
				else block();
				
			}
		});
		
	}
	
	
	
	
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
