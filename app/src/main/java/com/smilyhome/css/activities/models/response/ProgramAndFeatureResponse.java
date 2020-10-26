package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProgramAndFeatureResponse extends CommonResponse {

    @SerializedName("pageDisplay")
    private String pageDisplay;

    public String getPageDisplay() {
        return pageDisplay;
    }

    @SerializedName("programAndFeature")
    private List<ProgramAndFeatureItem> mProgramAndFeatureList = new ArrayList<>();

    public List<ProgramAndFeatureItem> getProgramAndFeatureList() {
        return mProgramAndFeatureList;
    }
}
