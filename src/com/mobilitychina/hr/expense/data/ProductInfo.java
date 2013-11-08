package com.mobilitychina.hr.expense.data;

import java.io.Serializable;

/**
 * @author baohua
 *费用下的某个产品
 */
public class ProductInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id ;
	private String  quantity;//数量
	private String amount ;//单价
	private String remark ;//备注
	private String name ;//
	private String date ;//
	private String ref ;//ref为单号（为空时为false）
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
		if(ref.equalsIgnoreCase("false"));
		this.ref = "";
	}
	
	
}
