package com.anthem.sclc.entcomp.eventupdater.svc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.anthem.sclc.entcomp.common.bean.payload.Premium;
import com.anthem.sclc.entcomp.eventupdater.svc.bean.SplitPremium;
import com.anthem.sclc.entcomp.eventupdater.svc.constants.EventUpdaterConstants;

/**
 * The Class EventUpdaterUtil.
 */
public class EventUpdaterUtil {
		
	/**
	 * Instantiates a new event updater util.
	 */
	private EventUpdaterUtil() {
		
	}
	/**
	 * Parses the date.
	 *
	 * @param date the date
	 * @param format the format
	 * @return the date
	 * @throws ParseException the parse exception
	 */
	public static Date parseDate(String date,String format) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.parse(date);
	}
	
	/**
	 * Format date.
	 *
	 * @param date the date
	 * @param format the format
	 * @return the string
	 */
	public static String formatDate(Date date,String format){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}
	
	
	/**
	 * Checks if is string contain alphabet.
	 *
	 * @param str the str
	 * @return true, if is string contain alphabet
	 */
	public static boolean isStringContainAlphabet(String str){
		Pattern pattern = Pattern.compile(".*[a-zA-Z]+.*");
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches();
	}
	
	/**
	 * Checks if is null or empty.
	 *
	 * @param str the str
	 * @return true, if is null or empty
	 */
	public static boolean isNullOrEmpty(String str){
		return null == str || str.trim().isEmpty() || "null".equalsIgnoreCase(str.trim());
	}
	

	/**
	 * Checks if is source MA.
	 *
	 * @param source the source
	 * @return true, if is source MA
	 */
	public static boolean isSourceMA(String source){
		return (Arrays.asList(EventUpdaterConstants.MEDICARE_ADVANTAGE).contains(source) ||
		Arrays.asList(EventUpdaterConstants.PARTD).contains(source));
	}
	

	/**
	 * Convert to local date.
	 *
	 * @param date the date
	 * @return the local date
	 */
	public static LocalDate convertToLocalDate(Date date) {
        if(date == null) return null;
        return new LocalDate(date);
    }

	/**
	 * Convert str to int.
	 *
	 * @param value the value
	 * @return the int
	 */
	public static int convertStrToInt(String value){
		int val = 0;
		if(value != null){
			try{
				val = Integer.parseInt(value);
			}catch(Exception e){
				//use default value of 0
			}
		}
		return val;
	}
	
	
	/**
	 * Gets the local date.
	 *
	 * @param date the date
	 * @return the local date
	 */
	public static java.time.LocalDate getLocalDate(Date date) {
		java.time.LocalDate localDate = null;
		if(date!=null) {
			localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return localDate;
	}
	
	/**
	 * Gets the util date.
	 *
	 * @param sqlDate the sql date
	 * @return the util date
	 */
	public static Date getUtilDate(java.sql.Date sqlDate) {
		Date utilDate = null;
		if(sqlDate!=null){
			utilDate = new Date(sqlDate.getTime());			
		}
		return utilDate;

	}
	
	/**
	 * Gets the split prem amt.
	 *
	 * @param splitPremium the split premium
	 * @param premium the premium
	 * @return the split prem amt
	 */
	public static StringBuilder getSplitPremAmt(SplitPremium splitPremium, Premium premium)
	{
        StringBuilder sbAmount = new StringBuilder();
        if(null != splitPremium.getSplitPremBillAmt()) {
               if(null != premium.getPremBillAmtSign() && !premium.getPremBillAmtSign().isEmpty()) {
                     sbAmount.append(premium.getPremBillAmtSign());
               } 
               sbAmount.append(splitPremium.getSplitPremBillAmt());
        }
		return sbAmount;
	}
	
	/**
	 * Gets the recon amt.
	 *
	 * @param splitPremium the split premium
	 * @param premium the premium
	 * @return the recon amt
	 */
	public static StringBuilder getReconAmt(SplitPremium splitPremium, Premium premium)
	{
		StringBuilder sbAmount = new StringBuilder();
        if(null != splitPremium.getReconAmount()) {
            if(null != premium.getPremReconAmtSign() && !premium.getPremReconAmtSign().isEmpty()) {
                  sbAmount.append(premium.getPremReconAmtSign());
            }
            sbAmount.append(splitPremium.getReconAmount());
     }
        return sbAmount;
	}
	
	/**
	 * This method used for convert I/R to INITIAL/RENEWAL.
	 *
	 * @param compType the comp type
	 * @return the comp type string
	 */
	public static String getCompTypeString(String compType) {
		if (EventUpdaterConstants.I.equals(compType)) {
			return EventUpdaterConstants.STRING_INITIAL;
		} else if (EventUpdaterConstants.R.equals(compType)) {
			return EventUpdaterConstants.EVENT_RENEWAL;//Due to Sonar lint error used existing constant
		} else {
			return compType;
		}
	}
	
	/**
	 * This method used to compare sting with null values. String will be trimmed for Comparison. " " will be equal to "". " " will be equal to null
	 *
	 * @param val1 the val 1
	 * @param val2 the val 2
	 * @return true, if is strings equal
	 */
	public static boolean isStringsEqual(String val1, String val2) {
		
		if(val1 == null || val1.trim().equalsIgnoreCase("")){
			val1 = "";
		}
		if(val2 == null || val2.trim().equalsIgnoreCase("")){
			val2 = "";
		}	
		
		return val1.equalsIgnoreCase(val2);
		
	}
	
	/**
	 * Compare Strings.
	 *
	 * @param val1 the val 1
	 * @param val2 the val 2
	 * @return true, if is equal
	 */
	public static boolean isEqual(String val1, String val2) {
		if (null != val1 && null != val2) {
			return val1.equalsIgnoreCase(val2);
		}
		return false;
	}
	/**
	 * Gets the 0101 date.
	 *
	 * @param contractEffDate the contract eff date
	 * @return the 0101 date
	 */
	public static String get0101Date(Date contractEffDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(contractEffDate);
		int year = calendar.get(Calendar.YEAR);
		return EventUpdaterConstants.STRING_01_01.concat(String.valueOf(year));
	}
	

	/**
	 * Convert local date to java util date.
	 *
	 * @param localDate the local date
	 * @return the date
	 */
	public static java.util.Date convertLocalDateToUtilDate(java.time.LocalDate localDate) {
		return localDate == null ? null : java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	

	/**
	 * Gets the month end date for a given date. Dates are of type util.java.Date
	 *
	 * @param inputDate the input date
	 * @return the month end date for A date
	 */
	public static java.util.Date getMonthEndDateForADate(java.util.Date inputDate) {
		
		if (inputDate == null) {
			return null;
		}
		
		java.time.LocalDate date = EventUpdaterUtil.getLocalDate(inputDate);
		java.time.YearMonth lastMonth = YearMonth.of(date.getYear(), date.getMonthValue());
		return EventUpdaterUtil.convertLocalDateToUtilDate(lastMonth.atEndOfMonth());
	}
	

	/**
	 * Gets the tokenizer.
	 *
	 * @param val the val
	 * @param inputList the input list
	 * @return the tokenizer
	 */
	public static String getTokenizer(String val, List<String> inputList) {
		if (val == null) {
			val = null;
		} else {
			for (String element : inputList) {
				if (element.startsWith(val + "-")) {
					String[] tokens = element.split("-");
					val = tokens[tokens.length - 1];
				}
			}
		}
		return val;
	}

	public static Date getEndDateOfMonth(LocalDateTime monthEndDate) {
		if(null != monthEndDate) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(monthEndDate.toDate());
			int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
			cal.set(Calendar.DATE, lastDayOfMonth);
			return cal.getTime();
		}
		return null;
	}

	public static Date getStartDateOfMonth(LocalDateTime localDt) {
		if(null != localDt) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(localDt.toDate());
			int firstDayOfMonth = cal.getActualMinimum(Calendar.DATE);
			cal.set(Calendar.DATE, firstDayOfMonth);
			return cal.getTime();
		}
		return null;
	}
	
	/**
	 * Gets the date list.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return the date list
	 */
	public static List<Date> getDateList(Date startDate, Date endDate) {
		if (null != startDate && null != endDate) {
			Calendar beginCalendar = Calendar.getInstance();
			Calendar finishCalendar = Calendar.getInstance();

			beginCalendar.setTime(startDate);
			finishCalendar.setTime(endDate);
			List<Date> dateList = new ArrayList<>();

			while (beginCalendar.compareTo(finishCalendar) <= 0) {
				// add one month to date per loop
				dateList.add(beginCalendar.getTime());
				beginCalendar.add(Calendar.MONTH, 1);
			}
			return dateList;
		}
		return null;
	}
	
	public static String addErrorStatus(String adjStatus) {
		if(!adjStatus.contains(EventUpdaterConstants.ERROR_SUFFIX))
			adjStatus = adjStatus + EventUpdaterConstants.ERROR_SUFFIX;
		return adjStatus;
	}
	
	public static String removeErrorStatus(String adjStatus) {
		if(adjStatus.contains(EventUpdaterConstants.ERROR_SUFFIX))
			adjStatus = adjStatus.substring(0,adjStatus.indexOf(EventUpdaterConstants.ERROR_SUFFIX));
		return adjStatus;
	}
	
	public static int getYearFromDate(Date date)
	{
		if(date != null)
		{
			LocalDateTime localDt = new LocalDateTime(date);
			{
				return localDt.getYear();
			}
		}
		return 0;
	}
	
	/**
	 * Get agent type
	 * @param agentType
	 * @return
	 */
	public static String getAgentType(String agentType) {
		if (EventUpdaterConstants.BASE.equalsIgnoreCase(agentType)) {
			return EventUpdaterConstants.B;
		} else if (EventUpdaterConstants.OVERRIDE.equalsIgnoreCase(agentType)) {
			return EventUpdaterConstants.O;
		} else {
			return agentType;
		}
	}
	/**
	 * Get agent validation status
	 * @param agntStatus
	 * @return
	 */
	public static String getAgentValidationStts(String agntStatus) {
		if (EventUpdaterConstants.TRUE.equalsIgnoreCase(agntStatus)) {
			return EventUpdaterConstants.P;
		} else if (EventUpdaterConstants.FALSE.equalsIgnoreCase(agntStatus)) {
			return EventUpdaterConstants.F;
		} else if (EventUpdaterConstants.NOT_APPLICABLE.equalsIgnoreCase(agntStatus)) {
			return EventUpdaterConstants.NOT_VALIDATED;
		} else {
			return agntStatus;
		}
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
}
