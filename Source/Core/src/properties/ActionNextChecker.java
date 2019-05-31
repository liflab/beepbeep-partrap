package properties;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.util.Booleans;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import utilityfeatures.EqualsJsonString;
import utilityfeatures.GetJsonFields;

/**
 * 
 * @author Helloïs BARBOSA
 *
 */
public class ActionNextChecker extends GroupProcessor
{
  
  /**
   * The Fork to split stream in two parts
   */
  private Fork m_mainFork;
  
  
  /**
   * The Filter that allow to let pass just right object
   * with id: s_idEnterValue or id: s_idActionNextValue
   */
  private Filter m_mainFilter;
  
  /**
   * The ApplyFunction allow to get the field with the key: "id"
   */
  private ApplyFunction m_getId;
  
  /**
   * The Fork to check if JsonElement id is: s_idEnterValue or s_idActionNextValue
   */
  private Fork m_idFork;
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constActionNext;
  
  /**
   * The ApplyFunction allow to check if desired (m_constActionNext) id and JSON id are equals
   */
  private ApplyFunction m_equalActionNext;
  
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constEnter;
  
  /**
   * The ApplyFunction allow to check if desired (m_constEnter) id and JSON id are equals
   */
  private ApplyFunction m_equalEnter;
  
  /**
   * The ApplyFunction to check that Object passed to the m_mainFilter is a right object
   */
  private ApplyFunction m_logicOr;
  
  /**
   * The ApplyFunction that call the inner UnaryFunction: PreventActionNext to
   * check if an event with id: s_idActionNextValue is prevent from an other event
   * with id: s_idEnterValue for s_idEnterValue millisecond
   */
  private ApplyFunction m_preventActionNext;

  /**
   * The Double that allow to save the last s_idEnterValue time value
   */
  private Double m_timeBeforeActionNext = null;
  
  /**
   * The String of the ActionNext id value field
   */
  private static String s_idActionNextValue = "ActionNext";
  
  /**
   * The String of the EnterState id value field
   */
  protected static String s_idEnterValue = "EnterState";
  
  /**
   * The int of the time that allow to prevent ActionNext event in milliseconds
   */
  private static int s_timePrevent = 100;
  
  
  /**
   * 
   */
  public ActionNextChecker()
  {
    super(1, 1);
    
    m_mainFork = new Fork(2);
    
    //First branch of the m_mainFork (output: 0): here we just pass
    //the JsonElement to: m_actionNextFilter
    m_mainFilter = new Filter();
    Connector.connect(m_mainFork, 0, m_mainFilter, 0);
    
    //Second branch of the m_mainFork (output: 1): here we check if
    //the JsonElement is: s_idActionNextValue or s_idEnterValue
    m_getId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_mainFork, 1, m_getId, 0);
    
    m_idFork = new Fork(2);
    Connector.connect(m_getId, m_idFork);
    
    //First branch of the m_idFork (output: 0): here we check if
    //the JsonElement is: s_idActionNextValue
    m_constActionNext = new ApplyFunction(new Constant(s_idActionNextValue));
    
    m_equalActionNext = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_idFork, 0, m_equalActionNext, 0);
    Connector.connect(m_constActionNext, 0, m_equalActionNext, 1);
    
    
    //Second branch of the m_idFork (output: 1): here we check if
    //the JsonElement is: s_idEnterValue
    m_constEnter = new ApplyFunction(new Constant(s_idEnterValue));
    
    m_equalEnter = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_idFork, 1, m_equalEnter, 0);
    Connector.connect(m_constEnter, 0, m_equalEnter, 1);
    
    m_logicOr = new ApplyFunction(Booleans.or);
    Connector.connect(m_equalActionNext, 0, m_logicOr, 0);
    Connector.connect(m_equalEnter, 0, m_logicOr, 1);
    
    Connector.connect(m_logicOr, 0, m_mainFilter, 1);
    
    m_preventActionNext = new ApplyFunction(new PreventActionNext());
    Connector.connect(m_mainFilter, m_preventActionNext);
    
    
    this.addProcessors(m_mainFork, m_mainFilter, m_getId, m_idFork, m_constActionNext,
        m_equalActionNext, m_constEnter, m_equalEnter, m_preventActionNext, m_logicOr);
    this.associateInput(0, m_mainFork, 0);
    this.associateOutput(0, m_preventActionNext, 0);
  }
  
  
  /**
   * This inner class allow to check if id: s_idEnterValue prevent
   * id: s_idActionNextValue for s_timePrevent
   * @author Helloïs BARBOSA
   *
   */
  private class PreventActionNext extends UnaryFunction<JsonElement, Boolean>
  {

    /**
     * Instantiates a new UnaryFunction
     */
    public PreventActionNext()
    {
      super(JsonElement.class, Boolean.class);
    }

    @Override
    public Boolean getValue(JsonElement arg0)
    {
      JsonMap map = (JsonMap) arg0;
      Double time = ((JsonNumber)map.get(GetJsonFields.JK_TIME)).numberValue().doubleValue();
      if (((JsonString)map.get(GetJsonFields.JK_ID)).stringValue().equals(s_idEnterValue))
      {
        m_timeBeforeActionNext = time;
      }
      else
      {
        if (m_timeBeforeActionNext != null)
        {
          return time - m_timeBeforeActionNext > s_timePrevent;
        }
      }
      return true;
    }
    
  }
  
}