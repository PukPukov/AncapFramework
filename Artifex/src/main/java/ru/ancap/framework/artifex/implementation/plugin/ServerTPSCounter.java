package ru.ancap.framework.artifex.implementation.plugin;

import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

public class ServerTPSCounter implements Runnable {
    
    private final int thinning;
    
    private transient long last;
    private final CyclicDeque<Double> history = new CyclicDeque<>(20);

    public ServerTPSCounter(int thinning) {
        this.thinning = thinning;
        this.last = System.currentTimeMillis();
        this.history.addLast(20D);
    }

    @Override
    public void run() {
        final long current = System.currentTimeMillis();
        long timeSpent = (current - this.last) / 1000L;
        if (timeSpent == 0L) timeSpent = 1L;
        final double tps = (1000000D / timeSpent) * this.thinning;
        if (tps <= 22.0) this.history.addLast(tps);
        this.last = current;
    }

    public double get() {
        return this.history.getLast();
    }
    
    @RequiredArgsConstructor
    private static class CyclicDeque<T> {
        
        private final LinkedList<T> base = new LinkedList<>();
        private final int size;
        
        public void addLast(T t) {
            if (this.base.size() >= this.size) this.base.removeFirst();
            this.base.addLast(t);
        }
        
        public T getLast() {
            return this.base.getLast();
        }
        
        public List<T> getAll() {
            return this.base;
        }
        
    }
    
}
