package com.springplug;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.springplug.common.util.DateFormat;
import com.springplug.common.util.date.DateTimeUtil;
import com.springplug.web.flux.convert.EnumConvertFactory;
import com.springplug.web.flux.databind.BaseEnumDeserializer;
import com.springplug.web.flux.exception.JsonExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Configuration
@EnableReactiveFeignClients
public class WebFluxConfiguration implements WebFluxConfigurer {

    @Autowired
    private EnumConvertFactory enumConvertFactory;

    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer) {
        JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler();
        jsonExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return jsonExceptionHandler;
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumConvertFactory);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class,new LocalDateTimeDeserializer());
            builder.deserializerByType(Enum.class,new BaseEnumDeserializer());
        };
    }

    /**
     * 序列化
     */
    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {

            if (value != null){
                long timestamp = value.atZone( ZoneOffset.of("+8")).toInstant().toEpochMilli();
                gen.writeNumber(timestamp);
            }
        }
    }



    /**
     * 反序列化
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
                throws IOException {

            long timestamp = p.getValueAsLong();
            if (timestamp > 0){
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.of("+8"));
            }else{
                return null;
            }
        }
    }

    /**
     * description:LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     * 接收毫秒级时间戳字符串——>LocalDateTime
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                //毫秒级时间戳转LocalDateTime
                Date parse = DateFormat.instance.parse(source);
                return DateTimeUtil.dateToLocalDateTime(parse);
            }
        };
    }

    /**
     * description:java.util.Date转换器
     * 接收毫秒级时间戳字符串——>Date
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                long longTimeStamp = new Long(source);
                return new Date(longTimeStamp);
            }
        };
    }

}
