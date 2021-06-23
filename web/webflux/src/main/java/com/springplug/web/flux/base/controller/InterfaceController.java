package com.springplug.web.flux.base.controller;

import com.springplug.common.util.json.JsonUtil;
import com.springplug.web.flux.domain.AjaxResult;
import com.springplug.web.flux.autoconfigure.SysProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${spring.application.name}")
public class InterfaceController {

    @Autowired
    SysProperties sysProperties;

    @GetMapping("myInterface")
    public Mono<AjaxResult> getInterface(){
        return AjaxResult.success(JsonUtil.toMap(sysProperties.getInfo()));
    }

}
