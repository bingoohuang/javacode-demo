package org.n3r.sandbox.guava;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import org.junit.Test;

public class MultisetTest {
    @Test
    public void test() {
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("abc", 100);
        multiset.add("abc", 200);

        Multisets.copyHighestCountFirst(multiset);
    }
}
