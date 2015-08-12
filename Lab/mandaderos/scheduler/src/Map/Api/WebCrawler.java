package Map.Api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class WebCrawler {
    public abstract URL create_call() throws MalformedURLException;
    public BufferedInputStream call() throws MalformedURLException, UnsupportedEncodingException, IOException{
        URL url_api = this.create_call();
        InputStream web_stream = url_api.openStream();
        BufferedInputStream buff_web_stream = new BufferedInputStream(web_stream);
        return buff_web_stream;
//        InputStreamReader web_reader = new InputStreamReader(web_stream, "UTF-8");
//        BufferedReader reader = new BufferedReader(web_reader);
//        for (String line; (line = reader.readLine()) != null;) {
//            System.out.println(line);
//        }
//        return reader;

//        InputStreamReader web_reader = new InputStreamReader(web_stream, "UTF-8");
//        BufferedReader reader = new BufferedReader(web_reader);
//        for (String line; (line = reader.readLine()) != null;) {
//            System.out.println(line);
//        }
//        return reader;
    }
}
