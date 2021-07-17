package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

//creation d'un container simple
public class SimpleContainer {
	
	public static void main(String[] args) throws ControllerException {
		
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl=new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");//on indique au conatiner a quelle adresse ip  se trouve le main container
		AgentContainer container=runtime.createAgentContainer(profileImpl);
		container.start();
		
	}

}
