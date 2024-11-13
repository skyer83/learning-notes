package com.tanji.test.dao;

import com.tanji.test.pojo.po.ProductPO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author shenjh
 * @version 1.0
 * @since 2024-10-11 13:34
 */
public interface ProductDao extends ElasticsearchRepository<ProductPO, Long> {

}
