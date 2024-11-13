package com.tanji.test.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author shenjh
 * @version 1.0
 * @since 2024-10-11 13:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "product", shards = 1, replicas = 1) // 服务启动时，会根据 @Document 自动创建对应索引
public class ProductPO {

    /** 商品唯一标识 */
    @Id // /必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"
    private Long id;
    /** 商品名称 */
    @Field(type = FieldType.Text, analyzer = "ik_max_word") // type - 字段数据类型，analyzer - 分词器类型，index - 是否索引(默认:true)
    private String title;
    /** 分类名称 */
    @Field(type = FieldType.Keyword) // Keyword - 不进行分词
    private String category;
    /** 商品价格 */
    @Field(type = FieldType.Double)
    private Double price;
    /** 图片地址 */
    @Field(type = FieldType.Keyword, index = false)
    private String images;
}
