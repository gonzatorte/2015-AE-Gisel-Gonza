//=============================================================================
// Copyright 2006-2010 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//=============================================================================
package pkg_backpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.stream.Stream;
import org.uncommons.watchmaker.framework.FitnessEvaluator;


public class Fitness implements FitnessEvaluator<List<Integer>>
{
    static List<Integer> ganancias= new ArrayList<>();
    static List<Integer> pesos= new ArrayList<>();
   public static Integer n;
   public static Integer p;
   public static Integer w;
    public Fitness(String textIn)
    {
        
        // Open the file
        FileInputStream fstream=null;
        try {
            
            fstream = new FileInputStream(textIn);
        
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
           
            //Read File Line By Line
            String todo=br.readLine();
            n=Integer.parseInt(todo);
            
            for (int i=0;i<n;i++)   {
                todo=br.readLine();
              String[] arrS= todo.split(" ");
              ganancias.add(Integer.parseInt(arrS[0])-1,Integer.parseInt(arrS[1]));
              pesos.add(Integer.parseInt(arrS[0])-1,Integer.parseInt(arrS[2]));
              
            }
            todo=br.readLine();
            w=Integer.parseInt(todo);
            //Close the input stream
            br.close();
        } catch (Exception ex) {
            Logger.getLogger(Fitness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getFitness(List<Integer> ind,
                             List<? extends List<Integer>> population)
    {
        int ganancia = 0;
        int peso = 0;
        for(int i=0;i<n;i++){
            if(ind.get(i)==1){
                ganancia += ganancias.get(i);
                peso += pesos.get(i);
            }
                    
        }
        p=peso;
        if (peso<=w){
            return ganancia;
        }
        else{
            return (ganancia - ((2*ganancia*(peso-w))/peso));
        }
        
    }


    public boolean isNatural()
    {
        return true;
    }
}
