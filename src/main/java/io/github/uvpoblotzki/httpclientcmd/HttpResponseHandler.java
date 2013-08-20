package io.github.uvpoblotzki.httpclientcmd;

import org.apache.http.HttpResponse;

public interface HttpResponseHandler {

  void handle(HttpResponse response);

}
