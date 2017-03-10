package com.alvarodelaserna.socket.serverclient.core.users;

import com.alvarodelaserna.socket.serverclient.domains.users.User;
import java.util.List;

public interface Users {

	String getAvatar();

	String getUserId() throws UsersException;

	void storeUserId(String userID);

	String getEmail();

	void storeEmail(String email);

	User getUserData() throws UsersException;

	List<User> getUsers(String emailOrPhone) throws UsersException;

	void uploadDocument(String imagePath) throws UsersException;

	boolean isUploadDocument();

	void deleteUser() throws UsersException;
	
	String updateAndGetUserAvatar(String avatar) throws UsersException;

	boolean isItTheFirstTime();

	void setFirstTime(boolean firstTime);

	String getPhone();

	void storePhone(String phone);

	String getOcrPicture();

	String authorizeShare(String ticket, String item_enc_key) throws UsersException;
	
	void storeAutoLock(long expireMillis);

	long getAutoLock();

	void setItIsTheFirstItemEverAsFalse();

	boolean itIsTheFirstItemEver();
	
	String updateUserPhone(String phoneNumber) throws UsersException;

	void validatePhone(String ticket, String phoneNumber, String code) throws UsersException;

	void deleteOCRUploadingImage();
}
