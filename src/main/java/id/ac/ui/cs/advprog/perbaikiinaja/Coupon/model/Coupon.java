package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

public abstract class Coupon {

	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	private void use() {
		// TODO Auto-generated method stub	
	}

	public int getUsageCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setCode(String string) {
		// TODO Auto-generated method stub
	}

	public int getMaxUsage() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setMaxUsage(int i) {
		// TODO Auto-generated method stub
	}
	
	public double getDiscountValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setDiscountValue(double d) {
		// TODO Auto-generated method stub
	}
	
	public abstract double applyDiscount(double originalPrice);
}
