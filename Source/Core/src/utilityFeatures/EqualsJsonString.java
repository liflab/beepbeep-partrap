package utilityFeatures;

import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonString;

/**
 * Function that check if a JsonElement (meaning a JsonString) and a String are equals
 * @author Helloïs BARBOSA
 *
 */
public class EqualsJsonString extends BinaryFunction<JsonElement, String, Boolean>{

	/**
	 * Instantiates a new BinaryFunction
	 */
	public EqualsJsonString() {
		super(JsonElement.class, String.class, Boolean.class);
	}

	@Override
	public Boolean getValue(JsonElement arg0, String arg1) {
		if(arg0 instanceof JsonNull) {
			return false;
		}
		
		//We remove: " " at the beginning and at the and of JsonElement
		String jsonValue = ((JsonString)arg0).toString();
		jsonValue =  jsonValue.substring(1, jsonValue.length()-1);
		
		//We make the comparison
		System.out.println("Compare: " + jsonValue + " and " + arg1);
		return arg1.equals(((JsonString)arg0).toString());
	}

}
