import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;

public class DecisionTree1 implements Serializable {

    DTNode rootDTNode;
    int minSizeDatalist; //minimum number of datapoints that should be present in the dataset so as to initiate a split

    // Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
    public static final long serialVersionUID = 343L;

    public DecisionTree1(ArrayList<Datum> datalist , int min) {
        minSizeDatalist = min;
        rootDTNode = (new DTNode()).fillDTNode(datalist);
    }

    class DTNode implements Serializable{
        //Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
        public static final long serialVersionUID = 438L;
        boolean leaf;
        int label = -1;      // only defined if node is a leaf
        int attribute; // only defined if node is not a leaf
        double threshold;  // only defined if node is not a leaf

        DTNode left, right; //the left and right child of a particular node. (null if leaf)

        DTNode() {
            leaf = true;
            threshold = Double.MAX_VALUE;
        }


        // this method takes in a datalist (ArrayList of type datum). It returns the calling DTNode object
        // as the root of a decision tree trained using the datapoints present in the datalist variable and minSizeDatalist.
        // Also, KEEP IN MIND that the left and right child of the node correspond to "less than" and "greater than or equal to" threshold
        DTNode fillDTNode(ArrayList<Datum> datalist) {
            //ADD CODE HERE
            if(datalist.size() < minSizeDatalist || allDataSameLabel(datalist)) {
                this.leaf = true;
                this.label = findMajority(datalist);
                return this;
            } else {
                //Find best split
                this.leaf = false;
                findBestSplit(datalist, this);

                //Split the datalist into two based on the best split found
                ArrayList<Datum> data1 = new ArrayList<>();
                ArrayList<Datum> data2 = new ArrayList<>();

                for (Datum data : datalist) {
                    if (data.x[this.attribute] < this.threshold) {
                        data1.add(data);
                    } else {
                        data2.add(data);
                    }
                }

                this.left = (new DTNode()).fillDTNode(data1);
                this.right = (new DTNode()).fillDTNode(data2);
                return this;
            }
        }



        // This is a helper method. Given a datalist, this method returns the label that has the most
        // occurrences. In case of a tie it returns the label with the smallest value (numerically) involved in the tie.
        int findMajority(ArrayList<Datum> datalist) {

            int [] votes = new int[2];

            //loop through the data and count the occurrences of datapoints of each label
            for (Datum data : datalist)
            {
                votes[data.y]+=1;
            }

            if (votes[0] >= votes[1])
                return 0;
            else
                return 1;
        }




        // This method takes in a datapoint (excluding the label) in the form of an array of type double (Datum.x) and
        // returns its corresponding label, as determined by the decision tree
        int classifyAtNode(double[] xQuery) {

            //ADD CODE HERE
            if (this.leaf) {
                return this.label;
            }

            if (xQuery[this.attribute] < this.threshold) {
                return this.left.classifyAtNode(xQuery);
            } else {
                return this.right.classifyAtNode(xQuery);
            }//dummy code.  Update while completing the assignment.
        }


        //given another DTNode object, this method checks if the tree rooted at the calling DTNode is equal to the tree rooted
        //at DTNode object passed as the parameter
        public boolean equals(Object dt2)
        {

            //ADD CODE HERE
            DTNode otherNode = (DTNode) dt2;
            if (this.leaf && otherNode.leaf) {
                return this.label == otherNode.label;
            }
            else if (!this.leaf && !otherNode.leaf) {
                return this.attribute == otherNode.attribute &&
                        Double.compare(this.threshold, otherNode.threshold) == 0 &&
                        ((this.left == null && otherNode.left == null) || (this.left != null && this.left.equals(otherNode.left))) &&
                        ((this.right == null && otherNode.right == null) || (this.right != null && this.right.equals(otherNode.right)));
            }
            return false; //dummy code.  Update while completing the assignment.
        }
    }



    //Given a dataset, this returns the entropy of the dataset
    double calcEntropy(ArrayList<Datum> datalist) {
        double entropy = 0;
        double px = 0;
        float [] counter= new float[2];
        if (datalist.size()==0)
            return 0;
        double num0 = 0.00000001,num1 = 0.000000001;

        //calculates the number of points belonging to each of the labels
        for (Datum d : datalist)
        {
            counter[d.y]+=1;
        }
        //calculates the entropy using the formula specified in the document
        for (int i = 0 ; i< counter.length ; i++)
        {
            if (counter[i]>0)
            {
                px = counter[i]/datalist.size();
                entropy -= (px*Math.log(px)/Math.log(2));
            }
        }

        return entropy;
    }


    // given a datapoint (without the label) calls the DTNode.classifyAtNode() on the rootnode of the calling DecisionTree object
    int classify(double[] xQuery ) {
        return this.rootDTNode.classifyAtNode( xQuery );
    }

    // Checks the performance of a DecisionTree on a dataset
    // This method is provided in case you would like to compare your
    // results with the reference values provided in the PDF in the Data
    // section of the PDF
    String checkPerformance( ArrayList<Datum> datalist) {
        DecimalFormat df = new DecimalFormat("0.000");
        float total = datalist.size();
        float count = 0;

        for (int s = 0 ; s < datalist.size() ; s++) {
            double[] x = datalist.get(s).x;
            int result = datalist.get(s).y;
            if (classify(x) != result) {
                count = count + 1;
            }
        }

        return df.format((count/total));
    }


    //Given two DecisionTree objects, this method checks if both the trees are equal by
    //calling onto the DTNode.equals() method
    public static boolean equals(DecisionTree dt1,  DecisionTree dt2)
    {
        boolean flag = true;
        flag = dt1.rootDTNode.equals(dt2.rootDTNode);
        return flag;
    }


    private boolean allDataSameLabel(ArrayList<Datum> datalist) {
        if (datalist.isEmpty()) {
            return true;
        }

        int firstLabel = datalist.get(0).y;
        for (Datum d : datalist) {
            if (d.y != firstLabel) {
                return false;
            }
        }
        return true;
    }

    private void findBestSplit(ArrayList<Datum> datalist, DTNode node) {
        double bestAvgEntropy = Double.POSITIVE_INFINITY;
        int bestAttribute = -1;
        double bestThreshold = -1;

        for (int attr = 0; attr < datalist.get(0).x.length; attr++) {
            for (Datum d : datalist) {
                double threshold = d.x[attr];
                double currentAvgEntropy = computeAvgEntropy(datalist, attr, threshold);

                if (currentAvgEntropy < bestAvgEntropy) {
                    bestAvgEntropy = currentAvgEntropy;
                    bestAttribute = attr;
                    bestThreshold = threshold;
                }
            }
        }

        // Check if the best split found actually reduces the entropy
        if (entropyWasImproved(datalist, bestAvgEntropy)) {
            // Entropy was improved, set the node to split on the best attribute and threshold
            node.attribute = bestAttribute;
            node.threshold = bestThreshold;
        } else {
            // Entropy was not improved, set the node to be a leaf with the majority label
            node.leaf = true;
            node.label = node.findMajority(datalist);
        }
    }

    private boolean entropyWasImproved(ArrayList<Datum> datalist, double bestAvgEntropy) {
        double initialEntropy = calcEntropy(datalist);
        return bestAvgEntropy < initialEntropy;
    }


    private double computeAvgEntropy(ArrayList<Datum> datalist, int attribute, double threshold) {
        ArrayList<Datum> leftSplit = new ArrayList<>();
        ArrayList<Datum> rightSplit = new ArrayList<>();

        // Divide the data into two lists based on the threshold
        for (Datum data : datalist) {
            if (data.x[attribute] < threshold) {
                leftSplit.add(data);
            } else {
                rightSplit.add(data);
            }
        }

        // Calculate the entropy for each split
        double leftEntropy = calcEntropy(leftSplit);
        double rightEntropy = calcEntropy(rightSplit);

        // Calculate the weighted average of the entropy
        double totalSize = datalist.size();
        double weightedAvgEntropy = (leftSplit.size() / totalSize) * leftEntropy +
                (rightSplit.size() / totalSize) * rightEntropy;

        return weightedAvgEntropy;
    }
}
