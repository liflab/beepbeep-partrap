package utilityfeatures;

import ca.uqac.lif.cep.io.ReadLines;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This class allow to read files in the following directory: "/trace/" (in application directory)
 * @author Helloïs BARBOSA
 *
 */
public class JsonOpener extends ReadLines
{

  /**
   * The String that correspond to the default path of JSON trace
   */
  private static final String defaultJsonTracePath =
      System.getProperty("user.dir") + "/trace/";

  /**
   * Instantiate ReadLines processor which this class extends
   * @param json_file_name
   *                        The JSON file name that will be read
   * 
   * @throws FileNotFoundException
   *                                Exception may be the thrown
   */
  public JsonOpener(String json_file_name) throws FileNotFoundException
  {
    super((InputStream) new FileInputStream(defaultJsonTracePath + json_file_name));
  }

}
