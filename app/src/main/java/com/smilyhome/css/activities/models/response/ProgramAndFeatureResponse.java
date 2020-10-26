package com.smilyhome.css.activities.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProgramAndFeatureResponse extends CommonResponse {

    @SerializedName("programAndFeature")
    private List<ProgramAndFeatureItem> mProgramAndFeatureList = new ArrayList<>();

    public List<ProgramAndFeatureItem> getProgramAndFeatureList() {
        return mProgramAndFeatureList;
    }
}
