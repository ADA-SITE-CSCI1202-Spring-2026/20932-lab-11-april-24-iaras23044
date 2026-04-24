class MathTask implements Runnable {
    private int id;

    public MathTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
         int sum = 0;
        for (int j = 0; j < 10000000; j++) {
            sum += id*j + Math.pow(id, 3);
        }
        System.out.println("Loop: " + id + " finished with sum: " + sum);
    }
}

public class Lab {
    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        long startTime = System.currentTimeMillis();
        Thread [] threads = new Thread[cores];
       
        for (int i = 1; i <= cores; i++) {
            MathTask task = new MathTask(i);
            Thread t = new Thread(task);
            threads[i - 1] = t;
            threads[i - 1].start();
        }

        for (int i = 0; i < cores; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                System.out.println("Error message: " + e.toString());
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
}