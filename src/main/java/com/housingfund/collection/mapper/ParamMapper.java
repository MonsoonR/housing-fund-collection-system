package com.housingfund.collection.mapper;

import com.housingfund.collection.entity.SystemParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ParamMapper {

    List<SystemParam> selectAll();

    List<SystemParam> selectBySeqnameLike(@Param("seqname") String seqname);

    SystemParam selectBySeqname(@Param("seqname") String seqname);

    int insert(SystemParam param);

    int update(SystemParam param);

    int deleteBySeqname(@Param("seqname") String seqname);
}
