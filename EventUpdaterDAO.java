package com.anthem.sclc.entcomp.eventupdater.svc.dao;


import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anthem.sclc.entcomp.common.bean.payload.Payload;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.AgentPaySchduleData;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.AgentPolicyData;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.CmsDetail;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.MemberDemographicData;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.SubscriberData;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.SwitcherData;
import com.anthem.sclc.entcomp.eventupdater.svc.constants.EventUpdaterConstants;
import com.anthem.sclc.entcomp.eventupdater.svc.exception.EventUpdateExption;
import com.anthem.sclc.entcomp.eventupdater.svc.exception.EventUpdateExption.exceptionCategory;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.AgentPaySchduleRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.AgentPolicyRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.CmsDetailRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.MemberDemographicDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PlcySchduleRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PolicyDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PolicyIndDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.PolicySubscriberDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.mapper.SwitcherDataRowMapper;
import com.anthem.sclc.entcomp.eventupdater.svc.util.ExceptionProperties;
import com.anthem.sclc.entcomp.svc.core.dao.EntCompDAO;
import com.anthem.sclc.entcomp.svc.core.dao.JdbcTemplateFactory;
import com.anthem.sclc.entcomp.svc.core.error.ErrorStatus;
import com.anthem.sclc.entcomp.svc.core.log.EntCompLogFactory;
import com.anthem.sclc.entcomp.svc.core.log.EntCompLogger;
import com.anthem.sclc.entcomp.svc.core.log.LogCIM;
import com.anthem.sclc.entcomp.svc.core.util.BeanFactory;

/**
 * The Class EventUpdaterDAO.
 */
@Repository
public class EventUpdaterDAO extends EntCompDAO implements IEventUpdaterDAO {
	
	/** The logger. */
	private static EntCompLogger logger = EntCompLogFactory.getLogger(EventUpdaterDAO.class,
			EventUpdaterConstants.COINS_EVENT_UPDATER_V1, Thread.currentThread().getId());
	
	/** The datasource. */
	@Autowired
	@Qualifier(value="coreDS")
	private DataSource datasource;
	
	@Autowired
	JdbcTemplateFactory templateFactoryDB;
	
	public int update(String queryClassName,Object[] values){
		String query = (String) BeanFactory.getBean(queryClassName);
		try {
			return this.update(datasource, query, values);
		} catch(DataAccessException exception) {
			
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
		
	public Integer queryForInt(String queryClassName,Object[] values){
		try{
			String query = (String) BeanFactory.getBean(queryClassName);
			return this.queryForInt(datasource, query, values);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int getAgentId(Payload payload,Map<Integer, Object> agentData,String tinId,String tinType){
		Integer agentId = queryForInt("query_agntId_event_update", new Object[]{tinId});
		Integer agentIdValue = null;
		Object[] values = new Object[] {agentId};
		if(null == agentId){
			try{
				agentIdValue = insert("insert_agent", agentData, "AGNT_ID");
			}
			catch(DuplicateKeyException dupKeyEx){
				agentId = queryForInt("query_agntId_event_update", new Object[]{tinId});
				logger.info("Skipping insertion since AGNT_TIN already available for ");
				logger.info("agentId :: "+agentId);
				agentIdValue = agentId;
				logger.info("agentIdValue :: "+agentIdValue);
				return agentIdValue;
			} catch(DataAccessException exception) {
				throw new EventUpdateExption().handleError(exceptionCategory.COULD_NOT_INSERT_AGENT, "Not able to insert Agent Details", values);
			}
		}else{
			agentIdValue = agentId;
		}
		return agentIdValue;
	}
	
	/* (non-Javadoc)
	 * @see com.anthem.sclc.entcomp.eventidentification.svc.dao.IEventIdentificationDAO#queryForString(java.lang.String, java.lang.Object[])
	 */
	public String queryForString(String queryClassName,Object[] values){
		try{
			String query = (String) BeanFactory.getBean(queryClassName);
			return this.queryForString(datasource, query, values);
		}catch (EmptyResultDataAccessException e) {
			return null;
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	public Date queryForDate(String queryClassName,Object[] values){
		try{
			String query = (String) BeanFactory.getBean(queryClassName);
			return this.queryForDate(datasource, query, values);
		}catch (EmptyResultDataAccessException e) {
			return null;
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	public int insert(String queryClassName,Map<Integer, Object> values,String prmykeyColmnName){
		String query = (String) BeanFactory.getBean(queryClassName);
		Object[] valuesArray = new Object[] {};
		try {
		return this.insert(datasource, query, values, prmykeyColmnName);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, valuesArray);
		}
	}
	
	public void insertDynamicQuery(String query,Map<Integer, Object> values){
		Object[] valuesArray = new Object[] {};
		try {
		this.insertDynamicQuery(datasource, query, values);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(query,
					ExceptionProperties.get(query), exception, valuesArray);
		}
	}
	
	public void insertBulkRecordsQuery(String query,Map<Integer, Object[]> values) {
		Object[] valuesArray = new Object[] {};
		try {
		this.insertBulkRecordsQuery(datasource, query, values);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(query,
					ExceptionProperties.get(query), exception, valuesArray);
		}
	}
	
	public List<Map<String,Object>> queryForMap(String queryClassName,Object[] values){
		String query = (String) BeanFactory.getBean(queryClassName);
		try {
		return this.queryForList(datasource, query, values);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	public List<java.util.Date> getDateList(String queryClassName,Object[] values){
		String query = (String) BeanFactory.getBean(queryClassName);
		try {
		return this.getDateList(datasource, query, values);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	public List<String> getStringList(String queryClassName,Object[] values){
		try {
		String query = (String) BeanFactory.getBean(queryClassName);
		return this.getStringList(datasource, query,String.class,values);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	/**
	 * Frame query to fetch the CMSN_CALCN_ID from sequence
	 * @param cmsnCalcnSeq - Sequence name
	 */
	public List<String> frameQuery(String queryClassName,Object[] values, String cmsnCalcnSeq){
		try {
		String query = (String) BeanFactory.getBean(queryClassName);
		// Replace the sequence name in query as it varies from env to env
		query = query.replace("SEQ_NAME", cmsnCalcnSeq);
		return this.getStringList(datasource, query,String.class,values);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	public int insertList(String queryClassName,List<Map<Integer, Object>> values,String prmykeyColmnName){
		Object[] valuesArray = new Object[] {};
		String query = (String) BeanFactory.getBean(queryClassName);
		try {
		return this.insertList(datasource, query, values, prmykeyColmnName);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, valuesArray);
		}
	}
	
	public String queryForSystemDate(String queryClassName,Object[] values){
		try{
			String query = (String) BeanFactory.getBean(queryClassName);
			return this.queryForString(datasource, query, values);
		}catch (EmptyResultDataAccessException e) {
			return null;
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	//added for commission calculation mapping
	
	
	public SubscriberData getSubscriberDetailsForCmsCalc(String queryClassName,Object[] values) {
		try {
		String query = (String) BeanFactory.getBean(queryClassName);
		List<Object> result = this.query(datasource, query, values, new PolicySubscriberDataRowMapper());
		// As per discussion, return null even if we find more than 1 record
		if(result == null || result.isEmpty() || result.size() > 1){
			return null;
		}else {
			return (SubscriberData)result.get(0);
		}
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	
	public MemberDemographicData getMemberDetailsForCmsCalc(String queryClassName,Object[] values) {
		try {
		String query = (String) BeanFactory.getBean(queryClassName);
		List<Object> result = this.query(datasource, query, values, new MemberDemographicDataRowMapper());
		// As per discussion, return null even if we find more than 1 record
		if(result == null || result.isEmpty() || result.size() > 1){
			return null;
		}else {
			return (MemberDemographicData)result.get(0);
		}
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	
	
	public CmsDetail getCmsDetails(String queryClassName,Object[] values) {
		try {
		String query = (String) BeanFactory.getBean(queryClassName);
		List<Object> result = this.query(datasource, query, values, new CmsDetailRowMapper());
		// As per discussion, return null even if we find more than 1 record
		if(result == null || result.isEmpty() || result.size() > 1){
			return null;
		}else {
			return (CmsDetail)result.get(0);
		}
		
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	
	public AgentPolicyData getAgentPolicy(String queryClassName,Object[] values) {
		try {
		String query = (String) BeanFactory.getBean(queryClassName);
		List<Object> result = this.query(datasource, query, values, new AgentPolicyRowMapper());
			// As per discussion, return null even if we find more than 1 record
		if(result == null || result.isEmpty() || result.size() > 1){
			return null;
		}else {
			return (AgentPolicyData)result.get(0);
		}
		
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	public AgentPolicyData getLatestAgentPolicy(Object[] values) {
		
		String queryClassName = "get_agent_plcy_by_plcy_id";
		try {
		String query = (String) BeanFactory.getBean(queryClassName);
		List<Object> result = this.query(datasource, query, values, new AgentPolicyRowMapper());
			// As per discussion, return null even if we find more than 1 record
		if(result == null || result.isEmpty()){
			return null;
		}else {
			return (AgentPolicyData)result.get(0);
		}
		
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	public String getReportDate(String queryClassName, Object[] values){
		try {
			String query = (String) BeanFactory.getBean(queryClassName);
			return this.queryForString(datasource, query, values);
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	/**
	 * DAO method for update CANCEL_IND field in premium table 
	 */
	@Override
	public Integer updateCancelIndPremium(Object[] columnValues, String beanName){
		String updateQuery = (String) BeanFactory.getBean(beanName);
		return update(datasource, updateQuery, columnValues);
	}
	
	/**
	 * DAO method for fetch policy data
	 */
	@Override
	public List<Object> fetchPolicyData(Object[] values) {
		String fetchPolicyDtl = (String) BeanFactory.getBean(EventUpdaterConstants.RETRO_FETCH_POLICY_DETAIL);
		return this.query(datasource, fetchPolicyDtl, values, new PolicyDataRowMapper());
	}

	/**
	 * DAO method for fetch retro policy data
	 * COINS-3734
	 */
	@Override
	public List<Object> fetchPolicyDataRetro(Object[] values) {
		String fetchPolicyDtl = (String) BeanFactory.getBean(EventUpdaterConstants.FETCH_POLICY_DETAIL_RETRO);
		return this.query(datasource, fetchPolicyDtl, values, new PolicyDataRowMapper());
	}
	
	@Override
	public SubscriberData getSubscriberDetails(String fetchSubscriberdata, Object[] objects) {
		try {
			String query = (String) BeanFactory.getBean(fetchSubscriberdata);
			List<Object> result = this.query(datasource, query, objects, new PolicySubscriberDataRowMapper());
			if(result == null || result.isEmpty()){
				return null;
			}else if( 1 <= result.size()){
				return (SubscriberData)result.get(0);
			}
			return null;
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(fetchSubscriberdata,
					ExceptionProperties.get(fetchSubscriberdata), exception, objects);
		}
	}
	
	@Override
	public List<Object> getAgentPaySchduleData(String queryClassName, Object[] values) {
		try {
			String query = (String) BeanFactory.getBean(queryClassName);
			List<Object> result = this.query(datasource, query, values, new AgentPaySchduleRowMapper());
			if (result == null || result.isEmpty()) {
				return Collections.emptyList();
			} else {
				logger.debug("Returning list of size {}", result.size());
				return result;
			}
		} catch (DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName, ExceptionProperties.get(queryClassName),
					exception, values);
		}

	}
	
	@Override
	public List<Object> getPolicyIndicatorDetails(String queryClassName, Object[] values) {
		try {
			String query = (String) BeanFactory.getBean(queryClassName);
			List<Object> result = this.query(datasource, query, values, new PolicyIndDataRowMapper());
			if (result == null || result.isEmpty()) {
				return Collections.emptyList();
			} else {
				logger.debug("Returning list of size {}", result.size());
				return result;
			}
		} catch (DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName, ExceptionProperties.get(queryClassName),
					exception, values);
		}

	}
	
	@Override
	public Integer queryForCnePolicyId(String queryClassName,Object[] values){
		try{
			String query = (String) BeanFactory.getBean(queryClassName);
			return this.queryForInt(datasource, query, values);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}


	@Override
	public List<AgentPaySchduleData> getPolicySchduleData(String queryClassName, Object[] values) {
		try {
			List<AgentPaySchduleData> schdlList = new ArrayList<>();
			String query = (String) BeanFactory.getBean(queryClassName);
			List<Object> result = this.query(datasource, query, values, new PlcySchduleRowMapper());
			if (result == null || result.isEmpty()) {
				return Collections.emptyList();
			} else {
				logger.debug("Returning list of size {}", result.size());
				result.forEach(obj -> schdlList.add((AgentPaySchduleData)obj));
			}
			return schdlList;
		} catch (DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName, ExceptionProperties.get(queryClassName),
					exception, values);
		}

	}
	
	@Override
	public List<Object> getPolicySchduleLatestData(String queryClassName, Object[] values) {
		try {
			String query = (String) BeanFactory.getBean(queryClassName);
			List<Object> result = this.query(datasource, query, values, new PlcySchduleRowMapper());
			if (result == null || result.isEmpty()) {
				return Collections.emptyList();
			} else {
				logger.debug("Returning list of size {}", result.size());
				return result;
			}
		} catch (DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName, ExceptionProperties.get(queryClassName),
					exception, values);
		} 

	}
	
	
	@Override
	public List<Object> getMemLatestData(String queryClassName, Object[] values) {
		try {
			String query = (String) BeanFactory.getBean(queryClassName);
			List<Object> result = this.query(datasource, query, values, new MemberDemographicDataRowMapper());
			if (result == null || result.isEmpty()) {
				return Collections.emptyList();
			} else {
				logger.debug("Returning list of size {}", result.size());
				return result;
			}
		} catch (DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName, ExceptionProperties.get(queryClassName),
					exception, values);
		}

	}
	
	public AgentPolicyData getAgentOverride(String queryClassName,Object[] values) {
		try {
		String query = (String) BeanFactory.getBean(queryClassName);
		List<Object> result = this.query(datasource, query, values, new AgentPolicyRowMapper());
			// As per discussion, return null even if we find more than 1 record
		if(result == null || result.isEmpty() || result.size() > 1){
			return null;
		}else {
			return (AgentPolicyData)result.get(0);
		}
		
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	@Override
	public List<AgentPolicyData> getAgentDetails(String query, Object[] params) {
		try {
			List<AgentPolicyData> agntList = new ArrayList<>();
			String queryStr = (String) BeanFactory.getBean(query);
			List<Object> result = this.query(datasource, queryStr, params, new AgentPolicyRowMapper());
			if (result == null || result.isEmpty()) {
				return Collections.emptyList();
			} else {
				logger.debug("Returning list of size {}", result.size());
				result.forEach(obj -> agntList.add( (AgentPolicyData)obj) );
			}
			return agntList;
		} catch (DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(query, ExceptionProperties.get(query), exception, params);
		}
	}

	public String getAgentTin(String agentId) {
		try {
			return this.queryForString("query_agnt_tin", new Object[] { agentId });
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Agent Tin not found in agent table for Agent Id:{}", agentId);
			return null;
		}
	}
	
	public boolean isPremExist(int policyId)
	{
		return this.queryForInt("checkPremiumRecords", new Object[]{policyId}) > 0;
	}
	
	public Integer getAgentId(String tinId) {
		try {
			String query = (String) BeanFactory.getBean("query_agntId_event_update");
			return this.queryForInt(datasource, query, new Object[] { tinId });
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (DataAccessException exception) {
			//Logging without PII data and inserting exceptions in error table with required PII data
			throw new EventUpdateExption().mapAndhandleErrorWithoutLoggingPII("query_agntId", ExceptionProperties.get("query_agntId"),
					exception, new Object[] { "" }, new Object[] { tinId });
		}
	}
	
	@Override
	public SubscriberData getSubDtlsByPlcyId(String fetchSubscriberdata, Object[] objects) {
		try {
			String query = (String) BeanFactory.getBean(fetchSubscriberdata);
			List<Object> result = this.query(datasource, query, objects, new PolicySubscriberDataRowMapper());
			if(result == null || result.isEmpty()){
				return null;
			}else if( 1 <= result.size()){
				return (SubscriberData)result.get(0);
			}
			return null;
		} catch(DataAccessException exception) {
			throw new EventUpdateExption().mapAndhandleError(fetchSubscriberdata,
					ExceptionProperties.get(fetchSubscriberdata), exception, objects);
		}
	}

	public String getAgentTierFactor(String rateId) {
		try {
			return this.queryForString("get_agent_tier_factor_eventUpdate", new Object[] { rateId });
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Tier factor not found in schedule rate summary table for Rate Id:{}", rateId);
			return null;
		}
	}
	
	public int updateSingleDbCall(String query, SqlParameterSource namedParameters) {
		logger.debug(new LogCIM.event[]{LogCIM.event.DB_QUERY, LogCIM.event.DB_QUERY_PARAMS}, query,namedParameters);
        Instant start = Instant.now();
        NamedParameterJdbcTemplate namedParameterTemplate = new NamedParameterJdbcTemplate(templateFactoryDB.getJdbcTemplate(datasource));
        int result = namedParameterTemplate.update(query, namedParameters); 
        Instant finish = Instant.now();
        logger.debug(new LogCIM.event[]{LogCIM.event.DB_QUERY, LogCIM.event.DB_EXEC_TIME},query,Duration.between(start, finish).toMillis());
        return result;
	}
	
	public int updateEnrollmentErrors(List<String> agPlcyId) {
		String query = (String) BeanFactory.getBean("update_ErrRec_By_AgPlcyId");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("agentPlcyId", agPlcyId);
		return this.update(datasource, query, namedParameters);
	}

	@Override
	public SwitcherData getSwitcherValue(int policyId, Date contrEffDate, String contractCode) {
		String query = (String) BeanFactory.getBean("query_switcher_value");
		try {
			List<Object> result = this.query(datasource, query, new Object[] { policyId, contrEffDate, contractCode },
					new SwitcherDataRowMapper());
			if(result == null || result.isEmpty()){
				return null;
			}else{
				return (SwitcherData)result.get(0);
			}
		} catch(DataAccessException exception){
			throw new EventUpdateExption().mapAndhandleError(query,
					ExceptionProperties.get(query), exception, new Object[] { policyId, contrEffDate});
		}
	}

	@Override
	public String getSalesChannelValue(int policySubscriberId) {
		try {
			return this.queryForString("query_saleschannel_value", new Object[] { policySubscriberId});
		} catch (EmptyResultDataAccessException e) {
			logger.debug("Sales Channel not found for policySubscriberId Id:{}", policySubscriberId);
			return null;
		}
	}
	
	public int updateScheduleErrorsSC(String policyId, String agntPlcyID) {
		String query = (String) BeanFactory.getBean("update_ErrRec_By_AgPlcyId_SC");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("policyId", policyId);
		namedParameters.addValue("agentPlcyId", agntPlcyID);
		return this.update(datasource, query, namedParameters);
	}

	@Override
	public boolean updateErrorRecByStatusAndCategory(String policyId, ErrorStatus status, String category) {
		String query = null;
		query = (String)BeanFactory.getBean("update_error_rec_by_status_category_event_update");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();	
		namedParameters.addValue("status", status.getStatusType());
		namedParameters.addValue("policyId", policyId);
		namedParameters.addValue("category", category);
		logger.debug("Updating error records for policyId:"+policyId);
		return this.update(datasource, query, namedParameters) > 0;
	}
	
	@Override
	public int updateAgentPolicy(int eventId, List<String> agntPlcyIds, java.util.Date endDate) {
		String query = (String)BeanFactory.getBean(EventUpdaterConstants.UPDATE_AGENT_POLICY_FOR_CNE);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("endDate", endDate);
		namedParameters.addValue("agntPlcyIds", agntPlcyIds);
		namedParameters.addValue("eventId", eventId);
		namedParameters.addValue("updatedBy", EventUpdaterConstants.SOURCE_INTERFACE_SERVICE);
		return this.update(datasource, query, namedParameters);
	}
	
	@Override
	public int updatePlcyApplicationLog(int applicationLogId, Integer plcyId){
		String query = (String)BeanFactory.getBean(EventUpdaterConstants.UPDATE_APP_LOG_PLCY);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("plcyId", plcyId);
		namedParameters.addValue("applicationLogId", applicationLogId);
		namedParameters.addValue("updatedBy", EventUpdaterConstants.SOURCE_INTERFACE_SERVICE);
		return this.update(datasource, query, namedParameters);
	    }
	
	@Override
	public List<AgentPolicyData> getAgentPolicyByPlcyIdList(Object[] values){
		String queryClassName = "get_agntIdList";
		try {
			List<AgentPolicyData> agntPlcyList = new ArrayList<>();
			String query = (String) BeanFactory.getBean(queryClassName);
			List <Object> result = this.query(datasource, query, values, new AgentPolicyRowMapper());
			if(null != result) {
				result.forEach(obj -> agntPlcyList.add((AgentPolicyData)obj));
				return agntPlcyList;
			} else {
				return null;
			}
		} catch (DataAccessException exception) {			
			throw new EventUpdateExption().mapAndhandleError(queryClassName,
					ExceptionProperties.get(queryClassName), exception, values);
		}
	}
	
	@Override
	public int updateAgentsToCNE(int eventId, List<String> agntPlcyIds) {
		String query = (String)BeanFactory.getBean(EventUpdaterConstants.UPDATE_PLANCHANGE_CNE_AGENTS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("eventId", eventId);
		namedParameters.addValue("updatedBy", EventUpdaterConstants.SOURCE_INTERFACE_SERVICE);
		namedParameters.addValue("agntPlcyIds", agntPlcyIds);
		return this.update(datasource, query, namedParameters);
	}
	
	@Override
	public int openPreviousAgents(java.util.Date endDate, int eventId, List<String> agntPlcyIds) {
		String query = (String)BeanFactory.getBean(EventUpdaterConstants.OPEN_PLANCHANGE_BEFORE_AGENTS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("endDate", endDate);
		namedParameters.addValue("eventId", eventId);
		namedParameters.addValue("updatedBy", EventUpdaterConstants.SOURCE_INTERFACE_SERVICE);
		namedParameters.addValue("agntPlcyIds", agntPlcyIds);
		return this.update(datasource, query, namedParameters);
	}


	@Override
	public void deleteErrorRecord(String eventId) {
		String deleteQuery = (String) BeanFactory.getBean(EventUpdaterConstants.DELETE_ERROR_RECORD);
        try {
        	Object[] values = new Object[]{eventId};
            int updateStatus = update(datasource, deleteQuery, eventId);
            logger.debug(new LogCIM.event[]{LogCIM.event.DB_RESULT, LogCIM.event.DB_QUERY, LogCIM.event.DB_QUERY_PARAMS}, updateStatus, deleteQuery, values);
        } catch (DataAccessException exception) {
            throw new EventUpdateExption().handleError(EventUpdateExption.exceptionCategory.CANT_DELETE_ERROR_RECORD,
                    "Unable to delete data from COINS_ERROR_RECORD table ", exception, new Object[]{eventId});
        }
		
	}
	
	@Override
	public List<Object> queryForList(String queryClassName, Object[] values, RowMapper<Object> mapper) {
		String query = (String) BeanFactory.getBean(queryClassName);
		List<Object> list = query(datasource, query, values, mapper);
		logger.debug("Returning list of size {} for query {} and value {}", list.size(), query, values);
		return list;
	}

}
