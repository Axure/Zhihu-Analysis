package info.axurez.facade.network;

import java.util.concurrent.Future;

/**
 * Created by axurez on 2016/2/4.
 */
public interface HttpClient {

    HttpRequest syncRequest(HttpRequest request);
    Future<HttpClient> asyncRequest(HttpRequest request);
    // TODO: solve the problems of chaining and redirection.
    // TODO: we should have a smart way to jump out of the process and start again.
    // TODO: so we need more states than provided in that async http client.


}
