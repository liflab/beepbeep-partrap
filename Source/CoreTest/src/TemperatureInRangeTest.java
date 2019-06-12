import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Pump;
import org.junit.Assert;
import org.junit.Test;
import properties.TemperatureInRange;
import utilityfeatures.ParseFileToJson;

import java.util.ArrayList;

public class TemperatureInRangeTest
{


  @Test
  public void ValidateJsonInputAssertIdHipCenter()
  {

    // Arrange
    TemperatureInRange.MyDerivation.inputsList = new ArrayList<Object>();

    ParseFileToJson parse = new ParseFileToJson("long_trace.json");

    TemperatureInRange prop = new TemperatureInRange(18, 35);
    prop.ReconnectAfterEqualId();

    Connector.connect(parse, prop);

    //Need a pump to automate the process
    Pump activator = new Pump(0);
    Connector.connect(prop, activator);

    Print p = new Print().setSeparator("\n\n");
    Connector.connect(activator, p);

    String a = "[87.22969184462295,241.80033059322645,-217.02861872450953]";
    //Act
    activator.run();
    Object b = TemperatureInRange.MyDerivation.inputsList.get(128);

    //Assert
    Assert.assertEquals( true , b);

  }
  gitk
}