package com.tanji.es.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

/**
 * @author shenjh
 * @version 1.0
 * @since 2024-10-07 10:42
 */
@Slf4j
public class TestUtils {

    public static HttpHost buildHttpHost() {
        return new HttpHost("localhost", 9200, "http");
    }

    public static RestClientBuilder buildRestClientBuilder() {
        return RestClient.builder(TestUtils.buildHttpHost());
    }

    public static String toJsonStr(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("", e);
            return object.toString();
        }
    }
}
