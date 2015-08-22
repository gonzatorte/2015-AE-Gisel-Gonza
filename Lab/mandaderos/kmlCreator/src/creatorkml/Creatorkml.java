/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package creatorkml;
import de.micromata.opengis.kml.v_2_2_0.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Gisel
 */
public class Creatorkml {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        Kml kml = new Kml();
        Document document = kml.createAndSetDocument().withName("Ejemplo");
            Folder folder = document.createAndAddFolder().withName("Mandadero 1");
                folder.createAndAddPlacemark().createAndSetLineString()
                        .addToCoordinates(-112.2550785337791,36.07954952145647,2357)
                        .addToCoordinates(-112.2549277039738,36.08117083492122,2357)
                        .addToCoordinates(-112.2552505069063,36.08260761307279,2357)
                        .addToCoordinates(-112.2564540158376,36.08395660588506,2357)
                        .addToCoordinates(-112.2580238976449,36.08511401044813,2357);
                folder.createAndAddPlacemark().withName("Inicio").createAndSetPoint().addToCoordinates(-112.2550785337791,36.07954952145647,2357);
                folder.createAndAddPlacemark().createAndSetPoint().addToCoordinates(-112.2549277039738,36.08117083492122,2357);
                folder.createAndAddPlacemark().createAndSetPoint().addToCoordinates(-112.2552505069063,36.08260761307279,2357);
                folder.createAndAddPlacemark().createAndSetPoint().addToCoordinates(-112.2564540158376,36.08395660588506,2357);
                folder.createAndAddPlacemark().withName("Fin").createAndSetPoint().addToCoordinates(-112.2580238976449,36.08511401044813,2357);
        kml.marshal(new File("Ejemplo.kml"));
    }
    
}
