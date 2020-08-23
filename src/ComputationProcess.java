import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

        public class ComputationProcess {

    static float[][] termTermKappaScoreMatrix;
    static float[][] termTermJaccardIndexMatrix;

        
    ArrayList<String> firstGeneComparison = new ArrayList<>();
    ArrayList<String> secondGeneComparison = new ArrayList<>();

        
        
        public  void InitializeTheArray(int numberOfPathways)
        {
            termTermKappaScoreMatrix = new float[numberOfPathways][numberOfPathways];
            termTermJaccardIndexMatrix = new float[numberOfPathways][numberOfPathways];


            for(int i=0;i<numberOfPathways;i++)
            {
                for(int j =0;j<numberOfPathways;j++ )
                {
                     if(i == j)
                     {
                         termTermKappaScoreMatrix[i][j] = 1;
                         termTermJaccardIndexMatrix[i][j] = 1;
                      }
                    else
                         termTermKappaScoreMatrix[i][j] = 1;
                         termTermJaccardIndexMatrix[i][j] = 1;
                }

            }
         }

        
        
    public void generateTheTermTermMatrix(File outputFile, String[] allPathways, String[] geneIDLIST , String[][] geneTermMatrix, int numberOfPathway, int numberOfUniqueGenes) throws IOException {

                CreateSifFile createSifFile = new CreateSifFile(outputFile);
                int geneTermMatrixLength = termTermKappaScoreMatrix.length;

                for (int i = 0; i < numberOfPathway; i++)
                {                                                           //Burada 3 for'un kullanÄ±lmasÄ±nÄ±n sebebi matris simetrik oldÄŸu iÃ§in iÅŸlem yÃ¼kÃ¼nÃ¼n yarÄ± yarÄ±ya
                    for (int l = i+1; l < numberOfPathway; l++)
                    {

                        for (int j = 0; j < numberOfUniqueGenes; j++)
                        {

                            firstGeneComparison.add(geneTermMatrix[i][j]);      //Burada 1. term'Ã¼n elemanlarÄ±nÄ± ekliyor...
                            secondGeneComparison.add(geneTermMatrix[l][j]);     //Burada karÅŸÄ±laÅŸtÄ±racaÄŸÄ± 2. term'Ã¼n elemanlarÄ±nÄ± ekliyor...

                              // if (j == numberOfUniqueGenes  1) {
                            if (firstGeneComparison.size() == numberOfUniqueGenes && secondGeneComparison.size() == numberOfUniqueGenes)
                            {
                                computeTheTermTermMatrix(firstGeneComparison, secondGeneComparison, numberOfUniqueGenes, i, l);
                                secondGeneComparison.clear(); //SÃ¼rekli array list'e yazmamasÄ± iÃ§in her dolduÄŸunda arraylisti silmemiz gerekiyor...

                            }
                        }

                     firstGeneComparison.clear(); //SÃ¼rekli array list'e yazmamasÄ± iÃ§in her dolduÄŸunda arraylisti silmemiz gerekiyor...

                    if(l==numberOfPathway-1)
                        break;     //Son pathway'in herhangi birÅŸey ile kÄ±yaslanmamasÄ± gerekiyor.
                    }
                    if (i == numberOfPathway  -1)
                        break;         //azaltÄ±lmak istememiz (1,0)'Ä± hesaplÄ±yorsak (0,1)'i hesaplamamza gerek yok...

                }

    int toplam = 0;
    float mean = 0;

    for (int i = 0; i < numberOfPathway; i++)
    {
       for (int j = 0; j < numberOfPathway; j++)
       {
                //System.out.printf("Matrix[%d][%d] : [%f]\n",i,j,termTermKappaScoreMatrix[i][j]);
                        toplam += termTermKappaScoreMatrix[i][j];
        }

    }
        
    mean = (float) toplam / (numberOfPathway * numberOfPathway);
    createSifFile.createTheSifFile(termTermKappaScoreMatrix,termTermJaccardIndexMatrix,allPathways, geneIDLIST, geneTermMatrixLength, mean);

}

    public void computeTheTermTermMatrix(ArrayList firstGeneGroup, ArrayList secondGeneGroup,int allSingleGenes, int firstIndex,int secondIndex) {
        int oneOneCounter = 0, oneZeroCounter = 0, zeroOneCounter = 0, zeroZeroCounter = 0;

                                        /// for (int i = 0; i < termSize; i++) {
        for (int j = 0; j < allSingleGenes; j++)
            {
                if (Integer.parseInt((String) firstGeneGroup.get(j))== 1 && Integer.parseInt((String) secondGeneGroup.get(j)) == 1) {
                    oneOneCounter++;
                } else if (Integer.parseInt((String) firstGeneGroup.get(j))== 1 && Integer.parseInt((String) secondGeneGroup.get(j)) == 0) {
                    oneZeroCounter++;
                } else if (Integer.parseInt((String) firstGeneGroup.get(j))== 0 && Integer.parseInt((String) secondGeneGroup.get(j)) == 1) {
                    zeroOneCounter++;
                } else if (Integer.parseInt((String) firstGeneGroup.get(j))== 0 && Integer.parseInt((String) secondGeneGroup.get(j)) == 0) {
                    zeroZeroCounter++;
                }
        }
        //}


        computeTheKappaScore(oneOneCounter, oneZeroCounter, zeroOneCounter, zeroZeroCounter, firstIndex,secondIndex);
        computeTheJaccardIndex(oneOneCounter, oneZeroCounter,zeroOneCounter,firstIndex, secondIndex);
        oneOneCounter = 0; oneZeroCounter = 0; zeroOneCounter = 0; zeroZeroCounter = 0;
    }

    public void computeTheJaccardIndex(int oneOneCounter, int oneZeroCounter, int zeroOneCounter,int firstIndex, int secondIndex) {

        float jaccardIndex = (float) (oneOneCounter)/(oneOneCounter+oneZeroCounter+zeroOneCounter);

        termTermJaccardIndexMatrix[firstIndex][secondIndex] = jaccardIndex;
        termTermJaccardIndexMatrix[secondIndex][firstIndex] = jaccardIndex;
    }

    public  void computeTheKappaScore(int oneOneCounter, int oneZeroCounter, int zeroOneCounter, int zeroZeroCounter,int firstIndex,int secondIndex)
    {
        int rowTotal = oneOneCounter+oneZeroCounter+zeroOneCounter+zeroZeroCounter;
        int C_1 = oneOneCounter+oneZeroCounter;
        int C1_ = oneOneCounter+zeroOneCounter;
        int C_0 = zeroOneCounter+zeroZeroCounter;
        int C0_ = zeroZeroCounter+oneZeroCounter;


        //We have to compute 2 value here,one is O(ab) and the other one is A(ab). Then Kappa score(K(ab)) can be calculated with this equation:
        // (O(ab)A(ab))/(1A(ab)). After this calculation we can write to the matrix...
        // System.out.println(rowTotal);

        float O = (float) (oneOneCounter+zeroZeroCounter)/rowTotal;
        float a = (float) (C_1  *C1_+C_0*C0_)/(rowTotal*rowTotal);
        float kappaScore = (O-a)/(1-a);
        // float kappaScore = (float)zeroZeroCounter;



        termTermKappaScoreMatrix[firstIndex][secondIndex] = kappaScore;  //Bu matrix simetrik olduÄŸundan dolayÄ± (0,1) ile (1,0) aynÄ± deÄŸer demektir...
        termTermKappaScoreMatrix[secondIndex][firstIndex] = kappaScore;
                        
    }

}

        
        
        

