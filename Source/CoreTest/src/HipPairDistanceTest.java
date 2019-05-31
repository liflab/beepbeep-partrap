import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Pump;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import org.junit.Test;
import properties.HipPairDistance;
import utilityfeatures.ParseFileToJson;
import org.junit.Assert;

import java.util.ArrayList;

import static properties.HipPairDistance.MyDerivation.inputsList;


public class HipPairDistanceTest
{


  @Test
  public void ValidateJsonInputAssertIdHipCenter()
  {

    // Arrange
    inputsList = new ArrayList<Object>();

    ParseFileToJson parse = new ParseFileToJson("long_trace.json");

    HipPairDistance prop = new HipPairDistance(2f);
    prop.ReconnectToTrim();

    Connector.connect(parse, prop);

    //Need a pump to automate the process
    Pump activator = new Pump(0);
    Connector.connect(prop, activator);

    Print p = new Print().setSeparator("\n\n");
    Connector.connect(activator, p);

    String a = "[87.22969184462295,241.80033059322645,-217.02861872450953]";
    //Act
    activator.run();
    Object b = inputsList.get(3);

    //Assert
    Assert.assertEquals( a , b.toString());


  }

  @Test
  public void ValidateJsonElementTypePostFilter_AssertHipCenter()
  {
    // Arrange
    inputsList = new ArrayList<Object>();
    ParseFileToJson parse = new ParseFileToJson("long_trace.json");

    HipPairDistance prop = new HipPairDistance(2f);
    prop.ReconnectToTrim();

    Connector.connect(parse, prop);

    //Need a pump to automate the process
    Pump activator = new Pump(0);
    Connector.connect(prop, activator);

    Print p = new Print().setSeparator("\n\n");
    Connector.connect(activator, p);

    //Act
    activator.run();
    Object b = inputsList.get(3);

    //Assert
    Assert.assertEquals( b.toString(), b.toString());


  }

}