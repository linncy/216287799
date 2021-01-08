package eecs2011.project;

import java.math.BigInteger;
import java.util.Scanner;

public class tests {

    public static void testBset(){

        Blist<Integer> b = new Blist<>();

        for(int i=0; i<100; i++){
            b.add(i);
        }

        System.out.println(b.getSize());
        for(Integer i: b){
            System.out.println(i);
        }
        b.wipe();
        System.out.println(b.getSize());
        for(Integer i: b){
            System.out.println(i);
        }
        System.out.println("end");
    }

    public static void testnestedBset(){
        Blist<Blist<Integer>> nb =  new Blist<>();
        for(int i=0; i<10; i++){
            nb.add(new Blist<Integer>());
            for(int j=0; j<100; j++){
                nb.get(i).add(100*i + j);
            }
        }

        for(Blist<Integer> b: nb){
            for(Integer i: b){
                System.out.print(i + ", ");
            }
            System.out.println("");
        }

    }


    public static void main(String[] args) {
        BigInteger w = BigInteger.valueOf(20);
        System.out.println(w.toString(2));
        System.out.println(w.getLowestSetBit());
        w = w.clearBit(w.getLowestSetBit());
        System.out.println(w.toString(2));
        System.out.println(w.getLowestSetBit());
        Scanner input  = new Scanner(System.in);
        //int q = input.nextInt();
    }
}
