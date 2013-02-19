/*
 * Range.java
 *
 * Copyright (c) 2013 Mike Strobel
 *
 * This source code is subject to terms and conditions of the Apache License, Version 2.0.
 * A copy of the license can be found in the License.html file at the root of this distribution.
 * By using this source code in any fashion, you are agreeing to be bound by the terms of the
 * Apache License, Version 2.0.
 *
 * You must not remove this notice, or any other, from this software.
 */

package com.strobel.decompiler.ast;

import com.strobel.core.VerifyArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author mstrobel
 */
public final class Range {
    private int _start;
    private int _end;

    public Range() {
    }

    public Range(final int start, final int end) {
        _start = start;
        _end = end;
    }

    public final int getStart() {
        return _start;
    }

    public final void setStart(final int start) {
        _start = start;
    }

    public final int getEnd() {
        return _end;
    }

    public final void setEnd(final int end) {
        _end = end;
    }

    public static List<Range> orderAndJoint(final Iterable<Range> input) {
        VerifyArgument.notNull(input, "input");

        final ArrayList<Range> ranges = new ArrayList<>();

        for (final Range range : input) {
            if (range != null) {
                ranges.add(range);
            }
        }

        Collections.sort(
            ranges,
            new Comparator<Range>() {
                @Override
                public int compare(final Range o1, final Range o2) {
                    return Integer.compare(o1._start, o2._start);
                }
            }
        );

        for (int i = 0; i < ranges.size() - 1; ) {
            final Range current = ranges.get(i);
            final Range next = ranges.get(i + 1);

            //
            // Merge consecutive ranges if they intersect.
            //
            if (current.getStart() <= next.getStart() &&
                next.getStart() <= current.getEnd()) {

                current.setEnd(Math.max(current.getEnd(), next.getEnd()));
                ranges.remove(i + 1);
            }
            else {
                ++i;
            }
        }

        return ranges;
    }

    public static List<Range> invert(final Iterable<Range> input, final int codeSize) {
        VerifyArgument.notNull(input, "input");
        VerifyArgument.isPositive(codeSize, "codeSize");

        final List<Range> ordered = orderAndJoint(input);

        if (ordered.isEmpty()) {
            return Collections.singletonList(new Range(0, codeSize));
        }

        final List<Range> inverted = new ArrayList<>();

        //
        // Gap before the first element...
        //
        if (ordered.get(0).getStart() != 0) {
            inverted.add(new Range(0, ordered.get(0).getStart()));
        }

        //
        // Gap between elements...
        //
        for (int i = 0; i < ordered.size() - 1; i++) {
            inverted.add(
                new Range(
                    ordered.get(i).getEnd(),
                    ordered.get(i + 1).getStart()
                )
            );
        }

        assert ordered.get(ordered.size() - 1).getEnd() <= codeSize;

        //
        // Gap after the last element...
        //
        if (ordered.get(ordered.size() - 1).getEnd() != codeSize) {
            inverted.add(
                new Range(
                    ordered.get(ordered.size() - 1).getEnd(),
                    codeSize
                )
            );
        }

        return inverted;
    }
}