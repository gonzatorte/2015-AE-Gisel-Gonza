package Map.Api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import my_utils.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import Map.Place;
import java.util.Locale;

public class PlacesWebCrawler extends WebCrawler {
    
    public PlacesWebCrawler(){}

    public double location_x;
    public double location_y;
    public int radius;

    public URL create_call() throws MalformedURLException {
        String base_url = "maps.googleapis.com/maps/api/place/radarsearch/xml";
        String coordinate_format = "%f,%f";
        String location = String.format(Locale.ENGLISH, coordinate_format, location_x, location_y);
        location = "location=" + location;
        String radius_format = "radius=%d";
        String radius_str = String.format(Locale.ENGLISH, radius_format, radius);
        String filter = "types=museum";
        String options = "mode=bicycling&language=es";
        String apikey = "key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs";
        String api_format = "https://%s?%s&%s&%s&%s&%s";
        String strurl = String.format(api_format, base_url, location, radius_str, filter, apikey, options);
        URL url_api = new URL(strurl);
        return url_api;
    }

    public List<Place> process_response() throws SAXException, IOException, ParserConfigurationException {
        BufferedInputStream web_stream = this.call();
        InputSource web_source = new InputSource(web_stream);
        DocumentBuilderFactory dom_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dom_factory.newDocumentBuilder();
        Document document = builder.parse(web_source);
        NodeList nodeList = document.getElementsByTagName("result");
        List<Place> places;
        places = new ArrayList<Place>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            String latit_str = ((Element) node).getElementsByTagName("lat")
                        .item(0).getChildNodes().item(0).getNodeValue();
            
            String longit_str = ((Element) node).getElementsByTagName("lng")
                        .item(0).getChildNodes().item(0).getNodeValue();

            String place_id = ((Element) node).getElementsByTagName("place_id")
                        .item(0).getChildNodes().item(0).getNodeValue();

            double latit = Double.parseDouble(latit_str);
            double longit = Double.parseDouble(longit_str);

            places.add(new Place(place_id, latit, longit));
        }
        return places;
    }
}
