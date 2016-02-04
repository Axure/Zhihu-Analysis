package info.axurez.network.http;

/**
 * Created by lluvi on 2016/2/2.
 */

interface ProcessContent {

}

public class ZhihuCrawler extends AsyncHttpClientCrawler {



    public String getAnswer(String questionId, String AnswerId) {

        StringBuilder stringBuilder = new StringBuilder("http://zhihu.com/problem/");
        stringBuilder.append(questionId);
        stringBuilder.append("/answer/");
        stringBuilder.append(AnswerId);
        return this.getContent(stringBuilder.toString());
    }

    public void getQuestion(String questionId) {

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
