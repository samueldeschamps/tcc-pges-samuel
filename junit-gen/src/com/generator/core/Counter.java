package com.generator.core;

public class Counter implements Comparable<Counter> {

	private final long covered;
	private final long total;

	public Counter(long covered, long total) {
		this.covered = covered;
		this.total = total;
	}

	public Counter add(Counter augend) {
		return new Counter(this.covered + augend.covered, this.total + augend.total);
	}

	public long getCovered() {
		return covered;
	}

	public long getTotal() {
		return total;
	}
	
	public boolean isEmpty() {
		return total == 0L;
	}

	public double getRatio() {
		if (total == 0L) {
			return 1.0;
		} else {
			return (double) covered / (double) total;
		}
	}
	
	public boolean greaterThan(Counter other) {
		if (this.total == 0) {
			return false;
		}
		if (other.total == 0) {
			return true;
		}
		return this.getRatio() > other.getRatio();
	}

	@Override
	public int compareTo(Counter other) {
		if (this.total == 0L) {
			if (other.total == 0L) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (other.total == 0L) {
				return 1;
			} else {
				return Double.compare(this.getRatio(), other.getRatio());
			}
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(covered) + "/" + String.valueOf(total);
	}

}
