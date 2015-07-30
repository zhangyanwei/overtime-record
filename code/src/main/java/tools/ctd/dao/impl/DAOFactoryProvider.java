package tools.ctd.dao.impl;

import tools.ctd.aop.ExtendUtils;
import tools.ctd.dao.IDAOFactoryProvider;
import tools.ctd.dao.IProjectDAO;
import tools.ctd.dao.IRecordDAO;
import tools.ctd.dao.IUserDAO;

public class DAOFactoryProvider implements IDAOFactoryProvider {

	@Override
	public IProjectDAO getProjectDAO() {
		return ExtendUtils.extend(ProjectDAO.class);
	}

	@Override
	public IRecordDAO getRecordDAO() {
		return ExtendUtils.extend(RecordDAO.class);
	}

	@Override
	public IUserDAO getUserDAO() {
		return ExtendUtils.extend(UserDAO.class);
	}

}
