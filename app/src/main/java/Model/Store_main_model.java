package Model;

public class Store_main_model {
    String stoer_id;
    String store_name;
    String user_email;
    String user_phone;
    String star;
    String store_details;

    public Store_main_model(String stoer_id, String store_name, String user_email, String user_phone, String star, String store_details, String user_image) {
        this.stoer_id = stoer_id;
        this.store_name = store_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.star = star;
        this.store_details = store_details;
        this.user_image = user_image;
    }

    public String getStore_details() {
        return store_details;
    }

    public void setStore_details(String store_details) {
        this.store_details = store_details;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    String user_image;

    public String getStoer_id() {
        return stoer_id;
    }

    public void setStoer_id(String stoer_id) {
        this.stoer_id = stoer_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public Store_main_model() {
    }

}
