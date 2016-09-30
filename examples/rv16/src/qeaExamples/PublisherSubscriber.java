package qeaExamples;

import qea.creation.QEABuilder;
import qea.structure.intf.QEA;
import static qea.structure.impl.other.Quantification.*;

public class PublisherSubscriber {

	/*
	 * The PublisherSubscriber property states that
	 * For every publisher there exists a subscriber that receives all messages
	 * published by that publisher.
	 * 
	 * There are two events:
	 *   - publish(pub,msg)
	 *   - receive(sub,msg)
	 */
	
	public static QEA make(){
		
		// Create Builder
		QEABuilder builder = new QEABuilder("PublisherSubscriber");
		
		// Declare variables
		int pub = -1;
		int sub = -2;
		int msg = -3;
		
		// Add Quantifications		Forall pub Exists sub Forall msg
		builder.addQuantification(FORALL, pub);
		builder.addQuantification(EXISTS, sub);
		builder.addQuantification(FORALL, msg);
		
		// Declare events
		int publish = 1;
		int receive = 2;
		
		// Declare states
		int start = 1;
		int published = 2;
		int received = 3;
		builder.addFinalStates(1,3);
		
		
		// Add transitions
		builder.addTransition(start, publish, new int[]{pub,msg},published);
		builder.addTransition(published,receive, new int[]{sub,msg},received);
		
		builder.setAllSkipStates();
		
		return builder.make();
	}
	
}
