package br.edu.ufcg.computacao.alumni.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.Curso;
import br.edu.ufcg.computacao.alumni.core.models.Grau;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;

public class Match {
	
	private static Curso getCurso(String field, String degree) {
		field = field.toUpperCase().trim();
		degree = degree.toUpperCase().trim();
		
		if ("".equals(field) || "".equals(degree)) {
			return null;
		}
		
		if (degree.contains("MASTER") || degree.contains("MSC") && field.contains("COMPUTER")) {
			return Curso.MESTRADO_EM_CIENCIA_DA_COMPUTACAO;
		}
		if (degree.contains("MASTER") || degree.contains("MSC") && field.contains("INFORMAT")) {
			return Curso.MESTRADO_EM_INFOMATICA;
		}
		if (degree.contains("BC") || degree.contains("BACHELOR") || degree.contains("BS") && field.contains("COMPUTER")) {
			return Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO;
		}
		if (degree.contains("BC") || degree.contains("BACHELOR") || degree.contains("BS") && field.contains("")) {
			return Curso.GRADUACAO_PROCESSAMENTO_DE_DADOS;
		}
		if (degree.contains("PH") && field.contains("COMPUT")) {
			return Curso.DOUTORADO_EM_CIENCIA_DA_COMPUTACAO;
		}
		return null;
	}
	
	private static int getScoreFromName(String alumniName, String linkedinName) {
		if (alumniName.equals(linkedinName)) return 1;
		return 0;
	}
	
	private static int getScoreFromCurso(List<Curso> alumniCursos, Curso linkedinCurso) {
		if (linkedinCurso == null) return 0;
		if (alumniCursos.contains(linkedinCurso)) return 1;
		return 0;
	}
	
	private static int getScoreFromSchoolUrl(String schoolUrl, String linkedinUrl) {
		if (schoolUrl.equals(linkedinUrl)) return 1;
		return 0;
	}
	
	private static int getScoreFromStartYear(List<String> alumniStartYears, String linkedinStartYear) {
		if (alumniStartYears.contains(linkedinStartYear)) return 1;
		return 0;
	}
	
	private static int getScoreFromEndYear(List<String> alumniEndYear, String linkedinEndYear) {
		if (alumniEndYear.contains(linkedinEndYear)) return 1;
		return 0;
	}
	
	private static List<String> getEndYears(Grau[] graus) {
		return Arrays.asList(graus)
				.stream()
				.map(grau -> grau.getSemestreFormatura())
				.collect(Collectors.toList());
	}
	
	private static List<String> getStartYears(Grau[] graus) {
		return Arrays.asList(graus)
				.stream()
				.map(grau -> grau.getSemestreIngresso())
				.collect(Collectors.toList());
	}
	
	private static List<Curso> getAlumniCursos(Grau[] graus) {
		return Arrays.asList(graus)
				.stream()
				.map(grau -> grau.getCurso())
				.collect(Collectors.toList());
	}
	
	public static Collection<LinkedinAlumnusData> getMatches(UfcgAlumnusData alumni, String schoolUrl) {
		try {
			Collection<LinkedinAlumnusData> linkedinProfilesList = LinkedinDataHolder.getInstance().getLinkedinAlumniData();
			Collection<LinkedinAlumnusData> selectedProfilesList = new LinkedList<>();
			
			String alumniName = alumni.getFullName();
			Grau[] alumniGraus = alumni.getGraus();
			List<String> alumniStartYears = getStartYears(alumniGraus);
			List<String> alumniEndYears = getEndYears(alumniGraus);
			List<Curso> alumniCursos = getAlumniCursos(alumniGraus);
			
			linkedinProfilesList.forEach(linkedinProfile -> {
				int score = 0;
				
				LinkedinSchoolData[] schoolData = linkedinProfile.getSchools();
				String linkedinAlumniFullName = linkedinProfile.getFullName().toUpperCase();
				
				score += getScoreFromName(alumniName, linkedinAlumniFullName);
				
				for (LinkedinSchoolData school : schoolData) {
					String linkedinUrl = school.getSchoolUrl();
					String linkedinDegree = school.getDegree();
					String linkedinField = school.getField();
					String linkedinStartYear = school.getDateRange().getStartYear();
					String linkedinEndYear = school.getDateRange().getEndYear();
					Curso linkedinCurso = getCurso(linkedinField, linkedinDegree);
					
					score += getScoreFromCurso(alumniCursos, linkedinCurso);
					score += getScoreFromEndYear(alumniEndYears, linkedinEndYear);
					score += getScoreFromStartYear(alumniStartYears, linkedinStartYear);
					score += getScoreFromSchoolUrl(schoolUrl, linkedinUrl);
				}
				
				if (score >= 1) {
					selectedProfilesList.add(linkedinProfile);
				}
			});
			return selectedProfilesList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return new LinkedList<>();
		}
	}
	
}
