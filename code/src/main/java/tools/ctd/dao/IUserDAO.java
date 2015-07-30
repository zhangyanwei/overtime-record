package tools.ctd.dao;

import tools.ctd.exception.CTDException;
import tools.ctd.vo.Project;

public interface IUserDAO {
	
	Project getDefaultProject() throws CTDException;

	void setDefaultProject(Project project) throws CTDException;
}
