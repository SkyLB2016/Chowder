package com.sky.chowder.api.view;

import com.sky.api.IBaseView;
import com.sky.chowder.model.ActivityModel;

import java.util.List;

/**
 * Created by SKY on 2017/5/29.
 */
public interface IMainView extends IBaseView {

    void setData(List<ActivityModel> data);

}