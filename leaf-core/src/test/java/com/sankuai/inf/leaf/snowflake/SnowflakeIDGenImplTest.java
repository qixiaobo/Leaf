package com.sankuai.inf.leaf.snowflake;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.UnsignedLong;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.PropertyFactory;
import com.sankuai.inf.leaf.common.Result;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SnowflakeIDGenImplTest {
    private static IDGen idGen;
    private BigInteger startBigInteger = BigInteger.valueOf(146 + 1).shiftLeft(56);
    private UnsignedLong start = UnsignedLong.valueOf(startBigInteger);
    private final int times = 100_0000;

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

    @Test
    public void testGetIdWithBigInteger() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 1; i < times; ++i) {

            Result r = idGen.get("a");
            BigInteger bigInteger = BigInteger.valueOf(r.getId());
            BigInteger plus = bigInteger.add(startBigInteger);
            System.out.println(plus.toString());
        }
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
    }

    @Test
    public void compute146ForSnowFlake() {
        System.out.println(snowFlakeYears(remainUnsignedLong(146)));
    }


    @Test
    public void computeServerIdForSnowFlake() {
        for (int i = 0; i < 255; i++) {
            System.out.println(i + " " + snowFlakeYears(remainUnsignedLong(i)));
        }

    }

    @Test
    public void compute199ForSnowFlake() {
        System.out.println(snowFlakeYears(remainUnsignedLong(199)));
    }

    private float snowFlakeYears(long maxNumber) {
        long mask = ((1L << 41) - 1) << 22;
        return ((maxNumber & mask) >> 22) / (1000L * 3600 * 24 * 365f);
    }

    private long remainUnsignedLong(int serverId) {

        BigInteger biggestServerIdUUid =
                new BigInteger(String.valueOf((serverId + 1) & 255)).shiftLeft(56).subtract(new BigInteger("1"));
        UnsignedLong remain = UnsignedLong.MAX_VALUE.minus(UnsignedLong.valueOf(biggestServerIdUUid));
        if (remain.compareTo(UnsignedLong.valueOf(Long.MAX_VALUE)) > 0) {
            return Long.MAX_VALUE;
        } else {
            return remain.longValue();
        }
    }
}
