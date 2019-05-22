package utilityfeatures;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.json.ParseJson;
import ca.uqac.lif.cep.util.Lists;

import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * This class is a GroupProcessor that it take a file
 * and parse it in String stream, allow us to parse in JSON
 * @author Helloïs BARBOSA
 *
 */
public class ParseFileToJson extends GroupProcessor
{
  /**
   * The JsonOpener processor
   */
  private JsonOpener m_jsonOpener;

  /**
   * The processor that allow to CheckJsonLine function to operate
   */
  private ApplyFunction m_checkJsonLine;
  
  /**
   * The Unpack processor
   */
  private Lists.Unpack m_unpack;

  /**
   * The processor that allow to ParseJson function to operate
   */
  private ApplyFunction m_parseJson;


  /**
   * Instantiate the GroupProcessor which this class extends
   * @param json_file_name
   *                        The JSON file name that will be read
   */
  public ParseFileToJson(String json_file_name)
  {
    super(0, 1);
    try
    {
      m_jsonOpener = new JsonOpener(json_file_name);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }


    m_checkJsonLine = new ApplyFunction(new CheckJsonLine());
    Connector.connect(m_jsonOpener, m_checkJsonLine);


    m_unpack = new Lists.Unpack();
    Connector.connect(m_checkJsonLine, m_unpack);


    m_parseJson = new ApplyFunction(ParseJson.instance);
    Connector.connect(m_unpack, m_parseJson);
    
    
    this.addProcessors(m_jsonOpener, m_checkJsonLine, m_unpack, m_parseJson);
    this.associateOutput(0, m_parseJson, 0);
  }


  /**
   * This class is a UnaryFunction that it take a String
   * (meaning a line of a file) and give an ArrayList of each object that he find in the String
   * @author Helloïs BARBOSA
   *
   */
  @SuppressWarnings("rawtypes")
  private class CheckJsonLine extends UnaryFunction<String, ArrayList>
  {
    
    /**
     * The ArrayList that allows to cumulate all object in a line
     */
    private ArrayList<String> m_buffer;

    /**
     * Instantiates a new UnaryFunction
     */
    public CheckJsonLine()
    {
      super(String.class, ArrayList.class);
    }

    @Override
    public ArrayList<String> getValue(String arg0)
    {
      m_buffer = new ArrayList<String>();
      if (!arg0.isEmpty())
      {
        int lineSize = arg0.length();
        for (int i = 1; i < lineSize; i++)
        {
          //First we check that the line strat by "{"
          while (!arg0.isEmpty() && arg0.charAt(0) != '{')
          {
            arg0 = arg0.substring(1);
            lineSize = arg0.length();
          }
          //Then we check that line have just one object
          if (!arg0.isEmpty() && arg0.charAt(i) == '}')
          {
            m_buffer.add(arg0.substring(0, i + 1));
            arg0 = arg0.substring(i + 1);
            lineSize = arg0.length();
            i = 0;
          }
        }
      }
      return m_buffer;
    }

  }

}