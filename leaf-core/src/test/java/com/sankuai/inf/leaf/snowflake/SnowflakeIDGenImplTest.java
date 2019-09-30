package com.sankuai.inf.leaf.snowflake;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.UnsignedLong;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.PropertyFactory;
import com.sankuai.inf.leaf.common.Result;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SnowflakeIDGenImplTest {
    static IDGen idGen;
    UnsignedLong start = UnsignedLong.valueOf("10546443563816384736");
    final int times = 10_0000;

    @BeforeClass
    public static void init() {
        Properties properties = PropertyFactory.getProperties();

        idGen = new SnowflakeIDGenImpl(properties.getProperty("leaf.zk.list"), 8080);
    }
    @Test
    public void testGetId() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 1; i < times; ++i) {
            Result r = idGen.get("a");
            //to avoid not invoke
            System.out.println(r.getId());
        }
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
    }

    @Test
    public void testGetIdWithUnsigned() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 1; i < times; ++i) {

            Result r = idGen.get("a");
            UnsignedLong unsignedLong = UnsignedLong.valueOf(r.getId());
            UnsignedLong plus = unsignedLong.plus(start);
            System.out.println(plus.toString());
        }
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
    }
}
