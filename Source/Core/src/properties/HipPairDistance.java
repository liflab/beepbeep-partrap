package properties;

import ca.uqac.lif.cep.*;
import ca.uqac.lif.cep.diagnostics.Derivation;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.tmf.Divert;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tmf.Trim;
import ca.uqac.lif.cep.util.Numbers;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;
import utilityfeatures.EqualsJsonString;
import utilityfeatures.GetJsonFields;
import utilityfeatures.UtilityMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class HipPairDistance extends GroupProcessor
{
  private ApplyFunction m_getId;

  private int m_maxDistance = 4;

  private static String s_idValue = "HipCenter";

  /*
   *  The derivator used for unit tests
   */
  private MyDerivation derivator;

  /*
   *   The first fork used to filter only the relevant events in the log file
   */
  private Fork m_baseFork;

  /*
   *   Gets the ID of the current event
   */
  private ApplyFunction m_getEventId;

  /*
   *  returns the desiered ID to be worked with
   */
  private ApplyFunction m_constHipCenterID;

  /*
   *   validates werther the ID of the event corresponds to constId
   */
  private ApplyFunction m_equalIdValidation;

  /*
   *   Filters events according to their ID
   */
  private Filter m_baseFilter;

  /*
   * gets the 3d point of the ID
   */
  private ApplyFunction m_getPoint;

  /*
   * is used once the HipCenter events have been filtered to compare them between one another
   */
  private Fork m_compareFork;

  /*
   *   trims the first point to create an offset used to compare two adjascent points
   */
  private Trim m_compareTrim;

  /*
   *  Calculates the distance between a pair of hips
   */
  private ApplyFunction m_hipDistance;

  /*
   * A constant of the upper limit of distance allowed between two hips reading
   */
  private ApplyFunction m_upperLimit;

  /*
   *  Checks if the distance between hips is smaller than the upper limit.
   *  This is also the group processor's output
   */
  private ApplyFunction m_smallerThan;

  /**
   * @param upper_limit The maximum distance allowed between pair of hips
   */
  public HipPairDistance(Float upper_limit)
  {
    super(1, 1);

    derivator = new MyDerivation();

    m_baseFork = new Fork(2);

    m_getEventId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_baseFork, 0, m_getEventId, 0);

    m_equalIdValidation = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getEventId, 0, m_equalIdValidation, 0);

    m_constHipCenterID = new ApplyFunction(new Constant(s_idValue));
    Connector.connect(m_constHipCenterID, 0, m_equalIdValidation, 1);

    m_baseFilter = new Filter();
    Connector.connect(m_baseFork, 1, m_baseFilter, 0);
    Connector.connect(m_equalIdValidation, 0, m_baseFilter, 1);

    m_getPoint = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_POINT));
    Connector.connect(m_baseFilter, m_getPoint);

    m_compareFork = new Fork(2);
    //delete this

    Connector.connect(m_getPoint, derivator);
    Connector.connect( derivator, m_compareFork);

    //Connector.connect(m_getPoint, m_compareFork);

    m_compareTrim = new Trim(1);
    Connector.connect(m_compareFork, 0, m_compareTrim, 0);

    m_hipDistance = new ApplyFunction(new HipPairDistance.CalculateDistance());
    Connector.connect(m_compareTrim, 0, m_hipDistance, 1);
    Connector.connect(m_compareFork, 1, m_hipDistance, 0);

    m_smallerThan = new ApplyFunction(Numbers.isLessThan);
    Connector.connect(m_hipDistance, m_smallerThan);

    m_upperLimit = new ApplyFunction(new Constant(upper_limit));
    Connector.connect(m_upperLimit, 0, m_smallerThan, 1);

    this.addProcessors(m_baseFork, m_getEventId, m_constHipCenterID, m_equalIdValidation,
      m_baseFilter, m_getPoint, m_compareFork, m_compareTrim,
      m_hipDistance, m_upperLimit, m_smallerThan);
    this.associateInput(0, m_baseFork, 0);
    this.associateOutput(0, m_smallerThan, 0);

  }

  public void ReconnectToGetEventId()
  {

    Connector.connect(m_baseFork, 0, derivator, 0);
    Connector.connect(derivator, m_getPoint );


  }

  public void ReconnectToTrim()
  {
    Connector.connect(m_baseFilter, derivator);
    Connector.connect (derivator, m_compareFork);

  }


  private class CalculateDistance extends BinaryFunction<JsonElement, JsonElement, Float>
  {


    public CalculateDistance()
    {
      super(JsonElement.class, JsonElement.class, Float.class);
    }

    @Override
    public Float getValue(JsonElement json_element, JsonElement json_element2)
    {

      if (json_element != null || json_element2 != null)
      {
        JsonList list1 = (JsonList) json_element;
        JsonList list2 = (JsonList) json_element2;

        Float[] point1 = new Float[] {((JsonNumber) list1.get(0)).numberValue().floatValue(),
          ((JsonNumber) list1.get(1)).numberValue().floatValue(),
          ((JsonNumber) list1.get(2)).numberValue().floatValue()};

        Float[] point2 = new Float[] {((JsonNumber) list2.get(0)).numberValue().floatValue(),
          ((JsonNumber) list2.get(1)).numberValue().floatValue(),
          ((JsonNumber) list2.get(2)).numberValue().floatValue()};
        Float a = 0f;
        return UtilityMethods.distanceProcessing(point1, point2);
      }
      return Float.valueOf(0);
    }
  }

  public static class MyDerivation extends Derivation
  {

    public static List<Object> inputsList = new ArrayList<Object>();

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs) throws ProcessorException {
      Pushable[] var6;

      inputsList.add(inputs[0]);

      int var5 = (var6 = this.m_pushables).length;

      for(int var4 = 0; var4 < var5; ++var4) {
        Pushable p = var6[var4];
        p.push(inputs[0]);
      }

      outputs.add(inputs);
      if (this.m_slowDown > 0L) {
        try {
          Thread.sleep(this.m_slowDown);
        } catch (InterruptedException var7) {
        }
      }

      return true;
    }
  }
}
