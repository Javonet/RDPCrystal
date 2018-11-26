package Create856EDIFile;

import java.util.ArrayList;
import java.util.List;

class Package
{
    private List<Item> items = new ArrayList<Item>();
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    private String PalletNumber;
    public String getPalletNumber() {
        return PalletNumber;
    }
    public void setPalletNumber(String palletNumber) {
        PalletNumber = palletNumber;
    }
}

