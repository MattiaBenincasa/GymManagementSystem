package DomainModel.PurchaseItems;

import java.util.ArrayList;

public class Bundle implements PurchaseBundle{
    private String name;
    private String description;
    private final ArrayList<Bundle> bundles;

    public Bundle() {
        this.bundles = new ArrayList<>();
    }

    public void addBundle(Bundle bundle) {
        this.bundles.add(bundle);
    }

    public void remove(Bundle bundle) {
        this.bundles.remove(bundle);
    }

    @Override
    public float calculateTotal() {
        float total = 0;
        for (Bundle bundle : this.bundles)
            total += bundle.calculateTotal();

        return total;
    }
}
