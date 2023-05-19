package com.learning.layoutsdk.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName JsonProviderMetaType
 * @Description TODO
 * @Author hufei
 * @Date 2021/9/27 14:24
 * @Version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonProviderMetaType {
    private String id;
    private String type;
    private String format;//int、double
    private String refId;//ref
    @JsonProperty("enum")
    private JsonProviderEnumValue[] enums;//enum
    private JsonProviderMetaType items;//array
    private JsonProviderMetaType[] types;//types
    private JsonProviderMetaType[] properties;//object
    private JsonProviderMetaType[] fields;//rule
    private Object value;//value
}
