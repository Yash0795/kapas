package com.kapas.workorder.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MetaDataField {

    public MetaDataField(String name, String type, boolean isRequired){
        this.name = name;
        this.type = type;
        this.isRequired = isRequired;
    }

    String name;

    String type;

    boolean isRequired;

    String defaultValue;
}
