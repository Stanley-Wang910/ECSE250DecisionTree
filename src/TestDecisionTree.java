import java.util.ArrayList;
import java.util.Arrays;

public class TestDecisionTree {

    public static void main(String[] args) {

        // local directory
        String localDir = System.getProperty("user.dir");

        // Adjust the paths based on your operating system and directory structure.
        String base = localDir + "/src/data/";
        String basedb = localDir + "/src/data/db/";

        boolean verbose = true;

        try {

            DataReader dr = new DataReader();
            dr.read_data(basedb + "data_high_overlap.csv");
            // split the data into training and testing
            dr.splitTrainTestData(.5);

            // Define the minimum sizes to test
            int[] minSizes = {1, 2, 4, 8, 16, 32, 64, 128};

            for (int minSize : minSizes) {
                // Build a new decision tree with the current minSize
                DecisionTree dt = new DecisionTree(dr.trainData, minSize);

                // Calculate and print the training error
                String trainingError = dt.checkPerformance(dr.trainData);
                System.out.println("Training error with minSizeDatalist " + minSize + ": " + trainingError);

                // Calculate and print the testing error
                String testingError = dt.checkPerformance(dr.testData);
                System.out.println("Testing error with minSizeDatalist " + minSize + ": " + testingError);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
