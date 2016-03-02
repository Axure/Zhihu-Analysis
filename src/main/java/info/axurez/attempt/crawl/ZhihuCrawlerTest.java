package info.axurez.attempt.crawl;
import info.axurez.network.http.ZhihuCrawler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by axurez on 2016/2/3.
 */
public class ZhihuCrawlerTest {
    public static void main(String args[]) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        System.out.println("Starting!");
//        ZhihuCrawler zhihuCrawler = new ZhihuCrawler();
        ZhihuCrawler zhihuCrawler = context.getBean("zhihuCrawler", ZhihuCrawler.class);
        String result = zhihuCrawler.getAnswerHtml("39357642", "81144987");
        System.out.println(result);
        System.out.println("Ending!");

        ((ClassPathXmlApplicationContext) context).close();
    }
}
