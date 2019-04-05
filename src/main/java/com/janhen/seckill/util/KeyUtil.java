package com.janhen.seckill.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Random;
import java.util.UUID;

public class KeyUtil {

    private static char[] ops = new char[] {'+', '-', '*'};

    public static String geneSeckillPath() {
        return MD5Util.md5(uuid()) + System.currentTimeMillis() % 1000;
    }

    public static String geneVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String geneToken() {
        return uuid();
    }

    static long i = 0;
    static long begin = 13000000000L;

    public static Long geneUserId() {
        return Long.parseLong("" + begin + (long) (100000 + Math.random() * 900000) + (i ++));
    }
}
