package com.learning.layoutsdk.layout;

import com.learning.layoutsdk.component.ArrayComponent;
import com.learning.layoutsdk.component.LayoutComponent;
import com.learning.layoutsdk.component.ObjectComponent;
import com.learning.layoutsdk.component.impl.FileInput;
import com.learning.layoutsdk.component.impl.MultiCheckbox;
import com.learning.layoutsdk.component.impl.SingleRadio;
import com.learning.layoutsdk.enums.TitleLayout;
import org.springframework.util.Assert;

/**
 * @ClassName LayoutBuilder
 * @Description 画布构建类
 * @Author hufei
 * @Date 2023/2/17 10:27
 * @Version 1.0
 */
public class LayoutBuilder {

    private final Layout layout;

    public LayoutBuilder() {
        layout = new Layout();
    }

    // 确定画布布局
    public LayoutBuilder withLayoutConfigCount(int count) {
        Assert.isTrue(layout.getComponents().size() == 0, "please set global config first before adding component");
        layout.setCount(count);
        return this;
    }

    // 确定画布布局
    public LayoutBuilder withLayoutConfigTitleLayout(TitleLayout titleLayout) {
        layout.setTitleLayout(titleLayout);
        return this;
    }

    // 确定画布布局
    public LayoutBuilder withLayoutConfigLabelSpan(int span) {
        Assert.isTrue(span > 0 && span <= 24, "please set valid span value");
        layout.setLabelSpan(span);
        return this;
    }

    // 确定画布布局
    public LayoutBuilder withLayoutConfig(int count, TitleLayout titleLayout, int span) {
        return this.withLayoutConfigCount(count)
                .withLayoutConfigTitleLayout(titleLayout)
                .withLayoutConfigLabelSpan(span);
    }

    // 追加元素
    public LayoutBuilder withComponent(LayoutComponent component) {
        Assert.notNull(component, "component can not be null");
        Assert.notNull(component.getId(), "component id can not be null");
        if (component instanceof ObjectComponent || component instanceof ArrayComponent ||
                component instanceof SingleRadio || component instanceof MultiCheckbox ||
                component instanceof FileInput) {
            component.withMulti(layout.getCount());
        }
        layout.getComponents().add(component);
        return this;
    }

    public boolean isEmpty() {
        return layout.getComponents().size() == 0;
    }

    public Layout build() {
        return layout;
    }

}
