package properties;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.ProcessorException;
import ca.uqac.lif.cep.Pushable;
import ca.uqac.lif.cep.diagnostics.Derivation;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.util.Booleans;
import utilityfeatures.EqualsJsonString;
import utilityfeatures.GetJsonFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * This class allows to verify the first property of the project
 * (refer to the pdf included in the project): occurrence_of Enter e where e . state == 'redo '
 * It take JsonElements and give a Boolean
 * @author Hello?s BARBOSA
 *
 */
public class RedoAcquisition extends GroupProcessor 
{

  /**
   * Derivator used for unit testing
   */
  private MyDerivation m_derivator;

  /**
   * The Fork to split stream in two parts
   */
  private Fork m_fork;

  /**
   * The ApplyFunction allow to get the field with the key: "id"
   */
  private ApplyFunction m_getId;


  /**
   * The ApplyFunction allow to check if desired id and JSON id are equals
   */
  private ApplyFunction m_equalId;
  
  /**
   * The ApplyFunction allow to define the constant id that
   * allow to compare it with the JSON filed with key "id"
   */
  private ApplyFunction m_constId;
  
  /**
   * The ApplyFunction allow to get the field with the key: "state"
   */
  private ApplyFunction m_getState;
  
  /**
   * The ApplyFunction allow to check if desired state and JSON state are equals
   */
  private ApplyFunction m_equalRedo;
  
  /**
   * The ApplyFunction allow to define the constant state
   * that allow to compare it with the JSON filed with key "state"
   */
  private ApplyFunction m_constRedo;
  
  /**
   * The ApplyFunction allow to check if the both condition of
   * this property are verified and give the final result
   */
  private ApplyFunction m_logicAnd;
  
  /**
   * The String of the state field
   */
  private static String s_redo = "";
  
  /**
   * The String of the value field
   */
  private static String s_idValue = "EnterState";
  
  
  /**
   * Instantiate the GroupProcessor which this class extends
   */
  public RedoAcquisition()
  {
    super(1, 1);

    m_derivator = new MyDerivation();

    m_fork = new Fork(2);
    

    //First branch of the fork (output: 0): here we check that the event ID is the right one
    m_getId = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_ID));
    Connector.connect(m_fork, 0, m_getId, 0);
    
    m_equalId = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getId, 0, m_equalId, 0);
    
    m_constId = new ApplyFunction(new Constant(s_idValue));
    Connector.connect(m_constId, 0, m_equalId, 1);
    
    
    //Second branch of the fork (output: 1): here we check that the event STATE is the right one
    m_getState = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_STATE));
    Connector.connect(m_fork, 1, m_getState, 0);
    
    m_equalRedo = new ApplyFunction(new EqualsJsonString());
    Connector.connect(m_getState, 0, m_equalRedo, 0);
    
    m_constRedo = new ApplyFunction(new Constant(s_redo));
    Connector.connect(m_constRedo, 0, m_equalRedo, 1);
    
    
    //We apply an AND logic between the two results because
    //we need two answer to be true to verify this property
    m_logicAnd = new ApplyFunction(Booleans.and);
    Connector.connect(m_equalId, 0, m_logicAnd, 0);
    Connector.connect(m_equalRedo, 0, m_logicAnd, 1);
    
    //We add all processors in the group and we associate input/output
    this.addProcessors(m_fork, m_getId, m_equalId, m_constId, m_getState, m_equalRedo,
        m_constRedo, m_logicAnd);
    this.associateInput(0, m_fork, 0);
    this.associateOutput(0, m_logicAnd, 0);    
  }


  public void ReconnectToEqualId()
  {
    Connector.connect(m_getId, m_derivator);
    Connector.connect(m_derivator, m_equalId);

  }

  public void ReconnectToEqualRedo()
  {
    Connector.connect(m_getState, m_derivator);
    Connector.connect(m_derivator, m_equalRedo);
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
