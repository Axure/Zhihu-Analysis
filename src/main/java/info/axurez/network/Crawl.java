package info.axurez.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.*;
import sun.rmi.runtime.Log;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Future;

/**
 * Created by axurez on 2016/2/2.
 */
class TypeMismatchException extends Exception {
    TypeMismatchException(String message) {
        super(message);
    }
}

enum REDIRECT {
    YES,
    NO
}

class HttpRequestInfo {
    private REDIRECT redirect;
    private String contentOrUrl;

    HttpRequestInfo() {
        this.redirect = REDIRECT.NO;
    }

    HttpRequestInfo(REDIRECT redirect, String contentOrUrl) {
        this.redirect = redirect;
        this.contentOrUrl = contentOrUrl;
    }

    String getContent() throws TypeMismatchException {
        if (redirect() == REDIRECT.YES) {
            throw new TypeMismatchException("The response does not need redirection.");
        } else {
            return this.contentOrUrl;
        }
    }

    String getUrl() throws TypeMismatchException {
        if (redirect() == REDIRECT.NO) {
            throw new TypeMismatchException("The response does not need redirection.");
        } else {
            return this.contentOrUrl;
        }
    }

    REDIRECT redirect() {
        return this.redirect;
    }
}

public class Crawl {
    Logger logger;
    public Crawl() {
        logger = LoggerFactory.getLogger(Crawl.class);
    }

    public String getContent(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            HttpRequestInfo httpRequestInfo = new HttpRequestInfo(REDIRECT.YES, url);
            while (httpRequestInfo.redirect() == REDIRECT.YES) {
                logger.info("Requesting " + httpRequestInfo.getUrl());
                Future<HttpRequestInfo> f = asyncHttpClient.prepareGet(httpRequestInfo.getUrl()).execute(new AsyncHandler<HttpRequestInfo>() {
                    private ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    private REDIRECT needsRedirect = REDIRECT.NO;
                    private String redirectUrl;

                    @Override
                    public STATE onStatusReceived(HttpResponseStatus status) throws Exception {
                        int statusCode = status.getStatusCode();
                        // The Status have been read
                        // If you don't want to read the headers,body or stop processing the response
                        if (statusCode >= 500) {
                            return STATE.ABORT;
                        }
                        if (statusCode == 301 || statusCode == 302) {
                            needsRedirect = REDIRECT.YES;
                        }
                        return STATE.CONTINUE;
                    }

                    @Override
                    public STATE onHeadersReceived(HttpResponseHeaders h) throws Exception {
                        // The headers have been read
                        if (needsRedirect == REDIRECT.YES) {
                            FluentCaseInsensitiveStringsMap headers = h.getHeaders();
                            redirectUrl = headers.get("Location").get(0);
                            return STATE.ABORT;
                        }
                        return STATE.CONTINUE;
                    }

                    @Override
                    public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                        bytes.write(bodyPart.getBodyPartBytes());
                        return STATE.CONTINUE;
                    }

                    @Override
                    public HttpRequestInfo onCompleted() throws Exception {
                        // Will be invoked once the response has been fully read or a ResponseComplete exception
                        // has been thrown.
                        // NOTE: should probably use Content-Encoding from headers
                        HttpRequestInfo result;
                        if (needsRedirect == REDIRECT.NO) {
                            result = new HttpRequestInfo(REDIRECT.NO, bytes.toString("UTF-8"));
                            return result;
                        } else {
                            result = new HttpRequestInfo(REDIRECT.YES, redirectUrl);
                        }
                        return result;
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                    }
                });
                httpRequestInfo = f.get();
            }
            stringBuilder.append(httpRequestInfo.getContent());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
