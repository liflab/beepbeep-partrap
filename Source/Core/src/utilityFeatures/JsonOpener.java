package utilityFeatures;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ca.uqac.lif.cep.io.ReadLines;

/**
 * This class allow to read files in the following directory: "/trace/" (in application directory)
 * @author Hello�s BARBOSA
 *
 */
public class JsonOpener extends ReadLines{
	
	/**
	 * The String that correspond to the default path of JSON trace
	 */
	private static final String defaultJsonTracePath = System.getProperty("user.dir") + "/trace/";

	/**
	 * Instantiate ReadLines processor which this class extends
	 * @param JsonFileName
	 * 						The JSON file name that will be read
	 * 
	 * @throws FileNotFoundException
	 * 									Exception may be the thrown
	 */
	public JsonOpener(String JsonFileName) throws FileNotFoundException {
		super((InputStream) new FileInputStream(defaultJsonTracePath + JsonFileName));
	}
	
}
