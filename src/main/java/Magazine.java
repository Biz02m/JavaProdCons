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
            if(stuckIterations > stuckBound && getFreeSpace() <= 0){
                emptyMagazine();
                System.out.println("___________");
                System.out.println("___PURGE___");
                System.out.println("___________");
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
        int freeSpace = getFreeSpace();
        if(getFreeSpace() > 0) {
            if (freeSpace > quantity) {
                if(this.Products.containsKey(type)) {
                    this.Products.put(type, this.Products.get(type) + quantity);
                }else{
                    this.Products.put(type, quantity);
                }
                status = 0;
            } else {
                if(this.Products.containsKey(type)) {
                    this.Products.put(type, this.Products.get(type) + freeSpace);
                }
                else{
                    this.Products.put(type, freeSpace);
                }
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
        System.out.println("Products in the warehouse: " + getProductCount() + "/" + this.Size);
        System.out.println("The contents of the magazine are: ");
        for(String item : this.Products.keySet()) {
           System.out.println(item + ": " + this.Products.get(item));
        }
    }
}
