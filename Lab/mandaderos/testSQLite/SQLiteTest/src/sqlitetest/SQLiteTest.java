package sqlitetest;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.File;

public class SQLiteTest {

    public static void main(String[] args) throws SQLiteException {
        SQLiteConnection sqLiteConnection = new SQLiteConnection(new File("Hola.db"));
        sqLiteConnection.open();
        sqLiteConnection.exec(
"CREATE TABLE \"Persona\" (\n" +
"    \"id\" INTEGER PRIMARY KEY,\n" +
"    \"primer_nombre\" TEXT NOT NULL,\n" +
"    \"primer_apellido\" TEXT\n" +
")");
        sqLiteConnection.exec(
"INSERT INTO \"Persona\" (\"id\", \"primer_nombre\", \"primer_apellido\") VALUES (1,'hola','hola')"
);
//        SQLiteStatement prepare = sqLiteConnection.prepare(
//"SELECT \"primer_nombre\" FROM \"Persona\" WHERE \"Persona\" = ?"
//);
        SQLiteStatement prepare = sqLiteConnection.prepare(
"SELECT \"primer_nombre\" FROM \"Persona\""
);
        SQLiteStatement res = prepare;
//        SQLiteStatement res = prepare.bind(1, "hola");
        while(res.step()){
//        while(res.hasRow()){
            Object columnValue = res.columnValue(0);
            System.out.println(columnValue);
        }
    }
}
