-- 공통코드
CREATE TABLE cmmn (
	cmmn_code	varchar2(20)		NOT NULL,
	upper_cmmn_code	varchar2(20)		NULL,
	cmmn_name	varchar2(100)		NOT NULL,
	description	varchar2(100)		NULL
);

ALTER TABLE cmmn ADD CONSTRAINT PK_CMMN PRIMARY KEY (
	cmmn_code
);
ALTER TABLE cmmn ADD CONSTRAINT FK_cmmn_TO_cmmn_1 FOREIGN KEY (
	upper_cmmn_code
)
REFERENCES cmmn (
	cmmn_code
);
-- 공통코드 이름 구하는 함수
CREATE or REPLACE FUNCTION fn_get_cmmn_name(p_cmmn_code varchar2)
    RETURN varchar2
IS
    v_cmmn_name varchar2(100);
BEGIN
    SELECT cmmn_name
    INTO v_cmmn_name
    FROM cmmn
    WHERE cmmn_code = p_cmmn_code;
    
    RETURN v_cmmn_name;
END;
/

-- 공통코드 구하는 함수
CREATE or REPLACE FUNCTION fn_get_cmmn_code(p_cmmn_name varchar2)
    RETURN varchar2
IS
    v_cmmn_code varchar2(20);
BEGIN
    SELECT cmmn_code
    INTO v_cmmn_code
    FROM cmmn
    WHERE cmmn_name = p_cmmn_name;

    RETURN v_cmmn_code;
END;
/

-- 공통코드 이름으로 코드 구할 때  중복값 때문에 오류 뜰 때 사용할 함수
CREATE OR REPLACE FUNCTION fn_get_cmmn_code_ver2(p_cmmn_name varchar2, p_section varchar2)
RETURN varchar2
IS
    v_cmmn_code varchar2(20);
    v_upper_cmmn_code varchar2(20);

    CURSOR cursor_cmmn IS
    SELECT cmmn_code, upper_cmmn_code
    FROM cmmn
    WHERE cmmn_name = p_cmmn_name;
    
    v_upper_name varchar(100);
    v_return varchar2(20) default '00';
BEGIN
    OPEN cursor_cmmn;
    LOOP
        FETCH cursor_cmmn INTO v_cmmn_code, v_upper_cmmn_code;
        EXIT WHEN cursor_cmmn%NOTFOUND;
        
        SELECT fn_get_cmmn_name(v_upper_cmmn_code)
        INTO v_upper_name
        FROM dual;
        
        IF v_upper_name LIKE '%' || p_section || '%' THEN
            v_return := v_cmmn_code;
        END IF;
    END LOOP;    
    CLOSE cursor_cmmn;
    
    RETURN v_return;
END;
/

-- 회계 테이블

CREATE TABLE accnut_assets (
	assets_code	varchar2(50)		NOT NULL,
	assets_name	varchar2(100)		NOT NULL,
	section	varchar2(20)		NOT NULL,
	financial_institution	varchar2(20)		NULL,
	finance_information	varchar2(30)		NULL,
	owner	varchar2(100)		NULL,
	amount	number(12,0)	DEFAULT 0	NULL,
	register_date	date	DEFAULT sysdate	NOT NULL,
	quantity	number(10)		NULL,
	fixtures_amount	number(12,0)	DEFAULT 0	NULL
);

COMMENT ON COLUMN accnut_assets.section IS 'ex) 현금(), 통장(), 카드(), 비품()';

COMMENT ON COLUMN accnut_assets.financial_institution IS '은행 및 카드사를 의미 ex)농협(), 국민(), 기업()';

COMMENT ON COLUMN accnut_assets.finance_information IS '통장번호 및 카드번호를 의미';

CREATE TABLE accnut_debt (
	debt_code	varchar2(50)		NOT NULL,
	debt_name	varchar2(100)		NOT NULL,
	section	varchar2(20)		NOT NULL,
	register_date	date	DEFAULT sysdate	NOT NULL,
	creditor	varchar2(100)		NULL,
	amount	number(12,0)	DEFAULT 0	NULL,
	interest	number(10,2)		NULL,
	time_limit	date	DEFAULT sysdate	NULL,
	prearrangement_due_date	date	DEFAULT sysdate	NULL
);

COMMENT ON COLUMN accnut_debt.section IS 'ex) 대출금(),미지급금()';

CREATE TABLE accnut_dealings_account_book (
	dealings_account_book_code	varchar2(50)		NOT NULL,
	section	varchar2(20)		NOT NULL,
	types_of_transaction	varchar2(20)		NOT NULL,
	amount	number(12,0)	DEFAULT 0	NOT NULL,
	vat_alternative	char(1)	DEFAULT 'N'	NULL,
	dealings_contents	varchar2(100)		NULL,
	deal_date	date	DEFAULT sysdate	NOT NULL,
	department	varchar2(30)		NULL
);

COMMENT ON COLUMN accnut_dealings_account_book.section IS 'ex) 수익(), 지출()';

COMMENT ON COLUMN accnut_dealings_account_book.types_of_transaction IS 'ex) 현금(), 통장(), 카드()';

CREATE TABLE accnut_salary_account_book (
	salary_account_book_code	varchar2(50)		NOT NULL,
	employee_code	varchar2(50)		NOT NULL,
	employee_name	varchar2(100)		NOT NULL,
	department	varchar2(30)		NULL,
	salary	number(12,0)	DEFAULT 0	NOT NULL,
	excess_allowance	number(12,0)	DEFAULT 0	NULL,
	bonus	number(10,2)		NULL,
	incidental_cost	number(12,0)	DEFAULT 0	NULL,
	deduction_item	number(12,0)	DEFAULT 0	NULL,
	payment_amount	number(12,0)	DEFAULT 0	NOT NULL,
	payment_prearranged_date	date	DEFAULT sysdate	NULL,
	payment_alternative	varchar2(20)	DEFAULT 'PY01'	NOT NULL,
	payer	number(10)		NULL
);

COMMENT ON COLUMN accnut_salary_account_book.payment_alternative IS 'ex)';

CREATE TABLE accnut_incidental_cost (
	incidental_cost_code	varchar2(50)		NOT NULL,
	department	varchar2(30)		NULL,
	section	varchar2(20)		NOT NULL,
	card_num	varchar2(30)		NOT NULL,
	amount	number(12,0)	DEFAULT 0	NOT NULL,
	contents	varchar2(100)		NULL,
	register_date	date	DEFAULT sysdate	NOT NULL,
	pay_date	date	DEFAULT sysdate	NULL,
	image	varchar2(40)		NULL,
	process_alternative	varchar2(20)	DEFAULT 'PC01'	NOT NULL,
	employee_num	number(10)		NULL,
	employee_name	varchar2(100)		NULL,
	account_category	varchar2(20)		NULL,
	confirmer	number(10)		NULL
);

COMMENT ON COLUMN accnut_incidental_cost.section IS 'ex) 개인(), 법인()';

COMMENT ON COLUMN accnut_incidental_cost.process_alternative IS 'ex)';

CREATE TABLE accnut_etc_payment (
	etc_payment_code	varchar2(50)		NOT NULL,
	section	varchar2(20)		NOT NULL,
	department	varchar2(30)		NULL,
	time_limit	date	DEFAULT sysdate	NOT NULL,
	amount	number(12,0)	DEFAULT 0	NOT NULL,
	payment_alternative	varchar2(20)	DEFAULT 'PY01'	NOT NULL,
	giro_num	varchar2(40)		NULL
);

CREATE TABLE accnut_deduction_details (
	deduction_details_code	varchar2(50)		NOT NULL,
	salary_account_book_code	varchar2(50)		NOT NULL,
	deduction_section	varchar2(20)		NOT NULL,
	deduction_amount	number(12,0)	DEFAULT 0	NOT NULL,
	process_alternative	varchar2(20)	DEFAULT 'PC01'	NOT NULL
);

COMMENT ON COLUMN accnut_deduction_details.deduction_section IS 'ex) 보험,() 연금(),소득세()';

ALTER TABLE accnut_assets ADD CONSTRAINT PK_ACCNUT_ASSETS PRIMARY KEY (
	assets_code
);

ALTER TABLE accnut_debt ADD CONSTRAINT PK_ACCNUT_DEBT PRIMARY KEY (
	debt_code
);

ALTER TABLE accnut_dealings_account_book ADD CONSTRAINT PK_ACCNUT_DEAL_ACCOUNT_BOOK PRIMARY KEY (
	dealings_account_book_code
);

ALTER TABLE accnut_salary_account_book ADD CONSTRAINT PK_ACCNUT_SALARY_ACCOUNT_BOOK PRIMARY KEY (
	salary_account_book_code
);

ALTER TABLE accnut_incidental_cost ADD CONSTRAINT PK_ACCNUT_INCIDENTAL_COST PRIMARY KEY (
	incidental_cost_code
);

ALTER TABLE accnut_etc_payment ADD CONSTRAINT PK_ACCNUT_ETC_PAYMENT PRIMARY KEY (
	etc_payment_code
);

ALTER TABLE accnut_deduction_details ADD CONSTRAINT PK_ACCNUT_DEDUCTION_DETAILS PRIMARY KEY (
	deduction_details_code
);

ALTER TABLE accnut_deduction_details ADD CONSTRAINT FK_salary_deduction_details_1 FOREIGN KEY (
	salary_account_book_code
)
REFERENCES accnut_salary_account_book (
	salary_account_book_code
);

-- 회계 테이블 회사코드 추가명령문 조회
select 'ALTER TABLE '|| table_name ||' ADD company_num NUMBER(6,0);'
from user_tables
where table_name like 'ACCNUT_'||'%';

-- 실제 추가문 쿼리
ALTER TABLE ACCNUT_SALARY_ACCOUNT_BOOK ADD company_num NUMBER(6,0);
ALTER TABLE ACCNUT_INCIDENTAL_COST ADD company_num NUMBER(6,0);
ALTER TABLE ACCNUT_ETC_PAYMENT ADD company_num NUMBER(6,0);
ALTER TABLE ACCNUT_DEDUCTION_DETAILS ADD company_num NUMBER(6,0);
ALTER TABLE ACCNUT_DEBT ADD company_num NUMBER(6,0);
ALTER TABLE ACCNUT_DEALINGS_ACCOUNT_BOOK ADD company_num NUMBER(6,0);
ALTER TABLE ACCNUT_ASSETS ADD company_num NUMBER(6,0);

-- 추가 컬럼
alter table accnut_assets add rgno varchar2(1000);

DROP TABLE ACCNUT_TAX_DETAIL;
-- 추가 테이블
CREATE TABLE accnut_tax_detail (
	tax_detail_num	NUMBER(10) 	NOT NULL,
	tax_num	number(10)		NOT NULL,
	month	number(10)		NOT NULL,
	day	number(10)		NOT NULL,
	option_code	varchar2(100)		NOT NULL,
	standard	varchar2(100)		NULL,
	quantity	number(10)		NOT NULL,
	amount	number(10)		NULL,
	total	number(10)		NULL,
	supply_price	number(10)		NULL,
	tax	number(10)		NULL,
	note	varchar(500)		NULL
);

CREATE TABLE accnut_tax_header (
	tax_num	number(10)		NOT NULL,
	from_rgno	varchar2(50)		NOT NULL,
	from_co_name	varchar2(100)		NOT NULL,
	from_name	varchar2(100)		NOT NULL,
	from_address	varchar(500)		NULL,
	from_status	varchar2(100)		NULL,
	from_cate	varchar2(100)		NULL,
	from_email	varchar2(100)		NULL,
	to_rgno	varchar2(50)		NOT NULL,
	to_co_name	varchar2(100)		NOT NULL,
	to_name	varchar2(100)		NOT NULL,
	to_address	varchar(500)		NULL,
	to_status	varchar2(100)		NULL,
	to_cate	varchar2(100)		NULL,
	to_email1	varchar2(100)		NULL,
	to_email2	varchar2(100)		NULL,
	rgdate	date	DEFAULT sysdate	NOT NULL,
	note	varchar(500)		NULL,
	total	number(10)		NULL,
	supply	number(10)		NULL,
	tax	number(10)		NULL,
	company_num	number(10)		NOT NULL
);

ALTER TABLE accnut_tax_detail ADD CONSTRAINT PK_ACCNUT_TAX_DETAIL PRIMARY KEY (
	tax_detail_num
);

ALTER TABLE accnut_tax_header ADD CONSTRAINT PK_ACCNUT_TAX_HEADER PRIMARY KEY (
	tax_num
);

ALTER TABLE accnut_tax_detail ADD CONSTRAINT FK_accnut_tax_TO_tax_detail_1 FOREIGN KEY (
	tax_num
)
REFERENCES accnut_tax_header (
	tax_num
);

