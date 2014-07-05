package com.marcomorain.topk;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TopKTest {

    @Test
    public void testText() throws IOException {

        final int k = 200;

        final ReferenceTopK<String> ref = new ReferenceTopK<>(k);
        final DeterministicTopK<String> det = new DeterministicTopK<>(k);
        final Pattern pattern = Pattern.compile("[\\p{Punct}\\p{Space}]");

        Resources.readLines(Resources.getResource("alice.txt"), Charsets.UTF_8, new LineProcessor<Void>() {

            @Override
            public boolean processLine(String line) throws IOException {
                Scanner scanner = new Scanner(line).useDelimiter(pattern);
                while (scanner.hasNext()) {
                    String item = scanner.next().toLowerCase();
                    ref.add(item);
                    det.add(item);
                }
                return true;
            }

            @Override
            public Void getResult() {
                return null;
            }
        });
        Iterable<String> a = det.get();
        Iterable<String> b = ref.get();

        System.out.format("Ref: %s%n", ref);
        System.out.format("Det: %s%n", det);

        List<String> detSet = ImmutableList.copyOf(Iterables.limit(det.get(), 20));
        List<String> refSet = ImmutableList.copyOf(Iterables.limit(ref.get(), 20));
        assertThat(detSet, equalTo(refSet));
    }

}
