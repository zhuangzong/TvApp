package org.tvapp.db.bean;


import java.util.List;

public class ListResult {
//    data.put("cur_page", page);
//            data.put("last_page", lastPage);
//            data.put("page_size", pageSize);
//            data.put("total", total);
//            data.put("banner", bannerList);
//            data.put("list", tagsVideoList);

    private int cur_page;
    private int last_page;
    private int page_size;
    private int total;
    private List<BannerVideoResult> banner;
    private List<TagVideo> list;

    public int getCur_page() {
        return cur_page;
    }

    public void setCur_page(int cur_page) {
        this.cur_page = cur_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BannerVideoResult> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerVideoResult> banner) {
        this.banner = banner;
    }

    public List<TagVideo> getList() {
        return list;
    }

    public void setList(List<TagVideo> list) {
        this.list = list;
    }

}
