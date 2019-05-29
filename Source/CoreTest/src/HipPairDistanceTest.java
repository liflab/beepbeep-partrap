import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Pump;
import ca.uqac.lif.json.JsonMap;
import org.junit.Test;
import properties.HipPairDistance;
import utilityfeatures.ParseFileToJson;
import org.junit.Assert;
import org.junit.Test;


public class HipPairDistanceTest
{


  @Test
  public void toto()
  {
    // Arrange
    ParseFileToJson parse = new ParseFileToJson("long_trace.json");

    HipPairDistance prop = new HipPairDistance(2f);
    prop.Reconnect();
    /** FIN PROPERTY 6 **/

    Connector.connect(parse, prop);
    //Need a pump to automate the process
    Pump activator = new Pump(0);
    Connector.connect(prop, activator);

    Print p = new Print().setSeparator("\n\n");
    Connector.connect(activator, p);

//Act
    activator.run();
    JsonMap a = new JsonMap();
    a
    //JsonMap a = '{\"id\":\"HipCenter\",\"time\":-6.2135683761E10,\"point\":[87.22969184462295,241.80033059322645,-217.02861872450953]}';

    //Assert
    Assert.assertEquals( HipPairDistance.MyDerivation.inputsList.get(3), HipPairDistance.MyDerivation.inputsList.get(3));


  }
}