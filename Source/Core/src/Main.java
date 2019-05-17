import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tmf.Pump;
import ca.uqac.lif.cep.tmf.Trim;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;
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
        Connector.connect(baseFork, 1, baseFilter, 0);
        Connector.connect(equalId, 0, baseFilter, 1);


        //property coding starts here

        // Fork propertyFork = new Fork(2);
        ApplyFunction getPoint = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_POINT));
        Connector.connect(baseFilter, getPoint);


        Trim myTrim = new Trim(1);
        Connector.connect()


        ApplyFunction myFunct = new ApplyFunction();

        Pump myPump = new Pump(0);

        Connector.connect(firstParser, myPump);
        Connector.connect(myPump, p);
        myPump.run();


    }

    private void parseString() {


    }


    private class CalculateDistance extends BinaryFunction<JsonElement, JsonElement, Float> {


        public CalculateDistance() {
            super(JsonElement.class, JsonElement.class, Float.class);
        }

        @Override
        public Float getValue(JsonElement jsonElement, JsonElement jsonElement2) {

            if (jsonElement != null || jsonElement2 != null) {
                JsonList list1 = (JsonList) jsonElement;
                JsonList list2 = (JsonList) jsonElement2;

                Float[] point1 = new Float[]{((JsonNumber) list1.get(0)).numberValue().floatValue(), ((JsonNumber) list1.get(1)).numberValue().floatValue(), ((JsonNumber) list1.get(2)).numberValue().floatValue()};
                Float[] point2 = new Float[]{((JsonNumber) list2.get(0)).numberValue().floatValue(), ((JsonNumber) list2.get(1)).numberValue().floatValue(), ((JsonNumber) list2.get(2)).numberValue().floatValue()};

            }
            return Float.valueOf(0);
        }
    }
}

