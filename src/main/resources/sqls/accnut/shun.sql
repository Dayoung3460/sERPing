2025-02-11

-- ���̺� ���� ��ɹ�
select 'drop table'|| ' ' || table_name || ';'
from user_tables;

-- ���̺� �÷� �߰� ��ɹ�
select 'ALTER TABLE '|| table_name ||' ADD company_num NUMBER(6,0);'
from user_tables
where table_name like 'ACCNUT_'||'%';

create table accnut_assets(
    assets_code varchar2(50) primary key,
    assets_name varchar2(100) not null,
    section varchar2(20) not null,
    financial_institution varchar2(20),
    finance_information varchar2(30),
    owner varchar2(100),
    amount number(12,0),
    register_date date default sysdate,
    quantity number(10),
    fixtures_amount number(12,0)
);
insert into accnut_assets( assets_code, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount)
values ('a1001','�ʱ� ���� ����1','AC01','FI01','-','����',1000, sysdate, 100, 10);
insert into accnut_assets( assets_code, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount)
values ('a1002','�ʱ� ���� ����2','AC01','FI01','-','����',10000, sysdate, 100, 100);
insert into accnut_assets( assets_code, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount)
values ('a1003','�ʱ� ���� ����3','AC01','FI01','-','����',100000, sysdate, 100, 1000);
insert into accnut_assets( assets_code, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount)
values ('a1004','�ʱ� ���� ����4','AC01','FI01','-','����',500000, sysdate, 100, 5000);
insert into accnut_assets( assets_code, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount)
values ('a1005','�ʱ� ���� ����5','AC01','FI01','-','����',1000000, sysdate, 100, 10000);
insert into accnut_assets( assets_code, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount)
values ('a1006','�ʱ� ���� ����6','AC01','FI01','-','����',5000000, sysdate, 100, 50000);
insert into accnut_assets( assets_code, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount)
values ('a1007','�ʱ� ���� ����7','AC01','FI01','-','����',10000000, sysdate, 100, 100000);

select assets_code, assets_name, fn_get_cmmn_name(section), fn_get_cmmn_code(fn_get_cmmn_name(financial_institution)), finance_information, owner, amount, register_date, quantity, fixtures_amount
from accnut_assets;

commit;

insert into cmmn(cmmn_code,cmmn_name)
values ('AC', '��������');
insert into cmmn(cmmn_code,upper_cmmn_code, cmmn_name)
values ('AC01','AC', '����');
insert into cmmn(cmmn_code, cmmn_name)
values ('FI', '�����������');
insert into cmmn(cmmn_code,upper_cmmn_code, cmmn_name)
values ('FI01','FI' , '����');

select * from cmmn;


-- 2025-02-12
select * from accnut_assets;
update accnut_assets
set company_num = 0;
commit;

create SEQUENCE assets_seq;


INSERT INTO accnut_assets
SELECT assets_seq.NEXTVAL, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount, company_num
FROM accnut_assets;

update accnut_assets set company_num = 1;

commit;

select fn_get_cmmn_code('��')
from dual;

select * from erp_company;
select * from hr_employee;


SELECT debt_code, debt_name, section, creditor, amount, interest, register_date
FROM (SELECT /*+INDEX_DESC(accnut_debt pk_accnut_debt)*/ rownum rn, debt_code, debt_name, fn_get_cmmn_name(section) as section, creditor, amount, interest, register_date
		FROM accnut_debt
        WHERE rownum <= 40 
        AND company_num = 0
--        AND rownum >=1
        )
WHERE rn >= 21
;

select * from accnut_dealings_account_book;


-- 2025-02-14
SELECT dealings_account_book_code, section, types_of_transaction, amount, vat_alternative, dealings_contents, deal_date, department, company_num
FROM accnut_dealings_account_book
--;
WHERE to_char(deal_date, 'yyyy-mm-dd') = '2025-02-14'
;
--WHERE dealings_account_book_code = #{dealingsAccountBookCode}
--;
select to_char(to_timestamp('02/14/2025 00:00:00.000', 'mm/dd/yyyy hh24:mi:ss.ff3'), 'yyyy-mm-dd') from dual;
create sequence accnut_dealings_book_seq;
commit;

insert into accnut_dealings_account_book values (ACCNUT_DEALINGS_BOOK_SEQ.nextval, fn_get_cmmn_code('��ä'), fn_get_cmmn_code('����'), 100000, 'Y', 'Aȸ�� �����ޱ�1', sysdate, fn_get_cmmn_code('����'), 0);

insert into accnut_dealings_account_book
select ACCNUT_DEALINGS_BOOK_SEQ.nextval, section, types_of_transaction, amount, vat_alternative, dealings_contents, deal_date, department, company_num
from accnut_dealings_account_book;

select fn_get_cmmn_code('����') from dual;
select * from cmmn;
select * from cmmn where cmmn_code like upper('%dt%');
select * from cmmn where cmmn_code like upper('%ee%');
select * from cmmn where cmmn_code like upper('%py%');
select * from cmmn where cmmn_code like upper('%pc%');
select * from cmmn where cmmn_code like upper('%ac%');
select * from cmmn where cmmn_code like upper('%ca%');

insert into cmmn(cmmn_code, cmmn_name, description) values ('FI','�����������','���� �� ī���');
insert into cmmn(cmmn_code, cmmn_name, description) values ('EE','���Ϳ��α���','�������� �������� �ڻ����� ��ä����');
insert into cmmn(cmmn_code, cmmn_name, description) values ('PY','���޿��α���','���� ������');
insert into cmmn(cmmn_code, cmmn_name, description) values ('PC','ó�����α���','ó�� �ݷ� ��ó��');
insert into cmmn(cmmn_code, cmmn_name, description) values ('AC','�������񱸺�','� ������������');
insert into cmmn(cmmn_code, cmmn_name, description) values ('CA','ī�屸��','�������� ��������');

insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('FI01','FI','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('FI02','FI','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('FI03','FI','���');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('FI04','FI','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('FI05','FI','IM(�뱸)');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('FI06','FI','�ϳ�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('FI07','FI','�츮');

insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('EE01','EE','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('EE02','EE','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('EE03','EE','�ڻ�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('EE04','EE','��ä');

insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('PY01','PY','������');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('PY02','PY','����');

insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('PC01','PC','��ó��');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('PC02','PC','ó��');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('PC03','PC','�ݷ�');

insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('CA01','CA','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('CA02','CA','����');

insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC01','AC','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC02','AC','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC03','AC','ī��');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC04','AC','��ǰ');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC05','AC','����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC06','AC','�ǹ�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC07','AC','�����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC08','AC','�����ޱ�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC09','AC','�ܻ���Ա�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC10','AC','������');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC11','AC','������');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC12','AC','��ä');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC13','AC','�Ӵ뺸����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC14','AC','�ں���');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC15','AC','�����׿���');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC16','AC','�����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC17','AC','�뿪����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC18','AC','���ڼ���');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC19','AC','�޿�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC20','AC','������');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC21','AC','�Ǹź�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC22','AC','�����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC23','AC','��ݺ�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC24','AC','�����󰢺�');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC25','AC','���μ���');

select * from accnut_salary_account_book;

select * from accnut_debt where section = 'AC08';
commit;

-- 2025-02-17

create sequence accnut_salary_book_seq;

SELECT * FROM accnut_salary_account_book;
insert into accnut_salary_account_book(SALARY_ACCOUNT_BOOK_CODE, EMPLOYEE_CODE, EMPLOYEE_NAME, SALARY, PAYMENT_AMOUNT, PAYMENT_ALTERNATIVE)
values(ACCNUT_SALARY_BOOK_SEQ.nextval, 'emp003', 'ȫ�浿',5000, 5000, 'PY02');


SELECT lower(column_name)  as "�÷���", data_type, nullable FROM COLS where  lower(table_name) = 'accnut_incidental_cost' order by column_id;


select * from accnut_etc_payment;

INSERT INTO accnut_etc_payment(ETC_PAYMENT_CODE, SECTION, TIME_LIMIT, AMOUNT, PAYMENT_ALTERNATIVE, COMPANY_NUM)
VALUES (ACCNUT_ETC_PAYMENT_SEQ.nextval, 'AC20', sysdate, 5000, 'PY01', 0);

CREATE SEQUENCE accnut_etc_payment_seq;

select * from cmmn where upper_cmmn_code like 'CA';
select * from cmmn where cmmn_name like '%ó��%';

insert into cmmn(cmmn_code , upper_cmmn_code, cmmn_name)
values ('AC26', 'AC', '���');
commit;

create sequence accnut_incidental_seq;

insert into accnut_incidental_cost(incidental_cost_code, section, card_num, amount, register_date, process_alternative, company_num)
values( ACCNUT_INCIDENTAL_SEQ.nextval, 'CA02', '123456789' , 1000, sysdate, 'PC01', 0);

select * from accnut_assets;
update accnut_assets set section = 'AC04' where assets_code = '3';

select * from cmmn where upper_cmmn_code = 'AC';
select * from cmmn where upper_cmmn_code = 'EE';


-- 2025-02-18

select * from accnut_assets;

SELECT /*+INDEX_DESC(ACCNUT_ASSETS PK_ACCNUT_ASSETS)*/ rownum rn, assets_code, assets_name, fn_get_cmmn_name(section) as section, fn_get_cmmn_name(financial_institution) as financial_institution, finance_information, owner, NVL(amount,0) as amount, register_date, NVL(quantity, 0) as quantity, NVL(fixtures_amount, 0) as fixtures_amount 
		      FROM accnut_assets;
              
UPDATE ACCNUT_ASSETS SET assets_code = '0' + assets_code where TO_NUMBER(assets_code, '999999') < 10;
Commit;
select * from accnut_dealings_account_book order by 1 desc;
select accnut_dealings_book_seq.nextval from dual;

update (select a.quantity as asd, b.quantity as def
        from bhf_warehouse a, bhf_returning_detail b
        where a.option_code = b.option_code) c
set c.asd = (c.asd - c.def);

UPDATE (SELECT a.quantity as asd, b.quantity as def, a.warehouse_code, b.returning_detail_code
        FROM bhf_warehouse a
        JOIN bhf_returning_detail b ON a.option_code = b.option_code)
SET asd = (asd - def);

-- 2025-02-20

select * from accnut_assets order by 1;
select * from cmmn where upper_cmmn_code like '%FI%';

select * from accnut_dealings_account_book order by 1;

select b.*
from (select a.*, rownum rn
     from (select * 
            from cmmn 
            order by 1) a)b
where b.rn <= 20
and b.rn >= 16;

select * from hr_employee;

select * from cmmn;
select * from hr_department;


SELECT c.* , d.*
FROM bhf_closing c RIGHT JOIN bhf_closing_detail d
ON (c.closing_code = d.closing_code);

SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION test_json
RETURN CLOB
IS
    v_json_closing CLOB;
    v_json_returning CLOB;
    v_json_order CLOB;
    v_final_json CLOB;
BEGIN
    -- BHF_CLOSING �����͸� JSON �������� ��ȯ
    v_json_closing := '[';
    FOR rec IN (SELECT closing_code, branch_office_id, closing_date FROM bhf_closing) LOOP
        v_json_closing := v_json_closing || 
                          '{"closing_code":"' || rec.closing_code || 
                          '","branch_office_id":"' || rec.branch_office_id || 
                          '","closing_date":"' || rec.closing_date || '"},';
    END LOOP;
    IF LENGTH(v_json_closing) > 1 THEN
        v_json_closing := SUBSTR(v_json_closing, 1, LENGTH(v_json_closing) - 1);
    END IF;
    v_json_closing := v_json_closing || ']';

    -- BHF_RETURNING �����͸� JSON �������� ��ȯ
    v_json_returning := '[';
    FOR rec IN (SELECT returning_code, progress_status, request_date FROM bhf_returning) LOOP
        v_json_returning := v_json_returning || 
                            '{"returning_code":"' || rec.returning_code || 
                            '","progress_status":"' || rec.progress_status || 
                            '","request_date":"' || rec.request_date || '"},';
    END LOOP;
    IF LENGTH(v_json_returning) > 1 THEN
        v_json_returning := SUBSTR(v_json_returning, 1, LENGTH(v_json_returning) - 1);
    END IF;
    v_json_returning := v_json_returning || ']';

    -- BHF_ORDER �����͸� JSON �������� ��ȯ
    v_json_order := '[';
    FOR rec IN (SELECT order_code, order_date FROM bhf_order) LOOP
        v_json_order := v_json_order || 
                        '{"order_code":"' || rec.order_code || 
                        '","order_date":"' || rec.order_date || '"},';
    END LOOP;
    IF LENGTH(v_json_order) > 1 THEN
        v_json_order := SUBSTR(v_json_order, 1, LENGTH(v_json_order) - 1);
    END IF;
    v_json_order := v_json_order || ']';

    -- ���� JSON ������ ����
    v_final_json := '{"bhf_closing": ' || v_json_closing || ', "bhf_returning": ' || v_json_returning || ', "bhf_order": ' || v_json_order || '}';

    RETURN v_final_json;
END test_json;
/

select test_json() from dual;

SELECT bc.* , bcd.*
FROM bhf_closing bc JOIN bhf_closing_detail bcd
ON (bc.closing_code = bcd.closing_code);

SELECT bo.*, bod.*
FROM bhf_order bo JOIN bhf_order_detail bod
ON (bo.order_code = bod.order_code);

SELECT br.*, brd.*
FROM bhf_returning br JOIN bhf_returning_detail brd
ON (br.returning_code = brd.returning_code);

select * from accnut_assets where financial_institution LIKE '%FI%' AND section = 'AC02';
select * from accnut_assets where section = 'AC02';
update accnut_assets set assets_name = '���ޱ޿�����', amount=0, quantity=null, fixtures_amount=0 where assets_code = '01';
update accnut_assets set rgno = '20250224000002849' where assets_code = '121';
commit;
delete from accnut_assets where assets_code = '124';
alter table accnut_assets add rgno varchar2(1000);

-- 2025-02-25

select * from accnut_salary_account_book;
update accnut_salary_account_book set payment_alternative = 'PY02', payer = '';
commit;
select * from cmmn where cmmn_code LIKE 'PY%';

-- ���� ��ȸ => ���� ����, ��ǰ��, ��ǰ ��� ����
SELECT bc.*, bcd.*
FROM bhf_closing bc JOIN bhf_closing_detail bcd
ON (bc.closing_code = bcd.closing_code);

SELECT *
FROM bhf_goods_mediation;
update bhf_goods_mediation set company_num = 100 WHERE mediation_code = 'medaiation81';
commit;

SELECT br.*, brd.*
FROM bhf_returning br JOIN bhf_returning_detail brd
ON (br.returning_code = brd.returning_code)
WHERE brd.returning_reason IN ('�ļ�', '��׷���');

SELECT  -- br.request_date,
        TO_CHAR(br.request_date, 'YYYY-MM-DD') request_date,
        --'2��' AS requst_date,
        br.branch_office_id,
        brd.option_code,
        brd.goods_name || '-' || brd.option_name option_name,
        sum( brd.quantity )
FROM bhf_returning br JOIN bhf_returning_detail brd
ON (br.returning_code = brd.returning_code)
WHERE brd.returning_reason IN ('�ļ�', '��׷���')
--AND TO_CHAR(br.request_date, 'YYYY-MM-DD') LIKE '2025-02-%'
GROUP BY  
TO_CHAR(br.request_date, 'YYYY-MM-DD'),
-- br.request_date, 
br.branch_office_id, 
brd.option_code, 
(brd.goods_name || '-' || brd.option_name)
--HAVING br.request_date BETWEEN '2025-02-01' AND '2025-02-20'
HAVING brd.goods_code = 'P002'
ORDER BY br.branch_office_id;

SELECT TO_CHAR(result_date, 'YYYY-MM-DD') result_date,
       a.office_id, 
       a.option_code, 
       a.option_name, 
       SUM(a.qy)
FROM (SELECT br.request_date result_date, 
             br.branch_office_id office_id,
             brd.option_code  option_code,
             brd.goods_name || '-' || brd.option_name AS option_name ,
             brd.quantity AS qy
      FROM bhf_returning br JOIN bhf_returning_detail brd
             ON (br.returning_code = brd.returning_code)
      WHERE brd.returning_reason IN ('�ļ�', '��׷���') )a
GROUP BY TO_CHAR(result_date, 'YYYY-MM-DD'),
         office_id, 
         option_code, 
         option_name
-- HAVING option_code = 'LH0011'
ORDER BY result_date desc, 
         office_id, 
         option_code 

;
-- UNION ALL

SELECT TO_CHAR(result_date, 'YYYY-MM-DD') as result_date,
       b.office_id,
       b.option_code,
       b.option_name,
       SUM(b.qy)
FROM (SELECT mediation_date result_date, 
            branch_office_id as office_id, 
            option_code, 
            goods_name || '-' || option_name as option_name, 
            NVL(quantity, 0) - NVL(mediation_quantity, 0) as qy
      FROM bhf_goods_mediation) b
GROUP BY TO_CHAR(result_date, 'YYYY-MM-DD'),
         office_id, 
         option_code, 
         option_name
-- HAVING option_code = 'LH0011'
ORDER BY result_date desc, 
         office_id, 
         option_code 
;





create or replace FUNCTION fn_get_goods_code(p_option_code number)
    RETURN number
IS
    v_result number;
BEGIN
    SELECT pg.goods_num
    INTO v_result
    FROM purchse_goods pg JOIN purchse_option po
    ON (pg.goods_num = po.goods_num)
    WHERE option_num = p_option_code;

    RETURN v_result;
END;
/

-- 2025-02-26

select fn_get_cmmn_code('���')
from dual;

select fn_get_cmmn_code_ver2('����', '����')
from dual;

select * from cmmn where cmmn_name = '����';

SELECT option_code, option_name
FROM purchse_option po JOIN purchse_goods pg
            ON (po.goods_num = pg.goods_num)
WHERE pg.company_num = 1 -- #{companyNum}
-- AND pg.goods_name LIKE '%' || null || '%'
ORDER BY 2;


select * from bsn_bhf;

select fn_get_cmmn_name(rental_type)
FROM bsn_bhf;

select * from cmmn;

-- 2025-02-27

SELECT
    * FROM bsn_bhf;

select * from hr_employee;

-- ���Ͽ����� �� ���̺� ��ĥ��
SELECT b.result_date, b.office_id, b.sell_qy, b.minus_qy, b.total_price
FROM (  SELECT rownum rn, a.*
        FROM (  SELECT -- 'asd' AS result_date,
                       TRUNC(c.result_date) result_date,
                       c.office_id,
                       SUM(c.sell_qy) AS sell_qy,
                       SUM(c.qy) AS minus_qy,
                       SUM(c.total_price) AS total_price
                FROM (SELECT br.branch_office_id AS office_id,
                             brd.option_code,
                             brd.goods_name || '-' || brd.option_name AS option_name,
                             br.request_date AS result_date, 
                             0 - NVL(brd.quantity, 0) AS qy,
                             0 AS sell_qy,
                             company_num,
                             fn_get_option_price(brd.option_code) * (0 - NVL(brd.quantity, 0)) AS total_price
                      FROM bhf_returning br JOIN bhf_returning_detail brd
                             ON (br.returning_code = brd.returning_code)
                      WHERE brd.returning_reason IN ('�ļ�', '��׷���')
                UNION ALL
                      SELECT branch_office_id AS office_id, 
                             option_code, 
                             goods_name || '-' || option_name AS option_name, 
                             mediation_date AS result_date, 
                             NVL(quantity, 0) - NVL(mediation_quantity, 0) AS qy,
                             0 AS sell_qy,
                             company_num ,
                             fn_get_option_price(option_code) * (NVL(quantity, 0) - NVL(mediation_quantity, 0)) AS total_price
                      FROM bhf_goods_mediation
                UNION ALL
                        SELECT bc.branch_office_id AS office_id,
                               bcd.option_code,
                               bcd.goods_name || '-' ||bcd.option_name AS option_name,
                               bc.closing_date AS result_date,
                               0 AS qy,
                               bcd.bnf_sle_qy AS sell_qy,
                               company_num,
                               fn_get_option_price(bcd.option_code) * (bcd.bnf_sle_qy) AS total_price
                        FROM bhf_closing bc JOIN bhf_closing_detail bcd
                        ON (bc.closing_code = bcd.closing_code)
                      ) c
                WHERE c.company_num = 1
                GROUP BY TRUNC(result_date),
                         office_id 
                -- HAVING SUM(c.sell_qy) > 0
                ORDER BY result_date desc, 
                         office_id 
                         ) a ) b
WHERE b.rn >= 1
AND b.rn < 41
;

SELECT goods_price
FROM purchse_goods pg JOIN  purchse_option po
ON (pg.goods_num = po.goods_num)
WHERE po.option_code = 'LH0011';

create or replace function fn_get_option_price(p_option_code varchar2)
return int
is
 v_result number;
begin
    SELECT goods_price
    INTO v_result
    FROM purchse_goods pg JOIN  purchse_option po
    ON (pg.goods_num = po.goods_num)
    WHERE po.option_code = p_option_code;
    
    return v_result;
end;
/

SELECT -- 'asd' AS result_date,
                       TRUNC(c.result_date) result_date,
                       c.office_id,
                       SUM(c.sell_qy) AS sell_qy,
                       SUM(c.qy) AS minus_qy,
                       SUM(c.total_qy) AS total_qy
                FROM (SELECT br.branch_office_id AS office_id,
                             brd.option_code,
                             brd.goods_name || '-' || brd.option_name AS option_name,
                             br.request_date AS result_date, 
                             0 - NVL(brd.quantity, 0) AS qy,
                             0 AS sell_qy,
                             company_num,
                             fn_get_option_price(brd.option_code) * (0 - NVL(brd.quantity, 0)) AS total_price
                      FROM bhf_returning br JOIN bhf_returning_detail brd
                             ON (br.returning_code = brd.returning_code)
                      WHERE brd.returning_reason IN ('�ļ�', '��׷���')
                UNION ALL
                      SELECT branch_office_id AS office_id, 
                             option_code, 
                             goods_name || '-' || option_name AS option_name, 
                             mediation_date AS result_date, 
                             NVL(quantity, 0) - NVL(mediation_quantity, 0) AS qy,
                             0 AS sell_qy,
                             company_num ,
                             fn_get_option_price(option_code) * (NVL(quantity, 0) - NVL(mediation_quantity, 0)) AS total_price
                      FROM bhf_goods_mediation
                UNION ALL
                        SELECT bc.branch_office_id AS office_id,
                               bcd.option_code,
                               bcd.goods_name || '-' ||bcd.option_name AS option_name,
                               bc.closing_date AS result_date,
                               0 AS qy,
                               bcd.bnf_sle_qy AS sell_qy,
                               company_num,
                               fn_get_option_price(bcd.option_code) * (bcd.bnf_sle_qy) AS total_price
                        FROM bhf_closing bc JOIN bhf_closing_detail bcd
                        ON (bc.closing_code = bcd.closing_code)
                      ) c
                WHERE c.company_num = 1
                GROUP BY TRUNC(result_date),
                         office_id 
                -- HAVING SUM(c.sell_qy) > 0
                ORDER BY result_date desc, 
                         office_id
;

select To_DATE('2025-02-25') - 2 from dual;

-- 2025-02-28

SELECT business_num AS "bsnNum",
company_name || '(' || company_eng_name || ')' AS "companyName",
representation_name AS "representor",
company_address AS "address",
'���Ҹ� �� �Һ��ڿ�ǰ ������' AS "status",
'ȭ��ǰ' AS "cate",
charger_email AS "email"
FROM erp_company
WHERE company_num = 1;




SELECT -- option_code AS "optionCode", option_name AS "optionName"
        -- goods_price AS "goodsPrice", goods_standard AS "goodsStandard", goods_name AS "goodsName"
        *
		FROM purchse_option po JOIN purchse_goods pg
		            ON (po.goods_num = pg.goods_num)
		WHERE pg.company_num = 1
		AND pg.goods_name LIKE '%' || '' || '%'
		ORDER BY 2;
        
        SELECT -- bhf_id AS "bhfId", bhf_name AS "bhfName"
        -- *
        bhf_phone AS "bhfPhone", bhf_address AS "bhfAddress", '���Ҹ�' AS "status", 'ȭ��ǰ' AS "cate"
		FROM bsn_bhf
		-- WHERE company_num = #{companyNum}
		ORDER BY 2;
        
        
-- 2025-03-04

select * from accnut_salary_account_book;

insert into accnut_salary_account_book
select ACCNUT_SALARY_BOOK_SEQ.nextval, employee_code, employee_name, department, salary,  excess_allowance, bonus, incidental_cost, deduction_item, payment_amount, payment_prearranged_date, payment_alternative, payer, company_num
from accnut_salary_account_book;
commit;

select * from hr_department;
select * from bsn_bhf;
select * from hr_employee;

SELECT department_num AS "departmentNum", department_name AS "departmentName"
FROM hr_department
WHERE company_num = #{companyNum}
AND parent_department_num IS NOT NULL
;

create or replace function fn_get_dept_name(p_dept varchar2, p_com number)
return varchar2
is
    v_result varchar(1000);
begin
    select department_name
    into v_result
    from hr_department
    where company_num = p_com
    and department_num = p_dept;

    return v_result;
end;
/


SELECT assets_code, assets_name, section, financial_institution, finance_information, owner, amount, register_date, quantity, fixtures_amount, rgno
		FROM accnut_assets
		WHERE assets_name Like '%' || '�޿�' || '%'
		AND company_num = 1
		and rownum = 1;
        
        
        
select * from hr_department;

select * from accnut_assets;

SELECT NVL(MAX(tax_num), 0)
FROM accnut_tax_header;

select * from bsn_bhf;



select * from accnut_tax_header;

SELECT option_code AS "optionCode", option_name AS "optionName", ROUND(goods_price / SUBSTR(REGEXP_REPLACE(goods_standard, '[^0-9]', ''), 2)) AS "goodsPrice", goods_standard AS "goodsStandard", goods_name AS "goodsName"
		FROM purchse_option po JOIN purchse_goods pg
		            ON (po.goods_num = pg.goods_num)
		WHERE pg.company_num = 45
		AND pg.goods_name LIKE '%' || '' || '%'
		ORDER BY 2
        ;
SELECT REGEXP_REPLACE('A123B456C', '[^0-9]', '') AS only_numbers FROM DUAL;
SELECT SUBSTR('123ABC456', 2) AS result FROM DUAL;


create sequence accnut_tax_detail_seq;

select accnut_tax_detail_seq.nextval from dual;

select * from accnut_tax_header;
select * from accnut_tax_detail;

SELECT th.tax_num, th.from_rgno, th.from_co_name, th.from_name, th.from_address, th.from_status, th.from_email, 
th.to_rgno, th.to_co_name, th.to_name, th.to_address, th.to_status, th.to_cate, th.to_email1, th.to_email2, 
th.rgdate, th.note, th.total, th.supply, th.tax,
td.tax_detail_num, td.month, td.day, td.option_code, td.standard, td.quantity, td.amount, td.total, td.supply_price, td.tax, td.note
FROM accnut_tax_header th JOIN accnut_tax_detail td
ON (th.tax_num = td.tax_num)
WHERE th.tax_num = #{pk};


select * from accnut_tax_header;

select * from accnut_tax_detail;

select * from purchse_option
where company_num = 1
;

select * from bhf_closing;

SELECT TO_CHAR(closing_date, 'YYYY-MM-DD') AS "day", closing_code AS "code"
FROM bhf_closing
WHERE company_num = #{companyNum}
AND branch_office_id = #{bhfId}
AND TO_CHAR(closing_date,'YYYY-MM') = #{month}

;




SELECT fn_get_option_price(option_code) AS "amount", 
	        goods_name || '-' || option_name AS "optionCode", 
	        bnf_sle_qy AS "quantity",
	        option_code AS "optCode", 
	        TO_CHAR(closing_date, 'MM') AS "month",
	        TO_CHAR(closing_date, 'DD') AS "day",
	        bnf_sle_qy * fn_get_option_price(option_code) AS "total",
	        bnf_sle_qy * fn_get_option_price(option_code) * 0.9 AS "supplyPrice",
	        bnf_sle_qy * fn_get_option_price(option_code) * 0.1 AS "total",
	        fn_get_standard(option_code) AS "standard"
		FROM bhf_closing_detail bcd JOIN bhf_closing bc
		ON (bcd.closing_code = bc.closing_code);
        
select * from purchse_goods;
select * from purchse_option;
select pg.*, po.* from purchse_goods pg join purchse_option po on (pg.goods_num = po.goods_num);

create or replace function fn_get_standard(p_option_code varchar2)
return varchar2
is
    v_result varchar2(1000);
begin
    select pg.goods_standard
    into v_result
    from purchse_goods pg join purchse_option po
    on (pg.goods_num = po.goods_num)
    where option_code = p_option_code;
    
    return v_result;
end;
/

select * from accnut_tax_header;

SELECT tax_num, to_rgno, to_co_name, to_name, to_address, to_status, to_cate, rgdate, note, total, supply, tax 
FROM (SELECT /*+INDEX_DESC(accnut_tax_header PK_ACCNUT_TAX_HEADER)*/ rownum rn, tax_num, to_rgno, to_co_name, to_name, to_address, to_status, to_cate, rgdate, note, total, supply, tax 
      FROM accnut_tax_header
<![CDATA[
      WHERE rownum <= #{end}
      AND 
]]>
      company_num = #{companyNum}
      );
      select * from accnut_tax_detail;

select * from accnut_tax_detail;

select * from accnut_tax_header;

select * from purchse_goods;
SELECT th.*, TO_CHAR(rgdate, 'YYYY') AS "year", TO_CHAR(rgdate,'MM') AS "month", TO_CHAR(rgdate, 'DD') AS "day" , 12 - length(total) AS "blank", NVL(note, ' ') AS "notnullnote"

FROM accnut_tax_header th;

select supply, tax, 
FROM accnut_tax_header th;


select TO_CHAR(sysdate, 'Month') from dual;


select * from bsn_bhf;

select * from hr_department where company_num = 45 and parent_department_num is not null;

update accnut_salary_account_book
set bonus = 0;
commit;


select * from accnut_assets;

select * from accnut_salary_account_book;

update accnut_salary_account_book set payment_amount = salary - deduction_item where salary_account_book_code <= 24;
commit;

select '������, ��浿, ����ä, ���缮, ����ȣ, ������, Ȳ����' from dual;

select he.employee_id, he.employee_name, he.department_num, hd.department_name, hec.bonus, hec.additional_pay, hec.monthly_salary, hec.social_insurance from hr_employee he JOIN hr_department hd ON (he.department_num = hd.department_num) LEFT JOIN hr_employee_contract hec ON (he.employee_num = hec.employee_num) where he.company_num = 45;


select * from hr_department;
select * from hr_employee_contract;

select * from purchs_warehousing_header pwh join grpwr_vendor gv on (pwh.vendor_id = gv.vendor_id);
select * from grpwr_vendor;

select * from accnut_debt where company_num = 45;
select * from bhf_closing;
select * from accnut_dealings_account_book;
select * from cmmn where upper_cmmn_code = 'AC';
select * from accnut_dealings_account_book;
SELECT * FROM HR_DEPARTMENT;
select * from purchs_warehousing_header;
select * from accnut_assets where section = 'AC02';
select * from accnut_debt;
select * from grpwr_vendor;

alter table accnut_debt add ware_num number;


create or replace function fn_get_vendor_name(p_vendor_id varchar2)
return varchar2
is
    v_result varchar2(1000);
begin
    select vendor_name
    into v_result
    from grpwr_vendor
    where vendor_id = p_vendor_id;
    
    return v_result;
end;
/


select TO_DATE(TO_CHAR(ADD_MONTHS(sysdate, 1), 'YYYY-MM') || '-10', 'YYYY-MM-DD') from dual;

create or replace trigger tr_warehousing
    after 
    insert or update on purchs_warehousing_header
    for each row
declare
begin
    if inserting then
        insert into accnut_debt(debt_code, debt_name, section, register_date, creditor, amount, interest, time_limit, prearrangement_due_date, company_num, ware_num)
        values (accnut_debt_seq.nextval, TO_CHAR(:new.warehousing_date, 'YYYY-MM-DD') || ' ' || fn_get_vendor_name(:new.vendor_id) || ' �����ޱ�' , 'AC08', :new.warehousing_date , fn_get_vendor_name(:new.vendor_id), :new.warehousing_total_amount , 0, null , TO_DATE(TO_CHAR(ADD_MONTHS(:new.warehousing_date, 1), 'YYYY-MM') || '-10', 'YYYY-MM-DD'), :new.company_num, :new.warehousing_header_num);
    elsif updating then
        update accnut_debt
        set amount = :new.warehousing_total_amount
        where ware_num = :old.warehousing_header_num;
    end if;
end;
/

create or replace trigger tr_closing
  after
  insert on bhf_closing -- �������� ���Խ�
  for each row
declare
begin
    if inserting then
    
        -- ���� ����
        insert into accnut_dealings_account_book (dealings_account_book_code, section, types_of_transaction, amount, vat_alternative, dealings_contents, deal_date, department, company_num  )
        values (accnut_dealings_book_seq.nextval, 'EE01', 'AC02', :new.sale_amount, 'Y', TO_CHAR(:new.closing_date, 'YYYY-MM-DD') || ' ' || :new.branch_office_id || ' ��������', :new.closing_date, 'DT004', :new.company_num );
        
    end if;
    
end;
/
