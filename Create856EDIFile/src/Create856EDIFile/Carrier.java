package Create856EDIFile;

class Carrier
{
    private String VendorNumber;

    public String getVendorNumber() {
        return VendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        VendorNumber = vendorNumber;
    }

    private String ProNbrReference;
    public String getProNbrReference() {
        return ProNbrReference;
    }

    public void setProNbrReference(String proNbrReference) {
        ProNbrReference = proNbrReference;
    }

    private String RoutingSequenceCode;

    public String getRoutingSequenceCode() {
        return RoutingSequenceCode;
    }

    public void setRoutingSequenceCode(String routingSequenceCode) {
        RoutingSequenceCode = routingSequenceCode;
    }

    private String RoutingSequenceID;

    public String getRoutingSequenceID() {
        return RoutingSequenceID;
    }

    public void setRoutingSequenceID(String routingSequenceID) {
        RoutingSequenceID = routingSequenceID;
    }
}
