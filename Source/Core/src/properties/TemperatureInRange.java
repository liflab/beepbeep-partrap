package properties;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.util.Booleans;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonNumber;
import utilityfeatures.EqualsJsonString;
import utilityfeatures.GetJsonFields;


/**
 * 
 * This class allows to verify the second property
 * of the project(refer to the pdf included in the project):
 *          absence_of Temp t where not ( a <= t . t1 and t . t1 < b)
 *          
 * It take JsonElements and give a Boolean
 * @author Helloïs BARBOSA
 *
 */
public class TemperatureInRange extends GroupProcessor
{

  /**
   * The Fork to split stream in two parts
   */
  private Fork m_fork;

  /**
   * The ApplyFunction allow to get the field with the key: "id"
   */
  private ApplyFunction m_getId;

  /**
   * The ApplyFunction allow to define the constant id that allow
   * to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constId;

  /**
   * The ApplyFunction allow to check if desired id and JSON id are equals
   */
  private ApplyFunction m_equalId;

  /**
   * The ApplyFunction allow to get the field with the key:
   * "v1" that correspond to the value of the temperature
   */
  private ApplyFunction m_getTemperature;

  /**
   * The ApplyFunction allow to define the constant of lowest and high value of range temperature
   */
  private ApplyFunction m_constRange;

  /**
   * The ApplyFunction allow to check if the temperature is between a range
   * (meaning that is, by calling the inner BinaryFunction: IsBetween)
   */
  private ApplyFunction m_checkIsBetween;

  /**
   * The ApplyFunction allow to check if the both condition of
   * this property are verified and give the final result
   */
  private ApplyFunction m_logicAnd;

  /**
   * The String of the value field
   */
  private static String s_idValue = "Temp";

  /**
   * Instantiate the GroupProcessor which this class extends
   * @param lowest_value
   *                    The lowest value that the temperature can take
   * @param highest_value
   *                    The highest value that the temperature can take (this value not included)
   */
  public TemperatureInRange(float lowest_value, float highest_value)
  {
    super(1, 1);

    m_fork = new Fork(2);


    //First branch of the fork (output: 0): here we check that the event ID is the right one
    m_getId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_fork, 0, m_getId, 0);

    m_constId = new ApplyFunction(new Constant(s_idValue));

    m_equalId = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getId, 0, m_equalId, 0);
    Connector.connect(m_constId, 0, m_equalId, 1);


    //Second branch of the fork (output: 1): here we check
    //that the temperature field is the right one
    m_getTemperature = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_TEMP));
    Connector.connect(m_fork, 1, m_getTemperature, 0);

    m_constRange = new ApplyFunction(new Constant(new Float[] {lowest_value, highest_value}));

    m_checkIsBetween = new ApplyFunction(new IsBetween());
    Connector.connect(m_constRange, 0, m_checkIsBetween, 0);
    Connector.connect(m_getTemperature, 0, m_checkIsBetween, 1);


    //We apply an AND logic between the two results because we
    //need two answer to be true to verify this property
    m_logicAnd = new ApplyFunction(Booleans.and);
    Connector.connect(m_equalId, 0, m_logicAnd, 0);
    Connector.connect(m_checkIsBetween, 0, m_logicAnd, 1);


    this.addProcessors(m_fork, m_getId, m_constId, m_equalId,
        m_getTemperature, m_constRange, m_checkIsBetween, m_logicAnd);
    this.associateInput(0, m_fork, 0);
    this.associateOutput(0, m_logicAnd, 0);
  }


  /**
   * This inner class is a BinaryFunction that it take a Float[]
   * (meaning a range of two value: [a, b]) and a JsonElement correspond
   * to the temperature (x) value and give a Boolean (a <= x < b)
   * @author Helloïs BARBOSA
   *
   */
  private class IsBetween extends BinaryFunction<Float[], JsonElement, Boolean>
  {

    /**
     * Instantiates a new BinaryFunction
     */
    public IsBetween()
    {
      super(Float[].class, JsonElement.class, Boolean.class);
    }

    @Override
    public Boolean getValue(Float[] arg0, JsonElement arg1)
    {
      if (!(arg1 instanceof JsonNull))
      {
        Float tempValue = ((JsonNumber)arg1).numberValue().floatValue();
        System.out.println("Temp value: " + tempValue);
        System.out.println("Res: " + (tempValue >= arg0[0] && tempValue < arg0[1]));
        return (tempValue >= arg0[0] && tempValue < arg0[1]);
      }
      return false;
    }

  }
  
}