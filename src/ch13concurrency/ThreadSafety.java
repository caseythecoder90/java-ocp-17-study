package ch13concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

/**
 * THREAD SAFETY - SYNCHRONIZATION AND ATOMIC OPERATIONS
 * ======================================================
 *
 * Techniques for writing thread-safe code: volatile, atomic classes,
 * synchronized blocks/methods, locks, and coordination utilities.
 *
 *
 * ============================================================================
 * VOLATILE KEYWORD
 * ============================================================================
 *
 * The volatile keyword guarantees that access to data within memory is consistent.
 *
 * PROBLEM WITHOUT VOLATILE:
 * - Each thread may cache variables in CPU cache
 * - Changes by one thread may not be visible to other threads
 * - Thread may read stale data from its cache
 *
 * VOLATILE GUARANTEES:
 * - Reads and writes go directly to main memory (not CPU cache)
 * - All threads see the most recent value
 * - Establishes happens-before relationship
 *
 * SYNTAX:
 *   private volatile boolean flag = false;
 *   private volatile int counter = 0;
 *
 * WHEN TO USE:
 * - Variable written by one thread, read by others
 * - Variable is a simple flag or state indicator
 * - Example: shutdown flags, status indicators
 *
 * LIMITATIONS:
 * - Does NOT provide atomicity for compound operations
 * - counter++ is NOT atomic even with volatile!
 * - Use atomic classes or synchronized for compound operations
 *
 * EXAMPLE - Common Pattern:
 *   private volatile boolean running = true;
 *
 *   public void run() {
 *       while (running) {  // Will see updates from other threads
 *           doWork();
 *       }
 *   }
 *
 *   public void stop() {
 *       running = false;  // Immediately visible to all threads
 *   }
 *
 *
 * ============================================================================
 * ATOMIC CLASSES
 * ============================================================================
 *
 * Classes in java.util.concurrent.atomic that provide atomic operations.
 * Atomic = operation completes in a single, uninterruptible step.
 *
 * COMMON ATOMIC CLASSES:
 * - AtomicBoolean - atomic boolean operations
 * - AtomicInteger - atomic int operations
 * - AtomicLong    - atomic long operations
 *
 * WHY USE ATOMIC CLASSES:
 * - Thread-safe without explicit synchronization
 * - Better performance than synchronized for simple operations
 * - Prevent race conditions on single variables
 *
 *
 * ============================================================================
 * ATOMIC CLASS METHODS (KNOW THESE!)
 * ============================================================================
 *
 * These methods are available on AtomicBoolean, AtomicInteger, AtomicLong
 * (with appropriate types).
 *
 *
 * get()
 * ─────
 *   int get()          // AtomicInteger
 *   long get()         // AtomicLong
 *   boolean get()      // AtomicBoolean
 *
 * - Returns the current value
 * - Thread-safe read
 *
 * EXAMPLE:
 *   AtomicInteger count = new AtomicInteger(5);
 *   int value = count.get();  // value = 5
 *
 *
 * set()
 * ─────
 *   void set(int newValue)         // AtomicInteger
 *   void set(long newValue)        // AtomicLong
 *   void set(boolean newValue)     // AtomicBoolean
 *
 * - Sets to the given value
 * - Thread-safe write
 *
 * EXAMPLE:
 *   AtomicInteger count = new AtomicInteger(5);
 *   count.set(10);  // count is now 10
 *
 *
 * getAndSet()
 * ───────────
 *   int getAndSet(int newValue)         // AtomicInteger
 *   long getAndSet(long newValue)       // AtomicLong
 *   boolean getAndSet(boolean newValue) // AtomicBoolean
 *
 * - Atomically sets to new value and returns OLD value
 * - EXAM NOTE: Returns the PREVIOUS value, not the new one!
 *
 * EXAMPLE:
 *   AtomicInteger count = new AtomicInteger(5);
 *   int oldValue = count.getAndSet(10);  // oldValue = 5, count is now 10
 *
 *
 * incrementAndGet()  (AtomicInteger, AtomicLong only)
 * ─────────────────
 *   int incrementAndGet()     // AtomicInteger
 *   long incrementAndGet()    // AtomicLong
 *
 * - Atomically increments by 1 and returns NEW value
 * - Equivalent to ++counter (prefix increment)
 * - MEMORY AID: "increment AND get" = increment THEN return
 *
 * EXAMPLE:
 *   AtomicInteger count = new AtomicInteger(5);
 *   int newValue = count.incrementAndGet();  // newValue = 6, count is 6
 *
 *
 * getAndIncrement()  (AtomicInteger, AtomicLong only)
 * ─────────────────
 *   int getAndIncrement()     // AtomicInteger
 *   long getAndIncrement()    // AtomicLong
 *
 * - Atomically increments by 1 and returns OLD value
 * - Equivalent to counter++ (postfix increment)
 * - MEMORY AID: "get AND increment" = return THEN increment
 *
 * EXAMPLE:
 *   AtomicInteger count = new AtomicInteger(5);
 *   int oldValue = count.getAndIncrement();  // oldValue = 5, count is 6
 *
 *
 * decrementAndGet()  (AtomicInteger, AtomicLong only)
 * ─────────────────
 *   int decrementAndGet()     // AtomicInteger
 *   long decrementAndGet()    // AtomicLong
 *
 * - Atomically decrements by 1 and returns NEW value
 * - Equivalent to --counter (prefix decrement)
 * - MEMORY AID: "decrement AND get" = decrement THEN return
 *
 * EXAMPLE:
 *   AtomicInteger count = new AtomicInteger(5);
 *   int newValue = count.decrementAndGet();  // newValue = 4, count is 4
 *
 *
 * getAndDecrement()  (AtomicInteger, AtomicLong only)
 * ─────────────────
 *   int getAndDecrement()     // AtomicInteger
 *   long getAndDecrement()    // AtomicLong
 *
 * - Atomically decrements by 1 and returns OLD value
 * - Equivalent to counter-- (postfix decrement)
 * - MEMORY AID: "get AND decrement" = return THEN decrement
 *
 * EXAMPLE:
 *   AtomicInteger count = new AtomicInteger(5);
 *   int oldValue = count.getAndDecrement();  // oldValue = 5, count is 4
 *
 *
 * COMPARISON TABLE - INCREMENT/DECREMENT METHODS:
 * ┌──────────────────────┬──────────────┬──────────────────────────────┐
 * │ Method               │ Returns      │ Equivalent To                │
 * ├──────────────────────┼──────────────┼──────────────────────────────┤
 * │ incrementAndGet()    │ NEW value    │ ++counter (prefix)           │
 * │ getAndIncrement()    │ OLD value    │ counter++ (postfix)          │
 * │ decrementAndGet()    │ NEW value    │ --counter (prefix)           │
 * │ getAndDecrement()    │ OLD value    │ counter-- (postfix)          │
 * └──────────────────────┴──────────────┴──────────────────────────────┘
 *
 * MEMORY TRICK:
 * - "AND get" at END = operation happens, THEN get (returns NEW)
 * - "get AND" at START = get OLD, THEN operation (returns OLD)
 *
 *
 * ============================================================================
 * SYNCHRONIZED BLOCKS
 * ============================================================================
 *
 * Use synchronized blocks to achieve mutual exclusion using a monitor/lock.
 * Only ONE thread can execute synchronized code on the same object at a time.
 *
 * SYNTAX:
 *   synchronized (lockObject) {
 *       // Critical section - only one thread at a time
 *   }
 *
 * HOW IT WORKS:
 * - Thread acquires monitor/lock on lockObject
 * - Other threads trying to synchronize on SAME object block (wait)
 * - Lock is automatically released when block exits
 * - Lock released even if exception is thrown
 *
 * LOCK OBJECT:
 * - Can be any object reference
 * - Common: synchronized(this) for instance methods
 * - Different lock objects = different locks (threads don't block each other)
 *
 * EXAMPLE:
 *   private final Object lock = new Object();
 *   private int count = 0;
 *
 *   public void increment() {
 *       synchronized (lock) {
 *           count++;  // Protected from race conditions
 *       }
 *   }
 *
 * CHOOSING LOCK OBJECT:
 *   synchronized(this)      // Lock on current instance
 *   synchronized(obj)       // Lock on specific object
 *   synchronized(MyClass.class)  // Lock on class object (static)
 *
 *
 * ============================================================================
 * SYNCHRONIZED METHODS
 * ============================================================================
 *
 * Shorthand for synchronizing entire method body.
 *
 * INSTANCE METHODS:
 *   public synchronized void increment() {
 *       count++;
 *   }
 *
 * Equivalent to:
 *   public void increment() {
 *       synchronized(this) {
 *           count++;
 *       }
 *   }
 *
 *
 * STATIC METHODS:
 *   public static synchronized void increment() {
 *       staticCount++;
 *   }
 *
 * Equivalent to:
 *   public static void increment() {
 *       synchronized(MyClass.class) {
 *           staticCount++;
 *       }
 *   }
 *
 *
 * SYNCHRONIZED METHOD vs BLOCK:
 * ┌────────────────────────┬─────────────────────────────────────────────┐
 * │ Synchronized Method    │ Synchronized Block                          │
 * ├────────────────────────┼─────────────────────────────────────────────┤
 * │ Locks entire method    │ Can lock just critical section             │
 * │ Always uses 'this'     │ Can choose lock object                      │
 * │ (or Class for static)  │                                             │
 * │ Less flexible          │ More flexible, better performance           │
 * └────────────────────────┴─────────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * LOCK FRAMEWORK - REENTRANTLOCK
 * ============================================================================
 *
 * Lock interface provides more flexible locking than synchronized.
 * ReentrantLock is the most common implementation.
 *
 * WHY REENTRANTLOCK:
 * - Can try to acquire lock without blocking (tryLock)
 * - Can timeout while waiting for lock
 * - Can interrupt thread waiting for lock
 * - More explicit control over lock acquisition/release
 *
 * CREATING A LOCK:
 *   Lock lock = new ReentrantLock();
 *
 * WHY "REENTRANT":
 * - Same thread can acquire the lock multiple times
 * - Must release same number of times as acquired
 * - Lock has a "hold count" per thread
 *
 *
 * ============================================================================
 * REENTRANTLOCK METHODS (KNOW THESE!)
 * ============================================================================
 *
 * lock()
 * ──────
 *   void lock()
 *
 * - Acquires the lock
 * - If lock not available, thread BLOCKS until it is
 * - If same thread already holds lock, increments hold count
 * - No timeout - waits indefinitely
 *
 * EXAMPLE:
 *   Lock lock = new ReentrantLock();
 *   lock.lock();
 *   try {
 *       // Critical section
 *   } finally {
 *       lock.unlock();  // MUST be in finally!
 *   }
 *
 *
 * unlock()
 * ────────
 *   void unlock()
 *
 * - Releases the lock
 * - Decrements hold count
 * - If hold count reaches 0, lock is released
 * - MUST call same number of times as lock() was called
 * - ALWAYS call in finally block!
 *
 * CRITICAL: If you don't unlock, other threads wait forever!
 *
 *
 * tryLock() - Two Signatures
 * ──────────────────────────
 *
 * SIGNATURE 1: No arguments
 *   boolean tryLock()
 *
 * - Tries to acquire lock immediately
 * - Returns true if lock acquired, false if not available
 * - Does NOT block - returns immediately
 * - Useful for avoiding deadlock
 *
 * EXAMPLE:
 *   Lock lock = new ReentrantLock();
 *   if (lock.tryLock()) {
 *       try {
 *           // Critical section
 *       } finally {
 *           lock.unlock();
 *       }
 *   } else {
 *       // Lock not available - do alternative action
 *   }
 *
 *
 * SIGNATURE 2: With timeout
 *   boolean tryLock(long time, TimeUnit unit) throws InterruptedException
 *
 * - Tries to acquire lock within the given timeout
 * - Returns true if lock acquired within timeout
 * - Returns false if timeout expires
 * - Throws InterruptedException if thread interrupted while waiting
 *
 * EXAMPLE:
 *   Lock lock = new ReentrantLock();
 *   try {
 *       if (lock.tryLock(5, TimeUnit.SECONDS)) {
 *           try {
 *               // Critical section
 *           } finally {
 *               lock.unlock();
 *           }
 *       } else {
 *           // Timeout - lock not acquired
 *       }
 *   } catch (InterruptedException e) {
 *       // Thread interrupted
 *   }
 *
 *
 * LOCK RELEASE RULE (CRITICAL!):
 * ──────────────────────────────
 * Must call unlock() SAME NUMBER OF TIMES as lock acquired!
 *
 *   lock.lock();        // Hold count = 1
 *   lock.lock();        // Hold count = 2 (reentrant)
 *   lock.unlock();      // Hold count = 1
 *   lock.unlock();      // Hold count = 0 (lock released)
 *
 * If you call unlock() fewer times:
 *   - Lock not fully released
 *   - Other threads can't acquire it
 *   - DEADLOCK!
 *
 *
 * BEST PRACTICE PATTERN:
 *   Lock lock = new ReentrantLock();
 *
 *   lock.lock();
 *   try {
 *       // Critical section
 *   } finally {
 *       lock.unlock();  // ALWAYS in finally!
 *   }
 *
 *
 * ============================================================================
 * CYCLICBARRIER
 * ============================================================================
 *
 * A synchronization aid that allows a set of threads to wait for each other
 * to reach a common barrier point.
 *
 * USE CASE:
 * - Divide work into parallel tasks
 * - All tasks must complete a phase before any proceed to next phase
 * - Example: Parallel computation where all threads must finish step 1
 *   before any can start step 2
 *
 * WHY "CYCLIC":
 * - Barrier can be reused after all threads release
 * - Unlike CountDownLatch (one-time use)
 *
 *
 * CONSTRUCTORS:
 * ─────────────
 *   CyclicBarrier(int parties)
 *   CyclicBarrier(int parties, Runnable barrierAction)
 *
 * - parties: Number of threads that must call await() before barrier releases
 * - barrierAction: Optional action run when all threads reach barrier
 *   (executed by LAST thread to arrive)
 *
 *
 * await() METHOD - Two Signatures:
 * ────────────────────────────────
 *
 * SIGNATURE 1: No timeout
 *   int await() throws InterruptedException, BrokenBarrierException
 *
 * - Thread waits at barrier until all parties arrive
 * - Returns arrival index (0 to parties-1)
 * - Last thread to arrive gets index 0
 * - First thread to arrive gets index (parties-1)
 * - Throws BrokenBarrierException if barrier broken (thread interrupted/timeout)
 *
 *
 * SIGNATURE 2: With timeout
 *   int await(long timeout, TimeUnit unit)
 *       throws InterruptedException, BrokenBarrierException, TimeoutException
 *
 * - Waits up to timeout for all parties to arrive
 * - Throws TimeoutException if timeout expires
 * - If timeout occurs, barrier is broken (other threads get BrokenBarrierException)
 *
 *
 * HOW IT WORKS:
 * ─────────────
 * 1. Create CyclicBarrier with number of parties (threads)
 * 2. Each thread does work then calls await()
 * 3. Threads block at await() until all parties arrive
 * 4. When last thread calls await(), all threads are released
 * 5. Optional barrier action runs before release
 * 6. Barrier resets and can be reused
 *
 *
 * EXAMPLE SCENARIO - 3 Threads Processing Data:
 *
 *   CyclicBarrier barrier = new CyclicBarrier(3, () -> {
 *       System.out.println("All threads ready - proceeding to next phase");
 *   });
 *
 *   // Thread 1
 *   loadData();
 *   barrier.await();  // Wait for others
 *   processData();
 *
 *   // Thread 2
 *   loadData();
 *   barrier.await();  // Wait for others
 *   processData();
 *
 *   // Thread 3
 *   loadData();
 *   barrier.await();  // Wait for others - last to arrive, releases all
 *   processData();
 *
 *   // After last await(), barrier action runs, then all proceed to processData()
 *
 *
 * REUSE EXAMPLE:
 *   CyclicBarrier barrier = new CyclicBarrier(3);
 *
 *   for (int round = 0; round < 5; round++) {
 *       doWork(round);
 *       barrier.await();  // All threads sync at end of each round
 *   }
 *   // Barrier reused 5 times
 *
 *
 * KEY METHODS:
 * ────────────
 *   int getParties()       // Returns number of parties
 *   int getNumberWaiting() // Returns number currently waiting at barrier
 *   boolean isBroken()     // Returns true if barrier broken
 *   void reset()           // Resets barrier to initial state
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class ThreadSafety {

    /**
     * Demonstrates volatile keyword for visibility
     */
    static class VolatileExample {
        private volatile boolean running = true;
        private int counter = 0;

        public void run() {
            System.out.println("Worker thread starting");
            while (running) {  // Will see updates from stop()
                counter++;
            }
            System.out.println("Worker thread stopped. Counter: " + counter);
        }

        public void stop() {
            System.out.println("Stopping worker thread");
            running = false;  // Immediately visible to run()
        }
    }

    public static void demonstrateVolatile() throws InterruptedException {
        System.out.println("=== Volatile Keyword ===");

        VolatileExample example = new VolatileExample();

        Thread worker = new Thread(example::run);
        worker.start();

        Thread.sleep(100);  // Let it run briefly
        example.stop();

        worker.join();
        System.out.println("Volatile ensured visibility of 'running' flag\n");
    }

    /**
     * Demonstrates atomic class methods
     */
    public static void demonstrateAtomicMethods() {
        System.out.println("=== Atomic Class Methods ===");

        AtomicInteger count = new AtomicInteger(5);
        System.out.println("Initial value: " + count.get());  // 5

        // set and get
        count.set(10);
        System.out.println("After set(10): " + count.get());  // 10

        // getAndSet - returns OLD value
        int oldValue = count.getAndSet(20);
        System.out.println("getAndSet(20) returned: " + oldValue);  // 10
        System.out.println("Current value: " + count.get());  // 20

        // incrementAndGet - returns NEW value (++count)
        int newValue = count.incrementAndGet();
        System.out.println("incrementAndGet() returned: " + newValue);  // 21
        System.out.println("Current value: " + count.get());  // 21

        // getAndIncrement - returns OLD value (count++)
        oldValue = count.getAndIncrement();
        System.out.println("getAndIncrement() returned: " + oldValue);  // 21
        System.out.println("Current value: " + count.get());  // 22

        // decrementAndGet - returns NEW value (--count)
        newValue = count.decrementAndGet();
        System.out.println("decrementAndGet() returned: " + newValue);  // 21
        System.out.println("Current value: " + count.get());  // 21

        // getAndDecrement - returns OLD value (count--)
        oldValue = count.getAndDecrement();
        System.out.println("getAndDecrement() returned: " + oldValue);  // 21
        System.out.println("Current value: " + count.get());  // 20

        System.out.println();
    }

    /**
     * Demonstrates race condition WITHOUT atomic
     */
    static class UnsafeCounter {
        private int count = 0;

        public void increment() {
            count++;  // NOT atomic! (read, increment, write)
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Demonstrates thread-safe counter WITH atomic
     */
    static class SafeCounter {
        private AtomicInteger count = new AtomicInteger(0);

        public void increment() {
            count.incrementAndGet();  // Atomic operation
        }

        public int getCount() {
            return count.get();
        }
    }

    public static void demonstrateAtomicThreadSafety() throws InterruptedException {
        System.out.println("=== Atomic Thread Safety ===");

        // Unsafe version
        UnsafeCounter unsafe = new UnsafeCounter();
        Thread[] unsafeThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    unsafe.increment();
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) t.join();
        System.out.println("Unsafe counter (race condition): " + unsafe.getCount());
        System.out.println("Expected: 10000");

        // Safe version
        SafeCounter safe = new SafeCounter();
        Thread[] safeThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    safe.increment();
                }
            });
            safeThreads[i].start();
        }
        for (Thread t : safeThreads) t.join();
        System.out.println("Safe atomic counter: " + safe.getCount());
        System.out.println("Expected: 10000\n");
    }

    /**
     * Demonstrates synchronized block
     */
    static class SynchronizedCounter {
        private final Object lock = new Object();
        private int count = 0;

        public void increment() {
            synchronized (lock) {
                count++;  // Only one thread at a time
            }
        }

        public int getCount() {
            synchronized (lock) {
                return count;
            }
        }
    }

    public static void demonstrateSynchronizedBlock() throws InterruptedException {
        System.out.println("=== Synchronized Block ===");

        SynchronizedCounter counter = new SynchronizedCounter();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();
        System.out.println("Synchronized counter: " + counter.getCount());
        System.out.println("Expected: 10000\n");
    }

    /**
     * Demonstrates synchronized method
     */
    static class SynchronizedMethodCounter {
        private int count = 0;

        public synchronized void increment() {
            count++;  // Entire method synchronized on 'this'
        }

        public synchronized int getCount() {
            return count;
        }
    }

    public static void demonstrateSynchronizedMethod() throws InterruptedException {
        System.out.println("=== Synchronized Method ===");

        SynchronizedMethodCounter counter = new SynchronizedMethodCounter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Synchronized method counter: " + counter.getCount());
        System.out.println("Expected: 2000\n");
    }

    /**
     * Demonstrates ReentrantLock basic usage
     */
    public static void demonstrateReentrantLock() throws InterruptedException {
        System.out.println("=== ReentrantLock ===");

        Lock lock = new ReentrantLock();
        int[] counter = {0};

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                lock.lock();
                try {
                    counter[0]++;
                } finally {
                    lock.unlock();  // MUST be in finally!
                }
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("ReentrantLock counter: " + counter[0]);
        System.out.println("Expected: 2000\n");
    }

    /**
     * Demonstrates tryLock() without timeout
     */
    public static void demonstrateTryLock() throws InterruptedException {
        System.out.println("=== tryLock() ===");

        Lock lock = new ReentrantLock();

        // Thread 1 holds lock for 2 seconds
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1 acquired lock");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println("Thread 1 released lock");
            }
        });

        // Thread 2 tries to acquire lock
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(500);  // Let t1 acquire first
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (lock.tryLock()) {
                try {
                    System.out.println("Thread 2 acquired lock");
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Thread 2 could not acquire lock - doing alternative work");
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println();
    }

    /**
     * Demonstrates tryLock() with timeout
     */
    public static void demonstrateTryLockTimeout() throws InterruptedException {
        System.out.println("=== tryLock(timeout) ===");

        Lock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1 holding lock for 3 seconds");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(500);
                System.out.println("Thread 2 trying to acquire lock (1 second timeout)");

                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Thread 2 acquired lock");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Thread 2 timeout - lock not acquired");
                }
            } catch (InterruptedException e) {
                System.out.println("Thread 2 interrupted");
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println();
    }

    /**
     * Demonstrates reentrant nature - same thread acquires lock multiple times
     */
    public static void demonstrateReentrant() {
        System.out.println("=== Reentrant Lock ===");

        Lock lock = new ReentrantLock();

        lock.lock();
        System.out.println("First lock acquired (hold count = 1)");
        try {
            lock.lock();
            System.out.println("Second lock acquired (hold count = 2)");
            try {
                System.out.println("In critical section");
            } finally {
                lock.unlock();
                System.out.println("First unlock (hold count = 1)");
            }
        } finally {
            lock.unlock();
            System.out.println("Second unlock (hold count = 0 - lock released)");
        }
        System.out.println();
    }

    /**
     * Demonstrates CyclicBarrier
     */
    public static void demonstrateCyclicBarrier() throws InterruptedException {
        System.out.println("=== CyclicBarrier ===");

        int parties = 3;
        CyclicBarrier barrier = new CyclicBarrier(parties, () -> {
            System.out.println("*** All threads reached barrier - proceeding! ***");
        });

        Runnable task = () -> {
            try {
                String name = Thread.currentThread().getName();

                System.out.println(name + " - Phase 1: Loading data");
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println(name + " - Phase 1 complete, waiting at barrier");

                int arrivalIndex = barrier.await();  // Wait for others
                System.out.println(name + " - Released from barrier (arrival index: " + arrivalIndex + ")");

                System.out.println(name + " - Phase 2: Processing data");
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println(name + " - Phase 2 complete");

            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
        System.out.println();
    }

    /**
     * Demonstrates CyclicBarrier reuse
     */
    public static void demonstrateCyclicBarrierReuse() throws InterruptedException {
        System.out.println("=== CyclicBarrier Reuse ===");

        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            System.out.println("  -> Barrier released");
        });

        Runnable task = () -> {
            try {
                for (int round = 1; round <= 3; round++) {
                    System.out.println(Thread.currentThread().getName() +
                                     " - Round " + round + " work");
                    Thread.sleep(500);
                    barrier.await();  // Sync at end of each round
                    System.out.println(Thread.currentThread().getName() +
                                     " - Round " + round + " complete");
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        Thread t1 = new Thread(task, "Worker-1");
        Thread t2 = new Thread(task, "Worker-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Barrier was reused 3 times\n");
    }

    /**
     * Demonstrates CyclicBarrier with timeout
     */
    public static void demonstrateCyclicBarrierTimeout() throws InterruptedException {
        System.out.println("=== CyclicBarrier Timeout ===");

        CyclicBarrier barrier = new CyclicBarrier(3);

        // Two threads arrive, one doesn't
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("Thread 1 waiting at barrier");
                barrier.await(2, TimeUnit.SECONDS);
                System.out.println("Thread 1 released");
            } catch (TimeoutException e) {
                System.out.println("Thread 1 timeout!");
            } catch (Exception e) {
                System.out.println("Thread 1 exception: " + e.getClass().getSimpleName());
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                System.out.println("Thread 2 waiting at barrier");
                barrier.await();
                System.out.println("Thread 2 released");
            } catch (BrokenBarrierException e) {
                System.out.println("Thread 2 - barrier broken!");
            } catch (Exception e) {
                System.out.println("Thread 2 exception: " + e.getClass().getSimpleName());
            }
        });

        t1.start();
        t2.start();
        // Third thread never arrives - barrier times out

        t1.join();
        t2.join();

        System.out.println("Barrier is broken: " + barrier.isBroken());
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        demonstrateVolatile();
        demonstrateAtomicMethods();
        demonstrateAtomicThreadSafety();
        demonstrateSynchronizedBlock();
        demonstrateSynchronizedMethod();
        demonstrateReentrantLock();
        demonstrateTryLock();
        demonstrateTryLockTimeout();
        demonstrateReentrant();
        demonstrateCyclicBarrier();
        demonstrateCyclicBarrierReuse();
        demonstrateCyclicBarrierTimeout();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - THREAD SAFETY
 * ============================================================================
 *
 * VOLATILE:
 * - Guarantees visibility of changes across threads
 * - Reads/writes go to main memory (not CPU cache)
 * - Does NOT provide atomicity for compound operations
 * - Use for: simple flags, status indicators
 *
 * ATOMIC CLASSES:
 * - AtomicBoolean, AtomicInteger, AtomicLong
 * - Thread-safe without explicit synchronization
 *
 * ATOMIC METHODS (know what they return!):
 * - get(), set(value) - basic read/write
 * - getAndSet(value) - set and return OLD value
 * - incrementAndGet() - increment and return NEW (++counter)
 * - getAndIncrement() - return OLD and increment (counter++)
 * - decrementAndGet() - decrement and return NEW (--counter)
 * - getAndDecrement() - return OLD and decrement (counter--)
 *
 * SYNCHRONIZED:
 * - synchronized(object) { } - locks on object
 * - synchronized method - locks on 'this' (instance) or Class (static)
 * - Provides mutual exclusion - one thread at a time
 * - Lock automatically released when exiting block
 *
 * REENTRANTLOCK:
 * - lock() - acquire lock (blocks if not available)
 * - unlock() - release lock (MUST be in finally!)
 * - tryLock() - try to acquire, return immediately
 * - tryLock(timeout, unit) - try with timeout
 * - CRITICAL: unlock() same number of times as lock()!
 * - Reentrant: same thread can acquire multiple times
 *
 * CYCLICBARRIER:
 * - Synchronization point for multiple threads
 * - Constructor: CyclicBarrier(parties) or CyclicBarrier(parties, action)
 * - await() - wait until all parties arrive
 * - await(timeout, unit) - wait with timeout
 * - Returns arrival index (0 to parties-1)
 * - Barrier can be reused (unlike CountDownLatch)
 * - If timeout/interrupt occurs, barrier is broken
 * - BrokenBarrierException if barrier broken
 */
