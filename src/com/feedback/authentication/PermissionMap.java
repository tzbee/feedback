package com.feedback.authentication;

import com.feedback.rest.UserAccountType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Static map linking account types with their respective permissions
 */
public class PermissionMap {
	public static final Multimap<UserAccountType, Permission> PERMISSION_MAP = ArrayListMultimap
			.create();
	static {
		PERMISSION_MAP.put(UserAccountType.USER, Permission.RATE);
		PERMISSION_MAP.put(UserAccountType.OWNER, Permission.CREATE);
		PERMISSION_MAP.put(UserAccountType.OWNER, Permission.CONFIGURE);
		PERMISSION_MAP.put(UserAccountType.OWNER, Permission.EDIT);
		PERMISSION_MAP.put(UserAccountType.OWNER, Permission.DELETE);
	}
}
