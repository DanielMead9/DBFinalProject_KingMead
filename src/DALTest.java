import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DALTest {

    private static DAL dal;

    @BeforeClass
    public static void Setup() {
        dal = new DAL();
    }

    @AfterClass
    public static void Clean() {
        System.out.println("Is there anything to be cleaned up? Maybe we should remove test data from database?");
    }

    @Test
    public void testEC() {
        assertFalse("Test expects a failure, but no exception/crash",
                dal.ensureConnection(null, null, null));
    }

    @Test
    public void testDP() {
        assertFalse("Test expects a failure, but no exception/crash",
                dal.displayProducts(null, null, null));
    }

    @Test
    public void testUP() {
        boolean result = dal.updateProduct(null, null, null, 0, 0);
        assertFalse("Test expects a failure, true was returned", result);
    }

    @Test
    public void testCheckout() {
        boolean result = dal.checkout(null, null, null, null, 0, 0, null, 0, null);
        assertFalse("Test expects success, false was returned", result);
    }

}
