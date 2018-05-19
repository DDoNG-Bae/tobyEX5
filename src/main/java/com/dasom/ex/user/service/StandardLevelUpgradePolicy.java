package com.dasom.ex.user.service;

import com.dasom.ex.user.dao.UserDao;
import com.dasom.ex.user.domain.Level;
import com.dasom.ex.user.domain.User;

import static com.dasom.ex.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.dasom.ex.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

public class StandardLevelUpgradePolicy implements UserLevelUpgradePolicy {
	
	UserDao userDao;
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch(currentLevel) {
			case BASIC : return (user.getLogin()>=MIN_LOGCOUNT_FOR_SILVER);
			case SILVER : return (user.getRecommend()>=MIN_RECOMMEND_FOR_GOLD);
			case GOLD : return false;
			default : throw new IllegalArgumentException("Unknown Level : "+currentLevel);
		}
	}

	@Override
	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}

}
