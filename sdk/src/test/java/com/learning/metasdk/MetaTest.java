package com.learning.metasdk;

import com.learning.metasdk.context.TypeContext;
import com.learning.metasdk.definition.MetaObject;
import com.learning.metasdk.definition.MetaProperty;
import com.learning.metasdk.definition.MetaType;
import com.learning.metasdk.enums.TypeStatus;
import com.learning.metasdk.handler.ObjectHandler;
import com.learning.metasdk.handler.TypeHandler;
import org.junit.jupiter.api.Test;

/**
 * @ClassName Test
 * @Description TODO
 * @Author hufei
 * @Date 2021/2/3 15:32
 * @Version 1.0
 */
public class MetaTest {

    @Test
    void memoryTest() {

        //定义类型
        //demo: class A {int a,string b}
        MetaType typeA = new MetaType()
                .setName("A")
                .setStatus(TypeStatus.Object)
                .addProperty(
                        new MetaProperty()
                                .setName("a")
                                .setType(
                                        new MetaType()
                                                .setStatus(TypeStatus.Int)
                                )
                )
                .addProperty(
                        new MetaProperty()
                                .setName("b")
                                .setType(
                                        new MetaType()
                                                .setStatus(TypeStatus.Double)
                                )
                );

        MetaType typeB = new MetaType()
                .setName("B")
                .setStatus(TypeStatus.Object)
                .addProperty(
                        new MetaProperty()
                                .setName("c")
                                .setType(typeA)
                )
                .addProperty(
                        new MetaProperty()
                                .setName("d")
                                .setType(
                                        new MetaType()
                                                .setStatus(TypeStatus.String)
                                )
                );

        MetaObject object = ObjectHandler.instanceObject(typeB);

        object.setFieldValue("c.a", 123);
        object.setFieldValue("c.b", 12.3);
        object.setFieldValue("d", "456");

        MetaObject object2 = ObjectHandler.instanceObject(typeB);

        object2.setFieldValue("c.a", 123);
        object2.setFieldValue("c.b", 12.3);
        object2.setFieldValue("d", "456");

        Object a = object.getFieldValue("c.a");
        System.out.println(a);

        Object b = object.getFieldValue("c.b");
        System.out.println(b);

        Object d = object.getFieldValue("d");
        System.out.println(d);

        System.out.println("object and object2 is " + (object.equals(object2) ? "" : "not ") + "equal");

    }

    @Test
    void serializeTest() {

        //定义类型
        //demo: class A {int a,string b}
        MetaType typeA = new MetaType()
                .setName("A")
                .setStatus(TypeStatus.Object)
                .addProperty(
                        new MetaProperty()
                                .setName("a")
                                .setType(
                                        new MetaType()
                                                .setStatus(TypeStatus.Int)
                                )
                )
                .addProperty(
                        new MetaProperty()
                                .setName("b")
                                .setType(
                                        new MetaType()
                                                .setStatus(TypeStatus.Double)
                                )
                );

        MetaType typeB = new MetaType()
                .setName("B")
                .setStatus(TypeStatus.Object)
                .addProperty(
                        new MetaProperty()
                                .setName("c")
                                .setType(typeA)
                )
                .addProperty(
                        new MetaProperty()
                                .setName("d")
                                .setType(
                                        new MetaType()
                                                .setStatus(TypeStatus.String)
                                )
                );

        String str = TypeHandler.serializeTypeJson(typeB);

        MetaType type = TypeHandler.loadTypeJson(str);

        TypeContext context = TypeHandler.loadTypeContext(str);

        System.out.println("object and object2 is " + (type.equals(context.getMetaType(type.getName())) ? "" : "not ") + "equal");
    }

}
