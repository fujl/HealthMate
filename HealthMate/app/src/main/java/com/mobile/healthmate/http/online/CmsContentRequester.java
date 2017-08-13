package com.mobile.healthmate.http.online;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mobile.healthmate.app.lib.json.JsonHelper;
import com.mobile.healthmate.http.OnHttpCodeListener;
import com.mobile.healthmate.http.ResultList;
import com.mobile.healthmate.http.lib.SimpleRequester;
import com.mobile.healthmate.model.online.CmsContentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/13.
 */

public class CmsContentRequester extends SimpleRequester<ResultList<CmsContentModel>> {

    public CmsContentRequester( OnHttpCodeListener<ResultList<CmsContentModel>> onHttpResponseListener) {
        super(onHttpResponseListener);
    }
    @Override
    public ResultList<CmsContentModel> onDumpData(JSONObject jsonObject) throws JSONException {
        JSONObject errData = jsonObject.getJSONObject("errData");
        JSONArray jsonArray = errData.getJSONArray("rows");
        int total = errData.getInt("total");
        int currentPage = errData.getInt("pageSize");
        List<CmsContentModel> list = JsonHelper.toList(jsonArray, CmsContentModel.class);
        return new ResultList<>(total, currentPage, list);
    }

    @Override
    protected void onPutParams(@NonNull Map<String, String> params) {
        params.put("channelId", 2 + "");
    }

    @NonNull
    @Override
    public String getChildrenUrl() {
        return "api/cms/content/search.do";
    }
}
