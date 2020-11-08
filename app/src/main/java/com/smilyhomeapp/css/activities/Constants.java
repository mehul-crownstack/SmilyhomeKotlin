package com.smilyhomeapp.css.activities;

import com.smilyhomeapp.css.activities.models.response.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String SHARED_PREF_NAME = "SHARED_PREF_SMILYHOME";
    public static final int SPLASH_TIME_INTERVAL = 2500;
    public static final String SUCCESS = "0000";
    public static final String USER_ID = "USER_ID";
    public static final String SURVEY_URL = "https://smilyhome.in/program_nd_features/survey.php";
    public static final int PERMISSION_REQUEST_CODE = 1001;
    private static List<CategoryItem> sCategoryList = new ArrayList<>();

    private static String sSMSHashString = "";

    public static String getSMSHashString() {
        return sSMSHashString;
    }

    public static void setSMSHashString(String SMSHashString) {
        sSMSHashString = SMSHashString;
    }

    public static List<CategoryItem> getCategoryList() {
        return sCategoryList;
    }

    public static void setCategoryList(List<CategoryItem> categoryList) {
        sCategoryList = categoryList;
    }

    public interface HomeScreenProductMode {
        int FEATURED = 1;
        int TOP_HOT_DEAL = 2;
        int TRENDING = 3;
        int AAJ_KA_OFFER = 4;
        int SUPER_SAVER = 5;
        int WHAT_S_NEW = 6;
    }

    public interface BottomSheetMode {
        String PROFILE = "PROFILE";
        String MENU = "MENU";
        String CATEGORY = "CATEGORY";
    }

    public interface ModeOfPayment {
        String COD = "COD";
        String ONLINE = "ONLINE";
    }

    public interface InfoConstants {

        String MODE_TERM_AND_CONDITION = "MODE_TERM_AND_CONDITION";
        String MODE_CONTACT_US = "MODE_CONTACT_US";
    }
}
