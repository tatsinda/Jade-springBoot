package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

//creation du container contenant l'agent ConsumerAgent 
public class ConsumerContainer {
	
	public static void main(String[] args) throws ControllerException {
		
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl=new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");//on indique au conatinerou se trouve le main container
		AgentContainer container=runtime.createAgentContainer(profileImpl);
		//on cree une instance de l'agent dans le containeur et on le deploie
		AgentController agentController=container 
				.createNewAgent("Consumer", "agents.ConsumerAgent", new Object[] {"XML"});//on passe en parametre: le nom de l'agent, la classe ou se trouve l'agent et le troisieme ces params envoyer a l'agent
		agentController.start();//on deploie l'agent et le container
		
	}

}
