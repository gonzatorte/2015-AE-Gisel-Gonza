package Map.Api;

import Map.LightDistanceTable;
import Map.Place;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class DistanceWebCrawler {
    private String consulta_origen;
    private String consulta_insert;
    private SQLiteConnection db_con;
    
    public DistanceWebCrawler() throws SQLiteException{
        SQLiteConnection sqLiteConnection = new SQLiteConnection(new File("./instances/distancias.db"));
        sqLiteConnection.open(true);
        this.db_con = sqLiteConnection;
        this.db_con.exec(
"CREATE TABLE IF NOT EXISTS \"Distances\" (\n" +
"    \"origin\" TEXT NOT NULL,\n" +
"    \"destination\" TEXT NOT NULL,\n" +
"    \"distance\" REAL NOT NULL,\n" +
"    PRIMARY KEY (\"origin\", \"destination\")\n" +
");\n"
        );
//        this.consulta_posiciones_rango = sqLiteConnection.prepare(
//"SELECT * FROM \"Distances\" WHERE \"primer_nombre\" = ?"
//);
        this.consulta_origen = 
"SELECT \"origin\",\"destination\",\"distance\" FROM \"Distances\" WHERE (\"origin\" = \"%s\" OR \"destination\" = \"%s\") " +
"AND ((\"destination\" IN (%s)) OR (\"origin\" IN (%s)))"
;
        this.consulta_insert = 
"INSERT INTO \"Distances\" (\"origin\",\"destination\",\"distance\") %s"
;
    }
    
    public HashMap<Place, Double> crawl(Place origen, List<Place> destinos) throws UnsupportedEncodingException, IOException, SAXException, ParserConfigurationException, SQLiteException{
        HashMap<Place, Double> new_distances = new HashMap<Place, Double>();
        if (destinos.isEmpty()){
            return new_distances;
        }
        LinkedList<Place> target_destinos = new LinkedList<Place>(destinos);
        LinkedList<String> destinos_str_list = new LinkedList<String>();
        for (Place destino : target_destinos){
            destinos_str_list.add("\"" + destino.place_id + "\"");
        }
        String destinos_str = String.join(",", destinos_str_list);
        String sql_query = String.format(this.consulta_origen, origen.place_id, origen.place_id, destinos_str, destinos_str);
        SQLiteStatement compiled_query = this.db_con.prepare(sql_query);
        compiled_query.step();
        while(compiled_query.hasRow()){
            String destination_id = compiled_query.columnString(0);
            String origin_id = compiled_query.columnString(1);
            if (!origin_id.equals(origen.place_id)){
                destination_id = origin_id;
            }
            double distance = compiled_query.columnDouble(2);
            compiled_query.step();
            for (Place aux_d : target_destinos){
                if (aux_d.place_id.equals(destination_id)){
                    new_distances.put(aux_d, distance);
                    target_destinos.remove(aux_d);
                    break;
                }
            }
        }
        compiled_query.dispose();
        if (target_destinos.isEmpty()){
            return new_distances;
        }
        URL url_api = this.create_call(origen, target_destinos);
        BufferedInputStream call = this.call(url_api);
        new_distances = this.process_response(call, target_destinos);
        
        LinkedList<String> resultados_str_list = new LinkedList<String>();
        for (Map.Entry<Place,Double> dd : new_distances.entrySet()){
            resultados_str_list.add("SELECT \"" + origen.place_id + "\",\"" + dd.getKey().place_id + "\"," + dd.getValue());
        }
        String resultados_str = String.join(" UNION ALL ", resultados_str_list);
        String sql_insert = String.format(this.consulta_insert, resultados_str);
        SQLiteStatement compiled_insert = this.db_con.prepare(sql_insert);
        compiled_insert.step();
        compiled_insert.dispose();
        return new_distances;
    }
    
    private BufferedInputStream call(URL url_api) throws MalformedURLException, UnsupportedEncodingException, IOException{
        InputStream web_stream = url_api.openStream();
        BufferedInputStream buff_web_stream = new BufferedInputStream(web_stream);
        return buff_web_stream;
    }
    
    /*
    elements = origins * destinations
    100 elements per query.
    100 elements per 10 seconds. (????)
    2500 elements per 24 hour period.
    */

    // ToDo: hacer llamadas de cada 9 places... sino de mas de 100 resultados
    // Luego ver como hacer una recorrida mas inteligente, eliminando las simetrias y reflexiones
    private URL create_call(Place origen, List<Place> destinos) throws MalformedURLException {
        String base_url = "maps.googleapis.com/maps/api/distancematrix/xml";
        String coordinate_format = "%f,%f";
        
        ArrayList all_destinos_str = new ArrayList();
        for (Place p : destinos) {
            String location = String.format(Locale.ENGLISH, coordinate_format, p.coord.latit, p.coord.longit);
            all_destinos_str.add(location);
        }
        String destinations = String.join("|", all_destinos_str);
        destinations = "destinations=" + destinations;

        String origen_str = String.format(Locale.ENGLISH, coordinate_format, origen.coord.latit, origen.coord.longit);
        String origins = "origins=" + origen_str;
        
        String options = "mode=walking&language=es&units=metric";
        String apikey = "key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs";
        String api_format = "https://%s?%s&%s&%s&%s";
        String strurl = String.format(api_format, base_url, origins, destinations, apikey, options);
//          new URL(base_url_api_distance + "origins=41.43206,-81.38992&destinations=41.43206,-83.38992&mode=bicycling&language=es&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs");
        URL url_api = new URL(strurl);
        return url_api;
    }

    private HashMap<Place, Double> process_response(BufferedInputStream web_stream, List<Place> destinos) throws SAXException, IOException, ParserConfigurationException {
        InputSource web_source = new InputSource(web_stream);
        DocumentBuilderFactory dom_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dom_factory.newDocumentBuilder();
        Document document = builder.parse(web_source);
        NodeList childNodes = document.getFirstChild().getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++){
            Node item = childNodes.item(j);
            if ((item.getNodeValue() != null) && ("".equals(item.getNodeValue().trim()))){
                continue;
            }
            Element elem_i = (Element) item;
            if ("status".equals(elem_i.getTagName())){
                String statusValue = elem_i.getChildNodes().item(0).getNodeValue();
                if (!"OK".equals(statusValue)){
                    throw new Error("Llamada fallida " + statusValue);
                }
                break;
            }
        }
        NodeList rowNodeList = document.getElementsByTagName("row");
        HashMap<Place, Double> interMap = new HashMap<Place, Double>();
        Node rowNode = rowNodeList.item(0);
        NodeList elementNodeList = ((Element) rowNode).getElementsByTagName("element");
        for (int j = 0; j < elementNodeList.getLength(); j++){
            Node elementNode = elementNodeList.item(j);
            Node statusNode = ((Element) elementNode).getElementsByTagName("status").item(0);
            String status_str = ((Element) statusNode).getFirstChild().getNodeValue();
            if (!"OK".equals(status_str)){
                throw new Error("Llamada fallida " + status_str);
            }
            Node distanceNode = ((Element) elementNode).getElementsByTagName("distance").item(0);

            String distance_str = ((Element) distanceNode).getElementsByTagName("value")
                        .item(0).getChildNodes().item(0).getNodeValue();

            double distance = Double.parseDouble(distance_str);

            interMap.put(destinos.get(j), distance);
        }
        return interMap;
    }
}
