package com.janhen.seckill.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorUtilTest {

    @Test
    public void isMobile() {
    }

    @Test
    public void isMobileV2() {
    }

    public static void main(String[] args) {
		System.out.println( ValidatorUtil.isMobileV2("1515151515151515") );
		System.out.println( ValidatorUtil.isMobileV2("15258656236") );
	}
}