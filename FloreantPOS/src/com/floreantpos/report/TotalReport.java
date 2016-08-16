package com.floreantpos.report;

import java.util.Date;

public class TotalReport {
	private Date fromDate;
	private Date toDate;
	private Date reportTime;

	private long totalCash;
	private double totalCashPaidAmount;
	private double totalCashTips;
	private long totalCard;
	private double totalCardPaidAmount;
	private double totalCardTips;
	private long totalMenuLog;
	private double totalMenuLogPaidAmount;
	private double totalMenuLogTips;
	private long totalEatNow;
	private double totalEatNowPaidAmount;
	private double totalEatNowTips;
	private long totalDeliveryHero;
	private double totalDeliveryHeroPaidAmount;
	private double totalDeliveryHeroTips;
	private long totalTrans;
	private double totalPaidAmount;
	private double totalTips;
	
	public Date getFromDate() {
		return fromDate;
	}



	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}



	public Date getToDate() {
		return toDate;
	}



	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}



	public Date getReportTime() {
		return reportTime;
	}



	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}



	public long getTotalCash() {
		return totalCash;
	}



	public void setTotalCash(long totalCash) {
		this.totalCash = totalCash;
	}



	public double getTotalCashPaidAmount() {
		return totalCashPaidAmount;
	}



	public void setTotalCashPaidAmount(double totalCashPaidAmount) {
		this.totalCashPaidAmount = totalCashPaidAmount;
	}



	public long getTotalCard() {
		return totalCard;
	}



	public void setTotalCard(long totalCard) {
		this.totalCard = totalCard;
	}



	public double getTotalCardPaidAmount() {
		return totalCardPaidAmount;
	}



	public void setTotalCardPaidAmount(double totalCardPaidAmount) {
		this.totalCardPaidAmount = totalCardPaidAmount;
	}



	public long getTotalMenuLog() {
		return totalMenuLog;
	}



	public void setTotalMenuLog(long totalMenuLog) {
		this.totalMenuLog = totalMenuLog;
	}



	public double getTotalMenuLogPaidAmount() {
		return totalMenuLogPaidAmount;
	}



	public void setTotalMenuLogPaidAmount(double totalMenuLogPaidAmount) {
		this.totalMenuLogPaidAmount = totalMenuLogPaidAmount;
	}



	public long getTotalEatNow() {
		return totalEatNow;
	}



	public void setTotalEatNow(long totalEatNow) {
		this.totalEatNow = totalEatNow;
	}



	public double getTotalEatNowPaidAmount() {
		return totalEatNowPaidAmount;
	}



	public void setTotalEatNowPaidAmount(double totalEatNowPaidAmount) {
		this.totalEatNowPaidAmount = totalEatNowPaidAmount;
	}



	public long getTotalDeliveryHero() {
		return totalDeliveryHero;
	}



	public void setTotalDeliveryHero(long totalDeliveryHero) {
		this.totalDeliveryHero = totalDeliveryHero;
	}



	public double getTotalDeliveryHeroPaidAmount() {
		return totalDeliveryHeroPaidAmount;
	}



	public void setTotalDeliveryHeroPaidAmount(double totalDeliveryHeroPaidAmount) {
		this.totalDeliveryHeroPaidAmount = totalDeliveryHeroPaidAmount;
	}



	public long getTotalTrans() {
		return totalTrans;
	}



	public void setTotalTrans(long totalTrans) {
		this.totalTrans = totalTrans;
	}



	public double getTotalPaidAmount() {
		return totalPaidAmount;
	}



	public void setTotalPaidAmount(double totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public double getTotalCashTips() {
		return totalCashTips;
	}



	public void setTotalCashTips(double totalCashTips) {
		this.totalCashTips = totalCashTips;
	}



	public double getTotalCardTips() {
		return totalCardTips;
	}



	public void setTotalCardTips(double totalCardTips) {
		this.totalCardTips = totalCardTips;
	}



	public double getTotalMenuLogTips() {
		return totalMenuLogTips;
	}



	public void setTotalMenuLogTips(double totalMenuLogTips) {
		this.totalMenuLogTips = totalMenuLogTips;
	}



	public double getTotalEatNowTips() {
		return totalEatNowTips;
	}



	public void setTotalEatNowTips(double totalEatNowTips) {
		this.totalEatNowTips = totalEatNowTips;
	}



	public double getTotalDeliveryHeroTips() {
		return totalDeliveryHeroTips;
	}



	public void setTotalDeliveryHeroTips(double totalDeliveryHeroTips) {
		this.totalDeliveryHeroTips = totalDeliveryHeroTips;
	}



	public double getTotalTips() {
		return totalTips;
	}



	public void setTotalTips(double totalTips) {
		this.totalTips = totalTips;
	}



	public void calculate() {
		totalTrans = totalCash + totalCard + totalMenuLog + totalEatNow + totalDeliveryHero;
		
		totalPaidAmount = totalCashPaidAmount + totalCardPaidAmount + totalMenuLogPaidAmount + totalEatNowPaidAmount + totalDeliveryHeroPaidAmount;
		
		totalTips = totalCashTips + totalCardTips + totalMenuLogTips + totalEatNowTips + totalDeliveryHeroTips;
						
	}
}
