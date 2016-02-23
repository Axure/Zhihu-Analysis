package info.axurez.attempt;

import info.axurez.network.http.ZhihuCrawler;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.slf4j.LoggerFactory;

/**
 * Created by axurez on 2016/2/24.
 * <p>
 * TODO: you'd better give it a more descriptive name.
 */
public class FirstAttempt {
    public static void main(String args[]) {
//        ZhihuCrawler zhihuCrawler = new ZhihuCrawler();
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        ZhihuCrawler zhihuCrawler = (ZhihuCrawler)context.getBean("zhihuCrawler");
        String result = zhihuCrawler.getQuestionHtml("28943259");
//
//
        Logger logger = LoggerFactory.getLogger(FirstAttempt.class);
        System.out.println(result);


    }

}
