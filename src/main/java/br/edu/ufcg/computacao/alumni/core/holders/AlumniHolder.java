package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.CodigoCurso;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.Match;
import br.edu.ufcg.computacao.alumni.core.models.Curso;
import br.edu.ufcg.computacao.alumni.core.models.Grau;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlumniHolder extends Thread {
    private Logger LOGGER = Logger.getLogger(AlumniHolder.class);
    public static final String PRIVATE_DIRECTORY = "private/";

    private static AlumniHolder instance;
    private long lastModificationDate;
    private UfcgAlumnusData[] alumni;

    private AlumniHolder() {
        this.lastModificationDate = 0;
    }

    public static AlumniHolder getInstance() {
        synchronized (AlumniHolder.class) {
            if (instance == null) {
                instance = new AlumniHolder();
            }
            return instance;
        }
    }

    public synchronized void loadAlumni(String filePath) throws IOException {
        if (!dataHasChanged(filePath)) {
            return;
        }
        List<UfcgAlumnusData> alumniList = new LinkedList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            String registration = data[0];
            String name = data[1];
            String codigoCurso = data[2];
            String semestreIngresso = data[3];
            String semestreFormatura = data[4];

            Grau grau = null;
            switch (codigoCurso) {
                case CodigoCurso.GRAD_PROC_DADOS:
                    grau = new Grau(Curso.GRADUACAO_PROCESSAMENTO_DE_DADOS, semestreIngresso, semestreFormatura);
                    break;
                case CodigoCurso.GRAD_COMPUTACAO:
                    grau = new Grau(Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO, semestreIngresso, semestreFormatura);
                    break;
                case CodigoCurso.MEST_INFORMATICA:
                    grau = new Grau(Curso.MESTRADO_EM_INFOMATICA, semestreIngresso, semestreFormatura);
                    break;
                case CodigoCurso.MEST_COMPUTACAO:
                    grau = new Grau(Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO, semestreIngresso, semestreFormatura);
                    break;
                case CodigoCurso.DOUT_COMPUTACAO:
                    grau = new Grau(Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO, semestreIngresso, semestreFormatura);
                    break;
                default:
                    LOGGER.error(String.format(Messages.INVALID_INPUT_S, row));
                    break;
            }
            Grau[] graus = new Grau[1];
            graus[0] = grau;
            UfcgAlumnusData alumnus = new UfcgAlumnusData(registration, name, graus);
            alumniList.add(alumnus);
        }
        csvReader.close();
        this.alumni = new UfcgAlumnusData[alumniList.size()];
        for(int i = 0; i < alumniList.size(); i++) {
            this.alumni[i] = alumniList.get(i);
            LOGGER.info(String.format(Messages.LOADING_ALUMNI_D_S, i, this.alumni[i].getFullName()));
        }
    }

    public synchronized Collection<UfcgAlumnusData> getAlumniData(String token) throws Exception {
        Collection<UfcgAlumnusData> alumniCollection = new LinkedList<>();

        for(int i = 0; i < this.alumni.length; i++) {
            alumniCollection.add(this.alumni[i]);
        }
        return alumniCollection;
    }

    public synchronized List<String> getAlumniNames(String token) throws Exception {
        List<String> alumniNames = new LinkedList<>();

        for(int i = 0; i < this.alumni.length; i++) {
            alumniNames.add(this.alumni[i].getFullName());
        }
        return alumniNames;
    }

    public List<CurrentJob> getAlumniCurrentJob(String token) throws Exception {
        List<CurrentJob> alumniCurrentJob = new LinkedList<>();

        for(int i = 0; i < this.alumni.length; i++) {
            String linkedinId = MatchesHolder.getInstance().getLinkedinId(this.alumni[i].getRegistration());
            CurrentJob current = LinkedinDataHolder.getInstance().getAlumnusCurrentJob(this.alumni[i].getFullName(),
                    linkedinId);
            if (!current.getCurrentJob().equals("bad match") && !current.getCurrentJob().equals("not available") &&
                                                                !current.getCurrentJob().equals("not matched")) {
                alumniCurrentJob.add(current);
            }
        }
        return alumniCurrentJob;
    }

    private boolean dataHasChanged(String filePath) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
        long currentDate = attr.lastModifiedTime().toMillis();
        if (currentDate > this.lastModificationDate) {
            this.lastModificationDate = currentDate;
            return true;
        } else {
            return false;
        }
    }

    /**
     * From time to time, updates Linkedin data with data recovered by the external scraping engine
     */
    @Override
    public void run() {
        boolean isActive = true;

        while (isActive) {
            try {
                this.loadAlumni(PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ALUMNI_INPUT_KEY));
                Thread.sleep(Long.parseLong(Long.toString(TimeUnit.SECONDS.toMillis(30))));
            } catch (InterruptedException e) {
                isActive = false;
                LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
            } catch (IOException e) {
                LOGGER.error(Messages.COULD_NOT_LOAD_ALUMNI_DATA, e);
            }
        }
    }
}
