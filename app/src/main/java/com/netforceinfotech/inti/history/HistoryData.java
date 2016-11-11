package com.netforceinfotech.inti.history;

import java.util.HashSet;

/**
 * Created by Netforce on 11/8/2016.
 */

public class HistoryData {
    public String id, date, event, remark, status;

    public HistoryData(String id, String date, String even, String remark, String status) {
        this.id = id;
        this.date = date;
        this.event = even;
        this.remark = remark;
        this.status = status;
    }

}
