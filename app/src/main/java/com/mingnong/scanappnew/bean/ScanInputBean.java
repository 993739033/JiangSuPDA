package com.mingnong.scanappnew.bean;

/**
 * Created by wyw on 2016/10/20.
 * 扫描二维码得到的实体类
 */
public class ScanInputBean {

    private String zhuiSuMa;//追溯码
    private String tongYongMing;//通用名
    private String wenHao;//批准文号
    private String name;//企业名称
    private String phone;//企业电话
    private String count = "1";//数量

    private String productionDate;//生产日期
    private String expiryDate;//有效期
    private String productNumber;//生产批号
    private String unit;//单位
    private String purchasePrice;//购买价格
    private String presellPrice;//预售价格

    private String yplxname; //抗生素 隐藏字段 选填
    private String tradeCodeList;//tradeCodeList 隐藏字段
    private String uuid;//同一个文件的上传的标识


    public ScanInputBean() {
    }

    public ScanInputBean(String zhuiSuMa, String tongYongMing, String wenHao, String name, String phone) {
        this.zhuiSuMa = zhuiSuMa;
        this.tongYongMing = tongYongMing;
        this.wenHao = wenHao;
        this.name = name;
        this.phone = phone;
    }

    public ScanInputBean(String zhuiSuMa, String tongYongMing, String wenHao, String name, String phone, String productionDate, String expiryDate, String productNumber, String unit, String purchasePrice, String presellPrice) {
        this.zhuiSuMa = zhuiSuMa;
        this.tongYongMing = tongYongMing;
        this.wenHao = wenHao;
        this.name = name;
        this.phone = phone;
        this.productionDate = productionDate;
        this.expiryDate = expiryDate;
        this.productNumber = productNumber;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.presellPrice = presellPrice;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getYplxname() {
        return yplxname;
    }

    public void setYplxname(String yplxname) {
        this.yplxname = yplxname;
    }

    public String getTradeCodeList() {
        return tradeCodeList;
    }

    public void setTradeCodeList(String tradeCodeList) {
        this.tradeCodeList = tradeCodeList;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getPresellPrice() {
        return presellPrice;
    }

    public void setPresellPrice(String presellPrice) {
        this.presellPrice = presellPrice;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getZhuiSuMa() {
        return zhuiSuMa;
    }

    public void setZhuiSuMa(String zhuiSuMa) {
        this.zhuiSuMa = zhuiSuMa;
    }

    public String getTongYongMing() {
        return tongYongMing;
    }

    public void setTongYongMing(String tongYongMing) {
        this.tongYongMing = tongYongMing;
    }

    public String getWenHao() {
        return wenHao;
    }

    public void setWenHao(String wenHao) {
        this.wenHao = wenHao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
