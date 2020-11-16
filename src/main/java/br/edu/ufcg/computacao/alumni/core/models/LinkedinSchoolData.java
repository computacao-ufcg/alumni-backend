package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LinkedinSchoolData {
    private String schoolUrl;
    private String schoolName;
    private Level degreeLevel;
    private CourseName courseName;
    private DateRange dateRange;
    private String description;

    public LinkedinSchoolData(String schoolUrl, String schoolName, String degreeLevel, String courseName,
                              DateRange dateRange, String description) {
        this.schoolUrl = schoolUrl;
        this.schoolName = schoolName;
        this.degreeLevel = mapLevel(degreeLevel);
        this.courseName = mapCourseName(courseName);
        this.dateRange = dateRange;
        this.description = description;
    }

    public String getSchoolUrl() {
        return schoolUrl;
    }

    public void setSchoolUrl(String schoolUrl) {
        this.schoolUrl = schoolUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Level getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(Level degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public CourseName getCourseName() {
        return courseName;
    }

    public void setCourseName(CourseName courseName) {
        this.courseName = courseName;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private CourseName mapCourseName(String courseName) {
        Set<String> computingNames = new HashSet<>(Arrays.asList(new String[] {"computing science", "computer science",
                            "computing_science", "computer_science", "computing", "computação", "ciência da computação",
                            "computacao", "ciencia da computacao", "ciencia_da_computacao"}));
        Set<String> dataProcessingNames = new HashSet<>(Arrays.asList(new String[] {"data processing",
                "processamento de dados", "data_processing", "processamento_de_dados"}));
        Set<String> informaticsNames = new HashSet<>(Arrays.asList(new String[] {"informatics", "informática",
                "informatica"}));
        String name = courseName.toLowerCase().trim();
        if (computingNames.contains(name)) {
            return CourseName.COMPUTING_SCIENCE;
        } else if (dataProcessingNames.contains(name)) {
            return CourseName.DATA_PROCESSING;
        } else if (informaticsNames.contains(name)) {
            return CourseName.INFORMATICS;
        } else {
            return CourseName.UNDEFINED;
        }
    }

    private Level mapLevel(String levelName) {
        Set<String> undergraduateNames = new HashSet<>(Arrays.asList(new String[] {"undergraduate", "bsc", "bs",
                "graduação", "graduacao", "bacharelado"}));
        Set<String> masterNames = new HashSet<>(Arrays.asList(new String[] {"master", "msc", "ms", "mestrado"}));
        Set<String> doctorateNames = new HashSet<>(Arrays.asList(new String[] {"doctorate", "phd", "doutorado", "dsc"}));
        String level = levelName.toLowerCase().trim();
        if (undergraduateNames.contains(level)) {
            return Level.UNDERGRADUATE;
        } else if (masterNames.contains(level)) {
            return Level.MASTER;
        } else if (doctorateNames.contains(level)) {
            return Level.DOCTORATE;
        } else {
            return Level.UNDEFINED;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedinSchoolData that = (LinkedinSchoolData) o;
        return getSchoolUrl().equals(that.getSchoolUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchoolUrl());
    }

    @Override
    public String toString() {
        return "LinkedinSchoolData{" +
                "schoolUrl='" + schoolUrl + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", degree='" + degreeLevel.toString() + '\'' +
                ", field='" + courseName.toString() + '\'' +
                ", dateRange='" + dateRange + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
