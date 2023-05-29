package com.learning.layoutsdk.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learning.layoutsdk.component.annotation.LayoutMethodTag;
import com.learning.layoutsdk.component.definition.JsonProviderEnumValue;
import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.RuleDict;
import com.learning.layoutsdk.enums.TypeDict;
import com.learning.utils.CollectionUtil;
import com.learning.utils.TextUtil;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @InterfaceName NumberComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:41
 * @Version 1.0
 */
public abstract class EnumComponent implements LayoutComponent {

    @Data
    private static class KVEntity {

        private String value;
        private Object key;

        KVEntity(Object key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class HttpConfig {
        private String url;
        private String method;
        private String body;
        private String key;
        private String value;
        private String watch;
        @JsonProperty("isCascader")
        private boolean cascade;
        @JsonProperty("isGroup")
        private boolean group;
        @JsonProperty("isTreeSelect")
        private boolean tree;
    }

    private int maxSelectCount = -1;
    private int maxTagCount = -1;
    private int maxTagTextLength = -1;
    private boolean showValue = false;
    private HttpConfig httpConfig = null;
    private LinkedHashMap<String, Object> values = new LinkedHashMap<>();
    private LinkedHashMap<String, Object> defaultValues = new LinkedHashMap<>();
    private LinkedHashMap<String, Object> defaultKeyValues = new LinkedHashMap<>();
    private Map<String, Object[]> paramMap = new HashMap<>();

    @LayoutMethodTag
    public void setMaxSelectCount(int max) {
        maxSelectCount = max;
        paramMap.put("setMaxSelectCount", new Object[]{max});
    }

    @LayoutMethodTag
    public void setMaxTagCount(int max) {
        maxTagCount = max;
        paramMap.put("setMaxTagCount", new Object[]{max});
    }

    @LayoutMethodTag
    public void setMaxTagTextLength(int max) {
        maxTagTextLength = max;
        paramMap.put("setMaxTagTextLength", new Object[]{max});
    }

    @LayoutMethodTag
    public void setShowValue() {
        showValue = true;
        paramMap.put("setShowValue", new Object[0]);
    }

    public void clearEnumItem() {
        values = new LinkedHashMap<>();
        paramMap.remove("batchAppendEnumItem");
    }

    @LayoutMethodTag
    public void appendEnumItem(String... val) {
        for (String item : val) {
            values.put(item, item);
        }
        paramMap.put("batchAppendEnumItem", new Object[]{TextUtil.fromJson(values)});
    }

    @LayoutMethodTag
    public void appendKVEnumItem(String name, Object val) {
        Assert.notNull(val, "enum key can not be null");
        values.put(name, val);
        paramMap.put("batchAppendEnumItem", new Object[]{TextUtil.fromJson(values)});
    }

    @LayoutMethodTag
    public void batchAppendEnumItem(String val) {
        LinkedHashMap value = TextUtil.parseJson(val, LinkedHashMap.class);
        if (null != value) {
            for (Object name : value.keySet()) {
                values.put(name.toString(), value.get(name));
            }
        }
        paramMap.put("batchAppendEnumItem", new Object[]{TextUtil.fromJson(values)});
    }

    @LayoutMethodTag
    public void appendDefaultEnumItem(String... val) {
        for (String item : val) {
            defaultValues.put(item, item);
        }
        paramMap.put("batchAppendDefaultEnumItem", new Object[]{TextUtil.fromJson(defaultValues)});
    }

    @LayoutMethodTag
    public void batchAppendDefaultEnumItem(String val) {
        LinkedHashMap value = TextUtil.parseJson(val, LinkedHashMap.class);
        if (null != value) {
            for (Object name : value.keySet()) {
                defaultKeyValues.put(name.toString(), value.get(name));
            }
        }
        paramMap.put("batchAppendDefaultEnumItem", new Object[]{TextUtil.fromJson(defaultKeyValues)});
    }

    public LinkedHashMap<String, Object> getValues() {
        return values;
    }

    public LinkedHashMap<String, Object> getDefaultValues() {
        return defaultValues.isEmpty() ? defaultKeyValues : defaultValues;
    }

    @LayoutMethodTag
    public void appendEnumItemOutsideWithGet(String url, String keyName, String valueName, boolean isCascade, boolean isGroup, boolean isTree) {
        httpConfig = new HttpConfig();
        httpConfig.setUrl(url);
        httpConfig.setKey(keyName);
        httpConfig.setValue(valueName);
        httpConfig.setCascade(isCascade);
        httpConfig.setGroup(isGroup);
        httpConfig.setTree(isTree);
        paramMap.put("appendEnumItemOutsideWithGet", new Object[]{url, keyName, valueName, isCascade, isGroup, isTree});
    }

    @LayoutMethodTag
    public void appendEnumItemOutsideWithPost(String url, Object body, String keyName, String valueName, String[] watch, boolean isCascade, boolean isGroup, boolean isTree) {
        httpConfig = new HttpConfig();
        httpConfig.setUrl(url);
        httpConfig.setMethod("post");
        httpConfig.setBody(TextUtil.fromJson(body));
        httpConfig.setKey(keyName);
        httpConfig.setValue(valueName);
        httpConfig.setWatch(null == watch || watch.length == 0 ? null : CollectionUtil.join(watch, ","));
        httpConfig.setCascade(isCascade);
        httpConfig.setGroup(isGroup);
        httpConfig.setTree(isTree);
        paramMap.put("appendEnumItemOutsideWithPost", new Object[]{url, body, keyName, valueName, watch, isCascade, isGroup, isTree});
    }

    public Map<String, Object[]> getParamMap() {
        return paramMap;
    }

    public abstract LayoutCommonConfig getCommonConfig();

    public abstract boolean isMultiple();

    public boolean isCheckbox() {
        return false;
    }

    @Override
    public JsonProviderMetaType toSchema() {

        Assert.isTrue(null != httpConfig || !values.isEmpty(), "enum can not be empty");

        List<JsonProviderEnumValue> enumValueList = new ArrayList<>();
        List<KVEntity> enumKeyValueList = new ArrayList<>();
        for (Map.Entry<String, Object> value : values.entrySet()) {
            if (!value.getKey().equals(value.getValue().toString())) {
                enumKeyValueList.add(new KVEntity(value.getValue(), value.getKey()));
            } else {
                JsonProviderEnumValue item = new JsonProviderEnumValue();
                item.setValue(value.getKey());
                enumValueList.add(item);
            }
        }

        if (!enumValueList.isEmpty() && !enumKeyValueList.isEmpty()) {
            for (JsonProviderEnumValue item : enumValueList) {
                enumKeyValueList.add(new KVEntity(item.getValue(), item.getValue()));
            }
            enumValueList.clear();
        }

        Map<String, Object> enumVal = new HashMap<>();
        if (!enumKeyValueList.isEmpty()) {
            enumVal.put("value", enumKeyValueList);
        }
        boolean multiple = isMultiple();
        if (multiple) {
            enumVal.put("isMutiple", true);
        }
        boolean checkbox = isCheckbox();
        if (checkbox) {
            enumVal.put("type", "checkbox");
        }
        if (maxSelectCount > 0) {
            enumVal.put("maxSelect", maxSelectCount);
        }
        if (maxTagCount > 0) {
            enumVal.put("maxTagCount", maxTagCount);
        } else if (multiple) {
            enumVal.put("maxTagCount", "responsive");
        }
        if (maxTagTextLength > 0) {
            enumVal.put("maxTagTextLength", maxTagTextLength);
        }
        if (showValue) {
            enumVal.put("treeNodeLabelProp", "value");
        }
        Map extraConfig = toConfigSchema();
        if (null != extraConfig && !extraConfig.isEmpty()) {
            for (Object key : extraConfig.keySet()) {
                enumVal.put(key.toString(), extraConfig.get(key));
            }
        }
        if (null != httpConfig) {
            enumVal.put("http", httpConfig);
        }

        Map<String, Object> val = new HashMap<>();
        val.put("common", getCommonConfig());
        if (!enumVal.isEmpty()) {
            val.put("enum", enumVal);
        }

        JsonProviderMetaType field = new JsonProviderMetaType();
        field.setId(RuleDict.Form.getId());
        field.setType(TypeDict.Ref.getName());
        field.setRefId(RuleDict.Form.getName());
        field.setValue(val);

        JsonProviderMetaType type = new JsonProviderMetaType();
        type.setId(getId());
        type.setType(TypeDict.Enum.getName());
        type.setFields(new JsonProviderMetaType[]{field});

        if (!enumValueList.isEmpty()) {
            type.setEnums(enumValueList.toArray(new JsonProviderEnumValue[0]));
        }

        return type;
    }
}
