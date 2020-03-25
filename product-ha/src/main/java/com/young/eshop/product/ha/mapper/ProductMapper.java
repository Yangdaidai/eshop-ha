package com.young.eshop.product.ha.mapper;

import com.young.eshop.product.ha.model.Product;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper {

    @Select("select * from eshop.product where id=#{id}")
    Product selectOne(Integer id);
}
