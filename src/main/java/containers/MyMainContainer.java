package containers;


import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

//le container main
public class MyMainContainer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Runtime runtime=Runtime.instance();//creation d'une instance de Jade
		ProfileImpl profileImpl=new ProfileImpl();//on definir un main ontainer
		profileImpl.setParameter(ProfileImpl.GUI, "true");//on definie ls param du container, au demarrge on dmaare l'interface graphique de Jad
		
		AgentContainer mainContainer=runtime.createMainContainer(profileImpl);//cretaion du mai container
		mainContainer.start();//on demmarrre le container*/

	}

}
