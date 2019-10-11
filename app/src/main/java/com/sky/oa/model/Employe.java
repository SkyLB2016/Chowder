package com.sky.oa.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by libin on 2019/09/06 21:49 Friday.
 */
public class Employe {
    private String userId;//":2,
    private String name;//":"",
    private String sn;//":"",
    private String dept;//":"",
    private String mobile;//":"",
    private long date;//":1567587143
    private String dateTitle;//":1567587143

    public Employe(String name, String sn, String dept, String mobile, String dateTitle) {
        this.name = name;
        this.sn = sn;
        this.dept = dept;
        this.mobile = mobile;
        this.dateTitle = dateTitle;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSn() {
        return sn;
    }

    public String getDept() {
        return dept;
    }

    public String getMobile() {
        return mobile;
    }


    public String getDate() {
        if (date == 0) return dateTitle;
        return new SimpleDateFormat("MM-dd", Locale.CHINA).format(new Date(date));
    }
}
