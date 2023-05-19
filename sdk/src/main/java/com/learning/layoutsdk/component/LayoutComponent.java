package com.learning.layoutsdk.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learning.layoutsdk.definition.JsonProviderMetaType;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @InterfaceName LayoutComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/21 10:34
 * @Version 1.0
 */
public interface LayoutComponent {

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class LayoutCommonConfig {
        private String id;
        @JsonProperty("label")
        private String name;
        @JsonProperty("color")
        private String titleColor;
        @JsonProperty("colSpan")
        private int multi;
        private Object defaultValue;
        private String help;
        private String tooltip;
        private String placeholder;
        private boolean require;
        private boolean disabled;
        private boolean hidden;
        private boolean br;
        @JsonIgnore
        private String notRequireCondition;
        @JsonIgnore
        private String showCondition;
        @JsonIgnore
        private String hiddenCondition;

        public static LayoutCommonConfig init() {
            LayoutCommonConfig config = new LayoutCommonConfig();
            config.setMulti(1);
            return config;
        }

        public Map<String, String> getDepend() {
            Map<String, String> depend = new HashMap<>();
            if (null != showCondition) {
                depend.put("show", showCondition);
            }
            if (null != hiddenCondition) {
                depend.put("hidden", hiddenCondition);
            }
            if (null != notRequireCondition) {
                depend.put("notRequire", notRequireCondition);
            }
            return depend.isEmpty() ? null : depend;
        }
    }

    // implement methods

    String getId();

    String getName();

    JsonProviderMetaType toSchema();

    Map toConfigSchema();

    default List<JsonProviderMetaType> obtainExtraTypeList() {
        return null;
    }

    // action method

    LayoutComponent withId(String id);

    LayoutComponent withName(String name);

    LayoutComponent withTitleColor(String titleColor);

    LayoutComponent withMulti(int multi);

    LayoutComponent withHelp(String help);

    LayoutComponent withTooltip(String tooltip);

    LayoutComponent withRequire();

    LayoutComponent withLineFeed();

    LayoutComponent withNotRequireCondition(String notRequireCondition);

    LayoutComponent withShowCondition(String showCondition);

    LayoutComponent withHiddenCondition(String hiddenCondition);

    LayoutComponent copy();
}
