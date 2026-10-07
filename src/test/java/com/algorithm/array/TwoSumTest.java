package com.algorithm.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * TwoSum 单元测试
 */
class TwoSumTest {

    @Test
    void twoSum() {
        TwoSum solution = new TwoSum();
        // 常规情况
        assertArrayEquals(new int[]{0, 1}, solution.twoSum(new int[]{2, 7, 11, 15}, 9));
        // 无解情况
        assertArrayEquals(new int[0], solution.twoSum(new int[]{1, 2, 3}, 100));
    }
}
