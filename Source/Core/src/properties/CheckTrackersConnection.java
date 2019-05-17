package properties;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonNumber;
import utilityFeatures.ContainJsonString;
import utilityFeatures.GetJsonFields;

/**
 * This class allows to verify the fifteenth property of the project (refer to the pdf included in the project)
 * It take JsonElements and give a Boolean
 * @author Helloïs BARBOSA
 *
 */
public class CheckTrackersConnection extends GroupProcessor {
	
	/**
	 * The Fork to split stream in two parts
	 */
	private Fork m_fork;
	
	/**
	 * The ApplyFunction allow to get the field with the key: "state"
	 */
	private ApplyFunction m_getState;
	
	/**
	 * The ApplyFunction allow to define the constant state that allow to compare it with the JSON filed with key "state"
	 */
	private ApplyFunction m_constState;
	
	/**
	 * The ApplyFunction allow to check if JSON state contain desired state
	 */
	private ApplyFunction m_containStateValue;
	
	/**
	 * The Filter that the only lets the right JsonElement through
	 */
	private Filter m_filter;
	
	/**
	 * The ApplyFunction allow to get the field with the key: "time"
	 */
	private ApplyFunction m_getTime;
	
	/**
	 * The CheckTimeBetweenLastEvent allow to check the final property
	 */
	private ApplyFunction m_checkTimeBetweenLastEvent;
		
	/**
	 * The String of the value field
	 */
	private static String s_stateValue = "TrackersConnection";
	
	/**
	 * The Double correspond to the time between two events with s_stateValue state
	 */
	private Double m_timeBetweenEvent = null;
	
	
	/**
	 * This int is the limit that must not be exceeded between two events with s_stateValue state
	 */
	private static int s_timeLimitBetweenTwoEvent = 120000;

	/**
	 * Instantiate the GroupProcessor which this class extends
	 */
	public CheckTrackersConnection() {
		super(1, 1);
		
		m_fork = new Fork(2);
		
				
		//Second branch of the fork (output: 1): here we check that the state field is the right one
		m_getState = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_STATE));
		Connector.connect(m_fork, 1, m_getState, 0);
		
		m_constState = new ApplyFunction(new Constant(s_stateValue));
		
		m_containStateValue = new ApplyFunction(new ContainJsonString());
		Connector.connect(m_getState, 0, m_containStateValue, 0);
		Connector.connect(m_constState, 0, m_containStateValue, 1);
		
		
		//We only keep events containing the s_stateValue status
		m_filter = new Filter();
		Connector.connect(m_fork, 0, m_filter, 0);
		Connector.connect(m_containStateValue, 0, m_filter, 1);
		
		
		//Now we check if the time between those two events meets expectations
		m_getTime = new ApplyFunction(new GetJsonFields(GetJsonFields.JK_TIME));
		Connector.connect(m_filter, m_getTime);
		
		m_checkTimeBetweenLastEvent = new ApplyFunction(new CheckTimeBetweenLastEvent());
		Connector.connect(m_getTime, m_checkTimeBetweenLastEvent);
		
		
		this.addProcessors(m_fork, m_getState, m_constState, m_containStateValue, m_filter, m_getTime, m_checkTimeBetweenLastEvent);
		this.associateInput(0, m_fork, 0);
		this.associateOutput(0, m_checkTimeBetweenLastEvent, 0);
	}
	


	/**
	 * This inner class allow to verify if time between the last event with s_stateValue state is less that constant: s_timeLimitBetweenTwoEvent
	 * @author Helloïs BARBOSA
	 *
	 */
	private class CheckTimeBetweenLastEvent extends UnaryFunction<JsonElement, Boolean> {

		/**
		 * Instantiates a new UnaryFunction
		 */
		public CheckTimeBetweenLastEvent() {
			super(JsonElement.class, Boolean.class);
		}

		@Override
		public Boolean getValue(JsonElement arg0) {
			if(!(arg0 instanceof JsonNull)) {
				double jsonElementTime = ((JsonNumber)arg0).numberValue().doubleValue();
				if(m_timeBetweenEvent == null) {
					m_timeBetweenEvent = jsonElementTime;
					return true;
				}
				else {
					if(jsonElementTime - m_timeBetweenEvent < s_timeLimitBetweenTwoEvent) {
						m_timeBetweenEvent = jsonElementTime;
						return true;
					}
				}
			}
			return false;
		}
		
	}

}
