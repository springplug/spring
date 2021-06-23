package com.springplug.data.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Page<T> {

    @NotNull(message = "当前页数不能为空")
    @JsonIgnore
    private Integer current;

    @NotNull(message = "查询条数不能为空")
    @JsonIgnore
    private Integer size;

    private Long total;

    private Integer pages;

    private List<T> records;

    /** 正序 */
    @JsonIgnore
    private List<String> ascs;
    /** 倒序*/
    @JsonIgnore
    private List<String> descs;

    public Page(){
    }

    public Page(int current, int size){
        this.size=size;
        this.current=current;
    }

    public Page(List<T> records, int current, int size){
        this.records=records;
        this.total=(long)records.size();
        this.size=size;
        this.current=current;
    }

    public Page(List<T> records, int current, int size, Long total){
        this.records=records;
        this.total=total;
        this.size=size;
        this.current=current;
        this.pages=(int)Math.ceil((double)total/size);
    }

}
