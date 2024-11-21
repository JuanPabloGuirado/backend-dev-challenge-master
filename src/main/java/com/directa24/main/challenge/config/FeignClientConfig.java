package com.directa24.main.challenge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@EnableAspectJAutoProxy
public class FeignClientConfig {

    /**
     * configuration bean for decoding specific media types
     *
     * @param objectMapper the mapper for serializing and deserializing
     * @return Decoder object
     */
    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new ResponseEntityDecoder(new SpringDecoder(() -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(objectMapper) {
                    @Override
                    protected boolean canRead(MediaType mediaType) {
                        return super.canRead(mediaType) || mediaType.includes(MediaType.APPLICATION_OCTET_STREAM);
                    }
                }
        )));
    }
}
