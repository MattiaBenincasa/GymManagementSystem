package DomainModel.Membership;

public class WeightRoomMembership extends Membership{
    private WRMembershipType type;

    public WeightRoomMembership() {}

    public WeightRoomMembership(int id) {
        super(id);
    }

    public WeightRoomMembership(WeightRoomMembership weightRoomMembership) {
        super(weightRoomMembership);
        this.type = weightRoomMembership.type;
    }

    public WRMembershipType getType() {
        return type;
    }

    public void setType(WRMembershipType type) {
        this.type = type;
    }

    public WeightRoomMembership copy() {
        return new WeightRoomMembership(this);
    }
}
