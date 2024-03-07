package com.restaurant.menuservice.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Scope("singleton")
public class UUIDGenerator {
    private final Lock lock = new ReentrantLock();

    public String generateUUIDString() {
        lock.lock();
        try {
            return UUID.randomUUID().toString();
        } finally {
            lock.unlock();
        }
    }
}





