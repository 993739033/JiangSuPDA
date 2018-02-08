package com.mingnong.scanappnew.bean;

import java.util.List;

/**
 * Created by wyw on 2016/11/29.
 * 追溯码扫描访问网络
 */

public class ScanInputNetworkBean extends BaseObjectEntity<ScanInputNetworkBean.DataBean>{


    public static class DataBean {
        private String tradeCode;
        /**
         * mintagunit : 盒
         * chsf : 富明路40号-兴义市黔畜康兽药经营部
         * cxcs : 37
         * zsmtype :
         * jxname : 注射剂
         * pzwh : 兽药字（2015）220442658
         * spm : /
         * yplxname : 抗生素
         * sxrq : 2019/06/12
         * qyname : 四川省泰信动物药业有限公司
         * tmjb : 1
         * tagratio : 1:16
         * specification : 4g（400万单位）
         * dyccxsj :
         * scrq : 2016/06/12
         * ph : 160601
         * cpname : 注射用硫酸链霉素
         * minpackunit : 瓶
         */

        private List<RowsBean> rows;

        public String getTradeCode() {
            return tradeCode;
        }

        public void setTradeCode(String tradeCode) {
            this.tradeCode = tradeCode;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            private String mintagunit;
            private String chsf;
            private int cxcs;
            private String zsmtype;
            private String jxname;
            private String pzwh;
            private String spm;
            private String yplxname;
            private String sxrq;//有效期
            private String qyname;//企业名称
            private int tmjb;
            private String tagratio;
            private String specification;
            private String dyccxsj;
            private String scrq;//生产日期
            private String ph;
            private String cpname;
            private String minpackunit;

            public String getMintagunit() {
                return mintagunit;
            }

            public void setMintagunit(String mintagunit) {
                this.mintagunit = mintagunit;
            }

            public String getChsf() {
                return chsf;
            }

            public void setChsf(String chsf) {
                this.chsf = chsf;
            }

            public int getCxcs() {
                return cxcs;
            }

            public void setCxcs(int cxcs) {
                this.cxcs = cxcs;
            }

            public String getZsmtype() {
                return zsmtype;
            }

            public void setZsmtype(String zsmtype) {
                this.zsmtype = zsmtype;
            }

            public String getJxname() {
                return jxname;
            }

            public void setJxname(String jxname) {
                this.jxname = jxname;
            }

            public String getPzwh() {
                return pzwh;
            }

            public void setPzwh(String pzwh) {
                this.pzwh = pzwh;
            }

            public String getSpm() {
                return spm;
            }

            public void setSpm(String spm) {
                this.spm = spm;
            }

            public String getYplxname() {
                return yplxname;
            }

            public void setYplxname(String yplxname) {
                this.yplxname = yplxname;
            }

            public String getSxrq() {
                return sxrq;
            }

            public void setSxrq(String sxrq) {
                this.sxrq = sxrq;
            }

            public String getQyname() {
                return qyname;
            }

            public void setQyname(String qyname) {
                this.qyname = qyname;
            }

            public int getTmjb() {
                return tmjb;
            }

            public void setTmjb(int tmjb) {
                this.tmjb = tmjb;
            }

            public String getTagratio() {
                return tagratio;
            }

            public void setTagratio(String tagratio) {
                this.tagratio = tagratio;
            }

            public String getSpecification() {
                return specification;
            }

            public void setSpecification(String specification) {
                this.specification = specification;
            }

            public String getDyccxsj() {
                return dyccxsj;
            }

            public void setDyccxsj(String dyccxsj) {
                this.dyccxsj = dyccxsj;
            }

            public String getScrq() {
                return scrq;
            }

            public void setScrq(String scrq) {
                this.scrq = scrq;
            }

            public String getPh() {
                return ph;
            }

            public void setPh(String ph) {
                this.ph = ph;
            }

            public String getCpname() {
                return cpname;
            }

            public void setCpname(String cpname) {
                this.cpname = cpname;
            }

            public String getMinpackunit() {
                return minpackunit;
            }

            public void setMinpackunit(String minpackunit) {
                this.minpackunit = minpackunit;
            }
        }
    }
}
