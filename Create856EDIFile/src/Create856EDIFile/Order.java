package Create856EDIFile;

import java.util.ArrayList;
import java.util.List;

class Order
{
    private List<Package> packages = new ArrayList<Package>();

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    private String PONumber;

    public String getPONumber() {
        return PONumber;
    }

    public void setPONumber(String pONumber) {
        PONumber = pONumber;
    }

}
