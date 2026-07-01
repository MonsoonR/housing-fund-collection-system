package com.housingfund.collection.entity;

import java.io.Serializable;

public class SystemParam implements Serializable {

    private String seqname;
    private Long seq;
    private Long maxseq;
    private String seqDesc;
    private String freeuse1;

    public String getSeqname() {
        return seqname;
    }

    public void setSeqname(String seqname) {
        this.seqname = seqname;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getMaxseq() {
        return maxseq;
    }

    public void setMaxseq(Long maxseq) {
        this.maxseq = maxseq;
    }

    public String getSeqDesc() {
        return seqDesc;
    }

    public void setSeqDesc(String seqDesc) {
        this.seqDesc = seqDesc;
    }

    public String getFreeuse1() {
        return freeuse1;
    }

    public void setFreeuse1(String freeuse1) {
        this.freeuse1 = freeuse1;
    }
}
