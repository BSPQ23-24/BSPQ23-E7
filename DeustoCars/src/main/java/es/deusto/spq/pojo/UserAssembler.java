package es.deusto.spq.pojo;

import java.util.ArrayList;
import java.util.List;

import es.deusto.spq.server.jdo.User;

public class UserAssembler {
	
	// Singleton
	private static UserAssembler instance;
	
	private UserAssembler() { }
	
	public static UserAssembler getInstance() {
		if (instance == null) {
			instance = new UserAssembler();
		}
		
		return instance;
	}
	
    /**
     * Changes the User to UserData.
     * 
     * @param user The user JDO to change.
     * @return UserData with all the necessary information.
     */
	public UserData UserToData(User user) {
		UserData data = new UserData();		
		
		data.setLogin(user.getLogin());
		data.setPassword(user.getPassword());
		data.setIsAdmin(user.getIsAdmin());
		
		return data;
	}
	
	/**
     * Changes a User list to a UserData list.
     * 
     * @param users The user JDO list to change.
     * @return UserData list with all the necessary information.
     */
	public List<UserData> UserListToData(List<User> users){
		List<UserData> data = new ArrayList<UserData>();
    	for (User jdo : users) {
			data.add(UserAssembler.getInstance().UserToData(jdo));
		}
    	
    	return data;
	}
	
    /**
     * Changes the UserData to User.
     * 
     * @param user The user data to change.
     * @return User with all the necessary information.
     */
	public User UserDataToJDO(UserData user) {
		User jdo = new User();		
		
		jdo.setLogin(user.getLogin());
		jdo.setPassword(user.getPassword());
		jdo.setIsAdmin(user.getIsAdmin());
		
		return jdo;
	}
}
