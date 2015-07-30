package tools.ctd.logic;

import tools.ctd.dao.DAOFactoryManager;
import tools.ctd.dao.IDAOFactoryProvider;
import tools.ctd.dao.IUserDAO;
import tools.ctd.exception.CTDException;
import tools.ctd.vo.Project;

public class UserAccessor {
	
	public Project getDefaultProject() throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IUserDAO usrDAO = provider.getUserDAO();
		return usrDAO.getDefaultProject();
	}
	
	public void setDefaultProject(Project project) throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IUserDAO usrDAO = provider.getUserDAO();
		usrDAO.setDefaultProject(project);
	}

}
