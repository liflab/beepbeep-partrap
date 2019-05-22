package properties;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonNumber;
import utilityfeatures.EqualsJsonString;
import utilityfeatures.GetJsonFields;
import utilityfeatures.UtilityMethods;

/**
 * This class allows to verify the fourth property of the project
 * (refer to the pdf included in the project):
 *      absence_of set ( HipCenter hc , KneeCenter kc ) where dist ( hc . point , kc . point ) <= d
 * It take JsonElements and give a Boolean
 * @author Helloïs BARBOSA
 *
 */
public class CheckPatientPosition extends GroupProcessor
{

  /**
   * The Fork to split stream in two parts: one to filter HipCenter
   * and the other one to filter KneeCenter
   */
  private Fork m_firstFork;

  /**
   * The Fork to split stream in two parts: to check if JsonElement are HipCenter
   */
  private Fork m_hipCenterFork;

  /**
   * The Filter to check if the JsonElement take in input is well a HipCenter event
   */
  private Filter m_filterHipCenter;
  
  /**
   * This ApplyFunction allow to get the field with the key: "id"
   */
  private ApplyFunction m_getIdHipCenter;

  /**
   * The ApplyFunction allow to define the constant id that allow
   * to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constIdHipCenter;

  /**
   * The ApplyFunction allow to check if desired id and JSON id are equals
   */
  private ApplyFunction m_equalIdHipCenter;

  /**
   * The ApplyFunction allow to get the field with the key: "point" that
   * correspond to a JsonList that contain 3D points of a HipCenter JsonObject
   */
  private ApplyFunction m_getHipCenterPoint;


  /**
   * The Fork to split stream in two parts: to check if JsonElement are KneeCenter
   */
  private Fork m_kneeCenterFork;

  /**
   * The Filter to check if the JsonElement take in input is well a KneeCenter event
   */
  private Filter m_filterKneeCenter;

  /**
   * This ApplyFunction allow to get the field with the key: "id"
   */
  private ApplyFunction m_getIdKneeCenter;

  /**
   * The ApplyFunction allow to define the constant id that allow
   * to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constIdKneeCenter;

  /**
   * The ApplyFunction allow to check if desired id and JSON id are equals
   */
  private ApplyFunction m_equalIdKneeCenter;
  
  /**
   * The ApplyFunction allow to get the field with the key: "point" that
   * correspond to a JsonList that contain 3D points of a KneeCenter JsonObject
   */
  private ApplyFunction m_getKneeCenterPoint;


  /**
   * This ApplyFunction allow to check if a set of Hip and Knee points are at the right
   * distance from each other
   * (meaning that is, by calling the inner BinaryFunction: CheckHipAndKneePoints)
   */
  private ApplyFunction m_checkHipAndKneePoints;

  /**
   * The String of the value of HipCenter id field
   */
  private static String s_idHipCenterValue = "HipCenter";

  /**
   * The String of the value of KneeCenter id field
   */
  private static String s_idKneeCenterValue = "KneeCenter";

  /**
   * Instantiate the GroupProcessor which this class extends
   * @param min_hip_knee_dist
   *                        The minimum distance that there must be between an
   *                        HipCenter point and a KneeCenter point
   */
  public CheckPatientPosition(Float min_hip_knee_dist)
  {
    super(1, 1);

    m_firstFork = new Fork(2);

    //First branch of the m_firstFork (output: 0): here we check that the event id is HipCenter
    m_hipCenterFork = new Fork(2);
    Connector.connect(m_firstFork, 0, m_hipCenterFork, 0);


    //First branch of the m_hipCenterFork (output: 0): here we just pass
    //JsonElement to m_filterHipCenter
    m_filterHipCenter = new Filter();
    Connector.connect(m_hipCenterFork, 0, m_filterHipCenter, 0);


    //Second branch of the m_hipCenterFork (output: 1): here we check that the event id is HipCenter
    m_getIdHipCenter = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_hipCenterFork, 1, m_getIdHipCenter, 0);
    
    m_constIdHipCenter = new ApplyFunction(new Constant(s_idHipCenterValue));
    
    m_equalIdHipCenter = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getIdHipCenter, 0, m_equalIdHipCenter, 0);
    Connector.connect(m_constIdHipCenter, 0, m_equalIdHipCenter, 1);
    
    Connector.connect(m_equalIdHipCenter, 0, m_filterHipCenter, 1);
    
    //To finish we get the HipCenter point if exists
    m_getHipCenterPoint = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_POINT));
    Connector.connect(m_filterHipCenter, m_getHipCenterPoint);
    
    
    
    //Second branch of the m_firstFork (output: 1): here we check that the event id is KneeCenter
    m_kneeCenterFork = new Fork(2);
    Connector.connect(m_firstFork, 1, m_kneeCenterFork, 0);
    
    
    //First branch of the m_kneeCenterFork (output: 0): here we just
    //pass JsonElement to m_filterKneeCenter
    m_filterKneeCenter = new Filter();
    Connector.connect(m_kneeCenterFork, 0, m_filterKneeCenter, 0);
    
    
    //Second branch of the m_hipCenterFork (output: 1): here we check
    //that the event id is KneeCenter
    m_getIdKneeCenter = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_kneeCenterFork, 1, m_getIdKneeCenter, 0);
    
    m_constIdKneeCenter = new ApplyFunction(new Constant(s_idKneeCenterValue));
    
    m_equalIdKneeCenter = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getIdKneeCenter, 0, m_equalIdKneeCenter, 0);
    Connector.connect(m_constIdKneeCenter, 0, m_equalIdKneeCenter, 1);
    
    Connector.connect(m_equalIdKneeCenter, 0, m_filterKneeCenter, 1);
    
    //To finish we get the KneeCenter point if exists
    m_getKneeCenterPoint = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_POINT));
    Connector.connect(m_filterKneeCenter, m_getKneeCenterPoint);
    
    
    m_checkHipAndKneePoints = new ApplyFunction(new CheckHipAndKneePoints(min_hip_knee_dist));
    Connector.connect(m_getHipCenterPoint, 0, m_checkHipAndKneePoints, 0);
    Connector.connect(m_getKneeCenterPoint, 0, m_checkHipAndKneePoints, 1);
    
    
    this.addProcessors(m_firstFork, m_checkHipAndKneePoints);
    this.addProcessors(m_hipCenterFork, m_filterHipCenter, m_getIdHipCenter, m_constIdHipCenter,
        m_equalIdHipCenter, m_getHipCenterPoint);
    this.addProcessors(m_kneeCenterFork, m_filterKneeCenter, m_getIdKneeCenter, m_constIdKneeCenter,
        m_equalIdKneeCenter, m_getKneeCenterPoint);
    this.associateInput(0,  m_firstFork, 0);
    this.associateOutput(0, m_checkHipAndKneePoints, 0);
  }
  
  /**
   * This inner class is a BinaryFunction that it take a two JsonElement
   * (meaning two JsonList of 3D points)and give Boolean
   * (with d the distance between the two 3D points: d > m_minHipKneeDist)
   * @author Helloïs BARBOSA
   *
   */
  private class CheckHipAndKneePoints extends BinaryFunction<JsonElement, JsonElement, Boolean>
  {

    /**
     * The minimum distance that there must be between an HipCenter point and a KneeCenter point
     */
    private Float m_minHipKneeDist;

    /**
     * Instantiates a new BinaryFunction
     * @param min_hip_knee_dist
     *                          The minimum distance that there must be between an
     *                          HipCenter point and a KneeCenter point
     */
    public CheckHipAndKneePoints(Float min_hip_knee_dist)
    {
      super(JsonElement.class, JsonElement.class, Boolean.class);
      m_minHipKneeDist = min_hip_knee_dist;
    }

    @Override
    public Boolean getValue(JsonElement arg0, JsonElement arg1)
    {
      if (!(arg0 instanceof JsonNull) && !(arg1 instanceof JsonNull))
      {
        JsonList hipCenter = (JsonList)arg0;
        System.out.println("HipCenter points: " + hipCenter);
        JsonList kneeCenter = (JsonList)arg1;
        System.out.println("KneeCenter points: " + kneeCenter);
        
        Float[] coordHipCenter = new Float[] {
            ((JsonNumber)hipCenter.get(0)).numberValue().floatValue(),
            ((JsonNumber)hipCenter.get(1)).numberValue().floatValue(),
            ((JsonNumber)hipCenter.get(2)).numberValue().floatValue()};
        
        Float[] coordKneeCenter = new Float[] {
            ((JsonNumber)kneeCenter.get(0)).numberValue().floatValue(),
            ((JsonNumber)kneeCenter.get(1)).numberValue().floatValue(),
            ((JsonNumber)kneeCenter.get(2)).numberValue().floatValue()};
        
        if (UtilityMethods.distanceProcessing(coordHipCenter, coordKneeCenter) > m_minHipKneeDist)
        {
          return true;
        }
      }
      return false;
    }
    
  }
  
}