package hipDistance;

import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import utilityFeatures.GetJsonFields;

public class HipDistance extends GroupProcessor {

    private ApplyFunction getId;

    public HipDistance() {
        super(1,1);

        getId = new ApplyFunction( new GetJsonFields(GetJsonFields.))



    }
}
