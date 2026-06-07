package com.mes.service;

import cn.hutool.json.JSONObject;

public interface UtilService{

    void SaveDataIntoDB(String tableName, JSONObject jsonObject);
}
