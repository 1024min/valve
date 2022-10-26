package com.hiwei.valve.controller;

/**
 * 限流类型
 * @author zhanghaiwei
 */
public enum ValveType {
    /**
     * 排队等待
     */
    QUEUE_WAIT,
    /**
     * 直接拒绝
     */
    REJECT;
}
