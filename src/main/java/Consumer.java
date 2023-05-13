import java.util.Random;

public class Consumer implements Runnable{
    private long ID;
    private boolean done;
    private ProductsList productList;
    private Magazine magazine;
    private final int timeBound;

    public Consumer(Magazine magazine, int timeBound){
        this.done = false;
        this.timeBound = timeBound;
        this.ID = Thread.currentThread().getId();
        this.productList = new ProductsList();
        this.magazine = magazine;
    }

    @Override
    public String toString() {
        return "Consumer: " + ID + " ";
    }

    @Override
    public void run() {
        System.out.println(this + "has started ordering objects");
        Random random = new Random();
        int randomType, randomTime, randomQuantity, quantity;
        while(!done){
            randomType = random.nextInt(ProductsList.products.length);
            randomQuantity = random.nextInt(magazine.getSize()/2);
            randomTime = random.nextInt(timeBound);
            System.out.println(this + "is ordering: " + ProductsList.products[randomType] + " of quantity: " + randomQuantity);
            try {
                Thread.sleep(1000 * randomTime);
                quantity = magazine.take(ProductsList.products[randomType], randomQuantity);
                if(quantity > 0){
                    System.out.println(this + "has received: " + randomQuantity + " objects of type: " + ProductsList.products[randomType]);
                }
                else{
                    System.out.println(this + "estimated time elapsed or magazine did not have the order, did not receive: " + ProductsList.products[randomType]);
                }
            } catch (InterruptedException e) {
                System.out.println(this + "Thread interrupted. Finished placing orders, ending...");
                done = true;
            }
        }
    }
}
