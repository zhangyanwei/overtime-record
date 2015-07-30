package tools.ctd.dao;

public interface IDAOFactoryProvider {
	
	IProjectDAO getProjectDAO();
	
	IRecordDAO getRecordDAO();
	
	IUserDAO getUserDAO();
	
}
