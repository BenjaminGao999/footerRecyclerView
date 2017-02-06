package com.qingmu.footerrecyclerview.module;

import java.io.Serializable;
import java.util.List;

/**
 * Author:　Created by benjamin
 * DATE :  2017/1/24 16:19
 */

public class RecordData implements Serializable{


    /**
     * data : {"totalCount":21,"data":[{"id":132,"missionId":4,"receiverId":111,"status":"RUNNING","missionName":"测试任务2107","missionParentId":null,"userId":13567133062,"userName":"jsxiaoshunzi","channelIds":"1","beginTime":1483491600000,"endTime":1485388800000,"totalSnapshot":null,"content":"{\"TEXT\":\"我的测试任务\",\"TITLE\":\"我的任务\"}","missionType":"VIDEO","missionFilterId":16,"identityType":"SPONSOR","gtmCreated":"2017.01.24","recordType":1}]}
     * error : null
     * code : 200
     * success : true
     */

    public DataBeanX data;
    public Object error;
    public int code;
    public boolean success;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBeanX {
        /**
         * totalCount : 21
         * data : [{"id":132,"missionId":4,"receiverId":111,"status":"RUNNING","missionName":"测试任务2107","missionParentId":null,"userId":13567133062,"userName":"jsxiaoshunzi","channelIds":"1","beginTime":1483491600000,"endTime":1485388800000,"totalSnapshot":null,"content":"{\"TEXT\":\"我的测试任务\",\"TITLE\":\"我的任务\"}","missionType":"VIDEO","missionFilterId":16,"identityType":"SPONSOR","gtmCreated":"2017.01.24","recordType":1}]
         */

        public int totalCount;
        public List<DataBean> data;

        public DataBeanX() {
            super();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        public static class DataBean {
            /**
             * id : 132
             * missionId : 4
             * receiverId : 111
             * status : RUNNING
             * missionName : 测试任务2107
             * missionParentId : null
             * userId : 13567133062
             * userName : jsxiaoshunzi
             * channelIds : 1
             * beginTime : 1483491600000
             * endTime : 1485388800000
             * totalSnapshot : null
             * content : {"TEXT":"我的测试任务","TITLE":"我的任务"}
             * missionType : VIDEO
             * missionFilterId : 16
             * identityType : SPONSOR
             * gtmCreated : 2017.01.24
             * recordType : 1
             */

            public int id;
            public int missionId;
            public int receiverId;
            public String status;
            public String missionName;
            public Object missionParentId;
            public long userId;
            public String userName;
            public String channelIds;
            public long beginTime;
            public long endTime;
            public Object totalSnapshot;
            public String content;
            public String missionType;
            public int missionFilterId;
            public String identityType;
            public String gtmCreated;
            public int recordType;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMissionId() {
                return missionId;
            }

            public void setMissionId(int missionId) {
                this.missionId = missionId;
            }

            public int getReceiverId() {
                return receiverId;
            }

            public void setReceiverId(int receiverId) {
                this.receiverId = receiverId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMissionName() {
                return missionName;
            }

            public void setMissionName(String missionName) {
                this.missionName = missionName;
            }

            public Object getMissionParentId() {
                return missionParentId;
            }

            public void setMissionParentId(Object missionParentId) {
                this.missionParentId = missionParentId;
            }

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getChannelIds() {
                return channelIds;
            }

            public void setChannelIds(String channelIds) {
                this.channelIds = channelIds;
            }

            public long getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(long beginTime) {
                this.beginTime = beginTime;
            }

            public long getEndTime() {
                return endTime;
            }

            public void setEndTime(long endTime) {
                this.endTime = endTime;
            }

            public Object getTotalSnapshot() {
                return totalSnapshot;
            }

            public void setTotalSnapshot(Object totalSnapshot) {
                this.totalSnapshot = totalSnapshot;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getMissionType() {
                return missionType;
            }

            public void setMissionType(String missionType) {
                this.missionType = missionType;
            }

            public int getMissionFilterId() {
                return missionFilterId;
            }

            public void setMissionFilterId(int missionFilterId) {
                this.missionFilterId = missionFilterId;
            }

            public String getIdentityType() {
                return identityType;
            }

            public void setIdentityType(String identityType) {
                this.identityType = identityType;
            }

            public String getGtmCreated() {
                return gtmCreated;
            }

            public void setGtmCreated(String gtmCreated) {
                this.gtmCreated = gtmCreated;
            }

            public int getRecordType() {
                return recordType;
            }

            public void setRecordType(int recordType) {
                this.recordType = recordType;
            }
        }
    }
}
