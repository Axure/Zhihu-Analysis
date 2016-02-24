package info.axurez.network.http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by lluvi on 2016/2/2.
 */

interface ProcessContent {

}

/**
 * TODO: dependency injection.
 */
public class ZhihuCrawler extends AsyncHttpClientCrawler {

    private String QUESTION_PREFIX = "http://zhihu.com/question/";
    private String ANSWER_INFIX = "/answer/";

    public ZhihuCrawler() {
        super(LoggerFactory.getLogger(ZhihuCrawler.class));
    }

    public ZhihuCrawler(Logger logger) {
        super(logger);
    }

    public String getAnswerHtml(String questionId, String AnswerId) {

        StringBuilder stringBuilder = new StringBuilder(QUESTION_PREFIX);
        stringBuilder.append(questionId);
        stringBuilder.append(ANSWER_INFIX);
        stringBuilder.append(AnswerId);
        return this.getContent(stringBuilder.toString());
    }

    public String getQuestionHtml(String questionId) {
        StringBuilder stringBuilder = new StringBuilder(QUESTION_PREFIX);
        stringBuilder.append(questionId);
        return this.getContent(stringBuilder.toString());
    }

    class ActionBuilder {
        public ActionBuilder() {

        }

        public void setProblemId() {

        }

        public void setAnswerId() {

        }

        public boolean isValid() {
            return true;
        }

        public void setType() {

        }


    }

    enum ACTION_TYPE {
        ANSWER,
        PROBLEM,
        USER,
        PAGE
    }
}
