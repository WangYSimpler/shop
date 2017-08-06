package com.gofirst.framework.bean;
// Generated 2017-7-18 8:53:23 by Hibernate Tools 4.0.0.Final

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
 * TOrder generated by hbm2java
 */
@Entity
@Table(name = "T_ORDER")
public class TOrder implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal id;
	private String orderNo;
	private String orderName;
	private String orderObject;
	private Character orderStatus;
	private BigDecimal orderPrice;
	private BigDecimal orderNum;
	private BigDecimal orderAmount;
	private BigDecimal freeNum;
	private String expressNo;
	private BigDecimal expressFee;
	private Date buyDate;
	private String buyAddress;
	private String remark;
	private String delFlag;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	public TOrder() {
	}

	public TOrder(BigDecimal id) {
		this.id = id;
	}

	public TOrder(BigDecimal id, String orderNo, String orderName, String orderObject, Character orderStatus,
			BigDecimal orderPrice, BigDecimal orderNum, BigDecimal orderAmount, BigDecimal freeNum, String expressNo,
			BigDecimal expressFee, Date buyDate, String buyAddress, String remark, String delFlag, String createUser,
			Date createDate, String updateUser, Date updateDate) {
		this.id = id;
		this.orderNo = orderNo;
		this.orderName = orderName;
		this.orderObject = orderObject;
		this.orderStatus = orderStatus;
		this.orderPrice = orderPrice;
		this.orderNum = orderNum;
		this.orderAmount = orderAmount;
		this.freeNum = freeNum;
		this.expressNo = expressNo;
		this.expressFee = expressFee;
		this.buyDate = buyDate;
		this.buyAddress = buyAddress;
		this.remark = remark;
		this.delFlag = delFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.updateUser = updateUser;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "ORDER_NO", length = 100)
	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "ORDER_NAME", length = 200)
	public String getOrderName() {
		return this.orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	@Column(name = "ORDER_OBJECT", length = 20)
	public String getOrderObject() {
		return this.orderObject;
	}

	public void setOrderObject(String orderObject) {
		this.orderObject = orderObject;
	}

	@Column(name = "ORDER_STATUS", length = 1)
	public Character getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(Character orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "ORDER_PRICE", precision = 15, scale = 4)
	public BigDecimal getOrderPrice() {
		return this.orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	@Column(name = "ORDER_NUM", precision = 22, scale = 0)
	public BigDecimal getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(BigDecimal orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "ORDER_AMOUNT", precision = 20, scale = 4)
	public BigDecimal getOrderAmount() {
		return this.orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	@Column(name = "FREE_NUM", precision = 22, scale = 0)
	public BigDecimal getFreeNum() {
		return this.freeNum;
	}

	public void setFreeNum(BigDecimal freeNum) {
		this.freeNum = freeNum;
	}

	@Column(name = "EXPRESS_NO", length = 100)
	public String getExpressNo() {
		return this.expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	@Column(name = "EXPRESS_FEE", precision = 20, scale = 4)
	public BigDecimal getExpressFee() {
		return this.expressFee;
	}

	public void setExpressFee(BigDecimal expressFee) {
		this.expressFee = expressFee;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BUY_DATE", length = 7)
	public Date getBuyDate() {
		return this.buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	@Column(name = "BUY_ADDRESS", length = 2000)
	public String getBuyAddress() {
		return this.buyAddress;
	}

	public void setBuyAddress(String buyAddress) {
		this.buyAddress = buyAddress;
	}

	@Column(name = "REMARK", length = 2000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "DEL_FLAG", length = 4)
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Column(name = "CREATE_USER", length = 200)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_USER", length = 200)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATE_DATE", length = 7)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
