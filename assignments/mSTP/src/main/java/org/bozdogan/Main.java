package org.bozdogan;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.HelpRequestedException;

public class Main{
    public static void main(String[] args){
        JewelParams argv;
        try{
            argv = CliFactory.parseArguments(JewelParams.class, args);
        } catch(HelpRequestedException e){
            // Print help.
            System.out.println(e.getMessage());
            return;

        } catch(ArgumentValidationException e){
            System.out.println("Invalid arguments.");
            System.out.println("Usage: mtsp-gen -d numberOfDepots -s numberOfSalesmen [-v]");
            System.out.println("\nTry ´mtsp-gen --help´ for more information.");
            return;
        }

        // Solution: former mTSP
        Solution best = null;
        int minCost = Integer.MAX_VALUE;

        for(int i = 0; i<100000; i++){
            Solution current = new Solution(argv.getNumDepots(), argv.getNumSalesmen());

            current.randomSolution();
            try{
                current.validate();
            } catch(Exception e){
                System.err.println("One of the systems didn't validate\n\tat iteration #"+i);
            }

            int cost = current.getCost();
            if(cost<minCost){
                best = current;
                minCost = cost;
            }
        }

        if(best!=null){
            best.print(argv.isVerbose());
            System.out.println("**Total cost is "+best.getCost());
        }
    }
}
