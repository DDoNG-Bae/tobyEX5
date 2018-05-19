package com.dasom.ex.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dasom.ex.user.dao.UserDao;
import com.dasom.ex.user.domain.Level;
import com.dasom.ex.user.domain.User;

import static com.dasom.ex.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.dasom.ex.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao; 
	
	List<User> users;
	
	@Before
	public void setUp() {
		users=Arrays.asList(
				new User("t1","ㅌ1","p1",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
				new User("t2","ㅌ2","p2",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
				new User("t3","ㅌ3","p3",Level.SILVER,MIN_RECOMMEND_FOR_GOLD-1,29),
				new User("t4","ㅌ4","p4",Level.SILVER,MIN_RECOMMEND_FOR_GOLD,30),
				new User("t5","ㅌ5","p5",Level.GOLD,100,Integer.MAX_VALUE)
				);
	}
	
	@Test
	public void upgradeLevel() {
		userDao.deleteAll();
		
		for(User user:users)
			userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), false);
		checkLevel(users.get(1), true);
		checkLevel(users.get(2), false);
		checkLevel(users.get(3), true);
		checkLevel(users.get(4), false);
	}
	
	public void checkLevel(User user,boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(),is(user.getLevel().nextLevel()));
		}
		else {
			assertThat(userUpdate.getLevel(),is(user.getLevel()));
		}
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
	
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(),is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(),is(Level.BASIC));
	}
}
