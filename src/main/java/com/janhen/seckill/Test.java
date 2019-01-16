package com.janhen.seckill;

import java.util.HashSet;
import java.util.Set;

public class Test {

    public static void main(String[] args) {

        Set<Integer> set = new HashSet<>();
        final int count = 1351;
        final int beginNum = 10000;
        final int endNum = 11500;
        for (int i = 0; i < count; i ++) {
            int val = beginNum + (int)((endNum - beginNum) * Math.random());
            while (set.contains(val)) {
                val = beginNum + (int)((endNum - beginNum) * Math.random());
            }
            set.add(val);
            System.out.println(val);
        }
        System.out.println("count: "  + set.size());
    }

}
