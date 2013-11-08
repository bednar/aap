package com.github.bednar.aap.it.dto.complex;

import javax.persistence.Column;
import java.math.BigDecimal;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author Jakub Bednář (08/11/2013 09:33)
 */
@ApiModel("Beer for my Pub")
public class Beer
{
    @Column(length = 100, nullable = false)
    @ApiModelProperty(value = "Name of Beer", position = 1)
    private String name;

    @Column(precision = 2, scale = 0)
    @ApiModelProperty(value = "Czech OLD degree Name", position = 2)
    private Integer degree;

    @Column(precision = 4, scale = 2)
    @ApiModelProperty(value = "Thousandth of Alcohol", position = 3)
    private BigDecimal thousandth;

    private String secret;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public Integer getDegree()
    {
        return degree;
    }

    public void setDegree(final Integer degree)
    {
        this.degree = degree;
    }

    public BigDecimal getThousandth()
    {
        return thousandth;
    }

    public void setThousandth(final BigDecimal thousandth)
    {
        this.thousandth = thousandth;
    }

    public String getSecret()
    {
        return secret;
    }

    public void setSecret(final String secret)
    {
        this.secret = secret;
    }
}
