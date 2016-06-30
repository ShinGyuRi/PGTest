package tiltcode.movingkey.com.tiltcode_new.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by Gyul on 2016-06-30.
 */
public class ListViewItem {

    private Drawable imgProduct;
    private String tvSale;
    private String tvBrandname, tvProduct, tvDday, tvExpDate;

    public Drawable getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(Drawable imgProduct) {
        this.imgProduct = imgProduct;
    }

    public String getTvSale() {
        return tvSale;
    }

    public void setTvSale(String tvSale) {
        this.tvSale = tvSale;
    }

    public String getTvBrandname() {
        return tvBrandname;
    }

    public void setTvBrandname(String tvBrandname) {
        this.tvBrandname = tvBrandname;
    }

    public String getTvProduct() {
        return tvProduct;
    }

    public void setTvProduct(String tvProduct) {
        this.tvProduct = tvProduct;
    }

    public String getTvDday() {
        return tvDday;
    }

    public void setTvDday(String tvDday) {
        this.tvDday = tvDday;
    }

    public String getTvExpDate() {
        return tvExpDate;
    }

    public void setTvExpDate(String tvExpDate) {
        this.tvExpDate = tvExpDate;
    }
}
