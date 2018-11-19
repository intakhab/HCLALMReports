package com.app.alm.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.app.alm.dto.MailDto;
/***
 * 
 * @author intakhabalam.s@hcl.com
 * @see MailDto
 */
public class AppUtil {

	
	/***
	 * @param listOfItems {@link List}
	 * @param separator {@link String}
	 * @return {@link String}
	 */
	public static String concatenate(List<String> listOfItems, String separator) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> stit = listOfItems.iterator();

		while (stit.hasNext()) {
			sb.append(stit.next());
			if (stit.hasNext()) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}
	
	
	/**
	 * Checks if is collection empty.
	 * @param collection {@link Collection}
	 * @return {@link Boolean}
	 */
	private static boolean isCollectionEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if is object empty.
	 * @param object {@link Object}
	 * @return {@link Boolean}
	 */
	public static boolean isObjectEmpty(Object object) {
		if(object == null) return true;
		else if(object instanceof String) {
			if (((String)object).trim().length() == 0) {
				return true;
			}
		} else if(object instanceof Collection) {
			return isCollectionEmpty((Collection<?>)object);
		}
		return false;
	}
}
