public class SplitFiles {

    public String SplitTheGeneIDGroup(String s){


        String[] g = s.split("\\:");    //Diğer versiyonlarda burayı görsel arayüzden seçtireceğim....
        return g[1];

    }

    public String[] SplitTheGeneGroup(String s){

        String[] allGenesWithPipe = s.split("\\|");
        return allGenesWithPipe;
    }



}
