package nandubrajan.ceknpy.iguard;

public class Model_notification {
    String lock_id,name,time_ ,status;
    byte []intruder;

    public String getLock_id() {
        return lock_id;
    }

    public void setLock_id(String lock_id) {
        this.lock_id = lock_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime_() {
        return time_;
    }

    public void setTime_(String time_) {
        this.time_ = time_;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getIntruder() {
        return intruder;
    }

    public void setIntruder(byte[] intruder) {
        this.intruder = intruder;
    }
}
