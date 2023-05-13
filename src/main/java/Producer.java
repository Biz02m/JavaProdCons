import java.util.Random;

public class Producer implements Runnable{
    private long ID;
    private boolean done;
    private ProductsList productList;
    private Magazine magazine;
    private final int timeBound;

    public Producer(Magazine magazine, int timeBound, int ID){
        this.done = false;
        this.ID = ID;
        this.productList = new ProductsList();
        this.magazine = magazine;
        if(timeBound < 10) {
            this.timeBound = 10;
        }else{
            this.timeBound = timeBound;
        }
    }

    public String toString() {
        return "Producer: " + ID + " ";
    }

    @Override
    public void run() {
        System.out.println(this + "has started producing objects");
        Random random = new Random();
        int randomType, randomTime, randomQuantity, quantity;
        while(!done){
            randomType = random.nextInt(ProductsList.products.length);
            randomQuantity = random.nextInt(magazine.getSize()/2);
            randomTime = random.nextInt(timeBound - 10,timeBound);

            try {
                System.out.println(this + "is putting: " + ProductsList.products[randomType] + " of quantity: " + randomQuantity + " to the magazine");
                quantity = magazine.put(ProductsList.products[randomType], randomQuantity);
                if(quantity < 0){
                    System.out.println(this + "did not manage to put any: " + ProductsList.products[randomType] + " to the magazine");
                    Thread.sleep(1000 * randomTime);
                }
                else if (quantity > 0){
                    System.out.println(this + "has placed part of the: " + randomQuantity + " objects of type: " + ProductsList.products[randomType] + " to the magazine");
                }
                else{ //quantity is 1
                    System.out.println(this + "has placed all: " + randomQuantity + " objects of type: " + ProductsList.products[randomType] + " to the magazine");
                }
                Thread.sleep(1000 * randomTime);
            } catch (InterruptedException e) {
                System.out.println(this + "Thread interrupted. Finished producing orders, ending...");
                done = true;
            }

        }
    }
}
