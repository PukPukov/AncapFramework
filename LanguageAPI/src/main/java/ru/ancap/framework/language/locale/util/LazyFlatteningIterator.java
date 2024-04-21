package ru.ancap.framework.language.locale.util;


import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class LazyFlatteningIterator<E> implements Iterator<E> {
    
    private final Iterator<Supplier<@Nullable List<E>>> supplierIterator;
    private Iterator<E> currentIterator = null;
    
    @Override
    public boolean hasNext() {
        while ((this.currentIterator == null || !this.currentIterator.hasNext()) && this.supplierIterator.hasNext()) {
            @Nullable List<E> list = this.supplierIterator.next().get();
            if (list == null) this.currentIterator = EmptyIterator.emptyIterator();
            else this.currentIterator = list.iterator();
        }
        return this.currentIterator != null && this.currentIterator.hasNext();
    }
    
    @Override
    public E next() {
        if (hasNext()) return this.currentIterator.next();
        else throw new NoSuchElementException();
    }
    
}