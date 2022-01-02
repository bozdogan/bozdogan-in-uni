package org.bozdogan;

import java.util.*;

/** Entity that covers the whole salesmen network. (former mTSP) */
public class Solution{
    /* System has multiple depots that doesn't have any city from other depots.
     * A depot has one route per salesman and every city must be visited one of the salesman. */

    // Global data set values
    private final String[] cityNames = TurkishNetwork.cities;
    private final int[][] distanceMatrix = TurkishNetwork.distance;

    private int numberOfDepots;
    private int numberOfSalesmen;
    private int cost = Integer.MAX_VALUE;

//    private static int MIN_CITIES_PER_ROUTE; //@TODO
    private List<Pair<Integer, List<List<Integer>>>> depots;

    /** Your standard constructor */
    public Solution(int depots, int salesmen){
        numberOfDepots = depots;
        numberOfSalesmen = salesmen;
    }

    // Getters
    public int getNumberOfDepots(){ return numberOfDepots; }
    public int getNumberOfSalesmen(){ return numberOfSalesmen; }

    /** Calculates the cost. It returns {@code Integer.MAX_VALUE} if the
     * Solution couldn't {@linkplain #validate()}.*/
    public int getCost(){
        // Use ´validate´ to calculate cost.
        if(cost==Integer.MAX_VALUE){
            // Either validate() isn't called yet or it failed to validate.
            try{ validate(); }
            catch(Exception e){/* > /dev/null */}
        }

        return cost;
    }

    /** Produces a Solution with randomly assigned cities. */
    public void randomSolution(){
        HashSet<Integer> used_cities = new HashSet<>();
        int randomCity;

        depots = new ArrayList<>();
        // Creating depots
        for(int i = 0; i<numberOfDepots; i++){
            do{
                randomCity = randint(0, cityNames.length);
            } while(!used_cities.add(randomCity));

            depots.add(new Pair<>(randomCity, new ArrayList<>()));
            // Creating routes
            for(int j = 0; j<numberOfSalesmen; j++){
                depots.get(i).getValue().add(new ArrayList<>());
                depots.get(i).getValue().get(j);
            }
        }

        // Randomly filling routes w/ cities
        while(used_cities.size()<cityNames.length){
            do{
                randomCity = randint(0, cityNames.length);
            } while(!used_cities.add(randomCity));

            // There is ´depot´ times ´salesman´ routes.
            int _depot = randint(0, numberOfDepots);
            int _route = randint(0, numberOfSalesmen);
            depots.get(_depot).getValue().get(_route).add(randomCity);
        }
    }

    /** Checks if Solution satisfied the requirements. */
    public void validate() throws Exception {
        // Make sure that all cities are in the network and every city
        // is visited only once (except for depots, which are visited twice).
        // Also, calculate the cost.

        List<Integer> cityCounts = new ArrayList<>();
        for(int i = 0; i<81; i++){ cityCounts.add(0); }

        int cost = 0;

        // depoları gez
        for(Pair<Integer, List<List<Integer>>> depot: depots){
            int depotCity = depot.getKey();
            cityCounts.set(depotCity, cityCounts.get(depotCity)+1); // counting cities
            List<List<Integer>> currentDepot = depot.getValue();

            // rotaları gez
            for(List<Integer> currentRoute: currentDepot){
                int prevCity = depotCity;
                for(int currentCity: currentRoute){
                    cost += distanceMatrix[prevCity][currentCity]; // calculating cost
                    cityCounts.set(currentCity, cityCounts.get(currentCity)+1); // counting cities
                    prevCity = currentCity; // iterate
                }
                // ´prevCity´ is the last city in current route at this point.
                cost += distanceMatrix[prevCity][depotCity];
            }
        }

        for(Integer city : cityCounts){
            if(city!=1)
                throw new Exception("Improper city placement.");
        }

        this.cost = cost;
    }

    /** Prints the Solution in a readable format. */
    public void print(boolean isVerbose){
        for(int i=0; i<depots.size(); i++){
            Pair<Integer, List<List<Integer>>> depot = depots.get(i);
            System.out.println("Depot"+(i+1)+": "+
                    (isVerbose ? cityNames[depot.getKey()] : depot.getKey()));

            for(int j=0; j< depot.getValue().size(); j++){
                List<Integer> route = depot.getValue().get(j);
                if(route!=null){
                    System.out.print("  Route"+(j+1)+": ");
                    for(int k=0; k<route.size();k++)
                        System.out.print((k==0 ? "" : ",")+
                                (isVerbose ? cityNames[route.get(k)] : route.get(k))
                        );
                    System.out.println();
                }
            }
        }
    }

    /** Range is {@code min}(included) to {@code max}(excluded).*/
    private static int randint(int min, int max){
        return (int)(Math.random()*max+min);
    }
}
