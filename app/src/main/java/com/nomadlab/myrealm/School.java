package com.nomadlab.myrealm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

// 테이블 클래스 정의
public class School extends RealmObject {
    //@Required // Name은 null이 될 수 없음
    @PrimaryKey
    private String name;
    private String location;

    public School() {} // 반드시 생성
    public School(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "School{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
