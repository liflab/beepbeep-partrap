package utilityFeatures;

import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonString;

/**
 * Function that check if a JsonElement (meaning a JsonString) conatin a String passed as a parameter
 * @author Helloïs BARBOSA
 *
 */
public class ContainJsonString extends BinaryFunction<JsonElement, String, Boolean>{

	/**
	 * Instantiates a new BinaryFunction
	 */
	public ContainJsonString() {
		super(JsonElement.class, String.class, Boolean.class);
	}

	@Override
	public Boolean getValue(JsonElement arg0, String arg1) {
		if(arg0 instanceof JsonNull) {
			return false;
		}
		//We make the comparison
		return ((JsonString)arg0).stringValue().contains(arg1);
	}

}
