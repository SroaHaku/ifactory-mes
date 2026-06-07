package com.mes.service.impl;

import cn.hutool.json.JSONObject;
import com.mes.mapper.CommonMapper;
import com.mes.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UtilServiceImpl implements UtilService {

    @Value("${db.black-table.list:}")
    private String blackTableStr;
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public void SaveDataIntoDB(String tableName, JSONObject jsonObject) {
        Set<String> blackTableSet = new HashSet<>(Arrays.asList(blackTableStr.split(",")));
        // 命中黑名单直接抛异常拦截
        if (blackTableSet.contains(tableName.toLowerCase())) {
            throw new RuntimeException("【禁止操作】当前数据表[" + tableName + "]在黑名单，不允许插入数据");
        }

        Map<String, Object> dataMap = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            dataMap.put(key, jsonObject.get(key));
        }
        if (dataMap.isEmpty()) {
            return;
        }
        commonMapper.insertByMap(tableName, dataMap);
    }
}
