package info.axurez.network;

import com.ning.http.client.*;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Future;

/**
 * Created by axurez on 2016/2/2.
 */
public class Crawl {
    public String getContent(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            Future<String> f = asyncHttpClient.prepareGet(url).execute(new AsyncHandler<String>() {
                private ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                @Override
                public STATE onStatusReceived(HttpResponseStatus status) throws Exception {
                    int statusCode = status.getStatusCode();
                    // The Status have been read
                    // If you don't want to read the headers,body or stop processing the response
                    if (statusCode >= 500) {
                        return STATE.ABORT;
                    }
                    return STATE.CONTINUE;
                }

                @Override
                public STATE onHeadersReceived(HttpResponseHeaders h) throws Exception {
                    // The headers have been read
                    return STATE.CONTINUE;
                }

                @Override
                public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                    bytes.write(bodyPart.getBodyPartBytes());
                    return STATE.CONTINUE;
                }

                @Override
                public String onCompleted() throws Exception {
                    // Will be invoked once the response has been fully read or a ResponseComplete exception
                    // has been thrown.
                    // NOTE: should probably use Content-Encoding from headers
                    return bytes.toString("UTF-8");
                }

                @Override
                public void onThrowable(Throwable t) {
                }
            });
            stringBuilder.append(f.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
