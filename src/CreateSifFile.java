import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

    public class CreateSifFile  {

    public CreateSifFile(File file)
    {

        outputPathwayFile = file;
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();

        String formattedMonth = String.format("%02d", month);
        String formattedDay = String.format("%02d", day);
        String formattedHour = String.format("%02d", hour);
        String formattedMinute = String.format("%02d", minute);
        String formattedSecond = String.format("%02d", second);

        currentTime = formattedHour+"_"+formattedMinute+"_"+formattedSecond+"_"+formattedDay+"_"+formattedMonth+"_"+year;
        File ResultFile = new File(System.getProperty("user.dir"));
        System.out.println("Result File "+ ResultFile);
        Path resultDirectoryPath = Paths.get(String.valueOf(ResultFile)+"\\Results");
        resolvedPath = resultDirectoryPath.resolve(currentTime);

        new File(String.valueOf(resolvedPath)).mkdir();

    }

    static String currentTime;
    static Path resolvedPath;
    static File wholeInformationFile = new File("Results\\Whole Information.txt");
    static File importantPathways = new File("D:\\Projects\\Java Projects\\PathwayClusteringTool\\src\\25Pathways.txt");

    //Standart Output Files
    static File outputPathwayFile = new File("Results\\Relationships Between Pathways.txt");
    static File outputKEGGIDPathwayFile = new File("Results\\Relationships Between Pathways with KEGGID.sif");
    static File kappaScoreFile = new File("Results\\Kappa Score.txt");

    //Filtered Files
    static File importantOutputKEGGIDPathwayFile = new File("Results\\Filtered Relationships Between Important Pathways with KEGGID.sif");
    static File importantOutputPathwayFile = new File("Results\\Filtered Relationships Between Important Pathways.txt");
    static File importantKappaScoreFile = new File("Results\\Filtered Kappa Score Important Pathways.txt");

    ArrayList<String> wholeInformationString = new ArrayList<>();
    ArrayList<String[]> importantInformationString = new ArrayList<>();
        
        public void createTheSifFile(float[][] termTermKappaScoreMatrix,float[][] termTermJaccardIndexMatrix, String[] allPathways, String[] allGeneIDLIST , int geneTermMatrixLength, float mean) throws IOException {



            ArrayList<String> allImportantPathways = new ArrayList<>();

                    //Existing Files Check If Exists Remove Them
                        /*    if(wholeInformationFile.exists()) wholeInformationFile.delete();
                              if(outputPathwayFile.exists()) outputPathwayFile.delete();
                              if(outputKEGGIDPathwayFile.exists()) outputKEGGIDPathwayFile.delete();
                              if(kappaScoreFile.exists()) kappaScoreFile.delete();
                              if(importantOutputPathwayFile.exists()) importantOutputPathwayFile.delete();
                              if(importantOutputKEGGIDPathwayFile.exists()) importantOutputKEGGIDPathwayFile.delete();
                              if(importantKappaScoreFile.exists()) importantKappaScoreFile.delete();*/

            ReadElements myReadElements = new ReadElements(importantPathways);
            allImportantPathways=myReadElements.readTheGenesID();

                    for(int i=0;i<geneTermMatrixLength-1;i++)
                {
                            for(int j=i;j<geneTermMatrixLength-1;j++)
                    {
                                if(i == j)
                        {
                                    continue;
                    }
                    else
                    {
                               // if(termTermKappaScoreMatrix[i][j] >= 0.15)      //Burada da iÅŸlemi diagonal yaptÄ±rmam lazÄ±m Ã§Ã¼nkÃ¼ zaten iliÅŸkisi olanÄ± yazdÄ±rdÄ±...
                                      //  {
                        String linkBetweenPathwaysWholeInformation = String.format("%s pp %s pp %s pp %s pp %f",allPathways[i],allPathways[j],allGeneIDLIST[i],allGeneIDLIST[j],termTermKappaScoreMatrix[i][j]);
                            wholeInformationString.add(linkBetweenPathwaysWholeInformation);
                       // }


                                               /* if(termTermJaccardIndexMatrix[i][j] > 0.15)
                {
                    String linkBetweenPathways = String.format("%s pp %s", allPathways[i], allPathways[j]);
                    String linkBetweenPathwaysKEGGID = String.format("%s pp %s",allGeneIDLIST[i],allGeneIDLIST[j]);
                    // String kappaScore = String.format("%s's strength of relationship with %s is\t  %f",allPathways[i],allPathways[j],termTermKappaScoreMatrix[i][j]);
                    String kappaScore = String.format("%f",termTermJaccardIndexMatrix[i][j]);
                    writeToTheFile(linkBetweenPathways);
                    writeToTheFileKEGGID(linkBetweenPathwaysKEGGID);
                    writeToTheFileKappaScore(kappaScore);
                }*/
                                                    }

                        }
            }
            writeToTheFileWholeInformation(wholeInformationString);
            writeToTheFiles();
            filterTheFile(allImportantPathways);

            Path wholeFile =resolvedPath.resolve(wholeInformationFile.getName()); //Make the whole information file invisible...
            //set hidden attribute
            Files.setAttribute(wholeFile, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
            //System.out.println("Mean is : "+mean);
                    System.exit(0);

                }

        private void filterTheFile(ArrayList<String> all25pathways) throws IOException {
            Path resolvedPathForWholeInformation =resolvedPath.resolve(wholeInformationFile.getName());
            BufferedReader reader = new BufferedReader(new FileReader(resolvedPathForWholeInformation.toFile()));
            String currentLine;

                    while((currentLine = reader.readLine()) != null) {
                    String[] trimmedLine = currentLine.trim().split("\\s+pp\\s+");
                    boolean checkPart1 =all25pathways.contains(trimmedLine[2]);
                    boolean checkPart2 = all25pathways.contains(trimmedLine[3]);

                            if(checkPart1 && checkPart2) importantInformationString.add(trimmedLine);
                    else continue;
                }

            writeToTheImportantFiles(importantInformationString);
            reader.close();

                }

        private  void writeToTheFileWholeInformation(ArrayList<String> wholeInfo) throws IOException {
            Path resolvedPathForWholeInformation =resolvedPath.resolve(wholeInformationFile.getName());
            FileWriter fw = new FileWriter(resolvedPathForWholeInformation.toFile(),true); //Overwrite it...
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i=0;i<wholeInfo.size();i++){
                    bw.write(wholeInfo.get(i));
                    bw.newLine();
                }
                    bw.close();


                        }

            private  void writeToTheFiles() throws IOException {
        
                Path resolvedPathForWholeInformation =resolvedPath.resolve(wholeInformationFile.getName());
                BufferedReader reader = new BufferedReader(new FileReader(resolvedPathForWholeInformation.toFile()));
        
                Path resolvedPathForPathwayNames =resolvedPath.resolve(outputPathwayFile.getName());
                Path resolvedPathForKEGGID =resolvedPath.resolve(outputKEGGIDPathwayFile.getName());
                Path resolvedPathForKappaScores =resolvedPath.resolve(kappaScoreFile.getName());
        
                FileWriter writerForPathwayNames = new FileWriter(resolvedPathForPathwayNames.toFile(),true);
                FileWriter writerForKEGGIDNetwork = new FileWriter(resolvedPathForKEGGID.toFile(),true);
                FileWriter writerForKappaScores = new FileWriter(resolvedPathForKappaScores.toFile(),true);
        
                
                BufferedWriter bufferedWriterForPathwayNames = new BufferedWriter(writerForPathwayNames);
                BufferedWriter bufferedWriterForKEGGIDNetwork = new BufferedWriter(writerForKEGGIDNetwork);
                BufferedWriter bufferedWriterForKappaScores = new BufferedWriter(writerForKappaScores);
                //BufferedWriter bw = new BufferedWriter(fw);
                        String currentLine;
        
                        while((currentLine = reader.readLine()) != null) {
                        String[] trimmedLine = currentLine.trim().split("\\s+pp\\s+");
                        bufferedWriterForPathwayNames.write(trimmedLine[0]+"\tpp\t"+ trimmedLine[1]+"\n");
                        bufferedWriterForKEGGIDNetwork.write(trimmedLine[2]+"\tpp\t"+ trimmedLine[3]+"\n");
                        bufferedWriterForKappaScores.write(trimmedLine[2]+"\tpp\t"+ trimmedLine[3]+"\tpp\t"+trimmedLine[4]+"\n");
                    }
                bufferedWriterForPathwayNames.close();
                bufferedWriterForKEGGIDNetwork.close();
                bufferedWriterForKappaScores.close();
        
                
                        
                                    }

            private  void writeToTheImportantFiles(ArrayList<String[]> wholeInfoFile) throws IOException {
                /// Path pn = Paths.get(String.valueOf(importantOutputKEGGIDPathwayFile));
                        //Path pn =Paths.get(String.valueOf(resolvedPath));
                Path resolvedPathForImportantPathwayNames =resolvedPath.resolve(importantOutputPathwayFile.getName());
                Path resolvedPathForImportantKEGGID =resolvedPath.resolve(importantOutputKEGGIDPathwayFile.getName());
                Path resolvedPathForImportantKappaScores =resolvedPath.resolve(importantKappaScoreFile.getName());
        
                FileWriter writerForImportantPathwayNames = new FileWriter(resolvedPathForImportantPathwayNames.toFile(),true);
                FileWriter writerForImportantKEGGIDNetwork = new FileWriter(resolvedPathForImportantKEGGID.toFile(),true);
                FileWriter writerForImportantKappaScores = new FileWriter(resolvedPathForImportantKappaScores.toFile(),true);
        
                
                BufferedWriter bufferedWriterForPathwayNames = new BufferedWriter(writerForImportantPathwayNames);
                BufferedWriter bufferedWriterForKEGGIDNetwork = new BufferedWriter(writerForImportantKEGGIDNetwork);
                BufferedWriter bufferedWriterForKappaScores = new BufferedWriter(writerForImportantKappaScores);
        
                        for(String[] importantInfo : wholeInfoFile){
                        bufferedWriterForPathwayNames.write(importantInfo[0]+"\tpp\t"+ importantInfo[1]+"\n");
                        bufferedWriterForKEGGIDNetwork.write(importantInfo[2]+"\tpp\t"+ importantInfo[3]+"\n");
                        bufferedWriterForKappaScores.write(importantInfo[2]+"\tpp\t"+ importantInfo[3]+"\tpp\t"+importantInfo[4]+"\n");
                    }

                bufferedWriterForPathwayNames.close();
                bufferedWriterForKEGGIDNetwork.close();
                bufferedWriterForKappaScores.close();
        
           }

        }

