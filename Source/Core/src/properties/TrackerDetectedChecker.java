package properties;

import java.util.ArrayList;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.util.Booleans;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;
import utilityfeatures.EqualsJsonString;
import utilityfeatures.GetJsonFields;

/**
 * This class allows to verify the thirteenth property of the project
 * (refer to the pdf included in the project):
 * 
 * before each EnterState e
 * where e. state == " mainCasp.TrackingConnection.TrackersVisibCheck ",
 * given last SearchTrackers st ,
 * forall ty in st.types ,
 * occurrence_of TrackerDetected td where td.ty == ty
 * 
 * @author Helloïs BARBOSA
 *
 */
public class TrackerDetectedChecker extends GroupProcessor
{
  
  /**
   * The Fork to split stream in many parts
   **/
  private Fork m_mainFork;
  
  /**
   * The ApplyFunction allow to get the field with the key: "id"
   */
  private ApplyFunction m_getId;
  
  /**
   * The Fork to split id stream in many parts to sorts JsonElement by id
   */
  private Fork m_idFork;
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constSearchId;

  /**
   * The ApplyFunction allow to check if desired (s_idSearchValue) id and JSON id are equals
   */
  private ApplyFunction m_equalSearch;
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constTrackDetectedId;

  /**
   * The ApplyFunction allow to check if desired (s_idTrackerDetectedValue)
   * id and JSON id are equals
   */
  private ApplyFunction m_equalTrackDetected;
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constEnterId;

  /**
   * The ApplyFunction allow to check if desired (s_idEnterValue) id and JSON id are equals
   */
  private ApplyFunction m_equalEnter;
  
  /**
   * The ApplyFunction that allow to do a logic or between
   * SearchTracker result and TrackerDetected result
   */
  private ApplyFunction m_firstLogicOr;
  
  /**
   * The ApplyFunction allow to get the field with the key: "state"
   */
  private ApplyFunction m_getState;
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "state"
   */
  private ApplyFunction m_constEnterState;

  /**
   * The ApplyFunction allow to check if desired (s_stateEnterValue) id and JSON id are equals
   */
  private ApplyFunction m_equalEnterState;
  
  /**
   * The ApplyFunction that allow to do a logic and between Enter state and Enter id results
   */
  private ApplyFunction m_logicAnd;
  
  /**
   * The ApplyFunction that allow to do a logic or between
   * the first logic or result and Enter result
   */
  private ApplyFunction m_secondLogicOr;
  
  /**
   * The Filter that allow to let pass just right object
   */
  private Filter m_mainFilter;
  
  
  /**
   * 
   */
  private ApplyFunction m_checkTracker;
  
  /**
   * The String of the SearchTrackers id value field
   */
  private static String s_idSearchValue = "SearchTrackers";
  
  /**
   * The String of the TrackerDetected id value field
   */
  private static String s_idTrackDetectedValue = "TrackerDetected";
  
  /**
   * The String of the EnterState id value field
   */
  private static String s_idEnterValue = "EnterState";
  
  /**
   * The String of the EnterState states value field
   */
  private static String s_stateEnterValue = "mainCasp.TrackingConnection.TrackersVisibCheck";
  
  
  /**
   * Instantiate the GroupProcessor which this class extends
   */
  public TrackerDetectedChecker()
  {
    super(1, 1);
    
    m_mainFork = new Fork(3);
    
    //First branch of the m_mainFork (output: 0): here we just pass
    //the JsonElement to: 
    m_mainFilter = new Filter();
    Connector.connect(m_mainFork, 0, m_mainFilter, 0);
    
    
    //Second branch of the m_mainFork (output: 1): here we check the id of the JsonElement
    m_getId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_mainFork, 1, m_getId, 0);
    
    m_idFork = new Fork(3);
    Connector.connect(m_getId, m_idFork);
    
    //First branch of the m_idFork (output: 0)
    m_constSearchId = new ApplyFunction(new Constant(s_idSearchValue));
    
    m_equalSearch = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_idFork, 0, m_equalSearch, 0);
    Connector.connect(m_constSearchId, 0, m_equalSearch, 1);
    
    //Second branch of the m_idFork (output: 1)
    m_constTrackDetectedId = new ApplyFunction(new Constant(s_idTrackDetectedValue));
    
    m_equalTrackDetected = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_idFork, 1, m_equalTrackDetected, 0);
    Connector.connect(m_constTrackDetectedId, 0, m_equalTrackDetected, 1);
    
    //Third branch of the m_idFork (output: 2)
    m_constEnterId = new ApplyFunction(new Constant(s_idEnterValue));
    
    m_equalEnter = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_idFork, 2, m_equalEnter, 0);
    Connector.connect(m_constEnterId, 0, m_equalEnter, 1);
    
       
    //Third branch of the m_mainFork (output: 2): here we check the state of the JsonElement
    m_getState = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_STATE));
    Connector.connect(m_mainFork, 2, m_getState, 0);
    
    m_constEnterState = new ApplyFunction(new Constant(s_stateEnterValue));
    
    m_equalEnterState = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getState, 0, m_equalEnterState, 0);
    Connector.connect(m_constEnterState, 0, m_equalEnterState, 1);
    
    m_logicAnd = new ApplyFunction(Booleans.and);
    Connector.connect(m_equalEnter, 0, m_logicAnd, 0);
    Connector.connect(m_equalEnterState, 0, m_logicAnd, 1);
    
    m_firstLogicOr = new ApplyFunction(Booleans.or);
    Connector.connect(m_equalSearch,  0, m_firstLogicOr, 0);
    Connector.connect(m_equalTrackDetected,  0, m_firstLogicOr, 1);
    
    m_secondLogicOr = new ApplyFunction(Booleans.or);
    Connector.connect(m_firstLogicOr, 0, m_secondLogicOr, 0);
    Connector.connect(m_logicAnd, 0, m_secondLogicOr, 1);
    
    
    Connector.connect(m_secondLogicOr, 0, m_mainFilter, 1);
    
    m_checkTracker = new ApplyFunction(new CheckTracker());
    Connector.connect(m_mainFilter, m_checkTracker);
    
    this.addProcessors(m_mainFork, m_getId, m_idFork, m_constSearchId, m_equalSearch,
        m_constTrackDetectedId, m_equalTrackDetected, m_constEnterId, m_equalEnter,
        m_firstLogicOr, m_getState, m_constEnterState, m_equalEnterState, m_logicAnd,
        m_secondLogicOr, m_mainFilter);
    this.associateInput(0, m_mainFork, 0);
    this.associateOutput(0, m_checkTracker, 0);
  }

  
  private class CheckTracker extends UnaryFunction<JsonElement, Boolean>
  {
    
    private ArrayList<String> m_trackerTypeList;

    public CheckTracker()
    {
      super(JsonElement.class, Boolean.class);
      m_trackerTypeList = new ArrayList<>();
    }

    @Override
    public Boolean getValue(JsonElement arg0)
    {
      String id = ((JsonString)((JsonMap) arg0).get(GetJsonFields.JK_ID)).stringValue();
      if (id.equals(s_idSearchValue))
      {
        JsonList searchList = ((JsonList)((JsonMap) arg0).get(GetJsonFields.JK_TYPES));
        for (int i = 0; i < searchList.size(); i++)
        {
          String currentElement = ((JsonString)searchList.get(i)).stringValue(); 
          if (!m_trackerTypeList.contains(currentElement))
          {
            m_trackerTypeList.add(currentElement);
            System.out.println("Add " + currentElement + " in the list");
          }          
        }
      }
      else if (id.equals(s_idTrackDetectedValue))
      {
        //
      }
      else
      {
        //
      }
      return true;
    }
    
  }
  
}
