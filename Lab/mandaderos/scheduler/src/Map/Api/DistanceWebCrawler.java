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
    public List<Place> destinos;
    public Place origen;
    private SQLiteStatement consulta_posiciones_rango;
    private SQLiteStatement consulta_origen;
    private SQLiteStatement consulta_insert;
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
        this.consulta_origen = sqLiteConnection.prepare(
"SELECT \"destination\",\"distance\" FROM \"Distances\" WHERE \"origin\" = ?"
);
        this.consulta_insert = sqLiteConnection.prepare(
"INSERT INTO \"Distances\" (\"origin\",\"destination\",\"distance\") VALUES (?,?,?)"
);
    }
    
    public HashMap<Place, Double> crawl() throws UnsupportedEncodingException, IOException, SAXException, ParserConfigurationException, SQLiteException{
        HashMap<Place, Double> new_distances = new HashMap<Place, Double>();
        if (this.destinos.size() == 0){
            return new_distances;
        }
        //ToDo, mejorar esto para tener varios origines a la vez.
        this.consulta_origen.bind(1, this.origen.place_id);
        this.consulta_origen.step();
        if (this.consulta_origen.hasRow()){
            while(this.consulta_origen.hasRow()){
                String destination_id = this.consulta_origen.columnString(0);
                double distance = this.consulta_origen.columnDouble(1);
                this.consulta_origen.reset();
                this.consulta_origen.step();
                Place destination = null;
                for (Place aux_d : this.destinos){
                    if (aux_d.place_id.equals(destination_id)){
                        destination = aux_d;
                        break;
                    }
                }
                new_distances.put(destination, distance);
            }
        } else {
            URL url_api = this.create_call();
            BufferedInputStream call = this.call(url_api);
            new_distances = this.process_response(call);
//            this.db_con.exec("BEGIN");
            for (Map.Entry<Place,Double> dd : new_distances.entrySet()){
                this.consulta_insert.bind(1, this.origen.place_id);
                this.consulta_insert.bind(2, dd.getKey().place_id);
                this.consulta_insert.bind(3, dd.getValue());
                this.consulta_insert.step();
                this.consulta_insert.reset();
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(DistanceWebCrawler.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
//            this.db_con.exec("COMMIT");
        }
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
    private URL create_call() throws MalformedURLException {
        String base_url = "maps.googleapis.com/maps/api/distancematrix/xml";
        String coordinate_format = "%f,%f";
        
        ArrayList all_destinos_str = new ArrayList();
        for (Place p : this.destinos) {
            String location = String.format(Locale.ENGLISH, coordinate_format, p.coord.latit, p.coord.longit);
            all_destinos_str.add(location);
        }
        String destinations = String.join("|", all_destinos_str);
        destinations = "destinations=" + destinations;

        String origen_str = String.format(Locale.ENGLISH, coordinate_format, this.origen.coord.latit, this.origen.coord.longit);
        String origins = "origins=" + origen_str;
        
        String options = "mode=walking&language=es&units=metric";
        String apikey = "key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs";
        String api_format = "https://%s?%s&%s&%s&%s";
        String strurl = String.format(api_format, base_url, origins, destinations, apikey, options);
//          new URL(base_url_api_distance + "origins=41.43206,-81.38992&destinations=41.43206,-83.38992&mode=bicycling&language=es&key=AIzaSyC0cd3JlrPkMuoDH2GMW_DSDAXV0vTmROs");
        URL url_api = new URL(strurl);
        return url_api;
    }

    private HashMap<Place, Double> process_response(BufferedInputStream web_stream) throws SAXException, IOException, ParserConfigurationException {
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

            interMap.put(this.destinos.get(j), distance);
        }
        return interMap;
    }
}
