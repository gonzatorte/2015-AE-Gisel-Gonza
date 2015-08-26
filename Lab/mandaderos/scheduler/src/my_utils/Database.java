package my_utils;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.File;

public class Database {
    SQLiteConnection places_connection = new SQLiteConnection(new File("./instances/places.db"));
    private SQLiteStatement consulta_get_lugar;
    private SQLiteStatement consulta_get_borders;
    private SQLiteStatement consulta_insert_lugar;
    
    private SQLiteConnection distances_connection = new SQLiteConnection(new File("./instances/distancias.db"));
    private SQLiteStatement consulta_posiciones_rango;
    private SQLiteStatement consulta_origen;
    private SQLiteStatement consulta_insert;
    
    public Database() throws SQLiteException{
        distances_connection.open(true);
        distances_connection.exec(
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
        this.consulta_origen = distances_connection.prepare(
"SELECT \"origin\",\"destination\",\"distance\" FROM \"Distances\" WHERE \"origin\" = :lugar OR \"destination\" = :lugar"
);
        this.consulta_insert = distances_connection.prepare(
"INSERT INTO \"Distances\" (\"origin\",\"destination\",\"distance\") VALUES (?,?,?)"
);
        
        places_connection.open(false);
        places_connection.exec(
"CREATE TABLE IF NOT EXISTS \"Places\" (\n" +
"    \"place_id\" TEXT NOT NULL,\n" +
"    \"latitud\" REAL NOT NULL,\n" +
"    \"longitud\" REAL NOT NULL,\n" +
"    PRIMARY KEY (\"place_id\")\n" +
");"
        );
        this.consulta_get_borders = places_connection.prepare(
"SELECT min(\"latitud\"), max(\"latitud\"), min(\"longitud\"), max(\"longitud\") "+
"FROM \"Places\" WHERE ? > \"longitud\" AND \"longitud\" > ? AND "+
"? > \"latitud\" AND \"latitud\" > ?"
);
        this.consulta_get_lugar = places_connection.prepare(
"SELECT \"place_id\", \"latitud\", \"longitud\" "+
"FROM \"Places\" WHERE ? > \"longitud\" AND \"longitud\" > ? AND "+
"? > \"latitud\" AND \"latitud\" > ?"
);
        this.consulta_insert_lugar = places_connection.prepare(
"INSERT INTO \"Places\" (\"place_id\", \"latitud\", \"longitud\") VALUES (?,?,?)"
);
    }
}
