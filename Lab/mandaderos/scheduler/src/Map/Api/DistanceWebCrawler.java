package Map.Api;

import Map.Place;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import my_utils.DualMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DistanceWebCrawler extends WebCrawler {
    public List<Place> places;
    
    public DistanceWebCrawler(List<Place> places){
        this.places = places;
    }

    public URL create_call() throws MalformedURLException {
        String base_url = "maps.googleapis.com/maps/api/distancematrix/xml";
        String coordinate_format = "%f,%f";
        ArrayList all_location_str = new ArrayList();
        for (Place p : this.places) {
            // ToDo: hacer llamadas de cada 9 places... sino de mas de 100 resultados
            // Luego ver como hacer una recorrida mas inteligente, eliminando las simetrias y reflexiones
            String location = String.format(Locale.ENGLISH, coordinate_format, p.coord.latit, p.coord.longit);
            all_location_str.add(location);
        }
        String locations = String.join("|", all_location_str);
        String destinations = "destinations=" + locations;
        String origins = "origins=" + locations;
        String options = "mode=walking&language=es&units=metric";
        String apikey = "key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs";
        String api_format = "https://%s?%s&%s&%s&%s";
        String strurl = String.format(api_format, base_url, origins, destinations, apikey, options);
        System.out.print(strurl);
//          new URL(base_url_api_distance + "origins=41.43206,-81.38992&destinations=41.43206,-83.38992&mode=bicycling&language=es&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs");
        URL url_api = new URL(strurl);
        return url_api;
    }

    public DualMap<Place, Place, Double> process_response() throws SAXException, IOException, ParserConfigurationException {
        BufferedInputStream web_stream = this.call();
        InputSource web_source = new InputSource(web_stream);
        DocumentBuilderFactory dom_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dom_factory.newDocumentBuilder();
        Document document = builder.parse(web_source);
        NodeList rowNodeList = document.getElementsByTagName("row");
        DualMap<Place, Place, Double> distances = new DualMap<Place, Place, Double>();
        for (int i = 0; i < rowNodeList.getLength(); i++) {
            Node rowNode = rowNodeList.item(i);
            NodeList elementNodeList = ((Element) rowNode).getElementsByTagName("element");
            for (int j = 0; j < elementNodeList.getLength(); j++){
                Node elementNode = elementNodeList.item(i);
                Node distanceNode = ((Element) elementNode).getElementsByTagName("distance").item(0);

                String distance_str = ((Element) distanceNode).getElementsByTagName("value")
                            .item(0).getChildNodes().item(0).getNodeValue();

                double distance = Double.parseDouble(distance_str);

                distances.put(this.places.get(i), this.places.get(j), distance);
            }
        }
        return distances;
    }
}