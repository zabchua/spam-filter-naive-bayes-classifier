import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.lang.*;
import java.util.Timer;



public class cmsc162-mp02{

    public static String data = ""; 
    public static String dataTemp = ""; //new

    static Scanner read = new Scanner(System.in);
    
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        //Queue<String[]> vocabularyV = new LinkedList<String[]>();
        LinkedList<String> vocabularyV = new LinkedList<String>();
        int totalHamCount=0;
        int totalSpamCount=0;
        double totalCount = 0; //new
        double hamMessagesProbability = 0; // new
        double spamMessagesProbability = 0; // new
        int hammedWords=0;
        int spammedWords=0;
        int wordSets=0; //new
        double hamProbability=0;
        double spamProbability=0;
        int temp = 0; //new
        int trainingSet = 1771; //new
        int x = 0;
        int wordExists = 0;  //new
        int toParse = 0; //new
        String r = ""; // new
        String label = "";
        String filename = "";
        int myTesting = 0;
        boolean okayFile = false;

        // String[] first = {"hello", "hi", "how are you"};
        // String[] second = {"bye", "goobai", "bye bye"};
        // String[] third = {"yes", "no", "maybe"};


        // read data set
        System.out.println();
        System.out.println("Spam Filter using Naive Bayes Classifier");
        System.out.println("Machine Problem 2");
        System.out.println();
        Thread.sleep(1000);
        System.out.println("Developers:");
        System.out.println("Chua, Elizabeth Mary E.");
        System.out.println("Nisay, Deiondre Judd V.");
        System.out.println();
        Thread.sleep(1000);
        System.out.println("\nEnter Training Data File with extension (i.e. train.txt):");
        filename = read.nextLine();
        File f = new File(filename);

        while (!okayFile) {
            if (f.exists()) {
                okayFile=true;
            }
            else{
                System.out.println("\nFile does not exist, please try again...");
                System.out.println("\nEnter Training Data File with extension (i.e. train.txt):");
                filename = read.nextLine();
                f = new File(filename);
            }
        }


        

        Scanner s = new Scanner(new File(filename));

        while (x < trainingSet) {

            System.out.println("Processing... " + x);
            
            if (!s.hasNextLine()) {
                break;
            }
            else{
                Scanner s2 = new Scanner(s.nextLine());
                String r1 = s2.next();
                if (r1.equals("ham") || r1.equals("Ham") || r1.equals("HAM")) {
                    label = "ham";
                    totalHamCount++;
                    temp = 1;
                    x++;
                }
                else if (r1.equals("spam") || r1.equals("Spam") || r1.equals("SPAM")) {
                    label = "spam";
                    totalSpamCount++;
                    temp = 1;
                    x++;
                }

                while (s2.hasNext() && temp == 1) {
                    String r2 = s2.next();

                    if (wordSets != 0) {          
                        for (int i = 0; i < wordSets; i=i+5) {       
                            data = vocabularyV.get(i); 
                            if (r2.equals(data)) {
                                if (label == "ham") {
                                    wordExists = 1;
                                    dataTemp = vocabularyV.get(i+1);
                                    toParse = Integer.parseInt(dataTemp) + 1;
                                    vocabularyV.set(i+1, String.valueOf(toParse));
                                    hammedWords++;
                                }
                                else{
                                    wordExists = 1;
                                    dataTemp = vocabularyV.get(i+2);
                                    toParse = Integer.parseInt(dataTemp) + 1;
                                    vocabularyV.set(i+2, String.valueOf(toParse));
                                    spammedWords++;
                                }
                            }
                        }     
                    }

                    if (wordExists == 0) {
                        vocabularyV.add(wordSets, r2);
                        if (label == "ham") {
                            //vocabularyV.add(wordSets+1, "1");
                            //vocabularyV.add(wordSets+2, "0");
                             vocabularyV.add(wordSets+1, "2");        // for smoothing
                             vocabularyV.add(wordSets+2, "1");        // for smoothing
                            //hammedWords++;
                             hammedWords++;                                  // for smoothing
                             spammedWords++;                                 // for smoothing    
                        }
                        else{
                            //vocabularyV.add(wordSets+1, "0");
                            //vocabularyV.add(wordSets+2, "1");
                             vocabularyV.add(wordSets+1, "1");        // for smoothing
                             vocabularyV.add(wordSets+2, "2");        // for smoothing
                            //spammedWords++;
                             spammedWords++;                                    // for smoothing
                             hammedWords++;                                      // for smoothing
                        }
                        vocabularyV.add(wordSets+3, "0");
                        vocabularyV.add(wordSets+4, "0");
                        wordSets = wordSets + 5;
                    }

                    wordExists = 0;

                    
                }

            }
            temp = 0;
        }

        if (hammedWords == 0 || totalHamCount == 0) {
            System.out.println("Insufficient Ham Messages for training data");
            myTesting = 1;
        }
        else if (spammedWords == 0 || totalSpamCount == 0) {
            System.out.println("Insufficient Spam Messages for training data");
            myTesting = 1;
        }
        else{
            for (int j = 0; j < wordSets; j=j+5) {
                // System.out.println();
                // ham probability
                dataTemp = vocabularyV.get(j+1);
                hamProbability = Double.parseDouble(dataTemp)/hammedWords;
                vocabularyV.set(j+3, String.valueOf(hamProbability));

                //spam probability
                dataTemp = vocabularyV.get(j+2);
                spamProbability = Double.parseDouble(dataTemp)/spammedWords;
                vocabularyV.set(j+4, String.valueOf(spamProbability));

                //check
                // System.out.print("[\t" + vocabularyV.get(j));
                // System.out.print(",\t" + vocabularyV.get(j+1));
                // System.out.print(",\t" + vocabularyV.get(j+2));
                // System.out.print(",\t" + vocabularyV.get(j+3));
                // System.out.print(",\t" + vocabularyV.get(j+4) + "\t]");
                // System.out.println();
            }
        }

        totalCount = totalHamCount + totalSpamCount;
        hamMessagesProbability = totalHamCount/totalCount;
        spamMessagesProbability = totalSpamCount/totalCount;



        while (myTesting == 0) {

            double TP = 0;
            double TN = 0;
            double FP = 0;
            double FN = 0;
            double precision = 0;
            double recall = 0;
            int y = 0;
            okayFile = false;

            String toTestFile = "";
            System.out.println("\nEnter Test Data with extension (i.e. test.txt)");
            toTestFile = read.nextLine();
            File t = new File(toTestFile);
            

            while (!okayFile) {
                if (t.exists()) {
                    okayFile=true;
                }
                else{
                    System.out.println("\nFile does not exist, please try again...");
                    System.out.println("\nEnter Test Data File with extension (i.e. test.txt)");
                    toTestFile = read.nextLine();
                    t = new File(filename);
                }
            }

            Scanner q = new Scanner(new File(toTestFile));

            while (q.hasNextLine()) {
                temp = 0;
                label = "";
                double yesProbability = 1.000000000000000000;
                double noProbability = 1.000000000000000000;
                String spamOrHam = "";
                Scanner q2 = new Scanner(q.nextLine());

                System.out.println("Testing... " + y);
                y++;

                String z1 = q2.next();
                if (z1.equals("ham") || z1.equals("Ham") || z1.equals("HAM")) {
                    label = "ham";
                    temp = 1;

                }
                else if (z1.equals("spam") || z1.equals("Spam") || z1.equals("SPAM")) {
                    label = "spam";
                    temp = 1;
                }

                while (q2.hasNext() && temp == 1) {
                    String q3 = q2.next();
                    for (int k = 0; k < wordSets; k=k+5) {
                        data = vocabularyV.get(k);
                        if (q3.equals(data)) {
                            dataTemp = vocabularyV.get(k+4);
                            yesProbability = Double.parseDouble(dataTemp)*yesProbability;
                            dataTemp = vocabularyV.get(k+3);
                            noProbability = Double.parseDouble(dataTemp)*noProbability;
                            break;
                        }
                    }
                }

                yesProbability = yesProbability*spamMessagesProbability;
                noProbability = noProbability*hamMessagesProbability;
                if (yesProbability > noProbability) {
                    spamOrHam = "spam";
                }
                else{
                    spamOrHam = "ham";
                }

                if (label.equals(spamOrHam)) {
                    if (spamOrHam == "ham") {
                        TN++;
                    }
                    else{
                        TP++;
                    }
                }
                else{
                    if (spamOrHam == "ham") {
                        FN++;
                    }
                    else{
                        FP++;
                    }
                }
                

            }

            if ((TP+FP)!=0) {
                precision = TP/(TP+FP);
            }
            if ((TP+FN)!=0) {
                recall = TP/(TP+FN);
            }
            
            System.out.println();
            System.out.println("Results:");
            
            
            System.out.println("True Positive: " + TP);
            System.out.println("True Negative: " + TN);
            System.out.println("False Positive: " + FP);
            System.out.println("False Negative: " + FN);
            System.out.println("Precision: " + precision);
            System.out.println("Recall: " + recall);
            System.out.println();
            System.out.println();

            int validLoop = 0;
            int reRun = 0;
            Scanner myDecision = new Scanner(System.in);


            System.out.println("Would you like to input another set of test data?");
            System.out.println("1 - Yes");
            System.out.println("0 - No");
            System.out.println("Answer:");

            while (validLoop==0) {
                if (myDecision.hasNextInt()) {
                    reRun = myDecision.nextInt();
                    validLoop=1;       
                }
                else{
                    System.out.println("Error: Invalid input, try again");
                    System.out.println();
                    myDecision = new Scanner(System.in);
                    System.out.print("Would you like to input another set of test data?");
                    System.out.println("1 - Yes");
                    System.out.println("0 - No");
                    System.out.println("Answer:");
                }
                if (reRun == 1 || reRun == 0) {
                    validLoop=1;
                } 
                else{
                    System.out.println("Error: Invalid input, try again");
                    System.out.println();
                    myDecision = new Scanner(System.in);
                    System.out.print("Would you like to input another set of test data?");
                    System.out.println("1 - Yes");
                    System.out.println("0 - No");
                    System.out.println("Answer:");
                    validLoop=0;
                }
            }

            if (reRun == 0) {
                myTesting = 1;
                System.out.println("Terminating program...");
                Thread.sleep(1000);
            }
            else{
                myTesting = 0;
                System.out.println();
            }
  
        }

    }


}