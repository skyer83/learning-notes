package com.tanji.es.test;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_01_Client {

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        RestHighLevelClient restHighLevelClient = null;
        try {
            restHighLevelClient = new RestHighLevelClient(restClientBuilder);

            // 处理业务

            // 主动关闭 ES 客户端
            restHighLevelClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient2 = new RestHighLevelClient(restClientBuilder)) {

            // 处理业务

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
