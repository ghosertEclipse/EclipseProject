package com.loadtrend.web.mobile.dao;

import com.loadtrend.web.mobile.dao.model.UserRecorder;

public interface UserRecorderDAO {
    public UserRecorder getUserRecorder(String id);
    public void saveUserRecorder(UserRecorder userRecorder);
    public void removeUserRecorder(String id);
}
