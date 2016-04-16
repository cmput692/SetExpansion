package setexpansion.algorithm;

import static setexpansion.util.MapUtility.DESC;
import static setexpansion.util.MapUtility.sortByComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import setexpansion.util.MapUtility;

/*
Dynamic_Thresholding (𝑠𝑒𝑒𝑑𝑠, 𝑔𝑟𝑎𝑝h) for each 𝑡𝑒𝑟𝑚𝑖 in 𝑔𝑟𝑎𝑝h.𝑡𝑒𝑟𝑚𝑠 do
𝑅𝑒𝑙_𝑆𝑐𝑜𝑟𝑒[𝑖] ← 𝑆𝑟𝑒𝑙(𝑡𝑒𝑟𝑚𝑖, 𝑠𝑒𝑒𝑑𝑠) end for
𝐾0 ← Pick_Threshold(𝑅𝑒𝑙_𝑆𝑐𝑜𝑟𝑒[𝑖])
sort 𝑡𝑒𝑟𝑚𝑖 by 𝑅𝑒𝑙_𝑆𝑐𝑜𝑟𝑒[𝑖] desc
𝑅0 ← the top ranked 𝐾0 terms by 𝑅𝑒𝑙_𝑆𝑐𝑜𝑟𝑒[𝑖] 𝑖𝑡𝑒𝑟 ← 1
while 𝑖𝑡𝑒𝑟 ≤ MAX_ITER do
for each 𝑡𝑒𝑟𝑚𝑖 in 𝑔𝑟𝑎𝑝h.𝑡𝑒𝑟𝑚𝑠 do
𝑆𝑖𝑚_𝑆𝑐𝑜𝑟𝑒[𝑖] ← 𝑆𝑟𝑒𝑙(𝑡𝑒𝑟𝑚𝑖, 𝑅𝑖𝑡𝑒𝑟−1)
𝑔(𝑡𝑒𝑟𝑚𝑖) ← 𝛼 ∗ 𝑆𝑖𝑚_𝑆𝑐𝑜𝑟𝑒[𝑖] + (1 − 𝛼) ∗ 𝑅𝑒𝑙_𝑆𝑐𝑜𝑟𝑒[𝑖]
end for
𝐾𝑖𝑡𝑒𝑟 ← Pick_Threshold(𝑔(𝑡𝑒𝑟𝑚𝑖))
sort 𝑡𝑒𝑟𝑚𝑖 by 𝑔(𝑡𝑒𝑟𝑚𝑖) desc
𝑅𝑖′ ← the top ranked 𝐾𝑖𝑡𝑒𝑟 terms by 𝑔(𝑡𝑒𝑟𝑚𝑖) 𝑅𝑖𝑡𝑒𝑟 ← 𝑅𝑖′𝑡𝑒𝑟
𝑖𝑡𝑒𝑟 + +
end while
return 𝑅𝑖𝑡𝑒𝑟
*/

/**
 * @author sanket
 *
 */
public class DynamicSeisa {
	
	private static final int MAX_ITER = 5;
	
	public static Set<String> getExpandedSet(Set<String> seeds , double alpha)
    {
        
        double[][] pairWiseMatrix = { 
            {1, 0.5, 0.8, 0.7, 0.6, 0.7},    
            {0.5, 1, 0.6, 0.7, 0.7, 0.5},   
            {0.8, 0.6, 1, 0, 0.9, 0.3},   
            {0.7, 0.7, 0, 1, 0.8, 0.5},   
            {0.6, 0.7, 0.9, 0.8, 1, 0.4}, 
            {0.7, 0.5, 0.3, 0.5, 0.4, 1}  
        };
        
        /*HashMap<String, Integer > dictionary = new HashMap<String, Integer>(){{
            put("a", 0);
            put("b", 1);
            put("c", 2);
            put("d", 3);
            put("e", 4);
            put("f", 5); 
        }};*/
        
        //List<String> dictionary = Arrays.asList("a","b","c","d","e","f");
//        Set<String> seeds = new HashSet();
//        seeds.add("internship");
//        seeds.add("temporary");
        
        MapUtility mUtil = new MapUtility();
        Set<String> dictionary = mUtil.termCountMap.keySet();
        Map<String, Double > relScoreMap = mUtil.findRelScore( dictionary, seeds);
        //double fa = mUtil.pairWiseMatrixLookUp(dictionary, pairWiseMatrix, "f", "a");
        //double fb = mUtil.pairWiseMatrixLookUp(dictionary, pairWiseMatrix, "f", "b");
        //System.out.println("fa:"+fa+" fb:"+fb);
        
        //MapUtility.printMap(relScoreMap);
        Map<String, Double> sortedMapDesc = sortByComparator(relScoreMap, DESC);
//        System.out.println("Sorted Rel Scores:");
//        mUtil.printMap(sortedMapDesc);
//        
        
        Iterator it = relScoreMap.entrySet().iterator();
        double relScore[] = new double[relScoreMap.size()];
        int i = 0;
        while(it.hasNext()){
        	Map.Entry entry = (Map.Entry) it.next();
        	relScore[i] = (Double) entry.getValue();
        	i++;
        }
        //double thresHold = 0.69;
        int thresHoldIndex = mUtil.OtsusThreshold(mUtil.getValuesFromMap(sortedMapDesc));
        double thresHold = mUtil.getValuesFromMap(sortedMapDesc).get(thresHoldIndex);
       // double thresHold = mUtil.OtsusThreshold(relScore);
        //data[mUtil.OtsusThreshold(data)];
        
        Map<String, Double> topKRelScores = mUtil.findTopK(sortedMapDesc, thresHold);
//        System.out.println("TopK Rel Scores:");
//        mUtil.printMap(topKRelScores);
        
        //double alpha = 0.9;
        Set<String> prevRelTerms = topKRelScores.keySet();
        
        int iterator = 0;
        
        while(iterator<MAX_ITER){
        	System.out.println("Iteration:" + iterator);
        	System.out.println("Threshold in iterator:" + iterator + " =" + thresHold);
            Map<String, Double > simScoreMap = 
                    mUtil.findRelScore(dictionary, prevRelTerms);
//            System.out.println("Sim Scores:");
//            mUtil.printMap(simScoreMap); 
//            
            Map<String, Double > gScoreMap = mUtil.findGScore(relScoreMap, simScoreMap,alpha);
//            System.out.println("g Scores:");
//            mUtil.printMap(gScoreMap);
//            
//            Iterator entries = gScoreMap.entrySet().iterator();
//            double gScores[] = new double[gScoreMap.size()];
//            int j = 0;
//            while(entries.hasNext()){
//            	Map.Entry entry = (Map.Entry) entries.next();
//            	gScores[j] = (Double) entry.getValue();
//            	j++;
//            }
            
            Map<String, Double> sortedGScoreMap = sortByComparator(gScoreMap, DESC);
            thresHoldIndex = mUtil.OtsusThreshold(mUtil.getValuesFromMap(sortedGScoreMap));
            thresHold = mUtil.getValuesFromMap(sortedGScoreMap).get(thresHoldIndex);
            //thresHold = mUtil.OtsusThreshold(gScores);
            //thresHold = 0.69;
            Set<String> nextRelTerms = mUtil.findTopK(sortedGScoreMap, thresHold).keySet();
            prevRelTerms = nextRelTerms;
            iterator++;
            

          
         }  
      
      return prevRelTerms;
         
    }

}
