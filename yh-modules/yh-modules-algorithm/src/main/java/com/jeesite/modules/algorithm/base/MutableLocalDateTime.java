package com.jeesite.modules.algorithm.base;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.UUID;

/**
 * 可变的时间
 * 由于业务计算需求，计划时间会因为各种原因变化，而持有时间的对象或者集合在时间变化之后会因为LocalDateTime是一个不可变的对象而不能随之改变。失去了一般对象持有其引用指针就能够找到原对象的能力
 * 此类组合了LocalDateTime类，实现了时间变化后能够在持有其时间对象的集合能够观察到其变化的功能，而非失去其引用。
 */


public class MutableLocalDateTime implements Cloneable,Comparable<MutableLocalDateTime> {

    private LocalDateTime data;
    private String uuid;


    public static MutableLocalDateTime of(LocalDate date, LocalTime time) {

        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        return new MutableLocalDateTime(localDateTime);
    }

    public static MutableLocalDateTime of(int year, Month month, int dayOfMonth, int hour, int minute, int second)  {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
        return new MutableLocalDateTime(localDateTime);
    }

	public static MutableLocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second)  {
		LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
		return new MutableLocalDateTime(localDateTime);
	}



    public MutableLocalDateTime() {
    }

    public MutableLocalDateTime(LocalDateTime data){
        this.data = data;
        this.uuid = UUID.randomUUID().toString();
    }




    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this. data = data;
    }

    public void updateData(LocalDateTime data){
        this.data = data;
    }

    public MutableLocalDateTime plusSeconds(long seconds) {
        return new MutableLocalDateTime(this.data.plusSeconds(seconds));
    }

    public MutableLocalDateTime plusSecondsSelf(long seconds) {
        this.data = this.data.plusSeconds(seconds);
        return this;
    }

    public MutableLocalDateTime plusMinutes(long minutes){
        return new MutableLocalDateTime(this.data.plusMinutes(minutes));
    }

    public MutableLocalDateTime plusMinutesSelf(long minutes){
        this.data = this.data.plusMinutes(minutes);
        return this;
    }
    public MutableLocalDateTime minusMinutes(long minutes){
        return new MutableLocalDateTime(this.data.minusMinutes(minutes));
    }

    public MutableLocalDateTime minusMinutesSelf(long minutes){
        this.data = this.data.minusMinutes(minutes);
        return this;
    }
    public MutableLocalDateTime minusHours(long hours){
        return new MutableLocalDateTime(this.data.minusHours(hours));
    }

    public MutableLocalDateTime minusHoursSelf(long hours){
        this.data = this.data.minusHours(hours);
        return this;
    }



    public MutableLocalDateTime plusHours(long hours) {
        return new MutableLocalDateTime(this.data.plusHours(hours));
    }

    public MutableLocalDateTime plusHoursSelf(long hours){
        this.data = this.data.plusHours(hours);
        return this;
    }

    public LocalTime toLocalTime(){
        return this.data.toLocalTime();
    }
    public LocalDate toLocalDate(){
        return this.data.toLocalDate();
    }



    public boolean isAfter(LocalDateTime other){
        return this.data.isAfter(other);
    }
    public boolean isAfterEqual(LocalDateTime other){
        return !this.data.isBefore(other);
    }

    public boolean isBefore(LocalDateTime other){
        return this.data.isBefore(other);
    }
    public boolean isBeforeEqual(LocalDateTime other){
        return !this.data.isAfter(other);
    }

    public boolean isAfter(MutableLocalDateTime other){
        return this.data.isAfter(other.getData());
    }
    public boolean isAfterEqual(MutableLocalDateTime other){
        return !this.data.isBefore(other.getData());
    }

    public boolean isBefore(MutableLocalDateTime other){
        return this.data.isBefore(other.getData());
    }
    public boolean isBeforeEqual(MutableLocalDateTime other){
        return !this.data.isAfter(other.getData());
    }


    public MutableLocalDateTime withHour(int hour) {
        return new MutableLocalDateTime(this.data.withHour(hour));
    }
    public MutableLocalDateTime withHourSelf(int hour) {
        this.data = this.data.withHour(hour);
        return this;
    }


    public MutableLocalDateTime withMinute(int minute) {
        return new MutableLocalDateTime(this.data.withMinute(minute));
    }
    public MutableLocalDateTime withMinuteSelf(int minute) {
        this.data = this.data.withMinute(minute);
        return this;
    }


    public MutableLocalDateTime withSecond(int second) {
        return new MutableLocalDateTime(this.data.withSecond(second));
    }
    public MutableLocalDateTime withSecondSelf(int second) {
        this.data = this.data.withSecond(second);
        return this;
    }


    public MutableLocalDateTime withNano(int nano) {
        return new MutableLocalDateTime(this.data.withNano(nano));
    }

    public MutableLocalDateTime withTime(int hour,int minute,int second){
        return new MutableLocalDateTime(this.data.withHour(hour).withMinute(minute).withSecond(second));
    }

    public MutableLocalDateTime withTimeSelf(int hour,int minute,int second){
        this.data = this.data.withHour(hour).withMinute(minute).withSecond(second);
        return this;
    }

    public String getUuid() {
        return uuid;
    }



    public Object clone()throws CloneNotSupportedException {
        MutableLocalDateTime clone = (MutableLocalDateTime)super.clone();
        clone.data = this.data.plusSeconds(0l);
        clone.uuid = this.uuid;
        return clone;
    }

    //-----------------------------------------------------------------------
    /**
     *
     */
    @Override
    public boolean equals(Object obj) {
        boolean b = this == obj;
        if (b)return true;

        MutableLocalDateTime mutableLocalDateTime = (MutableLocalDateTime) obj;
        return this.uuid.equals(mutableLocalDateTime.getUuid());
    }

    //-----------------------------------------------------------------------
    /**
     * 使用localDateTime的hashCode方法
     */
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    //-----------------------------------------------------------------------
    /**
     * 使用localDateTime的toString方法
     */
    @Override
    public String toString() {
        return this.data.toString();
    }

    @Override
    public int compareTo(MutableLocalDateTime other) {
        return this.getData().compareTo(other.getData());
    }


}
