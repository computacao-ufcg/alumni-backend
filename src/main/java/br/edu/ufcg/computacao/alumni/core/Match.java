package br.edu.ufcg.computacao.alumni.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.Curso;
import br.edu.ufcg.computacao.alumni.core.models.Grau;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;

public class Match {

	public Match() {
	}

	private Curso getCurso(String field, String degree) {
		field = field.toUpperCase().trim();
		degree = degree.toUpperCase().trim();

		if ("".equals(field) || "".equals(degree)) {
			return null;
		}
		if (field.contains("COMPUTER") && (degree.contains("MASTER") || degree.contains("MSC"))) {
			return Curso.MESTRADO_EM_CIENCIA_DA_COMPUTACAO;
		}
		if (field.contains("COMPUTER")
				&& (degree.contains("BC") || degree.contains("BACHELOR") || degree.contains("BS"))) {
			return Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO;
		}
		if (field.contains("COMPUT") && degree.contains("PH")) {
			return Curso.DOUTORADO_EM_CIENCIA_DA_COMPUTACAO;
		}
		if (field.contains("INFORMAT") && (degree.contains("MASTER") || degree.contains("MSC"))) {
			return Curso.MESTRADO_EM_INFOMATICA;
		}
		if (field.contains("") && (degree.contains("BC") || degree.contains("BACHELOR") || degree.contains("BS"))) {
			return Curso.GRADUACAO_PROCESSAMENTO_DE_DADOS;
		}
		return null;
	}

	private int getScoreFromName(String alumniName, String linkedinName) {
		if (alumniName.equals(linkedinName))
			return 1;
		return 0;
	}

	private int getScoreFromCurso(List<Curso> alumniCursos, Curso linkedinCurso) {
		if (linkedinCurso == null)
			return 0;
		if (alumniCursos.contains(linkedinCurso))
			return 1;
		return 0;
	}

	private int getScoreFromSchoolUrl(String schoolUrl, String linkedinUrl) {
		if (linkedinUrl == null)
			return 0;
		if (schoolUrl.equals(linkedinUrl))
			return 1;
		return 0;
	}

	private int getScoreFromYear(List<String> alumniYears, String linkedinYear) {
		if (linkedinYear == null)
			return 0;
		if (alumniYears.contains(linkedinYear))
			return 1;
		return 0;
	}

	private <E> List<E> filterGraus(Grau[] graus, Function<Grau, E> f) {
		return Arrays.asList(graus).stream().map(f).collect(Collectors.toList());
	}

	public Collection<LinkedinAlumnusData> getMatches(UfcgAlumnusData alumni, String schoolUrl) throws Exception {
		Collection<LinkedinAlumnusData> linkedinProfilesList = LinkedinDataHolder.getInstance().getLinkedinAlumniData();
		Collection<LinkedinAlumnusData> selectedProfilesList = new LinkedList<>();

		String alumniName = alumni.getFullName();
		Grau[] alumniGraus = alumni.getGraus();

		List<String> alumniStartYears = filterGraus(alumniGraus, Grau::getSemestreIngresso);
		List<String> alumniEndYears = filterGraus(alumniGraus, Grau::getSemestreFormatura);
		List<Curso> alumniCursos = filterGraus(alumniGraus, Grau::getCurso);

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
				score += getScoreFromYear(alumniEndYears, linkedinEndYear);
				score += getScoreFromYear(alumniStartYears, linkedinStartYear);
				score += getScoreFromSchoolUrl(schoolUrl, linkedinUrl);
			}

			if (score >= 1) {
				selectedProfilesList.add(linkedinProfile);
			}
		});
		return selectedProfilesList;

	}

}
