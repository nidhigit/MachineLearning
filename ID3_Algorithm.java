
import java.io.*;
import java.util.*;
import java.nio.*;
public class ID3_Algorithm {
	
	public static void main(String args[])
	{
		
	//	File file = new File("C:\\Machine Learning\\hw1\\experiment-data\\experiment-data\\data\\train.csv");
		File file = new File("C:\\Machine Learning\\hw1\\experiment-data\\experiment-data\\data\\debug.csv");
		 try {
		      FileInputStream fis = new FileInputStream(file);
		      BufferedReader df    = new BufferedReader(new InputStreamReader(fis));
		      String line;
		   //   Set <String> valuesOfAttribute = (Set<String>) new ArrayList();
		      HashMap <String,List> attribute_ValueSet = new HashMap();
		     
		      String attributes[] = new String[22];
		      char labels[][]=new char[21][7000];
		      String values[][]=new String[22][7000];
		      Integer totalRows=0;
		      Integer totalColumns=0;
		      boolean firstRow=true;
		      int valuesIndex=0;
		      int index = 0 ;
		      // read the training set and populate into datastructure.
		      	while ((line = df.readLine())!=null){
		      			String[] result = line.split("\\,");
		      				for (int i=0; i<result.length; i++) {
		      					if (firstRow)
		      					{
		      						attributes[i]=result[i];
		      						totalColumns++;
		      					}
		      					else
		      					{
		      						String value = result[i];
	      							values[i][valuesIndex]=value;
	           					}
		      					index = i;
		      				}
	 		    		 if(!firstRow)
	 		    			 valuesIndex++;
	 		    		 firstRow=false;
	

	 		    }
		      	
		      	totalRows = valuesIndex;
		      	df.close();
//entropy of total tree, first column
		  
		      	
		    	double positive = 0 ;
		      	double negative = 0;
		      	int total = 0;
		      		for(int i=0;i<valuesIndex;i++)
		      			{
		      	 
		      				if (values[0][i].charAt(0) == 'e')
		      					++positive;
		      				else
		      					++negative;
		 	      		total++;
		      		 
		      			}
		      
		    System.out.println("" + valuesIndex);
		    positive = positive/total;
		    negative = negative/total;
		    
		 	      
		      double overAllEntropy = entropy((double)positive,(double)negative); 
		      System.out.println("full entropy" + overAllEntropy);

  //check information gain for individual attributes, and get the attribute index that has most information gain. 
		      int bestInformationGainIndex = 0;
		      double bestInformationGain = 0;
		      double informationGain = 0;

		    	  for(int j=1;j<totalColumns;j++)
		    	  {
		    		  informationGain = informationGain(values,totalRows,attributes,j,overAllEntropy);
		    		  System.out.println("" + attributes[j] + " info gain" + informationGain);
		    		  if (informationGain > bestInformationGain)
		    		  {
		    			  bestInformationGain = informationGain;
		    			  bestInformationGainIndex = j;
		    		  }
		    			  
		    	  }
		    	  
		      System.out.println("best information gain" + attributes[bestInformationGainIndex]);
	      
 		   }	catch (IOException err) {
 			   		err.printStackTrace();
		    	}
		 
		 System.out.println("Done");
		 
	}
	
 public static double informationGain(String values[][],int totalRows, String attributes[], int columnNumber, double overAllEntropy )
 {
	 HashMap<String, Integer> positives = new HashMap<String, Integer>();
	 HashMap<String, Integer> negatives = new HashMap<String, Integer>();
	 double informationGain=0.0f;
	 double expectedEntropy=0.0f;

	
	 // build positive/negative counts hashmap with default zeros.
	 for(int i=0;i<totalRows;i++)
	 {
		 String key = values[columnNumber][i];
		  positives.put(key,0);
		  negatives.put(key,0);
	 }
	 
	 // counte positive and negatives based on labels that are in column zero.
	 for(int i=0;i<totalRows;i++)
	 {
		 String key = values[columnNumber][i];
		 if (values[0][i].charAt(0)=='e')
		 {
			 if (positives.containsKey(key))
				 positives.put(key,positives.get(key) + 1);
			 else	 
				 positives.put(key,1);
		 }
		 else
		 {
			 if (negatives.containsKey(key))
				 negatives.put(key,negatives.get(key) + 1);
			 else	 
				 negatives.put(key,1);
		 }
	 }
	 
	 // calculate information gain for the attribute
	 
		 double entropy;
		 String key;


		 for(Map.Entry<String, Integer> entry : positives.entrySet()) {
		     key = entry.getKey();
		     
		     double positive = entry.getValue();
		     double negative = negatives.get(key);
		     double total = positive + negative;
		     
		     if(positive==0 || negative==0)
				entropy =0;
			 else
				entropy = -positive/total * log2(positive/total) - negative/total*log2(negative/total);
		     
		     expectedEntropy = expectedEntropy + (positive+negative)/totalRows * entropy;
		     
		 }

	 
	 		informationGain = overAllEntropy - expectedEntropy;
			return informationGain;
	 	 
 }
	
	public static double entropy(double positive, double negative)
	{
		double entropy;
		if(positive==0 || negative==0)
			return 0;
		else
		entropy = -positive * log2(positive) - negative*log2(negative);
		return entropy;
	}
	
	
	   public static double log2(double d) {
		      return Math.log(d)/Math.log(2.0);
		   }
	   
	 private static <T> void printTree(Node<T> node, String appender) {
		   System.out.println(appender + node.getData());
		   node.getChildren().forEach(each ->  printTree(each, appender + appender));
		 }
}

