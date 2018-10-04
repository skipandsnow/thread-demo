public class DeadlockDemo {

    public static void main(String[] args) {

        Object obj1 = new Object();
        Object obj2 = new Object();

        Thread a = new Thread(() -> { // Runnable.run()
            String threadName = Thread.currentThread().getName();
            synchronized (obj1) {
                System.out.println(threadName + ": 取得 obj1 的鎖");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(threadName + ": 等待 obj2 的鎖");
                synchronized (obj2) {
                    System.out.println(threadName + ": 取得 obj2 的鎖");
                }
            }
        }, "thread-a");

        Thread b = new Thread(() -> { // Runnable.run()
            String threadName = Thread.currentThread().getName();
            synchronized (obj2) {
                System.out.println(threadName + ": 取得 obj2 的鎖");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(threadName + ": 等待 obj1 的鎖");
                synchronized (obj1) {
                    System.out.println(threadName + ": 取得 obj1 的鎖");
                }
            }
        }, "thread-b");

        a.start();
        b.start();

    }
}