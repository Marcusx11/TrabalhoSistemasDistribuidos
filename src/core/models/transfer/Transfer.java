package core.models.transfer;

import java.io.Serializable;

public class Transfer implements Serializable {
    private long id;
    private double amount;
    private long toUserId;
    private long fromUserId;

    public Transfer() {
    }

    public Transfer(long id, double amount, long toUserId, long fromUserId) {
        this.id = id;
        this.amount = amount;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
    }

    public Transfer(long id, double amount, long toUserId) {
        this.id = id;
        this.amount = amount;
        this.toUserId = toUserId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public long getToUserId() {
        return toUserId;
    }
}
