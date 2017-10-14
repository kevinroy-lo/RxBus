package com.tiemagolf.rxbus.thread;

import android.os.Looper;

import com.tiemagolf.rxbus.Bus;


/**
 * Enforces a thread confinement policy for methods on a particular event bus.
 */
public interface ThreadEnforcer {

    void enforce(Bus bus);

    ThreadEnforcer ANY = new ThreadEnforcer() {
        @Override
        public void enforce(Bus bus) {
            // Allow any thread.
        }
    };

    ThreadEnforcer MAIN = new ThreadEnforcer() {
        @Override
        public void enforce(Bus bus) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("Event bus " + bus + " accessed from non-main thread " + Looper.myLooper());
            }
        }
    };

}
