package ch13concurrency;

import java.util.concurrent.*;

/**
 * SCHEDULED EXECUTORS AND THREAD POOLS
 * =====================================
 *
 * ScheduledExecutorService for scheduling tasks and factory methods for thread pools.
 *
 *
 * ============================================================================
 * SCHEDULEDEXECUTORSERVICE INTERFACE
 * ============================================================================
 *
 * Extends ExecutorService to support scheduling tasks with delays and repetition.
 *
 * HIERARCHY:
 *   Executor
 *      ↑
 *   ExecutorService
 *      ↑
 *   ScheduledExecutorService
 *
 *
 * ============================================================================
 * SCHEDULED METHODS - KNOW THESE SIGNATURES!
 * ============================================================================
 *
 * Method 1: schedule(Callable)
 * ─────────────────────────────
 *   <V> ScheduledFuture<V> schedule(
 *       Callable<V> callable,
 *       long delay,
 *       TimeUnit unit
 *   )
 *
 * - Executes task ONCE after the specified delay
 * - Returns ScheduledFuture<V> with the result
 * - Task returns a value
 *
 * EXAMPLE:
 *   ScheduledFuture<Integer> future = scheduler.schedule(
 *       () -> 42,
 *       5,
 *       TimeUnit.SECONDS
 *   );
 *   // Task executes once, 5 seconds from now, returns 42
 *
 *
 * Method 2: schedule(Runnable)
 * ────────────────────────────
 *   ScheduledFuture<?> schedule(
 *       Runnable command,
 *       long delay,
 *       TimeUnit unit
 *   )
 *
 * - Executes task ONCE after the specified delay
 * - Returns ScheduledFuture<?> (will return null)
 * - Task does NOT return a value
 *
 * EXAMPLE:
 *   ScheduledFuture<?> future = scheduler.schedule(
 *       () -> System.out.println("Task"),
 *       10,
 *       TimeUnit.SECONDS
 *   );
 *   // Task executes once, 10 seconds from now
 *
 *
 * Method 3: scheduleAtFixedRate
 * ─────────────────────────────
 *   ScheduledFuture<?> scheduleAtFixedRate(
 *       Runnable command,
 *       long initialDelay,
 *       long period,
 *       TimeUnit unit
 *   )
 *
 * - Executes task REPEATEDLY at fixed time intervals
 * - First execution after initialDelay
 * - Subsequent executions every period (measured from START of previous execution)
 * - If task takes longer than period, next execution starts immediately after
 * - Does NOT account for task execution time
 *
 * MEMORY AID: "Fixed RATE" = fixed TIME between START times
 *
 * EXAMPLE:
 *   scheduler.scheduleAtFixedRate(
 *       () -> System.out.println("Tick"),
 *       0,        // Start immediately
 *       1,        // Every 1 second from start of previous execution
 *       TimeUnit.SECONDS
 *   );
 *
 *
 * Method 4: scheduleWithFixedDelay
 * ────────────────────────────────
 *   ScheduledFuture<?> scheduleWithFixedDelay(
 *       Runnable command,
 *       long initialDelay,
 *       long delay,
 *       TimeUnit unit
 *   )
 *
 * - Executes task REPEATEDLY with fixed delay between executions
 * - First execution after initialDelay
 * - Delay measured from END of previous execution to START of next
 * - Accounts for task execution time - guarantees delay BETWEEN executions
 *
 * MEMORY AID: "Fixed DELAY" = fixed TIME between END and next START
 *
 * EXAMPLE:
 *   scheduler.scheduleWithFixedDelay(
 *       () -> System.out.println("Tick"),
 *       0,        // Start immediately
 *       1,        // 1 second after previous execution COMPLETES
 *       TimeUnit.SECONDS
 *   );
 *
 *
 * ============================================================================
 * scheduleAtFixedRate vs scheduleWithFixedDelay - CRITICAL DIFFERENCE!
 * ============================================================================
 *
 * This is a common exam question and source of confusion!
 *
 * scheduleAtFixedRate(task, 0, 1, SECONDS)
 * ────────────────────────────────────────
 * Period measured from START to START of executions
 *
 *   Time (seconds):  0    1    2    3    4    5    6
 *   Task 1:         [--]
 *   Task 2:              [--]
 *   Task 3:                   [--]
 *   Task 4:                        [--]
 *
 * If task takes 0.5s, next task starts at 1s, 2s, 3s...
 * Fixed 1-second RATE regardless of execution time.
 *
 * If task takes longer than period (e.g., 1.5s):
 *   Time (seconds):  0    1    2    3    4
 *   Task 1:         [------]
 *   Task 2:                 [------]  (starts immediately after Task 1)
 *   Task 3:                          [------]
 *
 * Next task starts IMMEDIATELY after previous completes if period expired.
 *
 *
 * scheduleWithFixedDelay(task, 0, 1, SECONDS)
 * ───────────────────────────────────────────
 * Delay measured from END to START of executions
 *
 *   Time (seconds):  0    1    2    3    4    5    6
 *   Task 1:         [--]
 *                       |<- 1s delay ->|
 *   Task 2:                           [--]
 *                                         |<- 1s delay ->|
 *   Task 3:                                             [--]
 *
 * If task takes 0.5s, there's 1s delay after completion, so next starts at 1.5s, 3s, 4.5s...
 * Fixed 1-second DELAY between end and next start.
 *
 * If task takes longer (e.g., 1.5s):
 *   Time (seconds):  0    1    2    3    4    5    6
 *   Task 1:         [------]
 *                           |<-- 1s delay -->|
 *   Task 2:                                  [------]
 *                                                    |<-- 1s delay -->|
 *   Task 3:                                                          [------]
 *
 * Always 1-second delay AFTER task completes before next starts.
 *
 *
 * COMPARISON TABLE:
 * ┌──────────────────────┬─────────────────────┬────────────────────────┐
 * │ Aspect               │ scheduleAtFixedRate │ scheduleWithFixedDelay │
 * ├──────────────────────┼─────────────────────┼────────────────────────┤
 * │ Period/Delay         │ From START to START │ From END to START      │
 * │ measured             │                     │                        │
 * ├──────────────────────┼─────────────────────┼────────────────────────┤
 * │ If task takes longer │ Next starts         │ Always waits full      │
 * │ than period          │ immediately         │ delay after completion │
 * ├──────────────────────┼─────────────────────┼────────────────────────┤
 * │ Accounts for task    │ NO                  │ YES                    │
 * │ execution time       │                     │                        │
 * ├──────────────────────┼─────────────────────┼────────────────────────┤
 * │ Use case             │ Fixed-rate tasks    │ Tasks with variable    │
 * │                      │ (monitoring)        │ duration (polling)     │
 * └──────────────────────┴─────────────────────┴────────────────────────┘
 *
 *
 * EXAM TRICK QUESTION EXAMPLE:
 *
 * Task takes 500ms. scheduleAtFixedRate with period=1s.
 * When do executions occur?
 *   Answer: 0s, 1s, 2s, 3s... (fixed rate from start)
 *
 * Task takes 500ms. scheduleWithFixedDelay with delay=1s.
 * When do executions occur?
 *   Answer: 0s, 1.5s, 3s, 4.5s... (1s after each completion)
 *
 * Task takes 2s. scheduleAtFixedRate with period=1s.
 * When do executions occur?
 *   Answer: 0s, 2s, 4s, 6s... (starts immediately after each completes)
 *
 *
 * ============================================================================
 * METHOD SIGNATURE SUMMARY (MEMORIZE!)
 * ============================================================================
 *
 * ONE-TIME EXECUTION (schedule):
 *   <V> ScheduledFuture<V> schedule(Callable<V>, long delay, TimeUnit)
 *   ScheduledFuture<?>     schedule(Runnable,    long delay, TimeUnit)
 *
 * REPEATED EXECUTION:
 *   ScheduledFuture<?> scheduleAtFixedRate(
 *       Runnable, long initialDelay, long period, TimeUnit
 *   )
 *   ScheduledFuture<?> scheduleWithFixedDelay(
 *       Runnable, long initialDelay, long delay, TimeUnit
 *   )
 *
 * NOTES:
 * - All scheduled methods return ScheduledFuture (extends Future)
 * - Repeated execution methods only accept Runnable (not Callable)
 * - Repeated execution always returns ScheduledFuture<?>
 *
 *
 * ============================================================================
 * THREAD POOL FACTORY METHODS - EXECUTORS CLASS
 * ============================================================================
 *
 * The Executors class provides static factory methods for common thread pools.
 * Know when to use each one!
 *
 *
 * Method 1: newSingleThreadExecutor()
 * ───────────────────────────────────
 *   static ExecutorService newSingleThreadExecutor()
 *
 * CHARACTERISTICS:
 * - Creates executor with SINGLE worker thread
 * - Tasks execute sequentially in the order submitted
 * - If thread dies, a new one is created to replace it
 * - Unbounded queue for waiting tasks
 *
 * USE CASE:
 * - Need to ensure tasks execute one at a time
 * - Order of execution matters
 * - Example: Processing transactions sequentially
 *
 * EXAMPLE:
 *   ExecutorService executor = Executors.newSingleThreadExecutor();
 *   executor.submit(() -> System.out.println("Task 1"));
 *   executor.submit(() -> System.out.println("Task 2"));
 *   // Guaranteed: Task 1 completes before Task 2 starts
 *
 *
 * Method 2: newSingleThreadScheduledExecutor()
 * ────────────────────────────────────────────
 *   static ScheduledExecutorService newSingleThreadScheduledExecutor()
 *
 * CHARACTERISTICS:
 * - Single-threaded scheduled executor
 * - Can schedule tasks with delays and repetition
 * - Tasks execute sequentially
 * - If thread dies, replaced with new one
 *
 * USE CASE:
 * - Need scheduled/repeated tasks
 * - Want sequential execution (one at a time)
 * - Example: Periodic cleanup task
 *
 * EXAMPLE:
 *   ScheduledExecutorService scheduler =
 *       Executors.newSingleThreadScheduledExecutor();
 *   scheduler.scheduleAtFixedRate(
 *       () -> System.out.println("Cleanup"),
 *       0, 10, TimeUnit.MINUTES
 *   );
 *
 *
 * Method 3: newCachedThreadPool()
 * ───────────────────────────────
 *   static ExecutorService newCachedThreadPool()
 *
 * CHARACTERISTICS:
 * - Creates new threads as needed
 * - Reuses previously created threads when available
 * - Threads idle for 60 seconds are terminated and removed
 * - No limit on number of threads (can grow unbounded!)
 * - NO task queue (tasks execute immediately or create new thread)
 *
 * USE CASE:
 * - Many short-lived asynchronous tasks
 * - Task arrival rate varies
 * - Want to avoid thread creation overhead when possible
 * - WARNING: Can create too many threads if tasks are long-running!
 *
 * EXAMPLE:
 *   ExecutorService executor = Executors.newCachedThreadPool();
 *   for (int i = 0; i < 100; i++) {
 *       executor.submit(() -> doQuickTask());  // Creates threads as needed
 *   }
 *
 *
 * Method 4: newFixedThreadPool(int nThreads)
 * ──────────────────────────────────────────
 *   static ExecutorService newFixedThreadPool(int nThreads)
 *
 * CHARACTERISTICS:
 * - Creates pool with FIXED number of threads (nThreads)
 * - Threads exist for lifetime of executor
 * - If thread dies due to exception, replaced with new one
 * - Unbounded queue for waiting tasks
 * - Tasks wait in queue if all threads busy
 *
 * USE CASE:
 * - Know optimal number of threads for workload
 * - Want to limit resource usage
 * - Long-running tasks
 * - Example: Process requests with max 10 concurrent threads
 *
 * EXAMPLE:
 *   ExecutorService executor = Executors.newFixedThreadPool(10);
 *   for (int i = 0; i < 100; i++) {
 *       executor.submit(() -> processRequest());  // Max 10 concurrent
 *   }
 *
 *
 * Method 5: newScheduledThreadPool(int corePoolSize)
 * ──────────────────────────────────────────────────
 *   static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
 *
 * CHARACTERISTICS:
 * - Fixed number of threads that can handle scheduled tasks
 * - Can schedule tasks with delays and repetition
 * - Multiple tasks can execute concurrently (unlike single-threaded version)
 * - If thread dies, replaced with new one
 *
 * USE CASE:
 * - Need scheduled/repeated tasks
 * - Want concurrent execution of scheduled tasks
 * - Example: Multiple periodic monitoring tasks
 *
 * EXAMPLE:
 *   ScheduledExecutorService scheduler =
 *       Executors.newScheduledThreadPool(5);
 *   scheduler.scheduleAtFixedRate(() -> monitorCPU(), 0, 1, TimeUnit.SECONDS);
 *   scheduler.scheduleAtFixedRate(() -> monitorMemory(), 0, 1, TimeUnit.SECONDS);
 *   // Both can run concurrently
 *
 *
 * ============================================================================
 * THREAD POOL COMPARISON TABLE
 * ============================================================================
 *
 * ┌────────────────────────────┬────────────┬────────────┬───────────────┐
 * │ Factory Method             │ # Threads  │ Scheduling │ Queue         │
 * ├────────────────────────────┼────────────┼────────────┼───────────────┤
 * │ newSingleThreadExecutor    │ 1          │ NO         │ Unbounded     │
 * ├────────────────────────────┼────────────┼────────────┼───────────────┤
 * │ newSingleThread            │ 1          │ YES        │ Unbounded     │
 * │ ScheduledExecutor          │            │            │               │
 * ├────────────────────────────┼────────────┼────────────┼───────────────┤
 * │ newCachedThreadPool        │ 0 to ∞     │ NO         │ None (direct) │
 * ├────────────────────────────┼────────────┼────────────┼───────────────┤
 * │ newFixedThreadPool(n)      │ n (fixed)  │ NO         │ Unbounded     │
 * ├────────────────────────────┼────────────┼────────────┼───────────────┤
 * │ newScheduledThreadPool(n)  │ n (fixed)  │ YES        │ Unbounded     │
 * └────────────────────────────┴────────────┴────────────┴───────────────┘
 *
 *
 * CHOOSING THE RIGHT POOL:
 * ┌────────────────────────────────────┬─────────────────────────────────┐
 * │ Requirement                        │ Use                             │
 * ├────────────────────────────────────┼─────────────────────────────────┤
 * │ Tasks must execute sequentially    │ newSingleThreadExecutor         │
 * ├────────────────────────────────────┼─────────────────────────────────┤
 * │ Schedule tasks, sequential         │ newSingleThreadScheduledExecutor│
 * ├────────────────────────────────────┼─────────────────────────────────┤
 * │ Many short-lived tasks             │ newCachedThreadPool             │
 * ├────────────────────────────────────┼─────────────────────────────────┤
 * │ Limit max concurrent threads       │ newFixedThreadPool(n)           │
 * ├────────────────────────────────────┼─────────────────────────────────┤
 * │ Schedule tasks, concurrent         │ newScheduledThreadPool(n)       │
 * └────────────────────────────────────┴─────────────────────────────────┘
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class ScheduledExecutorsAndPools {

    /**
     * Demonstrates schedule(Callable) - one-time execution
     */
    public static void demonstrateScheduleCallable() throws Exception {
        System.out.println("=== schedule(Callable) ===");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        try {
            System.out.println("Scheduling task for 2 seconds from now...");

            ScheduledFuture<String> future = scheduler.schedule(
                () -> {
                    System.out.println("Task executing!");
                    return "Result from scheduled task";
                },
                2,
                TimeUnit.SECONDS
            );

            System.out.println("Waiting for result...");
            String result = future.get();
            System.out.println("Got result: " + result);

        } finally {
            scheduler.shutdown();
        }
    }

    /**
     * Demonstrates schedule(Runnable) - one-time execution
     */
    public static void demonstrateScheduleRunnable() throws Exception {
        System.out.println("\n=== schedule(Runnable) ===");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        try {
            System.out.println("Scheduling task for 1 second from now...");

            ScheduledFuture<?> future = scheduler.schedule(
                () -> System.out.println("Runnable task executing!"),
                1,
                TimeUnit.SECONDS
            );

            Object result = future.get();  // Will be null for Runnable
            System.out.println("Result: " + result);

        } finally {
            scheduler.shutdown();
        }
    }

    /**
     * Demonstrates scheduleAtFixedRate
     */
    public static void demonstrateScheduleAtFixedRate() throws InterruptedException {
        System.out.println("\n=== scheduleAtFixedRate ===");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        try {
            System.out.println("Starting fixed-rate task (every 1 second)...");

            scheduler.scheduleAtFixedRate(
                () -> {
                    long time = System.currentTimeMillis() % 100000;
                    System.out.println("  Tick at " + time + "ms");
                },
                0,        // Initial delay
                1,        // Period (from start to start)
                TimeUnit.SECONDS
            );

            Thread.sleep(5000);  // Let it run for 5 seconds
            System.out.println("Shutting down...");

        } finally {
            scheduler.shutdownNow();
        }
    }

    /**
     * Demonstrates scheduleWithFixedDelay
     */
    public static void demonstrateScheduleWithFixedDelay() throws InterruptedException {
        System.out.println("\n=== scheduleWithFixedDelay ===");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        try {
            System.out.println("Starting fixed-delay task (1 second after completion)...");

            scheduler.scheduleWithFixedDelay(
                () -> {
                    long time = System.currentTimeMillis() % 100000;
                    System.out.println("  Task starting at " + time + "ms");
                    try {
                        Thread.sleep(500);  // Task takes 500ms
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("  Task completed");
                },
                0,        // Initial delay
                1,        // Delay (from end to start)
                TimeUnit.SECONDS
            );

            Thread.sleep(5000);  // Let it run for 5 seconds
            System.out.println("Shutting down...");

        } finally {
            scheduler.shutdownNow();
        }
    }

    /**
     * Demonstrates difference between fixed rate and fixed delay
     */
    public static void demonstrateRateVsDelay() throws InterruptedException {
        System.out.println("\n=== Fixed Rate vs Fixed Delay ===");
        System.out.println("Task takes 500ms, period/delay = 1 second\n");

        // Fixed Rate
        System.out.println("Fixed Rate (start-to-start):");
        ScheduledExecutorService rate = Executors.newSingleThreadScheduledExecutor();
        long startTimeRate = System.currentTimeMillis();

        rate.scheduleAtFixedRate(() -> {
            long elapsed = System.currentTimeMillis() - startTimeRate;
            System.out.println("  Rate: Start at " + elapsed + "ms");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 0, 1, TimeUnit.SECONDS);

        Thread.sleep(3500);
        rate.shutdownNow();

        Thread.sleep(500);

        // Fixed Delay
        System.out.println("\nFixed Delay (end-to-start):");
        ScheduledExecutorService delay = Executors.newSingleThreadScheduledExecutor();
        long startTimeDelay = System.currentTimeMillis();

        delay.scheduleWithFixedDelay(() -> {
            long elapsed = System.currentTimeMillis() - startTimeDelay;
            System.out.println("  Delay: Start at " + elapsed + "ms");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 0, 1, TimeUnit.SECONDS);

        Thread.sleep(5000);
        delay.shutdownNow();
    }

    /**
     * Demonstrates newSingleThreadExecutor
     */
    public static void demonstrateSingleThreadExecutor() throws InterruptedException {
        System.out.println("\n=== newSingleThreadExecutor ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            for (int i = 1; i <= 3; i++) {
                int taskNum = i;
                executor.submit(() -> {
                    System.out.println("Task " + taskNum + " executing in: " +
                                     Thread.currentThread().getName());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            Thread.sleep(2000);  // Wait for completion
            System.out.println("All tasks executed sequentially");

        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates newCachedThreadPool
     */
    public static void demonstrateCachedThreadPool() throws InterruptedException {
        System.out.println("\n=== newCachedThreadPool ===");

        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            for (int i = 1; i <= 5; i++) {
                int taskNum = i;
                executor.submit(() -> {
                    System.out.println("Task " + taskNum + " in: " +
                                     Thread.currentThread().getName());
                });
            }

            Thread.sleep(1000);
            System.out.println("Tasks likely executed concurrently (different threads)");

        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates newFixedThreadPool
     */
    public static void demonstrateFixedThreadPool() throws InterruptedException {
        System.out.println("\n=== newFixedThreadPool(3) ===");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            for (int i = 1; i <= 6; i++) {
                int taskNum = i;
                executor.submit(() -> {
                    System.out.println("Task " + taskNum + " in: " +
                                     Thread.currentThread().getName());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            Thread.sleep(2000);
            System.out.println("Max 3 tasks executed concurrently");

        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates newScheduledThreadPool
     */
    public static void demonstrateScheduledThreadPool() throws InterruptedException {
        System.out.println("\n=== newScheduledThreadPool(2) ===");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        try {
            // Two periodic tasks can run concurrently
            scheduler.scheduleAtFixedRate(
                () -> System.out.println("Task 1 in " + Thread.currentThread().getName()),
                0, 1, TimeUnit.SECONDS
            );

            scheduler.scheduleAtFixedRate(
                () -> System.out.println("Task 2 in " + Thread.currentThread().getName()),
                0, 1, TimeUnit.SECONDS
            );

            Thread.sleep(3000);
            System.out.println("Both tasks executed concurrently");

        } finally {
            scheduler.shutdownNow();
        }
    }

    public static void main(String[] args) throws Exception {
        demonstrateScheduleCallable();
        Thread.sleep(500);

        demonstrateScheduleRunnable();
        Thread.sleep(500);

        demonstrateScheduleAtFixedRate();
        Thread.sleep(500);

        demonstrateScheduleWithFixedDelay();
        Thread.sleep(500);

        demonstrateRateVsDelay();
        Thread.sleep(500);

        demonstrateSingleThreadExecutor();
        Thread.sleep(500);

        demonstrateCachedThreadPool();
        Thread.sleep(500);

        demonstrateFixedThreadPool();
        Thread.sleep(500);

        demonstrateScheduledThreadPool();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - SCHEDULED EXECUTORS AND THREAD POOLS
 * ============================================================================
 *
 * SCHEDULEDEXECUTORSERVICE METHODS (memorize signatures!):
 *
 * ONE-TIME EXECUTION:
 * - <V> ScheduledFuture<V> schedule(Callable<V>, long delay, TimeUnit)
 * - ScheduledFuture<?> schedule(Runnable, long delay, TimeUnit)
 *
 * REPEATED EXECUTION:
 * - ScheduledFuture<?> scheduleAtFixedRate(
 *       Runnable, long initialDelay, long period, TimeUnit)
 * - ScheduledFuture<?> scheduleWithFixedDelay(
 *       Runnable, long initialDelay, long delay, TimeUnit)
 *
 * KEY DIFFERENCE:
 * - scheduleAtFixedRate: Period from START to START of executions
 *   • If task takes longer than period, next starts immediately
 *   • Use for fixed-rate monitoring
 *
 * - scheduleWithFixedDelay: Delay from END to START of executions
 *   • Always waits full delay after task completes
 *   • Use for tasks with variable duration
 *
 * THREAD POOL FACTORY METHODS (know them all!):
 *
 * 1. newSingleThreadExecutor()
 *    - 1 thread, sequential execution, unbounded queue
 *
 * 2. newSingleThreadScheduledExecutor()
 *    - 1 thread, can schedule tasks, sequential
 *
 * 3. newCachedThreadPool()
 *    - 0 to unlimited threads, reuses idle threads, no queue
 *    - WARNING: Can create too many threads!
 *
 * 4. newFixedThreadPool(int n)
 *    - n threads (fixed), unbounded queue
 *    - Limits concurrent execution
 *
 * 5. newScheduledThreadPool(int n)
 *    - n threads (fixed), can schedule tasks, concurrent execution
 *
 * CHOOSING A POOL:
 * - Sequential execution → newSingleThreadExecutor
 * - Sequential + scheduling → newSingleThreadScheduledExecutor
 * - Many short tasks → newCachedThreadPool
 * - Limit concurrency → newFixedThreadPool(n)
 * - Concurrent scheduling → newScheduledThreadPool(n)
 */
