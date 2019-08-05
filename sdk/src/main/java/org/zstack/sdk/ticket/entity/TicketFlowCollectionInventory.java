package org.zstack.sdk.ticket.entity;



public class TicketFlowCollectionInventory  {

    public java.lang.String uuid;
    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }
    public java.lang.String getUuid() {
        return this.uuid;
    }

    public java.lang.String name;
    public void setName(java.lang.String name) {
        this.name = name;
    }
    public java.lang.String getName() {
        return this.name;
    }

    public java.lang.String description;
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    public java.lang.String getDescription() {
        return this.description;
    }

    public java.lang.String state;
    public void setState(java.lang.String state) {
        this.state = state;
    }
    public java.lang.String getState() {
        return this.state;
    }

    public java.lang.String status;
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
    public java.lang.String getStatus() {
        return this.status;
    }

    public java.sql.Timestamp createDate;
    public void setCreateDate(java.sql.Timestamp createDate) {
        this.createDate = createDate;
    }
    public java.sql.Timestamp getCreateDate() {
        return this.createDate;
    }

    public java.sql.Timestamp lastOpDate;
    public void setLastOpDate(java.sql.Timestamp lastOpDate) {
        this.lastOpDate = lastOpDate;
    }
    public java.sql.Timestamp getLastOpDate() {
        return this.lastOpDate;
    }

    public java.lang.Boolean isDefault;
    public void setIsDefault(java.lang.Boolean isDefault) {
        this.isDefault = isDefault;
    }
    public java.lang.Boolean getIsDefault() {
        return this.isDefault;
    }

    public java.util.List flows;
    public void setFlows(java.util.List flows) {
        this.flows = flows;
    }
    public java.util.List getFlows() {
        return this.flows;
    }

    public java.util.List ticketTypeUuids;
    public void setTicketTypeUuids(java.util.List ticketTypeUuids) {
        this.ticketTypeUuids = ticketTypeUuids;
    }
    public java.util.List getTicketTypeUuids() {
        return this.ticketTypeUuids;
    }

}
