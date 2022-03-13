import java.util.Arrays;
import java.util.Scanner;



public class RouteToGateway {

    public static void main(String[] args){
        int[][] pairs;
        //int source;
        int MAXSIZE = 99999;    // value to mimic infinity when initializing the list
        int min; 
        Boolean[] VisitedSet;

        Scanner scanner = new Scanner(System.in);

        int size = scanner.nextInt();
        int[][] graph = new int[size][size];

        for (int r = 0; r < size; r++){
            for (int c = 0; c < size; c++){
                graph[r][c] = scanner.nextInt();
            } 
        }

        scanner.nextLine();
        String line = scanner.nextLine();
        String[] nums = line.split(" ");
        int[] terminals = new int[nums.length];

        for (int i = 0; i < nums.length; i++){
            terminals[i] = Integer.parseInt(nums[i]);
        }

// NEED TO GET THIS FROM INPUT
        //int size = 2;

        /*
        int[][] graph = {   {0,1,10,-1,-1,2},
                            {10,0,1,-1,-1,-1},
                            {1,10,0,-1,-1,-1},
                            {-1,-1,2,0,1,10},
                            {-1,-1,-1,10,0,1},
                            {-1,-1,-1,1,10,0}
                        };
        */
        /*
        int[][] graph = {   {0,10,10,-1,1,1,-1},
                            {10,0,-1,1,1,-1,-1},
                            {10,-1,0,-1,-1,1,1},
                            {-1,1,-1,0,-1,-1,-1},
                            {1,1,-1,-1,0,-1,-1},
                            {1,-1,1,-1,-1,0,-1},
                            {-1,-1,1,-1,-1,-1,0}
                    };
        int[][] graph = {{0,-1},{-1,0}};
        int[] terminals = {1};
        */

        int TerminalsLength = terminals.length;
        int[][] output = new int[TerminalsLength][3];




// Need to get code working

        graph = EliminateNegatives(graph, size, MAXSIZE);
        
        for (int i = 1; i < size + 1; i++){

            // setup the output terminals so we can print out at the end
            for (int k = 0; k < TerminalsLength; k++){
                output[k][0] = terminals[k];
                output[k][1] = -1;
                output[k][2] = -1;
            }
            //System.out.println(Arrays.deepToString(output));    

            VisitedSet = ResetVisited(size);
            pairs = ResetPairs(size, MAXSIZE);
    
            //System.out.println(Arrays.deepToString(VisitedSet));
            //System.out.println(Arrays.deepToString(pairs));

            if (Arrays.binarySearch(terminals, i) < 0){
                pairs[i][0] = 0;
                pairs[i][1] = 0;
                pairs = UpdateDistances(pairs, i, VisitedSet, graph, size);


                for (int j = 1; j < size+1; j++){
                    min = ExtractMin(pairs, VisitedSet, MAXSIZE, size);

                    //System.out.println("Extracted a Min value of  " + min);
                    if (min != -1){
                        pairs = UpdateDistances(pairs, min, VisitedSet, graph, size);
                    }
                   
                    //System.out.println(Arrays.deepToString(pairs));
                }

                System.out.println("Forwarding Table for " + i);
                System.out.println("\t To \t Cost \t Next Hop");

                // update the output table for distance and next hop
                for (int j = 0; j < TerminalsLength; j++){
                    int node_num = output[j][0];
                    int distance = pairs[node_num][0];

                    if (distance > 0 && distance < MAXSIZE){
                        output[j][1] = pairs[node_num][0];
                        int last_hop = FindLastHop(node_num, pairs, size, i);
                        output[j][2] = last_hop;
                    }

                System.out.println("\t   " + output[j][0] + "\t     " + output[j][1] + "\t        " + output[j][2]);
                }

                System.out.println();
                //System.out.println(Arrays.deepToString(output)); 

                //System.out.println(Arrays.deepToString(pairs));

            }
        }
    }

    static public Boolean[] ResetVisited(int size){
        Boolean VisitedSet[] = new Boolean[size + 1];

        for (int i = 0; i < size + 1; i++){
            VisitedSet[i] = false;
        }

        return VisitedSet;
    
    }
    static public int[][] ResetPairs(int size, int MAXSIZE){
        /* 
        initialize the list of pairs of data to show that we have yet to visit any node in the 
        graph, as a result our distance to any is the MAXSIZE, and the prior node will be -1
        Also take the opportunity to make note in the set that we have not visited the node
        */

        int [][] pairs = new int[size + 1][2];

        for (int i = 0; i < size + 1; i++){
            pairs[i][0] = MAXSIZE;
            pairs[i][1] = -1;
        }

        return pairs;

    }

    static public int[][] EliminateNegatives (int [][]graph, int size, int MAXSIZE){
        for(int r = 0; r < size; r++){
            for(int c = 0; c < size; c++){
                if (graph[r][c] == -1){
                    graph[r][c] = MAXSIZE;
                }
            }
        }
        return graph;
    }

    static public int[][] UpdateDistances (int [][] pairs, int current, Boolean[] VisitedSet, int[][] graph, int size){
        //System.out.println("checking Updated Distances");
        //System.out.println(Arrays.deepToString(graph));
        //System.out.println(Arrays.deepToString(pairs));
        if (VisitedSet[current] == false){

            for (int i = 1; i < size + 1; i++){
                //System.out.println("IN the update with " + i + " and current of " + current);
                //System.out.println(Arrays.deepToString(VisitedSet));

                if (VisitedSet[i] == false){
                    
                    //System.out.println(" DID I EVER GET HERE");
                    int existing = pairs[i][0];
                    int possible = pairs[current][0] + graph[current-1][i-1];

                    //System.out.println("Found existing  " + existing + "and possible " + possible);

                    if (possible < existing){
                        pairs[i][0] = possible;
                        pairs[i][1] = current;
                    }
                }
            }
            // set that we have now visited the node
            VisitedSet[current] = true;

        }
        //System.out.println(Arrays.deepToString(pairs));
        return pairs;
    }


    static public int ExtractMin(int[][] pairs, Boolean [] VisitedSet, int MAXSIZE, int size){
        int min = MAXSIZE;
        int index = -1;

        //System.out.println(Arrays.deepToString(pairs));

        for (int i = 1; i < size + 1; i++){


            if ((VisitedSet[i] == false) && (pairs[i][0] < min)){
                min = pairs[i][0];
                index = i;
            }
        }
        return index;
    }

    
    static public int  FindLastHop(int node_num, int[][]pairs, int size, int start){
        for (int i = 1; i < size; i++){
            if(pairs[node_num][1] != start){
                node_num = pairs[node_num][1];
            }

        }

        return node_num;

    }


}
