package info.axurez.attempt.parse;

import info.axurez.database.entities.Question;
import info.axurez.network.http.ZhihuCrawler;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import java.io.StringReader;
import java.util.Date;

/**
 * Created by axurez on 2016/2/25.
 */
public class SecondAttempt {
    public static void main(String args[]) {
        /**
         * Get the application context.
         */
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        /**
         *
         */
        EntityManager entityManager = context.getBean("entityManager", EntityManager.class);
        /**
         *
         */
        ZhihuCrawler crawler = context.getBean("zhihuCrawler", ZhihuCrawler.class);
        Long questionZhihuId = (long) 28943259;
        String result = crawler.getQuestionHtml(questionZhihuId.toString());
        /**
         *
         */
        ZhihuParser zhihuParser = new ZhihuParser();
        Question question = zhihuParser.parseQuestionHtml(result);
        question.setZhihuId(questionZhihuId);
        System.out.println(question);
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(question);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /**
         * Close the application context.
         */
        ((ClassPathXmlApplicationContext) context).close();
    }

}

class ZhihuParser {
    public Question parseQuestionHtml(String html) {
        Question question = new Question();
        /**
         *
         */
        question.setTimeCrawled(new Date());
        /**
         *
         */
        Document document = Jsoup.parse(html);
        try {
            Element title = document.select(".zm-item-title.zm-editable-content").first();
            System.out.println(title.text());
            question.setTitle(title.text());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        Elements contents = document.select(".zm-editable-content.clearfix");
        for (Element content : contents) {
            System.out.println(content.text());
        }
        /**
         *
         */
        return question;
    }
}
