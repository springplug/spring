package com.springplug.web.mvc.base.controller;

import com.springplug.common.util.json.JsonUtil;
import com.springplug.web.mvc.autoconfigure.SysProperties;
import com.springplug.web.mvc.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${spring.application.name}")
public class InterfaceController {

    @Autowired
    SysProperties sysProperties;

    @GetMapping("myInterface")
    public AjaxResult getInterface(){
        return AjaxResult.success(JsonUtil.toMap(sysProperties.getInfo()));
    }

}
