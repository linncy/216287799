package eecs2011.project;

import java.util.Iterator;

//stands for bad list. Because we only implement the functionality needed
class BlistIterator<E> implements Iterator<E> {
    private int i;
    private Blist b;



    public BlistIterator(Blist b){
        i = 0;
        this.b = b;

    }

    public boolean hasNext(){
        return i < b.getSize();
    }

    public E next(){
        return (E)b.get(i++);
    }


}

public class Blist<E> implements Iterable<E>{
    private int size;
    private E[] data;

    public Blist(){
        //100 because max n is 100, as per project specs
        data = (E[]) new Object[100];
        size = 0;
    }

    public void add(E x){
        data[size++] = x;
    }

    public void set(int i, E x){
        data[i] = x;
    }

    public void wipe(){
        data = (E[]) new Object[100];
        size = 0;
    }

    public boolean isEmpty(){
        return size ==0;
    }

    public int getSize(){
        return size;
    }

    public E get(int index){
        return data[index];
    }

    public Iterator<E> iterator(){
        return new BlistIterator(this);
    }




}
