package Model;

public class Rating_Model {
    String id,user_id,prod_id,star,comment;

    public Rating_Model(String id, String user_id, String prod_id, String star, String comment) {
        this.id = id;
        this.user_id = user_id;
        this.prod_id = prod_id;
        this.star = star;
        this.comment = comment;
    }

    public Rating_Model() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
