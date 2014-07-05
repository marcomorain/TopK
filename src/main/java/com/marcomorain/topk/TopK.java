package com.marcomorain.topk;

public interface TopK<Type> {
    public void add(Type item);
    public Iterable<Type> get();
}
