package com.anthem.sclc.entcomp.eventupdater.svc.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.anthem.sclc.entcomp.common.bean.payload.Payload;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.AgentPaySchduleData;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.AgentPolicyData;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.CmsDetail;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.MemberDemographicData;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.SubscriberData;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.AgentPaySchduleRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.AgentPolicyRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.CmsDetailRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.MemberDemographicDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PlcySchduleRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PolicyDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PolicyIndDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PolicySubscriberDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PrefundRowMapper;
import com.anthem.sclc.entcomp.svc.core.dao.JdbcTemplateFactory;
import com.anthem.sclc.entcomp.svc.core.error.ErrorStatus;
import com.anthem.sclc.entcomp.svc.core.util.BeanFactory;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:event_updater_queries.xml",
		 						  "classpath:agent_bonus_queries.xml"})
@Component
public class EventUpdaterDaoTest {
	
	@Spy
	@InjectMocks
	EventUpdaterDAO eventUpdaterDao;
	
	@Mock
	private List<Object> mockList;
	
	@Mock
	private List<Map<String, Object>> mockMap;
	
	@Mock
	DataSource dataSource;
	
	@Mock
	JdbcTemplateFactory templateFactory;
	
	@Mock
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	EmptyResultDataAccessException e;
	

	@Before
	public void setup() {
		BeanFactory.setContext(applicationContext);
	}
	@Test
	public void testUpdate() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn(1).when(eventUpdaterDao).update(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals(1,eventUpdaterDao.update("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testQueryForInt() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn(1).when(eventUpdaterDao).queryForInt(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals(new Integer(1),eventUpdaterDao.queryForInt("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testQueryForString() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn("Success").when(eventUpdaterDao).queryForString(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals("Success",eventUpdaterDao.queryForString("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testQueryForDate() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		long millis=System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		doReturn(date).when(eventUpdaterDao).queryForDate(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals(date,eventUpdaterDao.queryForDate("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testQueryForMap() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockMap = new ArrayList<Map<String, Object>>();
		doReturn(mockMap).when(eventUpdaterDao).queryForList(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals(mockMap,eventUpdaterDao.queryForMap("renewal_update_policy", new Object[1]));
	}
	@Test
	public void updateTest() {
		Object[] values = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(1).when(eventUpdaterDao).update(Mockito.any(DataSource.class),Mockito.any(String.class), Mockito.any(Object[].class));
		int result = eventUpdaterDao.update("updateDummyRecordStatus", values);
		assertEquals(1,result);
		
	}
	@Test
	public void testGetDateList() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockList = new ArrayList<Object>();
		doReturn(mockList).when(eventUpdaterDao).getDateList(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals(mockList,eventUpdaterDao.getDateList("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testGetStringList() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockList = new ArrayList<Object>();
		doReturn(mockList).when(eventUpdaterDao).getStringList(Mockito.any(DataSource.class), Mockito.anyString(), Mockito.any(Class.class),
				Mockito.any(Object[].class));
		Assert.assertEquals(mockList,eventUpdaterDao.getStringList("renewal_update_policy", new Object[1]));
	}
	
	@Test
	public void testqueryForSystemDate() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn("Success").when(eventUpdaterDao).queryForString(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals("Success",eventUpdaterDao.queryForSystemDate("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testgetReportDate() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn("Success").when(eventUpdaterDao).queryForString(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals("Success",eventUpdaterDao.getReportDate("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testupdateCancelIndPremium() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn(1).when(eventUpdaterDao).update(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals(new Integer(1),eventUpdaterDao.updateCancelIndPremium( new Object[1],"renewal_update_policy"));
	}
	@Test
	public void testQueryForCnePolicyId() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn(1).when(eventUpdaterDao).queryForInt(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals(new Integer(1),eventUpdaterDao.queryForCnePolicyId("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testgetAgentTin() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn("Success").when(eventUpdaterDao).queryForString(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals("Success",eventUpdaterDao.getAgentTin("renewal_update_policy"));
	}
	@Test
	public void testgetAgentId() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn(1).when(eventUpdaterDao).queryForInt(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals(new Integer(1),eventUpdaterDao.getAgentId("renewal_update_policy"));
	}
	@Test
	public void testgetAgentTierFactor() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn("Success").when(eventUpdaterDao).queryForString(Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals("Success",eventUpdaterDao.getAgentTierFactor("renewal_update_policy"));
	}
	@Test
	public void testgetPolicySchduleData() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockList = new ArrayList<Object>();
		doReturn(mockList).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.anyString(),
				Mockito.any(Object[].class), Mockito.any(PlcySchduleRowMapper.class));
		Assert.assertNotNull(eventUpdaterDao.getPolicySchduleData("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testgetPolicySchduleLatestData() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockList = new ArrayList<Object>();
		doReturn(mockList).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.anyString(),
				Mockito.any(Object[].class), Mockito.any(PlcySchduleRowMapper.class));
		Assert.assertNotNull(eventUpdaterDao.getPolicySchduleLatestData("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testgetMemLatestData() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockList = new ArrayList<Object>();
		doReturn(mockList).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.anyString(),
				Mockito.any(Object[].class), Mockito.any(MemberDemographicDataRowMapper.class));
		Assert.assertNotNull(eventUpdaterDao.getMemLatestData("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testgetAgentOverride() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockList = new ArrayList<Object>();
		doReturn(mockList).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.anyString(),
				Mockito.any(Object[].class), Mockito.any(AgentPolicyRowMapper.class));
		Assert.assertNull(eventUpdaterDao.getAgentOverride("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testgetAgentDetails() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockList = new ArrayList<Object>();
		doReturn(mockList).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.anyString(),
				Mockito.any(Object[].class), Mockito.any(AgentPolicyRowMapper.class));
		Assert.assertNotNull(eventUpdaterDao.getAgentDetails("renewal_update_policy", new Object[1]));
	}
	@Test
	public void testgetSubDtlsByPlcyId() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		mockList = new ArrayList<Object>();
		doReturn(mockList).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.anyString(),
				Mockito.any(Object[].class), Mockito.any(PolicySubscriberDataRowMapper.class));
		Assert.assertNull(eventUpdaterDao.getSubDtlsByPlcyId("renewal_update_policy", new Object[1]));
	}
	
	@Test
	public void queryForIntTest() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Integer res=1;
		Object[] values = new Object[1];
		Mockito.doReturn(1).when(eventUpdaterDao).queryForInt(Mockito.any(DataSource.class),Mockito.any(String.class), Mockito.any(Object[].class));
		Integer result = eventUpdaterDao.queryForInt("updateDummyRecordStatus", values);
		assertEquals(res,result);
	}
	
	@Test
	public void getAgentIdTest() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Integer res=1;
		Mockito.doReturn(null).when(eventUpdaterDao).queryForInt(Mockito.any(String.class), Mockito.any(Object[].class));
		Mockito.doReturn(1).when(eventUpdaterDao).insert(Mockito.any(String.class),Mockito.anyMap(), Mockito.any(String.class));
		Integer result=eventUpdaterDao.getAgentId(new Payload(),new HashMap<Integer,Object>(), "a", "b");
		assertEquals(res,result);
	}
	
	@Test
	public void queryForStringTest() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Object[] values = new Object[1];
		Mockito.doReturn("result").when(eventUpdaterDao).queryForString(Mockito.any(DataSource.class),Mockito.any(String.class), Mockito.any(Object[].class));
		String result = eventUpdaterDao.queryForString("updateDummyRecordStatus", values);
		assertEquals("result",result);
	}
	
	@Test
	public void queryForDateTest() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Object[] values = new Object[1];
		String str="2015-03-31";  
		java.sql.Date date=java.sql.Date.valueOf(str);
		Mockito.doReturn(date).when(eventUpdaterDao).queryForDate(Mockito.any(DataSource.class),Mockito.any(String.class), Mockito.any(Object[].class));
		java.sql.Date result = eventUpdaterDao.queryForDate("updateDummyRecordStatus", values);
		assertEquals(date,result);
	}
	
	@Test
	public void insertTest() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(1).when(eventUpdaterDao).insert(Mockito.any(DataSource.class),
				Mockito.any(String.class),Mockito.anyMap(), Mockito.any(String.class));
		int result = eventUpdaterDao.insert( "updateDummyRecordStatus",new HashMap<Integer,Object>(), "aa");
		assertEquals(1,result);
	}
	
	@Test
	public void insertDynamicQuery() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doNothing().when(eventUpdaterDao).insertDynamicQuery(Mockito.any(DataSource.class),
				Mockito.any(String.class), Mockito.anyMap());
		eventUpdaterDao.insertDynamicQuery("updateDummyRecordStatus", new HashMap<Integer,Object>());
		assertEquals(0,eventUpdaterDao.update(dataSource,"test query" ,"test query"));
	}
	
	@Test
	public void queryForMapTest() {
		 Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		 Map<String,Object> map = new HashMap<>();
		 map.put("11", new Object());
		 List<Map<String,Object>> list = new ArrayList<>();
		 list.add(map);
		 Object obj[] = new Object[1];
		 Mockito.doReturn(list).when(eventUpdaterDao).queryForList(Mockito.any(DataSource.class),Mockito.any(String.class), Mockito.any(Object[].class));
		 List<Map<String,Object>> result  =eventUpdaterDao.queryForMap("updateDummyRecordStatus", obj);
		 assertNotNull(result);
		 }
	
	@Test
	public void getDateListTest() throws ParseException {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Object obj[] = new Object[1];
		SimpleDateFormat df = new SimpleDateFormat("mm-DD-YYYY");
		List<java.util.Date> lis = new ArrayList<>();
		Date date = df.parse("12-12-2010");
		lis.add(date);
		Mockito.doReturn(lis).when(eventUpdaterDao).getDateList(Mockito.any(DataSource.class),Mockito.any(String.class), Mockito.any(Object[].class));
		List<Date> result = eventUpdaterDao.getDateList("updateDummyRecordStatus", obj);
		assertNotNull(result);
		}
	
	@Test
	public void getStringList() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Object obj[] = new Object[1];
		List<String> lis =new ArrayList<>();
		lis.add("aa");
		Mockito.doReturn(lis).when(eventUpdaterDao).getStringList(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Class.class), Mockito.any(Object[].class));
		List<String> result = eventUpdaterDao.getStringList("updateDummyRecordStatus", obj);
		assertNotNull(result);
	}
	
	@Test
	public void frameQueryTest() {
		Object obj[] = new Object[1];
		List<String> lis =new ArrayList<>();
		lis.add("aa");
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(lis).when(eventUpdaterDao).getStringList(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Class.class), Mockito.any(Object[].class));
		List<String> result = eventUpdaterDao.frameQuery("updateDummyRecordStatus", obj, "aaa");
		assertNotNull(result);
	}
	
	@Test
	public void queryForSystemDateTest() {
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn("aaa").when(eventUpdaterDao).queryForString(Mockito.any(DataSource.class),Mockito.any(String.class),
				 Mockito.any(Object[].class));
		String result = eventUpdaterDao.queryForSystemDate("updateDummyRecordStatus", obj);
		assertEquals("aaa",result);
	}
	
	@Test
	public void getSubscriberDetailsForCmsCalc() {
		Object obj[] = new Object[1];
		List<Object> result = new ArrayList<>();
		//result.add(new Object());
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class), Mockito.any(PolicySubscriberDataRowMapper.class));
		SubscriberData res = eventUpdaterDao.getSubscriberDetailsForCmsCalc("updateDummyRecordStatus", obj);
		assertEquals(null,res);
	}
	@Test
	public void getMemberDetailsForCmsCalc() {
		Object obj[] = new Object[1];
		List<Object> result = new ArrayList<>();
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class), Mockito.any(MemberDemographicDataRowMapper.class));
		MemberDemographicData result1=eventUpdaterDao.getMemberDetailsForCmsCalc("updateDummyRecordStatus", obj);
		assertEquals(null,result1);
	}
	
	@Test
	public void getCmsDetails() {
		Object obj[] = new Object[1];
		List<Object> result = new ArrayList<>();
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class), Mockito.any(CmsDetailRowMapper.class));
		CmsDetail cms=eventUpdaterDao.getCmsDetails("updateDummyRecordStatus",obj);
		assertEquals(null,cms);
	}
	
	@Test
	public void getAgentPolicy() {
		Object obj[] = new Object[1];
		List<Object> result = new ArrayList<>();
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class), Mockito.any(AgentPolicyRowMapper.class));
		AgentPolicyData result1=eventUpdaterDao.getAgentPolicy("updateDummyRecordStatus", obj);
		assertEquals(null,result1);
		
	}
	
	@Test
	public void getLatestAgentPolicy() {
		Object obj[] = new Object[1];
		List<Object> result = new ArrayList<>();
		//result.add(new Object());
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class), Mockito.any(AgentPolicyRowMapper.class));
		AgentPolicyData result1=eventUpdaterDao.getLatestAgentPolicy(obj);
		assertEquals(null,result1);
		
		
	}
	
	@Test
	public void getReportDate() {
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn("aa").when(eventUpdaterDao).queryForString(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class));
		String result = eventUpdaterDao.getReportDate("updateDummyRecordStatus", obj);
		assertEquals("aa",result);
	}
	
	@Test
	public void updateCancelIndPremiumTest() {
		Integer res=1;
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(1).when(eventUpdaterDao).update(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class));
		Integer result = eventUpdaterDao.updateCancelIndPremium(obj, "updateDummyRecordStatus");
		assertEquals(res,result);
	}
	
	@Test
	public void fetchPolicyData() {
		Object obj[] = new Object[1];
		List<Object> obj1= new ArrayList<>();
		obj1.add(new Object());
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(obj1).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class),Mockito.any(PolicyDataRowMapper.class));
		assertNotNull(eventUpdaterDao.fetchPolicyData(obj));
	}
	
	@Test
	public void fetchPolicyDataRetro() {
		Object obj[] = new Object[1];
		List<Object> obj1= new ArrayList<>();
		obj1.add(new Object());
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(obj1).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class),
				Mockito.any(Object[].class),Mockito.any(PolicyDataRowMapper.class));
		assertNotNull(eventUpdaterDao.fetchPolicyDataRetro(obj));
	}
	
	@Test
	public void getSubscriberDetails() {
		Object obj[] = new Object[1];
		List<Object> rs =new ArrayList<>();
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(rs).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(PolicySubscriberDataRowMapper.class));
		SubscriberData res = eventUpdaterDao.getSubscriberDetails("updateDummyRecordStatus", obj);
		assertEquals(null,res);
	}
	
	@Test
	public void getAgentPaySchduleData() {
		Object obj[] = new Object[1];
		List<Object> rs =new ArrayList<>();
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(rs).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(AgentPaySchduleRowMapper.class));
		List<Object> res = eventUpdaterDao.getAgentPaySchduleData("updateDummyRecordStatus", obj);
		assertNotNull(res);
	}
	
	@Test
	public void getPolicyIndicatorDetails() {
		Object obj[] = new Object[1];
		List<Object> rs =new ArrayList<>();
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(rs).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(PolicyIndDataRowMapper.class));
		List<Object> res = eventUpdaterDao.getPolicyIndicatorDetails("updateDummyRecordStatus", obj);
		assertNotNull(res);
	}
	
	@Test
	public void queryForCnePolicyId() {
		Integer result =1;
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(1).when(eventUpdaterDao).queryForInt(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class));
		Integer res = eventUpdaterDao.queryForCnePolicyId("updateDummyRecordStatus", obj);
		assertNotNull(res);
		
	}
	@Test
	public void getPolicySchduleData() {
		List<Object> result=new ArrayList<>();
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(PrefundRowMapper.class));
		List<AgentPaySchduleData> res = eventUpdaterDao.getPolicySchduleData("updateDummyRecordStatus", obj);
		assertNotNull(res);
		
	}
	
	@Test
	public void getPolicySchduleLatestDataTest() {
		List<Object> result=new ArrayList<>();
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(PlcySchduleRowMapper.class));
		 List<Object> res = eventUpdaterDao.getPolicySchduleLatestData("updateDummyRecordStatus", obj);
		 assertNotNull(res);
	}
	@Test
	public void getMemLatestData() {
		List<Object> result=new ArrayList<>();
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(MemberDemographicDataRowMapper.class));
		 List<Object> res = eventUpdaterDao.getMemLatestData("updateDummyRecordStatus", obj);
		 assertNotNull(res);
	}
	
	@Test
	public void getAgentOverride() {
		List<Object> result=new ArrayList<>();
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(AgentPolicyRowMapper.class));
		AgentPolicyData result1 = eventUpdaterDao.getAgentOverride("updateDummyRecordStatus", obj);
		assertEquals(null,result1);
		
		
	}
	
	@Test
	public void getAgentDetails() {
		List<Object> result=new ArrayList<>();
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(AgentPolicyRowMapper.class));
		List<AgentPolicyData> result1 = eventUpdaterDao.getAgentDetails("updateDummyRecordStatus", obj);
		assertNotNull(result1);
	}
	
	@Test
	public void getAgentTin() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn("111").when(eventUpdaterDao).queryForString(Mockito.any(String.class),Mockito.any(Object[].class));
		assertEquals("111",eventUpdaterDao.getAgentTin("111"));
	}
	
	@Test
	public void isPremExistTest() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(1).when(eventUpdaterDao).queryForInt(Mockito.any(String.class),Mockito.any(Object[].class));
		assertEquals(true,eventUpdaterDao.isPremExist(11));
	}
	@Test
	public void getAgentId() {
		Integer result =1;
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(1).when(eventUpdaterDao).queryForInt(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class));
		assertEquals(result,eventUpdaterDao.getAgentId("11"));
	}
	@Test
	public void getSubDtlsByPlcyId() {
		//Object obj[] = new Object[1];
		List<Object> result=new ArrayList<>();
		Object obj[] = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(result).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(PolicySubscriberDataRowMapper.class));
		SubscriberData res = eventUpdaterDao.getSubDtlsByPlcyId("updateDummyRecordStatus", obj);
		assertEquals(null,res);
		
	}
	@Test
	public void getAgentTierFactor() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn("111").when(eventUpdaterDao).queryForString(Mockito.any(String.class), Mockito.any(Object[].class));
		assertEquals("111",eventUpdaterDao.getAgentTierFactor("11"));
	}
	
	@Test
	public void testInsertBulkRecordsQuery() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doNothing().when(eventUpdaterDao).insertBulkRecordsQuery(Mockito.any(DataSource.class),
				Mockito.any(String.class), Mockito.anyMap());
		eventUpdaterDao.insertBulkRecordsQuery("updateDummyRecordStatus", new HashMap<Integer,Object[]>());
		assertEquals(0,eventUpdaterDao.update(dataSource,"test query" ,"test query"));
	}
	
	@Test
	public void getSalesChannelValueTest() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn("Success").when(eventUpdaterDao).queryForString(Mockito.anyString(),
				Mockito.any(Object[].class));
		Assert.assertEquals("Success",eventUpdaterDao.getSalesChannelValue(111));
	}
		
	@Test 
	public void testGetSwitcherValue() {
		mockList = new ArrayList<Object>();
		long millis=System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(mockList).when(eventUpdaterDao).query(Mockito.any(DataSource.class),Mockito.any(String.class), 
				Mockito.any(Object[].class),Mockito.any(PolicySubscriberDataRowMapper.class));
		eventUpdaterDao.getSwitcherValue(123, date, "MPRE");
		assertEquals(0,eventUpdaterDao.update(dataSource,"test query" ,"test query"));
	}
	
	@Test
	public void testupdateErrorRecByStatusAndCategory() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn(1).when(eventUpdaterDao).update(any(DataSource.class), any(String.class),
				any(MapSqlParameterSource.class));
		eventUpdaterDao.updateErrorRecByStatusAndCategory("", ErrorStatus.ERROR, "");
		assertEquals(0, eventUpdaterDao.update(dataSource, "test query", "test query"));
	}
	
	@Test
	public void testUpdatePlcyApplicationLog() {
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		doReturn(1).when(eventUpdaterDao).update(any(DataSource.class), any(String.class),
				any(MapSqlParameterSource.class));
		eventUpdaterDao.updatePlcyApplicationLog(1,1);
		assertEquals(0,eventUpdaterDao.update(dataSource,"test query" ,"test query"));
	}
	
	@Test
	public void getAgentPolicyByPlcyIdListTest() {
		List<Object> res = new ArrayList<>();
		Object[] values = new Object[1];
		Mockito.when(templateFactory.getJdbcTemplate(dataSource)).thenReturn(jdbcTemplate);
		Mockito.doReturn(res).when(eventUpdaterDao).query(Mockito.any(DataSource.class), Mockito.any(String.class), Mockito.any(Object[].class),
				Mockito.any(AgentPolicyRowMapper.class));
		List<AgentPolicyData> result = eventUpdaterDao.getAgentPolicyByPlcyIdList(values);
		assertNotNull(result);
		
		
	}
	
	@Test
	public void deleteErrorRecordTest() {
		doReturn(1).when(eventUpdaterDao).update(Mockito.any(DataSource.class), Mockito.anyString(),
				Mockito.anyString());
		eventUpdaterDao.deleteErrorRecord("17991160");
		assertEquals(1,eventUpdaterDao.update(dataSource,"test query" ,"test query"));
	}
	
}
