package com.mingnong.scanappnew.bean;

/**
 * Created by wyw on 2016/10/20.
 * 扫描二维码得到的实体类
 */
public class ScanOutputBean {

    private String FProductEnterprise;//生产企业
    private String FTyName;//通用名称
    private String FGuige;//规格
    private String FProductName;//产品名称
    private String FPzwh;//批准文号
    private String FYxqDate;//有效期
    private String FDw;//单位
    private String FHdjg;//价格
    private String mytId;//主键Id
    private String Yplx;//药品类型
    private String FGysmc; //供应商
    private String FXgzsh;//相关证书号
    private String FScph;//批号
    private String FScDate;//生产日期
    private String tradeCode;//追溯码
    private String FBuyNum = "1";//数量
    private String tradeCodeList;//子码集合
    private String uuid;//同一个文件的上传的标识
    public ScanOutputBean() {
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTradeCodeList() {
        return tradeCodeList;
    }

    public void setTradeCodeList(String tradeCodeList) {
        this.tradeCodeList = tradeCodeList;
    }

    public String getFProductEnterprise() {
        return FProductEnterprise;
    }

    public void setFProductEnterprise(String FProductEnterprise) {
        this.FProductEnterprise = FProductEnterprise;
    }

    public String getFTyName() {
        return FTyName;
    }

    public void setFTyName(String FTyName) {
        this.FTyName = FTyName;
    }

    public String getFGuige() {
        return FGuige;
    }

    public void setFGuige(String FGuige) {
        this.FGuige = FGuige;
    }

    public String getFProductName() {
        return FProductName;
    }

    public void setFProductName(String FProductName) {
        this.FProductName = FProductName;
    }

    public String getFPzwh() {
        return FPzwh;
    }

    public void setFPzwh(String FPzwh) {
        this.FPzwh = FPzwh;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getFScDate() {
        return FScDate;
    }

    public void setFScDate(String FScDate) {
        this.FScDate = FScDate;
    }

    public String getFYxqDate() {
        return FYxqDate;
    }

    public void setFYxqDate(String FYxqDate) {
        this.FYxqDate = FYxqDate;
    }

    public String getFDw() {
        return FDw;
    }

    public void setFDw(String FDw) {
        this.FDw = FDw;
    }

    public String getFHdjg() {
        return FHdjg;
    }

    public void setFHdjg(String FHdjg) {
        this.FHdjg = FHdjg;
    }

    public String getMytId() {
        return mytId;
    }

    public void setMytId(String mytId) {
        this.mytId = mytId;
    }

    public String getYplx() {
        return Yplx;
    }

    public void setYplx(String yplx) {
        Yplx = yplx;
    }

    public String getFGysmc() {
        return FGysmc;
    }

    public void setFGysmc(String FGysmc) {
        this.FGysmc = FGysmc;
    }

    public String getFXgzsh() {
        return FXgzsh;
    }

    public void setFXgzsh(String FXgzsh) {
        this.FXgzsh = FXgzsh;
    }

    public String getFScph() {
        return FScph;
    }

    public void setFScph(String FScph) {
        this.FScph = FScph;
    }

    public String getFBuyNum() {
        return FBuyNum;
    }

    public void setFBuyNum(String FBuyNum) {
        this.FBuyNum = FBuyNum;
    }
}
