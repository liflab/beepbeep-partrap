package customProcessors;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.SynchronousProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Constant;
import utilityFeatures.EqualsJsonString;
import utilityFeatures.GetJsonFields;

import java.util.Queue;

public class distanceCalculator extends SynchronousProcessor {
    public distanceCalculator() {
        super(2, 1);
    }

    @Override
    protected boolean compute(Object[] objects, Queue<Object[]> queue) {

        ApplyFunction getId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_POINT));
        ApplyFunction equalId = new ApplyFunction(new EqualsJsonString());
        Connector.connect(getId, 0, equalId, 0);

       // Pullable P = equal

        //return outputs.add(new Object{};
        //        ApplyFunction cpmstObject1 = new ApplyFunction(new Constant(objects[0]) );
        return false;
    }

    @Override
    public Processor duplicate(boolean b) {
        return null;
    }


    private float calculateDistance(Object[] objects) {


        //  return Math.sqrt(Math.pow(objects[0] - p.getxCoord(), 2) + Math.pow(y - p.getyCoord(), 2) + Math.pow(z - p.getzCoord(), 2));

        return 1;
    }
}
