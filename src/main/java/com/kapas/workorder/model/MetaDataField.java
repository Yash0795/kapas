package com.kapas.workorder.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MetaDataField {

    private String name;

    private String type;

    private boolean isRequired;
}
