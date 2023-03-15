package jdbc.vo;

public class LoLPbVO {
    private String id;
    private String pr_name;
    private String pr_type;
    private int pr_price;

    public LoLPbVO( String pr_name, String pr_type, int pr_price) {
        this.pr_name = pr_name;
        this.pr_type = pr_type;
        this.pr_price = pr_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPr_name() {
        return pr_name;
    }

    public void setPr_name(String pr_name) {
        this.pr_name = pr_name;
    }

    public String getPr_type() {
        return pr_type;
    }

    public void setPr_type(String pr_type) {
        this.pr_type = pr_type;
    }

    public int getPr_price() {
        return pr_price;
    }

    public void setPr_price(int pr_price) {
        this.pr_price = pr_price;
    }
}