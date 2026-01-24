package ch13concurrency;

/**
 * THREAD BASICS - CONCURRENCY FUNDAMENTALS
 * =========================================
 *
 * Core concepts and techniques for creating and managing threads in Java.
 *
 *
 * ============================================================================
 * CORE TERMINOLOGY
 * ============================================================================
 *
 * THREAD
 * - Smallest unit of execution that can be scheduled by the operating system
 * - A single sequential flow of control within a program
 * - Has its own call stack but shares memory with other threads
 *
 * PROCESS
 * - An instance of a running program
 * - Has its own memory space (isolated from other processes)
 * - Contains at least one thread (the main thread)
 *
 * MULTITHREADED PROCESS
 * - A process that has multiple threads executing concurrently
 * - All threads share the same memory space within the process
 * - Example: A web browser with separate threads for UI, network, rendering
 *
 * TASK
 * - A unit of work that can be executed by a thread
 * - Represents what the thread should do (the code to execute)
 * - In Java: implemented via Runnable or Callable
 *
 * SHARED MEMORY
 * - Memory space accessible by multiple threads
 * - Threads in the same process can read/write the same objects
 * - Requires synchronization to prevent race conditions
 *
 * CONCURRENCY
 * - Multiple tasks making progress at the same time
 * - Does NOT require multiple CPUs (can be time-slicing on single CPU)
 * - Example: One CPU switching between multiple threads
 *
 * THREAD SCHEDULER
 * - Part of the JVM/OS that determines which thread runs at any given time
 * - Uses thread priority and other factors to make scheduling decisions
 * - Behavior is NOT guaranteed to be deterministic
 *
 * CONTEXT SWITCH
 * - When the CPU switches from executing one thread to another
 * - Requires saving the state of the current thread and loading another
 * - Has performance overhead - too many switches can slow down execution
 *
 * THREAD PRIORITY
 * - A hint to the thread scheduler about relative importance
 * - Range: Thread.MIN_PRIORITY (1) to Thread.MAX_PRIORITY (10)
 * - Default: Thread.NORM_PRIORITY (5)
 * - EXAM NOTE: Priority is a HINT, not a guarantee
 *
 *
 * ============================================================================
 * RUNNABLE INTERFACE (KNOW BY HEART!)
 * ============================================================================
 *
 * The functional interface that defines a task to be executed by a thread.
 *
 * DEFINITION:
 *
 *   @FunctionalInterface
 *   public interface Runnable {
 *       void run();
 *   }
 *
 * KEY CHARACTERISTICS:
 * - Functional interface with single abstract method: run()
 * - run() takes NO parameters and returns void
 * - Does NOT throw checked exceptions (unlike Callable)
 * - Can be implemented as lambda: () -> { code }
 *
 *
 * ============================================================================
 * TWO WAYS TO CREATE A THREAD
 * ============================================================================
 *
 * METHOD 1: Provide Runnable to Thread Constructor
 * ─────────────────────────────────────────────────
 * PREFERRED APPROACH - Separates task from thread mechanism
 *
 * Using anonymous class:
 *   Thread thread = new Thread(new Runnable() {
 *       public void run() {
 *           System.out.println("Task executing");
 *       }
 *   });
 *
 * Using lambda (cleaner):
 *   Thread thread = new Thread(() -> System.out.println("Task executing"));
 *
 * Using method reference:
 *   Thread thread = new Thread(MyClass::myMethod);
 *
 *
 * METHOD 2: Extend Thread and Override run()
 * ───────────────────────────────────────────
 * LESS FLEXIBLE - Wastes inheritance if you don't need Thread-specific features
 *
 *   class MyThread extends Thread {
 *       @Override
 *       public void run() {
 *           System.out.println("Task executing");
 *       }
 *   }
 *
 *   Thread thread = new MyThread();
 *
 *
 * ============================================================================
 * run() vs start() - CRITICAL EXAM CONCEPT
 * ============================================================================
 *
 * start()  - Creates a NEW thread and calls run() asynchronously
 * run()    - Executes the task SYNCHRONOUSLY in the current thread
 *
 * EXAM TRICK: Calling run() directly does NOT create a new thread!
 *
 * Example demonstrating the difference:
 *
 *   Thread t = new Thread(() -> System.out.println("In thread: " +
 *                                Thread.currentThread().getName()));
 *
 *   t.start();  // Output: "In thread: Thread-0" (NEW thread created)
 *   t.run();    // Output: "In thread: main" (runs in CURRENT thread)
 *
 * BEHAVIOR:
 * ┌─────────────┬──────────────────────┬────────────────────────────────┐
 * │ Method      │ Creates New Thread?  │ Execution                      │
 * ├─────────────┼──────────────────────┼────────────────────────────────┤
 * │ start()     │ YES                  │ Asynchronous (separate thread) │
 * │ run()       │ NO                   │ Synchronous (current thread)   │
 * └─────────────┴──────────────────────┴────────────────────────────────┘
 *
 * IMPORTANT:
 * - Calling start() twice on the same Thread throws IllegalThreadStateException
 * - Calling run() multiple times is allowed (just a method call)
 *
 *
 * ============================================================================
 * THREAD TYPES
 * ============================================================================
 *
 * USER-DEFINED THREADS
 * - Threads explicitly created by application code
 * - Created via Thread constructor or ExecutorService
 * - Example: new Thread(() -> doWork()).start()
 *
 * SYSTEM THREADS
 * - Threads created by the JVM for internal operations
 * - Examples: Garbage collector, JIT compiler
 * - Application code typically doesn't interact with these
 *
 * DAEMON THREADS
 * - Background threads that don't prevent JVM from exiting
 * - JVM terminates when ONLY daemon threads remain
 * - Set via: thread.setDaemon(true) BEFORE calling start()
 *
 * DAEMON BEHAVIOR:
 *   Thread daemon = new Thread(() -> {
 *       while (true) { }  // runs forever
 *   });
 *   daemon.setDaemon(true);  // MUST be before start()
 *   daemon.start();
 *   // JVM can exit even though this thread is still running
 *
 * EXAM NOTES:
 * - setDaemon() must be called BEFORE start(), else IllegalThreadStateException
 * - Main thread is NOT a daemon thread
 * - Daemon status is inherited: threads created by daemon are also daemon
 *
 * ┌────────────────┬─────────────────────┬────────────────────────────┐
 * │ Type           │ Prevents JVM Exit?  │ Typical Use                │
 * ├────────────────┼─────────────────────┼────────────────────────────┤
 * │ User Thread    │ YES                 │ Application logic          │
 * │ Daemon Thread  │ NO                  │ Background services        │
 * └────────────────┴─────────────────────┴────────────────────────────┘
 *
 *
 * ============================================================================
 * THREAD STATES (6 STATES - KNOW THESE!)
 * ============================================================================
 *
 * Accessible via: Thread.getState() returns Thread.State enum
 *
 * 1. NEW
 * ──────
 * - Thread object created but start() not yet called
 * - Thread has been instantiated but not started
 *
 *   Thread t = new Thread(() -> {});  // State: NEW
 *
 *
 * 2. RUNNABLE
 * ───────────
 * - Thread is executing or ready to execute
 * - Includes both "ready" and "running" from OS perspective
 * - Thread may be waiting for CPU time from scheduler
 *
 *   t.start();  // State: RUNNABLE (or may already be running)
 *
 *
 * 3. BLOCKED
 * ──────────
 * - Thread is waiting to acquire a lock/monitor
 * - Trying to enter a synchronized block/method that's held by another thread
 * - Will transition to RUNNABLE when lock is acquired
 *
 *   synchronized(obj) {  // If lock held by another thread, state: BLOCKED
 *       // code
 *   }
 *
 *
 * 4. WAITING
 * ──────────
 * - Thread is waiting indefinitely for another thread to perform an action
 * - Common causes:
 *   • Object.wait() with no timeout
 *   • Thread.join() with no timeout
 *   • LockSupport.park()
 *
 *   obj.wait();     // State: WAITING (until notify/notifyAll)
 *   t.join();       // State: WAITING (until t terminates)
 *
 *
 * 5. TIMED_WAITING
 * ────────────────
 * - Thread is waiting for a specific amount of time
 * - Common causes:
 *   • Thread.sleep(millis)
 *   • Object.wait(timeout)
 *   • Thread.join(timeout)
 *   • LockSupport.parkNanos()
 *
 *   Thread.sleep(1000);     // State: TIMED_WAITING for 1 second
 *   obj.wait(500);          // State: TIMED_WAITING for 500ms
 *
 *
 * 6. TERMINATED
 * ─────────────
 * - Thread has completed execution
 * - run() method has finished (either normally or by exception)
 * - Cannot be restarted
 *
 *   // After run() completes, state: TERMINATED
 *
 *
 * STATE TRANSITIONS DIAGRAM:
 * ──────────────────────────
 *
 *     NEW
 *      |
 *      | start()
 *      ↓
 *   RUNNABLE ←──────────────────┐
 *      |                         |
 *      | (various waits)         | (notify/timeout/lock acquired)
 *      ↓                         |
 *   BLOCKED ─────────────────────┤
 *   WAITING ─────────────────────┤
 *   TIMED_WAITING ───────────────┘
 *      |
 *      | run() completes
 *      ↓
 *   TERMINATED
 *
 *
 * ============================================================================
 * THREAD INTERRUPTION
 * ============================================================================
 *
 * A cooperative mechanism to signal a thread that it should stop what it's doing.
 *
 * KEY METHODS:
 *
 *   thread.interrupt()             // Request thread to stop
 *   Thread.interrupted()           // Check and CLEAR interrupted flag (static)
 *   thread.isInterrupted()         // Check interrupted flag (does NOT clear)
 *
 *
 * INTERRUPT BEHAVIOR:
 *
 * If thread is in WAITING or TIMED_WAITING (sleep, wait, join):
 *   - Method throws InterruptedException
 *   - Interrupted flag is CLEARED when exception is thrown
 *
 * If thread is RUNNABLE (actively executing):
 *   - Interrupted flag is SET
 *   - Thread continues execution (must check flag manually)
 *
 *
 * EXAMPLE - Handling Interrupts:
 *
 *   Thread t = new Thread(() -> {
 *       try {
 *           while (!Thread.currentThread().isInterrupted()) {
 *               System.out.println("Working...");
 *               Thread.sleep(1000);  // May throw InterruptedException
 *           }
 *       } catch (InterruptedException e) {
 *           System.out.println("Interrupted during sleep");
 *           // Clean up and exit
 *       }
 *       System.out.println("Thread stopping gracefully");
 *   });
 *
 *   t.start();
 *   Thread.sleep(3000);
 *   t.interrupt();  // Signal thread to stop
 *
 *
 * EXAM NOTES:
 * - Interruption is COOPERATIVE - thread must check and respond
 * - InterruptedException is a checked exception - must handle
 * - Thread.interrupted() clears the flag (static method)
 * - isInterrupted() does NOT clear the flag (instance method)
 *
 * ┌──────────────────────────┬─────────────┬──────────────────────────┐
 * │ Method                   │ Clears Flag │ Method Type              │
 * ├──────────────────────────┼─────────────┼──────────────────────────┤
 * │ Thread.interrupted()     │ YES         │ Static (current thread)  │
 * │ thread.isInterrupted()   │ NO          │ Instance                 │
 * └──────────────────────────┴─────────────┴──────────────────────────┘
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class ThreadBasics {

    /**
     * METHOD 1: Creating thread with Runnable (lambda)
     */
    public static void demonstrateLambdaThread() {
        System.out.println("=== Method 1: Lambda Runnable ===");

        Thread thread = new Thread(() -> {
            System.out.println("Running in: " + Thread.currentThread().getName());
            System.out.println("Thread ID: " + Thread.currentThread().getId());
        });

        thread.setName("Lambda-Worker");
        thread.start();
    }

    /**
     * METHOD 1: Creating thread with Runnable (anonymous class)
     */
    public static void demonstrateAnonymousClassThread() {
        System.out.println("\n=== Method 1: Anonymous Runnable ===");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running in: " + Thread.currentThread().getName());
            }
        });

        thread.start();
    }

    /**
     * METHOD 2: Extending Thread class
     */
    static class CustomThread extends Thread {
        @Override
        public void run() {
            System.out.println("Running in custom thread: " + getName());
        }
    }

    public static void demonstrateExtendedThread() {
        System.out.println("\n=== Method 2: Extend Thread ===");

        CustomThread thread = new CustomThread();
        thread.setName("Custom-Worker");
        thread.start();
    }

    /**
     * CRITICAL: Demonstrates run() vs start()
     */
    public static void demonstrateRunVsStart() throws InterruptedException {
        System.out.println("\n=== run() vs start() ===");
        System.out.println("Main thread: " + Thread.currentThread().getName());

        Thread thread = new Thread(() ->
            System.out.println("Executing in: " + Thread.currentThread().getName())
        );

        System.out.println("\nCalling run() directly:");
        thread.run();  // Runs in MAIN thread (synchronous)

        System.out.println("\nCalling start():");
        thread.start();  // Runs in NEW thread (asynchronous)

        thread.join();  // Wait for thread to complete
    }

    /**
     * Demonstrates daemon threads
     */
    public static void demonstrateDaemonThread() throws InterruptedException {
        System.out.println("\n=== Daemon Thread ===");

        Thread daemon = new Thread(() -> {
            try {
                System.out.println("Daemon starting");
                Thread.sleep(5000);  // Long sleep
                System.out.println("Daemon finished");  // May not print!
            } catch (InterruptedException e) {
                System.out.println("Daemon interrupted");
            }
        });

        daemon.setDaemon(true);  // MUST be before start()
        daemon.start();

        System.out.println("Main thread sleeping briefly...");
        Thread.sleep(1000);
        System.out.println("Main thread exiting (JVM will terminate daemon)");
        // JVM exits even though daemon thread is still sleeping
    }

    /**
     * Demonstrates thread states
     */
    public static void demonstrateThreadStates() throws InterruptedException {
        System.out.println("\n=== Thread States ===");

        Thread thread = new Thread(() -> {
            try {
                synchronized (ThreadBasics.class) {
                    System.out.println("Thread running");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("State after creation: " + thread.getState());  // NEW

        thread.start();
        System.out.println("State after start(): " + thread.getState());   // RUNNABLE

        Thread.sleep(100);  // Give thread time to start sleeping
        System.out.println("State while sleeping: " + thread.getState());  // TIMED_WAITING

        thread.join();
        System.out.println("State after completion: " + thread.getState()); // TERMINATED
    }

    /**
     * Demonstrates thread interruption
     */
    public static void demonstrateInterruption() throws InterruptedException {
        System.out.println("\n=== Thread Interruption ===");

        Thread worker = new Thread(() -> {
            try {
                int count = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Working... " + (++count));
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted during sleep - cleaning up");
            }
            System.out.println("Worker thread exiting gracefully");
        });

        worker.start();
        Thread.sleep(2000);  // Let it work for 2 seconds

        System.out.println("Main thread interrupting worker");
        worker.interrupt();

        worker.join();  // Wait for graceful shutdown
        System.out.println("Main thread exiting");
    }

    /**
     * Demonstrates thread priority (hint to scheduler)
     */
    public static void demonstrateThreadPriority() {
        System.out.println("\n=== Thread Priority ===");

        Thread lowPriority = new Thread(() ->
            System.out.println("Low priority thread")
        );
        lowPriority.setPriority(Thread.MIN_PRIORITY);  // 1

        Thread highPriority = new Thread(() ->
            System.out.println("High priority thread")
        );
        highPriority.setPriority(Thread.MAX_PRIORITY);  // 10

        // Priority is a HINT - order not guaranteed
        lowPriority.start();
        highPriority.start();
    }

    /**
     * EXAM TRICK: Calling start() twice throws exception
     */
    public static void demonstrateDoubleStart() {
        System.out.println("\n=== Double Start (Throws Exception) ===");

        Thread thread = new Thread(() -> System.out.println("Running"));

        thread.start();  // OK

        try {
            thread.start();  // IllegalThreadStateException!
        } catch (IllegalThreadStateException e) {
            System.out.println("Cannot call start() twice: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        demonstrateLambdaThread();
        Thread.sleep(100);

        demonstrateAnonymousClassThread();
        Thread.sleep(100);

        demonstrateExtendedThread();
        Thread.sleep(100);

        demonstrateRunVsStart();

        demonstrateDaemonThread();

        demonstrateThreadStates();

        demonstrateInterruption();

        demonstrateThreadPriority();
        Thread.sleep(100);

        demonstrateDoubleStart();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - THREAD BASICS
 * ============================================================================
 *
 * RUNNABLE INTERFACE:
 * - @FunctionalInterface with void run() method
 * - No parameters, returns void, no checked exceptions
 * - Can use lambda: () -> { code }
 *
 * CREATING THREADS:
 * 1. Provide Runnable to Thread constructor (PREFERRED)
 * 2. Extend Thread and override run()
 *
 * run() vs start():
 * - start() creates NEW thread (asynchronous)
 * - run() executes in CURRENT thread (synchronous) - EXAM TRICK!
 * - Can only call start() once - second call throws IllegalThreadStateException
 *
 * THREAD TYPES:
 * - User threads: prevent JVM exit
 * - Daemon threads: don't prevent JVM exit
 * - setDaemon(true) must be BEFORE start()
 *
 * THREAD STATES (6):
 * 1. NEW - Created but not started
 * 2. RUNNABLE - Executing or ready to execute
 * 3. BLOCKED - Waiting for lock
 * 4. WAITING - Waiting indefinitely (wait, join)
 * 5. TIMED_WAITING - Waiting with timeout (sleep, wait with timeout)
 * 6. TERMINATED - Execution completed
 *
 * INTERRUPTION:
 * - interrupt() sets flag or throws InterruptedException
 * - Thread.interrupted() checks and CLEARS flag (static)
 * - isInterrupted() checks flag without clearing (instance)
 * - InterruptedException is checked - must handle
 * - Interruption is cooperative - thread must check and respond
 *
 * THREAD PRIORITY:
 * - Range: 1 (MIN_PRIORITY) to 10 (MAX_PRIORITY), default 5 (NORM_PRIORITY)
 * - Priority is a HINT, not a guarantee
 */
