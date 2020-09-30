package br.edu.ufcg.computacao.alumni.api.http.response;

public class PublicKey {
    private String publicKey;

    public PublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
