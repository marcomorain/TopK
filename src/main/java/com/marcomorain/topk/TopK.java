package com.marcomorain.topk;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;


public class TopK<T> {

    private final Multiset<T> counters;
    private final int k;

    public TopK(int k) {
        this.k = k;
        this.counters = HashMultiset.create();
    }

    public void add(T item) {
        counters.add(item);
    }

    public Iterable<T> get() {
        return Iterables.limit(Multisets.copyHighestCountFirst(counters).elementSet(), k);
    }

    @Override
    public String toString() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this).add("k", k);
        for (T item : get()) {
            helper.addValue(item);
        }
        return helper.toString();
    }
}