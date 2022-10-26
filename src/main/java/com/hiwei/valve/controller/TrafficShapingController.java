package com.hiwei.valve.controller;

/**
 * 流量控制器
 * @author
 */
public interface TrafficShapingController {
    boolean canPass();
}