package com.learning.layoutsdk;

import com.learning.layoutsdk.component.EnumComponent;
import com.learning.layoutsdk.component.LayoutComponent;
import com.learning.layoutsdk.component.impl.*;
import com.learning.layoutsdk.enums.TitleLayout;
import com.learning.layoutsdk.layout.Layout;
import com.learning.layoutsdk.layout.LayoutBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * @ClassName LayoutTest
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/24 11:34
 * @Version 1.0
 */
class LayoutTest {

    @Test
    void normalTest() {
        Layout layout = new LayoutBuilder()
                .withLayoutConfig(2, TitleLayout.MID, 10)
                .withComponent(new DoubleInput().withId("double").withName("浮点数"))
                .withComponent(new LongInput().withId("int").withName("整型"))
                .withComponent(new TextInput().withId("text").withName("普通文本"))
                .withComponent(new TextInput().withId("unit").withName("单位文本").withUnit("Hz", "rpm"))
                .withComponent(new TextareaInput().withId("textarea").withName("富文本"))
                .withComponent(new PasswordInput().withId("password").withName("密码"))
                .withComponent(new DateInput().withId("date").withName("日期选择").withFormatter("yyyy/MM/dd HH:mm:ss"))
                .withComponent(new ColorInput().withId("color").withName("颜色选择"))
                .withComponent(new FileInput().withId("file").withName("文件选择"))
                .withComponent(new SwitchInput().withId("switch").withName("开关").withDefaultValue(true))
                .withComponent(new SingleSelector().withId("normalsingle").withName("普通单选下拉").withEnumItem("A", "B"))
                .withComponent(new SingleSelector().withId("keysingle").withName("键值单选下拉").withKVEnumItem("A", 0).withKVEnumItem("B", 1))
                .withComponent(new SingleSelector().withId("httpsingle").withName("Http单选下拉").withEnumItemOutsideWithGet("/business-web/devcase/point/list?path=SampleSet:41:_id=45&hasMeasdef=false", "value", "label"))
                .withComponent(new MultiSelector().withId("normalmulti").withName("普通多选下拉").withEnumItem("A", "B"))
                .withComponent(new MultiSelector().withId("keymulti").withName("键值多选下拉").withKVEnumItem("A", 0).withKVEnumItem("B", 1))
                .withComponent(new MultiSelector().withId("httpmulti").withName("Http多选下拉").withEnumItemOutsideWithGet("/business-web/devcase/point/list?path=SampleSet:41:_id=45&hasMeasdef=false", "value", "label"))
                .withComponent(new SingleCascadeSelector().withId("singlecascade").withName("单选级联").withEnumItemOutsideWithGet("/business-web/devcase/point/list?path=SampleSet:41:_id=45&hasMeasdef=true", "value", "label"))
                .withComponent(new MultiCascadeSelector().withId("multicascade").withName("多选级联").withEnumItemOutsideWithGet("/business-web/devcase/point/list?path=SampleSet:41:_id=45&hasMeasdef=true", "value", "label"))
                .withComponent(new SingleRadio().withId("radio").withName("单选按钮").withEnumItem("A", "B"))
                .withComponent(new MultiCheckbox().withId("checkbox").withName("多选框").withEnumItem("A", "B"))
                .withComponent(new NumberRangeInput().withId("numberrange").withName("数字范围").withDefaultValue(1, 2).withMinKeyName("start").withMaxKeyName("end").withUnit("rpm", "Hz"))
                .build();
        System.out.println(layout.toString());
    }

    @Test
    void objectTest() {
        Layout layout = new LayoutBuilder()
                .withLayoutConfig(2, TitleLayout.MID, 10)
                .withComponent(new DoubleInput().withId("double").withName("浮点数"))
                .withComponent(new LongInput().withId("int").withName("整型"))
                .withComponent(new TextInput().withId("text").withName("普通文本"))
                .withComponent(new ObjectInput().withId("object1").withName("对象1")
                        .withProperty(new TextInput().withId("text1").withName("TEXT1"))
                        .withProperty(new LongInput().withId("number1").withName("NUMBER1"))
                        .withProperty(new ObjectInput().withId("object2").withName("对象2")
                                .withProperty(new TextInput().withId("text2").withName("TEXT2"))
                                .withProperty(new LongInput().withId("number2").withName("NUMBER2"))
                                .withProperty(new ObjectInput().withId("object3").withName("对象3")
                                        .withProperty(new TextInput().withId("text3").withName("TEXT3"))
                                        .withProperty(new LongInput().withId("number3").withName("NUMBER3")))))
                .build();
        System.out.println(layout.toString());
    }

    @Test
    void arrayTest() {
        Layout layout = new LayoutBuilder()
                .withLayoutConfig(2, TitleLayout.MID, 10)
                .withComponent(new ListInput().withId("textlist").withName("文本列表").withNormalSubType(new TextInput().withId("textitem").withName("文本项")))
                .withComponent(new ListInput().withId("numberlist").withName("数字列表").withNormalSubType(new DoubleInput().withId("doubleitem").withName("数字项")))
                .withComponent(new ListInput().withId("enumlist").withName("枚举列表").withNormalSubType(new SingleSelector().withId("enumitem").withName("枚举项").withEnumItem("A", "B", "C")))
                .withComponent(new ListInput().withId("objlist").withName("对象列表").withStructSubType(
                        new ObjectInput().withId("object1").withName("对象1")
                                .withProperty(new TextInput().withId("text1").withName("TEXT1"))
                                .withProperty(new LongInput().withId("number1").withName("NUMBER1"))
                                .withProperty(new ObjectInput().withId("object2").withName("对象2")
                                        .withProperty(new TextInput().withId("text2").withName("TEXT2"))
                                        .withProperty(new LongInput().withId("number2").withName("NUMBER2"))
                                        .withProperty(new ObjectInput().withId("object3").withName("对象3")
                                                .withProperty(new TextInput().withId("text3").withName("TEXT3"))
                                                .withProperty(new LongInput().withId("number3").withName("NUMBER3"))))
                ))
                .withComponent(new TableInput().withId("table").withName("表格").withSubType(
                        new ObjectInput().withId("object4").withName("对象4")
                                .withProperty(new TextInput().withId("text4").withName("TEXT4"))
                                .withProperty(new LongInput().withId("number4").withName("NUMBER4"))
                                .withProperty(new ObjectInput().withId("object5").withName("对象5")
                                        .withProperty(new TextInput().withId("text5").withName("TEXT5"))
                                        .withProperty(new LongInput().withId("number5").withName("NUMBER5"))
                                        .withProperty(new ObjectInput().withId("object6").withName("对象6")
                                                .withProperty(new TextInput().withId("text6").withName("TEXT6"))
                                                .withProperty(new LongInput().withId("number6").withName("NUMBER6"))))
                ))
                .build();
        System.out.println(layout.toString());
    }

    @Test
    void copyTest() {
        LayoutComponent component = new MultiSelector().withId("normalmulti").withName("普通多选下拉").withEnumItem("A", "B");
        if (component instanceof EnumComponent) {
            EnumComponent enumComponent = (EnumComponent) component;
            enumComponent.setMaxSelectCount(1);
        }
        LayoutComponent copyedComponent = component.copy();
        Assert.isTrue(component.getName().equals(copyedComponent.getName()), "ok");
    }
}
