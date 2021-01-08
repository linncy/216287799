package eecs2011.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * The Solution class is where you write your solutions.
 * Please do NOT modify the method declarations and the package name.
 * You can add other classes to the package if needed.
 * Read data from standard input and write your answer to standard output.
 * We use pipelines to send input to your program and send your output to our judge system.
 * Please do NOT read and write data from and to the disk.
 * You should strictly follow the sample output format.
 * Please do not use any built-in/third-party data structures or algorithms.
 * Memory Limit: 256MB  Time Limit: 10s
 */
public class Solution {

    public BigInteger[] vmatrix;
    public BigInteger[] hmatrix;
    public int n;
    public int[] rt;
    public Blist<Blist<Integer>> topo;
    public BigInteger visited;


    public Solution(){
        try {
            //todo: make sure to read from system.in in real version
            Scanner input = new Scanner(System.in);

            visited = BigInteger.ZERO;
            n =  input.nextInt();
            topo = new Blist<Blist<Integer>>();
            vmatrix = new BigInteger[n+2];
            hmatrix =  new BigInteger[n+2];
            rt =  new int[n+1];

            int next = 0;
            BigInteger bignext;

            for(int i=0; i< n+2;i++){
                vmatrix[i] = BigInteger.ZERO;
                hmatrix[i] = BigInteger.ZERO;
            }

            for(int i=0; i<n+2 ;i++){


                //read adjacency matrix
                for(int j=0; j<n+2; j++){
                    next = input.nextInt();

                    //a more efficient way of getting a bigint version of next
                    if(next == 1){
                        bignext = BigInteger.ONE;
                    }
                    else{
                        bignext = BigInteger.ZERO;
                    }

                    //add to horizontal number
                    hmatrix[i] = hmatrix[i].shiftLeft(1);
                    hmatrix[i] = hmatrix[i].add(bignext);

                    //add to vertical number
                    vmatrix[j] = vmatrix[j].shiftLeft(1);
                    vmatrix[j] = vmatrix[j].add(bignext);

                }



            }
            for(int i=1; i<n+1; i++){
                rt[i] = input.nextInt();
                //topo[i] = new int[n];
            }


        }catch (FileNotFoundException e){System.out.println("file not found");}
    }

    public static void prBin(BigInteger q){
        byte[] x = q.toByteArray();
        for (byte i: x){
            System.out.print(Integer.toBinaryString(Integer.valueOf((int) i)));
        }
        System.out.println("");
    }

    public int[] flattenTopo(){
        int[] result = new int[n+2];
        int c = 0;
        for(Blist<Integer> i: topo){
            for(Integer j: i){
                result[c++] = j;
            }
        }
        return result;

    }

    public void maketopo(){
        topo.add(new Blist<Integer>());
        topo.get(0).add(0);
        int d = 0;
        BigInteger next = BigInteger.ZERO;
        int potential = 0;
        visited = visited.setBit(n+1);
        BigInteger duplicatemask;
        topo.add(new Blist<Integer>());

//        for(Integer i: topo.get(d)){
//            visited.setBit(i);
//        }

        while(!topo.get(d).isEmpty()) {
            topo.add(new Blist<Integer>());
            duplicatemask =  BigInteger.valueOf(2).pow(n+2).subtract(BigInteger.ONE);

            for (Integer i : topo.get(d)) {
                hmatrix[i] = hmatrix[i].and(duplicatemask);
                while ((potential =hmatrix[i].getLowestSetBit()) != -1) {
                    hmatrix[i] = hmatrix[i].clearBit(potential);
                    potential = n + 1 - potential;

                    if ((vmatrix[potential] = visited.not().and(vmatrix[potential])).compareTo(BigInteger.ZERO) == 0) {
                        topo.get(d+1).add(potential);
                        duplicatemask = duplicatemask.clearBit(n+1-potential);

                    }
                }
            }
            d++;
            for (Integer i : topo.get(d)) {
                visited = visited.setBit(n + 1 - i);
                //System.out.println(n + 1 - i);
            }

        }




    }
    /**
     * Solution to Part 1
     */
    public void check_validity() {
        boolean result = true;

        //check if every fn is connected to end
        result &= vmatrix[n+1].compareTo(BigInteger.valueOf(2).pow(n+1).subtract(BigInteger.valueOf(2))) == 0;

        result &= vmatrix[0].compareTo(BigInteger.ZERO) == 0;

        if(result){
            this.maketopo();
        }
        for(BigInteger i: vmatrix){
            result &= i.compareTo(BigInteger.ZERO) == 0;
        }

        for(BigInteger i: hmatrix){
            result &= i.compareTo(BigInteger.ZERO) == 0;
        }

        result &= visited.compareTo(BigInteger.valueOf(2).pow(n+2).subtract(BigInteger.ONE)) == 0;


        System.out.println(result);
    }

    /**
     * Solution Part 2
     */
    public void schedule_1() {
        int time = 0;
        this.maketopo();
        int[] flat = flattenTopo();

        for(int i=1; i< n+1; i++){
            System.out.println(flat[i] + " " + time);
            time += rt[flat[i]];
        }
        System.out.println(time);

    }

    /**
     * Solution to Part 3
     */
    public void schedule_x() {
        int[] paths = new int[n+2];

        BigInteger[] vmatrix = new BigInteger[n+2];
        for(int i=0; i< n+2; i++){
            vmatrix[i] = this.vmatrix[i];
        }
        this.maketopo();
        int[] flat = this.flattenTopo();
        int current = 0;
        int max = 0;


        for(Integer i: flat){
            System.out.println("Operating on: " + i);
            System.out.println("Vmatrix = ");
            prBin(vmatrix[i]);
            max = 0;;
            while ((current = vmatrix[i].getLowestSetBit()) != -1){
                vmatrix[i] = vmatrix[i].clearBit(current);
                current = n + 1 - current;
                System.out.println(rt[current]);
                if(paths[current] + rt[current] > max){
                    max = paths[current] + rt[current];

                }
            }
            paths[i] = max;
        }

        System.out.println("paths");
        for(int i=1; i<n+1; i++){
            System.out.println(i + " " + paths[i]);
        }
        System.out.println(paths[n+1]);
    }

    public void reverse(int[] in){
        int[] result = new int[n+2];
        int[] temp = new int[n+2];
        for(int i=0; i<n+2; i++){
            result[n+2-i -1] = in[i];
        }
        for(int i=0; i<n+2; i++){
            in[i] = result[i];
        }
    }

    //inserts element in its place for a sorted list
    public static void badSortAdd(Blist<Integer[]> list, Integer[] add){
        int  i = list.getSize() - 1;
        if(list.getSize() == 0){
            list.add(add);
            return;
        }

        if(add[1] > list.get(list.getSize() - 1)[1]) {
            list.add(add);
            return;
        }
        else if(add[2] > list.get(i)[2]){
            list.add(add);
            return;
        }
        list.add(list.get(i));
        while((list.get(i)[1] > add[1]) || (list.get(i)[1] == add[1] && list.get(i)[2] > add[2]) && i !=0){
            list.set(i+1, list.get(i--));
        }
        list.set(i+1, add);
    }
    /**
     * Solution to Part 4 (optional)
     */
    public void schedule_2() {
        int[] epaths = new int[n+2];
        int[] dpaths = new int[n+2];
        BigInteger[] vmatrix0 = new BigInteger[n+2];
        BigInteger[] vmatrix1 = new BigInteger[n+2];

        for(int i=0; i<n+2; i++){
            vmatrix0[i] = vmatrix[i];
            vmatrix1[i] = vmatrix[i];
        }
        maketopo();
        int[] flat = flattenTopo();
        reverse(flat);


        int current = 0;
        int max = 0;
        for(Integer i: flat){
            System.out.println("Operating on: " + i);
            System.out.println("Vmatrix = ");
            max = 0;;
            while ((current = vmatrix0[i].getLowestSetBit()) != -1){
                vmatrix0[i] = vmatrix0[i].clearBit(current);
                current = n + 1 - current;
                System.out.println(rt[current]);
                if(dpaths[current] + rt[current] > max){
                    max = dpaths[current] + rt[current];

                }
            }
            dpaths[i] = max;
        }

        current = 0;
        max = 0;
        for(Integer i: flat){
            System.out.println("Operating on: " + i);
            System.out.println("Vmatrix = ");
            prBin(vmatrix1[i]);
            max = 0;;
            while ((current = vmatrix1[i].getLowestSetBit()) != -1){
                vmatrix1[i] = vmatrix1[i].clearBit(current);
                current = n + 1 - current;
                if(epaths[current] + 1 > max){
                    max = epaths[current] + 1;

                }
            }
            epaths[i] = max;
        }

        Integer[][] functions = new Integer[n][3];
        Blist<Integer[]> functionlist = new Blist<>();

        for(int i=1; i<n; i++){
            functions[i][0] = i;
            functions[i][1] = epaths[i];
            functions[i][2] = dpaths[i];
            badSortAdd(functionlist, functions[i]);
        }
        int time = 0 ;
        int fnNumber = 0;
        int m1t = 0;
        int m2t = 0;
        while(fnNumber != n-1){
            System.out.println(fnNumber + " " + m1t);
            m1t += rt[functionlist.get(fnNumber)[0]];
            fnNumber++;
            if(fnNumber != n-1) {
                System.out.println(fnNumber + " " + m2t);
                m2t += rt[functionlist.get(fnNumber)[0]];
                fnNumber++;
            }
        }



    }

    public static void main(String[] args) {
      Solution q = new Solution();
//        System.out.println("Testing constructor and input");
//        System.out.println("rt:");
//        for( int i: q.rt){
//            System.out.println(i);
//        }
//
//        System.out.println("\nvmatrix\n");
//        for(BigInteger i: q.vmatrix){
//            System.out.printf("%06d \n",Integer.parseInt(i.toString(2)));
//        }
//
//        System.out.println("\nhmatrix \n");
//        for(BigInteger i: q.hmatrix){
//            System.out.printf("%06d \n", Integer.parseInt(i.toString(2)));
//

        q.schedule_2();



    }
}
