package com.housingfund.collection.service.impl;

import com.housingfund.collection.entity.SystemParam;
import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.mapper.ParamMapper;
import com.housingfund.collection.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ParamServiceImpl implements ParamService {

    private static final Set<String> PROTECTED_SEQNAMES = Set.of("UNITACCNUM", "PERACCNUM");

    private final ParamMapper paramMapper;

    @Autowired
    public ParamServiceImpl(ParamMapper paramMapper) {
        this.paramMapper = paramMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemParam> findBySeqnameLike(String seqname) {
        String keyword = trimToNull(seqname);
        if (keyword == null) {
            return paramMapper.selectAll();
        }
        return paramMapper.selectBySeqnameLike(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public SystemParam getBySeqname(String seqname) {
        String normalizedSeqname = requireSeqname(seqname);
        SystemParam param = paramMapper.selectBySeqname(normalizedSeqname);
        if (param == null) {
            throw new BusinessException("参数不存在");
        }
        return param;
    }

    @Override
    @Transactional
    public void add(SystemParam param) {
        validateForSave(param, true);
        if (paramMapper.selectBySeqname(param.getSeqname()) != null) {
            throw new BusinessException("键值信息已存在");
        }
        paramMapper.insert(param);
    }

    @Override
    @Transactional
    public void update(String seqname, SystemParam param) {
        String normalizedSeqname = requireSeqname(seqname);
        if (paramMapper.selectBySeqname(normalizedSeqname) == null) {
            throw new BusinessException("参数不存在");
        }

        param.setSeqname(normalizedSeqname);
        validateForSave(param, false);

        int rows = paramMapper.update(param);
        if (rows == 0) {
            throw new BusinessException("参数不存在");
        }
    }

    @Override
    @Transactional
    public void delete(String seqname) {
        String normalizedSeqname = requireSeqname(seqname);
        if (PROTECTED_SEQNAMES.contains(normalizedSeqname.toUpperCase())) {
            throw new BusinessException("UNITACCNUM 和 PERACCNUM 用于账号生成，禁止删除");
        }

        int rows = paramMapper.deleteBySeqname(normalizedSeqname);
        if (rows == 0) {
            throw new BusinessException("参数不存在");
        }
    }

    private void validateForSave(SystemParam param, boolean creating) {
        if (param == null) {
            throw new BusinessException("参数信息不能为空");
        }
        if (creating) {
            param.setSeqname(requireSeqname(param.getSeqname()));
        } else {
            requireSeqname(param.getSeqname());
        }
        validateSeq(param.getSeq(), "当前序号不能为空", "当前序号必须为正整数");
        validateSeq(param.getMaxseq(), "最大序号不能为空", "最大序号必须为正整数");
        if (param.getSeq() > param.getMaxseq()) {
            throw new BusinessException("当前序号不能大于最大序号");
        }

        String seqDesc = trimToNull(param.getSeqDesc());
        if (seqDesc == null) {
            throw new BusinessException("描述不能为空");
        }
        if (seqDesc.length() > 200) {
            throw new BusinessException("描述不能超过200个字符");
        }
        param.setSeqDesc(seqDesc);

        String freeuse1 = trimToNull(param.getFreeuse1());
        if (freeuse1 != null && freeuse1.length() > 200) {
            throw new BusinessException("备用1不能超过200个字符");
        }
        param.setFreeuse1(freeuse1);
    }

    private String requireSeqname(String seqname) {
        String normalizedSeqname = trimToNull(seqname);
        if (normalizedSeqname == null) {
            throw new BusinessException("键值信息不能为空");
        }
        if (normalizedSeqname.length() > 32) {
            throw new BusinessException("键值信息不能超过32个字符");
        }
        return normalizedSeqname;
    }

    private void validateSeq(Long value, String nullMessage, String invalidMessage) {
        if (value == null) {
            throw new BusinessException(nullMessage);
        }
        if (value <= 0) {
            throw new BusinessException(invalidMessage);
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
