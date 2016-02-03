package info.axurez.network;
import junit.framework.*;
/**
 * Created by zhenghu on 2016/2/2.
 */
public class CrawlerTest extends TestCase {
    protected int value1, value2;

    // assigning the values
    protected void setUp(){
        value1=3;
        value2=3;
    }

    // test method to add two values
    public void testAdd(){
        double result= value1 + value2;
        assertTrue(result == 6);
    }
}
