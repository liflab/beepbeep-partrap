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
import utilityfeatures.ContainJsonString;
import utilityfeatures.EqualsJsonString;
import utilityfeatures.GetJsonFields;

/**
 * This class allows to verify the tenth property of the project
 * (refer to the pdf included in the project):
 * Enter e where e. state == 'TrackersConnection ' preceded_by CameraConnected
 * It take JsonElements and give a Boolean
 * @author Helloïs BARBOSA
 *
 */
public class CameraConnectedChecker extends GroupProcessor
{

  /**
   * The Fork to split stream in two parts
   */
  private Fork m_mainFork;
  
  /**
   * The Filter that allow to let pass just right object
   * with id: s_idCameraValue or id: s_idTrackerValue & state: s_stateTrackerValue
   */
  private Filter m_mainFilter;
  
  
  /**
   * The ApplyFunction allow to get the field with the key: "id"
   */
  private ApplyFunction m_getCameraId;
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constCameraId;
  
  /**
   * The ApplyFunction allow to check if desired id and JSON id are equals
   */
  private ApplyFunction m_equalCameraId;
  
  
  /**
   * The Fork to check if JsonElement is: s_idTrackerValue with state: s_stateTrackerValue
   */
  private Fork m_trackerFork; 
  
  /**
   * The ApplyFunction allow to get the field with the key: "id"
   */
  private ApplyFunction m_getTrackerId;
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constTrackerId;
  

  /**
   * The ApplyFunction allow to check if desired id and JSON id are equals
   */
  private ApplyFunction m_equalTrackerId;
  
  
  /**
   * The ApplyFunction allow to get the field with the key: "state"
   */
  private ApplyFunction m_getTrackerState;
  
  /**
   * The ApplyFunction allow to define the constant state that
   * allow to compare it with the JSON filed with key "state"
   */
  private ApplyFunction m_constTrackerState;
  
  /**
   * The ApplyFunction allow to check if desired state is contain in JSON state object
   */
  private ApplyFunction m_containTrackerState;
  
  /**
   * The ApplyFunction allow to check if the object correspond to
   * an id: s_idTrackerValue and a state: s_stateTrackerValue
   */
  private ApplyFunction m_trackerLogicAnd;
  
  /**
   * This ApplyFunction allow to check if the object correspond to
   * an id: s_idTrackerValue and a state: s_stateTrackerValue
   * or just id: s_idCameraValue
   */
  private ApplyFunction m_mainLogicOr;
  
  /**
   * The ApplyFunction that call the inner UnaryFunction: CameraAlreadyConnected to
   * check if Camera is already connected and to continue the process
   */
  private ApplyFunction m_cameraAlreadyConnected;
  
  /**
   * The String of the tracker state value field
   */
  private static String s_stateTrackerValue = "TrackersConnection";
  
  /**
   * The String of the tracker id value field
   */
  private static String s_idTrackerValue = "EnterState";

  /**
   * The String of the camera id value field
   */
  private static String s_idCameraValue = "CameraConnected";

  
  /**
   * Instantiate the GroupProcessor which this class extends
   */
  public CameraConnectedChecker()
  {
    super(1, 1);
    
    m_mainFork = new Fork(3);
    
    
    //First branch of the m_mainFork (output: 0): here we just pass the JsonElement to: m_mainFilter
    m_mainFilter = new Filter();
    Connector.connect(m_mainFork, 0, m_mainFilter, 0);
    
    
    //Second branch of the m_mainFork (output: 1): here we check if
    //the JsonElement is: s_idCameraValue
    m_getCameraId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_mainFork, 1, m_getCameraId, 0);
    
    m_constCameraId = new ApplyFunction(new Constant(s_idCameraValue));
    
    m_equalCameraId = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getCameraId, 0, m_equalCameraId, 0);
    Connector.connect(m_constCameraId, 0, m_equalCameraId, 1);
    
    
    //Third branch of the m_mainFork (output: 2): here we check if
    //the JsonElement is: s_idTrackerValue where his state contain: TrackersConnection
    m_trackerFork = new Fork(2);
    Connector.connect(m_mainFork, 2, m_trackerFork, 0);
    
    //First branch of the m_trackerFork (output: 0): here we check if id is: s_idTrackerValue
    m_getTrackerId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_trackerFork, 0, m_getTrackerId, 0);
    
    m_constTrackerId = new ApplyFunction(new Constant(s_idTrackerValue));
    
    m_equalTrackerId = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getTrackerId, 0, m_equalTrackerId, 0);
    Connector.connect(m_constTrackerId, 0, m_equalCameraId, 1);
    
    //Second branch of the m_trackerFork (output: 1): here we check
    //if state contain: TrackersConnection
    m_getTrackerState = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_trackerFork, 1, m_getTrackerState, 0);
    
    m_constTrackerState = new ApplyFunction(new Constant(s_stateTrackerValue));
    
    m_containTrackerState = new ApplyFunction(new ContainJsonString());
    Connector.connect(m_getTrackerState, 0, m_containTrackerState, 0);
    Connector.connect(m_constTrackerState, 0, m_containTrackerState, 1);
    
    //We merge the two results of our branches (of the m_trackerFork) to produce a new Boolean
    m_trackerLogicAnd = new ApplyFunction(Booleans.and);
    Connector.connect(m_equalTrackerId, 0, m_trackerLogicAnd, 0);
    Connector.connect(m_containTrackerState, 0, m_trackerLogicAnd, 1);
    
    
    //Now to check the entire proporty (meaning JsonElement with id: s_idCameraValue
    //or with id: s_idTrackerValue & state: TrackersConnection
    m_mainLogicOr = new ApplyFunction(Booleans.or);
    Connector.connect(m_equalCameraId, 0, m_mainLogicOr, 0);
    Connector.connect(m_trackerLogicAnd, 0, m_mainLogicOr, 1);
    
    //To finish we connect the m_mainLogicOr to the m_mainFilter
    Connector.connect(m_mainLogicOr, 0, m_mainFilter, 1);
    
    //We have just to pass the result in a custom UnaryFunction to do the specific processing
    m_cameraAlreadyConnected = new ApplyFunction(new CameraAlreadyConnected());
    Connector.connect(m_mainFilter, m_cameraAlreadyConnected);
    
    this.addProcessors(m_mainFork, m_mainFilter,
        m_getCameraId, m_constCameraId, m_equalCameraId,
        m_trackerFork, m_getTrackerId, m_constTrackerId, m_equalTrackerId, 
        m_getTrackerState, m_constTrackerState, m_containTrackerState, m_trackerLogicAnd, m_mainLogicOr, m_cameraAlreadyConnected);
    this.associateInput(0, m_mainFork, 0);
    this.associateOutput(0, m_cameraAlreadyConnected, 0);
  }
  
  /**
   * This inner class is a UnaryFunction that it take a JsonElement and give a Boolean
   * depending on whether Camera is already connected or not
   * @author Helloïs BARBOSA
   *
   */
  private class CameraAlreadyConnected extends UnaryFunction<JsonElement, Boolean>
  {

    /**
     * Instantiates a new UnaryFunction
     */
    public CameraAlreadyConnected()
    {
      super(JsonElement.class, Boolean.class);
    }
    
    @Override
    public Boolean getValue(JsonElement arg0)
    {
      System.out.println("Json object: " + arg0);
      return true;
    }
    
  }

};