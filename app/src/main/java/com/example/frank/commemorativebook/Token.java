package com.example.frank.commemorativebook;

import android.app.Activity;


import java.util.Map;

/**
 * File description.
 *
 * @author Frank
 * @date 2017/12/19
 * @emial 1320259466@qq.com
 * @description (about file's use)
 */

public class Token {
    private static String TOKEN="";



    public static String getTOKEN(Activity activity){
        Map<String, String> map1= LoginService.getSaveToken(activity);
        if (map1!=null){
            TOKEN=map1.get("token");
        }
        return TOKEN;
    }



}
