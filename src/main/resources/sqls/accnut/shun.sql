-- 2025-02-11

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

update accnut_assets set company_num = 0;

commit;

-- 2025-02-13
select fn_get_cmmn_code('��')
from dual;

select fn_get_cmmn_name('AC08')
from dual;

select * from cmmn where upper_cmmn_code = 'AC';

insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC07','AC','�����');
insert into cmmn(cmmn_code, upper_cmmn_code, cmmn_name) values ('AC08','AC','�����ޱ�');
COMMIT;

select * from accnut_debt;

create sequence accnut_debt_seq;

insert into accnut_debt(debt_code, debt_name, section, register_date, creditor, amount, interest, time_limit, prearrangement_due_date, company_num)
values (ACCNUT_DEBT_SEQ.nextval,'�����ޱ�4','AC08',sysdate, 'Aȸ��', 100000, 0, sysdate+90, sysdate+50, 1);

insert into accnut_debt
select accnut_debt_seq.nextval, debt_name, section, register_date, creditor, amount, interest, time_limit, prearrangement_due_date, company_num
from accnut_debt;
commit;

update accnut_debt
set company_num = 0;