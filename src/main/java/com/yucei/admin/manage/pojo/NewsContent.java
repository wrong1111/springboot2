package com.yucei.admin.manage.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class NewsContent {
    private Integer id;

    private Integer insertuserid;

    public Integer getInsertuserid() {
        return insertuserid;
    }

    public void setInsertuserid(Integer insertuserid) {
        this.insertuserid = insertuserid;
    }

    private String title;

    private String author;

    private String targeturl;

    private Date updatetime;

    private String publishflag;

    private Date publishtime;

    private String iscoment;

    private String isdel;

    private Date createtime;

    private String imgurl;

    private String imgurl2;

    private String content;

    private Integer lotteryid;

    private Integer seq;

    private String startDate;
    private String endDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTargeturl() {
        return targeturl;
    }

    public void setTargeturl(String targeturl) {
        this.targeturl = targeturl;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getPublishflag() {
        return publishflag;
    }

    public void setPublishflag(String publishflag) {
        this.publishflag = publishflag;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public String getIscoment() {
        return iscoment;
    }

    public void setIscoment(String iscoment) {
        this.iscoment = iscoment;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl2() {
        return imgurl2;
    }

    public void setImgurl2(String imgurl2) {
        this.imgurl2 = imgurl2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(Integer lotteryid) {
        this.lotteryid = lotteryid;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}