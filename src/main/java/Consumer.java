import java.util.Random;

public class Consumer implements Runnable{
    private final long ID;
    private boolean done;
    private ProductsList productList;
    private Magazine magazine;
    private final int timeBound;

    public Consumer(Magazine magazine, int timeBound, int ID){
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
            randomTime = random.nextInt(timeBound - 10, timeBound);
            System.out.println(this + "is ordering: " + ProductsList.products[randomType] + " of quantity: " + randomQuantity);
            try {
                quantity = magazine.take(ProductsList.products[randomType], randomQuantity);
                if(quantity > 0){
                    System.out.println(this + "has received: " + randomQuantity + " objects of type: " + ProductsList.products[randomType]);
                }
                else{
                    System.out.println(this + "estimated time elapsed or magazine did not have the order, did not receive: " + ProductsList.products[randomType]);
                }
                Thread.sleep(1000 * randomTime);
            } catch (InterruptedException e) {
                System.out.println(this + "Thread interrupted. Finished placing orders, ending...");
                done = true;
            }
        }
    }
}
