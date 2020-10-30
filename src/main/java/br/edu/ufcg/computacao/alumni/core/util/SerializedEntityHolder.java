package br.edu.ufcg.computacao.alumni.core.util;

public class SerializedEntityHolder<T> {
    private String className;
    private String payload;

    public SerializedEntityHolder(T instanceToSerialize) {
        this.className = instanceToSerialize.getClass().getName();
        this.payload = GsonHolder.getInstance().toJson(instanceToSerialize);
    }

    public T getSerializedEntity() throws ClassNotFoundException {
        return (T) GsonHolder.getInstance().fromJson(this.payload, Class.forName(this.className));
    }

    @Override
    public String toString() {
        return GsonHolder.getInstance().toJson(this, SerializedEntityHolder.class);
    }
}
