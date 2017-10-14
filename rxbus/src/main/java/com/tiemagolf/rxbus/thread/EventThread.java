package com.tiemagolf.rxbus.thread;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public enum EventThread {

    MAIN_THREAD,

    NEW_THREAD,

    IO,

    COMPUTATION,

    TRAMPOLINE,

    SINGLE,

    EXECUTOR;

    public static Scheduler getScheduler(EventThread thread) {
        Scheduler scheduler;
        switch (thread) {
            case MAIN_THREAD:
                scheduler = AndroidSchedulers.mainThread();
                break;
            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;
            case IO:
                scheduler = Schedulers.io();
                break;
            case COMPUTATION:
                scheduler = Schedulers.computation();
                break;
            case TRAMPOLINE:
                scheduler = Schedulers.trampoline();
                break;
            case SINGLE:
                scheduler = Schedulers.single();
                break;
            case EXECUTOR:
                scheduler = Schedulers.from(ThreadHandler.DEFAULT.getExecutor());
                break;
            default:
                scheduler = AndroidSchedulers.mainThread();
                break;
        }
        return scheduler;
    }
}
