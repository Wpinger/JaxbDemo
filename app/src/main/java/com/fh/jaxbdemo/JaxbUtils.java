package com.fh.jaxbdemo;


import com.fh.jaxbdemo.annotation.XmlAttribute;
import com.fh.jaxbdemo.annotation.XmlElement;
import com.fh.jaxbdemo.annotation.XmlElementWrapper;
import com.fh.jaxbdemo.annotation.XmlIgnore;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ChuanWang on 2018/5/2.
 */


public class JaxbUtils<T> {

    public T parse(String xml, Class<T> tClass) {
        if (StringUtils.isNotEmpty(xml)) {
            try {
                Document document = DocumentHelper.parseText(xml);
                return parseDocument(document, tClass);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public T parse(InputStream is, Class<T> tClass) {
        try {
            Document document = new SAXReader().read(is);
            return parseDocument(document, tClass);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T parseDocument(Document document, Class<T> tClass) {
        try {
            Element root = document.getRootElement();
            return parseElement(root, tClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析节点
     *
     * @param root
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T> T parseElement(Element root, Class<T> tClass) throws Exception {
        if (null == root)
            return null;

        Field[] fields = tClass.getDeclaredFields(); // 获取fields
        if (null != fields && !tClass.isPrimitive()) {
            T t = tClass.newInstance(); // 创建一个实例
            for (Field field : fields) {
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                if (field.isSynthetic()) {
                    continue;
                }
                if (null != field.getAnnotation(XmlIgnore.class)) { // 忽略属性
                    continue;
                }
                try {
                    field.setAccessible(true);
                    if (null != field.getAnnotation(XmlIgnore.class)) { // 忽略属性
                        continue;
                    }
                    Annotation annotation = null;
                    if ((annotation = AnnotationUtils.getXmlAnnotation(field)) != null) { // 有注解
                        if (annotation.annotationType() == XmlElement.class) {
                            XmlElement xmlElement = field.getAnnotation(XmlElement.class);
                            String value = xmlElement.name();
                            if (null != value && !value.isEmpty()) {
                                value = field.getName();
                            }
                            Element element = root.element(value);
                            Object obj = null;
                            if (CommonUtils.isPrimitiveType(field.getType())) {
                                if (null != element) {
                                    obj = CommonUtils.getValue(element.getTextTrim(), field.getType());
                                }
                            } else {
                                obj = parseElement(element, field.getType());
                            }
                            field.set(t, obj);
                        } else if (annotation.annotationType() == XmlElementWrapper.class) {
                            XmlElementWrapper elementWrapper = field.getAnnotation(XmlElementWrapper.class);
                            String value = elementWrapper.name();
                            String elementName = elementWrapper.elementName();
                            Class elementType = elementWrapper.elementType();
                            Iterator<Element> elementIterator = null;
                            if (StringUtils.isEmpty(value)) { // 无包装节点
                                if (StringUtils.isEmpty(elementName) && CommonUtils.isListType(field.getClass())) {
                                    elementName = CommonUtils.toLowerCase(elementType.getSimpleName());
                                }
                                elementIterator = root.elementIterator(elementName);
                            } else { // 有包装节点
                                elementIterator = root.element(value).elementIterator();
                            }
                            List list = parseElements(elementIterator, elementType);
                            field.set(t, list);
                        } else if (annotation.annotationType() == XmlAttribute.class) {
                            XmlAttribute xmlAttribute = field.getAnnotation(XmlAttribute.class);
                            String value = xmlAttribute.name();
                            String attributeValue = parseAttribute(root, value);
                            if (null != attributeValue) {
                                field.set(t, CommonUtils.getValue(attributeValue, field.getType()));
                            }
                        }
                    } else { // 无注解当attribute
                        if (CommonUtils.isPrimitiveType(field.getType())) { // 基础类型
                            String attributeValue = parseAttribute(root, field.getName());
                            if (null != attributeValue) {
                                field.set(t, CommonUtils.getValue(attributeValue, field.getType()));
                            }
                        } else if (CommonUtils.isListType(field.getType())) { // 集合
                            Class fieldArgClass = AnnotationUtils.getListGenericType(field);
                            if (null != fieldArgClass) {
                                List list = parseElements(root.elementIterator(CommonUtils.toLowerCase(fieldArgClass.getSimpleName())), fieldArgClass);
                                field.set(t, list);
                            }
                        } else { // 类
                            Element element = root.element(field.getName());
                            Object obj = parseElement(element, field.getType());
                            field.set(t, obj);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return t;
        }
        return null;
    }


    /**
     * 解析节点集
     *
     * @param iterator
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T> List<T> parseElements(Iterator<Element> iterator, Class<T> tClass) throws Exception {
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            T t = parseElement(element, tClass);
            list.add(t);
        }
        return list;
    }

    /**
     * 解析属性值
     *
     * @param element
     * @param attributeName
     * @return
     */
    private static String parseAttribute(Element element, String attributeName) {
        if (null == element.attributeValue(attributeName))
            return null;
        return element.attributeValue(attributeName).trim();
    }

}
