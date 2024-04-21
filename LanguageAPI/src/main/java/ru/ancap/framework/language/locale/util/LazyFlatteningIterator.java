package ru.ancap.framework.language.locale.util;


import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class LazyFlatteningIterator<E> implements Iterator<E> {
    
    private final Iterator<Supplier<List<E>>> supplierIterator;
    private Iterator<E> currentIterator = null;
    
    @Override
    public boolean hasNext() {
        while ((this.currentIterator == null || !this.currentIterator.hasNext()) && this.supplierIterator.hasNext()) {
            this.currentIterator = this.supplierIterator.next().get().iterator();
        }
        return this.currentIterator != null && this.currentIterator.hasNext();
    }
    
    @Override
    public E next() {
        if (hasNext()) {
            return this.currentIterator.next();
        } else {
            throw new NoSuchElementException();
        }
    }
    
}