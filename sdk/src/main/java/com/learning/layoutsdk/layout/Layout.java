package com.learning.layoutsdk.layout;

import com.learning.layoutsdk.component.LayoutComponent;
import com.learning.layoutsdk.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.RuleDict;
import com.learning.layoutsdk.enums.TitleLayout;
import com.learning.layoutsdk.enums.TypeDict;
import com.learning.utils.TextUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Layout
 * @Description 画布
 * @Author hufei
 * @Date 2023/2/17 10:26
 * @Version 1.0
 */
@Data
public class Layout {

    // 一行几列
    private int count = 1;

    // 标题位置
    private TitleLayout titleLayout = TitleLayout.MID;

    // 标题占比(N in 24)
    private int labelSpan = -1;

    // 容器
    private List<LayoutComponent> components = new ArrayList<>();

    @Override
    public String toString() {
        JsonProviderMetaType type = new JsonProviderMetaType();
        List<JsonProviderMetaType> properties = new ArrayList<>();
        List<JsonProviderMetaType> types = new ArrayList<>();
        for (LayoutComponent component : components) {
            properties.add(component.toSchema());
            List<JsonProviderMetaType> extraTypeList = component.obtainExtraTypeList();
            if (null != extraTypeList && extraTypeList.size() > 0) {
                types.addAll(extraTypeList);
            }
        }
        type.setId("layout");
        type.setType(TypeDict.Struct.getName());
        if (!types.isEmpty()) {
            type.setTypes(types.toArray(new JsonProviderMetaType[0]));
        }
        type.setProperties(properties.toArray(new JsonProviderMetaType[0]));
        Map<String, Object> fieldVal = new HashMap<>();
        if (count != 1) {
            fieldVal.put("colSpan", 24 / count);
        }
        if (titleLayout != TitleLayout.MID) {
            fieldVal.put("layout", titleLayout.getName());
        }
        if (labelSpan != -1) {
            fieldVal.put("labelSpan", labelSpan);
        }
        if (!fieldVal.isEmpty()) {
            JsonProviderMetaType field = new JsonProviderMetaType();
            field.setId(RuleDict.Form.getId());
            field.setType(TypeDict.Ref.getName());
            field.setRefId(RuleDict.Form.getName());
            field.setValue(fieldVal);
            type.setFields(new JsonProviderMetaType[]{field});
        }
        return "[" + TextUtil.fromJson(type) + "]";
    }
}
