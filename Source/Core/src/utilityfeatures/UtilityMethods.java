package utilityfeatures;

/**
 * @author Hellois BARBOSA
 *
 */
public class UtilityMethods
{

  /**
   * This method allow to calculate the distance between
   * two points in 3D and return it as a float
   * @param first_point
   *                        first_point is array of Float with
   *                        in index 0: x position
   *                        in index 1: y position
   *                        and in index 2: z position
   * @param second_point
   *                        second_point is formed in the same way
   *
   * @return distance as a float
   */
  public static Float distanceProcessing(Float[] first_point, Float[] second_point)
  {
    return (float) Math.sqrt(
        Math.pow(first_point[0].doubleValue() - second_point[0].doubleValue(), 2)
        + Math.pow(first_point[1].doubleValue() - second_point[1].doubleValue(), 2)
        + Math.pow(first_point[2].doubleValue() - second_point[2].doubleValue(), 2));
  }

}
