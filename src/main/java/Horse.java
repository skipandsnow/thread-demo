public class Horse extends Thread {
    private static int lastIdUsed;

    private String threadName;

    public Horse(String threadName) {
        this.threadName = threadName;
    }

    //覆寫Thread方法run()
    public void run() {
        for (int i = 0; i < 5000; i++) {
            ++lastIdUsed;
//            System.out.println(threadName + "+1");
        }
    }

    public int getLastIdUsed() {
        return lastIdUsed;
    }

    public void setLastIdUsed(int lastIdUsed) {

        this.lastIdUsed = lastIdUsed;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

}
