package com.springplug.web.flux.base.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
public class CreateAndUpdateBean extends BaseBean {

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @CreatedBy
    private Long createBy;

    @LastModifiedBy
    private Long updateBy;
}
