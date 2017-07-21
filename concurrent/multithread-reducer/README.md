Multi-threaded Reducer
======================

The multi-threaded reducer is a data structure that allows multiple threads to aggregate values together. At 
the end, all values (across all threads) can be accumulated with the same reduce() function. 

An example
----------

Let's say we want to track the amount of money we spend across several categories across the year. We have a magic function 
that returns a category and how much we've spent on some day. Here's some code!

First, we implement our Reducer.

    public class SpendingReducer extends MultithreadReducer<String, Double> {
        @Override
        public Double reduce(String category, Double totalSoFar, Double nextPurchase) {
          return totalSoFar + nextPurchase;
        }
    }

Next, we create a Runnable task that uses our Reducer.
    
    public class SpendingComputationWorker implements Runnable {
        private Map<String, Double> partOfSpending;
        private ConcurrentHashMap<String, Double> globalSpending;
        
        public SpendingComputationWorker(SpendingReducer reducer, Map<String, Double> partOfSpending, ConcurrentHashMap<String, Double> globalSpending) {
          this.reducer = reducer;
          this.partOfSpending = partOfSpending;
          this.globalSpending = globalSpending;
        }
    
        @Override
        public void run() {
           for (Map.Entry<String, Double> nextPurchase : partOfSpending.entrySet()) {
             reducer.reduceLocal(nextPurchase.getKey(), nextPurchase.getValue());
           }
           reducer.reduceGlobal(globalSpending);
        }
    }
    
Finally, schedule your Runnables!

    public static void main(String[] args) {
      ExecutorService executor = Executors.newFixedThreadPool(numThreads);
      SpendingReducer reducer = new SpendingReducer();
      ConcurrentHashMap<String, Double> globalSpending = new ConcurrentHashMap<>();
      
      for (int i = 0; i < numThreads; ++i) {
        Map<String, Double> partOfSpending = // get this workers piece
        executor.submit(new SpendingComputationWorker(reducer, partOfSpending, globalSpending);
      }
      
      executor.shutdown();
      executor.awaitTermination(MAX_WAIT_TIME, TimeUnit.MILLISECONDS);
      
      // Yahhhh, we did it.
      
      System.out.println(globalSpending);
      // OUTPUT:
      // {BEER=12050.25, VIDEO_GAMES=240.00, FOOD=100.00, RENT=15000.00, CAR=2300.00}
    }
