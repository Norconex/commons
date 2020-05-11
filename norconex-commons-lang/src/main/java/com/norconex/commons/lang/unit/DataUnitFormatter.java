/* Copyright 2010-2020 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.commons.lang.unit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>
 * Formats a data unit to a human-readable string.
 * </p>
 * <p>
 * As of 2.0.0, a distinction is made between decimal (default) and binary
 * units.  Also, units are now supported up to yotta (1000<sup>8</sup>)
 * or yobi (1024<sup>8</sup>).
 * </p>
 *
 * @author Pascal Essiembre
 * @since 1.4.0
 */
public class DataUnitFormatter implements Serializable {

    private static final long serialVersionUID = -8672773710734223185L;

    private Locale locale;
    private int decimalPrecision;
    private boolean fixedUnit;
    private boolean binaryNotation;
    private RoundingMode roundingMode;

    /**
     * Creates a new DataUnit formatter using decimal notation,
     * with default system locale, and without decimals.
     */
    public DataUnitFormatter() {
        super();
    }

    /**
     * Copy constructor. Passing a <code>null</code> formatter is the same
     * as invoking {@link #DataUnitFormatter()} without arguments
     * @param formatter formatter to copy
     */
    public DataUnitFormatter(DataUnitFormatter formatter) {
        if (formatter != null) {
            this.locale = formatter.locale;
            this.decimalPrecision = formatter.decimalPrecision;
            this.fixedUnit = formatter.fixedUnit;
            this.binaryNotation = formatter.binaryNotation;
        }
    }

    public Locale getLocale() {
        return locale;
    }
    public DataUnitFormatter setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }
    public DataUnitFormatter withLocale(Locale locale) {
        return new DataUnitFormatter(this).setLocale(locale);
    }

    public int getDecimalPrecision() {
        return decimalPrecision;
    }
    public DataUnitFormatter setDecimalPrecision(int decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
        return this;
    }
    public DataUnitFormatter withDecimalPrecision(int decimalPrecision) {
        return new DataUnitFormatter(this)
                .setDecimalPrecision(decimalPrecision);
    }

    public boolean isFixedUnit() {
        return fixedUnit;
    }
    public DataUnitFormatter setFixedUnit(boolean fixedUnit) {
        this.fixedUnit = fixedUnit;
        return this;
    }
    public DataUnitFormatter withFixedUnit(boolean fixedUnit) {
        return new DataUnitFormatter(this).setFixedUnit(fixedUnit);
    }

    public boolean isBinaryNotation() {
        return binaryNotation;
    }
    public DataUnitFormatter setBinaryNotation(boolean binaryNotation) {
        this.binaryNotation = binaryNotation;
        return this;
    }
    public DataUnitFormatter withBinaryNotation(boolean binaryNotation) {
        return new DataUnitFormatter(this).setBinaryNotation(binaryNotation);
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }
    public DataUnitFormatter setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
        return this;
    }
    public DataUnitFormatter withRoundingMode(RoundingMode roundingMode) {
        return new DataUnitFormatter(this).setRoundingMode(roundingMode);
    }

    /**
     * Creates a new DataUnit formatter without decimals.
     * The decimal and thousand separator symbols are set according to
     * supplied locale.
     * @param locale a locale
     * @deprecated Since 2.0.0 use setter methods.
     */
    @Deprecated
    public DataUnitFormatter(Locale locale) {
        this(locale, 0);
    }
    /**
     * Creates a new DataUnit formatter with default system locale
     * with the maximum number of decimals returned when applicable.
     * @param decimalPrecision number of decimals
     * @deprecated Since 2.0.0 use setter methods.
     */
    @Deprecated
    public DataUnitFormatter(int decimalPrecision) {
        this(null, decimalPrecision);
    }
    /**
     * Creates a new DataUnit formatter with the maximum number of decimals
     * returned when applicable.
     * The decimal and thousand separator symbols are set according to
     * supplied locale.
     * @param locale a locale
     * @param decimalPrecision number of decimals
     * @deprecated Since 2.0.0 use setter methods.
     */
    @Deprecated
    public DataUnitFormatter(Locale locale, int decimalPrecision) {
        this(locale, decimalPrecision, false);
    }
    /**
     * Creates a new DataUnit formatter with the maximum number of decimals
     * returned when applicable.
     * The decimal and thousand separator symbols are set according to
     * supplied locale.
     * Set the <code>fixedUnit</code> to <code>true</code> to ensure
     * the data units supplied are not change in the formatting.
     * @param locale a locale
     * @param decimalPrecision number of decimals
     * @param fixedUnit <code>true</code> to keep original unit in formatting
     * @deprecated Since 2.0.0 use setter methods.
     */
    @Deprecated
    public DataUnitFormatter(
            Locale locale, int decimalPrecision, boolean fixedUnit) {
        super();
        this.locale = locale;
        this.decimalPrecision = decimalPrecision;
        this.fixedUnit = fixedUnit;
    }

    /**
     * Formats a data amount of the given unit to a human-readable
     * representation.
     * @param amount the amount to format
     * @param unit the data unit type of the amount
     * @return formatted string
     */
    public String format(double amount, DataUnit unit) {
        return format(BigDecimal.valueOf(amount), unit);
    }

    /**
     * Formats a data amount of the given unit to a human-readable
     * representation.
     * @param amount the amount to format
     * @param unit the data unit type of the amount
     * @return formatted string
     */
    public String format(BigDecimal amount, DataUnit unit) {
        Objects.requireNonNull(amount, "'amount' must not be null.");

        // If no unit specified, return as string without a suffix
        if (unit == null) {
            return doFormat(amount, null);
        }

        List<DataUnit> units = binaryNotation
                ? DataUnit.BINARY_BYTE_UNITS : DataUnit.DECIMAL_BYTE_UNITS;
        int kiloLength = (binaryNotation
                ? DataUnit.KIB : DataUnit.KB).bytes().intValue();
        int unitIndex = unit.getGroupIndex();

        // Use coarser unit if applicable to make value more human-readable
        DataUnit resolvedUnit = units.get(unitIndex);
        BigDecimal resolvedAmount =
                resolvedUnit == unit ? amount : resolvedUnit.from(amount, unit);
        if (!fixedUnit) {
            int unitShift = (int) (Math.log(resolvedAmount.doubleValue())
                    / Math.log(kiloLength));
            if (unitShift > 0) {
                DataUnit scaledUnit = units.get(Math.min(
                        unitIndex + unitShift, units.size() -1));
                resolvedAmount = scaledUnit.from(resolvedAmount, resolvedUnit);
                resolvedUnit = scaledUnit;
            }
        }

        return doFormat(resolvedAmount, resolvedUnit);
    }

    private String doFormat(BigDecimal value, DataUnit unit) {
        // Decimal precision
        value = value.setScale(
                decimalPrecision,
                Optional.ofNullable(roundingMode).orElse(RoundingMode.HALF_UP));

        // Locale format
        NumberFormat fmt = NumberFormat.getInstance(
                Optional.ofNullable(locale).orElse(Locale.ENGLISH));
        StringBuilder b = new StringBuilder();
        b.append(fmt.format(value).replace(' ', '\u00A0'));
        if (unit != null) {
            b.append('\u00A0').append(unit.getSymbol());
        }
        return b.toString();
    }

    @Override
    public boolean equals(final Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(
                this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
