package agents;

import java.util.Random;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class VendeurAgent extends GuiAgent {

	private VendeurGui gui;
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
		//on couple l'agent et son interface grapique
				if(getArguments().length==1)
				{
					gui=(VendeurGui) getArguments()[0];
					gui.vendeurAgent=this;
				}
				
				//on donne des comportement  l'agent
				
				ParallelBehaviour parallelBehaviour= new ParallelBehaviour();
				addBehaviour(parallelBehaviour);
				
				//comportement permettant a l'agent de publier ses services dans l'annuaire
				parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
					
					@Override
					public void action() {
						// TODO Auto-generated method stub
						DFAgentDescription agentDescription=new DFAgentDescription();//objet permettant de doner une dscription sur l'agent
						agentDescription.setName(getAID()); //nom de l'agent
						ServiceDescription serviceDescription=new ServiceDescription();
						serviceDescription.setType("transaction"); //categorie auquel appartient le service
						serviceDescription.setName("vente-livres");//nom du service
						agentDescription.addServices(serviceDescription);//on ajoute le service
						
						try {
							DFService.register(myAgent, agentDescription);
						} catch (FIPAException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//on publie le service dans l'annuaire
						
					}
				});
				
				
				
				
				//Behaviour permettant de recevoir le message de l'agent et lui envoyer un reply
				parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
					
					@Override
					public void action() {
						// TODO Auto-generated method stub
						ACLMessage aclMessage=receive();
						if(aclMessage!=null)
						{
							gui.logMessage(aclMessage); //envoi du message vers l'interface graphique de l'agent
							
							//on effectue les action en foction de l'art de communication
							
							switch (aclMessage.getPerformative()) {
							
							case ACLMessage.CFP:
								
								//on envoi un repy a l'expediteur
								
								ACLMessage reply=aclMessage.createReply();
								reply.setPerformative(aclMessage.PROPOSE); //message avec l'art de communication propose
								
								reply.setContent(String.valueOf(500+new Random().nextInt(1000)));//envoi d'un nombre compris entre 500 et 1000
								send(reply);
								
								break;
								
							case ACLMessage.ACCEPT_PROPOSAL:
								
								//on envoi un reply avec l'art agree
								ACLMessage aclMessage2 = aclMessage.createReply();
								aclMessage2.setPerformative(ACLMessage.AGREE);
								send(aclMessage2);
								
								break;
								
							default:
								break;
							}
							
						}
						else block();
						
					}
				});
				
		
	}
	
	
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//methode executee avant la mort de l'agent
	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		//permettant de supprimer tous les service de l'agent dans l'annuaire  avant sa mort 
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
