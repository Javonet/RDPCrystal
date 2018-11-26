package Create856EDIFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ShipmentNotice
{
    private String ShipmentID;

    public String getShipmentID() {
        return ShipmentID;
    }

    public void setShipmentID(String shipmentID) {
        ShipmentID = shipmentID;
    }

    private NoticeType NoticeType;

    public NoticeType getNoticeType() {
        return NoticeType;
    }

    public void setNoticeType(NoticeType noticeType) {
        NoticeType = noticeType;
    }

    private List<Order> Orders = new ArrayList<Order>();

    public List<Order> getOrders() {
        return Orders;
    }

    public void setOrders(List<Order> orders) {
        Orders = orders;
    }

    private Date DateCreated;

    public Date getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        DateCreated = dateCreated;
    }

    private Date ShipmentDate;

    public Date getShipmentDate() {
        return ShipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        ShipmentDate = shipmentDate;
    }

    private Carrier Carrier;

    public Carrier getCarrier() {
        return Carrier;
    }

    public void setCarrier(Carrier carrier) {
        Carrier = carrier;
    }

    private String WeightingQualifier;

    public String getWeightingQualifier() {
        return WeightingQualifier;
    }

    public void setWeightingQualifier(String weightingQualifier) {
        WeightingQualifier = weightingQualifier;
    }

    private String PackingCode;

    public String getPackingCode() {
        return PackingCode;
    }

    public void setPackingCode(String packingCode) {
        PackingCode = packingCode;
    }

    private String UnitOfMeasure;

    public String getUnitOfMeasure() {
        return UnitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        UnitOfMeasure = unitOfMeasure;
    }

    private int LadingQuantity;

    public int getLadingQuantity() {
        return LadingQuantity;
    }

    public void setLadingQuantity(int ladingQuantity) {
        LadingQuantity = ladingQuantity;
    }

    private double Weight;

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    private Party ShipToParty;

    public Party getShipToParty() {
        return ShipToParty;
    }

    public void setShipToParty(Party shipToParty) {
        ShipToParty = shipToParty;
    }
}

