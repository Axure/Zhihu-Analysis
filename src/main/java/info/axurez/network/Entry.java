package info.axurez.network;

import com.ning.http.client.*;

import java.util.concurrent.Future;

/**
 * Created by zhenghu on 2016-01-23.
 */

/**
 * Entry point for the crawling.
 */
public class Entry {

    public static void main(String args[]) {
        System.out.println("haha");
        try {
            System.out.println("haha1");
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            Future<Response> f = asyncHttpClient
                .prepareGet("https://zhihu.com")
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {
                        return response;
                    }

                    @Override
                    public STATE onStatusReceived(HttpResponseStatus status) throws Exception {
                        int statusCode = status.getStatusCode();
                        // The Status have been read
                        // If you don't want to read the headers,body or stop processing the response
                        if (statusCode == 500) {
                            return STATE.ABORT;
                        }

                        if (statusCode == 301) {
                            System.out.println();
                        }
                        return STATE.CONTINUE;
                    }

                    @Override
                    public STATE onHeadersReceived(HttpResponseHeaders h) throws Exception {
//                        Headers headers = h.getHeaders();
                        // The headers have been read
                        // If you don't want to read the body, or stop processing the response
                        return STATE.ABORT;
                    }
                });
            Response r = f.get();
            System.out.println(r.getResponseBody());
        } catch (Exception e) {
            System.out.println("hah2");
            e.printStackTrace();
        } finally {
            System.out.println("haha3");
        }
        System.out.println("haha4");


    }
}
