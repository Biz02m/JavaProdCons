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
        magazine = new Magazine(20,3, 3, 5);
        sc = new Scanner(System.in);
        String input;
        System.out.println("Type in new | quit | print");
        while(!done){
            input = sc.nextLine();
            switch (input) {
                case "new" -> createNew();
                case "quit" -> quit();
                case "print" -> magazine.printProducts();
            }
        }
    }

    public static void createNew(){
        System.out.println("type in Producer | Consumer");
        String  input = sc.nextLine();
        if(input.equals("Producer")){
            Thread producer = new Thread(new Producer(magazine, 5));
            producer.start();
            Producers.add(producer);

        }else if(input.equals("Consumer")){
            Thread consumer = new Thread(new Consumer(magazine, 5));
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
