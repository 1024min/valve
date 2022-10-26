package com.hiwei.valve.controller.rule;

import com.hiwei.valve.controller.TrafficShapingController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口
 * @author
 */
public class SlidingWindow implements TrafficShapingController {
    private AtomicInteger[] windows = {new AtomicInteger(0),new AtomicInteger(0),new AtomicInteger(0)
            ,new AtomicInteger(0),new AtomicInteger(0),new AtomicInteger(0),new AtomicInteger(0)
            ,new AtomicInteger(0),new AtomicInteger(0),new AtomicInteger(0)};
    private Long oldTimestamp = System.currentTimeMillis();
    private int threshold;

    public SlidingWindow(int threshold){
        this.threshold = threshold;
    }

    public boolean canPass() {
        if(threshold<0){
            return true;
        }
        Long newTimestamp = System.currentTimeMillis();
        int index = ((Long)(newTimestamp % 1000 /100)).intValue();
        //同窗口
        if (oldTimestamp / 100 == newTimestamp / 100) {
            if(checkSum(threshold,newTimestamp)){
                return false;
            }
            windows[index].incrementAndGet();
        } else if (newTimestamp / 1000 - oldTimestamp / 1000 > 1) { //与上次间隔超过1秒
            //全部归零
            for (AtomicInteger window : windows) {
                window.set(0);
            }
            if(checkSum(threshold,newTimestamp)){
                return false;
            }
            windows[index].incrementAndGet();
        } else {//间隔不超过1秒
            //计算归零单元
            Long gap = newTimestamp / 100 - oldTimestamp / 100;
            Long indexS = oldTimestamp % 1000 / 100;
            for (Long i = indexS+1;i<indexS+gap;i++){
                int inx = ((Long) (i % 10)).intValue();
                windows[inx].set(0);
            }
            if(checkSum(threshold,newTimestamp)){
                return false;
            }
            int setInx = ((Long) (newTimestamp % 1000 / 100)).intValue();
            windows[setInx].set(1);
        }
        return true;
    }

    private boolean checkSum(int threshold,Long newTimestamp){
        oldTimestamp = newTimestamp;
        int sum = 1;
        for (AtomicInteger window : windows) {
            sum += window.get();
        }
        if (sum > threshold) {
            return true;
        }
        return false;
    }
}
