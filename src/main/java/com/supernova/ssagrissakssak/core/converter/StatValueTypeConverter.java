package com.supernova.ssagrissakssak.core.converter;

import com.supernova.ssagrissakssak.core.enums.StatValueType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StatValueTypeConverter implements Converter<String, StatValueType> {

    @Override
    public StatValueType convert(String source) {
        return StatValueType.fromValue(source);
    }

}
