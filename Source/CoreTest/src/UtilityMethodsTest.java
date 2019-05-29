import org.junit.Assert;
import org.junit.Test;
import utilityfeatures.UtilityMethods;

public class UtilityMethodsTest
{

    public void tearDown()
    {
    }

    @Test
    public void testDistanceProcessingEqualValuesAssert0()
    {
        // arrange;
        Float[] point1 = new Float[3];
        point1[0] = 0f;
        point1[1] = 1f;
        point1[2] = 2f;

        Float[] point2 = new Float[3];
        point2[0] = 0f;
        point2[1] = 1f;
        point2[2] = 2f;


        //act
        Float result = UtilityMethods.distanceProcessing(point1, point2);

        //assert
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void TestDistanceBiggerValues()
    {
        Float[] point1 = new Float[3];
        point1[0] = 7f;
        point1[1] = 4f;
        point1[2] = 3f;

        Float[] point2 = new Float[3];
        point2[0] = 17f;
        point2[1] = 6f;
        point2[2] = 2f;


        //act
        Float result = UtilityMethods.distanceProcessing(point1, point2);

        //assert
        Assert.assertEquals(10.246951, result, 0.001);

    }

    @Test
    public void TestDistanceNegativeValues()
    {

        Float[] point1 = new Float[3];
        point1[0] = -7f;
        point1[1] = 4f;
        point1[2] = 3f;

        Float[] point2 = new Float[3];
        point2[0] = 17f;
        point2[1] = -6f;
        point2[2] = 2f;


        //act
        Float result = UtilityMethods.distanceProcessing(point1, point2);

        //assert
        Assert.assertEquals(26.019224, result, 0.001);

    }

}
