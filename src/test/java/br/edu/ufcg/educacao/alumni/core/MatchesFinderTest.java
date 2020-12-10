package br.edu.ufcg.educacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.MatchesFinder;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore("jdk.internal.reflect.*")
@PrepareForTest(MatchesFinder.class)
public class MatchesFinderTest {

	private MatchesFinder matchesFinder;

	@Before
	public void setUp() {
		this.matchesFinder = Mockito.spy(MatchesFinder.class);
	}
	
	@Test
	public void testMock() {
		UfcgAlumnusData fakeAlumnus = createFakeAlumnus();
		SchoolName fakeSchool = createFakeSchool();
		Map<String, Collection<LinkedinAlumnusData>> fakeMatches = createFakeMatches();

		Mockito.doReturn(fakeMatches).when(this.matchesFinder).findMatches(fakeAlumnus, fakeSchool);

		Assert.assertEquals(createFakeMatches(), this.matchesFinder.findMatches(fakeAlumnus, fakeSchool));
		Mockito.verify(this.matchesFinder, Mockito.times(1)).findMatches(Mockito.any(), Mockito.any());
	}

	@Test
	public void testOne() {

	}

	@Test
	public void testNullInputFromConfFile() {
		Mockito.doReturn(null).when(this.matchesFinder).findMatches(null, createFakeSchool());

		Assert.assertNull(this.matchesFinder.findMatches(null, createFakeSchool()));
		Mockito.verify(this.matchesFinder, Mockito.times(1)).findMatches(Mockito.any(), Mockito.any());
	}

	@Test
	public void testFindRealMatch() {
		UfcgAlumnusData hugoRaniere = getRealAlumnus();
		Map<String, Collection<String>> realMatches = getRealMatches();

		Mockito.doReturn(realMatches).when(this.matchesFinder).findMatches(hugoRaniere, Mockito.any());

		MatchesFinder realMatchesFinder = MatchesFinder.getInstance();
		List<LinkedinAlumnusData> matches = new ArrayList<>(realMatchesFinder.findMatches(hugoRaniere, new SchoolName(new String[]{}, new DateRange[]{})).get("60"));
		String linkedinUrl = matches.get(0).getLinkedinProfile();

		Assert.assertTrue(this.matchesFinder.findMatches(hugoRaniere, new SchoolName(new String[] {}, new DateRange[] {})).get("60").stream().map(LinkedinAlumnusData::getLinkedinProfile).collect(Collectors.toList()).contains(linkedinUrl));
	}

	private Map<String, Collection<String>> getRealMatches() {
		Map<String, Collection<String>> map = new HashMap<>();
		map.put("60", Arrays.asList("https://www.linkedin.com/in/hugoraniere"));
		return map;
	}

	private UfcgAlumnusData getRealAlumnus() {
		return new UfcgAlumnusData("100110002", "HUGO RANIERE DI ASSUNCAO BRASILINO", getRealDegrees());
	}

	private Degree[] getRealDegrees() {
		return new Degree[] {
				new Degree(CourseName.COMPUTING_SCIENCE, Level.UNDERGRADUATE, "2001.1", "2005.2")
		};
	}

	private UfcgAlumnusData createFakeAlumnus() {
		return new UfcgAlumnusData("fake registration", "fake name", new Degree[] {});
	}
	
	private SchoolName createFakeSchool() {
		return new SchoolName(new String[] { "fake school name "}, new DateRange[] {});
	}
	
	private LinkedinAlumnusData createFakeLinkedinAlumnus() {
		return new LinkedinAlumnusData("fake full name", "fake company", "fake description", "fake location", new LinkedinJobData[] {}, new LinkedinSchoolData[] {}, "fake email", "fake linkedin profile", "fake twitter id");
	}
	
	private Map<String, Collection<LinkedinAlumnusData>> createFakeMatches() {
		Map<String, Collection<LinkedinAlumnusData>> map = new HashMap<>();
		map.put("30", Arrays.asList(createFakeLinkedinAlumnus(), createFakeLinkedinAlumnus()));
		return map;
	}
}
