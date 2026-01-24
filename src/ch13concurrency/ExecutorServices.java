package ch13concurrency;

import java.util.*;
import java.util.concurrent.*;

/**
 * EXECUTOR SERVICE - CONCURRENCY API
 * ===================================
 *
 * The Concurrency API provides ExecutorService for professional thread management.
 * PREFER ExecutorService over manually creating threads!
 *
 *
 * ============================================================================
 * WHY USE EXECUTORSERVICE?
 * ============================================================================
 *
 * PROBLEMS WITH MANUAL THREAD CREATION:
 * - No thread reuse - creates new thread for each task (expensive)
 * - No task queue management
 * - No easy way to get results from tasks
 * - Hard to control thread lifecycle
 * - No built-in thread pool management
 *
 * EXECUTORSERVICE BENEFITS:
 * - Thread reuse via thread pools
 * - Task queue management
 * - Future objects for task results
 * - Clean shutdown mechanisms
 * - Factory methods for common configurations
 *
 *
 * ============================================================================
 * EXECUTORSERVICE INTERFACE
 * ============================================================================
 *
 * ExecutorService defines services that create and manage threads.
 *
 * HIERARCHY:
 *   Executor
 *      ↑
 *      |
 *   ExecutorService
 *      ↑
 *      |
 *   ScheduledExecutorService
 *
 * KEY METHODS:
 * - execute(Runnable)
 * - submit(Runnable), submit(Callable)
 * - invokeAll(...), invokeAny(...)
 * - shutdown(), shutdownNow()
 * - isShutdown(), isTerminated()
 * - awaitTermination(...)
 *
 *
 * ============================================================================
 * CRITICAL: SHUTDOWN IS REQUIRED!
 * ============================================================================
 *
 * EXAM WARNING: ExecutorService creates NON-DAEMON threads on first task.
 * Failing to call shutdown() means the application NEVER TERMINATES!
 *
 * Example of the problem:
 *
 *   ExecutorService service = Executors.newSingleThreadExecutor();
 *   service.execute(() -> System.out.println("Task"));
 *   // If shutdown() not called, JVM never exits!
 *
 * ALWAYS call shutdown():
 *
 *   ExecutorService service = Executors.newSingleThreadExecutor();
 *   try {
 *       service.execute(() -> System.out.println("Task"));
 *   } finally {
 *       service.shutdown();  // REQUIRED!
 *   }
 *
 *
 * ============================================================================
 * SHUTDOWN METHODS
 * ============================================================================
 *
 * shutdown()
 * ──────────
 * - Initiates orderly shutdown
 * - REJECTS new tasks (throws RejectedExecutionException)
 * - CONTINUES executing previously submitted tasks
 * - Does NOT wait for tasks to complete (non-blocking)
 *
 *
 * shutdownNow()
 * ─────────────
 * - Attempts to stop all actively executing tasks
 * - REJECTS new tasks
 * - HALTS waiting tasks (does not start them)
 * - Returns List<Runnable> of tasks that were waiting
 * - Does NOT guarantee that running tasks will stop
 * - Uses thread interruption to cancel tasks
 *
 *
 * isShutdown()
 * ────────────
 * - Returns true if shutdown() or shutdownNow() was called
 * - Does NOT mean tasks are finished!
 * - Just means shutdown was initiated
 *
 *
 * isTerminated()
 * ──────────────
 * - Returns true if shutdown was initiated AND all tasks completed
 * - Only true after shutdown AND all work is done
 *
 *
 * SHUTDOWN LIFECYCLE:
 * ┌─────────────────┬──────────────┬─────────────────┬────────────────────┐
 * │ State           │ isShutdown() │ isTerminated()  │ Accepts New Tasks? │
 * ├─────────────────┼──────────────┼─────────────────┼────────────────────┤
 * │ Running         │ false        │ false           │ YES                │
 * │ Shutdown called │ true         │ false           │ NO                 │
 * │ All tasks done  │ true         │ true            │ NO                 │
 * └─────────────────┴──────────────┴─────────────────┴────────────────────┘
 *
 *
 * ============================================================================
 * SUBMITTING TASKS - METHOD SIGNATURES
 * ============================================================================
 *
 * execute() - Fire and Forget
 * ────────────────────────────
 * From Executor interface (parent of ExecutorService)
 *
 *   void execute(Runnable command)
 *
 * - Executes task asynchronously
 * - NO return value
 * - NO way to check if task completed
 * - Exceptions are silently swallowed (printed to console)
 * - Use when you don't care about result
 *
 *
 * submit() - With Future Return
 * ──────────────────────────────
 * Three overloaded versions:
 *
 *   Future<?> submit(Runnable task)
 *   - Returns Future<?> that will return null when done
 *   - Use isDone() to check completion
 *
 *   <T> Future<T> submit(Runnable task, T result)
 *   - Returns Future<T> that will return the provided result when done
 *   - Useful for tracking which task completed
 *
 *   <T> Future<T> submit(Callable<T> task)
 *   - Returns Future<T> with the computed result
 *   - Most common version for tasks that return values
 *
 *
 * COMPARISON:
 * ┌──────────────────────────────────┬─────────────┬───────────────────────┐
 * │ Method                           │ Return Type │ Use Case              │
 * ├──────────────────────────────────┼─────────────┼───────────────────────┤
 * │ execute(Runnable)                │ void        │ Fire and forget       │
 * │ submit(Runnable)                 │ Future<?>   │ Track completion      │
 * │ submit(Runnable, T)              │ Future<T>   │ Track with identifier │
 * │ submit(Callable<T>)              │ Future<T>   │ Get computed result   │
 * └──────────────────────────────────┴─────────────┴───────────────────────┘
 *
 *
 * ============================================================================
 * CALLABLE INTERFACE (KNOW BY HEART!)
 * ============================================================================
 *
 * Similar to Runnable but returns a value and can throw checked exceptions.
 *
 * DEFINITION:
 *
 *   @FunctionalInterface
 *   public interface Callable<V> {
 *       V call() throws Exception;
 *   }
 *
 * KEY DIFFERENCES FROM RUNNABLE:
 *
 * ┌────────────────┬──────────────────┬─────────────────────────────────┐
 * │ Interface      │ Method           │ Characteristics                 │
 * ├────────────────┼──────────────────┼─────────────────────────────────┤
 * │ Runnable       │ void run()       │ No return value                 │
 * │                │                  │ Cannot throw checked exceptions │
 * ├────────────────┼──────────────────┼─────────────────────────────────┤
 * │ Callable<V>    │ V call()         │ Returns value of type V         │
 * │                │                  │ Can throw checked Exception     │
 * └────────────────┴──────────────────┴─────────────────────────────────┘
 *
 * EXAMPLE LAMBDA:
 *   Callable<Integer> task = () -> 42;
 *   Callable<String> task2 = () -> "result";
 *
 *
 * ============================================================================
 * FUTURE INTERFACE - METHODS TO KNOW
 * ============================================================================
 *
 * Future<V> represents the result of an asynchronous computation.
 *
 * isDone()
 * ────────
 *   boolean isDone()
 *
 * - Returns true if task completed (normally, exception, or cancellation)
 * - Non-blocking check
 * - Does NOT tell you if task succeeded or failed
 *
 *
 * isCancelled()
 * ─────────────
 *   boolean isCancelled()
 *
 * - Returns true if task was cancelled before completion
 * - If true, isDone() will also be true
 *
 *
 * cancel()
 * ────────
 *   boolean cancel(boolean mayInterruptIfRunning)
 *
 * - Attempts to cancel the task
 * - Parameter:
 *   • true  = interrupt thread if task is running
 *   • false = don't interrupt if task already started
 * - Returns:
 *   • true if task was successfully cancelled
 *   • false if task already completed or couldn't be cancelled
 * - After successful cancel, get() throws CancellationException
 *
 *
 * get() - Two Signatures
 * ──────────────────────
 *   V get() throws InterruptedException, ExecutionException
 *
 * - BLOCKS indefinitely until task completes
 * - Returns the computed result
 * - Throws ExecutionException if task threw exception
 * - Throws InterruptedException if waiting thread interrupted
 * - Throws CancellationException if task was cancelled
 *
 *   V get(long timeout, TimeUnit unit)
 *       throws InterruptedException, ExecutionException, TimeoutException
 *
 * - BLOCKS up to specified timeout
 * - Returns result if available within timeout
 * - Throws TimeoutException if timeout expires
 * - Also throws InterruptedException, ExecutionException, CancellationException
 *
 *
 * ============================================================================
 * TIMEUNIT ENUM (KNOW THE VALUES!)
 * ============================================================================
 *
 * Used throughout Concurrency API for time-based operations.
 *
 * VALUES (from smallest to largest):
 *
 *   TimeUnit.NANOSECONDS   - 1/1,000,000,000 second
 *   TimeUnit.MICROSECONDS  - 1/1,000,000 second
 *   TimeUnit.MILLISECONDS  - 1/1,000 second
 *   TimeUnit.SECONDS       - 1 second
 *   TimeUnit.MINUTES       - 60 seconds
 *   TimeUnit.HOURS         - 60 minutes
 *   TimeUnit.DAYS          - 24 hours
 *
 * MEMORY AID: Nano, Micro, Milli, Seconds, Minutes, Hours, Days
 *
 * COMMON USAGE:
 *   future.get(5, TimeUnit.SECONDS);
 *   executor.awaitTermination(1, TimeUnit.MINUTES);
 *   Thread.sleep(100);  // Note: sleep takes milliseconds, NOT TimeUnit!
 *
 *
 * ============================================================================
 * INVOKEALL - EXECUTE MULTIPLE TASKS
 * ============================================================================
 *
 * Executes all tasks and returns Futures when ALL complete.
 *
 * SIGNATURES:
 *
 *   <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
 *       throws InterruptedException
 *
 *   <T> List<Future<T>> invokeAll(
 *       Collection<? extends Callable<T>> tasks,
 *       long timeout,
 *       TimeUnit unit
 *   ) throws InterruptedException
 *
 * BEHAVIOR:
 * - Executes all tasks
 * - BLOCKS until ALL tasks complete (or timeout)
 * - Returns List<Future<T>> in same order as input collection
 * - All Futures will be done (isDone() returns true)
 * - With timeout: incomplete tasks are cancelled
 *
 *
 * ============================================================================
 * INVOKEANY - EXECUTE UNTIL ONE SUCCEEDS
 * ============================================================================
 *
 * Executes tasks and returns result of FIRST successful completion.
 *
 * SIGNATURES:
 *
 *   <T> T invokeAny(Collection<? extends Callable<T>> tasks)
 *       throws InterruptedException, ExecutionException
 *
 *   <T> T invokeAny(
 *       Collection<? extends Callable<T>> tasks,
 *       long timeout,
 *       TimeUnit unit
 *   ) throws InterruptedException, ExecutionException, TimeoutException
 *
 * BEHAVIOR:
 * - Executes all tasks concurrently
 * - BLOCKS until ONE task completes successfully
 * - Returns the result directly (NOT a Future!)
 * - Cancels remaining tasks once one succeeds
 * - If all tasks fail, throws ExecutionException
 * - With timeout: throws TimeoutException if none complete in time
 *
 *
 * INVOKEALL vs INVOKEANY:
 * ┌─────────────┬──────────────────────┬─────────────────────────────────┐
 * │ Method      │ Returns              │ Waits For                       │
 * ├─────────────┼──────────────────────┼─────────────────────────────────┤
 * │ invokeAll   │ List<Future<T>>      │ ALL tasks to complete           │
 * │ invokeAny   │ T (the result)       │ ONE task to complete            │
 * └─────────────┴──────────────────────┴─────────────────────────────────┘
 *
 *
 * ============================================================================
 * AWAITTERMINATION - WAIT FOR SHUTDOWN COMPLETION
 * ============================================================================
 *
 * Waits for executor to finish after shutdown.
 *
 * SIGNATURE:
 *
 *   boolean awaitTermination(long timeout, TimeUnit unit)
 *       throws InterruptedException
 *
 * BEHAVIOR:
 * - BLOCKS until one of:
 *   1. All tasks complete after shutdown
 *   2. Timeout expires
 *   3. Current thread is interrupted
 * - Returns true if executor terminated
 * - Returns false if timeout elapsed before termination
 * - Does NOT initiate shutdown (call shutdown() first!)
 *
 * TYPICAL USAGE PATTERN:
 *
 *   executor.shutdown();
 *   if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
 *       executor.shutdownNow();  // Force shutdown if timeout
 *   }
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class ExecutorServices {

    /**
     * Demonstrates shutdown lifecycle: isShutdown() and isTerminated()
     */
    public static void demonstrateShutdownLifecycle() throws InterruptedException {
        System.out.println("=== Shutdown Lifecycle ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        System.out.println("Initial - isShutdown: " + executor.isShutdown() +
                         ", isTerminated: " + executor.isTerminated());

        executor.submit(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task executing");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();  // Initiate shutdown
        System.out.println("After shutdown() - isShutdown: " + executor.isShutdown() +
                         ", isTerminated: " + executor.isTerminated());

        Thread.sleep(1500);  // Wait for task to complete
        System.out.println("After tasks complete - isShutdown: " + executor.isShutdown() +
                         ", isTerminated: " + executor.isTerminated());
    }

    /**
     * Demonstrates shutdownNow() returning waiting tasks
     */
    public static void demonstrateShutdownNow() throws InterruptedException {
        System.out.println("\n=== shutdownNow() ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit multiple tasks - only first will execute
        executor.submit(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Task interrupted!");
            }
        });
        executor.submit(() -> System.out.println("Task 2"));
        executor.submit(() -> System.out.println("Task 3"));

        Thread.sleep(100);  // Let first task start

        List<Runnable> waiting = executor.shutdownNow();
        System.out.println("Tasks waiting in queue: " + waiting.size());
    }

    /**
     * Demonstrates execute() vs submit()
     */
    public static void demonstrateExecuteVsSubmit() throws InterruptedException {
        System.out.println("\n=== execute() vs submit() ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            // execute() - no return value
            executor.execute(() -> System.out.println("execute() - fire and forget"));

            // submit(Runnable) - returns Future<?>
            Future<?> future1 = executor.submit(() ->
                System.out.println("submit(Runnable) - returns Future<?>")
            );

            // submit(Runnable, T) - returns Future<T> with provided result
            Future<String> future2 = executor.submit(
                () -> System.out.println("submit with result"),
                "Task identifier"
            );

            // submit(Callable) - returns Future<T> with computed result
            Future<Integer> future3 = executor.submit(() -> {
                System.out.println("submit(Callable) - returns computed result");
                return 42;
            });

            System.out.println("Future1 result: " + future1.get());  // null
            System.out.println("Future2 result: " + future2.get());  // "Task identifier"
            System.out.println("Future3 result: " + future3.get());  // 42

        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates Callable interface
     */
    public static void demonstrateCallable() throws InterruptedException {
        System.out.println("\n=== Callable Interface ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            // Callable that returns Integer
            Callable<Integer> calculateSum = () -> {
                int sum = 0;
                for (int i = 1; i <= 10; i++) {
                    sum += i;
                }
                return sum;
            };

            Future<Integer> future = executor.submit(calculateSum);
            System.out.println("Sum: " + future.get());

            // Callable that can throw checked exception
            Callable<String> riskyTask = () -> {
                if (Math.random() > 0.5) {
                    throw new Exception("Random failure");
                }
                return "Success";
            };

            Future<String> riskyFuture = executor.submit(riskyTask);
            try {
                System.out.println("Result: " + riskyFuture.get());
            } catch (ExecutionException e) {
                System.out.println("Task threw: " + e.getCause().getMessage());
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates Future methods: isDone, isCancelled, cancel, get
     */
    public static void demonstrateFutureMethods() throws InterruptedException {
        System.out.println("\n=== Future Methods ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<String> future = executor.submit(() -> {
                Thread.sleep(2000);
                return "Task completed";
            });

            System.out.println("isDone (immediate): " + future.isDone());
            System.out.println("isCancelled: " + future.isCancelled());

            // Try to get with timeout
            try {
                String result = future.get(500, TimeUnit.MILLISECONDS);
                System.out.println("Result: " + result);
            } catch (TimeoutException e) {
                System.out.println("Timeout occurred!");
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            System.out.println("isDone (after timeout): " + future.isDone());

            // Cancel the task
            boolean cancelled = future.cancel(true);  // Interrupt if running
            System.out.println("Cancel successful: " + cancelled);
            System.out.println("isCancelled: " + future.isCancelled());
            System.out.println("isDone (after cancel): " + future.isDone());

            // Try to get cancelled task
            try {
                future.get();
            } catch (CancellationException e) {
                System.out.println("Task was cancelled!");
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates TimeUnit enum values
     */
    public static void demonstrateTimeUnit() {
        System.out.println("\n=== TimeUnit Values ===");

        System.out.println("NANOSECONDS");
        System.out.println("MICROSECONDS");
        System.out.println("MILLISECONDS");
        System.out.println("SECONDS");
        System.out.println("MINUTES");
        System.out.println("HOURS");
        System.out.println("DAYS");

        // Converting between units
        long millis = TimeUnit.SECONDS.toMillis(5);
        System.out.println("\n5 seconds = " + millis + " milliseconds");

        long seconds = TimeUnit.MINUTES.toSeconds(2);
        System.out.println("2 minutes = " + seconds + " seconds");
    }

    /**
     * Demonstrates invokeAll()
     */
    public static void demonstrateInvokeAll() throws InterruptedException {
        System.out.println("\n=== invokeAll() ===");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            List<Callable<String>> tasks = Arrays.asList(
                () -> { Thread.sleep(1000); return "Task 1"; },
                () -> { Thread.sleep(500); return "Task 2"; },
                () -> { Thread.sleep(1500); return "Task 3"; }
            );

            System.out.println("Submitting all tasks...");
            List<Future<String>> futures = executor.invokeAll(tasks);

            System.out.println("All tasks completed!");
            for (int i = 0; i < futures.size(); i++) {
                System.out.println("Future " + i + " isDone: " + futures.get(i).isDone());
                System.out.println("Result: " + futures.get(i).get());
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates invokeAll() with timeout
     */
    public static void demonstrateInvokeAllTimeout() throws InterruptedException {
        System.out.println("\n=== invokeAll() with Timeout ===");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            List<Callable<String>> tasks = Arrays.asList(
                () -> { Thread.sleep(500); return "Fast task"; },
                () -> { Thread.sleep(5000); return "Slow task"; },  // Won't complete
                () -> { Thread.sleep(500); return "Another fast task"; }
            );

            System.out.println("Submitting with 1 second timeout...");
            List<Future<String>> futures = executor.invokeAll(tasks, 1, TimeUnit.SECONDS);

            for (int i = 0; i < futures.size(); i++) {
                Future<String> future = futures.get(i);
                System.out.println("Future " + i + " isDone: " + future.isDone() +
                                 ", isCancelled: " + future.isCancelled());
                if (future.isDone() && !future.isCancelled()) {
                    System.out.println("  Result: " + future.get());
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates invokeAny()
     */
    public static void demonstrateInvokeAny() throws InterruptedException {
        System.out.println("\n=== invokeAny() ===");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            List<Callable<String>> tasks = Arrays.asList(
                () -> { Thread.sleep(2000); return "Slow task"; },
                () -> { Thread.sleep(500); return "Fast task"; },  // This wins!
                () -> { Thread.sleep(1000); return "Medium task"; }
            );

            System.out.println("Waiting for first to complete...");
            String result = executor.invokeAny(tasks);  // Returns the result directly!
            System.out.println("First result: " + result);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Demonstrates awaitTermination()
     */
    public static void demonstrateAwaitTermination() throws InterruptedException {
        System.out.println("\n=== awaitTermination() ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Task completed");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();
        System.out.println("Shutdown initiated, waiting for completion...");

        boolean terminated = executor.awaitTermination(3, TimeUnit.SECONDS);
        if (terminated) {
            System.out.println("Executor terminated successfully");
        } else {
            System.out.println("Timeout - forcing shutdown");
            executor.shutdownNow();
        }
    }

    /**
     * Common pattern: Try-finally with shutdown
     */
    public static void demonstrateProperShutdownPattern() {
        System.out.println("\n=== Proper Shutdown Pattern ===");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            executor.submit(() -> System.out.println("Task executing"));
            // More tasks...
        } finally {
            executor.shutdown();  // ALWAYS in finally block!
        }
    }

    public static void main(String[] args) throws InterruptedException {
        demonstrateShutdownLifecycle();
        Thread.sleep(500);

        demonstrateShutdownNow();
        Thread.sleep(500);

        demonstrateExecuteVsSubmit();
        Thread.sleep(500);

        demonstrateCallable();
        Thread.sleep(500);

        demonstrateFutureMethods();
        Thread.sleep(500);

        demonstrateTimeUnit();

        demonstrateInvokeAll();
        Thread.sleep(500);

        demonstrateInvokeAllTimeout();
        Thread.sleep(500);

        demonstrateInvokeAny();
        Thread.sleep(500);

        demonstrateAwaitTermination();

        demonstrateProperShutdownPattern();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - EXECUTORSERVICE
 * ============================================================================
 *
 * WHY EXECUTORSERVICE:
 * - Professional thread management via Concurrency API
 * - Prefer over manually creating threads
 * - Thread reuse, task queues, result handling
 *
 * CRITICAL: SHUTDOWN REQUIRED!
 * - ExecutorService creates NON-DAEMON threads
 * - App never terminates if shutdown() not called
 * - Always call shutdown() in finally block
 *
 * SHUTDOWN METHODS:
 * - shutdown() - reject new tasks, finish submitted tasks (orderly)
 * - shutdownNow() - attempt to stop all tasks, return waiting tasks (aggressive)
 * - isShutdown() - true if shutdown initiated
 * - isTerminated() - true if shutdown AND all tasks complete
 *
 * SUBMITTING TASKS:
 * - execute(Runnable) - void, fire and forget
 * - submit(Runnable) - returns Future<?>
 * - submit(Runnable, T) - returns Future<T> with provided result
 * - submit(Callable<T>) - returns Future<T> with computed result
 *
 * CALLABLE INTERFACE (know by heart):
 * @FunctionalInterface
 * public interface Callable<V> {
 *     V call() throws Exception;
 * }
 * - Returns value (unlike Runnable)
 * - Can throw checked exceptions (unlike Runnable)
 *
 * FUTURE METHODS:
 * - boolean isDone() - true if completed (any way)
 * - boolean isCancelled() - true if cancelled
 * - boolean cancel(boolean mayInterrupt) - attempt to cancel
 * - V get() - block until complete, return result
 * - V get(long timeout, TimeUnit unit) - block with timeout
 *   Throws: InterruptedException, ExecutionException, TimeoutException, CancellationException
 *
 * TIMEUNIT VALUES (know them!):
 * NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS
 *
 * BATCH OPERATIONS:
 * - invokeAll(tasks) - wait for ALL, returns List<Future<T>>
 * - invokeAll(tasks, timeout, unit) - with timeout
 * - invokeAny(tasks) - wait for ONE, returns T (the result)
 * - invokeAny(tasks, timeout, unit) - with timeout
 *
 * WAIT FOR SHUTDOWN:
 * - awaitTermination(timeout, unit) - blocks until terminated or timeout
 *   Returns true if terminated, false if timeout
 *   Does NOT initiate shutdown - call shutdown() first!
 *
 * TYPICAL PATTERN:
 * ExecutorService executor = Executors.newSingleThreadExecutor();
 * try {
 *     Future<T> future = executor.submit(task);
 *     T result = future.get();
 * } finally {
 *     executor.shutdown();
 * }
 */
