package com.monstertechno.moderndashbord.Data;

import com.monstertechno.moderndashbord.Model.TempInfor;

public class DataManager {
    private static DataManager instance;
    private com.monstertechno.moderndashbord.Model.TempInfor TempInfor;

    private DataManager() {
        // Khởi tạo đối tượng DataManager
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public TempInfor getTempInfor() {
        return TempInfor;
    }

    public void setTempInfor(TempInfor newData) {
        this.TempInfor = newData;
    }
}


