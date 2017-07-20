package com.gofirst.framework.bean;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TUser generated by hbm2java
 */
@Entity
@Table(name = "T_USER")
public class TUser implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;
	@Column(name = "USER_NO", length = 100)
	private String userNo;
	@Column(name = "USER_NAME", length = 200)
	private String userName;
	@Column(name = "PASSWORD", length = 100)
	private String password;
	@Column(name = "REMARK", length = 2000)
	private String remark;
	@Column(name = "DEL_FLAG", length = 4)
	private String delFlag;
	@Column(name = "CREATE_USER", length = 200)
	private String createUser;
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", length = 7)
	private Date createDate;
	@Column(name = "UPDATE_USER", length = 200)
	private String updateUser;
	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATE_DATE", length = 7)
	private Date updateDate;

	public TUser() {
	}

	public TUser(BigDecimal id) {
		this.id = id;
	}

	public TUser(BigDecimal id, String userNo, String userName, String password, String remark, String delFlag,
			String createUser, Date createDate, String updateUser, Date updateDate) {
		this.id = id;
		this.userNo = userNo;
		this.userName = userName;
		this.password = password;
		this.remark = remark;
		this.delFlag = delFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.updateUser = updateUser;
		this.updateDate = updateDate;
	}

	
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	
	public String getUserNo() {
		return this.userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
