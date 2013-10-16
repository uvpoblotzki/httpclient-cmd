package io.github.uvpoblotzki.httpclientcmd;

import org.apache.commons.cli.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Client {

  private static final Logger logger = LoggerFactory.getLogger(Client.class);

  private static final String OPT_SHORT_HEADER = "I";
  private static final String OPT_LONG_HEADER = "head";
  private static final String OPT_SHORT_LOCATION = "L";
  private static final String OPT_LONG_LOCATION = "location";
  private static final String OPT_SHORT_ADD_HEADER = "H";
  private static final String OPT_LONG_ADD_HEADER = "header";

  private CommandLine options;

  public static void main(String[] args) {
    try {
      CommandLine options = parseCommandLine(args);

      Client client = new Client(options);
      client.run();
    } catch (ParseException e) {
      logger.error("Error parsing args {}", args, e);
      System.err.println("Parsing failed. Reason: " + e.getMessage());
    }

  }

  public Client(CommandLine options) {
    this.options = options;
  }

  private String getUrl() {
    if (this.options == null) return null;
    if (this.options.getArgs().length == 0) {
      throw new IllegalArgumentException("no url given");
    }
    return this.options.getArgs()[0];
  }

  public void run() {
    HttpClient httpClient = createHttpClient();
    HttpUriRequest request = getHttpUriRequest();
    HttpResponseHandler handler = getResponseHandler();
    try {
      handler.handle(httpClient.execute(request));
    } catch (IOException e) {
      logger.error("Error executing request: {}", e.getMessage(), e);
      System.err.println(e.getMessage());
    } catch (RuntimeException e) {
      logger.error("Error executing request: {}", e.getMessage(), e);
      System.err.println(e.getMessage());
      request.abort();
    }
    httpClient.getConnectionManager().shutdown();
  }

  private HttpUriRequest getHttpUriRequest() {
    //TODO: Implement HEAD and POST requests
    HttpUriRequest request = new HttpGet(getUrl());
    HttpParams params = new BasicHttpParams();
    if (!this.options.hasOption(OPT_SHORT_LOCATION)) {
      params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
      request.setParams(params);
    }
    if (this.options.hasOption(OPT_SHORT_ADD_HEADER)) {
      String[] headers = this.options.getOptionValues(OPT_SHORT_ADD_HEADER);
      for (String header: headers) {
        String[] nameValue = header.split(":");
        if (nameValue.length == 2 && nameValue[0] != null && nameValue[1] != null) {
          request.addHeader(nameValue[0], nameValue[1]);
        }
      }
    }
    return request;
  }

  private HttpResponseHandler getResponseHandler() {
    HttpResponseHandler handler = null;

    if (this.options.hasOption(OPT_SHORT_HEADER)) {
      handler = new PrintResponseHeaderHandler();
    } else {
      handler = new PrintResponseContentHandler();
    }

    return handler;
  }

  private HttpClient createHttpClient() {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    if (this.options.hasOption(OPT_SHORT_HEADER)) {
      httpClient.setRedirectStrategy(new PrintResponseHeaderRedirectStrategy());
    }
    return httpClient;
  }

  private static CommandLine parseCommandLine(String[] args) throws ParseException {
    CommandLineParser parser = new PosixParser();
    return parser.parse(commandLineOptions(), args);
  }

  private static Options commandLineOptions () {
    Options options = new Options();

    options.addOption(OPT_SHORT_HEADER, OPT_LONG_HEADER, false, "Show document info only");
    options.addOption(OPT_SHORT_LOCATION, OPT_LONG_LOCATION, false, "Follow redirects");
    options.addOption(OPT_SHORT_ADD_HEADER, OPT_LONG_ADD_HEADER, true, "Custom header to parse to the server");

    return options;
  }

}
