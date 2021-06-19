package com.xh.cloud.entity.po;

import com.xh.cloud.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author xianghui
 * @date 2021/06/14 1:02
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "tb_user")
@NoArgsConstructor
@AllArgsConstructor
public class UserPO extends BaseEntity implements Serializable {
}
