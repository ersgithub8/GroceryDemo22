package Model;

public class Store_main_model {
    String stoer_id;
    String store_name;
    String star;
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

    public Store_main_model(String stoer_id, String store_name, String star, String user_image) {
        this.stoer_id = stoer_id;
        this.store_name = store_name;
        this.star = star;
        this.user_image = user_image;
    }
}
