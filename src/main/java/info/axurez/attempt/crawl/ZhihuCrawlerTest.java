package info.axurez.attempt.crawl;
import info.axurez.network.http.ZhihuCrawler;

/**
 * Created by axurez on 2016/2/3.
 */
public class ZhihuCrawlerTest {
    public static void main(String args[]) {
        System.out.println("Starting!");
        ZhihuCrawler zhihuCrawler = new ZhihuCrawler();
        String result = zhihuCrawler.getAnswerHtml("39357642", "81144987");
        System.out.println(result);
        System.out.println("Ending!");
    }
}