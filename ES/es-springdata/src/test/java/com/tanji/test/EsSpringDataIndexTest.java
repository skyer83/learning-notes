package com.tanji.test;

import com.tanji.test.pojo.po.ProductPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 索引操作
 * @author shenjh
 * @version 1.0
 * @since 2024-10-11 13:42
 */
@RunWith(SpringRunner.class) // 表明Test测试类要使用注入的类，比如@Autowired注入的类，有了@RunWith(SpringRunner.class)这些类才能实例化到spring容器中，自动注入才能生效
@SpringBootTest
public class EsSpringDataIndexTest {

    /** ElasticsearchRestTemplate 需要的 RestHighLevelClient 已在 EsConfig 中初始化 */
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 创建索引并增加映射配置
     *
     * @author shenjh
     * @version 1.0
     * @since 2024/10/11 14:04
     */
    @Test
    public void createIndex(){
        // 系统初始化时，根据持久对象注入的 @Document 会自动创建对应的索引，创建后，修改 @Document 内容后重启应用，不会修改已创建的索引
        System.out.println("创建索引");
    }

    @Test
    public void deleteIndex(){
        // 删除索引
        boolean flg = elasticsearchRestTemplate.deleteIndex(ProductPO.class);
        System.out.println("删除索引 = " + flg);
    }
}
