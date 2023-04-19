package com.example.db.lock;

import java.util.concurrent.ConcurrentHashMap;

public class Lock {
    private static ConcurrentHashMap<String,Object> locks;
    private static Lock instance = null;

    private Lock(){
        locks = new ConcurrentHashMap<>();
    }

    public static Lock getInstance(){
        if(instance == null)
            instance = new Lock();
        return instance;
    }

    public Object getLock(String lockName){
        locks.putIfAbsent(lockName,new Object());
        return locks.get(lockName);
    }

    private void initLock(String lockName){
        Object lock = new Object();
        locks.put(lockName,lock);
    }

}
