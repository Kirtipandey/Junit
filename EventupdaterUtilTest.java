package com.anthem.sclc.entcomp.eventupdater.svc.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventUpdaterUtilTest {

	@InjectMocks
	EventUpdaterUtil target;
	
	@Test
	public void testparseDate() throws Exception{
		java.util.Date dt = target.parseDate("22-10-2018", "dd-MM-yyyy");
		assertNotNull(dt);
	}
	
	@Test
	public void testGetMonthEndDateForADate() throws Exception {
		
		// Null tests
		java.util.Date result = EventUpdaterUtil.getMonthEndDateForADate(null);
		Assert.assertNull(result);
		
		// Not null tests
		SimpleDateFormat sdfObj = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date input = sdfObj.parse("08-01-2020");
		java.util.Date expected = sdfObj.parse("08-31-2020");
		
		result = EventUpdaterUtil.getMonthEndDateForADate(input);		
		Assert.assertNotNull(result);
		Assert.assertEquals(expected, result);		
	}
	
	@Test
	public void testConvertLocalDateToUtilDate() throws Exception {
		// Null tests
		java.util.Date result = EventUpdaterUtil.convertLocalDateToUtilDate(null);
		Assert.assertNull(result);
		
		// Not null tests
		SimpleDateFormat sdfObj = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date expected = sdfObj.parse("08-01-2020");
		java.time.LocalDate inputDate = EventUpdaterUtil.getLocalDate(expected);
		
		result = EventUpdaterUtil.convertLocalDateToUtilDate(inputDate);
		Assert.assertNotNull(result);
		Assert.assertEquals(expected, result);		
	}
	
	@Test
	public void testgetUtilDate() throws Exception {
		java.util.Date result = EventUpdaterUtil.getUtilDate(null);
		Assert.assertNull(result);
		
		long d = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(d);
		result = EventUpdaterUtil.getUtilDate(date);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testgetTokenizer() throws Exception {
		String val = "E";
		List<String> inputlist = new ArrayList<String>();
		inputlist.add("O-Open Enrollment");
		inputlist.add("E-External");
		EventUpdaterUtil.getTokenizer(val, inputlist);
		Assert.assertNotNull(val);
	}
	
	@Test
	public void getDateList() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date idCommEffDt = sdf.parse("2021-01-01");
		Date yearEndDt = sdf.parse("2021-02-01");
		EventUpdaterUtil.getDateList(idCommEffDt, yearEndDt);
		assertNotNull(idCommEffDt);
	}
	
	@Test
	public void testAddErrorStatus() {
		Assert.assertNotNull(EventUpdaterUtil.addErrorStatus("UNPROCESSED"));
	}
	
	@Test
	public void testRemoveErrorStatus() {
		Assert.assertNotNull(EventUpdaterUtil.removeErrorStatus("UNPROCESSED-ERR"));
	}
	
	@Test
	public void testGetYearFromDate() throws Exception {
		
		// Null tests
		int result = EventUpdaterUtil.getYearFromDate(null);
		int expected = 0;
		Assert.assertEquals(expected,result);
		
		// Not null tests
		SimpleDateFormat sdfObj = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date input = sdfObj.parse("01-01-2021");
		expected = 2021;
		result = EventUpdaterUtil.getYearFromDate(input);		
		Assert.assertEquals(expected, result);		
	}

}
