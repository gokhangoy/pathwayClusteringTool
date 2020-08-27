import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class GenesClass {

    static int counter=0,numberOfUniqueGenes=0;
    static ArrayList<String> allGenesInFile = new ArrayList<>();
    static HashSet<String> allUniqueGenes = new HashSet<>();

    private String[] allPathways;
    private String[] allGeneIDList;

    private ArrayList<String> wholeGenesInOnePathway = new ArrayList<>();
    private ArrayList<String> geneAndRepoInfo = new ArrayList<>();
    private ArrayList<String> wholePathwayNames = new ArrayList<>();


     GenesClass operativeClass;



    public GenesClass(){

    }


    public GenesClass GeneOperations(ReadElements readElements) throws IOException {


        wholeGenesInOnePathway = readElements.readTheGeneGroup();
        geneAndRepoInfo = readElements.readTheGenesID();
        wholePathwayNames = readElements.readThePathways();

        allGeneIDList = new String[geneAndRepoInfo.size() - 1];  //-1 lar yine header bilgisinden dolayı
        allPathways = new String[geneAndRepoInfo.size()-1];

        operativeClass =new GenesClass();
        operativeClass.setWholeGenesInOnePathway(wholeGenesInOnePathway);
        operativeClass.setGeneAndRepoInfo(geneAndRepoInfo);
        operativeClass.setWholePathwayNames(wholePathwayNames);
        operativeClass.setAllGeneIDList(allGeneIDList);
        operativeClass.setAllPathways(allPathways);



        return operativeClass;

    }

    public static void AddToHash(String[] seperateGenesOfOnePathway)
    {

        boolean flag = false;  //Eger flag true ise genes[] dizisine de ekliyoruz. term-gene matrisini oluşturabilmek için...
        int counter = 0;


        for (int i = 0; i < seperateGenesOfOnePathway.length; i++)
        {

            flag = allUniqueGenes.add(seperateGenesOfOnePathway[i]);
            //Eğer hash'e eklenebiliyorsa bu elemanı gene term matrisinin hesaplanabilmesi için ayrı bir diziye almam gerek,yor karşılaştırma yapmak için
            if (flag == true)
            {
                //genes[k] = seperateGenesOfOnePathway[i];
                allGenesInFile.add(seperateGenesOfOnePathway[i]);
                numberOfUniqueGenes++;

            }
        }
    }




    public ArrayList<String> getWholeGenesInOnePathway() {
        return wholeGenesInOnePathway;
    }

    public void setWholeGenesInOnePathway(ArrayList<String> wholeGenesInOnePathway) {
        this.wholeGenesInOnePathway = wholeGenesInOnePathway;
    }

    public ArrayList<String> getGeneAndRepoInfo() {
        return geneAndRepoInfo;
    }

    public void setGeneAndRepoInfo(ArrayList<String> geneAndRepoInfo) {
        this.geneAndRepoInfo = geneAndRepoInfo;
    }

    public ArrayList<String> getWholePathwayNames() {
        return wholePathwayNames;
    }

    public void setWholePathwayNames(ArrayList<String> wholePathwayNames) {
        this.wholePathwayNames = wholePathwayNames;
    }


    public String[] getAllPathways() {
        return allPathways;
    }

    public void setAllPathways(String[] allPathways) {
        this.allPathways = allPathways;
    }

    public String[] getAllGeneIDList() {
        return allGeneIDList;
    }

    public void setAllGeneIDList(String[] allGeneIDList) {
        this.allGeneIDList =allGeneIDList;
    }

    public void setAllGeneIDList(String i) {
        allGeneIDList[counter] = i;
        counter++;
    }

    public void setAllPathways(int i) {
        allPathways[i] = wholePathwayNames.get(i+1);
    }

    public static int getNumberOfUniqueGenes() {
        return numberOfUniqueGenes;
    }

    public static ArrayList<String> getAllGenesInFile() {
        return allGenesInFile;
    }

    public static HashSet<String> getAllUniqueGenes() {
        return allUniqueGenes;
    }
}
