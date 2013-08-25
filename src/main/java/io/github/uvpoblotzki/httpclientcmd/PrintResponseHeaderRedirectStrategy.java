package io.github.uvpoblotzki.httpclientcmd;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

public class PrintResponseHeaderRedirectStrategy extends DefaultRedirectStrategy {

  @Override
  public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
    boolean isRedirect = super.isRedirected(request, response, context);

    if (isRedirect) {
      System.out.println(response.getStatusLine());
      for (Header header: response.getAllHeaders()) {
        System.out.println(header.getName() + ": " + header.getValue());
      }
      System.out.println('\n');
    }

    return isRedirect;
  }
}
