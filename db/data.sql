USE housingfund_collection;

INSERT INTO tb001 (SEQNAME, SEQ, MAXSEQ, SEQ_DESC, FREEUSE1)
VALUES
    ('UNITACCNUM', 1, 999999999999, '单位账号序号', '账号生成参数，禁止删除'),
    ('PERACCNUM', 1, 999999999999, '个人账号序号', '账号生成参数，禁止删除') AS new_param
ON DUPLICATE KEY UPDATE
    MAXSEQ = new_param.MAXSEQ,
    SEQ_DESC = new_param.SEQ_DESC,
    FREEUSE1 = new_param.FREEUSE1;
