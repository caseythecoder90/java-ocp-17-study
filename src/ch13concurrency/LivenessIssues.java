package ch13concurrency;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * LIVENESS ISSUES
 * ===============
 *
 * Problems that prevent a program from making progress: deadlock, starvation, livelock.
 *
 *
 * ============================================================================
 * LIVENESS
 * ============================================================================
 *
 * The ability of an application to execute in a timely manner.
 *
 * LIVENESS PROBLEMS:
 * - Deadlock: Threads wait for each other forever
 * - Starvation: Thread perpetually denied access to resources
 * - Livelock: Threads actively responding but making no progress
 *
 * All three prevent threads from completing their work!
 *
 *
 * ============================================================================
 * 1. DEADLOCK
 * ============================================================================
 *
 * Two or more threads are blocked forever, waiting for each other.
 *
 * CLASSIC SCENARIO:
 * - Thread 1 holds Lock A, waits for Lock B
 * - Thread 2 holds Lock B, waits for Lock A
 * - Neither can proceed - DEADLOCK!
 *
 * DIAGRAM:
 *   Thread 1: [Has Lock A] --waits for--> Lock B [Held by Thread 2]
 *   Thread 2: [Has Lock B] --waits for--> Lock A [Held by Thread 1]
 *   ↑                                                              |
 *   └──────────────────────── Circular wait ─────────────────────┘
 *
 * REQUIREMENTS FOR DEADLOCK (all must be present):
 * 1. Mutual Exclusion: Resources can't be shared
 * 2. Hold and Wait: Thread holds resource while waiting for another
 * 3. No Preemption: Resources can't be forcibly taken
 * 4. Circular Wait: Circular chain of threads waiting for resources
 *
 * EXAMPLE CODE PATTERN (DEADLOCK):
 *   Object lock1 = new Object();
 *   Object lock2 = new Object();
 *
 *   // Thread 1
 *   synchronized(lock1) {
 *       Thread.sleep(50);  // Give time for Thread 2 to acquire lock2
 *       synchronized(lock2) {  // Waits forever!
 *           // work
 *       }
 *   }
 *
 *   // Thread 2
 *   synchronized(lock2) {
 *       Thread.sleep(50);  // Give time for Thread 1 to acquire lock1
 *       synchronized(lock1) {  // Waits forever!
 *           // work
 *       }
 *   }
 *
 * SYMPTOMS:
 * - Application freezes or hangs
 * - CPU usage may be low (threads blocked)
 * - Thread dump shows threads waiting for locks
 *
 * HOW TO DETECT:
 * - jstack tool shows thread dump
 * - Look for "waiting to lock" messages
 * - JConsole, VisualVM can detect deadlocks
 *
 *
 * PREVENTING DEADLOCK:
 * ════════════════════
 *
 * Strategy 1: Lock Ordering
 * ─────────────────────────
 * Always acquire locks in the SAME ORDER
 *
 *   // Both threads acquire lock1 first, then lock2
 *   synchronized(lock1) {
 *       synchronized(lock2) {
 *           // Safe!
 *       }
 *   }
 *
 *
 * Strategy 2: Lock Timeout
 * ────────────────────────
 * Use tryLock() with timeout instead of lock()
 *
 *   Lock lock1 = new ReentrantLock();
 *   Lock lock2 = new ReentrantLock();
 *
 *   if (lock1.tryLock(1, TimeUnit.SECONDS)) {
 *       try {
 *           if (lock2.tryLock(1, TimeUnit.SECONDS)) {
 *               try {
 *                   // Work with both locks
 *               } finally {
 *                   lock2.unlock();
 *               }
 *           }
 *       } finally {
 *           lock1.unlock();
 *       }
 *   }
 *
 *
 * Strategy 3: Avoid Nested Locks
 * ──────────────────────────────
 * Don't acquire multiple locks if possible
 * Use concurrent collections instead
 *
 *
 * Strategy 4: Use Higher-Level Concurrency Utilities
 * ───────────────────────────────────────────────────
 * ExecutorService, ConcurrentHashMap, etc.
 * Less prone to deadlock than manual locking
 *
 *
 * ============================================================================
 * 2. STARVATION
 * ============================================================================
 *
 * A thread is perpetually denied access to a resource.
 *
 * CHARACTERISTICS:
 * - Thread is RUNNABLE but never gets to execute
 * - Other threads keep "cutting in line"
 * - Thread makes NO progress over time
 * - Unlike deadlock, OTHER threads ARE making progress
 *
 * COMMON CAUSES:
 *
 * 1. Thread Priority Issues
 * ─────────────────────────
 * High-priority threads always run, low-priority thread never gets CPU
 *
 *   Thread lowPriority = new Thread(task);
 *   lowPriority.setPriority(Thread.MIN_PRIORITY);  // May starve!
 *
 *   Thread highPriority = new Thread(task);
 *   highPriority.setPriority(Thread.MAX_PRIORITY);  // Always runs first
 *
 *
 * 2. Unfair Locks
 * ───────────────
 * Lock repeatedly acquired by same threads
 *
 *   // Default ReentrantLock is unfair
 *   Lock lock = new ReentrantLock();  // Some threads may starve
 *
 *   // Fair lock ensures FIFO order
 *   Lock fairLock = new ReentrantLock(true);  // Prevents starvation
 *
 *
 * 3. Blocking I/O or Long Operations
 * ──────────────────────────────────
 * Thread holds resource while doing slow operation
 *
 *   synchronized(resource) {
 *       slowDatabaseQuery();  // Others wait a long time
 *   }
 *
 *
 * 4. Thread Pool Exhaustion
 * ─────────────────────────
 * All threads busy, new tasks never execute
 *
 *   ExecutorService executor = Executors.newFixedThreadPool(2);
 *   // Submit 100 long-running tasks
 *   // Last tasks may wait indefinitely (starve)
 *
 *
 * SYMPTOMS:
 * - Some threads never complete
 * - Uneven progress among threads
 * - Application appears to work but some operations never finish
 *
 *
 * PREVENTING STARVATION:
 * ══════════════════════
 *
 * 1. Avoid Thread Priorities
 * ──────────────────────────
 * Don't rely on priorities for correctness
 * Use default priority (NORM_PRIORITY)
 *
 *
 * 2. Use Fair Locks
 * ─────────────────
 *   Lock lock = new ReentrantLock(true);  // fair=true
 *
 *
 * 3. Minimize Lock Hold Time
 * ──────────────────────────
 *   synchronized(resource) {
 *       // Only critical section here
 *   }
 *   slowOperation();  // Do this OUTSIDE synchronized block
 *
 *
 * 4. Size Thread Pools Appropriately
 * ───────────────────────────────────
 * Enough threads for expected load
 *   ExecutorService executor = Executors.newFixedThreadPool(
 *       Runtime.getRuntime().availableProcessors()
 *   );
 *
 *
 * ============================================================================
 * 3. LIVELOCK
 * ============================================================================
 *
 * Threads are actively responding to each other but making no progress.
 *
 * ANALOGY:
 * Two people meet in a hallway:
 * - Person 1 moves left, Person 2 moves left (still blocking)
 * - Person 1 moves right, Person 2 moves right (still blocking)
 * - They keep moving but never pass each other!
 *
 * CHARACTERISTICS:
 * - Threads are NOT blocked (unlike deadlock)
 * - Threads are actively doing work (unlike starvation)
 * - BUT: No forward progress is made
 * - High CPU usage (threads constantly active)
 *
 * DIFFERENCE FROM DEADLOCK:
 * - Deadlock: Threads BLOCKED, waiting
 * - Livelock: Threads ACTIVE, but stuck in loop
 *
 * EXAMPLE SCENARIO:
 * Two threads trying to acquire same two locks:
 * - Thread 1: Try lock A, if fails, release everything and retry
 * - Thread 2: Try lock B, if fails, release everything and retry
 * - Both keep retrying but never succeed together
 *
 * CODE PATTERN (LIVELOCK):
 *   Thread 1:
 *   while (true) {
 *       if (lock1.tryLock()) {
 *           if (lock2.tryLock()) {
 *               // Work
 *               break;
 *           } else {
 *               lock1.unlock();  // Release and retry
 *               Thread.yield();  // Let other thread try
 *           }
 *       }
 *   }
 *
 *   Thread 2:
 *   while (true) {
 *       if (lock2.tryLock()) {
 *           if (lock1.tryLock()) {
 *               // Work
 *               break;
 *           } else {
 *               lock2.unlock();  // Release and retry
 *               Thread.yield();  // Let other thread try
 *           }
 *       }
 *   }
 *
 *   // Both threads keep trying and releasing - LIVELOCK!
 *
 *
 * SYMPTOMS:
 * - High CPU usage
 * - No deadlock detected
 * - Threads are RUNNABLE in thread dump
 * - Application makes no progress
 *
 *
 * PREVENTING LIVELOCK:
 * ════════════════════
 *
 * 1. Add Randomness
 * ─────────────────
 * Random backoff prevents synchronized retries
 *
 *   Thread.sleep((long)(Math.random() * 100));  // Random wait
 *
 *
 * 2. Lock Ordering
 * ────────────────
 * Same as deadlock prevention
 * Always acquire locks in same order
 *
 *
 * 3. Use tryLock() with Timeout
 * ─────────────────────────────
 * Eventually gives up instead of infinite retry
 *
 *   if (lock.tryLock(5, TimeUnit.SECONDS)) {
 *       // work
 *   } else {
 *       // Handle failure
 *   }
 *
 *
 * 4. Limit Retry Attempts
 * ───────────────────────
 *   for (int i = 0; i < MAX_RETRIES; i++) {
 *       if (tryOperation()) break;
 *       Thread.sleep(backoff);
 *   }
 *
 *
 * ============================================================================
 * COMPARISON TABLE
 * ============================================================================
 *
 * ┌─────────────────┬──────────────┬─────────────────┬──────────────────┐
 * │ Issue           │ Thread State │ CPU Usage       │ Progress         │
 * ├─────────────────┼──────────────┼─────────────────┼──────────────────┤
 * │ DEADLOCK        │ BLOCKED      │ Low (waiting)   │ None (blocked)   │
 * │                 │ WAITING      │                 │                  │
 * ├─────────────────┼──────────────┼─────────────────┼──────────────────┤
 * │ STARVATION      │ RUNNABLE     │ Low (not        │ None (never      │
 * │                 │              │ scheduled)      │ gets CPU)        │
 * ├─────────────────┼──────────────┼─────────────────┼──────────────────┤
 * │ LIVELOCK        │ RUNNABLE     │ High (active    │ None (stuck in   │
 * │                 │              │ loop)           │ retry loop)      │
 * └─────────────────┴──────────────┴─────────────────┴──────────────────┘
 *
 *
 * ┌─────────────────┬──────────────────────────────────────────────────┐
 * │ Issue           │ Key Characteristic                               │
 * ├─────────────────┼──────────────────────────────────────────────────┤
 * │ DEADLOCK        │ Circular wait - threads waiting for each other  │
 * ├─────────────────┼──────────────────────────────────────────────────┤
 * │ STARVATION      │ Perpetually denied resources - unfairness        │
 * ├─────────────────┼──────────────────────────────────────────────────┤
 * │ LIVELOCK        │ Active but no progress - excessive politeness    │
 * └─────────────────┴──────────────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class LivenessIssues {

    /**
     * Demonstrates DEADLOCK
     */
    static class DeadlockExample {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();

        public void demonstrateDeadlock() {
            System.out.println("=== DEADLOCK Example ===");
            System.out.println("Starting two threads that will deadlock...");

            Thread thread1 = new Thread(() -> {
                synchronized (lock1) {
                    System.out.println("Thread 1: Holding lock1...");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Thread 1: Waiting for lock2...");
                    synchronized (lock2) {
                        System.out.println("Thread 1: Acquired lock2!");
                    }
                }
            }, "Thread-1");

            Thread thread2 = new Thread(() -> {
                synchronized (lock2) {
                    System.out.println("Thread 2: Holding lock2...");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Thread 2: Waiting for lock1...");
                    synchronized (lock1) {
                        System.out.println("Thread 2: Acquired lock1!");
                    }
                }
            }, "Thread-2");

            thread1.start();
            thread2.start();

            try {
                thread1.join(2000);  // Wait max 2 seconds
                thread2.join(2000);

                if (thread1.isAlive() || thread2.isAlive()) {
                    System.out.println("DEADLOCK DETECTED! Threads still alive.");
                    System.out.println("Thread 1 state: " + thread1.getState());
                    System.out.println("Thread 2 state: " + thread2.getState());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * Demonstrates DEADLOCK PREVENTION using lock ordering
     */
    static class DeadlockPrevention {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();

        public void demonstratePreventDeadlock() throws InterruptedException {
            System.out.println("=== DEADLOCK PREVENTION (Lock Ordering) ===");

            Thread thread1 = new Thread(() -> {
                synchronized (lock1) {  // Both threads acquire lock1 first
                    System.out.println("Thread 1: Holding lock1...");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                    synchronized (lock2) {  // Then lock2
                        System.out.println("Thread 1: Acquired both locks!");
                    }
                }
            });

            Thread thread2 = new Thread(() -> {
                synchronized (lock1) {  // Same order as thread1!
                    System.out.println("Thread 2: Holding lock1...");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                    synchronized (lock2) {
                        System.out.println("Thread 2: Acquired both locks!");
                    }
                }
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            System.out.println("Both threads completed successfully - no deadlock!");
            System.out.println();
        }
    }

    /**
     * Demonstrates STARVATION
     */
    static class StarvationExample {
        private final Lock unfairLock = new ReentrantLock();  // Unfair

        public void demonstrateStarvation() throws InterruptedException {
            System.out.println("=== STARVATION Example ===");

            Runnable task = () -> {
                for (int i = 0; i < 100; i++) {
                    unfairLock.lock();
                    try {
                        // Quick work
                    } finally {
                        unfairLock.unlock();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " completed");
            };

            // Start multiple threads competing for lock
            Thread t1 = new Thread(task, "Greedy-1");
            Thread t2 = new Thread(task, "Greedy-2");
            Thread t3 = new Thread(task, "Starved");  // May not get fair access

            t1.setPriority(Thread.MAX_PRIORITY);
            t2.setPriority(Thread.MAX_PRIORITY);
            t3.setPriority(Thread.MIN_PRIORITY);  // Low priority may starve

            t1.start();
            t2.start();
            Thread.sleep(10);  // Let high-priority threads get ahead
            t3.start();

            t1.join();
            t2.join();
            t3.join(2000);  // Wait max 2 seconds

            if (t3.isAlive()) {
                System.out.println("Thread 3 may be starving (still running)");
            }
            System.out.println();
        }
    }

    /**
     * Demonstrates STARVATION PREVENTION using fair lock
     */
    static class StarvationPrevention {
        private final Lock fairLock = new ReentrantLock(true);  // Fair!

        public void demonstratePreventStarvation() throws InterruptedException {
            System.out.println("=== STARVATION PREVENTION (Fair Lock) ===");

            Runnable task = () -> {
                for (int i = 0; i < 50; i++) {
                    fairLock.lock();
                    try {
                        // Work
                    } finally {
                        fairLock.unlock();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " completed");
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

            System.out.println("All threads completed - fair access guaranteed!");
            System.out.println();
        }
    }

    /**
     * Demonstrates LIVELOCK
     */
    static class LivelockExample {
        private final Lock lock1 = new ReentrantLock();
        private final Lock lock2 = new ReentrantLock();

        public void demonstrateLivelock() throws InterruptedException {
            System.out.println("=== LIVELOCK Example ===");
            System.out.println("Threads will keep trying but make no progress...");

            final boolean[] thread1Done = {false};
            final boolean[] thread2Done = {false};

            Thread thread1 = new Thread(() -> {
                int attempts = 0;
                while (attempts < 10) {  // Limit to 10 attempts for demo
                    if (lock1.tryLock()) {
                        try {
                            Thread.sleep(10);  // Small delay
                            if (lock2.tryLock()) {
                                try {
                                    System.out.println("Thread 1: SUCCESS!");
                                    thread1Done[0] = true;
                                    return;
                                } finally {
                                    lock2.unlock();
                                }
                            } else {
                                System.out.println("Thread 1: Couldn't get lock2, releasing lock1");
                            }
                        } catch (InterruptedException e) {
                        } finally {
                            lock1.unlock();
                        }
                    }
                    attempts++;
                    Thread.yield();
                }
                System.out.println("Thread 1: Gave up after " + attempts + " attempts");
            });

            Thread thread2 = new Thread(() -> {
                int attempts = 0;
                while (attempts < 10) {
                    if (lock2.tryLock()) {
                        try {
                            Thread.sleep(10);
                            if (lock1.tryLock()) {
                                try {
                                    System.out.println("Thread 2: SUCCESS!");
                                    thread2Done[0] = true;
                                    return;
                                } finally {
                                    lock1.unlock();
                                }
                            } else {
                                System.out.println("Thread 2: Couldn't get lock1, releasing lock2");
                            }
                        } catch (InterruptedException e) {
                        } finally {
                            lock2.unlock();
                        }
                    }
                    attempts++;
                    Thread.yield();
                }
                System.out.println("Thread 2: Gave up after " + attempts + " attempts");
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            if (!thread1Done[0] && !thread2Done[0]) {
                System.out.println("LIVELOCK! Both threads gave up without completing.");
            }
            System.out.println();
        }
    }

    /**
     * Demonstrates LIVELOCK PREVENTION using random backoff
     */
    static class LivelockPrevention {
        private final Lock lock1 = new ReentrantLock();
        private final Lock lock2 = new ReentrantLock();

        public void demonstratePreventLivelock() throws InterruptedException {
            System.out.println("=== LIVELOCK PREVENTION (Random Backoff) ===");

            Runnable task = () -> {
                while (true) {
                    if (lock1.tryLock()) {
                        try {
                            if (lock2.tryLock()) {
                                try {
                                    System.out.println(Thread.currentThread().getName() +
                                                     ": Acquired both locks!");
                                    return;
                                } finally {
                                    lock2.unlock();
                                }
                            }
                        } finally {
                            lock1.unlock();
                        }
                    }

                    // Random backoff prevents synchronize retries
                    try {
                        Thread.sleep((long) (Math.random() * 50));
                    } catch (InterruptedException e) {
                    }
                }
            };

            Thread t1 = new Thread(task, "Thread-1");
            Thread t2 = new Thread(task, "Thread-2");

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            System.out.println("Both threads completed - random backoff prevented livelock!");
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Deadlock
        new DeadlockExample().demonstrateDeadlock();
        Thread.sleep(500);

        new DeadlockPrevention().demonstratePreventDeadlock();

        // Starvation
        new StarvationExample().demonstrateStarvation();

        new StarvationPrevention().demonstratePreventStarvation();

        // Livelock
        new LivelockExample().demonstrateLivelock();

        new LivelockPrevention().demonstratePreventLivelock();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - LIVENESS ISSUES
 * ============================================================================
 *
 * THREE LIVENESS PROBLEMS:
 *
 * 1. DEADLOCK:
 * - Threads waiting for each other (circular wait)
 * - Thread state: BLOCKED or WAITING
 * - CPU usage: Low
 * - Prevention: Lock ordering, tryLock() with timeout, avoid nested locks
 *
 * 2. STARVATION:
 * - Thread perpetually denied resources
 * - Thread state: RUNNABLE (but never runs)
 * - CPU usage: Low (thread not scheduled)
 * - Causes: Unfair locks, thread priorities, long operations in critical section
 * - Prevention: Fair locks, avoid priorities, minimize lock hold time
 *
 * 3. LIVELOCK:
 * - Threads active but making no progress
 * - Thread state: RUNNABLE (actively working)
 * - CPU usage: High (constantly retrying)
 * - Like two people in hallway moving same direction
 * - Prevention: Random backoff, lock ordering, limit retries
 *
 * COMPARISON:
 * ┌─────────────┬──────────────┬────────────┬──────────────────────┐
 * │ Issue       │ Thread State │ CPU Usage  │ Key Characteristic   │
 * ├─────────────┼──────────────┼────────────┼──────────────────────┤
 * │ Deadlock    │ BLOCKED      │ Low        │ Circular wait        │
 * │ Starvation  │ RUNNABLE     │ Low        │ Perpetually denied   │
 * │ Livelock    │ RUNNABLE     │ High       │ Active, no progress  │
 * └─────────────┴──────────────┴────────────┴──────────────────────┘
 *
 * PREVENTION STRATEGIES:
 * - Lock ordering (deadlock, livelock)
 * - tryLock() with timeout (deadlock, livelock)
 * - Fair locks (starvation)
 * - Random backoff (livelock)
 * - Avoid thread priorities (starvation)
 * - Minimize critical sections (all)
 */
