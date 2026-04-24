public class Main {

    // Shared Resource (Monitor)
    static class SharedResource {
        private int value;
        private boolean bChanged = false;

        // Consumer method
        public synchronized int get() {
            while (!bChanged) {
                try {
                    wait(); // wait until producer produces
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Interrupted in get()");
                }
            }

            bChanged = false; // mark as consumed
            notify(); // notify producer
            return value;
        }

        // Producer method
        public synchronized void set(int value) {
            while (bChanged) {
                try {
                    wait(); // wait until consumer consumes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Interrupted in set()");
                }
            }

            this.value = value;
            bChanged = true; // mark as new data available
            notify(); // notify consumer
        }
    }

    // Producer Thread
    static class Producer extends Thread {
        private SharedResource resource;

        public Producer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            int data = 0;
            while (true) {
                resource.set(data);
                System.out.println("Produced: " + data);
                data++;

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Consumer Thread
    static class Consumer extends Thread {
        private SharedResource resource;

        public Consumer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            while (true) {
                int data = resource.get();
                System.out.println("Consumed: " + data);

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Producer producer = new Producer(resource);
        Consumer consumer = new Consumer(resource);

        producer.start();
        consumer.start();
    }
}
