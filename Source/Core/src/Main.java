import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Pump;
import hipDistance.HipDistance;
import utilityFeatures.ParseFileToJson;

public class Main {

    public static void main(String[] args) {

        ParseFileToJson myParser = new ParseFileToJson("long_trace.json");

        HipDistance prop = new HipDistance();
        Connector.connect(myParser, prop);

        Pump myPump = new Pump(0);
        Connector.connect( prop, myPump);

        Print p = new Print().setSeparator("\n\n");
        Connector.connect(myPump, p);
        myPump.run();



    }
}

