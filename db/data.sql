USE housingfund_collection;

INSERT INTO TB001 (SEQNAME, SEQ, MAXSEQ, `DESC`, FREEUSE1)
VALUES
    ('UNITACCNUM', 1, 999999999, '公积金单位账号序号', '账号生成参数，禁止删除'),
    ('PERACCNUM', 1, 999999999, '公积金个人账号序号', '账号生成参数，禁止删除') AS new_param
ON DUPLICATE KEY UPDATE
    MAXSEQ = new_param.MAXSEQ,
    `DESC` = new_param.`DESC`,
    FREEUSE1 = new_param.FREEUSE1;
