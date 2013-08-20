package io.github.uvpoblotzki.httpclientcmd;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class PrintResponseHeaderHandler implements HttpResponseHandler {

  @Override
  public void handle(HttpResponse response) {
    if (response == null) return;

    System.out.println(response.getStatusLine());
    for (Header header: response.getAllHeaders()) {
      System.out.println(header.getName() + ": " + header.getValue());
    }
  }

}
