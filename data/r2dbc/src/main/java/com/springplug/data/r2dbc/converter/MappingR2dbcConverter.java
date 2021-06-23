package com.springplug.data.r2dbc.converter;

import com.springplug.data.core.enums.BaseEnum;
import org.springframework.core.CollectionFactory;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MappingR2dbcConverter extends org.springframework.data.r2dbc.convert.MappingR2dbcConverter{

    public MappingR2dbcConverter(MappingContext<? extends RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> context) {
        super(context);
    }

    public MappingR2dbcConverter(MappingContext<? extends RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> context, CustomConversions conversions) {
        super(context, conversions);
    }

    @Override
    public Object readValue(Object value, TypeInformation<?> type) {
        if (null == value) {
            return null;
        } else if (this.getConversions().hasCustomReadTarget(value.getClass(), type.getType())) {
            return this.getConversionService().convert(value, type.getType());
        } else {
            return !(value instanceof Collection) && !value.getClass().isArray() ? this.getPotentiallyConvertedSimpleRead(value, type.getType()) : this.readCollectionOrArray(asCollection(value), type);
        }
    }

    @Nullable
    private Object getPotentiallyConvertedSimpleRead(@Nullable Object value, @Nullable Class<?> target) {
        if (value == null || target == null || ClassUtils.isAssignableValue(target, value)) {
            return value;
        }

        if (getConversions().hasCustomReadTarget(value.getClass(), target)) {
            return getConversionService().convert(value, target);
        }

        if (Enum.class.isAssignableFrom(target)) {

            if(BaseEnum.class.isAssignableFrom(target)){
               return getIEnum((Class<BaseEnum>)target, value.toString());
            }else{
                return Enum.valueOf((Class<Enum>) target, value.toString());
            }
        }

        return getConversionService().convert(value, target);
    }

    public Object getIEnum(Class<? extends BaseEnum> targerType, String source) {
        for (BaseEnum enumObj : targerType.getEnumConstants()) {
            if (source.equals(String.valueOf(enumObj.getValue()))||source.equals(enumObj.getText())) {
                return enumObj;
            }
        }
        return null;
    }

    private Object readCollectionOrArray(Collection<?> source, TypeInformation<?> targetType) {

        Assert.notNull(targetType, "Target type must not be null!");

        Class<?> collectionType = targetType.isSubTypeOf(Collection.class) //
                ? targetType.getType() //
                : List.class;

        TypeInformation<?> componentType = targetType.getComponentType() != null //
                ? targetType.getComponentType() //
                : ClassTypeInformation.OBJECT;
        Class<?> rawComponentType = componentType.getType();

        Collection<Object> items = targetType.getType().isArray() //
                ? new ArrayList<>(source.size()) //
                : CollectionFactory.createCollection(collectionType, rawComponentType, source.size());

        if (source.isEmpty()) {
            return getPotentiallyConvertedSimpleRead(items, targetType.getType());
        }

        for (Object element : source) {

            if (!Object.class.equals(rawComponentType) && element instanceof Collection) {
                if (!rawComponentType.isArray() && !ClassUtils.isAssignable(Iterable.class, rawComponentType)) {
                    throw new MappingException(String.format(
                            "Cannot convert %1$s of type %2$s into an instance of %3$s! Implement a custom Converter<%2$s, %3$s> and register it with the CustomConversions",
                            element, element.getClass(), rawComponentType));
                }
            }
            if (element instanceof List) {
                items.add(readCollectionOrArray((Collection<Object>) element, componentType));
            } else {
                items.add(getPotentiallyConvertedSimpleRead(element, rawComponentType));
            }
        }

        return getPotentiallyConvertedSimpleRead(items, targetType.getType());
    }

    private static Collection<?> asCollection(Object source) {

        if (source instanceof Collection) {
            return (Collection<?>) source;
        }

        return source.getClass().isArray() ? CollectionUtils.arrayToList(source) : Collections.singleton(source);
    }
}
