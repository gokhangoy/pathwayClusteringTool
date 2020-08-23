import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadElements {
    static File genomicFile;

    public ReadElements(File file){

        genomicFile = file;
    }


    public ArrayList<String> readTheGeneGroup() throws IOException
    {

        ArrayList<String> allGenes = new ArrayList<>();
        BufferedReader readBufferVariable = null;

        try {
            String read = null;

            readBufferVariable = new BufferedReader(new FileReader(genomicFile));
            while ((read = readBufferVariable.readLine()) != null) {

                String[] splited = read.split("\\t");
                allGenes.add(splited[3]);//counter++;//Bütün dosyada 3. sıradaki elemanlar gen dizileri ama pipe'lı olanlar.
            }

        } catch (IOException e) {
            System.out.println("There was a problem: " + e);
            e.printStackTrace();
        } finally {
            try {
                readBufferVariable.close();
            } catch (Exception e) {
            }
        }

        return allGenes;
    }

    public  ArrayList<String> readThePathways() throws IOException
    {

        ArrayList<String> pathwayNames = new ArrayList<>();
        BufferedReader readBufferVariable = null;

        try {
            String read = null;

            readBufferVariable = new BufferedReader(new FileReader(genomicFile));
            while ((read = readBufferVariable.readLine()) != null) {

                String[] splited = read.split("\\t");
                pathwayNames.add(splited[2]);//counter++;//Bütün dosyada 2. sıradaki elemanlar yani pathway isimleri.
            }

        } catch (IOException e) {
            System.out.println("There was a problem: " + e);
            e.printStackTrace();
        } finally {
            try {
                readBufferVariable.close();
            } catch (Exception e) {
            }
        }

        return pathwayNames;
    }

    public  ArrayList<String> readTheGenesID() throws IOException
    {

        ArrayList<String> allGenesIDs = new ArrayList<>();
        //ArrayList<Integer> genesIDs = new ArrayList<>();
        String[] myGenes = null;

        BufferedReader readBufferVariable = null;
        String IDs = null;


        try {
            String read = null;

            readBufferVariable = new BufferedReader(new FileReader(genomicFile));
            while ((read = readBufferVariable.readLine()) != null) {

                String[] splited = read.split("\\t");
                allGenesIDs.add(splited[0]);
            }

        } catch (IOException e) {
            System.out.println("There was a problem: " + e);
            e.printStackTrace();
        } finally {
            try {
                readBufferVariable.close();
            } catch (Exception e) {
            }
        }

        return allGenesIDs;
    }

    public  ArrayList<String> readTheElementsWithWhiteSpaces(File file, int index) throws IOException{
        ArrayList<String> desiredValues = new ArrayList<>();
        BufferedReader readBufferVariable = null;

        try {
            String read = null;

            readBufferVariable = new BufferedReader(new FileReader(file));
            while ((read = readBufferVariable.readLine()) != null) {

                String[] splited = read.split("\\s+");
                desiredValues.add(splited[index]);//counter++;//Bütün dosyada 2. sıradaki elemanlar yani pathway isimleri.
            }

        } catch (IOException e) {
            System.out.println("There was a problem: " + e);
            e.printStackTrace();
        } finally {
            try {
                readBufferVariable.close();
            } catch (Exception e) {
            }
        }

        return desiredValues;

    }
}



