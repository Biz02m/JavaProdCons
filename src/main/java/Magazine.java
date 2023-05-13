import java.util.HashMap;
import java.util.Map;

public class Magazine {
    private final int Size;
    private final Map<String, Integer> Products;
    private final int timeout;
    private final int NumberOfTries;
    private int stuckIterations;
    private final int stuckBound;

    public Magazine(int Size, int timeout, int NumberOfTries, int stuckBound){
        this.Size = Size;
        this.timeout = timeout;
        this.NumberOfTries = NumberOfTries;
        this.stuckBound = stuckBound;
        this.Products = new HashMap<>();
        this.stuckIterations = 0;
    }

    public int getSize() {
        return Size;
    }

    public int getProductCount(){
        int prodCount = 0;
        for (Integer t: Products.values()) {
            prodCount += t;
        }
        return  prodCount;
    }

    public void emptyMagazine(){
        this.Products.clear();
    }

    public int getFreeSpace(){
        return this.Size - getProductCount();
    }

    //this function returns the quantity of items that were taken from magazine
    //method for consumer threads
    public synchronized int take(String type, int quantity) throws InterruptedException {
        int retQ;
        int tries = 0;
        while(!this.Products.containsKey(type)){
            wait(1000*timeout);
            tries ++;
            if(tries >=NumberOfTries){
                retQ = -1;
                stuckIterations++;
                return retQ;
            }

            //If we spotted that a number of orders have been cancelled: remove all
            //contents from the magazine.
            if(stuckIterations > stuckBound){
                emptyMagazine();
            }
        }
        stuckIterations--;
        if(this.Products.get(type) > quantity){
            this.Products.put(type, this.Products.get(type) - quantity);
            retQ = quantity;
        }
        else{
            retQ = quantity - this.Products.get(type);
            this.Products.remove(type);
        }
        return retQ;
    }

    public synchronized int put(String type, int quantity){
        int status;
        if(getFreeSpace() != 0) {
            if (getFreeSpace() > quantity) {
                this.Products.put(type, this.Products.get(type) + quantity);
                status = 0;
            } else {
                int fill = quantity - getFreeSpace();
                this.Products.put(type, this.Products.get(type) + fill);
                status = 1;
            }
        }
        else{
            status = -1;
        }
        notifyAll();
        return status;
    }

    public synchronized void printProducts(){
        System.out.println("The contents of the magazine are: ");
        for(String item : this.Products.keySet()) {
           System.out.println(item + ": " + this.Products.get(item));
        }
    }
}
