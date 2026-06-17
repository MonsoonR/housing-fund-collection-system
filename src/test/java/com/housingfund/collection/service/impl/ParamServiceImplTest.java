package com.housingfund.collection.service.impl;

import com.housingfund.collection.entity.SystemParam;
import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.mapper.ParamMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParamServiceImplTest {

    @Test
    public void addRejectsSeqGreaterThanMaxSeq() {
        ParamServiceImpl service = new ParamServiceImpl(new FakeParamMapper());

        try {
            service.add(buildParam("NORMAL", 10L, 5L, "普通参数"));
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("当前序号不能大于最大序号", ex.getMessage());
        }
    }

    @Test
    public void addRejectsDuplicateSeqname() {
        FakeParamMapper mapper = new FakeParamMapper();
        mapper.insert(buildParam("EXISTS", 1L, 100L, "已存在参数"));
        ParamServiceImpl service = new ParamServiceImpl(mapper);

        try {
            service.add(buildParam("EXISTS", 2L, 100L, "重复参数"));
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("键值信息已存在", ex.getMessage());
        }
    }

    @Test
    public void updateKeepsSeqnameAndValidatesRequiredDescription() {
        FakeParamMapper mapper = new FakeParamMapper();
        mapper.insert(buildParam("P001", 1L, 100L, "原描述"));
        ParamServiceImpl service = new ParamServiceImpl(mapper);
        SystemParam form = buildParam("SHOULD_NOT_CHANGE", 2L, 100L, " ");

        try {
            service.update("P001", form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("描述不能为空", ex.getMessage());
        }

        form.setSeqDesc("新描述");
        service.update("P001", form);

        SystemParam saved = mapper.selectBySeqname("P001");
        assertEquals("P001", saved.getSeqname());
        assertEquals("新描述", saved.getSeqDesc());
    }

    @Test
    public void deleteRejectsAccountSequenceParams() {
        ParamServiceImpl service = new ParamServiceImpl(new FakeParamMapper());

        try {
            service.delete("UNITACCNUM");
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("UNITACCNUM 和 PERACCNUM 用于账号生成，禁止删除", ex.getMessage());
        }

        try {
            service.delete("PERACCNUM");
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("UNITACCNUM 和 PERACCNUM 用于账号生成，禁止删除", ex.getMessage());
        }
    }

    @Test
    public void findBySeqnameLikeReturnsMatchingParams() {
        FakeParamMapper mapper = new FakeParamMapper();
        mapper.insert(buildParam("UNITACCNUM", 1L, 999999999999L, "单位账号序号"));
        mapper.insert(buildParam("PERACCNUM", 1L, 999999999999L, "个人账号序号"));
        mapper.insert(buildParam("NORMAL", 1L, 9L, "普通参数"));
        ParamServiceImpl service = new ParamServiceImpl(mapper);

        List<SystemParam> result = service.findBySeqnameLike("ACC");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(param -> param.getSeqname().contains("ACC")));
    }

    private static SystemParam buildParam(String seqname, Long seq, Long maxseq, String seqDesc) {
        SystemParam param = new SystemParam();
        param.setSeqname(seqname);
        param.setSeq(seq);
        param.setMaxseq(maxseq);
        param.setSeqDesc(seqDesc);
        param.setFreeuse1("备用");
        return param;
    }

    private static class FakeParamMapper implements ParamMapper {
        private final Map<String, SystemParam> data = new LinkedHashMap<>();

        @Override
        public List<SystemParam> selectAll() {
            return new ArrayList<>(data.values());
        }

        @Override
        public List<SystemParam> selectBySeqnameLike(String seqname) {
            List<SystemParam> result = new ArrayList<>();
            for (SystemParam param : data.values()) {
                if (param.getSeqname().contains(seqname)) {
                    result.add(param);
                }
            }
            return result;
        }

        @Override
        public SystemParam selectBySeqname(String seqname) {
            return data.get(seqname);
        }

        @Override
        public SystemParam selectBySeqnameForUpdate(String seqname) {
            return data.get(seqname);
        }

        @Override
        public int insert(SystemParam param) {
            data.put(param.getSeqname(), copy(param));
            return 1;
        }

        @Override
        public int update(SystemParam param) {
            data.put(param.getSeqname(), copy(param));
            return 1;
        }

        @Override
        public int updateSeq(String seqname, Long seq) {
            SystemParam param = data.get(seqname);
            if (param == null) {
                return 0;
            }
            param.setSeq(seq);
            return 1;
        }

        @Override
        public int deleteBySeqname(String seqname) {
            return data.remove(seqname) == null ? 0 : 1;
        }

        private SystemParam copy(SystemParam source) {
            SystemParam target = new SystemParam();
            target.setSeqname(source.getSeqname());
            target.setSeq(source.getSeq());
            target.setMaxseq(source.getMaxseq());
            target.setSeqDesc(source.getSeqDesc());
            target.setFreeuse1(source.getFreeuse1());
            return target;
        }
    }
}
