package Map.Api;

import Map.DistanceTable;
import Map.Place;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import my_utils.MapUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DistanceWebCrawler extends WebCrawler {
    public List<Place> destinos;
    public List<Place> origenes;
    
    public DistanceWebCrawler(){}
    
    /*
    elements = origins * destinations
    100 elements per query.
    100 elements per 10 seconds. (????)
    2500 elements per 24 hour period.
    */

    // ToDo: hacer llamadas de cada 9 places... sino de mas de 100 resultados
    // Luego ver como hacer una recorrida mas inteligente, eliminando las simetrias y reflexiones
    public URL create_call() throws MalformedURLException {
        String base_url = "maps.googleapis.com/maps/api/distancematrix/xml";
        String coordinate_format = "%f,%f";
        
        ArrayList all_destinos_str = new ArrayList();
        for (Place p : this.destinos) {
            String location = String.format(Locale.ENGLISH, coordinate_format, p.coord.latit, p.coord.longit);
            all_destinos_str.add(location);
        }
        String destinations = String.join("|", all_destinos_str);
        destinations = "destinations=" + destinations;

        ArrayList all_origenes_str = new ArrayList();
        for (Place p : this.origenes) {
            String location = String.format(Locale.ENGLISH, coordinate_format, p.coord.latit, p.coord.longit);
            all_origenes_str.add(location);
        }
        String origins = String.join("|", all_origenes_str);
        origins = "origins=" + origins;
        
        String options = "mode=walking&language=es&units=metric";
        String apikey = "key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs";
        String api_format = "https://%s?%s&%s&%s&%s";
        String strurl = String.format(api_format, base_url, origins, destinations, apikey, options);
        System.out.print(strurl);
//          new URL(base_url_api_distance + "origins=41.43206,-81.38992&destinations=41.43206,-83.38992&mode=bicycling&language=es&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs");
        URL url_api = new URL(strurl);
        return url_api;
    }

    public DistanceTable process_response() throws SAXException, IOException, ParserConfigurationException {
        BufferedInputStream web_stream = this.call();
        InputSource web_source = new InputSource(web_stream);
        DocumentBuilderFactory dom_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dom_factory.newDocumentBuilder();
        Document document = builder.parse(web_source);
        NodeList rowNodeList = document.getElementsByTagName("row");
        DistanceTable distances = new DistanceTable();
        for (int i = 0; i < rowNodeList.getLength(); i++) {
            HashMap<Place, Double> interMap = new HashMap<Place, Double>();
            Node rowNode = rowNodeList.item(i);
            NodeList elementNodeList = ((Element) rowNode).getElementsByTagName("element");
            for (int j = 0; j < elementNodeList.getLength(); j++){
                Node elementNode = elementNodeList.item(j);
                Node distanceNode = ((Element) elementNode).getElementsByTagName("distance").item(0);

                String distance_str = ((Element) distanceNode).getElementsByTagName("value")
                            .item(0).getChildNodes().item(0).getNodeValue();

                double distance = Double.parseDouble(distance_str);

                interMap.put(this.destinos.get(j), distance);
            }
            distances.put(this.origenes.get(i), interMap);
        }
        return distances;
    }
}
