package hipDistance;

import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import utilityFeatures.GetJsonFields;

public class HipDistance extends GroupProcessor {

    private ApplyFunction m_getId;

    private int maxDistance = 4;

    public HipDistance() {
        super(1,1);

        m_getId = new ApplyFunction( new GetJsonFields(GetJsonFields.JK_ID));



    }
}
