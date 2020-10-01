package br.edu.ufcg.computacao.alumni.api.http.response;

public class PublicKeyResponse {
    private String publicKey;

    public PublicKeyResponse(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
