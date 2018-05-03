package com.fh.jaxbdemo;

import android.util.SparseArray;
import com.fh.jaxbdemo.annotation.XmlAttribute;
import com.fh.jaxbdemo.annotation.XmlElement;
import com.fh.jaxbdemo.annotation.XmlElementWrapper;
import com.fh.jaxbdemo.annotation.XmlIgnore;
import com.fh.jaxbdemo.annotation.XmlRootElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaojie on 2017/5/2.
 */
public class AnnotationUtils {
    private final static List<Class<? extends Annotation>> supportXmlAnnotations = new ArrayList<>();

    public final static int XML_NODE_TYPE_ATTRIBUTE = 0;
    public final static int XML_NODE_TYPE_ELEMENT = 1;
    public final static int XML_NODE_TYPE_ELEMENT_TEXT = 2;
    public final static int XML_NODE_TYPE_ELEMENTS = 3;

    static {
        // xml annotations
        supportXmlAnnotations.add(XmlElement.class);
        supportXmlAnnotations.add(XmlAttribute.class);
        supportXmlAnnotations.add(XmlRootElement.class);
        supportXmlAnnotations.add(XmlElementWrapper.class);
        supportXmlAnnotations.add(XmlIgnore.class);
    }

    /**
     * 找出xml annotation
     * @param field
     * @return
     */
    public static Annotation getXmlAnnotation(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        if (null!=annotations&&annotations.length>0) {
            for (Annotation annotation : annotations) {
                if (supportXmlAnnotations.contains(annotation.annotationType())) {
                    return annotation;
                }
            }
        }
        return null;
    }

    public static Annotation getXmlAnnotation(Class c) {
        Annotation[] annotations = c.getDeclaredAnnotations();
        if (null!=annotations&&annotations.length>0) {
            for (Annotation annotation : annotations) {
                if (supportXmlAnnotations.contains(annotation.annotationType())) {
                    return annotation;
                }
            }
        }
        return null;
    }

    public static List<Annotation> getXmlAnnotations(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        List<Annotation> results = new ArrayList<>();
        if (null!=annotations&&annotations.length>0) {
            for (Annotation annotation : annotations) {
                if (supportXmlAnnotations.contains(annotation.annotationType())) {
                    results.add(annotation);
                }
            }
        }
        return results;
    }

    public static List<Annotation> getXmlAnnotations(Class c) {
        Annotation[] annotations = c.getDeclaredAnnotations();
        List<Annotation> results = new ArrayList<>();
        if (null!=annotations&&annotations.length>0) {
            for (Annotation annotation : annotations) {
                if (supportXmlAnnotations.contains(annotation.annotationType())) {
                    results.add(annotation);
                }
            }
        }
        return results;
    }

    public static boolean isSupportXmlAnnotation(Annotation annotation) {
        return supportXmlAnnotations.contains(annotation.annotationType());
    }

    /**
     * 是否由包装节点
     * @param field
     * @return
     */
    public static boolean isElementWrapper(Field field) {
        return null != field.getAnnotation(XmlElementWrapper.class) || CommonUtils.isListType(field.getType());
    }

    /**
     * 获取包装节点名
     * @param field
     * @return
     */
    public static String getElementWrapperName(Field field) {
        if (isElementWrapper(field)) {
            XmlElementWrapper xmlElementWrapper = field.getAnnotation(XmlElementWrapper.class);
            if (null != xmlElementWrapper) {
                return xmlElementWrapper.name();
            }
        }
        return "";
    }

    /**
     * 获取节点名称
     * @param field
     * @return
     */
    public static String getElementName(Field field) {
        if (isElementWrapper(field)) {
            XmlElementWrapper xmlElementWrapper = field.getAnnotation(XmlElementWrapper.class);
            if (null != xmlElementWrapper) {
                if (StringUtils.isNotEmpty(xmlElementWrapper.elementName()))
                    return xmlElementWrapper.elementName();

                if (null != xmlElementWrapper.elementType())
                    return CommonUtils.toLowerCase(xmlElementWrapper.elementType().getSimpleName());
            } else {
                Class genericType = getListGenericType(field);
                if (null != genericType)
                    return CommonUtils.toLowerCase(genericType.getSimpleName());
            }
        } else {
            XmlElement xmlElement = field.getAnnotation(XmlElement.class);
            if (null != xmlElement && StringUtils.isNotEmpty(xmlElement.name())) {
                return xmlElement.name();
            }
        }
        return field.getName();
    }

    /**
     * 获取list范型
     * @param field
     * @return
     */
    public static Class<?> getChildElementType(Field field) {
        if (CommonUtils.isListType(field.getType())) {
            XmlElementWrapper xmlElementWrapper = field.getAnnotation(XmlElementWrapper.class);
            if (null != xmlElementWrapper && null != xmlElementWrapper.elementType()) {
                return xmlElementWrapper.elementType();
            }
            return getListGenericType(field);
        }
        return null;
    }

    /**
     * 获取属性名
     * @param field
     * @return
     */
    public static String getAttributeName(Field field) {
        if (CommonUtils.isPrimitiveType(field.getType())) {
            XmlAttribute xmlAttribute = field.getAnnotation(XmlAttribute.class);
            if (null != xmlAttribute) {
                if (StringUtils.isNotEmpty(xmlAttribute.name())) {
                    return xmlAttribute.name();
                }
            }
        }
        return field.getName();
    }

    /**
     * 获取list集合元素范型类型
     * @param field
     * @return
     */
    public static Class getListGenericType(Field field) {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Type[] fieldArgTypes = parameterizedType.getActualTypeArguments();
        for(Type fieldArgType : fieldArgTypes){
            Class fieldArgClass = (Class) fieldArgType;
            return fieldArgClass;
        }
        return null;
    }

    /**
     * 计算节点类型
     * @param field
     * @return
     */
    public static int getXmlNodeType(Field field) {
        Annotation annotation = AnnotationUtils.getXmlAnnotation(field);
        if (null != annotation) {
            if (XmlAttribute.class == annotation.annotationType()) {
                return XML_NODE_TYPE_ATTRIBUTE;
            } else if (XmlElement.class == annotation.annotationType()) {
                if (CommonUtils.isPrimitiveType(field.getType())) {
                    return XML_NODE_TYPE_ELEMENT_TEXT;
                } else {
                    return XML_NODE_TYPE_ELEMENT;
                }
            } else if (XmlElementWrapper.class == annotation.annotationType()) {
                return XML_NODE_TYPE_ELEMENTS;
            }
        } else {
            if (CommonUtils.isPrimitiveType(field.getType())) {
                return XML_NODE_TYPE_ATTRIBUTE;
            } else if (CommonUtils.isListType(field.getType())) {
                return XML_NODE_TYPE_ELEMENTS;
            } else {
                return XML_NODE_TYPE_ELEMENT;
            }
        }
        return XML_NODE_TYPE_ATTRIBUTE;
    }

    /**
     * 根据class类型获取待解析xml根节点名称
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> String getRootName(Class<T> tClass) {
        XmlRootElement xmlRoot = tClass.getAnnotation(XmlRootElement.class);
        if (null != xmlRoot) {
            String rootName = xmlRoot.name();
            if (StringUtils.isNotEmpty(rootName)) {
                return rootName;
            }
        }
        return CommonUtils.toLowerCase(tClass.getSimpleName());
    }

    /**
     * 对类属性进行分组
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> SparseArray<LinkedList<HashMap<String, Object>>> classifyFields(Class<T> tClass) {
        SparseArray<LinkedList<HashMap<String, Object>>> sparseArray = new SparseArray<>(4);
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isSynthetic())
                continue;
            if (field.isEnumConstant())
                continue;
            if (null != field.getAnnotation(XmlIgnore.class))
                continue;
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            // TODO 如果field是attribute
            field.setAccessible(true);
            Annotation annotation;
            HashMap<String, Object> map = new HashMap<>();
            int nodeType = getXmlNodeType(field);
            if (XML_NODE_TYPE_ATTRIBUTE == nodeType) {
                map.put("name", getAttributeName(field));
                map.put("field", field);
                if (null == sparseArray.get(XML_NODE_TYPE_ATTRIBUTE)) {
                    LinkedList<HashMap<String, Object>> linkedList = new LinkedList<>();
                    linkedList.addLast(map);
                    sparseArray.put(XML_NODE_TYPE_ATTRIBUTE, linkedList);
                } else {
                    sparseArray.get(XML_NODE_TYPE_ATTRIBUTE).addLast(map);
                }
            } else if (XML_NODE_TYPE_ELEMENT == nodeType) {
                map.put("name", getElementName(field));
                map.put("field", field);
                if (null == sparseArray.get(XML_NODE_TYPE_ELEMENT)) {
                    LinkedList<HashMap<String, Object>> linkedList = new LinkedList<>();
                    linkedList.addLast(map);
                    sparseArray.put(XML_NODE_TYPE_ELEMENT, linkedList);
                } else {
                    sparseArray.get(XML_NODE_TYPE_ELEMENT).addLast(map);
                }
            } else if (XML_NODE_TYPE_ELEMENT_TEXT == nodeType) {
                map.put("name", getElementName(field));
                map.put("field", field);
                if (null == sparseArray.get(XML_NODE_TYPE_ELEMENT_TEXT)) {
                    LinkedList<HashMap<String, Object>> linkedList = new LinkedList<>();
                    linkedList.addLast(map);
                    sparseArray.put(XML_NODE_TYPE_ELEMENT_TEXT, linkedList);
                } else {
                    sparseArray.get(XML_NODE_TYPE_ELEMENT_TEXT).addLast(map);
                }
            } else if (XML_NODE_TYPE_ELEMENTS == nodeType) {
                map.put("wrapper", getElementWrapperName(field));
                map.put("name", getElementName(field));
                map.put("field", field);
                if (null == sparseArray.get(XML_NODE_TYPE_ELEMENTS)) {
                    LinkedList<HashMap<String, Object>> linkedList = new LinkedList<>();
                    linkedList.addLast(map);
                    sparseArray.put(XML_NODE_TYPE_ELEMENTS, linkedList);
                } else {
                    sparseArray.get(XML_NODE_TYPE_ELEMENTS).addLast(map);
                }
            }
        }
        return sparseArray;
    }
}
