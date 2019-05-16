import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tmf.Pump;
import utilityFeatures.EqualsJsonString;
import utilityFeatures.GetJsonFields;
import utilityFeatures.ParseFileToJson;

public class Main {

    public static void main(String[] args) {
        String s_idValue = "HipCenter";

        ParseFileToJson myParser = new ParseFileToJson("long_trace.json");

        Fork baseFork = new Fork(2);
        Connector.connect(myParser, baseFork);

        ApplyFunction getId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
        Connector.connect(baseFork, 0, getId, 0);

        ApplyFunction equalId = new ApplyFunction(new EqualsJsonString());
        Connector.connect(getId, 0, equalId, 0);


        ApplyFunction constId = new ApplyFunction(new Constant(s_idValue));
        Connector.connect(constId, 0, equalId, 1);
        Print p = new Print().setSeparator("\n\n");


        Filter baseFilter = new Filter();
        Connector.connect(baseFork,1,baseFilter,0);
        Connector.connect(equalId,0, baseFilter,1);

         Pump myPump = new Pump(0);

        Connector.connect(baseFilter, myPump);
        Connector.connect(myPump, p);
        myPump.run();

        //Filter

        //        Connector.connect(myParser, baseFork);
//        ApplyFunction m_constId = new ApplyFunction(new Constant(s_idValue));
//
//        Connector.connect(baseFork, 0, getId, 0);
//
//        Connector.connect(getId, equalId);
//
//
//        Print p = new Print().setSeparator("\n\n");
//
//        Pump myPump = new Pump(0);
//
//        Connector.connect(getId, myPump);
//        Connector.connect(myPump, p);
//        myPump.run();


        //        //HipDistance prop = new HipDistance();
//        Connector.connect(myParser, prop);
//
//        Pump myPump = new Pump(0);
//        Connector.connect(prop, myPump);
//
//        Print p = new Print().setSeparator("\n\n");
//        Connector.connect(myPump, p);
//        myPump.run();


    }
}

