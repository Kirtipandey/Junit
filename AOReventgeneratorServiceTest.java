package com.anthem.sclc.entcomp.eventgenerator.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.anthem.sclc.entcomp.common.bean.payload.AgentRequestV2;
import com.anthem.sclc.entcomp.common.bean.payload.AgentValidationInRequest;
import com.anthem.sclc.entcomp.common.bean.payload.CNSPayload;
import com.anthem.sclc.entcomp.common.bean.payload.PolicyDetails;
import com.anthem.sclc.entcomp.eventgenerator.dao.IEventDAO;


/**
 * JUnit test class for AOREventGeneratorService
 */
@RunWith(MockitoJUnitRunner.class)
public class AOREventGeneratorServiceTest {
	@InjectMocks
	AOREventGeneratorService aorEventGeneratorService;
	
	@Mock
	IEventDAO generateEventDAO;
	
	@Before
    public void setUp(){
    	MockitoAnnotations.initMocks(this);
    	
    }
	
	@Test
	public void testCreatePayloadWithNull() throws ParseException {
		String policyId = "";
		String agentPolicyId = "";
		DataSource dataSource = null;
		AgentValidationInRequest response = null;
		
		Mockito.when(generateEventDAO.fetchEventSequence(Mockito.any())).thenReturn("");
		Mockito.when(generateEventDAO.fetchPolicyAgentDetails(Mockito.any(), Mockito.anyString(),Mockito.anyString())).thenReturn(response);
		
		CNSPayload cnsPayload = aorEventGeneratorService.createPayload(policyId, agentPolicyId, dataSource);
		
		Assert.assertEquals(null, cnsPayload);
	}
	
	@Test
	public void testCreatePayloadWithoutNull() throws ParseException {
		String policyId = "200008626";
		String agentPolicyId = "8791103";
		DataSource dataSource = null;
		AgentValidationInRequest response = new AgentValidationInRequest();
		PolicyDetails policyDetail = new PolicyDetails();
		List<PolicyDetails> policyDetails = new ArrayList<PolicyDetails>();
		policyDetail.setCompanyCode("365C");
		policyDetail.setMbu("SR");
		policyDetail.setAgentEffectiveDate("01-01-2020");
		policyDetail.setIdCommissionEffectiveDate("01-01-2020");
		policyDetails.add(policyDetail);
		response.setPolicy(policyDetails);
		
		AgentRequestV2 agentRequest = new AgentRequestV2();
		ArrayList<AgentRequestV2> agentRequests = new ArrayList<AgentRequestV2>();
		agentRequest.setPaidTinEffDt("01-01-2020");
		agentRequest.setWritingTIN("1000345");
		agentRequest.setParentTIN("1000345");
		agentRequest.setPaidAgentTypeCode("B");
		agentRequests.add(agentRequest);
		response.setAgentList(agentRequests);
		
		Mockito.when(generateEventDAO.fetchEventSequence(Mockito.any())).thenReturn("17495120");
		Mockito.when(generateEventDAO.fetchPolicyAgentDetails(Mockito.any(), Mockito.anyString(),Mockito.anyString())).thenReturn(response);
		CNSPayload cnsPayload = aorEventGeneratorService.createPayload(policyId, agentPolicyId, dataSource);
		
		Assert.assertNotNull(cnsPayload);
		
	}
}
