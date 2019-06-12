import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Pump;
import org.junit.Assert;
import org.junit.Test;
import properties.CheckPatientPosition;
import utilityfeatures.ParseFileToJson;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CheckPatientPositionTest {

    @Test
    public void ValidateJsonInputAssertIdHipCenter()
    {

        // Arrange
        CheckPatientPosition.MyDerivation.inputsList = new ArrayList<Object>();

        ParseFileToJson parse = new ParseFileToJson("long_trace.json");

        CheckPatientPosition prop = new CheckPatientPosition(2f);
        prop.ReconnectAfterGetKneeCenterPoint();

        Connector.connect(parse, prop);

        //Need a pump to automate the process
        Pump activator = new Pump(0);
        Connector.connect(prop, activator);

        Print p = new Print().setSeparator("\n\n");
        Connector.connect(activator, p);

        String a = "[-6.383143405234997,-114.61403514185226,-9.373316322692352]";
        //Act
        activator.run();
        Object b = CheckPatientPosition.MyDerivation.inputsList.get(1);

        //Assert
        Assert.assertEquals( a , b.toString());


    }

    @Test
    public void ValidateHipCenterPoint()
    {


        // Arrange
        CheckPatientPosition.MyDerivation.inputsList = new ArrayList<Object>();

        ParseFileToJson parse = new ParseFileToJson("long_trace.json");

        CheckPatientPosition prop = new CheckPatientPosition(2f);
        prop.ReconnectAfterGetHipCenterPoint();

        Connector.connect(parse, prop);

        //Need a pump to automate the process
        Pump activator = new Pump(0);
        Connector.connect(prop, activator);

        Print p = new Print().setSeparator("\n\n");
        Connector.connect(activator, p);

        String a = "[87.22969184462295,241.80033059322645,-217.02861872450953]";
        //Act
        activator.run();
        Object b = CheckPatientPosition.MyDerivation.inputsList.get(5);

        //Assert
        Assert.assertEquals( a , b.toString());
    }

}