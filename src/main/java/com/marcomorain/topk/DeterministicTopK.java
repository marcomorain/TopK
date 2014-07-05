package com.marcomorain.topk;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1.1 Deterministic algorithm
 *
 * The following algorithm estimates item frequencies fj within an additive
 * error of n/k using with O(k log n) memory,
 *
 * 1. Maintain set S of k counters, initialize to 0. For each element xi in
 * stream:
 *
 * 2. If xi ∈ S increment the counter for xi.
 *
 * 3. If xi 6∈ S add xi to S if space is available, else decrement all counters
 * in S.
 *
 * An item in S whose count falls to 0 can be removed, the space requirement for
 * storing k counters is k log n and the update time per item is O(k). The
 * algorithm estimates the count of an item as the value of its counter or zero
 * if it has no counter.
 *
 * Claim 1
 * The frequency estimate nj produced by the algorithm satisﬁes fj − n/k
 * ≤ nj ≤ fj.
 *
 * Proof:
 * Clearly, nj is less than the true frequency fj. Diﬀerences
 * between fj and the value of the estimate are caused by one of the two
 * scenarios: (i) The item j 6∈ S, each counter in S gets decremented, this is
 * the case when xj occurs in the stream but the counter for j is not
 * incremented. (ii) The counter for j gets decremented due to an element j 0
 * that is not contained in S.
 *
 * Both scenarios result in k counters getting
 * decremented hence they can occur at most n/k times, showing that nj ≥ fj −
 * n/k.
 *
 * @param <Type>
 */
public class DeterministicTopK<Type> implements TopK<Type> {

    private final Multiset<Type> counters;
    private final int k;
    private final static Logger log = LoggerFactory.getLogger(DeterministicTopK.class);

    public DeterministicTopK(int k) {
        this.k = k;
        this.counters = HashMultiset.create(k);
    }

    @Override
    public void add(Type item) {
        if (counters.contains(item) || counters.elementSet().size() < k) {
            counters.add(item);
        } else {
            log.info("Removing to make space for {}", item);
            Set<Type> remove = ImmutableSet.copyOf(counters.elementSet());

            for (Type existing : remove) {
                log.info("Removing {} ({}) ", existing, counters.count(remove));
                counters.remove(existing);
                log.info("Removed {} ({}) ", existing, counters.count(remove));
            }
        }
    }

    @Override
    public Iterable<Type> get() {
        return Multisets.copyHighestCountFirst(counters).elementSet();
    }
}
