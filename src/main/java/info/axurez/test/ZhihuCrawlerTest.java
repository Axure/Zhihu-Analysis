package info.axurez.test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import info.axurez.network.ZhihuCrawler;

/**
 * Created by lluvi on 2016/2/3.
 */
public class ZhihuCrawlerTest {
    public static void main(String args[]) {
        System.out.println("Starting!");
        ZhihuCrawler zhihuCrawler = new ZhihuCrawler();
        String result = zhihuCrawler.getAnswer("39357642", "81144987");
        System.out.println(result);
        System.out.println("Ending!");
    }
}
