package com.example.bringit.model;

public class MemberModel {

    private String memberId;
    private String groupId;
    private String memberName;

    public MemberModel() {
    }

    public MemberModel(String memberId, String groupId, String memberName) {
        this.memberId = memberId;
        this.groupId = groupId;
        this.memberName = memberName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
