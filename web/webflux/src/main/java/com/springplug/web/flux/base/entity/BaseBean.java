package com.springplug.web.flux.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Data
public class BaseBean implements Persistable {

    @Transient
    @JsonIgnore
    private boolean isNew=true;

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
