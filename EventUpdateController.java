package com.anthem.sclc.entcomp.eventupdater.svc.controller;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anthem.sclc.entcomp.common.bean.payload.CNSPayload;
import com.anthem.sclc.entcomp.svc.core.controller.EntCompServiceController;
import com.anthem.sclc.entcomp.svc.core.log.EntCompLogFactory;
import com.anthem.sclc.entcomp.svc.core.log.EntCompLogger;

import org.springframework.web.util.HtmlUtils;

/**
 * The Class EventUpdateController.
 */
@RestController
public class EventUpdateController extends EntCompServiceController {


    /** The logger. */
    private static EntCompLogger logger = EntCompLogFactory.getLogger(EventUpdateController.class, 
    		"coins_event_updater_v1", Thread.currentThread().getId());

    /**
     * Validate if an agent has valid licensing and certification details.
     *
     * @param payLoadStr the pay load str
     * @return EventData with status set to true or false
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public String updateStatus(@RequestBody String payLoadStr) throws IOException {
        //process request
    	String payloadDataSanitized = HtmlUtils.htmlEscape(payLoadStr);
    	payloadDataSanitized = HtmlUtils.htmlUnescape(payloadDataSanitized);

    	 Object response = entCompService.Process(new CNSPayload().convertFrom(payloadDataSanitized), null, null);
    	 CNSPayload cnsResponse = (CNSPayload) response;
    	 logger.debug("Response received for event id : ", cnsResponse.getPayload().getEvent().getId());
         return ((CNSPayload)response).convertToStr();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.GET})
    public String getUpdateStatus(@RequestBody String payLoadStr) throws IOException {
        //process request
    	String payloadDataSanitized = HtmlUtils.htmlEscape(payLoadStr);
    	payloadDataSanitized = HtmlUtils.htmlUnescape(payloadDataSanitized);

    	 Object response = entCompService.Process(new CNSPayload().convertFrom(payloadDataSanitized), null, null);
    	 CNSPayload cnsResponse = (CNSPayload) response;
    	 logger.debug("Response received for event id : ", cnsResponse.getPayload().getEvent().getId());
         return ((CNSPayload)response).convertToStr();
    }

}
