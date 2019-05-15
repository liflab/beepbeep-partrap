package utilityFeatures;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.json.ParseJson;
import ca.uqac.lif.cep.util.Lists;

/**
 * This class is a GroupProcessor that it take a file and parse it in String stream, allow us to parse in JSON
 * @author Helloïs BARBOSA
 *
 */
public class ParseFileToJson extends GroupProcessor{
	
	/**
	 * The JsonOpener processor
	 */
	private JsonOpener m_JsonOpener;
	
	/**
	 * The processor that allow to CheckJsonLine function to operate
	 */
	private ApplyFunction m_CheckJsonLine;
	
	/**
	 * The Unpack processor
	 */
	private Lists.Unpack m_Unpack;
		
	/**
	 * The processor that allow to ParseJson function to operate
	 */
	private ApplyFunction m_ParseJson;
	

	/**
	 * Instantiate the GroupProcessor which this class extends
	 * @param JsonFileName
	 * 						The JSON file name that will be read
	 */
	public ParseFileToJson(String JsonFileName) {
		super(0, 1);
		try {
			m_JsonOpener = new JsonOpener(JsonFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				

		m_CheckJsonLine = new ApplyFunction(new CheckJsonLine());
		Connector.connect(m_JsonOpener, m_CheckJsonLine);
		
		
		m_Unpack = new Lists.Unpack();
		Connector.connect(m_CheckJsonLine, m_Unpack);
		
		
		m_ParseJson = new ApplyFunction(ParseJson.instance);
		Connector.connect(m_Unpack, m_ParseJson);
		
		
		this.addProcessors(m_JsonOpener, m_CheckJsonLine, m_Unpack, m_ParseJson);
		this.associateOutput(0, m_ParseJson, 0);
	}
		
	
	/**
	 * This class is a UnaryFunction that it take a String (meaning a line of a file) and give an ArrayList of each object that he find in the String
	 * @author Helloïs BARBOSA
	 *
	 */
	@SuppressWarnings("rawtypes")
	private class CheckJsonLine extends UnaryFunction<String, ArrayList>{
		
		/**
		 * The ArrayList that allows to cumulate all object in a line
		 */
		private ArrayList<String> m_Buffer;

		/**
		 * Instantiates a new UnaryFunction
		 */
		public CheckJsonLine() {
			super(String.class, ArrayList.class);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ArrayList<String> getValue(String arg0) {
			// TODO Auto-generated method stub
			m_Buffer = new ArrayList<String>();
			if(!arg0.isEmpty()) {
				int lineSize = arg0.length();
				for(int i = 1; i < lineSize; i++) {
					//First we check that the line strat by "{"
					while(!arg0.isEmpty() && arg0.charAt(0) != '{') {
						arg0 = arg0.substring(1);
						lineSize = arg0.length();
					}
					//Then we check that line have just one object
					if(!arg0.isEmpty() && arg0.charAt(i) == '}') {
						m_Buffer.add(arg0.substring(0, i+1));
						arg0 = arg0.substring(i+1);
						lineSize = arg0.length();
						i = 0;
					}
				}
			}
			return m_Buffer;
		}
		
	}
	
}
