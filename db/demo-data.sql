USE housingfund_collection;

-- Demo data for final Tomcat acceptance and defense walkthrough.
-- The script only clears the 000000900xxx demo account range and the demo id cards below.

DELETE FROM TB003
WHERE `UNITACCNUM` BETWEEN '000000900001' AND '000000900099'
   OR `ACCNUM` BETWEEN '000000900001' AND '000000900099'
   OR `IDCARD` IN (
        '110105199001010010',
        '110105199202020020',
        '110105199303030033',
        '110105199404040046'
   );

DELETE FROM TB002
WHERE `UNITACCNUM` BETWEEN '000000900001' AND '000000900099'
   OR `ORGCODE` IN ('DUNIT0001', 'DUNIT0002', 'DUNIT0099');

UPDATE TB001
SET `SEQ` = CASE WHEN `SEQ` < 900100 THEN 900100 ELSE `SEQ` END
WHERE `SEQNAME` = 'UNITACCNUM';

UPDATE TB001
SET `SEQ` = CASE WHEN `SEQ` < 900100 THEN 900100 ELSE `SEQ` END
WHERE `SEQNAME` = 'PERACCNUM';

INSERT INTO TB002 (
    `UNITACCNUM`, `UNITACCNAME`, `UNITADDR`, `ORGCODE`, `UNITCHAR`, `UNITKIND`,
    `SALARYDATE`, `UNITPHONE`, `UNITLINKMAN`, `UNITAGENTPAPNO`, `ACCSTATE`,
    `BALANCE`, `BASENUMBER`, `UNITPROP`, `PERPROP`, `UNITPAYSUM`, `PERPAYSUM`,
    `PERSNUM`, `LASTPAYDATE`, `INSTCODE`, `OP`, `CREATDATE`, `REMARK`
) VALUES
    (
        '000000900001', '演示正常单位', '合肥市演示路1号', 'DUNIT0001', '1', '150',
        '15', '0551-9000001', '演示经办人一', '11010519491231002X', '0',
        2880.00, 18000.00, 0.080, 0.080, 1440.00, 1440.00,
        3, '2026-05-01', '0110', '111111', '2026-06-01',
        '演示数据：正常单位和个人开户单位'
    ),
    (
        '000000900002', '演示资料修改单位', '合肥市演示路2号', 'DUNIT0002', '1', '150',
        '20', '0551-9000002', '演示经办人二', '11010519491231002X', '0',
        0.00, 0.00, 0.070, 0.070, 0.00, 0.00,
        0, '1899-12-01', '0110', '111111', '2026-06-01',
        '演示数据：用于单位资料修改'
    ),
    (
        '000000900099', '演示销户单位', '合肥市演示路99号', 'DUNIT0099', '1', '150',
        '25', '0551-9000099', '演示经办人三', '11010519491231002X', '9',
        0.00, 0.00, 0.080, 0.080, 0.00, 0.00,
        0, '1899-12-01', '0110', '111111', '2026-06-01',
        '演示数据：用于已销户单位不能修改校验'
    );

INSERT INTO TB003 (
    `ACCNUM`, `UNITACCNUM`, `PERNAME`, `IDTYPE`, `IDCARD`, `OPENDATE`,
    `BALANCE`, `PERACCSTATE`, `BASENUMBER`, `UNITPROP`, `INDIPROP`, `LASTPAYDATE`,
    `UNITMONPAYSUM`, `PERMONPAYSUM`, `YPAYAMT`, `YDRAWAMT`, `YINTERESTBAL`,
    `INSTCODE`, `OP`, `REMARK`
) VALUES
    (
        '000000900001', '000000900001', '演示张三', '01身份证', '110105199001010010', '2026-06-01',
        800.00, '0', 5000.00, 0.080, 0.080, '2026-05-01',
        400.00, 400.00, 0.00, 0.00, 0.00,
        '0110', '111111', '演示数据：正常个人'
    ),
    (
        '000000900002', '000000900001', '演示李四', '01身份证', '110105199202020020', '2026-06-01',
        960.00, '0', 6000.00, 0.080, 0.080, '2026-05-01',
        480.00, 480.00, 0.00, 0.00, 0.00,
        '0110', '111111', '演示数据：正常个人'
    ),
    (
        '000000900003', '000000900001', '演示王五', '01身份证', '110105199303030033', '2026-06-01',
        1120.00, '0', 7000.00, 0.080, 0.080, '2026-05-01',
        560.00, 560.00, 0.00, 0.00, 0.00,
        '0110', '111111', '演示数据：身份证冲突占用账户'
    ),
    (
        '000000900004', '000000900001', '演示赵六', '01身份证', '110105199404040046', '2026-06-01',
        0.00, '9', 5500.00, 0.080, 0.080, '1899-12-01',
        440.00, 440.00, 0.00, 0.00, 0.00,
        '0110', '111111', '演示数据：销户个人重新启用'
    );
