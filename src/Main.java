import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main {


    static String[][] geneTermMatrix;

    public static void main(String[] args) throws IOException {


        File ResultFile = new File(System.getProperty("user.dir"));
        File genomicFile= new File("Data\\Homo Sapiens_KEGG_Pathways_09.04.2015.txt");
        //File genomicFile= new File("src\\deneme.txt");
        File pathwayRelations = new File("Results\\Pathways Relations.txt");

        new File(ResultFile+"\\Results").mkdir();    //Kullanıcının projesinin olduğu dizine direk Results adında bir klasör ouşturuluyor ve sonuçlar oraya yazılıyor...

        ReadElements readElement = new ReadElements(genomicFile);

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



        GenesClass geneOperation = new GenesClass();
        geneOperation = geneOperation.GeneOperations(readElement);

        ComputationProcess initializeTheArray = new ComputationProcess();
        initializeTheArray.InitializeTheArray(geneOperation.getWholeGenesInOnePathway().size());


        for(int i=0; i<geneOperation.getAllPathways().length;i++){
            geneOperation.setAllPathways(i);
        }


        String[] onePathwaysGenes;
        SplitFiles splitTheGeneGroup = new SplitFiles();

        //System.out.println(allGenes.get(1));           //////////////////////
        for (int i = 1; i < geneOperation.getWholeGenesInOnePathway().size(); i++)
        {
            onePathwaysGenes = splitTheGeneGroup.SplitTheGeneGroup(geneOperation.getWholeGenesInOnePathway().get(i));
            geneOperation.AddToHash(onePathwaysGenes);
        }


        for (int i = 1; i < geneOperation.getGeneAndRepoInfo().size(); i++)
        {
            geneOperation.setAllGeneIDList(splitTheGeneGroup.SplitTheGeneIDGroup(geneOperation.
                    getGeneAndRepoInfo().get(i)));

        }
        
        //System.out.println(sizeOfGenes);                     //////


        //term-gene matrix'te term kısmı allGeneIDList dizisi, gene kısmı ise genes[] dizisi;
        geneTermMatrix = new String[geneOperation.getWholeGenesInOnePathway().size()][GenesClass.getNumberOfUniqueGenes()];
        //geneTermMatrix = new String[allPathways.length][k];

//Burayı computation process classı ile yap...
        for (int i = 0; i < geneOperation.getWholeGenesInOnePathway().size(); i++)
        //for (int i = 0; i < allPathways.length; i++)
        {
            for (int j = 0; j < GenesClass.numberOfUniqueGenes; j++)
                geneTermMatrix[i][j] = String.valueOf(0);       //Bütün diziyi 0 olarak initialize ediyorum burada...
        }


      //  System.out.printf("%s" , allGenes.get(1));
       // System.out.println();                                 ----> For Printing
       // System.out.printf("%s" , genes.length);


        for (int i = 0, j = 1; j < geneOperation.getWholeGenesInOnePathway().size(); i++, j++)                //Ayrı ayrı pathway olan allGenes.get(j) bütün genlerle karşılşatırmak için parametre olarak veriliyor.
        {
            generateTheGeneTermMatrix(geneOperation.getWholeGenesInOnePathway().get(j), i);   //term-gene matrix'te term kısmı allGeneIDList dizisi, gene kısmı ise genes[] dizisi;
        }

        System.out.println(GenesClass.getAllGenesInFile().get(0));
        System.out.println(geneOperation.getAllPathways().length);
        System.out.println(GenesClass.getNumberOfUniqueGenes());
        ComputationProcess newComputation = new ComputationProcess();
        newComputation.generateTheTermTermMatrix(pathwayRelations, geneOperation.getAllPathways(), geneOperation.getAllGeneIDList(), geneTermMatrix, geneOperation.getAllPathways().length, GenesClass.getNumberOfUniqueGenes());

    }

    private static void generateTheGeneTermMatrix(String s, int index)
    {
        String[] geneComparison = s.split("\\|");



        for (int i = 0; i < geneComparison.length; i++)
        {
            for (int j = 0; j < GenesClass.numberOfUniqueGenes; j++)
                //if (Objects.equals(geneComparison[i], genes[j]))
                if (Objects.equals(geneComparison[i], GenesClass.getAllGenesInFile().get(j)))
                {
                    geneTermMatrix[index][j] = String.valueOf(1);
                }
        }
    }
}






