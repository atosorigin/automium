package com.nithind.automium.reporters;

import com.nithind.automium.utils.PropertyConfig;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by a592282 on 18-07-2016.
 */
/*{
        "text": "This is a line of text in a channel.\nAnd this is another line of text.",
        "username":"GCS Automation Result"
        }

        {
   "attachments":[
      {
         "pretext":"GCS Automation have some errors.",
         "color":"#D00000",
         "fields":[
            {
               "title":"Result",
               "value":"There are 3 failure(s) and 8 warning(s).",
               "short":false
            }
         ]
      }
   ]
}

"color":"#D00000", error
36a64f green success
#edb431 amber warning.

        */
public class SlackReporter {
    private final String USER_AGENT = "Mozilla/5.0";

    public static boolean updateSlack(String message) throws Exception {
        try {
            String url = PropertyConfig.getProperty("slack.result.post.url");
            URL obj = new URL(url);
            HttpURLConnection conn = null;

            String proxyEnabled = PropertyConfig.getProperty("slack.result.post.proxy.enabled");
            String proxyServer = PropertyConfig.getProperty("slack.result.post.proxy.host");
            String proxyPort = PropertyConfig.getProperty("slack.result.post.proxy.port");
            final String proxyUsername = PropertyConfig.getProperty("slack.result.post.proxy.username");
            final String proxyPassword = PropertyConfig.getProperty("slack.result.post.proxy.password");


            if (proxyEnabled != null && proxyEnabled.contains("true") &&  null != proxyPort && null!=proxyServer) {
                if (proxyUsername != null && proxyPassword !=  null) {
                    Authenticator authenticator = new Authenticator() {

                        public PasswordAuthentication getPasswordAuthentication() {
                            return (new PasswordAuthentication(proxyUsername,
                                    proxyPassword.toCharArray()));
                        }
                    };
                    Authenticator.setDefault(authenticator);
                }
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyServer, Integer.parseInt(proxyPort)));
                conn = (HttpURLConnection) obj.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) obj.openConnection();
            }
            //HttpURLConnection.setFollowRedirects(true);

            //conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            //conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Content-Type", "application/rdf+xml");
            OutputStream output = new BufferedOutputStream(conn.getOutputStream());
            output.write(message.getBytes());
            output.flush();
            int status = conn.getResponseCode();
            System.out.println("updates slack status code  " + status);
            return true;
        } catch (ConnectException e) {
            throw new Exception("Unable to connect to slack. Check you added proxy settings in your config if needed");
            //return false;
        }
    }
}