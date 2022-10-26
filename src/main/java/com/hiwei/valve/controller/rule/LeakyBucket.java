package com.hiwei.valve.controller.rule;

import com.hiwei.valve.controller.TrafficShapingController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶
 * @author
 */
public class LeakyBucket implements TrafficShapingController {

    private int count;
    private long maxQueueingTimeMs;
    private AtomicLong latestPassedTime = new AtomicLong(-1);

    public LeakyBucket(int count,long maxQueueingTimeMs) {
        this.maxQueueingTimeMs = maxQueueingTimeMs;
        this.count = count;
    }

    @Override
    public boolean canPass() {
        if(count<0||maxQueueingTimeMs<0){
            return true;
        }
        long currentTime = System.currentTimeMillis();
        //计算请求耗时
        long costTime = Math.round(1.0 / count * 1000);
        //如果在下个间隔 则直接放行
        if (latestPassedTime.get() + costTime < currentTime) {
            latestPassedTime.set(currentTime);
            return true;
        }
        //计算等待时间
        long waitTime = latestPassedTime.get() + costTime - System.currentTimeMillis();
        //如果时间>超时时间 限流
        if (waitTime > maxQueueingTimeMs) {
            return false;
        }
        if (waitTime <= 0) {
            latestPassedTime.set(System.currentTimeMillis());
            return true;
        }
        try {
            latestPassedTime.set(System.currentTimeMillis() + waitTime);
            //休眠
            Thread.sleep(waitTime);
            System.out.println("开始执行：" + System.currentTimeMillis());
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
