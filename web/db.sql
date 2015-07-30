--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.3
-- Dumped by pg_dump version 9.2.3
-- Started on 2013-05-29 11:08:46

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 17314)
-- Name: ctd_acl; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE ctd_acl (
    pmserial integer NOT NULL,
    acl smallint NOT NULL
);


--
-- TOC entry 2000 (class 0 OID 0)
-- Dependencies: 170
-- Name: TABLE ctd_acl; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE ctd_acl IS '权限信息表，表示某一个成员在某个项目中的权限信息。';


--
-- TOC entry 2001 (class 0 OID 0)
-- Dependencies: 170
-- Name: COLUMN ctd_acl.pmserial; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_acl.pmserial IS '自增字段，该表的唯一标识, 也用于在其他场所表示某一成员与项目之间的依存关系。';


--
-- TOC entry 2002 (class 0 OID 0)
-- Dependencies: 170
-- Name: COLUMN ctd_acl.acl; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_acl.acl IS '成员在某一项目中的权限信息';


--
-- TOC entry 172 (class 1259 OID 17321)
-- Name: ctd_actualrecord; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE ctd_actualrecord (
    arid integer NOT NULL,
    pmserial integer NOT NULL,
    workdate date NOT NULL,
    begintime time without time zone NOT NULL,
    endtime time without time zone,
    taxitimebegin time without time zone,
    taxitimeend time without time zone,
    taxistartlocation character varying,
    taxiendlocation character varying,
    taxiticket numeric(6,2)
);


--
-- TOC entry 2003 (class 0 OID 0)
-- Dependencies: 172
-- Name: TABLE ctd_actualrecord; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE ctd_actualrecord IS '实际加班记录';


--
-- TOC entry 2004 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.arid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.arid IS '实际加班记录的唯一标识';


--
-- TOC entry 2005 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.pmserial; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.pmserial IS '自增字段，该表的唯一标识, 也用于在其他场所表示某一成员与项目之间的依存关系。';


--
-- TOC entry 2006 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.workdate; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.workdate IS '计划加班日期';


--
-- TOC entry 2007 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.begintime; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.begintime IS '实际加班开始时间';


--
-- TOC entry 2008 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.endtime; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.endtime IS '实际加班结束时间';


--
-- TOC entry 2009 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.taxitimebegin; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.taxitimebegin IS '打车时间';


--
-- TOC entry 2010 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.taxistartlocation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.taxistartlocation IS '上车地点';


--
-- TOC entry 2011 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.taxiendlocation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.taxiendlocation IS '打车终点';


--
-- TOC entry 2012 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN ctd_actualrecord.taxiticket; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_actualrecord.taxiticket IS '车票金额，精确到小数点后两位';


--
-- TOC entry 171 (class 1259 OID 17319)
-- Name: ctd_actualrecord_arid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE ctd_actualrecord_arid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    CYCLE;


--
-- TOC entry 2013 (class 0 OID 0)
-- Dependencies: 171
-- Name: ctd_actualrecord_arid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE ctd_actualrecord_arid_seq OWNED BY ctd_actualrecord.arid;


--
-- TOC entry 173 (class 1259 OID 17334)
-- Name: ctd_maillist; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE ctd_maillist (
    pid integer NOT NULL,
    mailto character varying NOT NULL,
    mailcc character varying NOT NULL
);


--
-- TOC entry 2014 (class 0 OID 0)
-- Dependencies: 173
-- Name: TABLE ctd_maillist; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE ctd_maillist IS '通过邮件发送加班申请是的邮件列表信息。';


--
-- TOC entry 2015 (class 0 OID 0)
-- Dependencies: 173
-- Name: COLUMN ctd_maillist.pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_maillist.pid IS '项目信息表的唯一标识，目的是允许更容易的变更项目编号(有可能输入错误的情况)。';


--
-- TOC entry 2016 (class 0 OID 0)
-- Dependencies: 173
-- Name: COLUMN ctd_maillist.mailto; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_maillist.mailto IS '上司(想该邮件发送加班申请)';


--
-- TOC entry 2017 (class 0 OID 0)
-- Dependencies: 173
-- Name: COLUMN ctd_maillist.mailcc; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_maillist.mailcc IS '加班申请邮件抄送地址列表，以分号(;)分隔';


--
-- TOC entry 175 (class 1259 OID 17344)
-- Name: ctd_planrecord; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE ctd_planrecord (
    prid integer NOT NULL,
    pmserial integer NOT NULL,
    workdate date NOT NULL,
    begintime time without time zone NOT NULL,
    endtime time without time zone NOT NULL,
    holiday boolean NOT NULL,
    taxi boolean NOT NULL,
    applied boolean NOT NULL
);


--
-- TOC entry 2018 (class 0 OID 0)
-- Dependencies: 175
-- Name: TABLE ctd_planrecord; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE ctd_planrecord IS '计划加班信息，可开发人员自行填写，也可以由项目管理者统一申请';


--
-- TOC entry 2019 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN ctd_planrecord.prid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_planrecord.prid IS '计划加班记录的唯一标识';


--
-- TOC entry 2020 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN ctd_planrecord.pmserial; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_planrecord.pmserial IS '自增字段，该表的唯一标识, 也用于在其他场所表示某一成员与项目之间的依存关系。';


--
-- TOC entry 2021 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN ctd_planrecord.workdate; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_planrecord.workdate IS '计划加班日期';


--
-- TOC entry 2022 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN ctd_planrecord.begintime; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_planrecord.begintime IS '计划加班开始时间';


--
-- TOC entry 2023 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN ctd_planrecord.endtime; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_planrecord.endtime IS '计划加班结束时间';


--
-- TOC entry 2024 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN ctd_planrecord.holiday; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_planrecord.holiday IS '是否申请调休';


--
-- TOC entry 2025 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN ctd_planrecord.taxi; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_planrecord.taxi IS '是否申请打车报销';


--
-- TOC entry 2026 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN ctd_planrecord.applied; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_planrecord.applied IS '标记该加班计划是否已经申请';


--
-- TOC entry 174 (class 1259 OID 17342)
-- Name: ctd_planrecord_prid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE ctd_planrecord_prid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    CYCLE;


--
-- TOC entry 2027 (class 0 OID 0)
-- Dependencies: 174
-- Name: ctd_planrecord_prid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE ctd_planrecord_prid_seq OWNED BY ctd_planrecord.prid;


--
-- TOC entry 177 (class 1259 OID 17356)
-- Name: ctd_project; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE ctd_project (
    pid integer NOT NULL,
    projectid character varying NOT NULL,
    projectname character varying NOT NULL,
    manager character varying NOT NULL,
    opendate date NOT NULL,
    closedate date
);


--
-- TOC entry 2028 (class 0 OID 0)
-- Dependencies: 177
-- Name: TABLE ctd_project; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE ctd_project IS '项目基本信息';


--
-- TOC entry 2029 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN ctd_project.pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_project.pid IS '项目信息表的唯一标识，目的是允许更容易的变更项目编号(有可能输入错误的情况)。';


--
-- TOC entry 2030 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN ctd_project.projectid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_project.projectid IS '项目编号(可修改)';


--
-- TOC entry 2031 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN ctd_project.projectname; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_project.projectname IS '项目名称';


--
-- TOC entry 2032 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN ctd_project.manager; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_project.manager IS '项目负责人的ID(使用公司LDAP的情况下，是邮箱名)';


--
-- TOC entry 2033 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN ctd_project.opendate; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_project.opendate IS '项目开始日期(当然只是项目开始记录加班的日期)';


--
-- TOC entry 2034 (class 0 OID 0)
-- Dependencies: 177
-- Name: COLUMN ctd_project.closedate; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_project.closedate IS '项目关闭日期，关闭后的项目不能再填写加班信息。';


--
-- TOC entry 176 (class 1259 OID 17354)
-- Name: ctd_project_pid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE ctd_project_pid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    CYCLE;


--
-- TOC entry 2035 (class 0 OID 0)
-- Dependencies: 176
-- Name: ctd_project_pid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE ctd_project_pid_seq OWNED BY ctd_project.pid;


--
-- TOC entry 179 (class 1259 OID 17369)
-- Name: ctd_projectmember; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE ctd_projectmember (
    pmserial integer NOT NULL,
    pid integer NOT NULL,
    usrid character varying NOT NULL,
    adddate date DEFAULT now() NOT NULL
);


--
-- TOC entry 2036 (class 0 OID 0)
-- Dependencies: 179
-- Name: TABLE ctd_projectmember; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE ctd_projectmember IS '项目与成员之间的关系表';


--
-- TOC entry 2037 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN ctd_projectmember.pmserial; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_projectmember.pmserial IS '自增字段，该表的唯一标识, 也用于在其他场所表示某一成员与项目之间的依存关系。';


--
-- TOC entry 2038 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN ctd_projectmember.pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_projectmember.pid IS '项目信息表的唯一标识，目的是允许更容易的变更项目编号(有可能输入错误的情况)。';


--
-- TOC entry 2039 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN ctd_projectmember.usrid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_projectmember.usrid IS '用户的唯一标识，通常是邮箱名。';


--
-- TOC entry 2040 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN ctd_projectmember.adddate; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN ctd_projectmember.adddate IS '成员加入项目时的日期';


--
-- TOC entry 178 (class 1259 OID 17367)
-- Name: ctd_projectmember_pmserial_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE ctd_projectmember_pmserial_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
    CYCLE;


--
-- TOC entry 2041 (class 0 OID 0)
-- Dependencies: 178
-- Name: ctd_projectmember_pmserial_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE ctd_projectmember_pmserial_seq OWNED BY ctd_projectmember.pmserial;


--
-- TOC entry 180 (class 1259 OID 17419)
-- Name: ctd_usrprops; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE ctd_usrprops (
    usrid character varying NOT NULL,
    propname character varying NOT NULL,
    propvalue text
);


--
-- TOC entry 1959 (class 2604 OID 17324)
-- Name: arid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_actualrecord ALTER COLUMN arid SET DEFAULT nextval('ctd_actualrecord_arid_seq'::regclass);


--
-- TOC entry 1960 (class 2604 OID 17347)
-- Name: prid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_planrecord ALTER COLUMN prid SET DEFAULT nextval('ctd_planrecord_prid_seq'::regclass);


--
-- TOC entry 1961 (class 2604 OID 17359)
-- Name: pid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_project ALTER COLUMN pid SET DEFAULT nextval('ctd_project_pid_seq'::regclass);


--
-- TOC entry 1962 (class 2604 OID 17372)
-- Name: pmserial; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_projectmember ALTER COLUMN pmserial SET DEFAULT nextval('ctd_projectmember_pmserial_seq'::regclass);


--
-- TOC entry 1965 (class 2606 OID 17318)
-- Name: ctd_acl_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_acl
    ADD CONSTRAINT ctd_acl_pkey PRIMARY KEY (pmserial);


--
-- TOC entry 1967 (class 2606 OID 17329)
-- Name: ctd_actualrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_actualrecord
    ADD CONSTRAINT ctd_actualrecord_pkey PRIMARY KEY (arid);


--
-- TOC entry 1969 (class 2606 OID 17333)
-- Name: ctd_actualrecord_pmserial_workdate_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_actualrecord
    ADD CONSTRAINT ctd_actualrecord_pmserial_workdate_key UNIQUE (pmserial, workdate);


--
-- TOC entry 1972 (class 2606 OID 17341)
-- Name: ctd_maillist_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_maillist
    ADD CONSTRAINT ctd_maillist_pkey PRIMARY KEY (pid);


--
-- TOC entry 1974 (class 2606 OID 17349)
-- Name: ctd_planrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_planrecord
    ADD CONSTRAINT ctd_planrecord_pkey PRIMARY KEY (prid);


--
-- TOC entry 1976 (class 2606 OID 17353)
-- Name: ctd_planrecord_pmserial_workdate_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_planrecord
    ADD CONSTRAINT ctd_planrecord_pmserial_workdate_key UNIQUE (pmserial, workdate);


--
-- TOC entry 1978 (class 2606 OID 17364)
-- Name: ctd_project_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_project
    ADD CONSTRAINT ctd_project_pkey PRIMARY KEY (pid);


--
-- TOC entry 1980 (class 2606 OID 17366)
-- Name: ctd_project_projectid_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_project
    ADD CONSTRAINT ctd_project_projectid_key UNIQUE (projectid);


--
-- TOC entry 1983 (class 2606 OID 17380)
-- Name: ctd_projectmember_pid_usrid_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_projectmember
    ADD CONSTRAINT ctd_projectmember_pid_usrid_key UNIQUE (pid, usrid);


--
-- TOC entry 1985 (class 2606 OID 17378)
-- Name: ctd_projectmember_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_projectmember
    ADD CONSTRAINT ctd_projectmember_pkey PRIMARY KEY (pmserial);


--
-- TOC entry 1988 (class 2606 OID 17426)
-- Name: ctd_usrprops_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_usrprops
    ADD CONSTRAINT ctd_usrprops_pkey PRIMARY KEY (usrid);


--
-- TOC entry 1981 (class 1259 OID 17407)
-- Name: ctd_projectmember_pid_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX ctd_projectmember_pid_idx ON ctd_projectmember USING btree (pid);


--
-- TOC entry 1986 (class 1259 OID 17408)
-- Name: ctd_projectmember_usrid_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX ctd_projectmember_usrid_idx ON ctd_projectmember USING btree (usrid);


--
-- TOC entry 1970 (class 1259 OID 17406)
-- Name: index_serial; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX index_serial ON ctd_actualrecord USING btree (workdate);


--
-- TOC entry 1989 (class 1259 OID 17427)
-- Name: propname_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX propname_idx ON ctd_usrprops USING btree (propname);


--
-- TOC entry 1990 (class 2606 OID 17391)
-- Name: ctd_acl_pmserial_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_acl
    ADD CONSTRAINT ctd_acl_pmserial_fkey FOREIGN KEY (pmserial) REFERENCES ctd_projectmember(pmserial) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 1991 (class 2606 OID 17396)
-- Name: ctd_actualrecord_pmserial_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_actualrecord
    ADD CONSTRAINT ctd_actualrecord_pmserial_fkey FOREIGN KEY (pmserial) REFERENCES ctd_projectmember(pmserial) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 1992 (class 2606 OID 17381)
-- Name: ctd_maillist_pid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_maillist
    ADD CONSTRAINT ctd_maillist_pid_fkey FOREIGN KEY (pid) REFERENCES ctd_project(pid) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 1993 (class 2606 OID 17401)
-- Name: ctd_planrecord_pmserial_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_planrecord
    ADD CONSTRAINT ctd_planrecord_pmserial_fkey FOREIGN KEY (pmserial) REFERENCES ctd_projectmember(pmserial) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 1994 (class 2606 OID 17386)
-- Name: ctd_projectmember_pid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY ctd_projectmember
    ADD CONSTRAINT ctd_projectmember_pid_fkey FOREIGN KEY (pid) REFERENCES ctd_project(pid) ON UPDATE RESTRICT ON DELETE RESTRICT;


-- Completed on 2013-05-29 11:08:46

--
-- PostgreSQL database dump complete
--

