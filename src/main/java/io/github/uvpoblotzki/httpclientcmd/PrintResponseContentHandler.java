package io.github.uvpoblotzki.httpclientcmd;

import com.google.common.io.ByteStreams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class PrintResponseContentHandler implements HttpResponseHandler {

  private static final Logger logger = LoggerFactory.getLogger(PrintResponseContentHandler.class);

  @Override
  public void handle(HttpResponse response) {
    if (response == null) return;
    HttpEntity entity = response.getEntity();

    if (entity != null) {
      InputStream inputStream = null;
      try {
        inputStream = entity.getContent();
        ByteStreams.copy(inputStream, System.out);
      } catch (IOException e) {
        logger.error("Error opening response content {}", e.getMessage(), e);
      } finally {
        if (inputStream != null) {
          try {inputStream.close();} catch (IOException e) {}
        }
      }


    }
  }
}
