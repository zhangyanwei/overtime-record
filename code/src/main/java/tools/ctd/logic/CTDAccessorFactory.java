package tools.ctd.logic;

import tools.ctd.aop.ExtendUtils;

public final class CTDAccessorFactory {
	
	public static ProjectAccessor getProjectAccessor() {
		return ExtendUtils.extend(ProjectAccessor.class);
	}
	
	public static RecordAccessor getRecordAccessor() {
		return ExtendUtils.extend(RecordAccessor.class);
	}
	
	public static UserAccessor getUserAccessor() {
		return ExtendUtils.extend(UserAccessor.class);
	}
	
}
