package com.guijk.doctor.http;

import android.support.annotation.NonNull;
import android.support.annotation.Size;

import java.util.List;

/**
 * 结果列表(分页加载的数据)
 * Created by Lxx on 2017/3/17.
 */
public class ResultList<T> {
    private int mTotal; //总页面数
    private int mCurrentPage; //当前页
    private List<T> list;

    public ResultList(@Size(min = 0) int mTotal, @Size(min = 0) int mCurrentPage, @NonNull List<T> list) {
        this.mTotal = mTotal;
        this.mCurrentPage = mCurrentPage;
        this.list = list;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        this.mTotal = mTotal;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ResultList{" +
                "mTotal=" + mTotal +
                ", mCurrentPage=" + mCurrentPage +
                ", list=" + list +
                '}';
    }
}
