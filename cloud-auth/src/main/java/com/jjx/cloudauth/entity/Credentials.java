package com.jjx.cloudauth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiangjx
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("credentials")
public class Credentials extends Model<Credentials> {

    private static final long serialVersionUID=1L;

    private Integer id;

    private Boolean enabled;

    private String name;

    private String password;

    private Integer version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
