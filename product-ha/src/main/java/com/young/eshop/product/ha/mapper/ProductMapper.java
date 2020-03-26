package com.young.eshop.product.ha.mapper;

import com.young.eshop.product.ha.model.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {

    @Select("select * from eshop.product p where p.id=#{id}")
    Product selectOne(Integer id);

    @Select({
            "<script>",
            "select * from eshop.product p where p.id in",
            "<foreach item='id' index='index' collection='ids'",
            "open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<Product> selectList(@Param("ids") List<Integer> ids);
}
