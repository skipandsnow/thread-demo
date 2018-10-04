public class ThreadDemo1 {

    public static void main(String[] args) {

        Horse h1 = new Horse("Horse 1");
        Horse h2 = new Horse("Horse 2");
        Horse h3 = new Horse("Horse 3");

            h1.start();
            h2.start();
            h3.start();
            try {
                h1.join();
                h2.join();
                h3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        System.out.println(h1.getLastIdUsed());
    }

}

