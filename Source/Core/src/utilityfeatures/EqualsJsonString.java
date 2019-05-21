package utilityfeatures;

import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonString;

/**
 * Function that check if a JsonElement (meaning a JsonString) and a String are equals
 * @author Helloïs BARBOSA
 *
 */
public class EqualsJsonString extends BinaryFunction<JsonElement, String, Boolean>
{
  /**
   * Instantiates a new BinaryFunction
   */
  public EqualsJsonString()
  {
    super(JsonElement.class, String.class, Boolean.class);
  }

  @Override
  public Boolean getValue(JsonElement arg0, String arg1)
  {
    if (arg0 instanceof JsonNull)
    {
      return false;
    }
    //We make the comparison
    return arg1.equals(((JsonString)arg0).stringValue());
  }

}
