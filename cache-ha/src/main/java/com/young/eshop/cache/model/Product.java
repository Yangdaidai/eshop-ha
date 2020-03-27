package com.young.eshop.cache.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品信息
 *
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product implements Serializable {

    private Integer id;
    private String name;
    private Double price;

    private Integer cityId;
    private String cityName;

    private Integer brandId;
    private String brandName;

    private String color;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;



}
