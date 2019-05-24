import groovy.util.GroovyTestCase;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Assert;
import utilityfeatures.UtilityMethods;

public class UtilityMethodsTest extends GroovyTestCase
{
    public void setUp()
    {
        super.setUp();
    }

    public void tearDown()
    {
    }

    @test
    public void testDistanceProcessing()
    {


        // arrange;
        Float[] point1 = new Float[3];
        DefaultGroovyMethods.putAt(point1, 0, 1f);
        DefaultGroovyMethods.putAt(point1, 1, 2f);
        DefaultGroovyMethods.putAt(point1, 2, 3f);


        Float[] point2 = new Float[3];
        DefaultGroovyMethods.putAt(point2, 0, 1f);
        DefaultGroovyMethods.putAt(point2, 1, 2f);
        DefaultGroovyMethods.putAt(point2, 2, 3f);


        //act
        Float result = UtilityMethods.distanceProcessing(point1, point2);

        //assert
        Assert.assertEquals(0.0, result, 0.0);
    }

}
