package utilityfeatures;

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNull;

/**
 * This class allow to get fields in JsonElement
 * (meaning a JsonMap), it return a JsonElement in output
 * @author Helloïs BARBOSA
 *
 */
public class GetJsonFields extends UnaryFunction<JsonElement, JsonElement>
{
  
  /**
   * The JSON key field in by the developer
   */
  private String m_jsonKey;
  
  /**
   * The JSON key correspond to time fields in JSON trace
   */
  public static final String JK_TIME = "time";

  /**
   * The JSON key correspond to type fields in JSON trace
   */
  public static final String JK_TYPE = "type";
  
  /**
   * The JSON key correspond to types fields in JSON trace
   */
  public static final String JK_TYPES = "types";

  /**
   * The JSON key correspond to state fields in JSON trace
   */
  public static final String JK_STATE = "state";
  
  /**
   * The JSON key correspond to id fields in JSON trace
   */
  public static final String JK_ID = "id";

  /**
   * The JSON key correspond to v1 fields in JSON trace (meaning temperature)
   */
  public static final String JK_TEMP = "v1";

  /**
   * The JSON key correspond to point fields in JSON trace
   * (meaning the coordinates of a point in 3D)
   */
  public static final String JK_POINT = "point";


  /**
   * Instantiate a new UnaryFunction
   * 
   * @param json_key
   *                The key that correspond to the field that we want to find
   */
  public GetJsonFields(String json_key)
  {
    super(JsonElement.class, JsonElement.class);
    m_jsonKey = json_key;
  }
  
  @Override
  public JsonElement getValue(JsonElement arg0)
  {
    if (arg0 instanceof JsonNull)
    {
      return JsonNull.instance;
    }
    JsonElement res = ((JsonMap) arg0).get(m_jsonKey);
    if (res == null)
    {
      return JsonNull.instance;
    }
    return res;
  }

}
