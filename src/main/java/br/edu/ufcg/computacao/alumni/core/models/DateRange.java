package br.edu.ufcg.computacao.alumni.core.models;

import br.edu.ufcg.computacao.alumni.constants.Messages;
import org.apache.log4j.Logger;

public class DateRange {
    private static Logger LOGGER = Logger.getLogger(DateRange.class);

    private String startMonth;
    private String startYear;
    private String endMonth;
    private String endYear;
    private boolean isCurrent;

    public DateRange(String range) {
        LOGGER.debug(String.format(Messages.RANGE_S, range));
        if (range == null || range.equals("")) {
            createEmptyDateRange();
        } else {
            parseDateRangeString(range);
        }
    }

    private void createEmptyDateRange() {
        this.startMonth = "";
        this.startYear = "";
        this.endMonth = "";
        this.endYear = "";
        this.isCurrent = false;
    }

    private void parseDateRangeString(String range) {
        int currentPosition = 0;
        this.isCurrent = false;
        if (Character.isDigit(range.charAt(currentPosition))) {
            this.startMonth = "";
            this.startYear = range.substring(currentPosition, currentPosition+4);
            currentPosition += 7;
        } else {
            this.startMonth = range.substring(currentPosition, currentPosition+3);
            currentPosition += 4;
            this.startYear = range.substring(currentPosition, currentPosition+4);
            currentPosition += 7;
        }
        if (currentPosition >= range.length()) {
            this.endMonth = "";
            this.endYear = "";
        } else {
            if (Character.isDigit(range.charAt(currentPosition))) {
                this.endMonth = "";
                this.endYear = range.substring(currentPosition, currentPosition + 4);
            } else {
                if (range.charAt(currentPosition) == 'P') {
                    this.endMonth = "";
                    this.endYear = "";
                    this.isCurrent = true;
                } else {
                    this.endMonth = range.substring(currentPosition, currentPosition + 3);
                    currentPosition += 4;
                    this.endYear = range.substring(currentPosition, currentPosition + 4);
                }
            }
        }
        LOGGER.debug(String.format(Messages.DATERANGE_S, this.toString()));
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "startMonth='" + startMonth + '\'' +
                ", startYear='" + startYear + '\'' +
                ", endMonth='" + endMonth + '\'' +
                ", endYear='" + endYear + '\'' +
                ", isCurrent=" + isCurrent +
                '}';
    }
}
