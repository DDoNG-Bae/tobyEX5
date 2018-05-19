package com.dasom.ex.user.service;

import com.dasom.ex.user.domain.User;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);
	void upgradeLevel(User user);
}
