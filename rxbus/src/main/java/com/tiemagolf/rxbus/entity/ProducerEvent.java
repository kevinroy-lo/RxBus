package com.tiemagolf.rxbus.entity;


import com.tiemagolf.rxbus.thread.EventThread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


public class ProducerEvent extends Event {

    /**
     * Object sporting the producer method.
     */
    private final Object target;
    /**
     * Producer method.
     */
    private final Method method;
    /**
     * Producer thread
     */
    private final EventThread thread;
    /**
     * Object hash code.
     */
    private final int hashCode;
    /**
     * Should this producer produce events
     */
    private boolean valid = true;

    public ProducerEvent(Object target, Method method, EventThread thread) {
        if (target == null) {
            throw new NullPointerException("EventProducer target cannot be null.");
        }
        if (method == null) {
            throw new NullPointerException("EventProducer method cannot be null.");
        }

        this.target = target;
        this.thread = thread;
        this.method = method;
        method.setAccessible(true);

        // Compute hash code eagerly since we know it will be used frequently and we cannot estimate the runtime of the
        // target's hashCode call.
        final int prime = 31;
        hashCode = (prime + method.hashCode()) * prime + target.hashCode();
    }

    public boolean isValid() {
        return valid;
    }

    public void invalidate() {
        valid = false;
    }

    public Observable<Object> produce() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                e.onNext(produceEvent());
                e.onComplete();
            }
        }).subscribeOn(EventThread.getScheduler(thread));
    }

    private Object produceEvent() throws InvocationTargetException {
        if (!valid) {
            throw new IllegalStateException(toString() + " has been invalidated and can no longer produce events.");
        }
        try {
            return method.invoke(target);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            throw e;
        }
    }

    @Override
    public String toString() {
        return "[EventProducer " + method + "]";
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProducerEvent other = (ProducerEvent) obj;
        return method.equals(other.method) && target == other.target;
    }

    public Object getTarget() {
        return target;
    }
}
