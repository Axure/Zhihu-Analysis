package info.axurez.network.http;

import info.axurez.database.entities.RequestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ning.http.client.*;

import javax.persistence.EntityManager;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

/**
 * TODO: Our crawler should be independent of the http implementations. So we need a proxy or wrapper or adapter.
 */
public class AsyncHttpClientCrawler implements Crawler {
    protected Logger logger;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected EntityManager entityManager;

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public AsyncHttpClientCrawler() {
        logger = LoggerFactory.getLogger(AsyncHttpClientCrawler.class);
    }

    public AsyncHttpClientCrawler(Logger logger) {
        this.logger = logger;
    }

    public Future<String> getContentAsync(String url) {
        Future<String> future = new Future<String>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public String get(long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
        return future;
    }

    public String getContent(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            entityManager.getTransaction().begin();
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient(); // TODO: DI.
            HttpRequestInfo httpRequestInfo = new HttpRequestInfo(REDIRECT.YES, url);
            while (httpRequestInfo.redirect() == REDIRECT.YES) {
                String requestUrl = httpRequestInfo.getUrl();
                logger.info("Requesting " + requestUrl);
                entityManager.persist(new RequestLog(requestUrl, new Date()));
                Future<HttpRequestInfo> f = asyncHttpClient
                    .prepareGet(requestUrl)
                    .execute(new AsyncHandler<HttpRequestInfo>() {
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
                if (httpRequestInfo.redirect() == REDIRECT.YES) {
                    logger.warn("Redirection happening! May decrease performance!");
                }
            }
            stringBuilder.append(httpRequestInfo.getContent());
            asyncHttpClient.close();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
