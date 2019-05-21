import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tmf.Pump;
import ca.uqac.lif.cep.tmf.Trim;
import ca.uqac.lif.cep.util.Numbers;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;
import properties.HipPairDistance;
import utilityFeatures.EqualsJsonString;
import utilityFeatures.GetJsonFields;
import utilityFeatures.ParseFileToJson;
import utilityFeatures.UtilityMethods;

public class Main {

    public static void main(String[] args) {


        ParseFileToJson myParser = new ParseFileToJson("long_trace.json");

        HipPairDistance proc = new HipPairDistance(1F);
        Connector.connect(myParser, proc);

        Print p = new Print();
        Pump myPump = new Pump(0);

        Connector.connect(proc, myPump);
        Connector.connect(myPump, p);
        myPump.run();


    }

    private void parseString() {


    }


}

