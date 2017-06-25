package com.gofirst.framework.bean;
// Generated 2017-6-22 23:29:00 by Hibernate Tools 4.0.0.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TOrder generated by hbm2java
 */
@Entity
@Table(name = "T_ORDER", schema = "SHOP")
public class TOrder implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String orderNo;
	private String orderName;
	private BigDecimal orderPrice;
	private BigDecimal orderNum;
	private BigDecimal orderAmount;
	private String expressNo;
	private String buyDate;
	private String buyAddress;
	private String remark;
	private Character flag;

	public TOrder() {
	}

	public TOrder(String id) {
		this.id = id;
	}

	public TOrder(String id, String orderNo, String orderName, BigDecimal orderPrice, BigDecimal orderNum,
			BigDecimal orderAmount, String expressNo, String buyDate, String buyAddress, String remark,
			Character flag) {
		this.id = id;
		this.orderNo = orderNo;
		this.orderName = orderName;
		this.orderPrice = orderPrice;
		this.orderNum = orderNum;
		this.orderAmount = orderAmount;
		this.expressNo = expressNo;
		this.buyDate = buyDate;
		this.buyAddress = buyAddress;
		this.remark = remark;
		this.flag = flag;
	}

	@Id
	@GeneratedValue
	@Column(name = "ID", unique = true, nullable = false, length = 100)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
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

	@Column(name = "EXPRESS_NO", length = 100)
	public String getExpressNo() {
		return this.expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	@Column(name = "BUY_DATE", length = 16)
	public String getBuyDate() {
		return this.buyDate;
	}

	public void setBuyDate(String buyDate) {
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

	@Column(name = "FLAG", length = 1)
	public Character getFlag() {
		return this.flag;
	}

	public void setFlag(Character flag) {
		this.flag = flag;
	}

}
