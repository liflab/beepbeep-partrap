import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Pump;
import properties.CameraConnectedChecker;
import utilityfeatures.ParseFileToJson;

/**
 * To test property
 * @author Helloïs BARBOSA
 *
 */
public class Main {

	/**
	 * The main method to run the property
	 * @param args
	 */
	public static void main(String[] args) {
		
		//We parse the Json file
		ParseFileToJson parse = new ParseFileToJson("long_trace.json");
		
		/** TEST PROPERTY 1 **/
		//RedoAcquisition prop = new RedoAcquisition();
		/** FIN PROPERTY 1 **/
		
		/** TEST PROPERTY 2 **/
		//TemperatureInRange prop = new TemperatureInRange(41f, 42f);
		/** FIN PROPERTY 2 **/
		
		/** TEST PROPERTY 15 **/
		//CheckTrackersConnection prop = new CheckTrackersConnection();
		/** FIN PROPERTY 15 **/
		
		/** TEST PROPERTY 4 **/
		 //CheckPatientPosition prop = new CheckPatientPosition(2f);
		/** FIN PROPERTY 4 **/
		
		/** TEST PROPERTY 10 **/
		CameraConnectedChecker prop = new CameraConnectedChecker();
		/** FIN PROPERTY 10 **/
		
		Connector.connect(parse, prop);
		//Need a pump to automate the process
		Pump activator = new Pump(0);
		Connector.connect(prop, activator);
			
		Print p = new Print().setSeparator("\n\n");
		Connector.connect(activator, p);
		activator.run();
	}
	
}
