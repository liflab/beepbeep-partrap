package utilityFeatures;

/**
 * @author Helloïs BARBOSA
 *
 */
public class UtilityMethods {
	
	/**
	 * This method allow to calculate the distance between two points in 3D and return it as a float
	 * @param firstPoint
	 * @param secondPoint
	 * 						firstPoint & secondPoint are array of Float with in index 0: x position, in index 1: y position and in index 2: z position
	 *
	 * @return distance as a float
	 * 
	 */
	public static Float distanceProcessing(Float[] firstPoint, Float[] secondPoint) {
		return (float) Math.sqrt(
				Math.pow(firstPoint[0].doubleValue() - secondPoint[0].doubleValue(), 2) + 
				Math.pow(firstPoint[1].doubleValue() - secondPoint[1].doubleValue(), 2) +
				Math.pow(firstPoint[2].doubleValue() - secondPoint[2].doubleValue(), 2));
	}

}
