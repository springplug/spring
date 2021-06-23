package com.springplug.push.gt.client.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PushResult {

    private int id;

    private String action;

    private String parameter;

    private String result;

    private int resultCode;

    private Date createTime;

}