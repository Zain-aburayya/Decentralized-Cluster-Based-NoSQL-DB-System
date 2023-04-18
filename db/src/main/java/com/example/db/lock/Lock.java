package com.example.db.lock;

import java.util.HashMap;

public class Lock {
    private static HashMap<String,Object> locks;
    private static Lock instance = null;

    private Lock(){
        locks = new HashMap<>();
    }

    public static Lock getInstance(){
        if(instance == null)
            instance = new Lock();
        return instance;
    }

    public Object getLock(String lockName){
        if(!locks.containsKey(lockName))
            initLock(lockName);
        return locks.get(lockName);
    }

    private void initLock(String lockName){
        Object lock = new Object();
        locks.put(lockName,lock);
    }

}
