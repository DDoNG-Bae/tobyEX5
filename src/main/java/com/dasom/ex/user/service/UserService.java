package com.dasom.ex.user.service;

import java.util.List;

import com.dasom.ex.user.dao.UserDao;
import com.dasom.ex.user.domain.Level;
import com.dasom.ex.user.domain.User;

public class UserService {
	
	public static final int MIN_LOGCOUNT_FOR_SILVER=50;
	public static final int MIN_RECOMMEND_FOR_GOLD=30;
	
	UserDao userDao;
	UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	
	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for(User user:users) {
			if(userLevelUpgradePolicy.canUpgradeLevel(user)) {
				userLevelUpgradePolicy.upgradeLevel(user);
			}
		}
	}
	
	public void add(User user) {
		if(user.getLevel()==null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}
	
}
