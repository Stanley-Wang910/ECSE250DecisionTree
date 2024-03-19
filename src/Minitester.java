import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;


class ReadFile {
    public static String localDir = System.getProperty("user.dir");
    //You might need to change these based on your operating system.
    public static String base = localDir + "/src/data/";
    //You might need to change these based on your operating system.
    public static String basedb = localDir + "/src/data/db/";

    public static DecisionTree getDTFromFile(String filename) {
        DecisionTree dt = DataReader.readSerializedTree(base + filename);
        if (dt == null) throw new AssertionError("[ERROR] Could not read DT from file.");
        return dt;
    }

    public static DataReader getCSVDataReader(String filename) {
        DataReader dr = new DataReader();
        try {
            dr.read_data(basedb + filename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("[ERROR] Could not read csv file.");
        }
        return dr;
    }
}


class DecisionTreeTest  {

    @Test
    public void DecisionTree_classify1() {
        boolean verbose = false;

        DataReader dr = ReadFile.getCSVDataReader("data_minimal_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree dt = ReadFile.getDTFromFile("data_minimal_overlap/thresh1.ser");

        int counter=0;
        int total = dr.trainData.size();
        for (int i = 0; i < total; i++) {
            double[] attrs = dr.trainData.get(i).x;
            int correctLabel = dr.trainData.get(i).y;
            int classifiedAs = dt.classify(attrs);

            if (verbose) {
                System.out.println("Attributes: " + Arrays.toString(attrs));
                System.out.println("Correct label: " + correctLabel + ", Your classification :" + classifiedAs);
            }
            if (correctLabel == classifiedAs) {
                counter++;
            }
        }
        System.out.println("Number of correct outputs : " + counter + " out of " + total);
        assertEquals(counter, total);
    }

    @Test
    public void DecisionTree_classify2() {
        boolean verbose = false;

        DataReader dr = ReadFile.getCSVDataReader("data_partial_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree dt = ReadFile.getDTFromFile("data_partial_overlap/thresh1.ser");

        int counter=0;
        int total = dr.trainData.size();
        for (int i = 0; i < total; i++) {
            double[] attrs = dr.trainData.get(i).x;
            int correctLabel = dr.trainData.get(i).y;
            int classifiedAs = dt.classify(attrs);

            if (verbose) {
                System.out.println("Attributes: " + Arrays.toString(attrs));
                System.out.println("Correct label: " + correctLabel + ", Your classification :" + classifiedAs);
            }
            if (correctLabel == classifiedAs) {
                counter++;
            }
        }
        System.out.println("Number of correct outputs : " + counter + " out of " + total);
        assertEquals(counter, total);
    }

    @Test
    public void DecisionTree_classify3() {
        boolean verbose = false;

        DataReader dr = ReadFile.getCSVDataReader("data_high_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree dt = ReadFile.getDTFromFile("data_high_overlap/thresh1.ser");

        int counter=0;
        int total = dr.trainData.size();

        for (int i = 0; i < total; i++) {

            double[] attrs = dr.trainData.get(i).x;
            int correctLabel = dr.trainData.get(i).y;
            int classifiedAs = dt.classify(attrs);

            if (verbose) {
                System.out.println("Attributes: " + Arrays.toString(attrs));
                System.out.println("Correct label: " + correctLabel + ", Your classification :" + classifiedAs);
            }
            if (correctLabel == classifiedAs) {
                counter++;
            }
        }
        System.out.println("Number of correct outputs : " + counter + " out of " + total);
        assertEquals(counter, total);
    }

    @Test
    public void DecisionTree_classify4() {
        boolean verbose = false;

        DataReader dr = ReadFile.getCSVDataReader("data_minimal_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree dt = ReadFile.getDTFromFile("data_minimal_overlap/thresh2.ser");

        int counter=0;
        int total = dr.trainData.size();
        for (int i = 0; i < total; i++) {

            double[] attrs = dr.trainData.get(i).x;
            int correctLabel = dr.trainData.get(i).y;
            int classifiedAs = dt.classify(attrs);

            if (verbose) {
                System.out.println("Attributes: " + Arrays.toString(attrs));
                System.out.println("Correct label: " + correctLabel + ", Your classification :" + classifiedAs);
            }
            if (correctLabel == classifiedAs) {
                counter++;
            }
        }
        System.out.println("Number of correct outputs : " + counter + " out of " + total);
        assertEquals(counter, total);
    }
}



class EqualTest {

    @Test
    public void Test_HighDiffFiles() {
        DecisionTree dt1 = ReadFile.getDTFromFile("data_high_overlap/thresh4.ser");
        DecisionTree dt2 = ReadFile.getDTFromFile("data_high_overlap/thresh1.ser");

        assertEquals(DecisionTree.equals(dt1, dt2), false);
    }

    @Test
    public void Equals_HighSameFiles() {
        DecisionTree dt1 = ReadFile.getDTFromFile("data_high_overlap/thresh1.ser");
        DecisionTree dt2 = ReadFile.getDTFromFile("data_high_overlap/thresh1.ser");

        assertEquals(DecisionTree.equals(dt1, dt2), true) ;
    }

    @Test
    public void Equals_MinDiffFiles() {
        DecisionTree dt1 = ReadFile.getDTFromFile("data_minimal_overlap/thresh8.ser");
        DecisionTree dt2 = ReadFile.getDTFromFile("data_minimal_overlap/thresh1.ser");
        assertEquals(DecisionTree.equals(dt1, dt2), false);
    }

    @Test
    public void Equals_MinSameFiles() {
        DecisionTree dt1 = ReadFile.getDTFromFile("data_minimal_overlap/thresh4.ser");
        DecisionTree dt2 = ReadFile.getDTFromFile("data_minimal_overlap/thresh4.ser");
        assertEquals(DecisionTree.equals(dt1, dt2), true) ;
    }

    @Test
    public void Equals_PartialDiffFiles() {
        DecisionTree dt1 = ReadFile.getDTFromFile("data_partial_overlap/thresh4.ser");
        DecisionTree dt2 = ReadFile.getDTFromFile("data_partial_overlap/thresh1.ser");
        assertEquals(DecisionTree.equals(dt1, dt2), false) ;
    }

    @Test
    public void Equals_PartialSameFiles() {
        DecisionTree dt1 = ReadFile.getDTFromFile("data_high_overlap/thresh1.ser");
        DecisionTree dt2 = ReadFile.getDTFromFile("data_high_overlap/thresh1.ser");
        assertEquals(DecisionTree.equals(dt1, dt2), true) ;
    }
}





class FillDTNodeTest {

    @Test
    public void FillDTNode_High1() {
        int threshold = 1;
        DataReader dr = ReadFile.getCSVDataReader("data_high_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_high_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);
        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }

    @Test
    public void FillDTNode_High2() {
        int threshold = 4;
        DataReader dr = ReadFile.getCSVDataReader("data_high_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_high_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);
        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }

    @Test
    public void FillDTNode_High3() {
        int threshold = 32;
        DataReader dr = ReadFile.getCSVDataReader("data_high_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_high_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);

        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }

    @Test
    public void FillDTNode_Min1() {
        int threshold = 1;
        DataReader dr = ReadFile.getCSVDataReader("data_minimal_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_minimal_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);
        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }

    @Test
    public void FillDTNode_Min2() {
        int threshold = 4;
        DataReader dr = ReadFile.getCSVDataReader("data_minimal_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_minimal_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);
        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }
    @Test
    public void FillDTNode_Min3() {
        int threshold = 64;
        DataReader dr = ReadFile.getCSVDataReader("data_minimal_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_minimal_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);

        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }

    @Test
    public void FillDTNode_Min4() {
        int threshold = 128;
        DataReader dr = ReadFile.getCSVDataReader("data_minimal_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_minimal_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);

        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }

    @Test
    public void FillDTNode_Partial1() {
        int threshold = 1;
        DataReader dr = ReadFile.getCSVDataReader("data_partial_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_partial_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);
        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }

    @Test
    public void FillDTNode_Partial2() {
        int threshold = 8;
        DataReader dr = ReadFile.getCSVDataReader("data_partial_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_partial_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);

        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }

    @Test
    public void FillDTNode_Partial3() {
        int threshold = 16;
        DataReader dr = ReadFile.getCSVDataReader("data_partial_overlap.csv");
        dr.splitTrainTestData(.5);

        DecisionTree serdt = ReadFile.getDTFromFile("data_partial_overlap/thresh" + threshold +".ser");
        DecisionTree dt = new DecisionTree(dr.trainData , threshold);
        assertEquals(DecisionTree.equals(serdt,dt), true) ;
    }
}
