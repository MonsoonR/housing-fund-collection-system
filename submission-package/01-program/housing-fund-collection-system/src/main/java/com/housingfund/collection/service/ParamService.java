package com.housingfund.collection.service;

import com.housingfund.collection.entity.SystemParam;

import java.util.List;

public interface ParamService {

    List<SystemParam> findBySeqnameLike(String seqname);

    SystemParam getBySeqname(String seqname);

    void add(SystemParam param);

    void update(String seqname, SystemParam param);

    void delete(String seqname);
}
