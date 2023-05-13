import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static int numberOfProducers;
    private static int numberOfConsumers;
    private static List<Thread> Producers;
    private static List<Thread> Consumers;
    private static boolean done;
    private static Scanner sc;
    private static Magazine magazine;

    public static void main(String[] args) {
        done = false;
        Producers = new ArrayList<>();
        Consumers = new ArrayList<>();
        magazine = new Magazine(30,10, 1, 5);
        sc = new Scanner(System.in);
        numberOfConsumers = 0;
        numberOfProducers = 0;
        String input;
        while(!done){
            System.out.println("Type in new | quit | print | genTen");
            input = sc.nextLine();
            switch (input) {
                case "new" -> createNew();
                case "quit" -> quit();
                case "print" -> magazine.printProducts();
                case "genTen" -> generateTenPC();
            }
        }
    }

    public static void generateTenPC(){
        for(int i = 0 ; i < 5 ; i++){
            numberOfProducers++;
            numberOfConsumers++;
            Thread producer = new Thread(new Producer(magazine, 20, numberOfProducers));
            Thread consumer = new Thread(new Consumer(magazine, 20, numberOfConsumers));
            Producers.add(producer);
            Consumers.add(consumer);
            producer.start();
            consumer.start();
        }
    }

    public static void createNew(){
        System.out.println("type in Producer | Consumer");
        String  input = sc.nextLine();
        if(input.equals("Producer")){
            numberOfProducers++;
            Thread producer = new Thread(new Producer(magazine, 20, numberOfProducers));
            producer.start();
            Producers.add(producer);

        }else if(input.equals("Consumer")){
            numberOfConsumers++;
            Thread consumer = new Thread(new Consumer(magazine, 20, numberOfConsumers));
            consumer.start();
            Producers.add(consumer);
        }else{
            System.out.println("Wrong input...");
        }
    }

    public static void quit(){
        for (Thread th: Producers) {
            th.interrupt();
        }
        for (Thread th: Consumers) {
            th.interrupt();
        }

        try {
            for (Thread th : Producers) {
                th.join();
            }
            for (Thread th : Consumers) {
                th.join();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            done = true;
        }
    }

}
