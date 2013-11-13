package com.github.bednar.aap.example;

import javax.persistence.Column;
import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
* @author Jakub Bednář (13/11/2013 18:55)
*/
@ApiModel("Tasty Meal")
public class Meal
{
    @Column(length = 100, nullable = false)
    @ApiModelProperty(value = "Name", position = 2)
    private String name;

    @Column(precision = 10, scale = 2)
    @ApiModelProperty(value = "Price", position = 1)
    private BigDecimal price;
}
