package DomainModel.Membership;

public abstract class Membership {
    private int id;
    private String name;
    private float price;
    private String description;
    private int durationInDays;

    public Membership() {}

    public Membership(int id) {
        this.id = id;
    }

    public Membership(Membership membership) {
        this.id = membership.id;
        this.name = membership.name;
        this.price = membership.price;
        this.description = membership.description;
        this.durationInDays = membership.durationInDays;
    }

    public abstract Membership copy();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }
}
