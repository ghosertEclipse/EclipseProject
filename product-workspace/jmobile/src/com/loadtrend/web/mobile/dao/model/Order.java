package com.loadtrend.web.mobile.dao.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Order implements Serializable {
	private String id = null;

	private String productcode = null;

	private String payer = null;

	private String sender = null;

	private String receiver = null;

	private String linkid = null;

	private String param = null;

	private Item item = null;

	private String paySuccess = null;

	private String pushSuccess = null;

	private Date optime = null;

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @return Returns the linkid.
	 */
	public String getLinkid() {
		return linkid;
	}

	/**
	 * @param linkid The linkid to set.
	 */
	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}

	/**
	 * @return Returns the optime.
	 */
	public Date getOptime() {
		return optime;
	}

	/**
	 * @param optime The optime to set.
	 */
	public void setOptime(Date optime) {
		this.optime = optime;
	}

	/**
	 * @return Returns the param.
	 */
	public String getParam() {
		return param;
	}

	/**
	 * @param param The param to set.
	 */
	public void setParam(String param) {
		this.param = param;
	}

	/**
	 * @return Returns the payer.
	 */
	public String getPayer() {
		return payer;
	}

	/**
	 * @param payer The payer to set.
	 */
	public void setPayer(String payer) {
		this.payer = payer;
	}

	/**
	 * @return Returns the paySuccess.
	 */
	public String getPaySuccess() {
		return paySuccess;
	}

	/**
	 * @param paySuccess The paySuccess to set.
	 */
	public void setPaySuccess(String paySuccess) {
		this.paySuccess = paySuccess;
	}

	/**
	 * @return Returns the productcode.
	 */
	public String getProductcode() {
		return productcode;
	}

	/**
	 * @param productcode The productcode to set.
	 */
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	/**
	 * @return Returns the pushSuccess.
	 */
	public String getPushSuccess() {
		return pushSuccess;
	}

	/**
	 * @param pushSuccess The pushSuccess to set.
	 */
	public void setPushSuccess(String pushSuccess) {
		this.pushSuccess = pushSuccess;
	}

	/**
	 * @return Returns the receiver.
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver The receiver to set.
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return Returns the sender.
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender The sender to set.
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

    public boolean equals(final Object other) {
        if (!(other instanceof Order))
            return false;
        Order castOther = (Order) other;
        return new EqualsBuilder().append(productcode, castOther.productcode)
                .append(payer, castOther.payer)
                .append(sender, castOther.sender).append(receiver,
                        castOther.receiver).append(linkid, castOther.linkid)
                .append(param, castOther.param)
                .append(optime, castOther.optime).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(productcode).append(payer).append(
                sender).append(receiver).append(linkid).append(param).append(
                optime).toHashCode();
    }

}
