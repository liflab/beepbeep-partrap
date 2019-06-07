package properties;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.tmf.Pump;
import org.junit.Assert;
import org.junit.Test;
import utilityfeatures.ParseFileToJson;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RedoAcquisitionTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }


    @org.junit.Test
    public void ValidateId_AssertHipCenter()
    {
        //Arrange
        RedoAcquisition.MyDerivation.inputsList = new ArrayList<Object>();

        ParseFileToJson parse = new ParseFileToJson("long_trace.json");

        RedoAcquisition prop = new RedoAcquisition();
        prop.ReconnectToEqualId();

        Connector.connect(parse, prop);

        //Need a pump to automate the process
        Pump activator = new Pump(0);
        Connector.connect(prop, activator);

        Print p = new Print().setSeparator("\n\n");
        Connector.connect(activator, p);

        //Act
        activator.run();
        Object b = RedoAcquisition.MyDerivation.inputsList.get(3);

        //Assert
        Assert.assertEquals(  "\"HipCenter\"" , b.toString());
    }


    @Test
    public void ValidateState()
    {
        //Arrange
        RedoAcquisition.MyDerivation.inputsList = new ArrayList<Object>();

        ParseFileToJson parse = new ParseFileToJson("long_trace.json");

        RedoAcquisition prop = new RedoAcquisition();
        prop.ReconnectToEqualRedo();

        Connector.connect(parse, prop);

        //Need a pump to automate the process
        Pump activator = new Pump(0);
        Connector.connect(prop, activator);

        Print p = new Print().setSeparator("\n\n");
        Connector.connect(activator, p);

        //Act
        activator.run();
        Object b = RedoAcquisition.MyDerivation.inputsList.get(47);

        //Assert
        Assert.assertEquals(  "\"mainCasp.TrackingConnection.TrackersConnection\"" , b.toString());

    }
}