package com.lulala.jfk.thread;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 验证 concurrencetHashMap 的线程安全
 * @author shenjh
 * @version 1.0
 * @since 2024-11-05 10:57
 */
@Slf4j
public class ConcurrentHashMapExample {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        Map<String, Integer> map = new HashMap<>();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                concurrentHashMap.put("Key" + i, i);
                // put 时计算的 size 线程不安全
                map.put("Key" + i, i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                concurrentHashMap.put("Key" + i, i);
                map.put("Key" + i, i);
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ConcurrentHashMap size: 1000
        System.out.println("ConcurrentHashMap size: " + concurrentHashMap.size());
        // Map size: 1048 ，实际 key 的数量还是 1000 个
        System.out.println("Map size: " + map.size());
        System.out.println(map);

        Map<String, Integer> unionMap = new HashMap<>();
        int i = 0;
        for (String key : map.keySet()) {
            i++;
            if (unionMap.get(key) != null) {
                System.out.println(key + ":::" + map.get(key));
            } else {
                unionMap.put(key, 1);
            }
        }
        // i: 1000
        System.out.println("i: " + i);
        // Map size2: 1048
        System.out.println("Map size2: " + map.size());
    }
}
