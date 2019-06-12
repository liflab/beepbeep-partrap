import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Pump;
import properties.HipPairDistance;
import properties.RedoAcquisition;
import utilityfeatures.ParseFileToJson;

public class Main
{


  public static void main(String[] args) {

    //We parse the Json file
    ParseFileToJson parse = new ParseFileToJson("long_trace.json");

    /** TEST PROPERTY 6 **/
    RedoAcquisition prop = new RedoAcquisition();
    /** FIN PROPERTY 6 **/

    Connector.connect(parse, prop);
    //Need a pump to automate the process
    Pump activator = new Pump(0);
    Connector.connect(prop, activator);

    Print p = new Print().setSeparator("\n\n");
    Connector.connect(activator, p);
    activator.run();
  }


}
