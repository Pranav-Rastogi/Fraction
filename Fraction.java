package mathExt;

import java.math.BigInteger;
import java.util.Objects;

public class Fraction implements Comparable<Fraction> {
    private final BigInteger numerator;
    private final BigInteger denominator;

    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction HALF = new Fraction(1, 2);
    public static final Fraction QUARTER = new Fraction(1, 4);

    private String toStringResult;

    public Fraction(int numerator) {
        this(numerator, 1);
    }

    public Fraction(int numerator, int denominator) {
        this(new BigInteger(numerator + ""), new BigInteger(denominator + ""));
    }

    public Fraction(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO))
            throw new IllegalArgumentException("Denominator of a fraction can not be zero.");

        if (numerator.equals(BigInteger.ZERO)) {
            this.numerator = BigInteger.ZERO;
            this.denominator = BigInteger.ONE;

            return;
        }

        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        BigInteger gcd = numerator.gcd(denominator);

        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd);
        this.toStringResult = null;
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    @Override
    public int compareTo(Fraction a) {
        Fraction res = this.subtract(a);

        return res.numerator.compareTo(BigInteger.ZERO);
    }

    @Override
    public String toString() {
        if (toStringResult == null)
            toStringResult = this.numerator + (!this.denominator.equals(BigInteger.ONE) ? "/" + denominator : "");

        return toStringResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fraction fraction = (Fraction) o;
        return numerator.equals(fraction.numerator) &&
                denominator.equals(fraction.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    public Fraction negate() {
        return new Fraction(this.numerator.negate(), this.denominator);
    }

    public Fraction reciprocate() {
        if (this.numerator.equals(BigInteger.ZERO))
            throw new ArithmeticException("Can not reciprocate fraction with value " + this);

        return new Fraction(this.denominator, this.numerator);
    }

    public Fraction add(Fraction b) {
        BigInteger numerator = this.numerator.multiply(b.denominator).add(this.denominator.multiply(b.numerator));
        BigInteger denominator = this.denominator.multiply(b.denominator);

        return new Fraction(numerator, denominator);
    }

    public Fraction subtract(Fraction b) {
        return this.add(b.negate());
    }

    public Fraction multiply(Fraction b) {
        BigInteger numerator = this.numerator.multiply(b.numerator);
        BigInteger denominator = this.denominator.multiply(b.denominator);

        return new Fraction(numerator, denominator);
    }

    public Fraction divide(Fraction b) {
        return this.multiply(b.reciprocate());
    }

    public BigInteger floor() {
        BigInteger res = this.numerator.divide(this.denominator);
        if (this.numerator.compareTo(BigInteger.ZERO) >= 0)
            return res;

        return res.subtract(BigInteger.ONE);
    }

    public BigInteger ceil() {
        BigInteger[] res = this.numerator.divideAndRemainder(this.denominator);

        if (res[1].compareTo(BigInteger.ZERO) > 0)
            return res[0].add(BigInteger.ONE);

        return res[0];
    }

    public Fraction max(Fraction b) {
        if (this.compareTo(b) < 0)
            return b;

        return this;
    }

    public Fraction min(Fraction b) {
        if(this.compareTo(b) < 0)
            return b;

        return this;
    }
}
