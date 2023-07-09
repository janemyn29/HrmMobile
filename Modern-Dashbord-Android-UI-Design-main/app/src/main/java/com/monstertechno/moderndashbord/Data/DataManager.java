package com.monstertechno.moderndashbord.Data;

import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.util.List;

public class DataManager {
    private static DataManager instance;
    private com.monstertechno.moderndashbord.Model.TempInfor TempInfor;

    private List<DefaultModel> LeaveShift;

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

    public List<DefaultModel> getLeaveShift() {
        return LeaveShift;
    }

    public void setLeaveShift(List<DefaultModel> leaveShift) {
        LeaveShift = leaveShift;
    }
}


