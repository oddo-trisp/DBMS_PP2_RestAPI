package gr.uoa.di.dbm.restapi.entity;
import java.io.Serializable;

public class UpvotedRequest extends ServiceRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    String ward;

    public UpvotedRequest(String id, String ward){
        this.setServiceRequestId(id);
        this.ward=ward;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
