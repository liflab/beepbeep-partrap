import org.junit.Assert
import utilityfeatures.UtilityMethods

class UtilityMethodsTest extends GroovyTestCase {
    void setUp() {
        super.setUp()
    }

    void tearDown() {
    }

    void testDistanceProcessing() {
        // arrange;
        Float[] point1 = new Float[3];
        point1.putAt(0, 1f);
        point1.putAt(1, 2f);
        point1.putAt(2, 3f);


        Float[] point2 = new Float[3];
        point2.putAt(0, 1f);
        point2.putAt(1, 2f);
        point2.putAt(2, 3f);


        //act
        Float result = UtilityMethods.distanceProcessing(point1, point2);

        //assert
        Assert.assertEquals 0.0, result, 0.0;
    }
}

/*distanceProcessing(Float[] first_point, Float[] second_point)*/
