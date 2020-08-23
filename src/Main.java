import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Main {


    static String[][] geneTermMatrix;
    static ArrayList<String> genes = new ArrayList<>();
    static HashSet<String> allSingleGenes = new HashSet<>();
    static int  sizeOfGenes = 0, sizeOfGeneIDs = 0, sizeOfPathways = 0, k = 0, counter = 0;



    public static void main(String[] args) throws IOException {


        File ResultFile = new File(System.getProperty("user.dir"));
        File genomicFile= new File("src\\Homo Sapiens_KEGG_Pathways_09.04.2015.txt");
        //File genomicFile= new File("src\\deneme.txt");
        File pathwayRelations = new File("Results\\Pathways Relations.txt");

        new File(ResultFile+"\\Results").mkdir();    //Kullanıcının projesinin olduğu dizine direk Results adında bir klasör ouşturuluyor ve sonuçlar oraya yazılıyor...

        String[] allPathways;
        String[] allGeneIDList;

        //Eğer seçmek istersem bu kodu kullanabilirim...

       /* JFrame frame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(frame);

        if(result == JFileChooser.APPROVE_OPTION)
        {
            genomicFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(frame,"File Selection is successful.");
        }*/




        ArrayList<String> allGenes = new ArrayList<>();
        ArrayList<String> allGenesIDs = new ArrayList<>();
        ArrayList<String> allPathwaysList = new ArrayList<>();
        String gene;
        String geneID;

        ReadElements readElement = new ReadElements(genomicFile);


        allGenes = readElement.readTheGeneGroup();
        allGenesIDs = readElement.readTheGenesID();
        allPathwaysList = readElement.readThePathways();


       // for(int i=0;i<allPathwaysList.size();i++)/////////////////////////////
        //    System.out.println(allPathwaysList.get(i));


        sizeOfGenes = allGenes.size();
        sizeOfGeneIDs = allGenesIDs.size();
        sizeOfPathways = allPathwaysList.size();
       // System.out.println(allPathwaysList.get(0));
       // for(int i=0;i<allPathwaysList.size();i++)
       // System.out.println(allPathwaysList.get(i));///////////////////////

        allGeneIDList = new String[sizeOfGeneIDs - 1];  //-1 lar yine header bilgisinden dolayı
        allPathways = new String[sizeOfPathways-1];

        ComputationProcess initializeTheArray = new ComputationProcess();
        initializeTheArray.InitializeTheArray(sizeOfGenes);


       /* for(int i=0;i<allPathwaysList.size();i++)
        System.out.printf("AllPathwayReturn[%d]: %s\n",i,allPathwaysList.get(i));*/

        for (int i = 0; i < allPathways.length; i++)   //-1 header bilgisinden dolayı ilk eleman header.
        {

                allPathways[i] = allPathwaysList.get(i + 1);

        }


        String[] onePathwaysGenes;
        SplitFiles splitTheGeneGroup = new SplitFiles();

        //System.out.println(allGenes.get(1));           //////////////////////
        for (int i = 1; i < allGenes.size(); i++)
        {
            onePathwaysGenes = splitTheGeneGroup.SplitTheGeneGroup(allGenes.get(i));
            AddToHash(onePathwaysGenes);
        }


        for (int i = 1; i < allGenesIDs.size(); i++)
        {
            allGeneIDList[counter] =  splitTheGeneGroup.SplitTheGeneIDGroup(allGenesIDs.get(i));
            counter++;

        }
        
        System.out.println(sizeOfGenes);                     //////


        //term-gene matrix'te term kısmı allGeneIDList dizisi, gene kısmı ise genes[] dizisi;
        geneTermMatrix = new String[sizeOfGenes][k];
        //geneTermMatrix = new String[allPathways.length][k];

//Burayı computation process classı ile yap...
        for (int i = 0; i < sizeOfGenes; i++)
        //for (int i = 0; i < allPathways.length; i++)
        {
            for (int j = 0; j < k; j++)
                geneTermMatrix[i][j] = String.valueOf(0);       //Bütün diziyi 0 olarak initialize ediyorum burada...
        }


      //  System.out.printf("%s" , allGenes.get(1));
       // System.out.println();                                 ----> For Printing
       // System.out.printf("%s" , genes.length);


        for (int i = 0, j = 1; j < sizeOfGenes; i++, j++)                //Ayrı ayrı pathway olan allGenes.get(j) bütün genlerle karşılşatırmak için parametre olarak veriliyor.
        {
            generateTheGeneTermMatrix(allGenes.get(j), i);   //term-gene matrix'te term kısmı allGeneIDList dizisi, gene kısmı ise genes[] dizisi;
        }


        ComputationProcess newComputation = new ComputationProcess();
        newComputation.generateTheTermTermMatrix(pathwayRelations, allPathways, allGeneIDList, geneTermMatrix, allPathways.length, k);

    }

    private static void generateTheGeneTermMatrix(String s, int index)
    {
        String[] geneComparison = s.split("\\|");


        for (int i = 0; i < geneComparison.length; i++)
        {
            for (int j = 0; j < k; j++)
                //if (Objects.equals(geneComparison[i], genes[j]))
                if (Objects.equals(geneComparison[i], genes.get(j)))
                {
                    geneTermMatrix[index][j] = String.valueOf(1);
                }
        }
    }

    public static void AddToHash(String[] seperateGenesOfOnePathway)
    {

        boolean flag = false;  //Eger flag true ise genes[] dizisine de ekliyoruz. term-gene matrisini oluşturabilmek için...
        int counter = 0;


        for (int i = 0; i < seperateGenesOfOnePathway.length; i++)
        {

            flag = allSingleGenes.add(seperateGenesOfOnePathway[i]);
            //Eğer hash'e eklenebiliyorsa bu elemanı gene term matrisinin hesaplanabilmesi için ayrı bir diziye almam gerek,yor karşılaştırma yapmak için
            if (flag == true)
            {
                //genes[k] = seperateGenesOfOnePathway[i];
                genes.add( seperateGenesOfOnePathway[i]);
                k++;

            }
        }
    }
}






