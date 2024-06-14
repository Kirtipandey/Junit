/**
 * 
 */
package com.anthem.sclc.entcomp.eventupdater.svc.exception;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;

import com.anthem.sclc.entcomp.eventupdater.svc.exception.EventUpdateExption.exceptionCategory;

/**
 * @author AC54733
 *
 */
public class EventUpdateExceptionTest {
	
	@Test
	public void testEnum() throws SQLException{
		EventUpdateExption ext = new EventUpdateExption();
	
	    assertEquals("ENRLMENT_RECORD_NOT_FOUND", exceptionCategory.ENRLMENT_RECORD_NOT_FOUND.toString());
	    assertEquals("COMMISSION_EVENT_NOT_FOUND", exceptionCategory.COMMISSION_EVENT_NOT_FOUND.toString());
	    assertEquals("ENROLLMENT_MATCHING_RECORD_NOT_FOUND", exceptionCategory.ENROLLMENT_MATCHING_RECORD_NOT_FOUND.toString());
	    assertEquals("CANCEL_EVENT_RECORD_NOT_FOUND", exceptionCategory.CANCEL_EVENT_RECORD_NOT_FOUND.toString());
		
	}

}
