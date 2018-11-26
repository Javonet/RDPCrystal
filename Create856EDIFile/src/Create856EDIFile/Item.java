package Create856EDIFile;


class Item
{
    private String PartNumber;

    public String getPartNumber() {
        return PartNumber;
    }

    public void setPartNumber(String partNumber) {
        PartNumber = partNumber;
    }

    private String UPCNumber;

    public String getUPCNumber() {
        return UPCNumber;
    }

    public void setUPCNumber(String uPCNumber) {
        UPCNumber = uPCNumber;
    }

    private String VenderCatalogNumber;

    public String getVenderCatalogNumber() {
        return VenderCatalogNumber;
    }

    public void setVenderCatalogNumber(String venderCatalogNumber) {
        VenderCatalogNumber = venderCatalogNumber;
    }

    private int NumberOfUnitsShipped;

    public int getNumberOfUnitsShipped() {
        return NumberOfUnitsShipped;
    }

    public void setNumberOfUnitsShipped(int numberOfUnitsShipped) {
        NumberOfUnitsShipped = numberOfUnitsShipped;
    }

    private String UnitOfMeasurement;

    public String getUnitOfMeasurement() {
        return UnitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        UnitOfMeasurement = unitOfMeasurement;
    }
}
